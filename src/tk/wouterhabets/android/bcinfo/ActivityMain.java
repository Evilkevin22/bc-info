package tk.wouterhabets.android.bcinfo;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ActivityMain extends SherlockActivity {
	
	private final static String NET_URL = "http://example.com";
	
	TextView tvTitle, tvSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
			refresh();
			break;
		case R.id.menu_main_settings_item:
			break;
		case R.id.menu_main_about_item:
			break;
		case android.R.id.home:
			// Code voor app icoon button hier invullen
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		
	}
		
}
