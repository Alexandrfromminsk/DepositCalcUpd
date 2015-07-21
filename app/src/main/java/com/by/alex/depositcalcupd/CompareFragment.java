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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CompareFragment extends Fragment {

    TextView txtPrecentProfitNow,txtCurOneProfitNow, txtCurTwoProfitNow, txtExcRateNow,
            txtInCurTwo1, txtInCurTwo2, txtInCurOne1,txtInCurOne2, txtExcRateCalc, vievForFocus,
            txtPrecentProfitDinamic,txtCurOneProfitDinamic, txtCurTwoProfitDinamic;
    EditText edtExcRateDinamic;
    Formatter f = new Formatter();

    private float ExcRateNow, ProfitA, ProfitB, PercentGrowA, PercentGrowB;
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
        txtInCurTwo1 = (TextView) rootView.findViewById(R.id.txtInCurTwo1);
        txtInCurTwo2 = (TextView) rootView.findViewById(R.id.txtInCurTwo2);

        vievForFocus = (TextView) rootView.findViewById(R.id.txtNoDiffInProfit);
        txtPrecentProfitNow = (TextView) rootView.findViewById(R.id.txtPrecentProfitNow);
        txtCurOneProfitNow = (TextView) rootView.findViewById(R.id.txtCurOneProfitNow);
        txtCurTwoProfitNow = (TextView) rootView.findViewById(R.id.txtCurTwoProfitNow);
        txtExcRateNow = (TextView) rootView.findViewById(R.id.txtExcRateNow);

        txtExcRateCalc = (TextView) rootView.findViewById(R.id.txtExcRateCalc);

        txtPrecentProfitDinamic = (TextView) rootView.findViewById(R.id.txtPrecentProfitDinamic);
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
                setDinamicPercent(dynRate);
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

        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void saveData(){
        //Send data which will be saved at MainActivity
        //Log.e("befocallbackCompareTab", "befocallbackCompareTab");
        mCallback.saveCompareTabData(777);

    }

    private void setDinamicPercent(float excRate) {
        float percentDiffRate = (excRate/ExcRateNow - 1)*100;
        txtPrecentProfitDinamic.setText(f.format(this.PercentGrowA - this.PercentGrowB - percentDiffRate) + "%");
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

        txtInCurOne1.setText(String.format("%s 1, %s", textInCur, currencyA));
        txtInCurOne2.setText(String.format("%s 1, %s", textInCur, currencyA));
        txtInCurTwo1.setText(String.format("%s 2, %s", textInCur, currencyB));
        txtInCurTwo2.setText(String.format("%s 2, %s", textInCur, currencyB));

        txtExcRateNow.setText(f.formatExcRate(excRateNow));

        if (Inverted_conversion) profitBConverted = profitB*excRateNow;
        else profitBConverted = profitB/excRateNow;

        diffProfit = profitA - profitBConverted;

        diffPercent = PercentGrowA - PercentGrowB;

        txtPrecentProfitNow.setText(f.format(diffPercent) + " %");
        txtCurOneProfitNow.setText(f.format(diffProfit));
        if (Inverted_conversion) diffInCurrB = diffProfit/excRateNow;
        else diffInCurrB = diffProfit*excRateNow;

        txtCurTwoProfitNow.setText(f.format(diffInCurrB));


        //maybe another approach should be used
        edtExcRateDinamic.setText(f.formatExcRate(excRateNow));
        txtPrecentProfitDinamic.setText(f.format(diffPercent) + " %");
        txtCurOneProfitDinamic.setText(f.format(diffProfit));
        txtCurTwoProfitDinamic.setText(f.format(diffInCurrB));

        float excRateCalc = (100+diffPercent)* excRateNow/100;
        txtExcRateCalc.setText(f.formatExcRate(excRateCalc));

    }


    @Override
    public void onResume(){
        super.onResume();
        edtExcRateDinamic.setFocusable(false);
        edtExcRateDinamic.setFocusableInTouchMode(false);

    }

}
