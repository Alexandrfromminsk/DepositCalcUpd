package com.by.alex.depositcalcupd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class CompareFragment extends Fragment {

    TextView txtPrecentProfitNow,txtCurOneProfitNow, txtCurTwoProfitNow, txtExcRateNow,
            txtInCurTwo1, txtInCurTwo2, txtInCurOne1,txtInCurOne2, txtInCurTwo3, txtInCurOne3,
            txtInCurTwo4, txtInCurOne4,txtExcRateCalc, vievForFocus,
            txtCurOneFullNov, txtCurTwoFullNov,txtCurOneFullDinamic, txtCurTwoFullDinamic,
            txtPercentProfitDinamic,txtCurOneProfitDinamic, txtCurTwoProfitDinamic;
    EditText edtExcRateDinamic;
    Formatter f = new Formatter();

    private float ExcRateNow, ProfitA, ProfitB, PercentGrowA, PercentGrowB, SummABegin;
    private String  textInCur;
    private boolean Inverted_conversion;
    private SeekBar mSeekBar;
    SharedPreferences mSettings;

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

        mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        textInCur = getResources().getString(R.string.cmpr_txtInCur);
        txtInCurOne1 = (TextView) rootView.findViewById(R.id.txtInCurOne1);
        txtInCurOne2 = (TextView) rootView.findViewById(R.id.txtInCurOne2);
        txtInCurOne3 = (TextView) rootView.findViewById(R.id.txtInCurOne3);
        txtInCurOne4 = (TextView) rootView.findViewById(R.id.txtInCurOne4);
        txtInCurTwo1 = (TextView) rootView.findViewById(R.id.txtInCurTwo1);
        txtInCurTwo2 = (TextView) rootView.findViewById(R.id.txtInCurTwo2);
        txtInCurTwo3 = (TextView) rootView.findViewById(R.id.txtInCurTwo3);
        txtInCurTwo4 = (TextView) rootView.findViewById(R.id.txtInCurTwo4);

        txtCurOneFullNov= (TextView) rootView.findViewById(R.id.txtCurOneFullNov);
        txtCurTwoFullNov= (TextView) rootView.findViewById(R.id.txtCurTwoFullNov);
        txtCurOneFullDinamic= (TextView) rootView.findViewById(R.id.txtCurOneFullDinamic);
        txtCurTwoFullDinamic= (TextView) rootView.findViewById(R.id.txtCurTwoFullDinamic);

        vievForFocus = (TextView) rootView.findViewById(R.id.txtNoDiffInProfit);
        txtPrecentProfitNow = (TextView) rootView.findViewById(R.id.txtPrecentProfitNow);
        txtCurOneProfitNow = (TextView) rootView.findViewById(R.id.txtCurOneProfitNow);
        txtCurTwoProfitNow = (TextView) rootView.findViewById(R.id.txtCurTwoProfitNow);
        txtExcRateNow = (TextView) rootView.findViewById(R.id.txtExcRateNow);

        txtExcRateCalc = (TextView) rootView.findViewById(R.id.txtExcRateCalc);

        txtPercentProfitDinamic = (TextView) rootView.findViewById(R.id.txtPrecentProfitDinamic);
        txtCurOneProfitDinamic = (TextView) rootView.findViewById(R.id.txtCurOneProfitDinamic);
        txtCurTwoProfitDinamic = (TextView) rootView.findViewById(R.id.txtCurTwoProfitDinamic);

        edtExcRateDinamic = (EditText) rootView.findViewById(R.id.edtExcRateDinamic);

        edtExcRateDinamic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtExcRateDinamic.setFocusable(true);
                edtExcRateDinamic.setFocusableInTouchMode(true);
                return false;
            }
        });
        edtExcRateDinamic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float dynRate, profitBConverted, diffProfit, diffInCurrB;
                dynRate = f.parseExcRate(edtExcRateDinamic.getText().toString()); //may be returned 1 if wrong format
                if (Inverted_conversion) profitBConverted = ProfitB * dynRate;
                else profitBConverted = ProfitB / dynRate;

                diffProfit = ProfitA - profitBConverted;

                if (Inverted_conversion) diffInCurrB = diffProfit / dynRate;
                else diffInCurrB = diffProfit * dynRate;
                txtCurTwoProfitDinamic.setText(f.format(diffInCurrB));
                txtCurOneProfitDinamic.setText(f.format(diffProfit));
                setDinamicPercentAndFullSumm(dynRate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtExcRateDinamic.clearFocus();
        vievForFocus.requestFocus();

        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(50);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float dynRate;
                float step = ExcRateNow / 100;
                dynRate = (progress - 50) * step + ExcRateNow;
                edtExcRateDinamic.setText(f.formatExcRate(dynRate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    /* Pure adMob
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
       */

        return rootView;
    }


    public void saveData(){
        //Send data which will be saved at MainActivity
        //Log.e("befocallbackCompareTab", "befocallbackCompareTab");
        mCallback.saveCompareTabData(777);

    }

    private void setDinamicPercentAndFullSumm(float excRate) {
        float percentDiffRate = (excRate/ExcRateNow - 1)*100;
        float percent, diffFullSumm,diffFullSummB;
        if (Inverted_conversion) {
            percent = this.PercentGrowA - this.PercentGrowB - percentDiffRate;
            diffFullSumm = (percent*SummABegin)/100;
            diffFullSummB = diffFullSumm/excRate;
        }
        else {
            percent = this.PercentGrowA - this.PercentGrowB + percentDiffRate;
            diffFullSumm = (percent*SummABegin)/100;
            diffFullSummB = diffFullSumm*excRate;
        }
        txtCurOneFullDinamic.setText(f.format(diffFullSumm));
        txtCurTwoFullDinamic.setText(f.format(diffFullSummB));
        txtPercentProfitDinamic.setText(f.format(percent) + "%");
    }


    public void setDataFromTabs(String currencyA, String currencyB, float profitA, float profitB,
                                float excRateNow, float PercentGrowA, float PercentGrowB,
                                boolean inverted_conversion, float summAbegin) {
        float profitBConverted, diffProfit, diffPercent, diffInCurrB, diffFullSumm, diffFullSummInCurrB;

        this.Inverted_conversion = inverted_conversion;

        this.ExcRateNow = excRateNow;
        this.PercentGrowA = PercentGrowA;
        this.PercentGrowB = PercentGrowB;
        this.ProfitA = profitA;
        this.ProfitB = profitB;
        this.SummABegin = summAbegin;

        String textInCurOne = String.format("%s 1, %s", textInCur, currencyA);
        String textInCurTwo = String.format("%s 2, %s", textInCur, currencyB);

        txtInCurOne1.setText(textInCurOne);
        txtInCurOne2.setText(textInCurOne);
        txtInCurOne3.setText(textInCurOne);
        txtInCurOne4.setText(textInCurOne);

        txtInCurTwo1.setText(textInCurTwo);
        txtInCurTwo2.setText(textInCurTwo);
        txtInCurTwo3.setText(textInCurTwo);
        txtInCurTwo4.setText(textInCurTwo);

        txtExcRateNow.setText(f.formatExcRate(excRateNow));

        diffPercent = PercentGrowA - PercentGrowB;
        diffFullSumm = summAbegin*diffPercent/100;

        if (Inverted_conversion) {
            profitBConverted = profitB * excRateNow;
            diffFullSummInCurrB = diffFullSumm/excRateNow;
        }
        else {
            profitBConverted = profitB/excRateNow;
            diffFullSummInCurrB = diffFullSumm*excRateNow;
        }

        diffProfit = profitA - profitBConverted;

        txtPrecentProfitNow.setText(f.format(diffPercent) + " %");
        txtCurOneProfitNow.setText(f.format(diffProfit));


        txtCurOneFullNov.setText(f.format(diffFullSumm));
        txtCurTwoFullNov.setText(f.format(diffFullSummInCurrB));
        txtCurOneFullDinamic.setText(f.format(diffFullSumm));
        txtCurTwoFullDinamic.setText(f.format(diffFullSummInCurrB));

        if (Inverted_conversion) diffInCurrB = diffProfit/excRateNow;
        else diffInCurrB = diffProfit*excRateNow;

        txtCurTwoProfitNow.setText(f.format(diffInCurrB));


        //maybe another approach should be used
        edtExcRateDinamic.setText(f.formatExcRate(excRateNow));
        txtPercentProfitDinamic.setText(f.format(diffPercent) + " %");
        txtCurOneProfitDinamic.setText(f.format(diffProfit));
        txtCurTwoProfitDinamic.setText(f.format(diffInCurrB));

        float excRateCalc;
        if (Inverted_conversion) excRateCalc=(100+diffPercent)*excRateNow/100;
        else  excRateCalc= (100-diffPercent)* excRateNow/100;
        txtExcRateCalc.setText(f.formatExcRate(excRateCalc));

    }


    @Override
    public void onResume(){
        super.onResume();
        edtExcRateDinamic.setFocusable(false);
        edtExcRateDinamic.setFocusableInTouchMode(false);

    }

}
