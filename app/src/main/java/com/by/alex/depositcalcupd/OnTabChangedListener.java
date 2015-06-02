package com.by.alex.depositcalcupd;

public interface OnTabChangedListener {
    void saveFirstTabData(String spn_currency, float summ, int timeperiod, int spn_tpr,
                                 String dateBegin, String dateEnd, float profit, float percent_grow);
    void saveSecondTabData(String spn_currency, float conversion, float profit, float percent_grow,
                            boolean inverted_conversion );
    void saveCompareTabData(int position); // TO DO not used
}
