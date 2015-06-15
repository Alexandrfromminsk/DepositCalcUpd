package com.by.alex.depositcalcupd;

import java.text.DecimalFormat;
import java.text.ParseException;


public class Formatter {

    DecimalFormat summFormat = new DecimalFormat("#,###");
    DecimalFormat myFormat = new DecimalFormat("#,##0.00");
    DecimalFormat ExcRateFormat = myFormat;


    public String format (float f) {
        return myFormat.format(f).replace(","," ");
    }

    public String formatSumm (long l) {
        //DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
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

    public long parseSumm(String str) {
        long number;
        try {
            number = Long.parseLong(str.replace(",", "").replace(" ", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            number = 1;
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
