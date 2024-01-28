package com.example.sennovations.newabis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sennovations on 21-06-2017.
 */

public class Portfolio extends Activity implements View.OnClickListener {
    ProgressDialog progressDialog;
    SqlHandler sqlHandler;
    String[] array1;
    String[] array2;
    String[] array3;
    String[] arraySpinner;
    String[] arraySpinner2;
    String[] arraySpinner3;
    String[] more1;
    String[] more2;
    String[] more3;
    int agegroupvalue;
    int assetgroupvalue;
    int riskprofilevalue;
    Button submitportfolio;
    int c1 = 0 ,c2 = 0,c3 = 0;
    String age,assate,risk;

    Spinner sp1,sp2,sp3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        sqlHandler = new SqlHandler(this);
        submitportfolio = (Button)findViewById(R.id.submitportfolio);
        submitportfolio.setOnClickListener(this);
//      Portfolio();
        show();
        show2();
        show3();
        Typeface tff = Typeface.createFromAsset(getAssets(), "arial.ttf");
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setTypeface(tff);
        Typeface tt = Typeface.createFromAsset(getAssets(),"helvetica.otf");
        TextView textView1 = (TextView)findViewById(R.id.text1);
        textView1.setTypeface(tt);
        TextView textView2 = (TextView)findViewById(R.id.text2);
        textView2.setTypeface(tt);
        TextView textView3 = (TextView)findViewById(R.id.text3);
        textView3.setTypeface(tt);


        String[] a =new String[]{"Select Type"};
        List<String> ar= new ArrayList<>();
        Collections.addAll(ar,a);
        Collections.addAll(ar,array1);
        String[] finalArray = ar.toArray(new String[ar.size()]);

        this.arraySpinner =  finalArray;

        sp1 = (Spinner) findViewById(R.id.sp1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                agegroupvalue = sp1.getSelectedItemPosition();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        String[] b =new String[]{"Select Type"};
        List<String> ar2= new ArrayList<>();
        Collections.addAll(ar2,b);
        Collections.addAll(ar2,array2);
        String[] finalArray2 = ar2.toArray(new String[ar.size()]);

        this.arraySpinner2 =  finalArray2;

        sp2 = (Spinner) findViewById(R.id.sp2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner2);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                assetgroupvalue = sp2.getSelectedItemPosition();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        String[] c =new String[]{"Select Type"};
        List<String> ar3= new ArrayList<>();
        Collections.addAll(ar3,c);
        Collections.addAll(ar3,array3);
        String[] finalArray3 = ar3.toArray(new String[ar.size()]);

        this.arraySpinner3 =  finalArray3;

        sp3 = (Spinner) findViewById(R.id.sp3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner3);
        sp3.setAdapter(adapter3);

        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                riskprofilevalue = sp3.getSelectedItemPosition();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });



    }


