package com.by.alex.depositcalcupd;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.by.alex.depositcalcupd.adapter.TabsPagerAdapter;

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

    private String[] tabs = {"Валюта 1", "Валюта 2", "Сравнение"};
    //private String[] tabs = getString(R.array.tabs_array);
    private static final int TAB_ONE = 0;
    private static final int TAB_TWO = 1;
    private static final int TAB_COMPARE = 2;

    //Save data fromtabs for communication
    String DataBegin, DataEnd, CurrencyA, CurrencyB;
    int   Timeperiod, Spn_timeperiod;
    float SummA, ExcRate;
    float ProfitA, ProfitB;

    //private Toolbar mToolbar;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myManager = getSupportFragmentManager();

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);



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
                //Toast.makeText(getApplicationContext(), "охуенно".toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "setSelectedNavigationItem".toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //Toast.makeText(getApplicationContext(), "onPageScrolled".toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                //Toast.makeText(getApplicationContext(), "onPageScrollStateChanged".toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

        String tag = makeFragmentName(mViewPager.getId(), tab.getPosition());

        switch (tab.getPosition()){
            case TAB_TWO:
                secondTab = (CurrencyTwoFragment) myManager.findFragmentByTag(tag);
                secondTab.setDataFromFirstTab(CurrencyA, (float) SummA, Timeperiod, Spn_timeperiod, DataBegin, DataEnd);
                break;
            case TAB_COMPARE:
                compareTab = (CompareFragment) myManager.findFragmentByTag(tag);
                compareTab.setDataFromTabs(CurrencyA, CurrencyB, ProfitA, ProfitB, ExcRate);
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

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Toast.makeText(getApplicationContext(), "onTabReselected".toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void saveFirstTabData(String spn_currency, float summ, int timeperiod, int spn_tpr, String dateBegin, String dateEnd, float profit) {
        this.CurrencyA = spn_currency;
        this.SummA = summ;
        this.Timeperiod = timeperiod;
        this.Spn_timeperiod = spn_tpr;
        this.DataBegin = dateBegin;
        this.DataEnd = dateEnd;
        this.ProfitA = profit;
    }

    @Override
    public void saveSecondTabData(String currency, float conversion, float profit) {
        this.ExcRate = conversion;
        this.ProfitB = profit;
        this.CurrencyB = currency;

    }

    @Override
    public void saveCompareTabData(int position) {

    }


    private String makeFragmentName(int viewId, int index)
    {
        return "android:switcher:" + viewId + ":" + mAdapter.getItemId(index) ;
    }

}
