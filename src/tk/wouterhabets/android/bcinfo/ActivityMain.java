package tk.wouterhabets.android.bcinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private final static String FILE_XML = "uitvalxml";
	private final static String FILE_DATA = "uitvaldata";
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
		refresh(getLevel());

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
		editor.putInt("currentLevel", getLevel());
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
		}
		return super.onOptionsItemSelected(item);
	}

	public void setLevel(int level) {
		currentLevel = level;
		String items[] = getResources().getStringArray(R.array.spinner1);
		TextView tv = (TextView) findViewById(R.id.layout_main_level);
		tv.setText(items[level]);
	}

	public int getLevel() {
		return currentLevel;
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

		Log.i("RefreshMethod", "Thread maken");
		// refresh thread, zorgt voor het ophalen van de uitval
		Thread refreshThread = new Thread() {
			public void run() {
				try {
					Log.i("BCInfo thread", "Uitval downloader instellen");
					UitvalDownload downloader = new UitvalDownload(
							openFileOutput(FILE_XML, Context.MODE_PRIVATE),
							(new OutputStreamWriter(openFileOutput(
									FILE_DATA, Context.MODE_WORLD_WRITEABLE))));
					downloader.getUitvalFromServer();
					downloader.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					Log.e("BCInfo thread", "Geen betand gevonden");
				}

				// xml reader

				try {
					InputSource ic;
					ic = new InputSource(openFileInput("uitvalxml"));
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp;
					sp = spf.newSAXParser();
					XMLReader xr;
					xr = sp.getXMLReader();
					xr.setContentHandler(new RSSHandler());

					Log.i("BCInfo thread", "XMLReader starten...");
					xr.parse(ic);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};
		refreshThread.start();

	}
}
