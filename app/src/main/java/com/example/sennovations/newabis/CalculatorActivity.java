package com.example.sennovations.newabis;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class CalculatorActivity  extends TabActivity implements TabHost.OnTabChangeListener, View.OnClickListener {
    TabHost tabHost;
    Button logout;
    private Boolean exit = false;
    String email,age_group,tax_age_group;
    SqlHandler sqlHandler;
    long TIME_DELAY = 0L;
    private static long back_pressed;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_thumb_port:
                    Intent intent = new Intent(getApplicationContext(),Portfolio.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_cal:
                    item.setIcon(R.drawable.cal2);
                    Intent i = new Intent(getApplicationContext(),CalculatorActivity.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_profile:
//                    Intent ii = new Intent(getApplicationContext(),Subscription.class);
//                    startActivity(ii);
                    return true;
                case R.id.navigation_invest_profile:
                    Intent intent1 = new Intent(getApplicationContext(),InvestmentActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_exit:
                    android.app.AlertDialog.Builder builder;
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new android.app.AlertDialog.Builder(CalculatorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new android.app.AlertDialog.Builder(CalculatorActivity.this);
                        }
                    builder.setMessage("Are you sure you want to exit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();

                    return true;

            }
            return false;
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        sqlHandler = new SqlHandler(this);
//        SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
//        sp.getString("age_group",age_group);
//        sp.getString("tax_age_group",tax_age_group);
//        Log.e("tax_age_group",tax_age_group+""+age_group+"");
        String query = "Select * from age_pattern";
        Cursor c = sqlHandler.selectQuery(query);
        try {
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        age_group = c.getString(c.getColumnIndex("age_pattern_age_group"));
                        tax_age_group = c.getString(c.getColumnIndex("age_pattern_tax_age_group"));
                        email = c.getString(c.getColumnIndex("age_pattern_email"));
                        Log.e("Group", tax_age_group + "" + age_group + ""+email+"");
                    }
                    while (c.moveToNext());
                }
            }
            c.close();
        }catch (Exception e){
            e.getMessage();
        }

//        Intent in = getIntent();
//        email = in.getStringExtra("email");
//        age_group = in.getStringExtra("age_group");
//        tax_age_group = in.getStringExtra("tax_age_group");

        Typeface tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
        TextView image = (TextView)findViewById(R.id.image);
        image.setTypeface(tf);


        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(this);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        tabHost = getTabHost();

        // Set TabChangeListener called when tab changed
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent intent;

        /************* TAB1 ************/
        // Create  Intents to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, FD_INTEREST.class);
        intent.putExtra("email", email);
        intent.putExtra("age_group",age_group);
        intent.putExtra("tax_age_group",tax_age_group);
        spec = tabHost.newTabSpec("First").setIndicator("")
                .setContent(intent);

        //Add intent to tab
        tabHost.addTab(spec);

        /************* TAB2 ************/
        intent = new Intent().setClass(this, RECURRING_FD_INTEREST.class);
        intent.putExtra("email", email);
        intent.putExtra("age_group",age_group);
        intent.putExtra("tax_age_group",tax_age_group);
        spec = tabHost.newTabSpec("Second").setIndicator("")
                .setContent(intent);
        tabHost.addTab(spec);

        /************* TAB3 ************/
        intent = new Intent().setClass(this, EMI.class);
        spec = tabHost.newTabSpec("Third").setIndicator("")
                .setContent(intent);
        tabHost.addTab(spec);
        tabHost.getTabWidget().setStripEnabled(false);


        // Set drawable images to tab
        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setText("FD");
        tv.setTextSize(18);
        tv.setTextColor(Color.parseColor("#ffffff"));
        Typeface tff = Typeface.createFromAsset(getAssets(), "arial.ttf");
        tv.setTypeface(tff);

        TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv1.setText("RD");
        tv1.setTextSize(18);
        tv1.setTextColor(Color.parseColor("#ffffff"));
        Typeface tff1 = Typeface.createFromAsset(getAssets(), "arial.ttf");
        tv1.setTypeface(tff1);

        TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        tv2.setText("EMI");
        tv2.setTextSize(18);
        tv2.setTextColor(Color.parseColor("#ffffff"));
        Typeface tff2 = Typeface.createFromAsset(getAssets(), "arial.ttf");
        tv2.setTypeface(tff2);

        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.ab);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.ab);
//
//        // Set Tab1 as Default tab and change image
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.abch);


    }

    @Override
    public void onTabChanged(String tabId) {

        /************ Called when tab changed *************/

        //********* Check current selected tab and change according images *******/

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);

            if(i==0)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ab);

            else if(i==1)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ab);
            else if(i==2)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ab);
        }


        Log.i("tabs", "CurrentTab: "+tabHost.getCurrentTab());

        if(tabHost.getCurrentTab()==0)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.abch);
        else if(tabHost.getCurrentTab()==1)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.abch);
        else if(tabHost.getCurrentTab()==2)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.abch);

    }


    @Override
    public void onClick(View v) {
        SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();
        e.clear();
        e.commit();
        startActivity(new Intent(CalculatorActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