//    private void Portfolio() {
//        String url = "http://solodiary.com/abis/webservice/thumbrule_portfolio/show_from_database.php";
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
//                            {
//                                JSONArray age_group = jsonObject.getJSONArray("age_group");
//                                Log.e("age_group", String.valueOf(age_group));
//                                for (int i = 0; i < age_group.length(); i++) {
//                                    JSONObject json_obj = age_group.getJSONObject(i);
//
//                                    String NAME = json_obj.getString("NAME");
//                                    String DESCRIPTION = json_obj.getString("DESCRIPTION");
//                                    String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
//                                    String MORE_INFO = json_obj.getString("MORE_INFO");
//
//                                    Log.e("json_obj", NAME + ",," + DESCRIPTION + ",," + DEFAULT_VALUE + ",," + MORE_INFO);
//
//                                    String Query = "Insert into age_group (age_group_NAME,age_group_DESCRIPTION,age_group_MORE,age_group_VALUE) Values ('" + NAME + "','" + DESCRIPTION + "','" + MORE_INFO + "','" + DEFAULT_VALUE + "')";
//                                    sqlHandler.executeQuery(Query);
//                                    Log.e("Query", Query + "");
//                                }
//                            }
//                                {
//
//                                    JSONArray asset_group = jsonObject.getJSONArray("asset_group");
//                                    Log.e("asset_group", String.valueOf(asset_group));
//                                    for (int i = 0; i < asset_group.length(); i++) {
//                                        JSONObject json_obj = asset_group.getJSONObject(i);
//
//                                        String NAME = json_obj.getString("NAME");
//                                        String DESCRIPTION = json_obj.getString("DESCRIPTION");
//                                        String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
//                                        String MORE_INFO = json_obj.getString("MORE_INFO");
//
//                                        Log.e("json_obj", NAME + ",," + DESCRIPTION + ",," + DEFAULT_VALUE + ",," + MORE_INFO);
//
//                                        String Query = "Insert into asset_group (asset_group_NAME,asset_group_DESCRIPTION,asset_group_MORE,asset_group_VALUE) Values ('" + NAME + "','" + DESCRIPTION + "','" + MORE_INFO + "','" + DEFAULT_VALUE + "')";
//                                        sqlHandler.executeQuery(Query);
//                                        Log.e("Query", Query + "");
//                                    }
//                                }
//                            {
//
//                                JSONArray riskprofile = jsonObject.getJSONArray("return");
//                                Log.e("return", String.valueOf(riskprofile));
//                                for (int i = 0; i <riskprofile.length(); i++) {
//                                    JSONObject json_obj = riskprofile.getJSONObject(i);
//
//                                    String CATEGORY = json_obj.getString("CATEGORY");
//                                    String DESCRIPTION = json_obj.getString("DESCRIPTION");
//                                    String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
//                                    String MORE_INFO = json_obj.getString("MORE_INFO");
//                                    String MIN_RANGE = json_obj.getString("MIN_RANGE");
//                                    String MAX_RANGE = json_obj.getString("MAX_RANGE");
//
//                                    Log.e("json_obj", CATEGORY + ",," + DESCRIPTION + ",," + DEFAULT_VALUE + ",," + MORE_INFO +", ,"+ MIN_RANGE+",,"+MAX_RANGE);
//
//                                    String Query = "Insert into riskprofile (riskprofile_CATEGORY,riskprofile_DESCRIPTION,riskprofile_MORE,riskprofile_VALUE,riskprofile_MIN_RANGE,riskprofile_MAX_RANGE) Values ('" + CATEGORY + "','" + DESCRIPTION + "','" + MORE_INFO + "','" + DEFAULT_VALUE + "','" + MIN_RANGE + "','" + MAX_RANGE + "')";
//                                    sqlHandler.executeQuery(Query);
//                                    Log.e("Query", Query + "");
//                                }
//                            }
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
//

    public void show() {
        String q = "Select * from age_group";
        Cursor c1 = sqlHandler.selectQuery(q);
        array1 = new String[c1.getCount()];
        more1 = new String[c1.getCount()];
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String age_group_DESCRIPTION = c1.getString(c1.getColumnIndex("age_group_DESCRIPTION"));
                    array1[i] = age_group_DESCRIPTION;
                    String age_group_MORE = c1.getString(c1.getColumnIndex("age_group_MORE"));
                    more1[i] = age_group_MORE;
                    i++;

                } while (c1.moveToNext());
            }
        }
        c1.close();
    }
    public void show2() {
        String q = "Select * from asset_group";
        Cursor c1 = sqlHandler.selectQuery(q);
        array2 = new String[c1.getCount()];
        more2 = new String[c1.getCount()];
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String asset_group_DESCRIPTION = c1.getString(c1.getColumnIndex("asset_group_DESCRIPTION"));
                    array2[i] = asset_group_DESCRIPTION;
                    String asset_group_MORE = c1.getString(c1.getColumnIndex("asset_group_MORE"));
                    more2[i] = asset_group_MORE;
                    i++;

                } while (c1.moveToNext());
            }
        }
        c1.close();
    }
    public void show3() {
        String q = "Select * from riskprofile";
        Cursor c1 = sqlHandler.selectQuery(q);
        array3 = new String[c1.getCount()];
        more3 = new String[c1.getCount()];
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    String riskprofile_DESCRIPTION = c1.getString(c1.getColumnIndex("riskprofile_DESCRIPTION"));
                    array3[i] = riskprofile_DESCRIPTION;
                    String riskprofile_MORE = c1.getString(c1.getColumnIndex("riskprofile_MORE"));
                    more3[i] = riskprofile_MORE;
                    i++;

                } while (c1.moveToNext());
            }
        }
        c1.close();
    }


    @Override
    public void onClick(View v) {

        if(agegroupvalue == 1) {

            c1 = Integer.parseInt(more1[0]);
            age = array1[0];
            Log.e("agePartten",c1+""+age+"");
        }else if(agegroupvalue ==2) {
            c1 = Integer.parseInt(more1[1]);
            age = array1[1];
            Log.e("agePartten",c1+""+age+"");
        }else if(agegroupvalue == 3) {
            c1 = Integer.parseInt(more1[2]);
            age = array1[2];
            Log.e("agePartten",c1+""+age+"");
        }

        if(assetgroupvalue == 1) {
            c2 = Integer.parseInt(more2[0]);
            assate = array2[0];
            Log.e("assetgroupvalue",c2+""+assate+"");
        }else if(assetgroupvalue ==2) {
            c2 = Integer.parseInt(more2[1]);
            assate = array2[1];
            Log.e("assetgroupvalue",c2+""+assate+"");
        }else if(assetgroupvalue == 3) {
            c2 = Integer.parseInt(more2[2]);
            assate = array2[2];
            Log.e("assetgroupvalue",c2+""+assate+"");
        }

        if(riskprofilevalue == 1) {
            c3 = Integer.parseInt(more3[0]);
            risk = array3[0];
            Log.e("riskprofilevalue",c3+""+risk+"");
        }else if(riskprofilevalue ==2) {
            c3 = Integer.parseInt(more3[1]);
            risk = array3[1];
            Log.e("riskprofilevalue",c3+""+risk+"");
        }else if(riskprofilevalue == 3) {
            c3 = Integer.parseInt(more3[2]);
            risk = array3[2];
            Log.e("riskprofilevalue",c3+""+risk+"");
        }


        if(c1 != 0 && c2 !=0 && c3 !=0) {
            Intent intent = new Intent(Portfolio.this, PiechartActivity.class);
            int value = Integer.valueOf(String.valueOf(c1) + String.valueOf(c2)+ String.valueOf(c3));
            intent.putExtra("value",value);
            intent.putExtra("age",age);
            intent.putExtra("assate",assate);
            intent.putExtra("risk",risk);
            Log.e("Values",value+"");
            startActivity(intent);
        }else {

            Toast.makeText(getApplicationContext(),"Please Select All Values",Toast.LENGTH_LONG).show();

        }





    }
}
