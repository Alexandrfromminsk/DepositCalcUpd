package com.by.alex.depositcalcupd.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebView;

import com.by.alex.depositcalcupd.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_detail);
        WebView webView = (WebView) findViewById(R.id.webView);

        Intent intent = getIntent();
        //get line and create res name
        String resName = "help" + intent.getIntExtra("head", 1);
        Log.i("name", resName);
        Context context = getBaseContext(); //get Context

        //Read text from from res by name
        String text = readRawTextFile(context, getResources().getIdentifier(resName, "raw", getPackageName()));

        webView.loadDataWithBaseURL(null, text, "text/html", "en_US", null);
    }

    //Read text from raw-res
    public static String readRawTextFile(Context context, int resId)
    {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputReader);
        String line;
        StringBuilder builder = new StringBuilder();

        try {
            while (( line = buffReader.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            return null;
        }
        return builder.toString();
    }
}

