package com.by.alex.depositcalcupd.help;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.by.alex.depositcalcupd.R;

public class HelpDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.help_dialog, container, false);
        getDialog().setTitle(getResources().getString(R.string.help_title));

        Button help_category_one = (Button)rootView.findViewById(R.id.help_category_one);
        help_category_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpCategory(1);
            }
        });

        Button help_category_two = (Button)rootView.findViewById(R.id.help_category_two);
        help_category_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpCategory(2);
            }
        });

        Button help_category_three = (Button)rootView.findViewById(R.id.help_category_three);
        help_category_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpCategory(3);
            }
        });

        Button btnDismiss = (Button)rootView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

        public void showHelpCategory(int position) {

            Intent intent = new Intent();
            intent.setClass(getActivity(), HelpDetailActivity.class);

            intent.putExtra("head", position);

            //запускаем вторую активность
            startActivity(intent);
        }

    }




