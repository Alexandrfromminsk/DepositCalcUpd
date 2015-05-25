package com.by.alex.depositcalcupd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;


public class CurrencyTwoFragment extends Fragment {

    EditText edtCurrencyA, edtExcRateNow, edtPercentB;
    TextView edtDateEnd, txtProfitBValue, txtGrowValue, txtFullSummValue, edtSummAvalue,
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

    private Float summFromFirstTab = (float)1;
    Formatter f = new Formatter();

    OnTabChangedListener mCallback;

//    public interface OnTabChangedListener {
//        public void saveSecondTabData(int position);
//    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTabChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTabChangedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cur_two_fragment, container,false);

        mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        edtExcRateNow = (EditText) rootView.findViewById(R.id.edtExchangeRateB);
        edtSummAvalue = (TextView) rootView.findViewById(R.id.edtSummBvalue);
        edtPercentB = (EditText) rootView.findViewById(R.id.edtPercentB);


        edtExcRateNow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                edtSummAvalue.setText(f.format(calc_summ()));
                calc_it();
            }

            @Override
            public void afterTextChanged(Editable editable) {  }
        });

        edtPercentB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                calc_it();
            }

            @Override
            public void afterTextChanged(Editable editable) {    }
        });

        edtBeginDate = (TextView) rootView.findViewById(R.id.edtBeginDateB);
        edtTimeperiod = (TextView) rootView.findViewById(R.id.edtTimeperiodB);
        edtDateEnd = (TextView) rootView.findViewById(R.id.edtEndDateB);
        txtProfitBValue = (TextView) rootView.findViewById(R.id.txtProfitB);
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
        spnCapital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calc_it();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spnCurrency.setAdapter(adapter);

        spnTimeperiod.setEnabled(false);
        edtTimeperiod.setEnabled(false);


        if(savedInstanceState == null){
            loadSavedSettings();

        }else {
            loadSavedInstanceState(savedInstanceState);
        }

        calc_it();

        return rootView;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtPercentB", edtPercentB.getText().toString());
        outState.putString("edtTimeperiod", edtTimeperiod.getText().toString());
        outState.putString("edtBeginDate", edtBeginDate.getText().toString());
        outState.putString("edtDateEndB", edtDateEnd.getText().toString());
        outState.putString("edtSummAvalue", edtSummAvalue.getText().toString());
        outState.putString("txtProfitBValue", txtProfitBValue.getText().toString());
        outState.putString("txtGrowValue", txtGrowValue.getText().toString());
        outState.putString("txtFullSummValue", txtFullSummValue.getText().toString());
        //Spinners
        outState.putInt("spnCapital",spnCapital.getSelectedItemPosition());
        outState.putInt("spnCurrency",spnCurrency.getSelectedItemPosition());
        outState.putInt("spnTimeperiod",spnTimeperiod.getSelectedItemPosition());
    }

    void loadSavedInstanceState(Bundle savedInstanceState) {
        edtTimeperiod.setText(savedInstanceState.getString("edtTimeperiod"));
        edtDateEnd.setText(savedInstanceState.getString("edtDateEnd"));
        edtSummAvalue.setText(savedInstanceState.getString("edtSummAvalue"));
        edtPercentB.setText(savedInstanceState.getString("edtPercentB"));
        txtProfitBValue.setText(savedInstanceState.getString("txtProfitBValue"));
        txtGrowValue.setText(savedInstanceState.getString("txtGrowValue"));
        txtFullSummValue.setText(savedInstanceState.getString("txtFullSummValue"));
        //Spinners
        spnCapital.setSelection(savedInstanceState.getInt("spnCapital"));
        spnCurrency.setSelection(savedInstanceState.getInt("spnCurrency"));
        spnTimeperiod.setSelection(savedInstanceState.getInt("spnTimeperiod"));
    }

    void loadSavedSettings(){
        edtDateEnd.setText(mSettings.getString(END_DATE_B, "01-02-2017"));
        edtExcRateNow.setText(mSettings.getString(EXC_RATE_NOW_B, "15000"));
        edtSummAvalue.setText(mSettings.getString(SUMM_B_VALUE, "1000000"));
        edtPercentB.setText(mSettings.getString(PERCENT_B, "50"));
        edtTimeperiod.setText(mSettings.getString(TIMEPERIOD_B, "365"));
        txtProfitBValue.setText(mSettings.getString(PROFIT_B, "0"));
        txtGrowValue.setText(mSettings.getString(GROW_B, "0"));
        txtFullSummValue.setText(mSettings.getString(GROW_B, "0"));
        edtBeginDate.setText(mSettings.getString(BEGIN_DATE_B, "01-02-2016"));
        spnCapital.setSelection(mSettings.getInt(SPN_CAPITAL_B, 0));
        spnTimeperiod.setSelection(mSettings.getInt(SPN_TIMELINE_B,0));
        spnCurrency.setSelection(mSettings.getInt(SPN_CURRENCY_B, 0));

    }

    void saveSettings(){
        SharedPreferences.Editor ed = mSettings.edit();
        ed.putString(TIMEPERIOD_B, edtTimeperiod.getText().toString());
        ed.putString(BEGIN_DATE_B, edtBeginDate.getText().toString());
        ed.putString(END_DATE_B, edtDateEnd.getText().toString());
        ed.putString(EXC_RATE_NOW_B, edtExcRateNow.getText().toString());
        ed.putString(SUMM_B_VALUE, edtSummAvalue.getText().toString());
        ed.putString(PERCENT_B, edtPercentB.getText().toString());
        ed.putString(PROFIT_B, txtProfitBValue.getText().toString());
        ed.putString(GROW_B, txtGrowValue.getText().toString());
        ed.putString(FULL_SUMM_VALUE_B, txtFullSummValue.getText().toString());
        ed.putInt(SPN_TIMELINE_B, spnTimeperiod.getSelectedItemPosition());
        ed.putInt(SPN_CAPITAL_B, spnCapital.getSelectedItemPosition());
        ed.putInt(SPN_CURRENCY_B, spnCurrency.getSelectedItemPosition());

        ed.commit();

    }
    public boolean allFieldsWithData(){
        return (edtPercentB.getText().length()>0)&(edtSummAvalue.getText().length()>0)&(edtTimeperiod.getText().length()>0);

    }
    public void calc_it(){
        if (allFieldsWithData()) {
            Float s = f.parseNumber(edtSummAvalue.getText().toString());
            Float pr = Float.parseFloat(edtPercentB.getText().toString());
            Integer d = Integer.parseInt(edtTimeperiod.getText().toString());
            int dmy = spnTimeperiod.getSelectedItemPosition();
            if (dmy==1) d*=30;
            if (dmy==2) d*=365;
            int cap = spnCapital.getSelectedItemPosition();
            Float[] profit = Calculator.calcProfit(s, pr, d,cap);

            txtGrowValue.setText(f.format(profit[Calculator.PERCENT]));
            txtProfitBValue.setText(f.format(profit[Calculator.PROFIT]));
            txtFullSummValue.setText(f.format(profit[Calculator.FULLSUMM]));
        }

    }

    private Float formatTwoDecimals (float f) {
        DecimalFormat twoFForm = new DecimalFormat("#.##");
        return Float.parseFloat(twoFForm.format(f));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveSettings();
    }


    private float calc_summ(){
        float conv;
        try {
            conv = Float.parseFloat(edtExcRateNow.getText().toString());
            if (conv == 0) conv  = (float)1;
            }
        catch (NumberFormatException e)        {
            e.printStackTrace();
            Log.e("Calc", "Issue wuth edtExcRateNow field ");
            conv=(float) 1;
        }
        return conv * this.summFromFirstTab;
    }

    public void saveData(){
        float conversion = Float.valueOf(edtExcRateNow.getText().toString());
        float profit = f.parseNumber(txtProfitBValue.getText().toString());
        mCallback.saveSecondTabData(spnCurrency.getSelectedItem().toString(), conversion, profit);
    }

    public void setDataFromFirstTab(String spn_currency, float summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd){
        edtBeginDate.setText(dateBegin);
        edtDateEnd.setText(dateEnd);
        spnTimeperiod.setSelection(spn_tpr);
        edtTimeperiod.setText(Integer.toString(timeperiod));

        //TO DO
        // samething with currency string
        //String cur  = spn_currency;

        this.summFromFirstTab = summ;
        edtSummAvalue.setText(String.valueOf(calc_summ()));
    }
}
