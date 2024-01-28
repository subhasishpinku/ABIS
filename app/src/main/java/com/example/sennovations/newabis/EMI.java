package com.example.sennovations.newabis;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sennovations on 02-06-2017.
 */

public class EMI extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    SeekBar seekBar;
    TextView textView;
    Spinner s_day,s_month,s_year;
    String year,month,day;
    EditText amou;
    EditText intamount;
    EditText emi;
    Button compute;
    double amount = 0;
    Double seekValue1 = 0.0;
    ProgressDialog progressDialog;
    int int_rate_default,max_int_rate,min_int_rate;
    int minimumVal;
    SqlHandler sqlHandler;
    String type = "EMI";
    String[] array ;
    Spinner mySpin;
    private String[] arraySpinner2;
    int loantype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_emi);
        sqlHandler = new SqlHandler(this);

        Typeface tf = Typeface.createFromAsset(getAssets(), "helvetica.otf");
        TextView text1 = (TextView)findViewById(R.id.text1);
        TextView text2 = (TextView)findViewById(R.id.text2);
        TextView text3 = (TextView)findViewById(R.id.text3);
        TextView text4 = (TextView)findViewById(R.id.text4);
        TextView text5 = (TextView)findViewById(R.id.text5);
        TextView text6 = (TextView)findViewById(R.id.text6);
        TextView text7 = (TextView)findViewById(R.id.text7);
        TextView text8 = (TextView)findViewById(R.id.text8);
        TextView text9 = (TextView)findViewById(R.id.text9);
        TextView text10 = (TextView)findViewById(R.id.text10);
        text1.setTypeface(tf);
        text2.setTypeface(tf);
        text3.setTypeface(tf);
        text4.setTypeface(tf);
        text5.setTypeface(tf);
        text6.setTypeface(tf);
        text7.setTypeface(tf);
        text8.setTypeface(tf);
        text9.setTypeface(tf);
        text10  .setTypeface(tf);


        textView = (TextView)findViewById(R.id.seekbarvalue);
        seekBar = (SeekBar)findViewById(R.id.emiseekBar);

        seekBar.setOnSeekBarChangeListener(this);
        amou = (EditText)findViewById(R.id.amount);
        emi = (EditText)findViewById(R.id.emi);
        intamount = (EditText)findViewById(R.id.intamount);
        compute =(Button)findViewById(R.id.compute);
        compute.setOnClickListener(this);

