package com.oilfieldapps.allspark.gridlayoutlearning.ConverterFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.oilfieldapps.allspark.gridlayoutlearning.R;
import com.oilfieldapps.allspark.gridlayoutlearning.calculators.Converter;

import java.text.DecimalFormat;

/**
 * Created by Allspark on 01/07/2017.
 */

public class VolumeConverter extends Fragment {

    Spinner volume_converter_spinner;

    TextView[] unitLabels = new TextView[4];
    TextView[] unitUnits = new TextView[4];
    TextView[] outputValue = new TextView[4];

    EditText inputValue;

    TextView startLabel;
    TextView startUnit;

    String oldLabel;
    String oldUnit;

    String newLabel;
    String newUnit;

    private Converter unitConverter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View volumeView = inflater.inflate(R.layout.converter_volume_layout, container, false);

        unitConverter = new Converter();

        volume_converter_spinner = volumeView.findViewById(R.id.volume_converter_spinner);

        unitLabels[0] = volumeView.findViewById(R.id.converter_ft3_title);
        unitLabels[1] = volumeView.findViewById(R.id.converter_gal_title);
        unitLabels[2] = volumeView.findViewById(R.id.converter_m3_title);
        unitLabels[3] = volumeView.findViewById(R.id.converter_l_title);

        unitUnits[0] = volumeView.findViewById(R.id.converter_ft3_unit);
        unitUnits[1] = volumeView.findViewById(R.id.converter_gal_unit);
        unitUnits[2] = volumeView.findViewById(R.id.converter_m3_unit);
        unitUnits[3] = volumeView.findViewById(R.id.converter_l_unit);

        outputValue[0] = volumeView.findViewById(R.id.main_output_ft3_converter);
        outputValue[1] = volumeView.findViewById(R.id.main_output_gal_converter);
        outputValue[2] = volumeView.findViewById(R.id.main_output_m3_converter);
        outputValue[3] = volumeView.findViewById(R.id.main_output_l_converter);

        startLabel = volumeView.findViewById(R.id.converter_bbl_title);
        startUnit = volumeView.findViewById(R.id.converter_bbl_unit);

        inputValue = volumeView.findViewById(R.id.main_input_volume_converter);

        String lastSpinnerValue = volume_converter_spinner.getSelectedItem().toString();

        SplitToLabelAndUnit(lastSpinnerValue);

        volume_converter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newSelectedItem = volume_converter_spinner.getSelectedItem().toString();
                SplitNewToLabelAndUnit(newSelectedItem);
                for(int i = 0; i < unitLabels.length; i++) {
                    if(unitLabels[i].getText().toString().equals(newLabel)) {
                        unitLabels[i].setText(oldLabel);
                        startLabel.setText(newLabel);
                    }
                }
                Log.d("Unit: ", oldUnit + " " +newUnit);
                for(int i = 0; i < unitUnits.length; i++) {
                    if(unitUnits[i].getText().toString().equals(newUnit)) {
                        unitUnits[i].setText(oldUnit);
                        startUnit.setText(newUnit);
                    }
                }

                SplitToLabelAndUnit(newSelectedItem);

                try {
                    double inputDouble = Double.parseDouble(inputValue.getText().toString());

                    Log.d("VOLUME TEST: ", String.valueOf(inputDouble));
                    String oldUnit = startUnit.getText().toString();
                    for(int i = 0; i < outputValue.length; i++) {
                        double inputDouble2 = unitConverter.VolumeConverter(oldUnit, unitUnits[i].getText().toString(), inputDouble);
                        Log.d("VOLUME TEST: ", oldUnit + " | " + unitUnits[i].getText().toString());
                        double roundedResult = Round3Decimals(inputDouble2);
                        outputValue[i].setText(new DecimalFormat("0.000").format(roundedResult));
                    }

                } catch (NumberFormatException nfe) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double inputDouble = Double.parseDouble(inputValue.getText().toString());

                    Log.d("VOLUME TEST: ", String.valueOf(inputDouble));
                    String oldUnit = startUnit.getText().toString();
                    for(int i = 0; i < outputValue.length; i++) {
                        double inputDouble2 = unitConverter.VolumeConverter(oldUnit, unitUnits[i].getText().toString(), inputDouble);
                        Log.d("VOLUME TEST: ", oldUnit + " | " + unitUnits[i].getText().toString());
                        double roundedResult = Round3Decimals(inputDouble2);
                        outputValue[i].setText(new DecimalFormat("0.000").format(roundedResult));
                    }

                } catch (NumberFormatException nfe) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return volumeView;
    }

    void SplitToLabelAndUnit(String currentSpinnerItem) {
        String[] splittedItem = currentSpinnerItem.split(", ");
        oldLabel = splittedItem[0];
        oldUnit = splittedItem[1];
    }

    void SplitNewToLabelAndUnit(String newSpinnerItem) {
        String[] splittedItem = newSpinnerItem.split(", ");
        newLabel = splittedItem[0];
        newUnit = splittedItem[1];
    }

    double Round3Decimals(double value) {
        double tempValue = value * 1000;
        tempValue = Math.round(tempValue);
        return tempValue / 1000;
    }
}
