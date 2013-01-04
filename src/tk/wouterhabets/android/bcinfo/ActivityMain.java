package tk.wouterhabets.android.bcinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class ActivityMain extends SlidingActivity implements
		OnItemClickListener {

	private int currentLevel;
	private final static String PREFERENCES_NAME = "mSharedPreferences";
	// private final static String FILE_XML = "uitvalxml";
	// private final static String FILE_DATA = "uitvaldata";
	private final static String DEBUG_TAG = "BCInfo - ActivityMain";
	private String netWebURL;

	private WebView webview;
	private TextView levelTitle, levelDescription, firstRunTextView;
	private ActionBar actionbar;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// xml interface laden
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.activityslider_main);

		// overige view shit instellen
		webview = (WebView) findViewById(R.id.webview1);
		levelTitle = (TextView) findViewById(R.id.layout_main_level);
		levelDescription = (TextView) findViewById(R.id.layout_main_text);
		netWebURL = getResources().getString(R.string.netweb);

		// actionbar instellen
		actionbar = getSupportActionBar();
		actionbar.setSubtitle("Uitval");
		actionbar.setTitle("BC Info");
		actionbar.setDisplayHomeAsUpEnabled(true);

		// currentLevel uit de SharedPreferences halen
		settings = getSharedPreferences(PREFERENCES_NAME, 0);
		setLevel(settings.getInt("currentLevel", 0));
		refresh(currentLevel);

		// indien eerste keer gestart, help tekst weergeven
		if (settings.getBoolean("firstRun", true)) {
			firstRunTextView = (TextView) findViewById(R.id.textView_firstrun);
			firstRunTextView.setVisibility(View.VISIBLE);
		}

		// slidingmenu instellen
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.schaduw);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		ListView lv = (ListView) findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				getResources().getStringArray(R.array.spinner1));
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		setLevel(position);
		refresh(position);
		toggle();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onStop() {
		// currentLevel opslaan in SharedPreferences
		// en dat de app al een keer is gestart
		editor = settings.edit();
		editor.putInt("currentLevel", currentLevel);
		editor.putBoolean("firstRun", false);
		editor.commit();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu op actionbar/menutoets instellen
		MenuInflater mMenuInflater = getSupportMenuInflater();
		mMenuInflater.inflate(R.menu.activity_main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// controleert welk menu item is geselecteerd en voert code uit
		switch (item.getItemId()) {
		case R.id.menu_main_refresh_item:
			refresh(currentLevel);
			break;
		case R.id.menu_main_settings_item:
			Intent mIntentSettings = new Intent(
					"tk.wouterhabets.android.bcinfo.PREFERENCESMAIN");
			startActivity(mIntentSettings);
			break;
		case R.id.menu_main_about_item:
			Intent mIntentAbout = new Intent(
					"tk.wouterhabets.android.bcinfo.ACTIVITYABOUT");
			startActivity(mIntentAbout);
			break;
		case android.R.id.home:
			toggle();
			break;
		case R.id.menu_main_download_item:
			// DownloadThread downloadThread = new DownloadThread(this);
			// downloadThread.run();
			new UitvalDownload().execute();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setLevel(int level) {
		currentLevel = level;
		String items[] = getResources().getStringArray(R.array.spinner1);
		TextView tv = (TextView) findViewById(R.id.layout_main_level);
		tv.setText(items[level]);
	}

	private void refresh(int level) {

		if (level == 0) {
			webview.loadUrl(netWebURL);
			webview.setWebViewClient(new WebViewClient());
			webview.setVisibility(View.VISIBLE);

			levelTitle
					.setText(getResources().getStringArray(R.array.spinner1)[level]);
			levelDescription.setVisibility(View.GONE);
		} else {
			webview.setVisibility(View.GONE);
			levelTitle
					.setText(getResources().getStringArray(R.array.spinner1)[level]);
			levelDescription.setVisibility(View.VISIBLE);
		}

		if (level >= 2) {
			try {
				InputSource ic;
				ic = new InputSource(openFileInput("uitvalxml"));
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp;
				sp = spf.newSAXParser();
				XMLReader xr;
				xr = sp.getXMLReader();
				xr.setContentHandler(new RSSHandler());
				xr.parse(ic);
			} catch (FileNotFoundException e) {
				Log.e(DEBUG_TAG, "Refresh - FileNotFoundException");
			} catch (ParserConfigurationException e) {
				Log.e(DEBUG_TAG, "Refresh - ParserConfigurationException");
			} catch (SAXException e) {
				Log.e(DEBUG_TAG, "Refresh - SAXException");
			} catch (IOException e) {
				Log.e(DEBUG_TAG, "Refresh - IOException");
				// e.printStackTrace();
			}
		}

	}

	private class UitvalDownload extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			final String FILE_XML = "uitvalxml";
			final String XML_URL = "http://wouterhabets.tk/bcinfo.xml";
			final String DEBUG_TAG = "UitvalDownload";
			FileOutputStream fos = null;

			try {
				fos = openFileOutput(FILE_XML, Context.MODE_PRIVATE);
			} catch (FileNotFoundException e) {
				Log.e(DEBUG_TAG, "DownloadThread() FileNotFoundException");
			}

			if (fos != null) {
				try {
					new DefaultHttpClient().execute(new HttpGet(XML_URL))
							.getEntity().writeTo(fos);
				} catch (ClientProtocolException e1) {
					Log.e(DEBUG_TAG,
							"getUitvalFromServer() ClientProtocolException");
				} catch (IOException e1) {
					Log.e(DEBUG_TAG, "getUitvalFromServer() I/O exception");
				}

				try {
					fos.close();
				} catch (IOException e) {
					Log.e(DEBUG_TAG, "close() IOException");
				}
			} else {
				Log.e(DEBUG_TAG, "FileOutputSteam = null");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.d(DEBUG_TAG, "onPostExe");
			File file1 = getBaseContext().getFileStreamPath("uitvalxml");
			if (file1.exists()) {
				Log.d(DEBUG_TAG, "Bestand bestaat");
			} else {
				Log.d(DEBUG_TAG, "Bestand bestaat niet");
			}

			super.onPostExecute(result);
		}

	}

	private class RSSHandler extends DefaultHandler {

		StringBuffer chars = new StringBuffer();
		//private String levelStrings[] = {null, null, "Havo 2", "Havo 3", "Havo BOVENBOUW"};
		//private int level;
		private boolean getIt = false;

		//public RSSHandler(int inlevel, TextView tv1, TextView tv2) {
		//	level = inlevel;
		//}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) {

			chars = new StringBuffer();
			if (localName.equalsIgnoreCase("item")) {
				
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			if (localName.equalsIgnoreCase("title")) {
				Log.d("RSSHandler", "Title gevonden: " + chars.toString());
				if (chars.toString() == "Havo BOVENBOUW") {
					getIt = true;
					Log.i(DEBUG_TAG, "Gevonden!!!!!!!");
				}

			}

			if (localName.equalsIgnoreCase("description")) {
				Log.d("RSSHandler", "Description gevonden: " + chars.toString());
				if (getIt) {
					Log.i("RSSHandler", "GEVONDEN: " + chars.toString());
					getIt = false;
				}

			}

			if (localName.equalsIgnoreCase("item")) {
				Log.d("RSSHandler", "Item gevonden.");

			}

		}

		@Override
		public void characters(char ch[], int start, int length) {
			chars.append(new String(ch, start, length));
		}

	}
}
