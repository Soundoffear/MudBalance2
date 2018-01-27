package com.oilfieldapps.allspark.gridlayoutlearning.calculators;

import android.util.Log;

/**
 * Created by Allspark on 20/06/2017.
 */

public class Converter {

    public Converter() {

    }

    public double DensityConverter(String oldDensityUnit, String currentDensityUnit, double value) {
        double finalValue;
        switch (oldDensityUnit) {
            case "ppg":
                switch (currentDensityUnit) {
                    case "SG":
                        finalValue = value * 0.1198264;
                        return finalValue;
                    case "kg/l":
                        finalValue = value * 0.1198264;
                        return finalValue;
                    case "kg/m3":
                        finalValue = value * 119.8264;
                        return finalValue;
                    case "lb/ft3":
                        finalValue = value * 7.480519350799;
                        return finalValue;
                    default:
                        return value;
                }
            case "SG":
                switch (currentDensityUnit) {
                    case "ppg":
                        finalValue = value * 8.3454063545262;
                        return finalValue;
                    case "kg/l":
                        finalValue = value * 1;
                        return finalValue;
                    case "kg/m3":
                        finalValue = value * 1000;
                        return finalValue;
                    case "lb/ft3":
                        finalValue = value * 62.427973725314;
                        return finalValue;
                    default:
                        return value;
                }
            case "kg/l":
                switch (currentDensityUnit) {
                    case "SG":
                        finalValue = value * 1;
                        return finalValue;
                    case "ppg":
                        finalValue = value * 8.3454063545262;
                        return finalValue;
                    case "kg/m3":
                        finalValue = value * 1000;
                        return finalValue;
                    case "lb/ft3":
                        finalValue = value * 62.427973725314;
                        return finalValue;
                    default:
                        return value;
                }
            case "kg/m3":
                switch (currentDensityUnit) {
                    case "SG":
                        finalValue = value * 0.001;
                        return finalValue;
                    case "kg/l":
                        finalValue = value * 0.001;
                        return finalValue;
                    case "ppg":
                        finalValue = value * 0.0083454063545262;
                        return finalValue;
                    case "lb/ft3":
                        finalValue = value * 0.062427973725314;
                        return finalValue;
                    default:
                        return value;
                }
            case "lb/ft3":
                switch (currentDensityUnit) {
                    case "SG":
                        finalValue = value * 0.01601846;
                        return finalValue;
                    case "kg/l":
                        finalValue = value * 0.01601846;
                        return finalValue;
                    case "kg/m3":
                        finalValue = value * 16.01846;
                        return finalValue;
                    case "ppg":
                        finalValue = value * 0.13368055787372;
                        return finalValue;
                    default:
                        return value;
                }

            default:
                return 1;
        }
    }

    public double VolumeConverter(String oldVolumeUnit, String newVolumeUnit, double value) {
        double finalVolValue;
        switch (oldVolumeUnit) {
            case "bbl":
                switch (newVolumeUnit) {
                    case "gal":
                        Log.d("testing Gal", "gal gal gal");
                        finalVolValue = value * 42.000001339881;
                        return finalVolValue;
                    case "ft3":
                        Log.d("testing FT3", "FT3");
                        finalVolValue = value * 5.6145835124493;
                        return finalVolValue;
                    case "m3":
                        Log.d("testing M3", "M3");
                        finalVolValue = value * 0.1589873;
                        return finalVolValue;
                    case "l":
                        finalVolValue = value * 158.9873;
                        return finalVolValue;
                    default:
                        return value;
                }
            case "gal":
                switch (newVolumeUnit) {
                    case "bbl":
                        finalVolValue = value * 0.023809523049954;
                        return finalVolValue;
                    case "ft3":
                        finalVolValue = value * 0.13368055555556;
                        return finalVolValue;
                    case "m3":
                        finalVolValue = value * 0.003785411784;
                        return finalVolValue;
                    case "l":
                        finalVolValue = value * 3.785411784;
                        return finalVolValue;
                    default:
                        return value;
                }
            case "ft3":
                switch (newVolumeUnit) {
                    case "gal":
                        finalVolValue = value * 7.4805194805195;
                        return finalVolValue;
                    case "bbl":
                        finalVolValue = value * 0.17810760099706;
                        return finalVolValue;
                    case "m3":
                        finalVolValue = value * 0.028316846592;
                        return finalVolValue;
                    case "l":
                        finalVolValue = value * 28.316846592;
                        return finalVolValue;
                    default:
                        return value;
                }
            case "m3":
                switch (newVolumeUnit) {
                    case "gal":
                        finalVolValue = value * 264.17205235815;
                        return finalVolValue;
                    case "ft3":
                        finalVolValue = value * 35.314666721489;
                        return finalVolValue;
                    case "bbl":
                        finalVolValue = value * 6.2898105697751;
                        return finalVolValue;
                    case "l":
                        finalVolValue = value * 1000;
                        return finalVolValue;
                    default:
                        return value;
                }
            case "l":
                switch (newVolumeUnit) {
                    case "gal":
                        finalVolValue = value * 0.26417205235815;
                        return finalVolValue;
                    case "ft3":
                        finalVolValue = value * 0.035314666721489;
                        return finalVolValue;
                    case "m3":
                        finalVolValue = value * 0.001;
                        return finalVolValue;
                    case "bbl":
                        finalVolValue = value * 0.0062898105697751;
                        return finalVolValue;
                    default:
                        return value;
                }
            default:
                return value;
        }

    }

