package com.oilfieldapps.allspark.gridlayoutlearning.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.oilfieldapps.allspark.gridlayoutlearning.R;

/**
 * Created by Allspark on 30/06/2017.
 */

public class CutWeightPrefs extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.cut_weight_menu);
    }
}
