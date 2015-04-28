package com.by.alex.depositcalcupd;


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

            fullsumm = (float) (SummBegin *Math.pow(1+Percent*30.0/(365.0*100.0),Days/30.0));
            profit = fullsumm - SummBegin;
            percent = (profit/ SummBegin) * 100;
            result[PERCENT] = percent;
            result[PROFIT] = profit;
            result[FULLSUMM] = fullsumm;
        }

        if (Capitalization==3) {

            //Capitalization every 90 day (kvartal)

            fullsumm = (float) (SummBegin *Math.pow(1+Percent*90.0/(365.0*100.0),Days/90.0));
            profit = fullsumm - SummBegin;
            percent = (profit/ SummBegin) * 100;
            result[PERCENT] = percent;
            result[PROFIT] = profit;
            result[FULLSUMM] = fullsumm;
        }

        if (Capitalization==4) {

            //Capitalization every 365 day

            fullsumm = (float) (SummBegin *Math.pow(1+Percent/100.0,Days/365.0));
            profit = fullsumm - SummBegin;
            percent = (profit/ SummBegin) * 100;
            result[PERCENT] = percent;
            result[PROFIT] = profit;
            result[FULLSUMM] = fullsumm;
        }

        return result;
    }

}