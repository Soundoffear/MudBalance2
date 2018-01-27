package com.oilfieldapps.allspark.gridlayoutlearning.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.oilfieldapps.allspark.gridlayoutlearning.R;

/**
 * Created by Allspark on 19/06/2017.
 */

public class Preferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs_weight_up);
    }
}
