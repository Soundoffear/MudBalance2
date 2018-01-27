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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilfieldapps.allspark.gridlayoutlearning.calculators.Converter;
import com.oilfieldapps.allspark.gridlayoutlearning.calculators.WeightUpCalculator;
import com.oilfieldapps.allspark.gridlayoutlearning.preferences.Preferences;

import java.text.DecimalFormat;

/**
 * Created by Allspark on 17/06/2017.
 */

public class WeightUpActivity extends AppCompatActivity {

    private String SPINNER_SAVE = "spinner_save";
    private String WEIGHTING_MAT = "weighting_material";
    private String INITIAL_DEN = "initial_density";
    private String INITIAL_VOL = "initial_volume";
    private String DESIRED_DEN = "densired_density";
    private String MATERIAL_NEED = "material_needed";
    private String VOLUME_INC = "volume_increase";
    private String TOTAL_VOLUME = "total_volume";

    private String LAST_DENSITY_UNIT = "last_density_unit";
    private String LAST_VOLUME_UNIT = "last_volume_unit";
    private String LAST_WEIGHT_UNIT = "last_weight_unit";

    private EditText input_weighting_material;
    private EditText input_initial_volume;
    private EditText input_initial_mud_weight;
    private EditText input_desired_mud_weight;

    private TextView output_material_needed;
    private TextView output_volume_increase;
    private TextView output_total_volume;

    private TextView units_weighting_material;
    private TextView units_initial_volume;
    private TextView units_initial_mud_weight;
    private TextView units_desired_mud_weight;
    private TextView units_output_material_needed;
    private TextView units_output_volume_increase;
    private TextView units_output_total_volume;

    private Button calculate_btn;
    private Button clear_btn;

    private Spinner weighting_material_spinner;

    private String[] weightingMatValues = {"4.2", "4.1", "2.7", "2.3", "2.1"};

    private static final String SAVE_DATA_WEIGHT_UP = "WeightUpData";

    private Converter converter;

    //Sacks calculation fields
    private TextView sacksSizeLabel;
    private EditText sacksEditText;
    private TextView sacksUnit;
    private TextView sacksAmountLabel;
    private TextView output_sacks;
    private TextView sacks_sacks;

    Toolbar toolbar;

    String densityUnits;
    String volumeUnits;
    String weightUnits;
    String sacksUnits;

    String weightingMat;
    String iniMudWeight;
    String iniVolume;
    String desMudWeight;

    AdView weightUpAdView;

