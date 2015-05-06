package com.by.alex.depositcalcupd;

public interface OnTabChangedListener {
    public void saveFirstTabData(int position);
    public void saveSecondTabData(int position);
    public void saveCompareFragmentTabData(int position);
    public void getDataForSecondTab(int position);
    public void getDataForCompareTab(int position);
}
