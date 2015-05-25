package com.by.alex.depositcalcupd;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class Formatter {

    DecimalFormat myFormat = new DecimalFormat("#,##0.00");

    public String format (float f) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        return myFormat.format(f);
    }

    public float parseNumber (String str) {
        float number;
        try {
            number = myFormat.parse(str).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
            number = Float.parseFloat(str);
        }
        return number;
    }
}
