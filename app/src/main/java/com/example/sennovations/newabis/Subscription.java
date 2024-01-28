package com.example.sennovations.newabis;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * Created by Sennovations on 18-07-2017.
 */

public class Subscription extends FragmentActivity {

    ViewPager viewpager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        viewpager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);



    }

    public void setViewpager(int fragmentnumber){
        viewpager.setCurrentItem(fragmentnumber);
    }



}
