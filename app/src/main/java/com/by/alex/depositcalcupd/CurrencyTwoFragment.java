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

import java.util.ArrayList;


public class CurrencyTwoFragment extends Fragment {

    EditText edtCurrencyA, edtExcRateNow, edtPercentB;
    TextView edtDateEnd, txtProfitBValue, txtGrowValue, txtFullSummValue, edtSummAvalue,
            edtBeginDate, txtTimeperiod;

    Spinner spnCapital, spnCurrency, spnConversion, spnTypeConversion;

    SharedPreferences mSettings;
    public static final String BEGIN_DATE_B = "BEGIN_DATE_B";
    public static final String END_DATE_B = "END_DATE_B";
    public static final String EXC_RATE_NOW_B = "EXC_RATE_NOW_B";
    public static final String SUMM_B_VALUE = "SUMM_B_VALUE";
    public static final String PERCENT_B = "PERCENT_B";
    public static final String PROFIT_B = "PROFIT_B";
    public static final String GROW_B = "GROW_B";
    public static final String FULL_SUMM_VALUE_B = "FULL_SUMM_VALUE_B";
    public static final String SPN_CAPITAL_B = "SPN_CAPITAL_B";
    public static final String SPN_CURRENCY_B = "SPN_CURRENCY_B";
    public static final String SPN_TYPE_CONVERSION = "SPN_TYPE_CONVERSION";
    public static final String CURRENCY_A = "CURRENCY_A";

    private Float summFromFirstTab = (float)1;
    private int spnTimeperiodNumber, spnTimelineChoice;
    private String  spnPeriod, CurrencyA;
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
        txtTimeperiod = (TextView) rootView.findViewById(R.id.txtTimeperiod);


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
        edtDateEnd = (TextView) rootView.findViewById(R.id.edtEndDateB);
        txtProfitBValue = (TextView) rootView.findViewById(R.id.txtProfitB);
        txtGrowValue = (TextView)rootView.findViewById(R.id.txtGrowB);
        txtFullSummValue =(TextView)rootView.findViewById(R.id.txtFullSummValueB);


        //Spinners
        spnTypeConversion = (Spinner) rootView.findViewById(R.id.spnTypeConversion);
        spnCurrency = (Spinner) rootView.findViewById(R.id.spnCurrencyB);
        spnCapital = (Spinner) rootView.findViewById(R.id.spnCapitalB);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.capitals_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnCurrency.setAdapter(adapter);
        spnCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnTypeConversion.setAdapter(getCurrencyPairs());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        if(savedInstanceState == null){
            loadSavedSettings();

        }else {
            loadSavedInstanceState(savedInstanceState);
        }

        if (txtTimeperiod.getText().toString().length()==0)
            txtTimeperiod.setText(this.spnTimeperiodNumber + " " + this.spnPeriod);

        spnTypeConversion.setAdapter(getCurrencyPairs());
        spnTypeConversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtSummAvalue.setText(String.valueOf(f.format(calc_summ())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        calc_it();

        return rootView;
    }

    private ArrayAdapter<String> getCurrencyPairs () {
        ArrayList<String> conversions = new ArrayList<String>();
        conversions.add(String.format("%s / %s", this.CurrencyA, spnCurrency.getSelectedItem().toString()));
        conversions.add(String.format("%s / %s", spnCurrency.getSelectedItem().toString(), this.CurrencyA));

        ArrayAdapter<String> convAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, conversions);
        convAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  convAdapter;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtPercentB", edtPercentB.getText().toString());
        outState.putString("edtBeginDate", edtBeginDate.getText().toString());
        outState.putString("edtDateEndB", edtDateEnd.getText().toString());
        outState.putString("edtSummAvalue", edtSummAvalue.getText().toString());
        outState.putString("txtProfitBValue", txtProfitBValue.getText().toString());
        outState.putString("txtGrowValue", txtGrowValue.getText().toString());
        outState.putString("txtFullSummValue", txtFullSummValue.getText().toString());
        outState.putString("CurrencyA", this.CurrencyA);
        //!!!AddtxtTimeperiod
        //Spinners
        outState.putInt("spnCapital", spnCapital.getSelectedItemPosition());
        outState.putInt("spnCurrency", spnCurrency.getSelectedItemPosition());
        outState.putInt("spnTypeConversion", spnTypeConversion.getSelectedItemPosition());
    }

    void loadSavedInstanceState(Bundle savedInstanceState) {
        edtDateEnd.setText(savedInstanceState.getString("edtDateEnd"));
        edtSummAvalue.setText(savedInstanceState.getString("edtSummAvalue"));
        edtPercentB.setText(savedInstanceState.getString("edtPercentB"));
        txtProfitBValue.setText(savedInstanceState.getString("txtProfitBValue"));
        txtGrowValue.setText(savedInstanceState.getString("txtGrowValue"));
        txtFullSummValue.setText(savedInstanceState.getString("txtFullSummValue"));
        this.CurrencyA = savedInstanceState.getString("CurrencyA");
        //Spinners
        spnCapital.setSelection(savedInstanceState.getInt("spnCapital"));
        spnCurrency.setSelection(savedInstanceState.getInt("spnCurrency"));
        spnTypeConversion.setSelection(savedInstanceState.getInt("spnTypeConversion"));
    }

