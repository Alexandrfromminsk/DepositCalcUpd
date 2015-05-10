package com.by.alex.depositcalcupd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CurrencyOneFragment extends Fragment implements OnClickListener, TextWatcher {

    EditText edtSummAvalue, edtPercentA, edtBeginDate, edtTimeperiod;
    TextView edtDateEnd, txtProfitAValue, txtGrowValue, txtFullSummValue;
    Spinner spnTimeperiod, spnCapital, spnCurrency;

    SharedPreferences mSettings;

    public static final String BEGIN_DATE = "BEGIN_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String SUMM_A_VALUE = "SUMM_A_VALUE";
    public static final String PERCENT_A = "PERCENT_A";
    public static final String TIMEPERIOD = "TIMEPERIOD";
    public static final String PROFIT = "PROFIT";
    public static final String GROW = "GROW";
    public static final String FULL_SUMM_VALUE = "FULL_SUMM_VALUE";
    public static final String SPN_TIMELINE = "SPN_TIMELINE";
    public static final String SPN_CAPITAL = "SPN_CAPITAL";
    public static final String SPN_CURRENCY_A = "SPN_CURRENCY_A";

    // Container Activity must implement this interface
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
        View rootView = inflater.inflate(R.layout.cur_one_fragment, container, false);

        mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        edtSummAvalue = (EditText) rootView.findViewById(R.id.edtSummAvalue);
        edtSummAvalue.addTextChangedListener(this);
        edtPercentA = (EditText) rootView.findViewById(R.id.edtPercent);
        edtPercentA.addTextChangedListener(this);
        edtBeginDate = (EditText) rootView.findViewById(R.id.edtBeginDate);
        edtBeginDate.addTextChangedListener(this);
        edtTimeperiod = (EditText) rootView.findViewById(R.id.edtTimeperiod);
        edtTimeperiod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                setEndDate();
                calc_it();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edtDateEnd = (TextView) rootView.findViewById(R.id.edtEndDate);
        txtProfitAValue = (TextView) rootView.findViewById(R.id.txtProfit);
        txtGrowValue = (TextView)rootView.findViewById(R.id.txtGrow);
        txtFullSummValue =(TextView)rootView.findViewById(R.id.txtFullSummValue);


        //Spinners
        spnCurrency = (Spinner) rootView.findViewById(R.id.spnCurrencyA);
        spnTimeperiod = (Spinner) rootView.findViewById(R.id.spnTimeperiod);
        spnCapital = (Spinner) rootView.findViewById(R.id.spnCapital);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.timeperiods_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spnTimeperiod.setAdapter(adapter);
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
        spnCapital.setOnItemSelectedListener(new OnItemSelectedListener() {
                                                 @Override
                                                 public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                     calc_it();
//                                                     Log.e("getItemSelected", spnCapital.getSelectedItem()+"");
//                                                     Log.e("getSelectedItemId", spnCapital.getSelectedItemId()+"");
//                                                     Log.e("getSelectedItemPosition", spnCapital.getSelectedItemPosition()+"");
                                                 }

                                                 @Override
                                                 public void onNothingSelected(AdapterView<?> adapterView) {

                                                 }
                                             });

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spnCurrency.setAdapter(adapter);

        edtBeginDate = (EditText) rootView.findViewById(R.id.edtBeginDate);
        edtBeginDate.setOnClickListener(this);


        if(savedInstanceState == null){
            loadSavedSettings();

        }else {
            loadSavedInstanceState(savedInstanceState);
            //edtTimeperiod.setText(savedInstanceState.getString("edtTimeperiod"));
        }

        setEndDate();

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
            // Debug Toast.makeText(getActivity(), date.toString(), Toast.LENGTH_LONG).show();
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

    public void calc_it(){
        if (allFieldsWithData()) {
            Float s = Float.parseFloat(edtSummAvalue.getText().toString());
            Float pr = Float.parseFloat(edtPercentA.getText().toString());
            Integer d = Integer.parseInt(edtTimeperiod.getText().toString());
            int cap = spnCapital.getSelectedItemPosition();
            String BeginDate, EndDate;
            BeginDate = edtBeginDate.getText().toString();
            EndDate = edtDateEnd.getText().toString();

            Float[] profit;
            int dmy = spnTimeperiod.getSelectedItemPosition();
            if (dmy==0) profit = Calculator.calcProfit(s, pr, d,cap);
            else  profit = Calculator.calcProfit(s, pr, cap, BeginDate, EndDate);

            txtGrowValue.setText(formatTwoDecimals(profit[Calculator.PERCENT]).toString());
            txtProfitAValue.setText(formatTwoDecimals(profit[Calculator.PROFIT]).toString());
            txtFullSummValue.setText(formatTwoDecimals(profit[Calculator.FULLSUMM]).toString());
        }

    }

    private Float formatTwoDecimals (float f) {
        DecimalFormat twoFForm = new DecimalFormat("#.##");
        return Float.parseFloat(twoFForm.format(f));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtTimeperiod", edtTimeperiod.getText().toString());
        outState.putString("edtBeginDate", edtBeginDate.getText().toString());
        outState.putString("edtDateEnd", edtDateEnd.getText().toString());
        outState.putString("edtSummAvalue", edtSummAvalue.getText().toString());
        outState.putString("edtPercentA", edtPercentA.getText().toString());
        outState.putString("txtProfitAValue", txtProfitAValue.getText().toString());
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
        edtPercentA.setText(savedInstanceState.getString("edtPercentA"));
        txtProfitAValue.setText(savedInstanceState.getString("txtProfitAValue"));
        txtGrowValue.setText(savedInstanceState.getString("txtGrowValue"));
        txtFullSummValue.setText(savedInstanceState.getString("txtFullSummValue"));
        //Spinners
        spnCapital.setSelection(savedInstanceState.getInt("spnCapital"));
        spnCurrency.setSelection(savedInstanceState.getInt("spnCurrency"));
        spnTimeperiod.setSelection(savedInstanceState.getInt("spnTimeperiod"));
    }

    void loadSavedSettings(){
        edtDateEnd.setText(mSettings.getString(END_DATE, "01-02-2016"));
        edtSummAvalue.setText(mSettings.getString(SUMM_A_VALUE, "1000000"));
        edtPercentA.setText(mSettings.getString(PERCENT_A, "50"));
        edtTimeperiod.setText(mSettings.getString(TIMEPERIOD, "365"));
        txtProfitAValue.setText(mSettings.getString(PROFIT, "0"));
        txtGrowValue.setText(mSettings.getString(GROW, "0"));
        txtFullSummValue.setText(mSettings.getString(FULL_SUMM_VALUE, "0"));
        edtBeginDate.setText(mSettings.getString(BEGIN_DATE, "01-02-2015"));
        spnCapital.setSelection(mSettings.getInt(SPN_CAPITAL,0));
        spnTimeperiod.setSelection(mSettings.getInt(SPN_TIMELINE,0));

    }

    void saveSettings(){
        SharedPreferences.Editor ed = mSettings.edit();
        ed.putString(TIMEPERIOD, edtTimeperiod.getText().toString());
        ed.putString(BEGIN_DATE, edtBeginDate.getText().toString());
        ed.putString(END_DATE, edtDateEnd.getText().toString());
        ed.putString(SUMM_A_VALUE, edtSummAvalue.getText().toString());
        ed.putString(PERCENT_A, edtPercentA.getText().toString());
        ed.putString(PROFIT, txtProfitAValue.getText().toString());
        ed.putString(GROW, txtGrowValue.getText().toString());
        ed.putString(FULL_SUMM_VALUE, txtFullSummValue.getText().toString());
        ed.putInt(SPN_TIMELINE, (int) spnTimeperiod.getSelectedItemPosition());
        ed.putInt(SPN_CAPITAL, spnCapital.getSelectedItemPosition());

        ed.commit();

    }


    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(getActivity(), "onstop First tab".toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveSettings();
        //Toast.makeText(getActivity(), "ondestroy  First tab".toString(), Toast.LENGTH_SHORT).show();

    }

    // 3 TextWatcher's methods
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        calc_it();
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public void saveData(){
        float  sum = Float.parseFloat(edtSummAvalue.getText().toString());
        int tpr = Integer.valueOf(edtTimeperiod.getText().toString());
        float profit = Float.valueOf(txtProfitAValue.getText().toString());
        //Log.e("befocallbackFirstTab", sum + "");
        mCallback.saveFirstTabData(spnCurrency.getSelectedItem().toString(), sum, tpr, spnTimeperiod.getSelectedItemPosition(), edtBeginDate.getText().toString(), edtDateEnd.getText().toString(), profit);
        //Log.e("aftercallbackFirstTab", sum + "");
    }
}
