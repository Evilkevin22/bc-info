package tk.wouterhabets.android.bcinfo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ActivityMain extends Activity {

	private String currentLevel;
	private final static String NET_URL = "http://www.wouterhabets.tk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startWebView();
	}

	private void startWebView() {
		WebView webview = new WebView(this);
		setContentView(webview);
		webview.loadUrl(NET_URL);
	}
}
