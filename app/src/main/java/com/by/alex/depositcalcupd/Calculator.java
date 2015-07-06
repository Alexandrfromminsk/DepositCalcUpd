package com.by.alex.depositcalcupd;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public  class Calculator {

    static final int PERCENT = 0;
    static final int PROFIT = 1;
    static final int FULLSUMM = 2;


    public static Float[] calcProfit(float SummBegin, float Percent, int Days){

        float profit, fullsumm, percent;
        Float[] result = new Float[3];

        profit = (float) ((SummBegin*Percent*Days)/(365.0*100.0));
        fullsumm = SummBegin + profit;
        percent = (float) (((fullsumm - SummBegin)/SummBegin)*100.0);
        result[PERCENT] = percent;
        result[PROFIT] = profit;
        result[FULLSUMM] = fullsumm;
        return result;
    }
    //difficult percents
    public static Float[] calcProfit(float SummBegin, float Percent, int Days, int Capitalization){

        Float[] result = new Float[3];
        float profit, fullsumm, percent;

        if (Capitalization==0)
            result=calcProfit(SummBegin, Percent, Days);

        if (Capitalization==1) {
            //Capitalization every day

            fullsumm = (float) (SummBegin *Math.pow(1+Percent/(365.0*100.0),Days));
            profit = fullsumm - SummBegin;
            percent = (profit/ SummBegin) * 100;
            result[PERCENT] = percent;
            result[PROFIT] = profit;
            result[FULLSUMM] = fullsumm;
        }

        if (Capitalization==2) {
            //Capitalization every 30 day

            int num_capitalizations = Days/30;

            if (num_capitalizations>0) {

                fullsumm = (float) (SummBegin * Math.pow(1 + Percent * 30.0 / (365.0 * 100.0), num_capitalizations));
                profit = fullsumm - SummBegin;
                percent = (profit / SummBegin) * 100;
                result[PERCENT] = percent;
                result[PROFIT] = profit;
                result[FULLSUMM] = fullsumm;
            }
            else result=calcProfit(SummBegin, Percent, Days);
        }

        if (Capitalization==3) {

            //Capitalization every 90 day (kvartal)

            int num_capitalizations = Days/90;

            if (num_capitalizations>0) {

                fullsumm = (float) (SummBegin * Math.pow(1 + Percent * 90.0 / (365.0 * 100.0), num_capitalizations));
                profit = fullsumm - SummBegin;
                percent = (profit / SummBegin) * 100;
                result[PERCENT] = percent;
                result[PROFIT] = profit;
                result[FULLSUMM] = fullsumm;
            }
            else result=calcProfit(SummBegin, Percent, Days);
        }

        if (Capitalization==4) {

            //Capitalization every 365 day
            int num_capitalizations = Days/365;

            if (num_capitalizations>0) {

                fullsumm = (float) (SummBegin * Math.pow(1 + Percent / 100.0, num_capitalizations));
                profit = fullsumm - SummBegin;
                percent = (profit / SummBegin) * 100;
                result[PERCENT] = percent;
                result[PROFIT] = profit;
                result[FULLSUMM] = fullsumm;
            }
            else result=calcProfit(SummBegin, Percent, Days);
        }

        return result;
    }

    public static Float[] calcProfit(float SummBegin, float Percent, int Capitalization,
                                     String BeginDate, String EndDate) {
       // Float[] result = new Float[3];
        long Days;
        int d=0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date start = sdf.parse(BeginDate);
            Date end = sdf.parse(EndDate);
            long diff = end.getTime() - start.getTime();
            Days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            Days=0;
        }

        try {
            if (Days < Integer.MIN_VALUE || Days > Integer.MAX_VALUE) {
                throw new IllegalArgumentException
                        (Days + " cannot be cast to int without changing its value.");
            } else d = (int) Days;
        }
        catch (IllegalArgumentException ex) {
            Log.e("Calculator","Timeperiod so big");
        }


        Float[] result = calcProfit(SummBegin, Percent, d, Capitalization);
        return result;
    }

}