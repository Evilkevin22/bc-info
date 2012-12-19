package tk.wouterhabets.android.bcinfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class ActivityMain extends SlidingActivity {

	private int currentLevel;
	private final static String PREFERENCES_NAME = "mSharedPreferences";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//xml interface laden
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.activityslider_main);

		// currentLevel uit de SharedPreferences halen
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
		setLevel(settings.getInt("currentLevel", 0));

		// actionbar instellen
		ActionBar actionbar = getSupportActionBar();
		actionbar.setTitle("Level: " + getLevel());
		actionbar.setSubtitle("BC Info - Uitval");
		
		SlidingMenu menu = getSlidingMenu();
		
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.schaduw);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // menu.setMenu(R.layout.menu);
		
	}

	@Override
	protected void onStop() {
		// currentLevel opslaan in SharedPreferences
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("currentLevel", getLevel());
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
		}
		return super.onOptionsItemSelected(item);
	}

	public void setLevel(int level) {
		currentLevel = level;
	}

	public int getLevel() {
		return currentLevel;
	}

	private void refresh(int level) {
		
		//refresh thread, zorgt voor het ophalen van de uitval
		Thread refreshThread = new Thread() {
			public void run() {
				try {
					URL url1 = new URL("http://wouterhabets.tk/bcinfo.xml");
					Log.i("ActivityMain", "RSS feed downloaden van http://wouterhabets.tk/bcinfo.xml...");
					InputSource ic = new InputSource(url1.openStream());
					
					
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp;
					sp = spf.newSAXParser();
					XMLReader xr;
					xr = sp.getXMLReader();
					xr.setContentHandler(new RSSHandler());		
					
					Log.i("ActivityMain", "XMLReader starten...");
					xr.parse(ic);
					

				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
			}
		};
		refreshThread.start();
		
		
	}

}
