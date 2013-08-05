package routines;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;

public class PIDGen{

	private static URL pidUrl;
	
	private static HashMap<String, String> PIDMap;
	public PIDGen(String URL) {
//		URL pidUrl = new URL("https://st03.mi.med.uni-goettingen.de/psx/cgi-psx/psx-soap.cgi?ctx=req&sts=1");
		try {
			pidUrl = new URL(URL);
		} catch (MalformedURLException e) {
			StatusListener.error("PIDGen Failed: Malformed URL","PIDGen Failed!" , "");
			e.printStackTrace();
		}
		setPIDMap(new HashMap<String, String>());
	}
	public static void setPIDMap(HashMap<String, String> pIDMap) {
		PIDMap = pIDMap;
	}
	public static HashMap<String, String> getPIDMap() {
		return PIDMap;
	}

	public String getPid(String meldedatum, String vorname, String nachname, String geburtsdatum, String geschlecht) throws Exception
	{
		//some SSL hacks
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager2()}, new SecureRandom());
		SSLContext.setDefault(ctx);
		//Notwendige ï¿½bergabe Parameter
		String data = "fld_Sicherheit=&";
		data+="fld_Meldedatum=" + meldedatum + "&";
		data+="fld_Meldeeinrichtung=abc&";
		data+="fld_ArztID=abc&";
		data+="fld_KrankenkassenNummer=&";
		data+="fld_VersichertenNummer1=&";
		data+="fld_VersichertenNummer2=&";
		data+="fld_Geburtsdatum=" + geburtsdatum + "&";
		data+="fld_Titel=&";
		data+="fld_Vorname=" + vorname + "&";
		data+="fld_Namenszusatz=&";
		data+="fld_Familienname=" + nachname + "&";
		data+="fld_Geschlecht=" + geschlecht + "&";
		data+="fld_Geburtsname=&";
		data+="fld_WohnortPLZ=&";
		data+="fld_WohnortName=&";
		data+="fld_Strasse_Hausnummer=&";
		data+="fld_TelefonNr=&";
		data+="fld_FaxNr=&";
		data+="fld_eMail=&";
		data+="fld_GeburtsortPLZ=&";
		data+="fld_GeburtsortName=&";
		data+="fld_GeburtsWohnortPLZ=&";
		data+="fld_GeburtsWohnortName=&";
		data+="fld_KontaktTitel=&";
		data+="fld_KontaktVorname=&";
		data+="fld_KontaktNamenszusatz=&";
		data+="fld_KontaktFamilienname=&";
		data+="fld_KontaktWohnortPLZ=&";
		data+="fld_KontaktWohnortName=&";
		data+="fld_KontaktStrasse_Hausnummer=&";
		data+="fld_KontaktTelefonNr=&";
		data+="fld_KontaktFaxNr=&";
		data+="fld_KontakteMail=&";
		data+="fld_Studienteilnahme=&";
		data+="cmd=Send";

		//Aufbau der URL Connection 
		//HTTP
		if (pidUrl.toString().startsWith("http:")){
		HttpURLConnection con = (HttpURLConnection)  pidUrl.openConnection();        
		con.setDoOutput( true );
		con.setRequestMethod("POST");

		//Form-Daten an den PIDGenerator ï¿½bertragen
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(data);
		out.flush();

		//Lesen der Antwort
		BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line= "";
		String all = "";

		while ((line = rd.readLine()) != null)
		{
			all+=line;

			//StringTokenizer linetok = new StringTokenizer(line,":");
			//System.out.println("CODE:"+ linetok.nextToken());

			//if(linetok.hasMoreTokens())
			//System.out.println("PID:"+linetok.nextToken());

			//if(linetok.hasMoreTokens())
			//System.out.println("Message:"+linetok.nextToken());
		}
		//System.out.println(all);


		rd.close();
		out.close();
		con.disconnect();
		return all;
		}
		//HTTPS
		else {
			HttpsURLConnection con = (HttpsURLConnection)  pidUrl.openConnection();        
			con.setDoOutput( true );
			con.setRequestMethod("POST");

			//Form-Daten an den PIDGenerator ï¿½bertragen
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(data);
			out.flush();

			//Lesen der Antwort
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line= "";
			String all = "";

			while ((line = rd.readLine()) != null)
			{
				all+=line;

				//StringTokenizer linetok = new StringTokenizer(line,":");
				//System.out.println("CODE:"+ linetok.nextToken());

				//if(linetok.hasMoreTokens())
				//System.out.println("PID:"+linetok.nextToken());

				//if(linetok.hasMoreTokens())
				//System.out.println("Message:"+linetok.nextToken());
			}
			//System.out.println(all);


			rd.close();
			out.close();
			con.disconnect();
			return all;
		}
	}


}
class DefaultTrustManager2 implements X509TrustManager
{
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
	{
	}


	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
	{
	}


	public X509Certificate[] getAcceptedIssuers()
	{
		return null;
	}
}
