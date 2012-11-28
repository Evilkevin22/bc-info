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
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ActivityMain extends SherlockActivity {

	private int currentLevel;
	private final static String PREFERENCES_NAME = "mSharedPreferences";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// currentLevel uit de SharedPreferences halen
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
		setLevel(settings.getInt("currentLevel", 0));

		// actionbar met spinner instellen
		ActionBar actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.spinner1,
						android.R.layout.simple_spinner_dropdown_item);
		actionbar.setListNavigationCallbacks(mSpinnerAdapter,
				new ActionBar.OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						setLevel(itemPosition);
						refresh(itemPosition);

						return false;
					}
				});
		actionbar.setSelectedNavigationItem(getLevel());
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
		Thread refreshThread = new Thread() {
			public void run() {
				try {
					URL url1 = new URL("http://86.94.58.174/rssext.xml");
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp;
					sp = spf.newSAXParser();
					XMLReader xr;
					xr = sp.getXMLReader();
					xr.setContentHandler(new RSSHandler());

					InputSource ic = new InputSource(url1.openStream());
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
