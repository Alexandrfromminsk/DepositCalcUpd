package com.by.alex.depositcalcupd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CurrencyOneFragment extends Fragment implements OnClickListener, OnFocusChangeListener {

    Button btnCalc, btnAdd, btnCancel;
    EditText edtCurrencyA, edtExcRateNow, edtSummAvalue, edtPercentA, edtBeginDate, edtTimeperiod;
    TextView edtDateEnd, txtProfitAValue, txtGrowValue, txtFullSummValue;
    CheckBox chbAddPercentOn;
    Spinner spnTimeperiod, spnCapital;


    SharedPreferences mSettings;

    public static final String BEGIN_DATE = "BEGIN_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String EXC_RATE_NOW = "EXC_RATE_NOW";
    public static final String SUMM_A_VALUE = "SUMM_A_VALUE";
    public static final String PERCENT_A = "PERCENT_A";
    public static final String TIMEPERIOD = "TIMEPERIOD";
    public static final String ADD_PERCENT = "ADD_PERCENT";
    public static final String PROFIT = "PROFIT";
    public static final String GROW = "GROW";
    public static final String FULL_SUMM_VALUE = "FULL_SUMM_VALUE";
    public static final String SPN_TIMELINE = "SPN_TIMELINE";
    public static final String SPN_CAPITAL = "SPN_CAPITAL";
    public static final String SPN_CURRENCY_A = "SPN_CURRENCY_A";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cur_one_fragment, container,false);

        mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);


        //btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCalc = (Button) rootView.findViewById(R.id.btnCalc);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);


        edtExcRateNow = (EditText) rootView.findViewById(R.id.edtExchangeRate);
        edtSummAvalue = (EditText) rootView.findViewById(R.id.edtSummAvalue);
        edtSummAvalue.setOnFocusChangeListener(this);
        edtPercentA = (EditText) rootView.findViewById(R.id.edtPercent);
        edtPercentA.setOnFocusChangeListener(this);
        edtBeginDate = (EditText) rootView.findViewById(R.id.edtBeginDate);
        edtBeginDate.setOnFocusChangeListener(this);
        edtTimeperiod = (EditText) rootView.findViewById(R.id.edtTimeperiod);
        edtTimeperiod.setOnFocusChangeListener(this);

        chbAddPercentOn = (CheckBox) rootView.findViewById(R.id.chbAddPercentOn);
        chbAddPercentOn.setOnFocusChangeListener(this);

        edtDateEnd = (TextView) rootView.findViewById(R.id.edtEndDate);
        txtProfitAValue = (TextView) rootView.findViewById(R.id.txtProfit);
        txtGrowValue = (TextView)rootView.findViewById(R.id.txtGrow);
        txtFullSummValue =(TextView)rootView.findViewById(R.id.txtFullSummValue);


        //Spinners
        spnTimeperiod = (Spinner) rootView.findViewById(R.id.spnTimeperiod);
        spnCapital = (Spinner) rootView.findViewById(R.id.spnCapital);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.timeperiods_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spnTimeperiod.setAdapter(adapter);
        spnTimeperiod.setOnFocusChangeListener(this);
        spnTimeperiod.setOnItemSelectedListener(new OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        setEndDate();
                                                        calc_it();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }
                                                });

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.capitals_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spnCapital.setAdapter(adapter);
        spnCapital.setOnFocusChangeListener(this);
        spnCapital.setOnItemSelectedListener(new OnItemSelectedListener() {
                                                 @Override
                                                 public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                     calc_it();
                                                 }

                                                 @Override
                                                 public void onNothingSelected(AdapterView<?> adapterView) {

                                                 }
                                             });


        edtBeginDate = (EditText) rootView.findViewById(R.id.edtBeginDate);
        edtBeginDate.setOnClickListener(this);



        if(savedInstanceState == null){
            loadSavedSettings();

        }else {
            edtTimeperiod.setText(savedInstanceState.getString("edtTimeperiod"));
        }

        calc_it();

        return rootView;
    }

    public boolean allFieldsWithData(){
        return (edtPercentA.getText().length()>0)&(edtSummAvalue.getText().length()>0)&(edtTimeperiod.getText().length()>0);

    }

    public  void setEndDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        try {
            Date date = sdf.parse(edtBeginDate.getText().toString());
            cal.setTime(date);
            int tpr;
            try {
                tpr = Integer.parseInt(edtTimeperiod.getText().toString());
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                tpr = 0;
            }
            switch (spnTimeperiod.getSelectedItemPosition()){
                case 0:
                    cal.add(Calendar.DAY_OF_MONTH, tpr);
                    break;
                case 1:
                    cal.add(Calendar.MONTH, tpr);
                    break;
                case 2:
                    cal.add(Calendar.YEAR,tpr);
                    break;
            }

            edtDateEnd.setText(sdf.format(cal.getTime()).toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.edtBeginDate:
                DialogFragment dateDial = new DatePicker();
                dateDial.show(getActivity().getSupportFragmentManager(), "datePicker");
                setEndDate();
                break;
            default:
                calc_it();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()) {
            case R.id.edtTimeperiod:
                // Timeperiod field
                setEndDate();
                break;
            case R.id.spnTimeperiod:
                // Timeperiod spinner
                setEndDate();
                break;
            default:
                calc_it();
                break;
        }

    }

    public void calc_it(){
        if (allFieldsWithData()) {
            Float s = Float.parseFloat(edtSummAvalue.getText().toString());
            Float pr = Float.parseFloat(edtPercentA.getText().toString());
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtTimeperiod", edtTimeperiod.getText().toString());
        outState.putString("edtBeginDate", edtBeginDate.getText().toString());
        outState.putString("edtDateEnd", edtDateEnd.getText().toString());
    }

    void loadSavedSettings(){
        edtDateEnd.setText(mSettings.getString(END_DATE, "01.02.2016"));
        edtExcRateNow.setText(mSettings.getString(EXC_RATE_NOW, "15000"));
        edtSummAvalue.setText(mSettings.getString(SUMM_A_VALUE, "1000000"));
        edtPercentA.setText(mSettings.getString(PERCENT_A, "50"));
        edtTimeperiod.setText(mSettings.getString(TIMEPERIOD, "365"));
        if (mSettings.contains(ADD_PERCENT))
            chbAddPercentOn.setChecked(mSettings.getBoolean(ADD_PERCENT, false));
        txtProfitAValue.setText(mSettings.getString(PROFIT, "0"));
        txtGrowValue.setText(mSettings.getString(GROW, "0"));
        txtFullSummValue.setText(mSettings.getString(FULL_SUMM_VALUE, "0"));
        edtBeginDate.setText(mSettings.getString(BEGIN_DATE, "01.02.2017"));
        spnCapital.setSelection(mSettings.getInt(SPN_CAPITAL,0));
        spnTimeperiod.setSelection(mSettings.getInt(SPN_TIMELINE,0));

    }

    void saveSettings(){
        SharedPreferences.Editor ed = mSettings.edit();
        ed.putString(TIMEPERIOD, edtTimeperiod.getText().toString());
        ed.putString(BEGIN_DATE, edtBeginDate.getText().toString());
        ed.putString(END_DATE, edtDateEnd.getText().toString());
        ed.putString(EXC_RATE_NOW, edtExcRateNow.getText().toString());
        ed.putString(SUMM_A_VALUE, edtSummAvalue.getText().toString());
        ed.putString(PERCENT_A, edtPercentA.getText().toString());
        ed.putBoolean(ADD_PERCENT, chbAddPercentOn.isChecked());
        ed.putString(PROFIT, txtProfitAValue.getText().toString());
        ed.putString(GROW, txtGrowValue.getText().toString());
        ed.putString(FULL_SUMM_VALUE, txtFullSummValue.getText().toString());
        ed.putInt(SPN_TIMELINE, (int) spnTimeperiod.getSelectedItemPosition());
        ed.putInt(SPN_CAPITAL, spnCapital.getSelectedItemPosition());

        ed.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveSettings();
    }
}
