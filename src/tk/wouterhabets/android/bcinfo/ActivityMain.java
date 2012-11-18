package tk.wouterhabets.android.bcinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ActivityMain extends SherlockActivity {

	private int currentLevel;
	private final static String MENU_REFRESH = "Vernieuwen";
	private final static String MENU_SETTINGS = "Instellingen";
	private final static String MENU_ABOUT = "Over";
	private final static String PREFERENCES_NAME = "mSharedPreferences";

	TextView tvTitle, tvSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
						// refresh(itemPosition);
						
						return false;
					}
				});

		// 2 textviews van layout_main.xml inladen
		tvTitle = (TextView) findViewById(R.id.layout_main_level);
		tvSummary = (TextView) findViewById(R.id.layout_main_text);

		// currentLevel uit de SharedPreferences halen
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
		currentLevel = settings.getInt("currentLevel", 0);
	}

	@Override
	protected void onStop() {
		// currentLevel opslaan in SharedPreferences
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("currentLevel", currentLevel);
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
			Toast.makeText(getApplicationContext(), MENU_REFRESH,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_main_settings_item:
			Toast.makeText(getApplicationContext(), MENU_SETTINGS,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_main_about_item:
			Toast.makeText(getApplicationContext(), MENU_ABOUT,
					Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh(int level) {
		// placeholder voor vernieuwen
	}

}
