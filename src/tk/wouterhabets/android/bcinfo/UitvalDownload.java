package tk.wouterhabets.android.bcinfo;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class UitvalDownload {

	private final static String XML_URL = "http://wouterhabets.tk/bcinfo.xml";
	private FileOutputStream fos;

	public UitvalDownload(FileOutputStream ifos) {
		// openFileOutput("uitvalxml", Context.MODE_PRIVATE)
		fos = ifos;
	}

	public void getUitval() {
		try {
			new DefaultHttpClient().execute(new HttpGet(XML_URL)).getEntity()
					.writeTo(fos);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void close() {
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}