    private WeightUpCalculator weightUpCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_up_layout);

        weightUpAdView = findViewById(R.id.weight_up_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        weightUpAdView.loadAd(adRequest);

        converter = new Converter();
        weightUpCalculator = new WeightUpCalculator();

        input_weighting_material = findViewById(R.id.input_weighting_material);
        input_initial_mud_weight = findViewById(R.id.input_initial_mud_weight);
        input_initial_volume = findViewById(R.id.input_initial_volume);
        input_desired_mud_weight = findViewById(R.id.input_desired_mud_weight);

        output_material_needed = findViewById(R.id.output_material_needed);
        output_volume_increase = findViewById(R.id.output_volume_increase);
        output_total_volume = findViewById(R.id.output_total_volume);

        sacksSizeLabel = findViewById(R.id.label_sacks_size);
        sacksEditText = findViewById(R.id.input_sacks);
        sacksUnit = findViewById(R.id.unit_sacks);
        sacksAmountLabel = findViewById(R.id.label_sacks_amount);
        output_sacks = findViewById(R.id.output_sacks);
        sacks_sacks = findViewById(R.id.sacks);

        DisableSacksCalculations();

        EnablePackageCalculation();

        setUnits();

        calculate_btn = findViewById(R.id.calculate_data);
        clear_btn = findViewById(R.id.clear_data);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Weight Up");
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));

        calculate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    weightingMat = input_weighting_material.getText().toString();
                    iniMudWeight = input_initial_mud_weight.getText().toString();
                    iniVolume = input_initial_volume.getText().toString();
                    desMudWeight = input_desired_mud_weight.getText().toString();

                    weightUpCalculator.CalculateData(densityUnits, volumeUnits, weightUnits, weightingMat, iniMudWeight, iniVolume, desMudWeight);

                    java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("0.00");

                    String volumeInc = String.valueOf(decimalFormat.format(weightUpCalculator.getVolumeIncrease()));
                    String volumeTot = String.valueOf(decimalFormat.format(weightUpCalculator.getTotalVolume()));
                    String materialN = String.valueOf(decimalFormat.format(weightUpCalculator.getMaterialNeeded()));

                    output_volume_increase.setText(volumeInc);
                    output_total_volume.setText(volumeTot);
                    output_material_needed.setText(materialN);

                    SharedPreferences sacksEnabled = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean isSacksCalculatable = sacksEnabled.getBoolean("SACKS_CALCULATOR", false);

                    if(isSacksCalculatable) {
                        Log.d("Is Working sacks", "Sacks Test");
                        String sacksSize = sacksEditText.getText().toString();

                        weightUpCalculator.CalculateSacks(sacksSize, weightUnits, sacksUnits);

                        String sacksAmount = String.valueOf(decimalFormat.format(weightUpCalculator.getMaterialSacksAmount()));

                        output_sacks.setText(sacksAmount);
                    }


                } catch (NumberFormatException nfe) {
                    DisplayAlert();
                }

                HideKeyboard();

            }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_initial_mud_weight.setText(getString(R.string.zero));
                input_weighting_material.setText(getString(R.string.zero));
                input_initial_volume.setText(getString(R.string.zero));
                input_desired_mud_weight.setText(R.string.zero);
                output_material_needed.setText(R.string.zero);
                output_volume_increase.setText(R.string.zero);
                output_total_volume.setText(R.string.zero);
            }
        });

        weighting_material_spinner = findViewById(R.id.spinner_weighting_material);

        weighting_material_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String wmSelected = weighting_material_spinner.getItemAtPosition(position).toString();
                input_weighting_material.setText(spinnerUnits(wmSelected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.general_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.units_menu) {
            Intent intent = new Intent(WeightUpActivity.this, Preferences.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavingAllValues();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUnits();

        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_DATA_WEIGHT_UP, Context.MODE_PRIVATE);

        String resume_weighting_material = sharedPreferences.getString(WEIGHTING_MAT, getString(R.string.zero));
        String resume_initial_density = sharedPreferences.getString(INITIAL_DEN, getString(R.string.zero));
        String resume_initial_volume = sharedPreferences.getString(INITIAL_VOL, getString(R.string.zero));
        String resume_desired_density = sharedPreferences.getString(DESIRED_DEN, getString(R.string.zero));
        String resume_o_material_needed = sharedPreferences.getString(MATERIAL_NEED, getString(R.string.zero));
        String resume_o_volume_increase = sharedPreferences.getString(VOLUME_INC, getString(R.string.zero));
        String resume_o_total_volume = sharedPreferences.getString(TOTAL_VOLUME, getString(R.string.zero));

        SharedPreferences packPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean resume_package = packPrefs.getBoolean("SACKS_CALCULATOR", false);

        String old_density_unit = sharedPreferences.getString(LAST_DENSITY_UNIT, getString(R.string.zero));

        String newDensity = units_desired_mud_weight.getText().toString();
        String newVolumeUnit = units_initial_volume.getText().toString();
        String newWeightUnit = units_output_material_needed.getText().toString();

        try {
            double recalculatedDensity_weighting_material = converter.DensityConverter(old_density_unit, newDensity, Double.parseDouble(resume_weighting_material));
            input_weighting_material.setText(new DecimalFormat("0.00").format(recalculatedDensity_weighting_material));
        } catch (NumberFormatException nfe) {

        }

        input_initial_mud_weight.setText(resume_initial_density);
        input_desired_mud_weight.setText(resume_desired_density);
        input_initial_volume.setText(resume_initial_volume);
        output_material_needed.setText(resume_o_material_needed);
        output_volume_increase.setText(resume_o_volume_increase);
        output_total_volume.setText(resume_o_total_volume);

        units_weighting_material.setText(newDensity);
        units_initial_mud_weight.setText(newDensity);
        units_desired_mud_weight.setText(newDensity);
        units_initial_volume.setText(newVolumeUnit);
        units_output_volume_increase.setText(newVolumeUnit);
        units_output_total_volume.setText(newVolumeUnit);

        units_output_material_needed.setText(newWeightUnit);

        Log.d("Package enabled: ", String.valueOf(resume_package));

        if(resume_package) {
            Log.d("Package enabled IN: ", String.valueOf(resume_package));
            EnablePackageCalculation();
            sacksUnit.setText(sacksUnits);
        } else {
            DisableSacksCalculations();
        }

    }

    public String spinnerUnits(String weightMatSelected) {

        String defaultUnit = getString(R.string.sg);
        String newDensityUnit = units_weighting_material.getText().toString();

        String[] weightMat = getResources().getStringArray(R.array.weighting_agents);

        for(int i = 0; i < weightMat.length;i++) {
            if(weightMat[i].contains(weightMatSelected)) {
                double weightingMaterialValue = Double.parseDouble(weightingMatValues[i]);
                double convertedValue = converter.DensityConverter(defaultUnit, newDensityUnit, weightingMaterialValue);
                return new DecimalFormat("0.00").format(convertedValue);
            }
        }

        return null;
    }

    public void setUnits() {

        SharedPreferences unitsPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        densityUnits = unitsPrefs.getString("MUD_DENSITY_UNITS", "SG");
        volumeUnits = unitsPrefs.getString("MUD_VOLUME_UNITS", "m3");
        weightUnits = unitsPrefs.getString("WEIGHT_UNITS", "kg");

        units_weighting_material = findViewById(R.id.unit_weighting_material);
        units_initial_mud_weight = findViewById(R.id.unit_initial_mud_weight);
        units_initial_volume = findViewById(R.id.unit_initial_volume);
        units_desired_mud_weight = findViewById(R.id.unit_desired_mud_weight);
        units_output_volume_increase = findViewById(R.id.unit_volume_increase);
        units_output_material_needed = findViewById(R.id.unit_material_needed);
        units_output_total_volume = findViewById(R.id.unit_total_volume);

        units_weighting_material.setText(densityUnits);
        units_initial_mud_weight.setText(densityUnits);
        units_initial_volume.setText(volumeUnits);
        units_desired_mud_weight.setText(densityUnits);
        units_output_volume_increase.setText(volumeUnits);
        units_output_total_volume.setText(volumeUnits);
        units_output_material_needed.setText(weightUnits);

        if(sacksAreEnabled) {
            sacksUnit.setText(sacksUnits);
        }

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

    private boolean sacksAreEnabled;

    public void EnablePackageCalculation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sacksAreEnabled = sharedPreferences.getBoolean("SACKS_CALCULATOR", false);

        if(sacksAreEnabled) {
            sacksUnits = sharedPreferences.getString("SACKS_UNIT", "kg/sack");
            sacksSizeLabel.setVisibility(View.VISIBLE);
            sacksEditText.setVisibility(View.VISIBLE);
            sacksUnit.setVisibility(View.VISIBLE);
            sacksAmountLabel.setVisibility(View.VISIBLE);
            output_sacks.setVisibility(View.VISIBLE);
            sacks_sacks.setVisibility(View.VISIBLE);

        }
    }

    public void DisableSacksCalculations() {
        sacksSizeLabel.setVisibility(View.GONE);
        sacksEditText.setVisibility(View.GONE);
        sacksUnit.setVisibility(View.GONE);
        sacksAmountLabel.setVisibility(View.GONE);
        output_sacks.setVisibility(View.GONE);
        sacks_sacks.setVisibility(View.GONE);
    }

    public void SavingAllValues() {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_DATA_WEIGHT_UP, Context.MODE_PRIVATE);
        SharedPreferences.Editor weightUpDataSave = sharedPreferences.edit();

        weightUpDataSave.putInt(SPINNER_SAVE, (int)weighting_material_spinner.getSelectedItemId());
        weightUpDataSave.putString(WEIGHTING_MAT, input_weighting_material.getText().toString());
        weightUpDataSave.putString(INITIAL_DEN, input_initial_mud_weight.getText().toString());
        weightUpDataSave.putString(INITIAL_VOL, input_initial_volume.getText().toString());
        weightUpDataSave.putString(DESIRED_DEN, input_desired_mud_weight.getText().toString());
        weightUpDataSave.putString(MATERIAL_NEED, output_material_needed.getText().toString());
        weightUpDataSave.putString(VOLUME_INC, output_volume_increase.getText().toString());
        weightUpDataSave.putString(TOTAL_VOLUME, output_total_volume.getText().toString());

        weightUpDataSave.putString(LAST_DENSITY_UNIT, units_desired_mud_weight.getText().toString());
        weightUpDataSave.putString(LAST_VOLUME_UNIT, units_initial_volume.getText().toString());
        weightUpDataSave.putString(LAST_WEIGHT_UNIT, units_output_material_needed.getText().toString());

        weightUpDataSave.commit();

    }

    public void HideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
