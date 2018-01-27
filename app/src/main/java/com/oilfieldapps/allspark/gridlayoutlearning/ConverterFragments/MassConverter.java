package com.oilfieldapps.allspark.gridlayoutlearning.ConverterFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

public class MassConverter extends Fragment {

    TextView[] massLabels = new TextView[5];
    TextView[] massUnits = new TextView[5];
    TextView[] massOutput = new TextView[5];

    TextView inputLabel;
    TextView inputUnit;

    Spinner mass_converter_spinner;

    String inputLabelPart;
    String inputUnitPart;

    String newSpinnerLabel;
    String newSpinnerUnit;

    EditText inputValue;

    Converter unitConverter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View massView = inflater.inflate(R.layout.convert_mass_layout, container, false);

        unitConverter = new Converter();

        mass_converter_spinner = (Spinner) massView.findViewById(R.id.mass_converter_spinner);
        massLabels[0] = (TextView) massView.findViewById(R.id.converter_lb_title);
        massLabels[1] = (TextView) massView.findViewById(R.id.converter_g_title);
        massLabels[2] = (TextView) massView.findViewById(R.id.converter_ton_title);
        massLabels[3] = (TextView) massView.findViewById(R.id.converter_oz_title);
        massLabels[4] = (TextView) massView.findViewById(R.id.converter_cd_title);

        massUnits[0] = (TextView) massView.findViewById(R.id.converter_lb_unit);
        massUnits[1] = (TextView) massView.findViewById(R.id.converter_g_unit);
        massUnits[2] = (TextView) massView.findViewById(R.id.converter_ton_unit);
        massUnits[3] = (TextView) massView.findViewById(R.id.converter_oz_unit);
        massUnits[4] = (TextView) massView.findViewById(R.id.converter_cd_unit);

        massOutput[0] = (TextView) massView.findViewById(R.id.main_output_lb_converter);
        massOutput[1] = (TextView) massView.findViewById(R.id.main_output_g_converter);
        massOutput[2] = (TextView) massView.findViewById(R.id.main_output_ton_converter);
        massOutput[3] = (TextView) massView.findViewById(R.id.main_output_oz_converter);
        massOutput[4] = (TextView) massView.findViewById(R.id.main_output_cd_converter);

        inputLabel = (TextView) massView.findViewById(R.id.converter_kg_title);
        inputUnit = (TextView) massView.findViewById(R.id.converter_kg_unit);

        inputValue = (EditText) massView.findViewById(R.id.main_input_mass_converter);

        String massSpinnerItem = mass_converter_spinner.getSelectedItem().toString();

        SplitInputSpinner(massSpinnerItem);

        mass_converter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newSpinnerItemSelected = mass_converter_spinner.getSelectedItem().toString();
                SplitNewSpinner(newSpinnerItemSelected);

                for (int i = 0; i < massLabels.length; i++) {
                    if (massLabels[i].getText().toString().contains(newSpinnerLabel)) {
                        massLabels[i].setText(inputLabelPart);
                        inputLabel.setText(newSpinnerLabel);
                    }
                }

                for (int i = 0; i < massUnits.length; i++) {
                    if (massUnits[i].getText().toString().contains(newSpinnerUnit)) {
                        massUnits[i].setText(inputUnitPart);
                        inputUnit.setText(newSpinnerUnit);
                    }
                }

                SplitInputSpinner(newSpinnerItemSelected);

                try {
                    double startingValue = Double.parseDouble(inputValue.getText().toString());
                    String currentUnit = inputUnit.getText().toString();
                    for (int i = 0; i < massUnits.length; i++) {
                        String massUnitConverter = massUnits[i].getText().toString();
                        double convertedValue = unitConverter.MassConverter(currentUnit, massUnitConverter, startingValue);

                        massOutput[i].setText(new DecimalFormat("0.00").format(convertedValue));

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

                    double startingValue = Double.parseDouble(inputValue.getText().toString());
                    String currentUnit = inputUnit.getText().toString();
                    for (int i = 0; i < massUnits.length; i++) {
                        String massUnitConverter = massUnits[i].getText().toString();
                        double convertedValue = unitConverter.MassConverter(currentUnit, massUnitConverter, startingValue);

                        massOutput[i].setText(new DecimalFormat("0.00").format(convertedValue));

                    }
                } catch (NumberFormatException nfe) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return massView;
    }

    void SplitInputSpinner(String spinnerInput) {
        String[] splitSpinner = spinnerInput.split(", ");
        inputLabelPart = splitSpinner[0];
        inputUnitPart = splitSpinner[1];
    }

    void SplitNewSpinner(String spinnerNew) {
        String[] splitSpinner = spinnerNew.split(", ");
        newSpinnerLabel = splitSpinner[0];
        newSpinnerUnit = splitSpinner[1];
    }
}
