package com.by.alex.depositcalcupd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class CompareFragment extends Fragment {

    TextView txtExcRateCalc, vievForFocus, txtItog,
            tab1CurOne, tab1CurTwo,
            tab1SumOne, tab1SumOneCurTwo,
            tab1SumTwo, tab1SumTwoCurTwo,
            tab1DiffOne, tab1DiffOneTwo,
            tab2CurOne, tab2CurTwo,
            tab2SumOne, tab2SumOneCurTwo,
            tab2SumTwo, tab2SumTwoCurTwo,
            tab2DiffOne, tab2DiffOneTwo;

    EditText edtExcRateDinamic;
    Formatter f = new Formatter();

    private float ExcRateNow, ProfitA, ProfitB, PercentGrowA, PercentGrowB, FullSummA, FullSummB;
    private String  itog, vkladOneColored, colorOne, vkladTwoColored, colorTwo,
            kursColored, colorKurs, colorTemplate;
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

        tab1CurOne = (TextView) rootView.findViewById(R.id.tab12);
        tab1CurTwo = (TextView) rootView.findViewById(R.id.tab13);
        tab1SumOne = (TextView) rootView.findViewById(R.id.tab15);
        tab1SumOneCurTwo = (TextView) rootView.findViewById(R.id.tab16);
        tab1SumTwo= (TextView) rootView.findViewById(R.id.tab18);
        tab1SumTwoCurTwo = (TextView) rootView.findViewById(R.id.tab19);
        tab1DiffOne = (TextView) rootView.findViewById(R.id.tab111);
        tab1DiffOneTwo = (TextView) rootView.findViewById(R.id.tab112);

        tab2CurOne = (TextView) rootView.findViewById(R.id.tab22);
        tab2CurTwo = (TextView) rootView.findViewById(R.id.tab23);
        tab2SumOne = (TextView) rootView.findViewById(R.id.tab25);
        tab2SumOneCurTwo = (TextView) rootView.findViewById(R.id.tab26);
        tab2SumTwo= (TextView) rootView.findViewById(R.id.tab28);
        tab2SumTwoCurTwo = (TextView) rootView.findViewById(R.id.tab29);
        tab2DiffOne = (TextView) rootView.findViewById(R.id.tab211);
        tab2DiffOneTwo = (TextView) rootView.findViewById(R.id.tab212);


        itog = getResources().getString(R.string.cmpr_txtItog);
        colorTemplate = "<font color='%1$s'>%2$s </font>";
        colorOne = "green";
        colorKurs = String.valueOf(getResources().getColor(R.color.colorAccent));
        colorTwo = "red";
        vkladOneColored = String.format(colorTemplate, colorOne ,getResources().getStringArray(R.array.tabs_array)[0]);
        vkladTwoColored = String.format(colorTemplate, colorTwo ,getResources().getStringArray(R.array.tabs_array)[1]);

        vievForFocus = (TextView) rootView.findViewById(R.id.txtExcRateCalc);
        txtItog=(TextView) rootView.findViewById(R.id.txtItog);
        txtExcRateCalc = (TextView) rootView.findViewById(R.id.txtExcRateCalc);
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
                float dynRate;
                dynRate = f.parseExcRate(edtExcRateDinamic.getText().toString()); //may be returned 1 if wrong format
                setDinamicPercentFullSummProfit(dynRate);
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

    private void makeItog(float diffPercent) {

        String kursColored = String.format(colorTemplate, colorKurs, edtExcRateDinamic.getText());
        String percentColored = String.format(colorTemplate, colorKurs, f.format(Math.abs(diffPercent)));

        if (diffPercent>=0)
            txtItog.setText(Html.fromHtml(String.format(itog, vkladOneColored, kursColored, percentColored)), TextView.BufferType.SPANNABLE);
        else
            txtItog.setText(Html.fromHtml(String.format(itog, vkladTwoColored, kursColored, percentColored)), TextView.BufferType.SPANNABLE);

    }


    public void saveData(){
        //Send data which will be saved at MainActivity
        //Log.e("befocallbackCompareTab", "befocallbackCompareTab");
        mCallback.saveCompareTabData(777);
    }

    private void setDinamicPercentFullSummProfit(float excRate) {
        float percentDiffRate = (excRate/ExcRateNow - 1)*100;
        float percent, fullSummAConverted, profitBConverted, profitAConverted,
                fullSummBConverted;
        if (Inverted_conversion) {
            percent = this.PercentGrowA - this.PercentGrowB - percentDiffRate;
            profitAConverted = this.ProfitA / excRate;
            profitBConverted = this.ProfitB * excRate;

            fullSummAConverted = this.FullSummA / excRate;
            fullSummBConverted = this.FullSummB * excRate;
        }
        else {
            percent = this.PercentGrowA - this.PercentGrowB + percentDiffRate;
            profitAConverted = this.ProfitA * excRate;
            profitBConverted = this.ProfitB / excRate;

            fullSummAConverted = this.FullSummA * excRate;
            fullSummBConverted = this.FullSummB / excRate;
        }

        makeItog(percent);

        tab1SumOneCurTwo.setText(f.format(profitAConverted));
        tab1SumTwo.setText(f.format(profitBConverted));
        tab1DiffOne.setText(f.format(this.ProfitA - profitBConverted));
        tab1DiffOneTwo.setText(f.format(profitAConverted - this.ProfitB));

        tab2SumOneCurTwo.setText(f.format(fullSummAConverted));
        tab2SumTwo.setText(f.format(fullSummBConverted));
        tab2DiffOne.setText(f.format(this.FullSummA - fullSummBConverted));
        tab2DiffOneTwo.setText(f.format(fullSummAConverted - this.FullSummB));

    }


    public void setDataFromTabs(String currencyA, String currencyB, float profitA, float profitB,
                                float excRateNow, float PercentGrowA, float PercentGrowB,
                                boolean inverted_conversion, float summAbegin) {
        float diffPercent;

        this.Inverted_conversion = inverted_conversion;

        this.ExcRateNow = excRateNow;
        this.PercentGrowA = PercentGrowA;
        this.PercentGrowB = PercentGrowB;
        this.ProfitA = profitA;
        this.ProfitB = profitB;
        this.FullSummA = summAbegin + profitA;
        this.FullSummB = (Inverted_conversion) ? summAbegin/excRateNow + profitB: summAbegin*excRateNow + profitB;

        tab1CurOne.setText(currencyA);
        tab2CurOne.setText(currencyA);
        tab1CurTwo.setText(currencyB);
        tab2CurTwo.setText(currencyB);

        tab1SumOne.setText(f.format(profitA));
        tab1SumTwoCurTwo.setText(f.format(profitB));

        tab2SumOne.setText(f.format(FullSummA));
        tab2SumTwoCurTwo.setText(f.format(FullSummB));

        diffPercent = PercentGrowA - PercentGrowB;
        edtExcRateDinamic.setText(f.formatExcRate(excRateNow));

        float excRateCalc;
        if (Inverted_conversion) excRateCalc=(100+diffPercent)*excRateNow/100;
        else  excRateCalc= (100-diffPercent)* excRateNow/100;
        txtExcRateCalc.setText(f.formatExcRate(excRateCalc));

        setDinamicPercentFullSummProfit(excRateNow);
        makeItog(diffPercent);
    }


    @Override
    public void onResume(){
        super.onResume();
        edtExcRateDinamic.setFocusable(false);
        edtExcRateDinamic.setFocusableInTouchMode(false);
    }

}
