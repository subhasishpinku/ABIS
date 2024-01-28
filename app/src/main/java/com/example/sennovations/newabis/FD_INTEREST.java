package com.example.sennovations.newabis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.lang.*;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sennovations on 02-06-2017.
 */

public class FD_INTEREST extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    SeekBar seekBar;
    TextView textView;
    Spinner mySpin,s_day,s_month,s_year,compounding;
    private String[] arraySpinner;
    private String[] arraySpinner2;
    ProgressDialog progressDialog;
    int int_rate_default,max_int_rate,min_int_rate;
    int min_tenure,max_tenure,tenure_default;
    int minimumVal;
    SqlHandler sqlHandler;
    String type = "FD";
    String[] array ;
    String[] array2;
    String[] more;
    int payout;
    int compound;
    Button compute;
    EditText amou;
    EditText intamount,taxincome,taxonint,ann_eff_return;
    double amount = 0;
    Double seekValue1 = 0.0;
    String year,month,day;
    LinearLayout ll_compounding,lltenure,ll_eff;
    private Boolean exit = false;
    ArrayList<String> Array1 = new ArrayList<String>();
    ArrayList<String> Array2 = new ArrayList<String>();
    long max,min;
    String email;
    String age_group,tax_age_group;
    int taxable_Income;
    int taxable_Income_more;
    double taxable_Income2;
    double tax = 0,tax2 = 0 ;
    Double eff_ret = 0.0;
    Double tax3 = 0.0;
    Double a = 0.0;
    double tenture;
    double year1,month1,day1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fd_interest);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        age_group = intent.getStringExtra("age_group");
        tax_age_group = intent.getStringExtra("tax_age_group");
        Log.e("email",email+""+age_group+""+tax_age_group+"");
        ll_compounding = (LinearLayout)findViewById(R.id.ll_compounding);
        ll_compounding.setVisibility(View.GONE);
        lltenure = (LinearLayout)findViewById(R.id.lltenure);
        lltenure.setVisibility(View.GONE);
        ll_eff = (LinearLayout)findViewById(R.id.ll_eff);
        ll_eff.setVisibility(View.GONE);
        Typeface tf = Typeface.createFromAsset(getAssets(), "helvetica.otf");
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        TextView text2 = (TextView)findViewById(R.id.text2);
        TextView text3 = (TextView)findViewById(R.id.text3);
        TextView text4 = (TextView)findViewById(R.id.text4);
        TextView text5 = (TextView)findViewById(R.id.text5);
        TextView text6 = (TextView)findViewById(R.id.text6);
        TextView text7 = (TextView)findViewById(R.id.text7);
        TextView text8 = (TextView)findViewById(R.id.text8);
        TextView text9 = (TextView)findViewById(R.id.text9);
        textView2.setTypeface(tf);
        text2.setTypeface(tf);
        text3.setTypeface(tf);
        text4.setTypeface(tf);
        text5.setTypeface(tf);
        text6.setTypeface(tf);
        text7.setTypeface(tf);
        text8.setTypeface(tf);
        text9.setTypeface(tf);




        sqlHandler = new SqlHandler(this);
        s_day = (Spinner)findViewById(R.id.s_day);
        s_month = (Spinner)findViewById(R.id.s_month);
        s_year = (Spinner)findViewById(R.id.s_year);
        compute = (Button)findViewById(R.id.compute);
        compute.setOnClickListener(this);
        amou = (EditText)findViewById(R.id.amount);
        intamount = (EditText)findViewById(R.id.intamount);
        taxincome = (EditText)findViewById(R.id.taxincome);
        taxonint = (EditText)findViewById(R.id.taxonint);
        ann_eff_return = (EditText)findViewById(R.id.ann_eff_return);

//        fdDetails();

        textView = (TextView)findViewById(R.id.seekbarvalue);
        seekBar = (SeekBar)findViewById(R.id.fdseekBar);

        seekBar.setOnSeekBarChangeListener(this);
