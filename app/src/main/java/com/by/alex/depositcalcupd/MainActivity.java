package com.by.alex.depositcalcupd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.by.alex.depositcalcupd.adapter.TabsPagerAdapter;
import com.by.alex.depositcalcupd.help.HelpDialog;

//import android.app.FragmentManager;


public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener, OnTabChangedListener {

    ViewPager mViewPager;
    ActionBar actionBar;
    TabsPagerAdapter mAdapter;
    FragmentManager myManager;
    CurrencyOneFragment firstTab;
    CurrencyTwoFragment secondTab;
    CompareFragment compareTab;

    public static final String APP_PREFERENCES = "calcsettings";

    SharedPreferences mSettings, defaultSettings;

    private String[] tabs;
    private static final int TAB_ONE = 0;
    private static final int TAB_TWO = 1;
    private static final int TAB_COMPARE = 2;

    //Save data fromtabs for communication
    private String DataBegin, DataEnd, CurrencyA, CurrencyB;
    private int   Timeperiod, Spn_timeperiod;
    private float SummA, ExcRate, PercentGrowA, PercentGrowB;
    private float ProfitA, ProfitB;
    private boolean Inverted_conversion;

    //private Toolbar mToolbar;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        defaultSettings = PreferenceManager.getDefaultSharedPreferences(this);

        boolean showOverlays = defaultSettings.getBoolean("show_overlays", true);
        if (showOverlays == true)
        {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("show_overlay_0", true);
            editor.putBoolean("show_overlay_1", true);
            editor.putBoolean("show_overlay_2", true);
            editor.commit();
            SharedPreferences.Editor editorDefault = defaultSettings.edit();
            editorDefault.putBoolean("showOverlays", false);
            editor.commit();
        }


        setContentView(R.layout.activity_main);
        tabs = getResources().getStringArray(R.array.tabs_array);
        myManager = getSupportFragmentManager();

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        //Add tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sendEmail:
                sendEmail();
                return true;
            case R.id.feedback:
                leaveFeedback();
                return true;
            case R.id.help:
                showHelp();
                return true;
            case R.id.about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void leaveFeedback() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void sendEmail() {
        Intent Email = new Intent(android.content.Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "android.depositcalc@gmail.com" });
        Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
        try {
            startActivity(Email);;
        } catch (android.content.ActivityNotFoundException exception) {
            Toast.makeText(this, getResources().getString(R.string.sendEmail_error), Toast.LENGTH_LONG).show();
        }

    }

    private void showAbout() {
        final AlertDialog aboutDialog = new AlertDialog.Builder(
                this).setMessage(getResources().getString(R.string.about_text))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();

        aboutDialog.show();
    }

    private void showHelp() {

        FragmentManager fm = getSupportFragmentManager();
        HelpDialog dialogFragment = new HelpDialog ();
        dialogFragment.show(fm, "");

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

        String tag = makeFragmentName(mViewPager.getId(), tab.getPosition());

        switch (tab.getPosition()){
            case TAB_ONE:
                showActivityOverlay(TAB_ONE);
                break;
            case TAB_TWO:
                secondTab = (CurrencyTwoFragment) myManager.findFragmentByTag(tag);
                secondTab.setDataFromFirstTab(CurrencyA, SummA, Timeperiod, Spn_timeperiod, DataBegin, DataEnd);
                showActivityOverlay(TAB_TWO);
                break;
            case TAB_COMPARE:
                compareTab = (CompareFragment) myManager.findFragmentByTag(tag);
                compareTab.setDataFromTabs(CurrencyA, CurrencyB, ProfitA, ProfitB, ExcRate,
                        PercentGrowA, PercentGrowB, Inverted_conversion);
                showActivityOverlay(TAB_COMPARE);
                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        String tag = makeFragmentName(mViewPager.getId(), tab.getPosition());

        switch (tab.getPosition()){
            case TAB_ONE:
                firstTab = (CurrencyOneFragment) myManager.findFragmentByTag(tag);
                firstTab.saveData();
                break;
            case TAB_TWO:
                secondTab = (CurrencyTwoFragment) myManager.findFragmentByTag(tag);
                secondTab.saveData();
                break;
            case TAB_COMPARE:
                compareTab = (CompareFragment) myManager.findFragmentByTag(tag);
                compareTab.saveData();
                break;
        }
    }
    private void showActivityOverlay(int tab_number) {

        String settings_name = "show_overlay_" + tab_number;

        boolean showOverlay = mSettings.getBoolean(settings_name, true);
        if (showOverlay == true) {

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(settings_name, false);
            editor.commit();

            final Dialog dialog = new Dialog(this,
                    android.R.style.Theme_Translucent_NoTitleBar);

            dialog.setContentView(R.layout.overlay_activity);

            LinearLayout layout = (LinearLayout) dialog
                    .findViewById(R.id.Overlay_activity);
            ImageView iv = (ImageView) dialog.findViewById(R.id.ivOverlayEntertask);
            String image_name = "overlay_" + tab_number;
            iv.setImageResource(getResources().getIdentifier(image_name, "drawable", getPackageName()));
            layout.setBackgroundColor(Color.TRANSPARENT);
            layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Toast.makeText(getApplicationContext(), "onTabReselected".toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void saveFirstTabData(String spn_currency, float summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd, float profit, float percent_grow) {
        this.CurrencyA = spn_currency;
        this.SummA = summ;
        this.Timeperiod = timeperiod;
        this.Spn_timeperiod = spn_tpr;
        this.DataBegin = dateBegin;
        this.DataEnd = dateEnd;
        this.ProfitA = profit;
        this.PercentGrowA = percent_grow;
    }

    @Override
    public void saveSecondTabData(String currency, float conversion, float profit,
                                  float percent_grow, boolean inverted_conversion) {
        this.ExcRate = conversion;
        this.ProfitB = profit;
        this.CurrencyB = currency;
        this.PercentGrowB = percent_grow;
        this.Inverted_conversion = inverted_conversion;

    }

    @Override
    public void saveCompareTabData(int position) {

    }


    private String makeFragmentName(int viewId, int index)
    {
        return "android:switcher:" + viewId + ":" + mAdapter.getItemId(index) ;
    }

}
