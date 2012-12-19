package tk.wouterhabets.android.bcinfo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesMain extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		addPreferencesFromResource(R.xml.main_preference);
	}
	
	

}