//        show5();
        show();
        show2();
        show3();
        show4();

        Log.e("pay_fd_name",array+"");
        String[] b = new String[]{"Select Type"};

        List<String> array4 = new ArrayList<>();
        Collections.addAll(array4,b);
        Collections.addAll(array4,array);
        String[] finalArray1 = array4.toArray(new String[array4.size()]);
        this.arraySpinner = finalArray1;

        String[] a =new String[]{"Select Type"};
        List<String> array3 = new ArrayList<>();
        Collections.addAll(array3,a);
        Collections.addAll(array3,array2);
        String[] finalArray2 = array3.toArray(new String[array3.size()]);

        this.arraySpinner2 =  finalArray2;

        mySpin = (Spinner) findViewById(R.id.payouttype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        mySpin.setAdapter(adapter);

        mySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                             @Override
                                             public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                                 payout = mySpin.getSelectedItemPosition();
                                                 ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
//                                                 payout = mySpin.getSelectedItem().toString();
                                                 if(payout == 1){
                                                     ll_compounding.setVisibility(View.VISIBLE);
                                                     lltenure.setVisibility(View.VISIBLE);
                                                     ll_eff.setVisibility(View.VISIBLE);
                                                 }else {
                                                     ll_compounding.setVisibility(View.GONE);
                                                     lltenure.setVisibility(View.GONE);
                                                     ll_eff.setVisibility(View.GONE);
                                                 }

                                                 Log.e("PAYOUT Spinner value",payout+"");
                                             }

                                             @Override
                                             public void onNothingSelected(AdapterView<?> parentView) {
                                                 // your code here
                                             }
                                         });


        compounding = (Spinner)findViewById(R.id.compounding);
        ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner2);
        compounding.setAdapter(adapter0);

        compounding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                                        compound = compounding.getSelectedItemPosition();
                                                        ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
//                                                        compound = compounding.getSelectedItem().toString();
                                                        Log.e("Compounding Spinner value",compound+"");
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parentView) {
                                                        // your code here
                                                    }
                                                });



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
                Log.e("Month value",month+"");
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
                Log.e("Day value",day+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }


