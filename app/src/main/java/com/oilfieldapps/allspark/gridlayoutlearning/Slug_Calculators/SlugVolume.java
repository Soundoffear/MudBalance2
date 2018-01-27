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

public class SlugVolume extends Fragment {

    EditText input_density_current_mud;
    EditText input_density_slug;
    EditText input_dry_pipe_length;
    EditText input_drill_pipe_id;

    TextView units_density_current_mud;
    TextView units_density_slug;
    TextView units_dry_pipe_length;
    TextView units_drill_pipe_id;

    TextView output_volume_required;
    TextView units_volume_required;

    TextView back_flow_label;
    TextView back_flow_output;
    TextView back_flow_unit;

    Button slug_calculate;
    Button slug_clearData;

    Converter converter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.slug_volume_required_layout, container, false);

        GetAllViews(contentView);
        SetUnits();

        slug_calculate = contentView.findViewById(R.id.slug_calculate);
        slug_clearData = contentView.findViewById(R.id.slug_clear_data);

        converter = new Converter();

        slug_clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    input_density_current_mud.setText(getString(R.string.zero));
                    input_density_slug.setText(getString(R.string.zero));
                    input_dry_pipe_length.setText(getString(R.string.zero));
                    input_drill_pipe_id.setText(getString(R.string.zero));

                    output_volume_required.setText(getString(R.string.zero));
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

        slug_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double ro_current_mud = Double.parseDouble(input_density_current_mud.getText().toString());
                    double ro_slug = Double.parseDouble(input_density_slug.getText().toString());
                    double length_dry_pipe = Double.parseDouble(input_dry_pipe_length.getText().toString());
                    double drill_pipe_id = Double.parseDouble(input_drill_pipe_id.getText().toString());

                    ro_current_mud = converter.DensityConverter(units_density_current_mud.getText().toString(), "ppg", ro_current_mud);
                    ro_slug =  converter.DensityConverter(units_density_slug.getText().toString(), "ppg", ro_slug);
                    length_dry_pipe = converter.ConvertToFT(length_dry_pipe, units_dry_pipe_length.getText().toString());
                    drill_pipe_id = converter.diameterConverter(units_drill_pipe_id.getText().toString(), getResources().getString(R.string.in), drill_pipe_id);

                    double hydrostaticPressurePSI = ro_current_mud * 0.052 * length_dry_pipe;
                    double pressureDifferencePSIFT = (ro_slug - ro_current_mud) * 0.052;
                    double slugLengthFT = hydrostaticPressurePSI / pressureDifferencePSIFT;
                    double drillPipeCapacity = Math.pow(drill_pipe_id, 2) / 1029.4;
                    double slugVolumeBBL = slugLengthFT * drillPipeCapacity;

                    double convertedSlugVolumeBBL = converter.VolumeConverter("bbl", units_volume_required.getText().toString(), slugVolumeBBL);
                    output_volume_required.setText(new DecimalFormat("0.00").format(convertedSlugVolumeBBL));

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    boolean calculateBackflow = preferences.getBoolean("SLUG_BACK_FLOW", false);

                    if(calculateBackflow) {
                        double emptyStringVolume = length_dry_pipe * Math.pow(drill_pipe_id, 2) / 1029.4;
                        double volumeDisplaced = emptyStringVolume + slugVolumeBBL;
                        Log.d("VOLUME DISP", String.valueOf(volumeDisplaced) + " " + String.valueOf(emptyStringVolume));
                        double finalVol_displacement = converter.VolumeConverter("bbl", units_volume_required.getText().toString(), volumeDisplaced);
                        back_flow_output.setText(new DecimalFormat("0.00").format(finalVol_displacement));
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

    void GetAllViews(View contentView) {
        input_density_current_mud = contentView.findViewById(R.id.slug_input_current_mud_weight);
        input_density_slug = contentView.findViewById(R.id.slug_input_slug_mud_weight);
        input_dry_pipe_length = contentView.findViewById(R.id.slug_input_length_mud_weight);
        input_drill_pipe_id = contentView.findViewById(R.id.slug_input_dpid_mud_weight);

        units_density_current_mud = contentView.findViewById(R.id.slug_unit_current_mud_weight);
        units_density_slug = contentView.findViewById(R.id.slug_unit_slug_mud_weight);
        units_dry_pipe_length = contentView.findViewById(R.id.slug_unit_length_mud_weight);
        units_drill_pipe_id = contentView.findViewById(R.id.slug_unit_dpid_mud_weight);

        output_volume_required = contentView.findViewById(R.id.slug_output_volume_required);
        units_volume_required = contentView.findViewById(R.id.slug_unit_volume_required);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean backFlow = sharedPreferences.getBoolean("SLUG_BACK_FLOW", false);

        back_flow_label = contentView.findViewById(R.id.slug_volume_back_flow_label);
        back_flow_output = contentView.findViewById(R.id.slug_output_volume_back_flow);
        back_flow_unit = contentView.findViewById(R.id.slug_unit_volume_back_flow);

        if(backFlow) {
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
        boolean backFlow = unitsPrefs.getBoolean("SLUG_BACK_FLOW", false);

        units_density_current_mud.setText(densityUnits);
        units_density_slug.setText(densityUnits);
        units_dry_pipe_length.setText(lengthUnits);
        units_drill_pipe_id.setText(pipeCapacityUnits);
        units_volume_required.setText(volumeUnits);

        if(backFlow) {
            back_flow_label.setVisibility(View.VISIBLE);
            back_flow_output.setVisibility(View.VISIBLE);
            back_flow_unit.setVisibility(View.VISIBLE);
            back_flow_unit.setText(volumeUnits);
        } else {
            back_flow_label.setVisibility(View.GONE);
            back_flow_output.setVisibility(View.GONE);
            back_flow_unit.setVisibility(View.GONE);
        }
    }

    public void HideKeyboard() {
        View view = this.getView();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
