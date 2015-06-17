package restAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.jface.dialogs.MessageDialog;
import org.xml.sax.InputSource;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.importtool.misc.FileHandler;

public class BambooRESTApi {
	
	final static String BAMBOOURI = "https://dev.mi.med.uni-goettingen.de/bamboo/rest/api/latest/result/IDRT-IDRT2/?expand=results.result.artifacts";
	final static String FIND_REPOSITORY_Expression = "/results/results/result[1]/artifacts/artifact[name='IMT']/link/@href";
	final static String FIND_LATEST_BUILD_Expression = "/results/results/result[1]/buildNumber";
	final static String BUILD_COMPLETED_TIME_Expression = "/results/results/result[1]/buildCompletedTime";
	public static void checkForUpdates() {
		checkForUpdates(false);
	}
	
	public static void checkForUpdates(boolean fromMenu) {
		try {
			int version = 0;
			try {
				version = Integer.parseInt(Activator.getDefault().getBundle().getVersion().getQualifier());

			}catch (Exception e){
				System.err.println("VERSION IS NO INTEGER");
				version = 0;
			}

//			System.out.println("CURRENTVERSION: "+ version);
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			final Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));


			String URI = defaultProps.getProperty("bambooURI",BAMBOOURI);

			IMTUpdate latest = BambooRESTApi.getLatestBuild(URI);
			String URIBuild = latest.getArtifactURI();
//			System.out.println("LATEST VERSION: " + latest.getBuildNumber());
			if (version < latest.getBuildNumber() && version!=0){
//				System.out.println("OLD VERSION!");
				boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Newer Version", "There is a newer Version available.\nOpen Download Page in Browser?");

				if (confirm){
					if(java.awt.Desktop.isDesktopSupported() ) {
						java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
						if(desktop.isSupported(java.awt.Desktop.Action.BROWSE) ) {
							java.net.URI uri;
							uri = new java.net.URI(URIBuild);
							desktop.browse(uri);
						}
					}
				}
			}
			else {
				if (fromMenu)
					MessageDialog.openInformation(Application.getShell(), "Version up-to-date", "Your version: " + version +"\nServer version: "
							+ latest.getBuildNumber()+", Build Time: " + latest.getBuildTimeString());
				System.out.println("No newer Version online");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static IMTUpdate getLatestBuild(String URI)  {
		try {
			URL url = new URL(URI);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(500);
			conn.setRequestProperty("Accept", "application/xml");

			if (conn.getResponseCode() != 200) {
				System.err.println("Error in BambooRESTApi");
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output = br.readLine();

//			System.out.println(output);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			InputSource source1 = new InputSource(new StringReader(output));
			InputSource source2 = new InputSource(new StringReader(output));
			InputSource source3 = new InputSource(new StringReader(output));
			
			String repo = xpath.evaluate(FIND_REPOSITORY_Expression, source1);
			String buildNumber = xpath.evaluate(FIND_LATEST_BUILD_Expression, source2);
			String buildTime = xpath.evaluate(BUILD_COMPLETED_TIME_Expression, source3);
//			System.out.println("BUILD: " + buildNumber + " REPO: " + repo);
			
			IMTUpdate update = new IMTUpdate();
			update.setArtifactURI(repo);
			update.setBuildNumber(Integer.parseInt(buildNumber));
			update.setBuildTimeString(buildTime);
			conn.disconnect();
			
			return update;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;

	}
}
