package com.by.alex.depositcalcupd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.appodeal.ads.Appodeal;
import com.by.alex.depositcalcupd.adapter.TabsPagerAdapter;
import com.by.alex.depositcalcupd.help.HelpDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;


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
    public static final String APPODEAl_KEY = "2692085ef276225935b92b6e70888009f34d1df42690867b";
    //adsdisable
    String inappid = "android.test.purchased";


    SharedPreferences mSettings;

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

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            checkPurchase();
        }
    };


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOverflowMenu();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        boolean showOverlays = mSettings.getBoolean("show_overlays", true);
        if (showOverlays == true)
        {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("show_overlay_0", true);
            editor.putBoolean("show_overlay_1", true);
            editor.putBoolean("show_overlay_2", true);
            editor.putBoolean("show_overlays", false);
            editor.commit();
        }

        //billing
        //https://stuff.mit.edu/afs/sipb/project/android/docs/google/play/billing/billing_integrate.html
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


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

        if (!mSettings.getBoolean("AdsDisable", false)) {
            // Appodeal
            String appKey = MainActivity.APPODEAl_KEY;
            Appodeal.disableLocationPermissionCheck();
            Appodeal.initialize(this, appKey, Appodeal.BANNER);

            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        }

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
            case R.id.off_adv:
                buyAdsDisable();
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

    private void buyAdsDisable() {
        String tag = "test_purchase";

        if (mService == null) return;

        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add(inappid);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        Bundle skuDetails;
        try {
            skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);

            Toast.makeText(this, "getSkuDetails() - success return Bundle", Toast.LENGTH_SHORT).show();
            Log.i(tag, "getSkuDetails() - success return Bundle");
        } catch (RemoteException e) {
            e.printStackTrace();

            Toast.makeText(this, "getSkuDetails() - fail!", Toast.LENGTH_SHORT).show();
            Log.w(tag, "getSkuDetails() - fail!");
            return;
        }

        int response = skuDetails.getInt("RESPONSE_CODE");
        Toast.makeText(this, "getSkuDetails() - \"RESPONSE_CODE\" return " + String.valueOf(response), Toast.LENGTH_SHORT).show();
        Log.i(tag, "getSkuDetails() - \"RESPONSE_CODE\" return " + String.valueOf(response));

        if (response != 0) return;

        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
        Log.i(tag, "getSkuDetails() - \"DETAILS_LIST\" return " + responseList.toString());

        if (responseList.size() == 0) return;

        for (String thisResponse : responseList) {
            try {
                JSONObject object = new JSONObject(thisResponse);

                String sku   = object.getString("productId");
                String title = object.getString("title");
                String price = object.getString("price");

                Log.i(tag, "getSkuDetails() - \"DETAILS_LIST\":\"productId\" return " + sku);
                Log.i(tag, "getSkuDetails() - \"DETAILS_LIST\":\"title\" return " + title);
                Log.i(tag, "getSkuDetails() - \"DETAILS_LIST\":\"price\" return " + price);

                if (!sku.equals(inappid)) continue;

                Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(), sku, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

                Toast.makeText(this, "getBuyIntent() - success return Bundle", Toast.LENGTH_SHORT).show();
                Log.i(tag, "getBuyIntent() - success return Bundle");

                response = buyIntentBundle.getInt("RESPONSE_CODE");
                Toast.makeText(this, "getBuyIntent() - \"RESPONSE_CODE\" return " + String.valueOf(response), Toast.LENGTH_SHORT).show();
                Log.i(tag, "getBuyIntent() - \"RESPONSE_CODE\" return " + String.valueOf(response));

                if (response != 0) continue;

                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                try {
                    startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();

                Toast.makeText(this, "getSkuDetails() - fail!", Toast.LENGTH_SHORT).show();
                Log.w(tag, "getBuyIntent() - fail!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String tag = "test_purchase";
        if (requestCode == 1001) {
            if (resultCode != RESULT_OK) return;

            int responseCode = data.getIntExtra("RESPONSE_CODE", 1);
            Toast.makeText(this, "onActivityResult() - \"RESPONSE_CODE\" return " + String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
            Log.i(tag, "onActivityResult() - \"RESPONSE_CODE\" return " + String.valueOf(responseCode));

            if (responseCode != 0) return;

            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            Log.i(tag, "onActivityResult() - \"INAPP_PURCHASE_DATA\" return " + purchaseData.toString());
            Log.i(tag, "onActivityResult() - \"INAPP_DATA_SIGNATURE\" return " + dataSignature.toString());

            // TODO: management purchase result
            if (purchaseData.toString().equals(inappid)){
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("AdsDisable", true);
                editor.commit();

                //disable menu
                invalidateOptionsMenu();
            }
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

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makeFragmentName(int viewId, int index)
    {
        return "android:switcher:" + viewId + ":" + mAdapter.getItemId(index) ;
    }

    private void checkPurchase() {
            String tag = "test_purchase";

            if (mService == null) return;

            Bundle ownedItems;
            try {
                ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
                Log.i(tag, "checkPurchase() - success return Bundle");
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.w(tag, "checkPurchase() - fail! No Bundle get");
                return;
            }

            int response = ownedItems.getInt("RESPONSE_CODE");
            Log.i(tag, "checkPurchase() - RESPONSE_CODE return " + String.valueOf(response));

            if (response != 0) return;

            ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
            String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

            Log.i(tag, "getPurchases() - \"INAPP_PURCHASE_ITEM_LIST\" return " + ownedSkus.toString()); // must be adsdisable
            Log.i(tag, "getPurchases() - \"INAPP_PURCHASE_DATA_LIST\" return " + purchaseDataList.toString());
            Log.i(tag, "getPurchases() - \"INAPP_DATA_SIGNATURE\" return " + (signatureList != null ? signatureList.toString() : "null"));
            Log.i(tag, "getPurchases() - \"INAPP_CONTINUATION_TOKEN\" return " + (continuationToken != null ? continuationToken : "null"));

            // TODO: management owned purchase
            if (ownedSkus.toString().contains(inappid)){
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("AdsDisable", true);
                editor.commit();

                //disable menu
                invalidateOptionsMenu();
            }

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {

        MenuItem item = menu.findItem(R.id.off_adv);
        if (mSettings.getBoolean("AdsDisable", false)) {
            //Disable off_adv menu item
            item.setEnabled(false);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSettings.getBoolean("AdsDisable", false))
            Appodeal.onResume(this, Appodeal.BANNER);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

}
