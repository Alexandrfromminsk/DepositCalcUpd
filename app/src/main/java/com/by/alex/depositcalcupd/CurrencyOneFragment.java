package com.by.alex.depositcalcupd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CurrencyOneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cur_one_fragment, container,false);

        Button btnCalc, btnAdd, btnCancel;
        EditText edtCurrencyA, edtExcRateNow, edtSummAvalue, edtPercentA, edtBeginDate, edtTimeperiod;
        TextView edtDateEnd, txtProfitAValue, txtGrowValue, txtFullSummValue;
        CheckBox chbAddPercentOn;
        Spinner spnTimeperiod, spnCapital;
        SharedPreferences DefPref;

        //String[] timePeriods = {"days", "months", "years"};
        //String[] capitals = {"at the end", "daily", "montly", "once per kvartal", "every year",};

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



        return rootView;
    }
}
