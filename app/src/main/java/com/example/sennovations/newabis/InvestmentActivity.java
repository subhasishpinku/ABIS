package com.example.sennovations.newabis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
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

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sennovations on 11-07-2017.
 */

public class InvestmentActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    ProgressDialog progressDialog;
    ArrayList<String> Array1 = new ArrayList<String>();
    ArrayList<String> Array2 = new ArrayList<String>();
    ArrayList<String> Array3 = new ArrayList<String>();
    ArrayList<String> Array4 = new ArrayList<String>();
    ArrayList<String> Array5 = new ArrayList<String>();
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
    CheckBox checkBox5,checkBox6,checkBox7,checkBox8;
    Spinner ex_an_return;
    private String[] arraySpinner2;
    String array[];
    int value;
    String content;
    Button submitportfolio;
    StringBuilder result=new StringBuilder();
    EditText invamount;
    double investment_amount = 0.0;
    SeekBar seekBar;
    double seekValue1;
    TextView seekbarvalue;
    String expected_return;
    SqlHandler sqlHandler;
    String API_KEY = "";
    int min_return = 0;
    int max_return = 0;
    String asset_class = "";
    ArrayList<String> assetarray;
    ArrayList<Integer> position = new ArrayList<>();
    boolean ii = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        Intent intent = getIntent();
        assetarray = intent.getStringArrayListExtra("assetarray");
        Log.e("assetarray",assetarray+"");


        checkBox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkbox3);
        checkBox4 = (CheckBox)findViewById(R.id.checkbox4);
        checkBox5 = (CheckBox)findViewById(R.id.checkbox5);
        checkBox6 = (CheckBox)findViewById(R.id.checkbox6);
        checkBox7 = (CheckBox)findViewById(R.id.checkbox7);
        checkBox8 = (CheckBox)findViewById(R.id.checkbox8);
        ex_an_return =(Spinner)findViewById(R.id.ex_an_return);
        submitportfolio = (Button)findViewById(R.id.submitportfolio);
        invamount = (EditText)findViewById(R.id.invamount);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekbarvalue = (TextView)findViewById(R.id.seekbarvalue);
        seekBar.setMax(100);
        seekBar.setProgress(1);
        view_investment();
        submitportfolio.setOnClickListener(this);
        sqlHandler = new SqlHandler(this);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekValue1 = Double.valueOf(seekBar.getProgress()).doubleValue();
        seekbarvalue.setText(String .format("%.02f", seekValue1) + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    private void view_investment() {
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.view_investment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            JSONArray asset = jsonObject.getJSONArray("asset");
                            Log.e("asset", String.valueOf(asset));
                            for(int i=0;i<asset.length();i++) {
                                JSONObject json_obj = asset.getJSONObject(i);

                                String NAME = json_obj.getString("NAME");
                                String DESCRIPTION = json_obj.getString("DESCRIPTION");
                                Array1.add(DESCRIPTION);
                                Array2.add(NAME);
                                Log.e("Details",NAME+""+DESCRIPTION+"");
                            }

                            Log.e("Status Code",statusCode+"");

                            JSONArray specs = jsonObject.getJSONArray("specs");
                            for(int j =0;j<specs.length();j++ ){
                                JSONObject jsonObject1 = specs.getJSONObject(j);
                                String DESCRIPTION2 = jsonObject1.getString("DESCRIPTION");
                                String MIN_RANGE = jsonObject1.getString("MIN_RANGE");
                                String MAX_RANGE = jsonObject1.getString("MAX_RANGE");
                                String MORE_INFO = jsonObject1.getString("MORE_INFO");
                                Array3.add(DESCRIPTION2);
                                Array4.add(MIN_RANGE);
                                Array5.add(MAX_RANGE);
                                Log.e("Details2",DESCRIPTION2);

                            }
                            check();

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

    public void check(){
        for(int i = 0;i<Array1.size();i++) {
            try {
                checkBox1.setText(Array1.get(0));
                checkBox1.setEnabled(false);
                checkBox2.setText(Array1.get(1));
                checkBox2.setEnabled(false);
                checkBox3.setText(Array1.get(2));
                checkBox3.setEnabled(false);
                checkBox4.setText(Array1.get(3));
                checkBox4.setEnabled(false);
                checkBox5.setText(Array1.get(4));
                checkBox5.setEnabled(false);
                checkBox6.setText(Array1.get(5));
                checkBox6.setEnabled(false);
                checkBox7.setText(Array1.get(6));
                checkBox7.setEnabled(false);
                checkBox8.setText(Array1.get(7));
                checkBox8.setEnabled(false);

            } catch (NumberFormatException ex) {
                // handle your exception
            }
        }


        String[] a =new String[]{"Select Type"};
        ArrayList<String> array3 = new ArrayList<>();
        Collections.addAll(array3,a);

        for(int k= 0; k<Array3.size();k++){
            Collections.addAll(array3, Array3.get(k).concat(" ").concat(Array4.get(k)).concat("-").concat(Array5.get(k)).concat("%"));
        }
//        String[] b =new String[]{"Custom"};
//        Collections.addAll(array3,b);
        String[] finalArray2 = array3.toArray(new String[array3.size()]);

        this.arraySpinner2 =  finalArray2;
        Log.e("finalArray2",finalArray2.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner2);
        ex_an_return.setAdapter(adapter);

        ex_an_return.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                value = ex_an_return.getSelectedItemPosition();
                Log.e("Position",value+"");
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
                content = ex_an_return.getSelectedItem().toString();

                Log.e("Spinner value",content+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        selecttype();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitportfolio:

                if(isNetworkAvailable()) {
                String query = "Select * from API_KEY";
                Cursor c = sqlHandler.selectQuery(query);
                if (c != null && c.getCount() != 0) {
                    if (c.moveToFirst()) {
                        do {
                            API_KEY = c.getString(c.getColumnIndex("api_key"));

                        } while (c.moveToNext());
                    }
                }
                c.close();

                DecimalFormat form = new DecimalFormat("0.00");
                if (invamount.length() == 0) {
                    invamount.setError("Please Enter the Amount");
                } else {
                    investment_amount = Double.parseDouble(invamount.getText().toString());
                }
                String yy = "";
                if (content.equals("Select Type")) {
                    yy = "";
                } else {
                    yy = content;

                    min_return = Integer.parseInt(Array4.get(value - 1));
                    max_return = Integer.parseInt(Array5.get(value - 1));
                    expected_return = Array3.get(value - 1);
                    Log.e("Content", yy + "-" + min_return + "-" + max_return + "-" + expected_return + "");

                }
//                Toast.makeText(getApplication(), "New Features are coming soon", Toast.LENGTH_SHORT).show();

                ArrayList<String> list = new ArrayList<>();
                if (checkBox1.isChecked()) {
//                    text = text  + Array2.get(0);
                    list.add(Array2.get(0));
//                    Log.e("Select Value",text+"");
                }
                if (checkBox2.isChecked()) {

//                    text = text + Array2.get(1);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(1));
                }
                if (checkBox3.isChecked()) {
//                    text = text  + Array2.get(2);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(2));
                }
                if (checkBox4.isChecked()) {
//                    text = text  + Array2.get(3);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(3));
                }
                if (checkBox5.isChecked()) {
//                    text = text  + Array2.get(4);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(4));
                }
                if (checkBox6.isChecked()) {
//                    text = text + Array2.get(5);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(5));
                }
                if (checkBox7.isChecked()) {
//                    text = text  + Array2.get(6);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(6));

                }
                if (checkBox8.isChecked()) {
//                    text = text  + Array2.get(7);
//                    Log.e("Select Value",text+"");
                    list.add(Array2.get(7));
                }

                result.append("\nTotal: " + list + "");
                asset_class = TextUtils.join(", ", list);
                Log.e("asset_class", asset_class + "");
                Log.e("Text", result.toString() + "");


                    createprofile();

            }else {

                }
                break;
        }
    }



    public void selecttype() {

        for (int z = 0; z < Array2.size(); z++) {
            for (int j = 0; j < assetarray.size(); j++) {

                if (Array2.get(z).equals(assetarray.get(j))) {
                    assetarray.get(j);
                    position.add(z);
                    Log.e("z", z + "");
                    Log.e("array 2 ", assetarray.toString() + "--" + assetarray.get(j) + "--" + z);
                    Log.e("ZZZZ",z+"");
                    if (z == 0) {
                        checkBox1.setEnabled(true);
                    }
                    if (z == 1) {
                        checkBox2.setEnabled(true);
                    }
                    if (z == 2) {
                        checkBox3.setEnabled(true);
                    }
                    if (z == 3) {
                        checkBox4.setEnabled(true);
                    }
                    if (z == 4) {
                        checkBox5.setEnabled(true);
                    }
                    if (z == 5) {
                        checkBox6.setEnabled(true);
                    }
                    if (z == 6) {
                        checkBox7.setEnabled(true);
                    }
                    if (z == 7) {
                        checkBox8.setEnabled(true);
                    }

                    break;
                }


            }
        }

        Log.e("position", position + "");


    };

    public void createprofile(){
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.create_investment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            Log.e("Response obj",jsonObject+"");

                            if (Integer.parseInt(statusCode)==200) {

                                final AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(InvestmentActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(InvestmentActivity.this);
                                }
                                builder.setTitle("Created")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(InvestmentActivity.this,CalculatorActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(InvestmentActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(InvestmentActivity.this);
                                }
                                builder.setTitle("Sorry !")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }

                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }

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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key",API_KEY );
                params.put("asset_class",asset_class);
                params.put("investment_amount", String.valueOf(investment_amount));
                params.put("expected_return",expected_return);
                params.put("max_money_locked", String.valueOf(seekValue1));
                params.put("min_return", String.valueOf(min_return));
                params.put("max_return", String.valueOf(max_return));



                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


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

}