//    private void fdDetails() {
//        String url = "http://solodiary.com/abis/webservice/calculator/?cal_type=fd";
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
//                           //------------Interest Insert Completed  ----------------------------
//
//                            String tenure = jsonObject.getString("tenure");
//                            JSONObject jsonObject2 = new JSONObject(tenure);
//                            min_tenure = jsonObject2.getInt("min_tenure");
//                            max_tenure = jsonObject2.getInt("max_tenure");
//                            tenure_default = jsonObject2.getInt("tenure_default");
//                            String query1 = "Insert into TENURE (tenure_type,min_tenure,max_tenure,tenure_default) values ('"+ type +"','"+ min_tenure +"','"+ max_tenure +"','"+ tenure_default +"') ";
//                            sqlHandler.executeQuery(query1);
//                            Log.e("Query",query1);
//                            //------------------- Tenure Insert Completed----------------------------
//
//
//                            JSONArray array_payout = jsonObject.getJSONArray("payout");
//                            Log.e("array_payout", String.valueOf(array_payout));
//                            for(int i=0;i<array_payout.length();i++){
//                                JSONObject json_obj = array_payout.getJSONObject(i);
//
//                                String NAME = json_obj.getString("NAME");
//                                String pay_fd_DESCRIPTION = json_obj.getString("DESCRIPTION");
//                                String MORE_INFO = json_obj.getString("MORE_INFO");
//                                String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
//
//                                Log.e("Name json_obj_payout ",NAME+",,"+MORE_INFO+",,"+DEFAULT_VALUE);
//
//                                String query2 = "Insert into PAYOUTTYPE_FD (pay_fd_name,pay_fd_DESCRIPTION,pay_fd_more,pay_fd_value) values ('"+ NAME +"','"+ pay_fd_DESCRIPTION +"','"+ MORE_INFO +"','"+ DEFAULT_VALUE +"') ";
//                                sqlHandler.executeQuery(query2);
//                                Log.e("Query",query2);
//
//
//                            }
//
//                            //------------------- PAYOUTTYPE_FD Insert Completed----------------------------
//
//                            JSONArray array_compounding = jsonObject.getJSONArray("compounding");
//                            Log.e("array_compounding", String.valueOf(array_compounding));
//                            for(int i=0;i<array_compounding.length();i++){
//                                JSONObject json_obj_compounding = array_compounding.getJSONObject(i);
//
//                                String NAME = json_obj_compounding.getString("NAME");
//                                String DESCRIPTION = json_obj_compounding.getString("DESCRIPTION");
//                                String MORE_INFO = json_obj_compounding.getString("MORE_INFO");
//                                String DEFAULT_VALUE = json_obj_compounding.getString("DEFAULT_VALUE");
//
//                                Log.e("json_obj_compounding",NAME+",,"+MORE_INFO+",,"+DEFAULT_VALUE);
//
//                                String query3 = "Insert into COMPOUNDING_FD (com_fd_name,com_fd_des,com_fd_more,com_fd_value) values ('"+ NAME +"','"+ DESCRIPTION +"','"+ MORE_INFO +"','"+ DEFAULT_VALUE +"') ";
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




    public void show(){
        String q = "Select * from PAYOUTTYPE_FD";
        Cursor c1 = sqlHandler.selectQuery(q);
        array = new String[c1.getCount()];
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String pay_fd_DESCRIPTION = c1.getString(c1.getColumnIndex("pay_fd_DESCRIPTION"));
                    array[i] = pay_fd_DESCRIPTION;
                    i++;

                }while (c1.moveToNext());
            }
        }
        c1.close();



    }

    public void show2(){
        String q = "Select * from COMPOUNDING_FD";
        Cursor c1 = sqlHandler.selectQuery(q);
        array2 = new String[c1.getCount()];
        more = new String[c1.getCount()];
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String com_fd_des = c1.getString(c1.getColumnIndex("com_fd_des"));
                    array2[i] = com_fd_des;
                    String com_fd_more = c1.getString(c1.getColumnIndex("com_fd_more"));
                    more[i] = com_fd_more;
                    i++;

                }while (c1.moveToNext());
            }
        }
        c1.close();



    }
    public void show3(){
        String q = "Select * from INTEREST where type = 'FD' ";
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

    public void show4(){
        String q = "Select * from taxable_inc";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String Name = c1.getString(c1.getColumnIndex("taxable_inc_NAME"));
                    String Description = c1.getString(c1.getColumnIndex("taxable_inc_DESCRIPTION"));
                    String More = c1.getString(c1.getColumnIndex("taxable_inc_MORE"));
                    Log.e("Name",Name+""+More+"");
                    Array1.add(Name);
                    Array2.add(More);
                    Log.e("ARRAY1",Array1.toString());
                    Log.e("Array111",Array1.get(0));
                    Log.e("ARRAY2",Array2.toString());
                    Log.e("Array222",Array2.get(0));

                }while (c1.moveToNext());

            }

        }
        c1.close();


    }

