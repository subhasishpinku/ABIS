package com.example.sennovations.newabis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
    private Free free;
    private Premium premium;
    private Basic basic;
    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 0:
                free = new Free();
                return (Fragment)free;
            case 1:
                basic = new Basic();
                return (Fragment)basic;
            case 2:
                premium = new Premium();
                return (Fragment)premium;

            default:
                return  null;

        }
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

}

