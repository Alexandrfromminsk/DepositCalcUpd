package com.by.alex.depositcalcupd;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;


public class Formatter {

    //DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.UK);
    //final String decimalSeparator = ",";

    DecimalFormat summFormat = new DecimalFormat("#,###");
    DecimalFormat myFormat = new DecimalFormat("#,##0.00");
    DecimalFormat ExcRateFormat =  new DecimalFormat("#,###.##");


    public String format (float f) {

        return myFormat.format(f);
    }

    public String formatSumm (long l) {

        return summFormat.format(l);
    }

    public String formatExcRate (float l) {

        if (l<1) {
            ExcRateFormat.applyPattern("#,###.########");
        }
        else {
            ExcRateFormat.applyLocalizedPattern("#,###.##");
        }

        return ExcRateFormat.format(l);
    }

    public float parseNumber (String str) {
        float number;
        try {
            number = myFormat.parse(str).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                number = Float.parseFloat(str);
            } catch (NumberFormatException ee) {
                ee.printStackTrace();
                number = 1;
            }
        }
        return number;
    }

    public long parseSumm(String str){
        long number;
        DecimalFormatSymbols symbols = summFormat.getDecimalFormatSymbols();
        String separator = String.valueOf(symbols.getGroupingSeparator());
        str=str.replace(separator, "");
        try {
            number = summFormat.parse(str.replace(" ", "")).longValue();
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                number = Long.parseLong(str);
            } catch (NumberFormatException ee) {
                ee.printStackTrace();
                number = 1;
            }
        }

        return number;
    }

    public float parseExcRate(String str) {
        float number;
        try {
            number = ExcRateFormat.parse(str).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                number = Float.parseFloat(str);
            } catch (NumberFormatException ee) {
                ee.printStackTrace();
                number = 1;
            }
        }
        return number;
    }
}
