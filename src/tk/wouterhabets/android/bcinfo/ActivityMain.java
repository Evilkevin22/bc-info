package tk.wouterhabets.android.bcinfo;

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

	TextView tvTitle, tvSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setTitle(R.string.title_uitval);
		actionbar.setSubtitle(getSubtitle());
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.spinner1,
						android.R.layout.simple_spinner_dropdown_item);
		actionbar.setListNavigationCallbacks(mSpinnerAdapter,
				new ActionBar.OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						// PLACEHOLDER
						Toast.makeText(getApplicationContext(), itemPosition,
								Toast.LENGTH_SHORT).show();
						return false;
					}
				});

		tvTitle = (TextView) findViewById(R.id.layout_main_level);
		tvSummary = (TextView) findViewById(R.id.layout_main_text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mMenuInflater = getSupportMenuInflater();
		mMenuInflater.inflate(R.menu.activity_main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		case android.R.id.home:
			// Code voor app icoon button hier invullen
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh(int level) {

	}

	private int getSubtitle() {
		switch (currentLevel) {
		case 0:
			return R.string.level_0;
		case 1:
			return R.string.level_1;
		case 2:
			return R.string.level_2;
		case 3:
			return R.string.level_3;
		case 4:
			return R.string.level_4;
		case 5:
			return R.string.level_5;
		case 6:
			return R.string.level_6;
		case 7:
			return R.string.level_7;
		case 8:
			return R.string.level_8;
		case 9:
			return R.string.level_9;
		default:
			return R.string.hello_world;
		}
	}
}
