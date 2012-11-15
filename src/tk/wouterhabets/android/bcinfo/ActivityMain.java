package tk.wouterhabets.android.bcinfo;

import android.app.Activity;
import android.os.Bundle;

public class ActivityMain extends Activity {
	
	private String currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

}
