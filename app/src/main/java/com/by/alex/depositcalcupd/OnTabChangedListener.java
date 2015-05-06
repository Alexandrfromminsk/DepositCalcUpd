package com.by.alex.depositcalcupd;

public interface OnTabChangedListener {
    public void saveFirstTabData(String spn_currency, int summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd, float profit);
    public void saveSecondTabData(int position);
    public void saveCompareFragmentTabData(int position);
    public void getDataForSecondTab(int position);
    public void getDataForCompareTab(int position);
}
