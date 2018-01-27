package com.oilfieldapps.allspark.gridlayoutlearning.Slug_Calculators;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oilfieldapps.allspark.gridlayoutlearning.calculators.Converter;
import com.oilfieldapps.allspark.gridlayoutlearning.R;

import java.text.DecimalFormat;

/**
 * Created by Allspark on 26/06/2017.
 */

public class SlugWeight extends Fragment {

    EditText slug_volume_input;
    EditText dpid_input;
    EditText mud_density_input;
    EditText dry_pipe_input;

    TextView units_slug_volume_input;
    TextView units_dpid_input;
    TextView units_mud_density_input;
    TextView units_dry_pipe_input;

    TextView output_slug_weight;
    TextView units_slug_weight;

    TextView back_flow_label;
    TextView back_flow_output;
    TextView back_flow_unit;

    Button calculateData;
    Button clearData;

    Converter converter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.slug_weight_required_layout, container, false);

        GetAllView(contentView);

        SetUnits();

        converter = new Converter();

        calculateData = contentView.findViewById(R.id.slug_weight_calculate);
        clearData = contentView.findViewById(R.id.slug_weight_clear);

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    slug_volume_input.setText(getString(R.string.zero));
                    dpid_input.setText(getString(R.string.zero));
                    mud_density_input.setText(getString(R.string.zero));
                    dry_pipe_input.setText(getString(R.string.zero));
                    output_slug_weight.setText(getString(R.string.zero));
                } catch (NumberFormatException nfe) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Wrong Entry").setMessage("One of the fields does not contain number or is empty");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                HideKeyboard();
            }
        });

        calculateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double slug_volume = Double.parseDouble(slug_volume_input.getText().toString());
                    double mud_density = Double.parseDouble(mud_density_input.getText().toString());
                    double drill_pipe_id = Double.parseDouble(dpid_input.getText().toString());
                    double dry_pipe_length = Double.parseDouble(dry_pipe_input.getText().toString());

                    slug_volume = converter.VolumeConverter(units_slug_volume_input.getText().toString(), "bbl", slug_volume);
                    mud_density = converter.DensityConverter(units_mud_density_input.getText().toString(), "ppg", mud_density);
                    drill_pipe_id = converter.diameterConverter(units_dpid_input.getText().toString(), getResources().getString(R.string.in), drill_pipe_id);
                    dry_pipe_length = converter.ConvertToFT(dry_pipe_length, units_dry_pipe_input.getText().toString());

                    Log.d("Converted Data: ", "Vol: " + String.valueOf(slug_volume) + " Ro: " + String.valueOf(mud_density) +
                            " Capacity: " + String.valueOf(drill_pipe_id) + " DryPipe: " + String.valueOf(dry_pipe_length));

                    double drillPipeCapacity = Math.pow(drill_pipe_id, 2) / 1029.4;
                    double lengthOfSlug = slug_volume / drillPipeCapacity;
                    double hydrostaticPressure = mud_density * 0.052 * dry_pipe_length;
                    double weightOfSlug = hydrostaticPressure / 0.052 / lengthOfSlug + mud_density;

                    weightOfSlug = converter.DensityConverter("ppg", units_slug_weight.getText().toString(), weightOfSlug);

                    Log.d("Converted Data: ", units_slug_weight.getText().toString() + " Ro: " + String.valueOf(weightOfSlug));

                    output_slug_weight.setText(new DecimalFormat("0.00").format(weightOfSlug));

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    boolean calculateDisplacement = preferences.getBoolean("SLUG_BACK_FLOW", false);
                    if (calculateDisplacement) {
                        double empty_pipe_volume = dry_pipe_length * Math.pow(drill_pipe_id, 2) / 1029.4;
                        double total_vol_displacement = empty_pipe_volume + slug_volume;
                        double convertedTotalVol = converter.VolumeConverter("bbl", units_slug_volume_input.getText().toString(), total_vol_displacement);
                        back_flow_output.setText(new DecimalFormat("0.00").format(convertedTotalVol));
                    }

                } catch (NumberFormatException nfe) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Wrong Entry").setMessage("One of the fields does not contain number or is empty");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                HideKeyboard();

            }
        });

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetUnits();
    }

    void GetAllView(View contentView) {
        slug_volume_input = contentView.findViewById(R.id.input_slug_volume);
        dpid_input = contentView.findViewById(R.id.input_slug_pipe_id);
        mud_density_input = contentView.findViewById(R.id.input_slug_current_mud_weight);
        dry_pipe_input = contentView.findViewById(R.id.input_slug_current_dry_pipe_length);

        units_slug_volume_input = contentView.findViewById(R.id.slug_volume_unit_slug_volume);
        units_dpid_input = contentView.findViewById(R.id.slug_volume_unit_pipe_id);
        units_mud_density_input = contentView.findViewById(R.id.slug_volume_unit_current_mud_weight);
        units_dry_pipe_input = contentView.findViewById(R.id.slug_volume_unit_dry_pipe_length);

        output_slug_weight = contentView.findViewById(R.id.output_slug_weight);
        units_slug_weight = contentView.findViewById(R.id.slug_output_unit_weight);

        back_flow_label = contentView.findViewById(R.id.output_slug_weight_label_flow_back);
        back_flow_output = contentView.findViewById(R.id.output_slug_weight_flow_back);
        back_flow_unit = contentView.findViewById(R.id.slug_output_unit_weight_flow_back);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean calculateDisplacement = preferences.getBoolean("SLUG_BACK_FLOW", false);

        if (calculateDisplacement) {
            back_flow_label.setVisibility(View.VISIBLE);
            back_flow_output.setVisibility(View.VISIBLE);
            back_flow_unit.setVisibility(View.VISIBLE);
        } else {
            back_flow_label.setVisibility(View.GONE);
            back_flow_output.setVisibility(View.GONE);
            back_flow_unit.setVisibility(View.GONE);
        }

    }

    void SetUnits() {
        SharedPreferences unitsPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String densityUnits = unitsPrefs.getString("SLUG_DENSITY_UNITS", "ppg");
        String lengthUnits = unitsPrefs.getString("SLUG_LENGTH_UNITS", "ft");
        String pipeCapacityUnits = unitsPrefs.getString("SLUG_DRILL_PIPE_ID", getResources().getString(R.string.in));
        String volumeUnits = unitsPrefs.getString("SLUG_VOLUME_UNITS", "bbl");

        units_slug_volume_input.setText(volumeUnits);
        units_dpid_input.setText(pipeCapacityUnits);
        units_mud_density_input.setText(densityUnits);
        units_dry_pipe_input.setText(lengthUnits);
        units_slug_weight.setText(densityUnits);

        boolean displacement = unitsPrefs.getBoolean("SLUG_BACK_FLOW", false);

        if (displacement) {
            back_flow_label.setVisibility(View.VISIBLE);
            back_flow_output.setVisibility(View.VISIBLE);
            back_flow_unit.setVisibility(View.VISIBLE);
        } else {
            back_flow_label.setVisibility(View.GONE);
            back_flow_output.setVisibility(View.GONE);
            back_flow_unit.setVisibility(View.GONE);
        }

        if (displacement) {
            back_flow_unit.setText(volumeUnits);
        }
    }

    public void HideKeyboard() {
        View view = this.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
