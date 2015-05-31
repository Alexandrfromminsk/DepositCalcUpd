package com.by.alex.depositcalcupd;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class Formatter {

    DecimalFormat myFormat = new DecimalFormat("#,##0.00");
    DecimalFormat summFormat = new DecimalFormat("#,###");

    public String format (float f) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        return myFormat.format(f).replace(","," ");
    }

    public String formatSumm (long f) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        return summFormat.format(f).replace(",", " ");
    }

    public float parseNumber (String str) {
        str=str.replace(" ", ",");
        float number;
        try {
            number = myFormat.parse(str).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
            number = Float.parseFloat(str);
        }
        return number;
    }

    public long parseSumm(String str) {
        long number = Long.parseLong(str.replace(",", "").replace(" ",""));

        return number;
    }
}
