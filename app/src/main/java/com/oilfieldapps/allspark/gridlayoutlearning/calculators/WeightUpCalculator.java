package com.oilfieldapps.allspark.gridlayoutlearning.calculators;

import android.util.Log;

/**
 * Created by Allspark on 19/06/2017.
 */

public class WeightUpCalculator {

    public double volumeIncrease;
    public double totalVolume;
    public double materialNeeded;
    public double materialSacksAmount;

    private Converter converter = new Converter();

    public WeightUpCalculator() {

    }

    public void CalculateData(String unitsDens, String unitsVol, String unitsWeight, String weightingAgent, String initialMud, String initialVolume, String desiredMud) {

        double ro1 = Double.parseDouble(initialMud);
        double ro2 = Double.parseDouble(weightingAgent);
        double ro3 = Double.parseDouble(desiredMud);
        double vol1 = Double.parseDouble(initialVolume);

        volumeIncrease = vol1 * ((ro3 - ro1)/(ro2 - ro3));

        double barrels = converter.VolumeConverter(unitsVol, "bbl", volumeIncrease);

        materialNeeded = barrels * converter.materialFactor(unitsDens)* ro2;
        Log.d("mat needed check: ", unitsDens + " | " + converter.materialFactor(unitsDens));
        materialNeeded = converter.MassConverter("lb", unitsWeight, materialNeeded);

        totalVolume = volumeIncrease + vol1;

    }

    public void CalculateSacks(String sacksSize, String weightUnit, String packageUnit) {

        double sacksSizeDouble = Double.parseDouble(sacksSize);

        materialSacksAmount = materialNeeded / sacksSizeDouble * correctSackFactor(weightUnit, packageUnit);

    }

    public double getVolumeIncrease() {
        return volumeIncrease;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public double getMaterialNeeded() {
        return materialNeeded;
    }

    public double getMaterialSacksAmount() {
        return materialSacksAmount;
    }

    public double correctSackFactor(String weightUnit, String packegeUnit) {

        if(weightUnit.contains("kg") && packegeUnit.contains("kg")) {
            return 1;
        } else if (weightUnit.contains("kg") && packegeUnit.contains("lb")) {
            return 2.2046223302272;
        } else if (weightUnit.contains("lb") && packegeUnit.contains("lb")) {
            return 1;
        } else if (weightUnit.contains("lb") && packegeUnit.contains("kg")) {
            return 0.45359243;
        }
        return 1;
    }

}