    void loadSavedSettings(){
        edtDateEnd.setText(mSettings.getString(END_DATE_B, "01-02-2017"));
        edtExcRateNow.setText(mSettings.getString(EXC_RATE_NOW_B, "15000"));
        edtSummAvalue.setText(mSettings.getString(SUMM_B_VALUE, "1000000"));
        edtPercentB.setText(mSettings.getString(PERCENT_B, "50"));
        txtProfitBValue.setText(mSettings.getString(PROFIT_B, "0"));
        txtGrowValue.setText(mSettings.getString(GROW_B, "0"));
        txtFullSummValue.setText(mSettings.getString(GROW_B, "0"));
        edtBeginDate.setText(mSettings.getString(BEGIN_DATE_B, "01-02-2016"));
        this.CurrencyA = mSettings.getString(CURRENCY_A, "BLR");
        spnCapital.setSelection(mSettings.getInt(SPN_CAPITAL_B, 0));
        spnCurrency.setSelection(mSettings.getInt(SPN_CURRENCY_B, 1));
        spnTypeConversion.setSelection(mSettings.getInt(SPN_TYPE_CONVERSION, 0));

    }

    void saveSettings(){
        SharedPreferences.Editor ed = mSettings.edit();
        ed.putString(BEGIN_DATE_B, edtBeginDate.getText().toString());
        ed.putString(END_DATE_B, edtDateEnd.getText().toString());
        ed.putString(EXC_RATE_NOW_B, edtExcRateNow.getText().toString());
        ed.putString(SUMM_B_VALUE, edtSummAvalue.getText().toString());
        ed.putString(PERCENT_B, edtPercentB.getText().toString());
        ed.putString(PROFIT_B, txtProfitBValue.getText().toString());
        ed.putString(CURRENCY_A,this.CurrencyA);
        ed.putString(GROW_B, txtGrowValue.getText().toString());
        ed.putString(FULL_SUMM_VALUE_B, txtFullSummValue.getText().toString());
        ed.putInt(SPN_CAPITAL_B, spnCapital.getSelectedItemPosition());
        ed.putInt(SPN_CURRENCY_B, spnCurrency.getSelectedItemPosition());
        ed.putInt(SPN_TYPE_CONVERSION, spnTypeConversion.getSelectedItemPosition());

        ed.commit();

    }
    public boolean allFieldsWithData(){
        return (edtPercentB.getText().length()>0)&(edtSummAvalue.getText().length()>0);

    }
    public void calc_it(){
        if (allFieldsWithData()) {
            float s = f.parseNumber(edtSummAvalue.getText().toString());
            float pr = Float.parseFloat(edtPercentB.getText().toString());
            int d = this.spnTimeperiodNumber;
            if (this.spnTimelineChoice ==1) d*=30;
            if (this.spnTimelineChoice ==2) d*=365;
            int cap = spnCapital.getSelectedItemPosition();
            Float[] profit = Calculator.calcProfit(s, pr, d,cap);

            txtGrowValue.setText(f.format(profit[Calculator.PERCENT]));
            txtProfitBValue.setText(f.format(profit[Calculator.PROFIT]));
            txtFullSummValue.setText(f.format(profit[Calculator.FULLSUMM]));
        }

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
            Log.e("Calc", "Issue with edtExcRateNow field ");
            conv=(float) 1;
        }

        if (spnTypeConversion.getSelectedItemId()==1)  conv = 1/conv;

        return conv * this.summFromFirstTab;
    }

    public void saveData(){
        float conversion = Float.valueOf(edtExcRateNow.getText().toString());
        float profit = f.parseNumber(txtProfitBValue.getText().toString());
        float percent_grow = f.parseNumber(txtGrowValue.getText().toString());
        mCallback.saveSecondTabData(spnCurrency.getSelectedItem().toString(), conversion, profit, percent_grow);
    }

    public void setDataFromFirstTab(String spn_currency, float summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd){
        edtBeginDate.setText(dateBegin);
        edtDateEnd.setText(dateEnd);

        this.spnTimeperiodNumber = timeperiod;
        this.spnPeriod = getResources().getStringArray(R.array.timeperiods_array)[spn_tpr];
        this.spnTimelineChoice = spn_tpr;
        txtTimeperiod.setText(spnTimeperiodNumber + " " + spnPeriod);

        this.CurrencyA = spn_currency;
        spnTypeConversion.setAdapter(getCurrencyPairs());

        this.summFromFirstTab = summ;
        edtSummAvalue.setText(String.valueOf(f.format(calc_summ())));
        calc_it();
    }
}
