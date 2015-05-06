package com.by.alex.depositcalcupd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class CurrencyTwoFragment extends Fragment implements TextWatcher {

    EditText edtCurrencyA, edtExcRateNow, edtPercentB;
    TextView edtDateEnd, txtProfitAValue, txtGrowValue, txtFullSummValue, edtSummAvalue,
            edtBeginDate, edtTimeperiod;

    Spinner spnTimeperiod, spnCapital, spnCurrency;

    SharedPreferences mSettings;
    public static final String BEGIN_DATE_B = "BEGIN_DATE_B";
    public static final String END_DATE_B = "END_DATE_B";
    public static final String EXC_RATE_NOW_B = "EXC_RATE_NOW_B";
    public static final String SUMM_B_VALUE = "SUMM_B_VALUE";
    public static final String PERCENT_B = "PERCENT_B";
    public static final String TIMEPERIOD_B = "TIMEPERIOD_B";
    public static final String PROFIT_B = "PROFIT_B";
    public static final String GROW_B = "GROW_B";
    public static final String FULL_SUMM_VALUE_B = "FULL_SUMM_VALUE_B";
    public static final String SPN_TIMELINE_B = "SPN_TIMELINE_B";
    public static final String SPN_CAPITAL_B = "SPN_CAPITAL_B";
    public static final String SPN_CURRENCY_B = "SPN_CURRENCY_B";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cur_two_fragment, container,false);

        mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        edtExcRateNow = (EditText) rootView.findViewById(R.id.edtExchangeRateB);
        edtSummAvalue = (TextView) rootView.findViewById(R.id.edtSummBvalue);
        edtPercentB = (EditText) rootView.findViewById(R.id.edtPercentB);

        edtBeginDate = (TextView) rootView.findViewById(R.id.edtBeginDateB);
        edtTimeperiod = (TextView) rootView.findViewById(R.id.edtTimeperiodB);
        edtDateEnd = (TextView) rootView.findViewById(R.id.edtEndDateB);
        txtProfitAValue = (TextView) rootView.findViewById(R.id.txtProfitB);
        txtGrowValue = (TextView)rootView.findViewById(R.id.txtGrowB);
        txtFullSummValue =(TextView)rootView.findViewById(R.id.txtFullSummValueB);


        //Spinners
        spnCurrency = (Spinner) rootView.findViewById(R.id.spnCurrencyB);
        spnTimeperiod = (Spinner) rootView.findViewById(R.id.spnTimeperiodB);
        spnCapital = (Spinner) rootView.findViewById(R.id.spnCapitalB);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.timeperiods_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spnTimeperiod.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.capitals_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spnCapital.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spnCurrency.setAdapter(adapter);
        spnTimeperiod.setEnabled(false);

        edtPercentB.addTextChangedListener(this);
        edtExcRateNow.addTextChangedListener(this);


        if(savedInstanceState == null){
            loadSavedSettings();

        }else {
            edtTimeperiod.setText(savedInstanceState.getString("edtTimeperiod"));
        }

        calc_it();

        return rootView;
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtTimeperiodB", edtTimeperiod.getText().toString());
        outState.putString("edtBeginDateB", edtBeginDate.getText().toString());
        outState.putString("edtDateEndB", edtDateEnd.getText().toString());

    }

    void loadSavedSettings(){
        edtDateEnd.setText(mSettings.getString(END_DATE_B, "01-02-2017"));
        edtExcRateNow.setText(mSettings.getString(EXC_RATE_NOW_B, "15000"));
        edtSummAvalue.setText(mSettings.getString(SUMM_B_VALUE, "1000000"));
        edtPercentB.setText(mSettings.getString(PERCENT_B, "50"));
        edtTimeperiod.setText(mSettings.getString(TIMEPERIOD_B, "365"));
        txtProfitAValue.setText(mSettings.getString(PROFIT_B, "0"));
        txtGrowValue.setText(mSettings.getString(GROW_B, "0"));
        txtFullSummValue.setText(mSettings.getString(GROW_B, "0"));
        edtBeginDate.setText(mSettings.getString(BEGIN_DATE_B, "01-02-2016"));
        spnCapital.setSelection(mSettings.getInt(SPN_CAPITAL_B,0));
        spnTimeperiod.setSelection(mSettings.getInt(SPN_TIMELINE_B,0));

    }

    void saveSettings(){
        SharedPreferences.Editor ed = mSettings.edit();
        ed.putString(TIMEPERIOD_B, edtTimeperiod.getText().toString());
        ed.putString(BEGIN_DATE_B, edtBeginDate.getText().toString());
        ed.putString(END_DATE_B, edtDateEnd.getText().toString());
        ed.putString(EXC_RATE_NOW_B, edtExcRateNow.getText().toString());
        ed.putString(SUMM_B_VALUE, edtSummAvalue.getText().toString());
        ed.putString(PERCENT_B, edtPercentB.getText().toString());;
        ed.putString(PROFIT_B, txtProfitAValue.getText().toString());
        ed.putString(GROW_B, txtGrowValue.getText().toString());
        ed.putString(FULL_SUMM_VALUE_B, txtFullSummValue.getText().toString());
        ed.putInt(SPN_TIMELINE_B, (int) spnTimeperiod.getSelectedItemPosition());
        ed.putInt(SPN_CAPITAL_B, (int) spnCapital.getSelectedItemPosition());

        ed.commit();

    }
    public boolean allFieldsWithData(){
        return (edtPercentB.getText().length()>0)&(edtSummAvalue.getText().length()>0)&(edtTimeperiod.getText().length()>0);

    }
    public void calc_it(){
        if (allFieldsWithData()) {
            Float s = Float.parseFloat(edtSummAvalue.getText().toString());
            Float pr = Float.parseFloat(edtPercentB.getText().toString());
            Integer d = Integer.parseInt(edtTimeperiod.getText().toString());
            int dmy = spnTimeperiod.getSelectedItemPosition();
            if (dmy==1) d*=30;
            if (dmy==2) d*=365;
            int cap = spnCapital.getSelectedItemPosition();
            Float[] profit = Calculator.calcProfit(s, pr, d,cap);

            txtGrowValue.setText(profit[Calculator.PERCENT].toString());
            txtProfitAValue.setText(profit[Calculator.PROFIT].toString());
            txtFullSummValue.setText(profit[Calculator.FULLSUMM].toString());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveSettings();
        Toast.makeText(getActivity(), "ondestroy Second tab".toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "onpause Second tab".toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        calc_it();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
