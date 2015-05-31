package com.by.alex.depositcalcupd;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class CompareFragment extends Fragment {

    TextView txtPrecentProfitNow,txtCurOneProfitNow, txtCurTwoProfitNow, txtExcRateNow,
            txtPrecentProfitCalc,txtCurOneProfitCalc, txtCurTwoProfitCalc, txtExcRateCalc,
            txtPrecentProfitDinamic,txtCurOneProfitDinamic, txtCurTwoProfitDinamic;
    EditText edtExcRateDinamic;
    Formatter f = new Formatter();

    private float ExcRateNow, ProfitA,ProfitB;
    private String CurrencyA, CurrencyB;
    private SeekBar mSeekBar;

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
        View rootView = inflater.inflate(R.layout.compare_fragment_new, container,false);

        txtPrecentProfitNow = (TextView) rootView.findViewById(R.id.txtPrecentProfitNow);
        txtCurOneProfitNow = (TextView) rootView.findViewById(R.id.txtCurOneProfitNow);
        txtCurTwoProfitNow = (TextView) rootView.findViewById(R.id.txtCurTwoProfitNow);
        txtExcRateNow = (TextView) rootView.findViewById(R.id.txtExcRateNow);

        txtPrecentProfitCalc = (TextView) rootView.findViewById(R.id.txtPrecentProfitCalc);
        txtCurOneProfitCalc = (TextView) rootView.findViewById(R.id.txtCurOneProfitCalc);
        txtCurTwoProfitCalc = (TextView) rootView.findViewById(R.id.txtCurTwoProfitCalc);
        txtExcRateCalc = (TextView) rootView.findViewById(R.id.txtExcRateCalc);

        txtPrecentProfitDinamic = (TextView) rootView.findViewById(R.id.txtPrecentProfitDinamic);
        txtCurOneProfitDinamic = (TextView) rootView.findViewById(R.id.txtCurOneProfitDinamic);
        txtCurTwoProfitDinamic = (TextView) rootView.findViewById(R.id.txtCurTwoProfitDinamic);

        edtExcRateDinamic = (EditText) rootView.findViewById(R.id.edtExcRateDinamic);
        edtExcRateDinamic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {     }

            @Override
            public void afterTextChanged(Editable s) {      }
        });

        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        mSeekBar.setMax(400);
        mSeekBar.setProgress(200);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double dynRate;
                double step = (2*ExcRateNow)/200.0;
                dynRate = (progress - 200)*step + ExcRateNow;
                edtExcRateDinamic.setText(String.valueOf(dynRate));

                float diff = (float) (ProfitA - ProfitB/dynRate);

                txtPrecentProfitDinamic.setText(f.format((diff / ProfitA) * 100)+"%");
                txtCurOneProfitDinamic.setText(f.format(diff) + CurrencyA);
                txtCurTwoProfitDinamic.setText(f.format((float) (diff*dynRate)) + CurrencyB);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {    }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {     }
        });

        return rootView;
    }

    public void saveData(){
        //Send data which will be saved at MainActivity
        //Log.e("befocallbackCompareTab", "befocallbackCompareTab");
        mCallback.saveCompareTabData(777);
        //Log.e("aftercallbackCompareTab", "aftercallbackCompareTab");

    }


    public void setDataFromTabs(String currencyA, String currencyB, float profitA, float profitB,
                                float excRateNow, float PercentGrowA, float PercentGrowB) {
        float profitBConverted, diff;

        ExcRateNow = excRateNow;
        ProfitA = profitA;
        ProfitB = profitB;
        CurrencyA = String.format(" %s", currencyA);
        CurrencyB = String.format(" %s", currencyB);

        txtExcRateNow.setText(f.format(excRateNow));

        profitBConverted = profitB/excRateNow;

        diff = profitA - profitBConverted;

        txtPrecentProfitNow.setText(f.format(PercentGrowA - PercentGrowB) + " %");
        txtCurOneProfitNow.setText(f.format(diff) + CurrencyA);
        txtCurTwoProfitNow.setText(f.format(diff*excRateNow) + CurrencyB);

        //maybe another approach should be used
        edtExcRateDinamic.setText(f.format(excRateNow));
        txtPrecentProfitDinamic.setText(f.format((diff / profitA) * 100) + " %");
        txtCurOneProfitDinamic.setText(f.format(diff) + CurrencyA);
        txtCurTwoProfitDinamic.setText(f.format(diff* excRateNow) + CurrencyB);

        float excRateCalc = profitB/profitA;
        txtExcRateCalc.setText(f.format(excRateCalc));

        profitBConverted = profitB/excRateCalc;
        diff = profitA - profitBConverted;

        //txtCurOneProfitCalc.setText(String.valueOf(diff) + CurrencyA);
        //txtCurTwoProfitCalc.setText(String.valueOf(diff*excRateCalc) + CurrencyB);

        //TO DO
        //set seekbar with min and max values like here
        //http://stackoverflow.com/questions/20762001/how-to-set-seekbar-min-and-max-value

    }

}
