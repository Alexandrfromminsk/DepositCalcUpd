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

    EditText edtExcRateNow, edtPercentB;
    TextView edtDateEnd, txtProfitBValue, txtGrowValue, txtFullSummValue, edtSummBvalue,
            edtBeginDate, txtTimeperiod, txtSummWithCurrency, txtFullSummWithCurrency, txtProfitWithCurrency;

    Spinner spnCapital, spnCurrency, spnTypeConversion;
    String textSumm, textFullSumm, textProfit;

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

    private float summFromFirstTab = (float)1;
    private int timeperiodNumber, spnTimelineChoice;
    private String  spnPeriod, CurrencyA;
    Formatter f = new Formatter();

    OnTabChangedListener mCallback;

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
        textSumm = getResources().getString(R.string.txtSumm);
        textFullSumm = getResources().getString(R.string.txtFullSumm);
        textProfit = getResources().getString(R.string.txtProfit);

        mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        edtExcRateNow = (EditText) rootView.findViewById(R.id.edtExchangeRateB);
        edtSummBvalue = (TextView) rootView.findViewById(R.id.edtSummBvalue);
        edtPercentB = (EditText) rootView.findViewById(R.id.edtPercentB);
        txtTimeperiod = (TextView) rootView.findViewById(R.id.txtTimeperiod);
        txtSummWithCurrency = (TextView) rootView.findViewById(R.id.txtSummWithCurrencyB);
        txtFullSummWithCurrency = (TextView) rootView.findViewById(R.id.txtFullSummWithCurrencyB);
        txtProfitWithCurrency = (TextView) rootView.findViewById(R.id.txtProfitWithCurrencyB);

        edtExcRateNow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                edtSummBvalue.setText(f.format(calc_summ()));
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
            public void onNothingSelected(AdapterView<?> adapterView) {      }
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
                String currency = spnCurrency.getSelectedItem().toString();
                setTxtCurrency(currency);
                if (CurrencyA.equals(currency))
                    edtExcRateNow.setText("1");
                calc_it();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setTxtCurrency(spnCurrency.getSelectedItem().toString());

        spnTypeConversion.setAdapter(getCurrencyPairs());

        spnTypeConversion.post(new Runnable() {
            public void run() {
                spnTypeConversion.setSelection(mSettings.getInt(SPN_TYPE_CONVERSION, 0));
            }
        });

        spnTypeConversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtSummBvalue.setText(String.valueOf(f.format(calc_summ())));
                calc_it();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(savedInstanceState == null){
            loadSavedSettings();

        }else {
            loadSavedInstanceState(savedInstanceState);
        }

        if (txtTimeperiod.getText().toString().length()==0)
            txtTimeperiod.setText(this.timeperiodNumber + " " + this.spnPeriod);

        // Pure AdMob
        /*
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        return rootView;
    }


    private ArrayAdapter<String> getCurrencyPairs() {
        ArrayList<String> conversions = new ArrayList<>();
        conversions.add(String.format("%s / %s", this.CurrencyA, spnCurrency.getSelectedItem().toString()));
        conversions.add(String.format("%s / %s", spnCurrency.getSelectedItem().toString(), this.CurrencyA));

        ArrayAdapter<String> convAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, conversions);
        convAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  convAdapter;
    }


    @Override
      public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtPercentB", edtPercentB.getText().toString());
        outState.putString("edtBeginDate", edtBeginDate.getText().toString());
        outState.putString("edtDateEndB", edtDateEnd.getText().toString());
        outState.putString("edtSummBvalue", edtSummBvalue.getText().toString());
        outState.putString("txtProfitBValue", txtProfitBValue.getText().toString());
        outState.putString("txtGrowValue", txtGrowValue.getText().toString());
        outState.putString("txtFullSummValue", txtFullSummValue.getText().toString());
        outState.putString("CurrencyA", this.CurrencyA);
        //!!!AddtxtTimeperiod TO DO
        //Spinners
        outState.putInt("spnCapital", spnCapital.getSelectedItemPosition());
        outState.putInt("spnCurrency", spnCurrency.getSelectedItemPosition());
        outState.putInt("spnTypeConversion", spnTypeConversion.getSelectedItemPosition());
    }

    void loadSavedInstanceState(Bundle savedInstanceState) {
        edtDateEnd.setText(savedInstanceState.getString("edtDateEnd"));
        edtSummBvalue.setText(savedInstanceState.getString("edtSummBvalue"));
        edtPercentB.setText(savedInstanceState.getString("edtPercentB"));
        txtProfitBValue.setText(savedInstanceState.getString("txtProfitBValue"));
        txtGrowValue.setText(savedInstanceState.getString("txtGrowValue"));
        txtFullSummValue.setText(savedInstanceState.getString("txtFullSummValue"));
        this.CurrencyA = savedInstanceState.getString("CurrencyA");
        //!!!AddtxtTimeperiod To DO
        //Spinners
        spnCapital.setSelection(savedInstanceState.getInt("spnCapital"));
        spnCurrency.setSelection(savedInstanceState.getInt("spnCurrency"));
        spnTypeConversion.setSelection(savedInstanceState.getInt("spnTypeConversion"));
    }

    void loadSavedSettings() {
        edtDateEnd.setText(mSettings.getString(END_DATE_B, "1-1-2017"));
        edtExcRateNow.setText(mSettings.getString(EXC_RATE_NOW_B, "1.13"));
        edtSummBvalue.setText(mSettings.getString(SUMM_B_VALUE, "11300"));
        edtPercentB.setText(mSettings.getString(PERCENT_B, "3.5"));
        txtProfitBValue.setText(mSettings.getString(PROFIT_B, "0"));
        txtGrowValue.setText(mSettings.getString(GROW_B, "0"));
        txtFullSummValue.setText(mSettings.getString(GROW_B, "0"));
        edtBeginDate.setText(mSettings.getString(BEGIN_DATE_B, "1-1-2016"));
        this.CurrencyA = mSettings.getString(CURRENCY_A, "EUR");
        spnCapital.setSelection(mSettings.getInt(SPN_CAPITAL_B, 2));
        spnCurrency.setSelection(mSettings.getInt(SPN_CURRENCY_B, 3));
        spnTypeConversion.setSelection(mSettings.getInt(SPN_TYPE_CONVERSION, 0));

    }

    void saveSettings() {
        SharedPreferences.Editor ed = mSettings.edit();
        ed.putString(BEGIN_DATE_B, edtBeginDate.getText().toString());
        ed.putString(END_DATE_B, edtDateEnd.getText().toString());
        ed.putString(EXC_RATE_NOW_B, edtExcRateNow.getText().toString());
        ed.putString(SUMM_B_VALUE, edtSummBvalue.getText().toString());
        ed.putString(PERCENT_B, edtPercentB.getText().toString());
        ed.putString(PROFIT_B, txtProfitBValue.getText().toString());
        ed.putString(CURRENCY_A, this.CurrencyA);
        ed.putString(GROW_B, txtGrowValue.getText().toString());
        ed.putString(FULL_SUMM_VALUE_B, txtFullSummValue.getText().toString());
        ed.putInt(SPN_CAPITAL_B, spnCapital.getSelectedItemPosition());
        ed.putInt(SPN_CURRENCY_B, spnCurrency.getSelectedItemPosition());
        ed.putInt(SPN_TYPE_CONVERSION, spnTypeConversion.getSelectedItemPosition());
        ed.apply();

    }
    public boolean allFieldsWithData(){
        return (edtPercentB.getText().length()>0)&(edtSummBvalue.getText().length()>0);

    }
    public void calc_it(){
        if (allFieldsWithData()) {
            float s = f.parseNumber(edtSummBvalue.getText().toString());
            float pr = Float.parseFloat(edtPercentB.getText().toString());
            int cap = spnCapital.getSelectedItemPosition();
            Float[] profit;
            int days = Calculator.calcNumberBankDays(timeperiodNumber, spnTimelineChoice);

            boolean russian_tax = mSettings.getBoolean("russian_tax", false);
            boolean ukr_tax = mSettings.getBoolean("ukr_tax", false);

            if (russian_tax) {
                int tax_percent;
                float key_percent;
                String tax_percent_string = mSettings.getString("tax_percent", "35");
                String key_percent_string = (spnCurrency.getSelectedItem().toString().equals("RUR"))?
                        mSettings.getString("key_percent", "18.25"): mSettings.getString("key_percent_foreign", "9");


                try {
                    key_percent = Float.valueOf(key_percent_string);
                    tax_percent= Integer.valueOf(tax_percent_string);
                } catch (NumberFormatException nfe)
                {

                    tax_percent = (int) getResources().getDimension(R.dimen.default_rus_tax_percent_int);
                    key_percent = getResources().getDimension(R.dimen.default_key_percent_float);
                }

                profit = Calculator.calcProfit(s, pr, days, cap, tax_percent, key_percent);

            }
            else if (ukr_tax) {
                int ukr_tax_percent;
                String ukr_tax_percent_string = mSettings.getString("ukr_tax_percent", "20");

                try {
                    ukr_tax_percent= Integer.valueOf(ukr_tax_percent_string);
                } catch (NumberFormatException nfe)
                {
                    ukr_tax_percent = (int)getResources().getDimension(R.dimen.default_ukr_tax_percent_int);
                }

                profit = Calculator.calcProfit(s, pr, days, cap, ukr_tax_percent);

            }
            else {
                profit = Calculator.calcProfit(s, pr, days, cap);

            }

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

    @Override
    public void onResume() {
        super.onResume();
        calc_it();
    }


    private void setTxtCurrency(String currency){
        txtSummWithCurrency.setText(this.textSumm + ", " + currency);
        txtFullSummWithCurrency.setText(this.textFullSumm + ", " + currency);
        txtProfitWithCurrency.setText(this.textProfit + ", " + currency);
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
   //     float summBbegin = f.parseSumm(txtSummWithCurrency.)
        //inverted_conversion = true mean that conversion is inverted and must be used / instaed of *
        boolean inverted_conversion = (spnTypeConversion.getSelectedItemPosition()>0);
        mCallback.saveSecondTabData(spnCurrency.getSelectedItem().toString(), conversion, profit,
                percent_grow, inverted_conversion);
    }

    public void setDataFromFirstTab(String spn_currency, float summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd){
        edtBeginDate.setText(dateBegin);
        edtDateEnd.setText(dateEnd);

        this.timeperiodNumber = timeperiod;
        this.spnPeriod = getResources().getStringArray(R.array.timeperiods_array)[spn_tpr];
        this.spnTimelineChoice = spn_tpr;
        txtTimeperiod.setText(timeperiodNumber + " " + spnPeriod);

        if (!this.CurrencyA.equals(spn_currency)) {
            this.CurrencyA = spn_currency;
            spnTypeConversion.setAdapter(getCurrencyPairs());
        }

        this.summFromFirstTab = summ;
        edtSummBvalue.setText(String.valueOf(f.format(calc_summ())));
        calc_it();
    }
}
