package restAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

public class BambooRESTApi {

//	private static String URI = "https://dev.mi.med.uni-goettingen.de/bamboo/rest/api/latest/result/IDRT-IDRT2/";
	public static String getLatestBuild(String URI)  {
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


			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "/results/results/result[1]/planResultKey/resultNumber";
			InputSource source1 = new InputSource(new StringReader(output));
			String msg = xpath.evaluate(expression, source1);
			conn.disconnect();	
			return msg;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