//    public void show5(){
//        String w = "Select * from profile where pro_email = '"+email+"' ";
//        Cursor c1 = sqlHandler.selectQuery(w);
//        int a = c1.getCount();
//        Log.e("Count",a+"");
//        if (c1.getCount() != 0) {
//            if (c1.moveToFirst()) {
//                do {
//                    pro_tax_age_group = c1.getString(c1.getColumnIndex("pro_tax_age_group"));
//                    pro_age_group = c1.getString(c1.getColumnIndex("pro_age_group"));
//                    Log.e("pro_tax_age_group",pro_tax_age_group+""+pro_age_group+"");
//
//                }while (c1.moveToNext());
//
//            }
//
//        }
//        c1.close();
//
//    }

    public boolean isNetworkAvailable() {
        boolean connect=false;
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(this)
                    .setTitle("Network Unreachable")
                    .setMessage("No internet connection on your device." +
                            " Please connect to the internet.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isNetworkAvailable();
                        }
                    }).show();
            connect=false;
        }else{
            connect= true;
        }
        return connect;
    }

    @Override
    public void onClick(View v) {

        if(isNetworkAvailable()){
            taxable_Income = Integer.parseInt(taxincome.getText().toString());
            int k = Array1.size();
            Log.e("Size",k+"");
            for(int i = 0;i<Array1.size();i++) {
                try {
                    String[] age = Array1.get(i).split("_");
                    Log.e("age_group_tax", Arrays.deepToString(age));
                    min = Integer.parseInt(age[0]);
                    max = Integer.parseInt(age[1]);
                    Log.e("min Value", min + "");
                    Log.e("max Value", max + "");
                }
                catch(NumberFormatException ex){ // handle your exception
                }
                if(taxable_Income >= min && taxable_Income <= max){
                    Log.e("Selected",i+"");
                    taxable_Income_more= Integer.parseInt(Array2.get(i));
                    Log.e("taxable_Income_more", String.valueOf(taxable_Income_more));
                }

            }

            DecimalFormat form = new DecimalFormat("0.00");
            if(amou.length() == 0 ){
                amou.setError("Please Enter the Amount");
            }
            else {
                amount = Double.parseDouble(amou.getText().toString());
            }




            Log.e("AMOUNT ",amount+"");







            if(payout == 1 ){


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

                if(compound == 1){

                    Log.e("SEEKVALUE",seekValue1+"");
                    Log.e("More",more.toString()+"");
                    double  c = Double.parseDouble(more[0]);
                    Log.e("Value of C",c+"");
                    double b = 1+ c*seekValue1/(12*100);
                    Log.e("VAlue of B",b+"");
                    double d = tenture/c;
                    Log.e("Value of d",d+"");

                    double E1 = Math.pow(b, d);
                    a = amount *(E1 -1);
                    if(taxable_Income != 0){
                        taxx();
//                        taxx2();
                    }
//                    tax3 = tax2-tax;
//                    taxonint.setText(form.format(tax3));
//                    intamount.setText(form.format(a) );

                }else if(compound ==2){
                    Log.e("SEEKVALUE",seekValue1+"");
                    Log.e("More",more.toString()+"");
                    double  c = Double.parseDouble(more[1]);
                    Log.e("Value of C",c+"");
                    double b = 1+ c*seekValue1/(12*100);
                    Log.e("VAlue of B",b+"");
                    double d = tenture/c;
                    Log.e("Value of d",d+"");

                    double E1 = Math.pow(b, d);
                    a = amount *(E1 -1);
                    if(taxable_Income != 0){
                        taxx();
//                        taxable_Income2 = taxable_Income+a;
//                        taxx2();
                    }
//                    tax3 = tax2-tax;
//                    taxonint.setText(form.format(tax3));
//                    intamount.setText(form.format(a) );

                }else if(compound == 3){
                    Log.e("SEEKVALUE",seekValue1+"");
                    Log.e("More",more.toString()+"");
                    double  c = Double.parseDouble(more[2]);
                    Log.e("Value of C",c+"");
                    double b = 1+ c*seekValue1/(12*100);
                    Log.e("VAlue of B",b+"");
                    double d = tenture/c;
                    Log.e("Value of d",d+"");

                    double E1 = Math.pow(b, d);
                    a = amount *(E1 -1);
                    if(taxable_Income != 0){
                        taxx();
//                        taxable_Income2 = taxable_Income+a;
//                        taxx2();
                    }
//                intamount.setText(a.toString());

//                    tax3 = tax2-tax;
//                    taxonint.setText(form.format(tax3));
//                    intamount.setText(form.format(a) );

                }else if(compound == 4){
                    Log.e("SEEKVALUE",seekValue1+"");
                    Log.e("More",more.toString()+"");
                    double  c = Double.parseDouble(more[3]);
                    Log.e("Value of C",c+"");
                    double b = 1+ c*seekValue1/(12*100);
                    Log.e("VAlue of B",b+"");
                    double d = tenture/c;
                    Log.e("Value of d",d+"");

                    double E1 = Math.pow(b, d);
                    a = amount *(E1 -1);
                    if(taxable_Income != 0){
                        taxx();
//                        taxable_Income2 = taxable_Income+a;
//                        taxx2();
                    }
//                    tax3 = tax2-tax;
//                    taxonint.setText(form.format(tax3));
//                    intamount.setText(form.format(a) );
                }else if(compound == 5){
                    Log.e("SEEKVALUE",seekValue1+"");
                    Log.e("More",more.toString()+"");
                    double  c = Double.parseDouble(more[4]);
                    Log.e("Value of C",c+"");
                    double b = 1+ c*seekValue1/(12*100);
                    Log.e("VAlue of B",b+"");
                    double d = tenture/c;
                    Log.e("Value of d",d+"");

                    double E1 = Math.pow(b, d);
                    a = amount *(E1 -1);
                    if(taxable_Income != 0){
                        taxx();
//                        taxable_Income2 = taxable_Income+a;
//                        taxx2();
                    }
//                    tax3 = tax2-tax;
//                    taxonint.setText(form.format(tax3));
//                    intamount.setText(form.format(a) );

                }

            }else if(payout == 2){
                a = amount*seekValue1/(4*100);
                if(taxable_Income != 0){
                    taxx();
//                    taxable_Income2 = taxable_Income+a;
//                    taxx2();
                }
//                tax3 = tax2-tax;
//                taxonint.setText(form.format(tax3));
//                intamount.setText(form.format(a) );

            }else if(payout == 3){

                try {
                    a = amount * seekValue1 / (12 * 100);
                    if(taxable_Income != 0) {
                        taxx();
//                    taxable_Income2 = taxable_Income + a;
//                        taxx2();
                        Log.e("Interest Amount ", a + "");
                    }
//                    tax3 = tax2 - tax;

//                    taxonint.setText(form.format(tax3));
//                    intamount.setText(form.format(a));
                }catch (Exception e){
                    e.printStackTrace();

                }

            }





        }else {

        }

    }
    public void taxx() {
        String pattern = taxable_Income_more+""+tax_age_group+""+age_group;
        Log.e("pattern",pattern+"");
       String url = "http://solodiary.com/abis/webservice/calculator/tax.php?pattern="+pattern+"&taxable_income="+taxable_Income+"";
        Log.e("URL",url+"");
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            if(Integer.parseInt(statusCode)==200){
                                tax = jsonObject.getDouble("tax");
                                taxx2(tax);

                            }else {

                            }
//

                        }catch (Exception e){
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","volley error"+error.getMessage());
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }

            }
        }) ;
        requestQueue.add(stringRequest);
    }

    public void taxx2(final double tax) {
        final DecimalFormat form = new DecimalFormat("0.00");
        taxable_Income2 = taxable_Income+a;
        String pattern = taxable_Income_more+""+tax_age_group+""+age_group;
        Log.e("pattern",pattern+"");
        String url = "http://solodiary.com/abis/webservice/calculator/tax.php?pattern="+pattern+"&taxable_income="+taxable_Income2+"";
        Log.e("URL",url+"");
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            if(Integer.parseInt(statusCode)==200){
                                try {
                                    tax2 = jsonObject.getDouble("tax");
                                    intamount.setText(form.format(a));
                                    taxonint.setText(form.format(tax2 - tax));

                                    if (payout == 1) {
                                        if(tenture == 0){
                                            Toast.makeText(getApplicationContext(),"Please Select Tenture Value",Toast.LENGTH_SHORT).show();
                                        }else {
                                            tax3 = (tax2 - tax);
                                            double hh = (amount + a - tax3);
                                            double kk = hh / amount;
                                            double jj = 12 / tenture;
                                            double ll = Math.pow(kk, jj);
                                            eff_ret = (ll - 1) * 100;
                                            ann_eff_return.setText(form.format(eff_ret) + "" + "%");
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else {

                            }
//

                        }catch (Exception e){
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","volley error"+error.getMessage());
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }

            }
        }) ;
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
