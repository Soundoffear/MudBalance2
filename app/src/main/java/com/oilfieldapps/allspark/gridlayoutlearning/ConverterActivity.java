package com.oilfieldapps.allspark.gridlayoutlearning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilfieldapps.allspark.gridlayoutlearning.ConverterFragments.DensityConverter;
import com.oilfieldapps.allspark.gridlayoutlearning.ConverterFragments.MassConverter;
import com.oilfieldapps.allspark.gridlayoutlearning.ConverterFragments.VolumeConverter;

/**
 * Created by Allspark on 01/07/2017.
 */

public class ConverterActivity extends AppCompatActivity {

    AdView converterAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_layout);

        converterAdView = findViewById(R.id.converter_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        converterAdView.loadAd(adRequest);

        Toolbar converter_toolbar = findViewById(R.id.converter_toolbar);
        TabLayout converter_tabLayout = findViewById(R.id.converter_tabLayout);
        ViewPager converter_viePager = findViewById(R.id.converter_viewPager);
        PagerAdapterConverter pagerAdapterConverter = new PagerAdapterConverter(getSupportFragmentManager());

        setSupportActionBar(converter_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Converter");
        converter_toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));

        converter_viePager = findViewById(R.id.converter_viewPager);
        converter_viePager.setAdapter(pagerAdapterConverter);

        converter_tabLayout.setupWithViewPager(converter_viePager);
    }

    private class PagerAdapterConverter extends FragmentPagerAdapter {

        public PagerAdapterConverter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DensityConverter();
                case 1:
                    return new VolumeConverter();
                case 2:
                    return new MassConverter();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Density";
                case 1:
                    return "Volume";
                case 2:
                    return "Mass";
            }
            return null;
        }
    }

}
