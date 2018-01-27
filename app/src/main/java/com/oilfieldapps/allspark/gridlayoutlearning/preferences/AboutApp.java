package com.oilfieldapps.allspark.gridlayoutlearning.preferences;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.oilfieldapps.allspark.gridlayoutlearning.R;

/**
 * Created by Allspark on 10/07/2017.
 */

public class AboutApp extends AppCompatActivity {

    private Toolbar about_app_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app_about);

        about_app_toolbar = findViewById(R.id.about_app_toolbar);

        setSupportActionBar(about_app_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("About App");
        about_app_toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));

    }
}
