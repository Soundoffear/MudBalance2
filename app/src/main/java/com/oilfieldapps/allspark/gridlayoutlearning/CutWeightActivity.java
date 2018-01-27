package com.oilfieldapps.allspark.gridlayoutlearning;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilfieldapps.allspark.gridlayoutlearning.calculators.Converter;
import com.oilfieldapps.allspark.gridlayoutlearning.preferences.CutWeightPrefs;

import java.text.DecimalFormat;

public class CutWeightActivity extends AppCompatActivity {

    Converter converter;
    Toolbar cut_weight_toolbar;

    EditText input_density_current_mud;
    EditText input_volume_current_mud;
    EditText input_density_light_fluid;
    EditText input_volume_light_fluid;

    TextView output_density_final;
    TextView output_volume_final;

    TextView unit_density_current_mud;
    TextView unit_volume_current_mud;
    TextView unit_density_light_fluid;
    TextView unit_volume_light_fluid;

    TextView unit_density_final;
    TextView unit_volume_final;

    Button calculate_button;
    Button clear_button;

    Spinner light_fluid_spinner;

    AdView cutWeightAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cut_mudweight_layout);

        cutWeightAdView = findViewById(R.id.cut_weight_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        cutWeightAdView.loadAd(adRequest);

        converter = new Converter();

        cut_weight_toolbar = findViewById(R.id.cut_weight_toolbar);
        light_fluid_spinner = findViewById(R.id.spinnerCM_lighter_fluid_name);

        setSupportActionBar(cut_weight_toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Cut Fluid Weight");
        cut_weight_toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));

        input_density_current_mud = findViewById(R.id.inputCM_current_mud_density);
        input_volume_current_mud = findViewById(R.id.inputCM_current_mud_volume);
        input_density_light_fluid = findViewById(R.id.inputCM_lighter_mud_density);
        input_volume_light_fluid = findViewById(R.id.inputCM_lighter_mud_volume);

        output_density_final = findViewById(R.id.outputCM_lighter_mud_density);
        output_volume_final = findViewById(R.id.outputCM_lighter_mud_volume);

        unit_density_current_mud = findViewById(R.id.inputCM_unit_current_mud_density);
        unit_volume_current_mud = findViewById(R.id.inputCM_unit_current_mud_volume);
        unit_density_light_fluid = findViewById(R.id.inputCM_unit_lighter_mud_density);
        unit_volume_light_fluid = findViewById(R.id.inputCM_unit_lighter_mud_volume);

        unit_density_final = findViewById(R.id.outputCM_unit_lighter_mud_density);
        unit_volume_final = findViewById(R.id.outputCM_unit_lighter_mud_volume);

        calculate_button = findViewById(R.id.cut_weight_calculate);
        clear_button = findViewById(R.id.cut_weight_clear);

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_volume_light_fluid.setText(getString(R.string.zero));
                input_volume_current_mud.setText(getString(R.string.zero));
                input_density_current_mud.setText(getString(R.string.zero));
                input_density_light_fluid.setText(getString(R.string.zero));

                output_density_final.setText(getString(R.string.zero));
                output_volume_final.setText(getString(R.string.zero));
            }
        });

        calculate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double current_ro = Double.parseDouble(input_density_current_mud.getText().toString());
                    double current_vol = Double.parseDouble(input_volume_current_mud.getText().toString());
                    double light_ro = Double.parseDouble(input_density_light_fluid.getText().toString());
                    double light_vol = Double.parseDouble(input_volume_light_fluid.getText().toString());

                    double final_volume = current_vol + light_vol;
                    double final_ro = (current_ro * current_vol + light_ro * light_vol) / final_volume;

                    output_volume_final.setText(new DecimalFormat("0.00").format(final_volume));
                    output_density_final.setText(new DecimalFormat("0.00").format(final_ro));

                } catch (NumberFormatException nfe) {
                    DisplayAlert();
                }
            }
        });

        light_fluid_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (light_fluid_spinner.getSelectedItem().toString().contains("Water")) {
                    String currentDensityUnit = unit_density_current_mud.getText().toString();
                    double waterBaseValuePPG = 8.345;
                    double waterBaseValueConverted = converter.DensityConverter("ppg", currentDensityUnit, waterBaseValuePPG);
                    input_density_light_fluid.setText(new DecimalFormat("0.00").format(waterBaseValueConverted));
                } else if (light_fluid_spinner.getSelectedItem().toString().contains("Diesel")) {
                    String currentDensityUnit = unit_density_current_mud.getText().toString();
                    double waterBaseValuePPG = 7.00;
                    double waterBaseValueConverted = converter.DensityConverter("ppg", currentDensityUnit, waterBaseValuePPG);
                    input_density_light_fluid.setText(new DecimalFormat("0.00").format(waterBaseValueConverted));
                } else if (light_fluid_spinner.getSelectedItem().toString().contains("Other")) {
                    input_density_light_fluid.setText(getString(R.string.zero));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RestoreValues();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetAndSetAllUnits();

        try {
            SharedPreferences sp = getSharedPreferences(CUT_MUD_VALUES, Context.MODE_PRIVATE);
            String oldDensityUnit = sp.getString(UNIT_DENSITY, getString(R.string.ppg));
            String currentDensityUnit = unit_density_light_fluid.getText().toString();
            Double currentValue = Double.parseDouble(input_density_light_fluid.getText().toString());

            double recalculatedLighterDensity = converter.DensityConverter(oldDensityUnit, currentDensityUnit, currentValue);

            input_density_light_fluid.setText(new DecimalFormat("0.00").format(recalculatedLighterDensity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void GetAndSetAllUnits() {
        SharedPreferences unitsPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String densityUnits = unitsPrefs.getString("CUT_MUD_DENSITY_UNITS", "ppg");
        String volumeUnits = unitsPrefs.getString("CUT_MUD_VOLUME_UNITS", "bbl");

        unit_density_current_mud.setText(densityUnits);
        unit_density_light_fluid.setText(densityUnits);
        unit_density_final.setText(densityUnits);

        unit_volume_current_mud.setText(volumeUnits);
        unit_volume_light_fluid.setText(volumeUnits);
        unit_volume_final.setText(volumeUnits);
    }

    final String CUT_MUD_VALUES = "cut_mud_values";
    final String IN_RO_CURRENT = "input_current_density";
    final String IN_VOL_CURRENT = "input_current_volume";
    final String IN_RO_LIGHT = "input_current_density";
    final String IN_VOL_LIGHT = "input_current_volume";
    final String OUT_RO_FINAL = "output_density_final";
    final String OUT_VOL_FINAL = "output_volume_final";
    final String SPINNER_VALUE = "spinner_item_value";

    final String UNIT_DENSITY = "densityUnits";
    final String UNIT_VOLUME = "volumeUnits";

    @Override
    protected void onStop() {
        super.onStop();
        SaveValues();
    }

    void SaveValues() {

        SharedPreferences savedValues = getSharedPreferences(CUT_MUD_VALUES, Context.MODE_PRIVATE);
        SharedPreferences.Editor savedValuesEditor = savedValues.edit();

        savedValuesEditor.putString(IN_RO_CURRENT, input_density_current_mud.getText().toString());
        savedValuesEditor.putString(IN_VOL_CURRENT, input_volume_current_mud.getText().toString());
        savedValuesEditor.putString(IN_RO_LIGHT, input_density_light_fluid.getText().toString());
        savedValuesEditor.putString(IN_VOL_LIGHT, input_volume_light_fluid.getText().toString());
        savedValuesEditor.putString(OUT_RO_FINAL, output_density_final.getText().toString());
        savedValuesEditor.putString(OUT_VOL_FINAL, output_volume_final.getText().toString());
        savedValuesEditor.putLong(SPINNER_VALUE, light_fluid_spinner.getSelectedItemId());

        savedValuesEditor.putString(UNIT_DENSITY, unit_density_current_mud.getText().toString());
        savedValuesEditor.putString(UNIT_VOLUME, unit_volume_current_mud.getText().toString());
        savedValuesEditor.apply();

    }

    void RestoreValues() {
        SharedPreferences restoreValues = getSharedPreferences(CUT_MUD_VALUES, Context.MODE_PRIVATE);
        input_density_current_mud.setText(restoreValues.getString(IN_RO_CURRENT, getString(R.string.zero)));
        input_volume_current_mud.setText(restoreValues.getString(IN_VOL_CURRENT, getString(R.string.zero)));
        input_density_light_fluid.setText(restoreValues.getString(IN_RO_LIGHT, getString(R.string.zero)));
        input_volume_light_fluid.setText(restoreValues.getString(IN_VOL_LIGHT, getString(R.string.zero)));
        output_density_final.setText(restoreValues.getString(OUT_RO_FINAL, getString(R.string.zero)));
        output_volume_final.setText(restoreValues.getString(OUT_VOL_FINAL, getString(R.string.zero)));

        String densityU = restoreValues.getString(UNIT_DENSITY, getString(R.string.ppg));

        unit_density_current_mud.setText(densityU);
        unit_density_light_fluid.setText(densityU);
        unit_density_final.setText(densityU);

        String volumeU = restoreValues.getString(UNIT_VOLUME, getString(R.string.bbl));

        unit_volume_current_mud.setText(volumeU);
        unit_volume_light_fluid.setText(volumeU);
        unit_volume_final.setText(volumeU);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cut_weight_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cut_weight_options) {
            Intent intent = new Intent(CutWeightActivity.this, CutWeightPrefs.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void DisplayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("One of the fields is empty or not a number, please check");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
