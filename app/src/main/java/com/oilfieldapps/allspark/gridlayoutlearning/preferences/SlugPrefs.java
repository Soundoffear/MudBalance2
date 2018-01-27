package com.oilfieldapps.allspark.gridlayoutlearning.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.oilfieldapps.allspark.gridlayoutlearning.R;

/**
 * Created by Allspark on 27/06/2017.
 */

public class SlugPrefs extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.slug_options_menu);
    }
}