    public double MassConverter(String oldMassUnit, String newMassUnit, double value) {
        double finalValue;
        switch (oldMassUnit) {
            case "kg":
                switch (newMassUnit) {
                    case "lb":
                        finalValue = value * 2.2046223302272;
                        return finalValue;
                    case "g":
                        finalValue = value * 1000;
                        return finalValue;
                    case "ton":
                        finalValue = value * 0.001;
                        return finalValue;
                    case "oz.":
                        finalValue = value * 35.27396194958;
                        return finalValue;
                    case "cd":
                        finalValue = value * 5000;
                        return finalValue;
                    default:
                        return value;
                }
            case "g":
                switch (newMassUnit) {
                    case "lb":
                        finalValue = value * 0.0022046223302272;
                        return finalValue;
                    case "kg":
                        finalValue = value * 0.001;
                        return finalValue;
                    case "ton":
                        finalValue = value * 0.000001;
                        return finalValue;
                    case "oz.":
                        finalValue = value * 0.03527396194958;
                        return finalValue;
                    case "cd":
                        finalValue = value * 5;
                        return finalValue;
                    default:
                        return value;
                }
            case "ton":
                switch (newMassUnit) {
                    case "lb":
                        finalValue = value * 2204.6223302272;
                        return finalValue;
                    case "g":
                        finalValue = value * 1000000;
                        return finalValue;
                    case "kg":
                        finalValue = value * 1000;
                        return finalValue;
                    case "oz.":
                        finalValue = value * 35273.96194958;
                        return finalValue;
                    case "cd":
                        finalValue = value * 5000000;
                        return finalValue;
                    default:
                        return value;
                }
            case "oz.":
                switch (newMassUnit) {
                    case "lb":
                        finalValue = value * 0.062499991732666;
                        return finalValue;
                    case "g":
                        finalValue = value * 28.349523125;
                        return finalValue;
                    case "ton":
                        finalValue = value * 0.000028349523125;
                        return finalValue;
                    case "kg":
                        finalValue = value * 0.028349523125;
                        return finalValue;
                    case "cd":
                        finalValue = value * 141.747615625;
                        return finalValue;
                    default:
                        return value;
                }
            case "lb":
                switch (newMassUnit) {
                    case "kg":
                        finalValue = value * 0.45359243;
                        return finalValue;
                    case "g":
                        finalValue = value * 453.59243;
                        return finalValue;
                    case "ton":
                        finalValue = value * 0.00045359243;
                        return finalValue;
                    case "oz.":
                        finalValue = value * 16.000002116438;
                        return finalValue;
                    case "cd":
                        finalValue = value * 2267.96215;
                        return finalValue;
                    default:
                        return value;
                }
            case "cd":
                switch (newMassUnit) {
                    case "lb":
                        finalValue = value * 0.00044092446604543;
                        return finalValue;
                    case "g":
                        finalValue = value * 0.2;
                        return finalValue;
                    case "ton":
                        finalValue = value * 0.0000002;
                        return finalValue;
                    case "oz.":
                        finalValue = value * 0.0070547923899161;
                        return finalValue;
                    case "kg":
                        finalValue = value * 0.0002;
                        return finalValue;
                    default:
                        return value;
                }
            default:
                return value;
        }
    }

    public double diameterConverter (String oldUnit, String newUnit, double value) {
        switch (oldUnit) {
            case "in": switch (newUnit) {
                case "mm":
                    return value * 25.4;
                case "cm":
                    return value * 2.54;
                default:
                    return value;
            }
            case "mm": switch (newUnit) {
                case "in":
                    return value * 0.03937;
                case "cm":
                    return value * 0.1;
                default:
                    return value;
            }
            case "cm": switch (newUnit) {
                case "in":
                    return value * 0.393701;
                case "mm":
                    return value * 10;
                default:
                    return value;
            }
            default:
                return value;
        }
    }

    public double materialFactor(String densityUnit) {
        if(densityUnit.equals("ppg")) {
            return 42.000001339881;
        } else if (densityUnit.equals("SG")) {
            return 158.9873 * 2.2046223302272;
        } else if (densityUnit.equals("kg/l")) {
            return 158.9873 * 2.2046223302272;
        } else if (densityUnit.equals("lb/ft3")) {
            return 5.6145835124493;
        } else if(densityUnit.equals("kg/m3")) {
            return 0.1589873;
        }

        return 1;
    }

    double FROM_BBLM = 3.28048;
    double FROM_M3FT = 0.1589873;
    double FROM_M3M = 0.524597;
    double FROM_LM = 0.001917;

    public double ConvertToBBLFT(double currentValue, String currentUnit) {
        if(currentUnit.equals("bbl/m")) {
            return currentValue / FROM_BBLM;
        } else if (currentUnit.equals("m3/ft")) {
            return currentValue / FROM_M3FT;
        } else if (currentUnit.equals("m3/m")) {
            return currentValue / FROM_M3M;
        } else if (currentUnit.equals("l/m")) {
            return currentValue / FROM_LM;
        } else if (currentUnit.equals("bbl/ft")) {
            return currentValue;
        }

        return currentValue;
    }

    public double ConvertToFT(double currentValue, String currentUnit) {
        if(currentUnit.contains("m")) {
            return 3.28048 * currentValue;
        }
        return currentValue;
    }
}
