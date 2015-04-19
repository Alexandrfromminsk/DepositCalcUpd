package com.by.alex.depositcalcupd;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cur_one_fragment, container,false);


        //btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCalc = (Button) rootView.findViewById(R.id.btnCalc);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);


        edtExcRateNow = (EditText) rootView.findViewById(R.id.edtExchangeRate);
        edtSummAvalue = (EditText) rootView.findViewById(R.id.edtSummAvalue);
        edtPercentA = (EditText) rootView.findViewById(R.id.edtPercent);
        edtBeginDate = (EditText) rootView.findViewById(R.id.edtBeginDate);
        edtTimeperiod = (EditText) rootView.findViewById(R.id.edtTimeperiod);

        chbAddPercentOn = (CheckBox) rootView.findViewById(R.id.chbAddPercentOn);

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

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.capitals_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spnCapital.setAdapter(adapter);

        edtBeginDate = (EditText) rootView.findViewById(R.id.edtBeginDate);
        edtBeginDate.setOnClickListener(this);

        edtTimeperiod.setText(String.valueOf(365));







        return rootView;
    }

    public boolean ifFieldsWithData(){
        return (edtExcRateNow.getText().length()>0)&(edtSummAvalue.getText().length()>0)&(edtTimeperiod.getText().length()>0);
    }

    public  void setEndDate(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        try {
            Date date = sdf.parse(edtBeginDate.getText().toString());
            cal.setTime(date);



            int tpr = Integer.parseInt(edtTimeperiod.getText().toString());

            //Spinner spnTimeperiod = (Spinner) findViewById(R.id.spnTimeperiod);
            int dmy = spnTimeperiod.getSelectedItemPosition();

            if (dmy==0) cal.add(Calendar.DAY_OF_MONTH, tpr);
            else if (dmy==1) cal.add(Calendar.MONTH, tpr);
            else cal.add(Calendar.YEAR,tpr);


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
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

}
