package com.by.alex.depositcalcupd;

public interface OnTabChangedListener {
    public void saveFirstTabData(String spn_currency, float summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd, float profit);
    public void saveSecondTabData(String spn_currency, float conversion, float profit);
    public void saveCompareTabData(int position);
}
