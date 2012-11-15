package tk.wouterhabets.android.bcinfo;

import android.os.Bundle;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;

public class ActivityMain extends SherlockActivity {
	
	private final static String NET_URL = "http://www.wouterhabets.tk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setWebView();
	}

	private void setWebView() {
		WebView webview = (WebView) findViewById(R.id.webView1);
		webview.loadUrl(NET_URL);
	}
		
}
