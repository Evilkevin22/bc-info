package tk.wouterhabets.android.bcinfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.util.Log;

public class UitvalDataStore {

	private BufferedWriter bw;
	private final static String DEBUG_TAG = "UitvalDataStore";

	public UitvalDataStore(OutputStreamWriter iosw) {
		// new OutputStreamWriter(openFileOutput(FILE_DATA,
		// Context.MODE_PRIVATE)
		bw = new BufferedWriter(iosw);
	}

	public void setUitvalData(String t, String d) {

	}

	public String getUitvalDataT(int level) {
		return null;
	}

	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "close() I/O exception");
		}
	}
}