//        EMI();
        show2();
        show3();

        s_day = (Spinner)findViewById(R.id.s_day);
        s_month = (Spinner)findViewById(R.id.s_month);
        s_year = (Spinner)findViewById(R.id.s_year);

        String[] list1 = new String[] {"Y","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list1);
        s_year.setAdapter(adapter1);
        s_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                year = s_year.getSelectedItem().toString();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
                Log.e("Year value",year+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        String[] list2 = new String[] {"M","0","1","2","3","4","5","6","7","8","9","10","11","12"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list2);
        s_month.setAdapter(adapter2);
        s_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                month = s_month.getSelectedItem().toString();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
                Log.e("Year value",month+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        String[] list3 = new String[] {"D","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list3);
        s_day.setAdapter(adapter3);
        s_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                day = s_day.getSelectedItem().toString();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
                Log.e("Year value",day+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        String[] a =new String[]{"Select Type"};
        List<String> array3 = new ArrayList<>();
        Collections.addAll(array3,a);
        Collections.addAll(array3,array);
        String[] finalArray2 = array3.toArray(new String[array3.size()]);

        this.arraySpinner2 =  finalArray2;

        mySpin = (Spinner) findViewById(R.id.loantype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner2);
        mySpin.setAdapter(adapter);

        mySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                loantype = mySpin.getSelectedItemPosition();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
//                                                 payout = mySpin.getSelectedItem().toString();

                Log.e("PAYOUT Spinner value",loantype+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });



    }

//    private void EMI() {
//        String url = "http://solodiary.com/abis/webservice/calculator/?cal_type=emi";
//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Response",response);
//                        try{
//                            JSONObject jsonObject=new JSONObject(response);
//                            String statusCode=jsonObject.getString("status");
//                            String int_rate  =jsonObject.getString("int_rate");
//                            //-------------------int_rate---------------------
//                            JSONObject jsonObject1 = new JSONObject(int_rate);
//                            min_int_rate = jsonObject1.getInt("min_int_rate");
//                            max_int_rate = jsonObject1.getInt("max_int_rate");
//                            int_rate_default = jsonObject1.getInt("int_rate_default");
//
//                            String query = "Insert into INTEREST (type,min_int_rate,max_int_rate,int_rate_default) values ('"+ type +"','"+ min_int_rate +"','"+ max_int_rate +"','"+ int_rate_default +"') ";
//                            sqlHandler.executeQuery(query);
//                            Log.e("Query",query);
//
//                            //------------Interest Insert Completed  ----------------------------
//
//
//                            JSONArray emi_type = jsonObject.getJSONArray("emi_type");
//                            Log.e("emi_type", String.valueOf(emi_type));
//                            for(int i=0;i<emi_type.length();i++){
//                                JSONObject json_obj_emi_type = emi_type.getJSONObject(i);
//
//                                String NAME = json_obj_emi_type.getString("NAME");
//                                String DESCRIPTION = json_obj_emi_type.getString("DESCRIPTION");
//                                String DEFAULT_VALUE = json_obj_emi_type.getString("DEFAULT_VALUE");
//
//                                Log.e("json_obj_compounding",NAME+",,"+DESCRIPTION+",,"+DEFAULT_VALUE);
//
//                                String query3 = "Insert into emi_type (emi_type_NAME,emi_type_DESCRIPTION,emi_type_VALUE) values ('"+ NAME +"','"+ DESCRIPTION +"','"+ DEFAULT_VALUE +"') ";
//                                sqlHandler.executeQuery(query3);
//                                Log.e("Query",query3);
//
//                            }
//
//                            //------------------- COMPOUNDING_FD Insert Completed----------------------------
//
//
//
//
//                            Log.e("FD DETAILS",statusCode+" - - "+" - - "+int_rate_default+"----"+max_int_rate+"----"+min_int_rate+"");
//
//                            seekBar.setMax(max_int_rate);
//                            minimumVal = min_int_rate;
//                            seekBar.setProgress(int_rate_default);
//                            Log.e("Rate",max_int_rate+"--"+int_rate_default+"");
//
//                        }catch (Exception e){
//                            if (progressDialog!=null){
//                                progressDialog.dismiss();
//                            }
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Error","volley error"+error.getMessage());
//                if (progressDialog!=null){
//                    progressDialog.dismiss();
//                }
//
//            }
//        }) ;
//        requestQueue.add(stringRequest);
//    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekValue1 = Double.valueOf(seekBar.getProgress()).doubleValue();
//        textView.setText(Double.toString(seekValue1));
        textView.setText(String .format("%.02f", seekValue1) + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    public void show2(){
        String q = "Select * from emi_type";
        Cursor c1 = sqlHandler.selectQuery(q);
        array = new String[c1.getCount()];
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String emi_type_DESCRIPTION = c1.getString(c1.getColumnIndex("emi_type_DESCRIPTION"));
                    array[i] = emi_type_DESCRIPTION;

                    i++;

                }while (c1.moveToNext());
            }
        }
        c1.close();



    }
    public void show3(){
        String q = "Select * from INTEREST where Type  = 'EMI' ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    max_int_rate = c1.getInt(c1.getColumnIndex("max_int_rate"));
                    min_int_rate = c1.getInt(c1.getColumnIndex("min_int_rate"));
                    int_rate_default = c1.getInt(c1.getColumnIndex("int_rate_default"));
                    seekBar.setMax(max_int_rate);
                    minimumVal = min_int_rate;
                    seekBar.setProgress(int_rate_default);
                    Log.e("Rate",max_int_rate+"--"+int_rate_default+"");
                }while (c1.moveToNext());
            }
        }
        c1.close();



    }

    @Override
    public void onClick(View v) {
        DecimalFormat form = new DecimalFormat("0.00");
        if(amou.length() == 0 ){
            amou.setError("Please Enter the Amount");
        }
        else {
            amount = Double.parseDouble(amou.getText().toString());
        }

        Double a = 0.0;
        Log.e("AMOUNT ",amount+"");
        double tenture;
        double year1,month1,day1;

        if(year.equals("Y")){
            year1 = 0.0;
        }
        else {
            year1 = Double.parseDouble(year);
        }
        if(month.equals("M")){

            month1 = 0.0;
        }else {
            month1 = Double.parseDouble(month);
        }
        if(day.equals("D")){
            day1 = 0.0;
        }else {
            day1 = Double.parseDouble(day);
        }

        Log.e("YEAR-Month-day",year1+"--"+month1+"--"+day1+"");

        tenture = (day1/30.0)+(year1*12)+month1;
        Log.e(" Tenture",tenture+"");


        double b = amount*seekValue1/(12*100);
        double c =1+(seekValue1/1200);
        double d = -(tenture);
        double E1 = Math.pow(c, d);
        double f = (1-E1);
        a = b/f;
        emi.setText(form.format(a) );
        double g = (a*tenture)-amount;
        intamount.setText(form.format(g));


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}