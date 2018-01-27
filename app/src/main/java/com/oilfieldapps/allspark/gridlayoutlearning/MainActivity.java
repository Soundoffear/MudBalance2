package com.oilfieldapps.allspark.gridlayoutlearning;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.oilfieldapps.allspark.gridlayoutlearning.preferences.AboutApp;

public class MainActivity extends AppCompatActivity {

    private ImageButton weightUp_button;
    private ImageButton mixFluids_button;
    private ImageButton cutWeight_button;
    private ImageButton slug_button;
    private ImageButton converter_button;

    private Button other_Apps_button;
    private Button rate_Apps_button;

    private AdView mainView_AdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.appID_dummy));

        mainView_AdView = findViewById(R.id.mud_balance_main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mainView_AdView.loadAd(adRequest);

        firstAppRun();

        weightUp_button = findViewById(R.id.btn_weight_up);
        mixFluids_button = findViewById(R.id.btn_mix_fluids);
        cutWeight_button = findViewById(R.id.btn_cut_mudWeight);
        slug_button = findViewById(R.id.btn_slug);
        converter_button = findViewById(R.id.btn_converter);

        other_Apps_button = findViewById(R.id.mud_balance_otherApps);
        rate_Apps_button = findViewById(R.id.mud_balance_rateUs);

        weightUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeightUpActivity.class);
                startActivity(intent);
            }
        });

        mixFluids_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MixFluidsActivity.class);
                startActivity(intent);
            }
        });

        slug_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SlugActivity.class);
                startActivity(intent);
            }
        });

        cutWeight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CutWeightActivity.class);
                startActivity(intent);
            }
        });

        converter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConverterActivity.class);
                startActivity(intent);
            }
        });

        other_Apps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherAppsIntent = new Intent(Intent.ACTION_VIEW);
                otherAppsIntent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Oilfield+Apps"));
                startActivity(otherAppsIntent);
            }
        });

        rate_Apps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                rateIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.oilfieldapps.allspark.mudBalance&rdid=com.oilfieldapps.allspark.mudBalance"));
                startActivity(rateIntent);
            }
        });
    }

    public void firstAppRun() {

        final SharedPreferences firstRun = getSharedPreferences("firstRun", Context.MODE_PRIVATE);

        if (firstRun.getBoolean("F_RUN", true)) {

            AlertDialog.Builder firstRunMessage = new AlertDialog.Builder(this);
            firstRunMessage.setTitle("Please Read");
            firstRunMessage.setCancelable(false);
            firstRunMessage.setMessage(getResources().getString(R.string.alert_box));
            firstRunMessage.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor valueFirstRun = firstRun.edit();
                    valueFirstRun.putBoolean("F_RUN", false);
                    valueFirstRun.commit();
                    dialog.dismiss();

                }
            }).setNegativeButton("NO, THANKS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor valueFirstRun = firstRun.edit();
                    valueFirstRun.putBoolean("F_RUN", true);
                    valueFirstRun.commit();
                    System.exit(0);

                }
            });

            AlertDialog alertDialog = firstRunMessage.create();
            alertDialog.show();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about_mud_balance) {
            Intent intent = new Intent(MainActivity.this, AboutApp.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
