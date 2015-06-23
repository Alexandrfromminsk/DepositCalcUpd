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

    private float ExcRateNow, ProfitA, ProfitB, PercentGrowA, PercentGrowB;
    private String CurrencyA, CurrencyB;
    private boolean Inverted_conversion;
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
        final View rootView = inflater.inflate(R.layout.compare_fragment, container,false);

        txtPrecentProfitNow = (TextView) rootView.findViewById(R.id.txtPrecentProfitNow);
        txtCurOneProfitNow = (TextView) rootView.findViewById(R.id.txtCurOneProfitNow);
        txtCurTwoProfitNow = (TextView) rootView.findViewById(R.id.txtCurTwoProfitNow);
        txtExcRateNow = (TextView) rootView.findViewById(R.id.txtExcRateNow);

        txtExcRateCalc = (TextView) rootView.findViewById(R.id.txtExcRateCalc);

        txtPrecentProfitDinamic = (TextView) rootView.findViewById(R.id.txtPrecentProfitDinamic);
        txtCurOneProfitDinamic = (TextView) rootView.findViewById(R.id.txtCurOneProfitDinamic);
        txtCurTwoProfitDinamic = (TextView) rootView.findViewById(R.id.txtCurTwoProfitDinamic);

        edtExcRateDinamic = (EditText) rootView.findViewById(R.id.edtExcRateDinamic);
        edtExcRateDinamic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float dynRate, profitBConverted, diffProfit, diffInCurrB;
                dynRate = f.parseExcRate(edtExcRateDinamic.getText().toString()); //may be returned 1 if wrong format
                if (Inverted_conversion) profitBConverted = ProfitB*dynRate;
                else profitBConverted = ProfitB/dynRate;

                diffProfit = ProfitA - profitBConverted;

                if (Inverted_conversion) diffInCurrB = diffProfit/dynRate;
                else diffInCurrB = diffProfit*dynRate;
                txtCurTwoProfitDinamic.setText(f.format(diffInCurrB) + CurrencyB);
                txtCurOneProfitDinamic.setText(f.format(diffProfit) + CurrencyA);
                setDinamicPercent(dynRate);
            }

            @Override
            public void afterTextChanged(Editable s) {      }
        });

        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(50);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float dynRate;
                float step = ExcRateNow/100;
                dynRate = (progress - 50)*step + ExcRateNow;
                edtExcRateDinamic.setText(f.formatExcRate(dynRate));

                /* At text watcher above
                if (Inverted_conversion) profitBConverted = ProfitB/dynRate;
                else profitBConverted = ProfitB*dynRate;

                diffProfit = ProfitA - profitBConverted;

                txtPrecentProfitDinamic.setText(f.format(777777)+"%");

                if (Inverted_conversion) diffInCurrB = diffProfit/dynRate;
                else diffInCurrB = diffProfit*dynRate;
                txtCurTwoProfitDinamic.setText(f.format(diffInCurrB) + CurrencyB);
                txtCurOneProfitDinamic.setText(f.format(diffProfit) + CurrencyA);
                */
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

    private void setDinamicPercent(float excRate) {
        float percentDiffRate = (excRate/ExcRateNow - 1)*100;
        //float currTwoUpdatedPercent = this.PercentGrowB + this.PercentGrowB*percentDiffRate;
        txtPrecentProfitDinamic.setText(f.format(this.PercentGrowA - this.PercentGrowB - percentDiffRate)+"%");
    }


    public void setDataFromTabs(String currencyA, String currencyB, float profitA, float profitB,
                                float excRateNow, float PercentGrowA, float PercentGrowB,
                                boolean inverted_conversion) {
        float profitBConverted, diffProfit, diffPercent, diffInCurrB;

        this.Inverted_conversion = inverted_conversion;

        this.ExcRateNow = excRateNow;
        this.PercentGrowA = PercentGrowA;
        this.PercentGrowB = PercentGrowB;
        this.ProfitA = profitA;
        this.ProfitB = profitB;
        this.CurrencyA = String.format(" %s", currencyA);
        this.CurrencyB = String.format(" %s", currencyB);

        txtExcRateNow.setText(f.formatExcRate(excRateNow));

        if (Inverted_conversion) profitBConverted = profitB*excRateNow;
        else profitBConverted = profitB/excRateNow;

        diffProfit = profitA - profitBConverted;

        diffPercent = PercentGrowA - PercentGrowB;

        txtPrecentProfitNow.setText(f.format(diffPercent) + " %");
        txtCurOneProfitNow.setText(f.format(diffProfit) + CurrencyA);
        if (Inverted_conversion) diffInCurrB = diffProfit/excRateNow;
        else diffInCurrB = diffProfit*excRateNow;

        txtCurTwoProfitNow.setText(f.format(diffInCurrB) + CurrencyB);


        //maybe another approach should be used
        edtExcRateDinamic.setText(f.formatExcRate(excRateNow));
        txtPrecentProfitDinamic.setText(f.format(diffPercent) + " %");
        txtCurOneProfitDinamic.setText(f.format(diffProfit) + CurrencyA);
        txtCurTwoProfitDinamic.setText(f.format(diffInCurrB) + CurrencyB);

        float excRateCalc = (100+diffPercent)* excRateNow/100;
        txtExcRateCalc.setText(f.formatExcRate(excRateCalc));

    }

}
