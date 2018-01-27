package com.oilfieldapps.allspark.gridlayoutlearning;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilfieldapps.allspark.gridlayoutlearning.calculators.Converter;

import java.text.DecimalFormat;

/**
 * Created by Allspark on 23/06/2017.
 */

public class MixFluidsActivity extends AppCompatActivity {

    Spinner gridViewSpinner;
    GridLayout gridLayout1;
    GridLayout gridLayout2;
    GridLayout gridLayout3;
    GridLayout gridLayout4;

    EditText density1, density2, density3, density4;
    EditText volume1, volume2, volume3, volume4;

    TextView u_density1, u_density2, u_density3, u_density4;
    TextView u_volume1, u_volume2, u_volume3, u_volume4;

    RadioGroup densityRadioButton;
    RadioGroup volumeRadioButton;

    TextView output_final_density, output_final_volume, output_density_unit, output_volume_unit;

    Button calculateButton;
    Button clearButton;

    Toolbar mix_fluids_toolbar;

    private AdView adView;

    private Converter converter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mix_fluids_layout);
        converter = new Converter();

        adView = findViewById(R.id.mix_fluids_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        gridViewSpinner = findViewById(R.id.spinner_number_of_fluids);
        getAllTextAndEditTextView();

        gridLayout1 = findViewById(R.id.grid_1);
        gridLayout2 = findViewById(R.id.grid_2);
        gridLayout3 = findViewById(R.id.grid_3);
        gridLayout4 = findViewById(R.id.grid_4);

        volumeRadioButton = findViewById(R.id.volume_unit_radio);

        densityRadioButton = findViewById(R.id.density_unit_radio);
        setGridVisibility(4);

        gridViewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int viewNumber = Integer.parseInt(gridViewSpinner.getSelectedItem().toString());
                setGridVisibility(viewNumber);
                getAllTextAndEditTextView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mix_fluids_toolbar = findViewById(R.id.mix_fluids_toolbar);

        setSupportActionBar(mix_fluids_toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Mix Fluids");
        mix_fluids_toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));

        calculateButton = findViewById(R.id.fluid_mix_calculate);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String ro1 = density1.getText().toString();
                    String ro2 = density2.getText().toString();
                    String vol1 = volume1.getText().toString();
                    String vol2 = volume2.getText().toString();
                    if (Integer.parseInt(gridViewSpinner.getSelectedItem().toString()) == 2) {
                        Calculate2Fluids(ro1, vol1,ro2, vol2);
                    } else if (Integer.parseInt(gridViewSpinner.getSelectedItem().toString()) == 3) {
                        String ro3 = density3.getText().toString();
                        String vol3 = volume3.getText().toString();
                        Calculate3Fluids(ro1, vol1, ro2, vol2, ro3, vol3);
                    } else if (Integer.parseInt(gridViewSpinner.getSelectedItem().toString()) == 4) {
                        String ro3 = density3.getText().toString();
                        String vol3 = volume3.getText().toString();
                        String ro4 = density4.getText().toString();
                        String vol4 = volume4.getText().toString();
                        Calculate4Fluids(ro1, vol1, ro2, vol2, ro3, vol3, ro4, vol4);
                    }
                } catch (NumberFormatException nfe) {
                    DisplayAlert();
                }
                HideKeyboard();
            }

        });

        densityRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.density_radio_sg) {
                    u_density1.setText(getResources().getString(R.string.sg));
                    u_density2.setText(getResources().getString(R.string.sg));
                    output_density_unit.setText(getResources().getString(R.string.sg));
                    try {
                        String ro1 = density1.getText().toString();
                        double ro1double = Double.parseDouble(ro1);
                        String ro2 = density2.getText().toString();
                        double ro2double = Double.parseDouble(ro2);

                        double convertedToSGro1 = converter.DensityConverter("ppg", getResources().getString(R.string.sg), ro1double);
                        double convertedToSGro2 = converter.DensityConverter("ppg", getResources().getString(R.string.sg), ro2double);

                        density1.setText(new DecimalFormat("0.00").format(convertedToSGro1));
                        density2.setText(new DecimalFormat("0.00").format(convertedToSGro2));

                        if(is3Fluids()) {
                            u_density3.setText(getResources().getString(R.string.sg));
                            String ro3 = density3.getText().toString();
                            double ro3double = Double.parseDouble(ro3);
                            double convertedToSGro3 = converter.DensityConverter("ppg", getResources().getString(R.string.sg), ro3double);
                            density3.setText(new DecimalFormat("0.00").format(convertedToSGro3));
                        } else if (is4Fluids()) {
                            u_density3.setText(getResources().getString(R.string.sg));
                            u_density4.setText(getResources().getString(R.string.sg));
                            String ro3 = density3.getText().toString();
                            double ro3double = Double.parseDouble(ro3);
                            String ro4 = density4.getText().toString();
                            double ro4double = Double.parseDouble(ro4);
                            double convertedToSGro3 = converter.DensityConverter("ppg", getResources().getString(R.string.sg), ro3double);
                            double convertedToSGro4 = converter.DensityConverter("ppg", getResources().getString(R.string.sg), ro4double);
                            density3.setText(new DecimalFormat("0.00").format(convertedToSGro3));
                            density4.setText(new DecimalFormat("0.00").format(convertedToSGro4));
                        }

                    } catch (NumberFormatException nfe) {

                    }


                } else if (checkedId == R.id.density_radio_ppg) {

                    u_density1.setText(getResources().getString(R.string.ppg));
                    u_density2.setText(getResources().getString(R.string.ppg));
                    output_density_unit.setText(getResources().getString(R.string.ppg));
                    try {
                        String ro1 = density1.getText().toString();
                        double ro1double = Double.parseDouble(ro1);
                        String ro2 = density2.getText().toString();
                        double ro2double = Double.parseDouble(ro2);

                        double convertedToSGro1 = converter.DensityConverter("SG", getResources().getString(R.string.ppg), ro1double);
                        double convertedToSGro2 = converter.DensityConverter("SG", getResources().getString(R.string.ppg), ro2double);

                        density1.setText(new DecimalFormat("0.00").format(convertedToSGro1));
                        density2.setText(new DecimalFormat("0.00").format(convertedToSGro2));

                        if(is3Fluids()) {
                            u_density3.setText(getResources().getString(R.string.ppg));
                            String ro3 = density3.getText().toString();
                            double ro3double = Double.parseDouble(ro3);
                            double convertedToSGro3 = converter.DensityConverter("SG", getResources().getString(R.string.ppg), ro3double);
                            density3.setText(new DecimalFormat("0.00").format(convertedToSGro3));
                        } else if (is4Fluids()) {
                            u_density3.setText(getResources().getString(R.string.ppg));
                            u_density4.setText(getResources().getString(R.string.ppg));
                            String ro3 = density3.getText().toString();
                            double ro3double = Double.parseDouble(ro3);
                            String ro4 = density4.getText().toString();
                            double ro4double = Double.parseDouble(ro4);
                            double convertedToSGro3 = converter.DensityConverter("SG", getResources().getString(R.string.ppg), ro3double);
                            double convertedToSGro4 = converter.DensityConverter("SG", getResources().getString(R.string.ppg), ro4double);
                            density3.setText(new DecimalFormat("0.00").format(convertedToSGro3));
                            density4.setText(new DecimalFormat("0.00").format(convertedToSGro4));
                        }
                    } catch (NumberFormatException nfe) {

                    }

                }
            }
        });

        volumeRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.volume_radio_bbl) {

                    u_volume1.setText(getResources().getString(R.string.bbl));
                    u_volume2.setText(getResources().getString(R.string.bbl));
                    output_volume_unit.setText(getResources().getString(R.string.bbl));
                    try {
                        String vol1 = volume1.getText().toString();
                        double vol1double = Double.parseDouble(vol1);
                        String vol2 = volume2.getText().toString();
                        double vol2double = Double.parseDouble(vol2);

                        double convertedFromM3Vol1 = converter.VolumeConverter("m3", getResources().getString(R.string.bbl), vol1double);
                        double convertedFromM3Vol2 = converter.VolumeConverter("m3", getResources().getString(R.string.bbl), vol2double);

                        volume1.setText(new DecimalFormat("0.00").format(convertedFromM3Vol1));
                        volume2.setText(new DecimalFormat("0.00").format(convertedFromM3Vol2));

                        if(is3Fluids()) {
                            u_volume3.setText(getResources().getString(R.string.bbl));
                            String vol3 = volume3.getText().toString();
                            double vol3double = Double.parseDouble(vol3);
                            double convertedFromM3Vol3 = converter.VolumeConverter("m3", getResources().getString(R.string.bbl), vol3double);
                            volume3.setText(new DecimalFormat("0.00").format(convertedFromM3Vol3));
                        } else if (is4Fluids()) {
                            u_volume3.setText(getResources().getString(R.string.bbl));
                            u_volume4.setText(getResources().getString(R.string.bbl));
                            String vol3 = volume3.getText().toString();
                            double vol3double = Double.parseDouble(vol3);
                            String vol4 = volume4.getText().toString();
                            double vol4double = Double.parseDouble(vol4);
                            double convertedFromM3Vol3 = converter.VolumeConverter("m3", getResources().getString(R.string.bbl), vol3double);
                            double convertedFromM3Vol4 = converter.VolumeConverter("m3", getResources().getString(R.string.bbl), vol4double);
                            volume3.setText(new DecimalFormat("0.00").format(convertedFromM3Vol3));
                            volume4.setText(new DecimalFormat("0.00").format(convertedFromM3Vol4));
                        }

                    } catch (NumberFormatException nfe) {

                    }

                } else if (checkedId == R.id.volume_radio_m3) {
                    u_volume1.setText(getResources().getString(R.string.m3));
                    u_volume2.setText(getResources().getString(R.string.m3));
                    output_volume_unit.setText(getResources().getString(R.string.m3));
                    try {
                        String vol1 = volume1.getText().toString();
                        double vol1double = Double.parseDouble(vol1);
                        String vol2 = volume2.getText().toString();
                        double vol2double = Double.parseDouble(vol2);

                        double convertedFromBBLVol1 = converter.VolumeConverter("bbl", getResources().getString(R.string.m3), vol1double);
                        double convertedFromBBLVol2 = converter.VolumeConverter("bbl", getResources().getString(R.string.m3), vol2double);

                        volume1.setText(new DecimalFormat("0.00").format(convertedFromBBLVol1));
                        volume2.setText(new DecimalFormat("0.00").format(convertedFromBBLVol2));

                        if(is3Fluids()) {
                            u_volume3.setText(getResources().getString(R.string.m3));
                            String vol3 = volume3.getText().toString();
                            double vol3double = Double.parseDouble(vol3);
                            double convertedFromBBLVol3 = converter.VolumeConverter("bbl", getResources().getString(R.string.m3), vol3double);
                            volume3.setText(new DecimalFormat("0.00").format(convertedFromBBLVol3));
                        } else if (is4Fluids()) {
                            u_volume3.setText(getResources().getString(R.string.m3));
                            u_volume4.setText(getResources().getString(R.string.m3));
                            String vol3 = volume3.getText().toString();
                            double vol3double = Double.parseDouble(vol3);
                            String vol4 = volume4.getText().toString();
                            double vol4double = Double.parseDouble(vol4);
                            double convertedFromBBLVol3 = converter.VolumeConverter("bbl", getResources().getString(R.string.m3), vol3double);
                            double convertedFromBBLVol4 = converter.VolumeConverter("bbl", getResources().getString(R.string.m3), vol4double);
                            volume3.setText(new DecimalFormat("0.00").format(convertedFromBBLVol3));
                            volume4.setText(new DecimalFormat("0.00").format(convertedFromBBLVol4));
                        }
                    } catch (NumberFormatException nfe) {

                    }
                }
            }
        });

        clearButton = findViewById(R.id.fluid_mix_clear_data);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                density1.setText(getString(R.string.zero));
                volume1.setText(getString(R.string.zero));
                density2.setText(getString(R.string.zero));
                volume2.setText(getString(R.string.zero));

                output_final_density.setText(getString(R.string.zero));
                output_final_volume.setText(getString(R.string.zero));

                if(is3Fluids()) {
                    density3.setText(getString(R.string.zero));
                    volume3.setText(getString(R.string.zero));
                }

                if(is4Fluids()) {
                    density3.setText(getString(R.string.zero));
                    volume3.setText(getString(R.string.zero));
                    density4.setText(getString(R.string.zero));
                    volume4.setText(getString(R.string.zero));
                }
            }
        });

        RestoreAllValues();
    }

    boolean is3Fluids() {
        return gridViewSpinner.getSelectedItemId() == 1;
    }

    boolean is4Fluids() {
        return gridViewSpinner.getSelectedItemId() == 2;
    }

    void Calculate2Fluids(String ro1s, String vol1s, String ro2s, String vol2s) {
        try {
            double ro1 = Double.parseDouble(ro1s);
            double vol1 = Double.parseDouble(vol1s);
            double ro2 = Double.parseDouble(ro2s);
            double vol2 = Double.parseDouble(vol2s);

            double ro_final;
            double vol_final;

            vol_final = vol1 + vol2;
            ro_final = (ro1 * vol1 + ro2 * vol2) / vol_final;

            SetResults(ro_final, vol_final);
        } catch (NumberFormatException nfe) {

        }
    }

    void Calculate3Fluids(String ro1s, String vol1s, String ro2s, String vol2s, String ro3s, String vol3s) {

        try {

            double ro1 = Double.parseDouble(ro1s);
            double vol1 = Double.parseDouble(vol1s);
            double ro2 = Double.parseDouble(ro2s);
            double vol2 = Double.parseDouble(vol2s);
            double ro3 = Double.parseDouble(ro3s);
            double vol3 = Double.parseDouble(vol3s);

            double ro_final;
            double vol_final;

            vol_final = vol1 + vol2 + vol3;
            ro_final = (ro1 * vol1 + ro2 * vol2 + ro3 * vol3) / vol_final;

            SetResults(ro_final, vol_final);

        } catch (NumberFormatException nfe) {

        }
    }

    void Calculate4Fluids(String ro1s, String vol1s, String ro2s, String vol2s, String ro3s, String vol3s, String ro4s, String vol4s) {
        try {
            double ro1 = Double.parseDouble(ro1s);
            double vol1 = Double.parseDouble(vol1s);
            double ro2 = Double.parseDouble(ro2s);
            double vol2 = Double.parseDouble(vol2s);
            double ro3 = Double.parseDouble(ro3s);
            double vol3 = Double.parseDouble(vol3s);
            double ro4 = Double.parseDouble(ro4s);
            double vol4 = Double.parseDouble(vol4s);

            double ro_final;
            double vol_final;

            vol_final = vol1 + vol2 + vol3 + vol4;
            ro_final = (ro1 * vol1 + ro2 * vol2 + ro3 * vol3 + ro4 * vol4) / vol_final;

            SetResults(ro_final, vol_final);
        } catch (NumberFormatException nfe) {

        }
    }

    void SetResults(double ro_final, double vol_final) {

        output_final_density.setText(new DecimalFormat("#.00").format(ro_final));
        output_final_volume.setText(new DecimalFormat("#.00").format(vol_final));

    }

    void getAllTextAndEditTextView() {
        density1 = findViewById(R.id.density1);
        density2 = findViewById(R.id.density2);

        volume1 = findViewById(R.id.volume1);
        volume2 = findViewById(R.id.volume2);

        u_density1 = findViewById(R.id.density1_units);
        u_density2 = findViewById(R.id.density2_units);

        u_volume1 = findViewById(R.id.volume1_units);
        u_volume2 = findViewById(R.id.volume2_units);

        output_density_unit = findViewById(R.id.units_final_density);
        output_volume_unit = findViewById(R.id.units_final_volume);

        output_final_density = findViewById(R.id.output_final_density);
        output_final_volume = findViewById(R.id.output_final_volume);


        if(gridViewSpinner.getSelectedItemId() == 1 || gridViewSpinner.getSelectedItemId() == 2) {
            density3 = findViewById(R.id.density3);
            volume3 = findViewById(R.id.volume3);
            u_density3 = findViewById(R.id.density3_units);
            u_volume3 = findViewById(R.id.volume3_units);
        }

        if(gridViewSpinner.getSelectedItemId() == 2) {
            density4 = findViewById(R.id.density4);
            volume4 = findViewById(R.id.volume4);
            u_density4 = findViewById(R.id.density4_units);
            u_volume4 = findViewById(R.id.volume4_units);
        }
    }

    void setGridVisibility(int numberOfViews) {
        if(numberOfViews == 2) {
            gridLayout3.setVisibility(View.GONE);
            gridLayout4.setVisibility(View.GONE);
        } else if (numberOfViews == 3) {
            gridLayout3.setVisibility(View.VISIBLE);
            gridLayout4.setVisibility(View.GONE);
        } else if(numberOfViews == 4) {
            gridLayout3.setVisibility(View.VISIBLE);
            gridLayout4.setVisibility(View.VISIBLE);
        }
    }

    private final String MIX_FLUIDS_NUMBERS = "mix_fluids_save";
    private final String SPINNER = "spinner";
    private final String DENS_1 = "density_1";
    private final String DENS_2 = "density_2";
    private final String DENS_3 = "density_3";
    private final String DENS_4 = "density_4";
    private final String VOL_1 = "volume_1";
    private final String VOL_2 = "volume_2";
    private final String VOL_3 = "volume_3";
    private final String VOL_4 = "volume_4";
    private final String RB_DENSITY = "radioButton_density";
    private final String RB_VOLUME = "radioButton_volume";

    @Override
    protected void onStop() {
        super.onStop();
        SaveAllValues();
    }

    void SaveAllValues() {
        SharedPreferences savedValuesMixFluids = getSharedPreferences(MIX_FLUIDS_NUMBERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEditor = savedValuesMixFluids.edit();

        Log.d("SAVING", "Saving all values");

        long itemSpinner = gridViewSpinner.getSelectedItemId();
        saveEditor.putLong(SPINNER, itemSpinner);
        saveEditor.putString(DENS_1, density1.getText().toString());
        saveEditor.putString(DENS_2, density2.getText().toString());
        saveEditor.putString(VOL_1, volume1.getText().toString());
        saveEditor.putString(VOL_2, volume2.getText().toString());
        saveEditor.putInt(RB_DENSITY, densityRadioButton.getCheckedRadioButtonId());
        saveEditor.putInt(RB_VOLUME, volumeRadioButton.getCheckedRadioButtonId());

        if(itemSpinner == 1 || itemSpinner == 2) {

            Log.d("Spinner value", String.valueOf(itemSpinner));
            saveEditor.putString(DENS_3, density3.getText().toString());
            saveEditor.putString(VOL_3, volume3.getText().toString());
            saveEditor.commit();
        }

        if(itemSpinner == 2) {
            saveEditor.putString(DENS_4, density4.getText().toString());
            saveEditor.putString(VOL_4, volume4.getText().toString());
        }

        saveEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestoreAllValues();
    }

    void RestoreAllValues() {
        SharedPreferences bringValuesBack = getSharedPreferences(MIX_FLUIDS_NUMBERS, Context.MODE_PRIVATE);
        String DEFAULT_VALUE = "0";
        Log.d("RESTORING: ", "Restoring all the values - running");
        long spinnerValue = bringValuesBack.getLong(SPINNER, 2);
        int spinnerID = Integer.parseInt(String.valueOf(spinnerValue));
        gridViewSpinner.setSelection(spinnerID);
        int rb_density = bringValuesBack.getInt(RB_DENSITY, 0);
        int rb_volume = bringValuesBack.getInt(RB_VOLUME, 0);
        getAllTextAndEditTextView();
        if(R.id.density_radio_sg == rb_density) {
            densityRadioButton.check(R.id.density_radio_sg);
            u_density1.setText(getString(R.string.sg));
            u_density2.setText(getString(R.string.sg));
            output_density_unit.setText(getString(R.string.sg));

            if(spinnerID == 1 || spinnerID == 2) {
                getAllTextAndEditTextView();
                u_density3.setText(getString(R.string.sg));
            }
            if(spinnerID == 2) {
                getAllTextAndEditTextView();
                u_density4.setText(getString(R.string.sg));
            }
        } else {
            densityRadioButton.check(R.id.density_radio_ppg);
            u_density1.setText(getString(R.string.ppg));
            u_density2.setText(getString(R.string.ppg));
            output_density_unit.setText(getString(R.string.ppg));
            if(spinnerID == 1 || spinnerID == 2) {
                u_density3.setText(getString(R.string.ppg));
            }
            if(spinnerID == 2) {
                u_density4.setText(getString(R.string.ppg));
            }
        }
        if(R.id.volume_radio_m3 == rb_volume) {
            volumeRadioButton.check(R.id.volume_radio_m3);
            u_volume1.setText(getString(R.string.m3));
            u_volume2.setText(getString(R.string.m3));
            output_volume_unit.setText(getString(R.string.m3));
            if(spinnerID == 1 || spinnerID == 2) {
                u_volume3.setText(getString(R.string.m3));
            }
            if(spinnerID == 2) {
                u_volume4.setText(getString(R.string.m3));
            }
        } else {
            volumeRadioButton.check(R.id.volume_radio_bbl);
            u_volume1.setText(getString(R.string.bbl));
            u_volume2.setText(getString(R.string.bbl));
            output_volume_unit.setText(getString(R.string.bbl));
            if(spinnerID == 1 || spinnerID == 2) {
                u_volume3.setText(getString(R.string.bbl));
            }
            if(spinnerID == 2) {
                u_volume4.setText(getString(R.string.bbl));
            }
        }

        String den1 = bringValuesBack.getString(DENS_1, DEFAULT_VALUE);
        String den2 = bringValuesBack.getString(DENS_2, DEFAULT_VALUE);
        String vol1 = bringValuesBack.getString(VOL_1, DEFAULT_VALUE);
        String vol2 = bringValuesBack.getString(VOL_2, DEFAULT_VALUE);

        density1.setText(den1);
        density2.setText(den2);
        volume1.setText(vol1);
        volume2.setText(vol2);

        Calculate2Fluids(den1, vol1, den2, vol2);

        if(spinnerValue == 1) {
            String den3 = bringValuesBack.getString(DENS_3, DEFAULT_VALUE);
            String vol3 = bringValuesBack.getString(VOL_3, DEFAULT_VALUE);

            density3.setText(den3);
            volume3.setText(vol3);

            Calculate3Fluids(den1, vol1, den2, vol2, den3, vol3);
        }

        if(spinnerValue == 2) {
            String den4 = bringValuesBack.getString(DENS_4, DEFAULT_VALUE);
            String vol4 = bringValuesBack.getString(VOL_4, DEFAULT_VALUE);
            String den3 = bringValuesBack.getString(DENS_3, DEFAULT_VALUE);
            String vol3 = bringValuesBack.getString(VOL_3, DEFAULT_VALUE);
            density3.setText(den3);
            volume3.setText(vol3);
            density4.setText(den4);
            volume3.setText(vol4);
            Calculate4Fluids(den1, vol1, den2, vol2, den3, vol3, den4, vol4);
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

    public void HideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
