package tk.wouterhabets.android.bcinfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

public class DownloadThread implements Runnable {

	private final static String FILE_XML = "uitvalxml";
	private final static String XML_URL = "http://wouterhabets.tk/bcinfo.xml";
	private final static String DEBUG_TAG = "UitvalDownload";
	private FileOutputStream fos;
	private Context context;

	public DownloadThread(Context c) {
		context = c;
		try {
			fos = context.openFileOutput(FILE_XML, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			Log.e(DEBUG_TAG, "DownloadThread() FileNotFoundException");
		}
	}

	@Override
	public void run() {
		Log.d(DEBUG_TAG, "Downloader starten...");
		getUitvalFromServer();
		close();
	}
	
	private void getUitvalFromServer() {
		try {
			new DefaultHttpClient().execute(new HttpGet(XML_URL))
					.getEntity().writeTo(fos);
		} catch (ClientProtocolException e1) {
			Log.e(DEBUG_TAG,
					"getUitvalFromServer() ClientProtocolException");
		} catch (IOException e1) {
			Log.e(DEBUG_TAG, "getUitvalFromServer() I/O exception");
		}
	}
	
	private void close() {
		try {
			fos.close();
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "close() I/O exception");
		}

	}
}
