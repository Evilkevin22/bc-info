package tk.wouterhabets.android.bcinfo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

public class UitvalDownload {

	private final static String XML_URL = "http://wouterhabets.tk/bcinfo.xml";
	private FileOutputStream fos;
	private BufferedWriter bw;

	public UitvalDownload(FileOutputStream ifos, OutputStreamWriter iosw) {
		// openFileOutput("uitvalxml", Context.MODE_PRIVATE)
		fos = ifos;
		bw = new BufferedWriter(iosw);
	}

	public void getUitvalFromServer() {
		try {
			new DefaultHttpClient().execute(new HttpGet(XML_URL)).getEntity()
					.writeTo(fos);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setUitvalBestand(String t, String d) {

	}

	public void close() {
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}