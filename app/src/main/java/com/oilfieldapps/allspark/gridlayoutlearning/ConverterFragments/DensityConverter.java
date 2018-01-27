package com.oilfieldapps.allspark.gridlayoutlearning.ConverterFragments;

import android.content.ContentValues;
import android.os.Bundle;
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

/**
 * Created by Allspark on 01/07/2017.
 */

public class DensityConverter extends Fragment {

    Spinner starting_unit_spinner;

    TextView[] unitsLabels = new TextView[4];
    TextView[] unitsUnits = new TextView[4];
    TextView[] outputUnits = new TextView[4];

    TextView inputLabel;
    TextView inputUnit;

    private EditText inputValue;

    String currentSpinnerItem;
    String oldUnitLabel;
    String oldUnitUnit;
    String selectedName;
    String selectedUnit;

    private Converter unitConverter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View densityView = inflater.inflate(R.layout.converter_density_layout, container, false);

        unitConverter = new Converter();

        starting_unit_spinner = (Spinner) densityView.findViewById(R.id.density_converter_spinner);
        unitsLabels[0] = (TextView) densityView.findViewById(R.id.converter_lbft3_title);
        unitsLabels[1] = (TextView) densityView.findViewById(R.id.converter_kgl_title);
        unitsLabels[2] = (TextView) densityView.findViewById(R.id.converter_kgm3_title);
        unitsLabels[3] = (TextView) densityView.findViewById(R.id.converter_sg_title);

        unitsUnits[0] = (TextView) densityView.findViewById(R.id.converter_lbft3_unit);
        unitsUnits[1] = (TextView) densityView.findViewById(R.id.converter_kgl_unit);
        unitsUnits[2] = (TextView) densityView.findViewById(R.id.converter_kgm3_unit);
        unitsUnits[3] = (TextView) densityView.findViewById(R.id.converter_sg_unit);

        outputUnits[0] = (TextView) densityView.findViewById(R.id.main_output_lbft3_converter);
        outputUnits[1] = (TextView) densityView.findViewById(R.id.main_output_kgl_converter);
        outputUnits[2] = (TextView) densityView.findViewById(R.id.main_output_kgm3_converter);
        outputUnits[3] = (TextView) densityView.findViewById(R.id.main_output_sg_converter);

        inputLabel = (TextView) densityView.findViewById(R.id.converter_ppg_title);
        inputUnit = (TextView) densityView.findViewById(R.id.converter_ppg_unit);

        inputValue = (EditText) densityView.findViewById(R.id.main_input_density_converter);

        currentSpinnerItem = starting_unit_spinner.getSelectedItem().toString();

        CurrentLabelandUnitSpinner(currentSpinnerItem);

        starting_unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedNameNow = starting_unit_spinner.getSelectedItem().toString();
                NewLabelandUnitSpinner(selectedNameNow);
                for(int i = 0; i < unitsLabels.length; i++) {
                    if(unitsLabels[i].getText().toString().equals(selectedName)) {
                        unitsLabels[i].setText(oldUnitLabel);
                        inputLabel.setText(selectedName);
                    }
                }

                for(int i = 0; i < unitsUnits.length; i++) {
                    if(unitsUnits[i].getText().toString().equals(selectedUnit)) {
                        unitsUnits[i].setText(oldUnitUnit);
                        inputUnit.setText(selectedUnit);
                    }
                }
                CurrentLabelandUnitSpinner(selectedNameNow);

                try {
                    double valueToConvert = Double.parseDouble(inputValue.getText().toString());
                    String currentUnitOnSpinner = inputUnit.getText().toString();

                    for(int i = 0; i < outputUnits.length; i++) {
                        double convertedValue = unitConverter.DensityConverter(currentUnitOnSpinner, unitsUnits[i].getText().toString(), valueToConvert);

                        Log.d(unitsLabels[i].getText().toString(), String.valueOf(convertedValue) + " | " + unitsUnits[i].getText().toString());

                        convertedValue = Round3Decimals(convertedValue);

                        outputUnits[i].setText(String.valueOf(convertedValue));
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
                    double valueToConvert = Double.parseDouble(inputValue.getText().toString());
                    String currentUnitOnSpinner = inputUnit.getText().toString();

                    for(int i = 0; i < outputUnits.length; i++) {
                        double convertedValue = unitConverter.DensityConverter(currentUnitOnSpinner, unitsUnits[i].getText().toString(), valueToConvert);

                        convertedValue = Round3Decimals(convertedValue);

                        outputUnits[i].setText(String.valueOf(convertedValue));
                    }

                } catch (NumberFormatException nfe) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return densityView;
    }

    void CurrentLabelandUnitSpinner(String currentSpinnerValue) {
        String[] splitCurrentSpinnerItem = currentSpinnerValue.split(", ");
        oldUnitLabel = splitCurrentSpinnerItem[0];
        oldUnitUnit = splitCurrentSpinnerItem[1];
    }

    void NewLabelandUnitSpinner(String newSpinnerValue) {
        String[] splitCurrentSpinnerItem = newSpinnerValue.split(", ");
        selectedName = splitCurrentSpinnerItem[0];
        selectedUnit = splitCurrentSpinnerItem[1];
    }

    double Round3Decimals(double value) {
        double tempValue = value * 100;
        tempValue = Math.round(tempValue);
        return tempValue / 100;
    }
}
