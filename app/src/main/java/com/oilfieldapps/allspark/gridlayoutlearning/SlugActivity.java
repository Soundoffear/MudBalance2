package com.oilfieldapps.allspark.gridlayoutlearning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilfieldapps.allspark.gridlayoutlearning.Slug_Calculators.SlugVolume;
import com.oilfieldapps.allspark.gridlayoutlearning.Slug_Calculators.SlugWeight;
import com.oilfieldapps.allspark.gridlayoutlearning.preferences.SlugPrefs;

public class SlugActivity extends AppCompatActivity {

    Toolbar slug_toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    AdView slugAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slug_layout);

        slugAdView = findViewById(R.id.slug_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        slugAdView.loadAd(adRequest);

        slug_toolbar = findViewById(R.id.slug_toolbar);
        setSupportActionBar(slug_toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Slug Calculations");
        slug_toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));

        pageAdapter = new PageAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.slug_view_pager);
        viewPager.setAdapter(pageAdapter);

        tabLayout = findViewById(R.id.slug_tabLayout);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.slug_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.slug_options) {
            Intent slug_prefs = new Intent(getApplicationContext(), SlugPrefs.class);
            startActivity(slug_prefs);
        }
        return super.onOptionsItemSelected(item);
    }

    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: return new SlugVolume();
                case 1: return new SlugWeight();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Slug Volume";
                case 1: return "Slug Weight";
            }

            return null;
        }
    }

    public void HideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
