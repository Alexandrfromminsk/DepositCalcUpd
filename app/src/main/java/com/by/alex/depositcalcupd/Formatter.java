package com.by.alex.depositcalcupd;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;


public class Formatter {

    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.UK);

    DecimalFormat summFormat = new DecimalFormat("#,###");
    DecimalFormat myFormat = new DecimalFormat("#,##0.00", otherSymbols);
    DecimalFormat ExcRateFormat =  new DecimalFormat();


    public String format (float f) {
        return myFormat.format(f).replace(","," ");
    }

    public String formatSumm (long l) {

        return summFormat.format(l).replace(",", " ");
    }

    public String formatExcRate (float l) {
        StringBuilder format = new StringBuilder();
        format.append("#,##0.00");
        if (l<1) {
            if (l < 0.00001)
                format.append("0000000");
            else if (l < 0.0001)
                format.append("000000");
            else if (l < 0.0001)
                format.append("000000");
            else if (l < 0.001)
                format.append("00000");
            else if (l < 0.01)
                format.append("0000");
            else if (l < 0.1)
                format.append("00");
        }

        ExcRateFormat = new DecimalFormat(format.toString());

        return ExcRateFormat.format(l).replace(",", " ");
    }

    public float parseNumber (String str) {
        str=str.replace(" ", ",");
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
        try {
            number = (long) summFormat.parse(str.replace(",", "").replace(" ", ""));
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
        str=str.replace(" ", ",");
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
