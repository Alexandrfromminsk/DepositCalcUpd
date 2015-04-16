package com.by.alex.depositcalcupd.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.by.alex.depositcalcupd.CompareFragment;
import com.by.alex.depositcalcupd.CurrencyOneFragment;
import com.by.alex.depositcalcupd.CurrencyTwoFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new CurrencyOneFragment();
            case 1:
                return new CurrencyTwoFragment();
            case 2:
                return new CompareFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
