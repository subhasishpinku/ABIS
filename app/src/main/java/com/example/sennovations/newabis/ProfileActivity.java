package com.example.sennovations.newabis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sennovations on 29-06-2017.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    SqlHandler sqlHandler;
    String responcecode;
    String mobile,API_KEY,pin_p,state_p,country_p,city_p,email;
    Button skip;
    String NAME,NAME1,NAME2;
    String DESCRIPTION ;
    EditText selectage,pin,state,country,phone,city;
    int ageselect;
    String MORE_INFO,MORE_INFO1,MORE_INFO2;
    String age_group_tax;
    String age_group;
    Button save;
    ArrayList<String> Array1 = new ArrayList<String>();
    ArrayList<String> Array2 = new ArrayList<String>();
    ArrayList<String> Array3 = new ArrayList<String>();
    ArrayList<String> Array4 = new ArrayList<String>();
    int min_age,max_age;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String gender,fullname;
    ProgressDialog mProgress;
    String tell_no="";
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 999;
    EditText dob;
    String dateofbirth;
    LinearLayout linearLayout;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sqlHandler = new SqlHandler(this);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        Intent intent = getIntent();
        fullname = intent.getStringExtra("fullname");
        mobile = intent.getStringExtra("mobile");
        API_KEY = intent.getStringExtra("API_KEY");
        email  = intent.getStringExtra("email");
        Log.e("API_KEY",API_KEY+""+mobile+""+email+""+fullname+"");
        phone = (EditText)findViewById(R.id.mobile);
        phone.setText(mobile);
        selectage = (EditText)findViewById(R.id.age);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        country = (EditText)findViewById(R.id.country);
        pin = (EditText)findViewById(R.id.pin);
        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(this);
        TextView textView = (TextView)findViewById(R.id.textname);
        textView.setText(fullname);
        agegroup();
        dob = (EditText)findViewById(R.id.dob);
        linearLayout = (LinearLayout)findViewById(R.id.ll);
        linearLayout.setOnClickListener(this);
        dob.setEnabled(false);


    }

    private void agegroup() {

        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.age_group,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);

                            String age_group  =jsonObject.getString("age_group");
                            JSONArray jsonArray = new JSONArray(age_group);
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                NAME = jsonObject1.getString("NAME");
                                String DESCRIPTION = jsonObject1.getString("DESCRIPTION");
                                MORE_INFO = jsonObject1.getString("MORE_INFO");
                                String DEFAULT_VALUE = jsonObject1.getString("DEFAULT_VALUE");
                                Log.e("age_group", jsonArray.toString() + ""+NAME+""+DESCRIPTION);




//                                age = NAME.split("_");
//                                String min_age = age[0];
//                                String max_age = age[1];
//                                Log.e("Age Value",min_age+""+max_age+"");

//                                x = new String[][] {
//                                        new String[] { min_age, max_age },
//                                        new String[] { MORE_INFO }
//                                };

                                Array1.add(NAME);
                                Array2.add(MORE_INFO);
                            }
                            Log.e("ARRAY1",Array1.toString());
                            Log.e("Array111",Array1.get(0));
                            Log.e("ARRAY2",Array2.toString());
                            Log.e("Array222",Array2.get(0));


                            String age_group_tax  =jsonObject.getString("age_group_tax");
                            JSONArray jsonArray1 = new JSONArray(age_group_tax);
                            for(int j=0;j<jsonArray1.length();j++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                NAME1 = jsonObject2.getString("NAME");
                                String DESCRIPTION1 = jsonObject2.getString("DESCRIPTION");
                                MORE_INFO1 = jsonObject2.getString("MORE_INFO");
                                String DEFAULT_VALUE1 = jsonObject2.getString("DEFAULT_VALUE");
                                Log.e("age_group_tax", jsonArray1.toString() + ""+NAME1+""+DESCRIPTION1);
                                Array3.add(NAME1);
                                Array4.add(MORE_INFO1);
                            }
                            Log.e("ARRAY3",Array3.toString());
                            Log.e("Array333",Array3.get(0));
                            Log.e("ARRAY4",Array4.toString());
                            Log.e("Array444",Array4.get(0));

                            String gender = jsonObject.getString("gender");
                            JSONArray jsonArray2 = new JSONArray(gender);
                            for(int l = 0; l<jsonArray2.length();l++){
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(l);
                                DESCRIPTION = jsonObject3.getString("DESCRIPTION");
                                NAME2 = jsonObject3.getString("NAME");
                                MORE_INFO2 = jsonObject3.getString("MORE_INFO");
                                list.add(NAME2);
                                list1.add(MORE_INFO2);
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
        }) ;
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.ll:

                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                }
                            }, year, month, day);
                    datePickerDialog.show();

                break;

            case R.id.save:
                dateofbirth = dob.getText().toString();
                country_p = country.getText().toString();
                city_p = city.getText().toString();
                state_p = state.getText().toString();
                pin_p = pin.getText().toString();
                ageselect = Integer.parseInt(selectage.getText().toString());
                if(mobile.equals("0")){
                    mobile = phone.getText().toString();
                    Log.e("Mobile",mobile+"");

                }else {
                    mobile = phone.getText().toString();
//                    phone.setText(mobile);
//                    phone.addTextChangedListener(new TextWatcher() {
//
//                        @Override
//                        public void afterTextChanged(Editable s) {}
//
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start,
//                                                      int count, int after) {
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start,
//                                                  int before, int count) {
//                            if(s.length() != 0)
//                                phone.setText("");
//                        }
//                    });
                }
                Log.e("Select age ",ageselect+"");
                                int j = Array1.size();
                                Log.e("Size",j+"");
                for(int i = 0;i<Array1.size();i++) {
                    String[] age = Array1.get(i).split("_");
                    Log.e("Array1", Arrays.deepToString(age));
                    min_age = Integer.parseInt(age[0]);
                    max_age = Integer.parseInt(age[1]);
                    Log.e("min Age Value", min_age + "");
                    Log.e("max Age Value", max_age + "");
                    if(ageselect >= min_age && ageselect <= max_age){
                        Log.e("Selected",i+"");
                        age_group= Array2.get(i);
                        Log.e("age_group",age_group);

                    }
                }

                int k = Array3.size();
                Log.e("Size",k+"");
                for(int i = 0;i<Array3.size();i++) {
                    String[] age = Array3.get(i).split("_");
                    Log.e("age_group_tax", Arrays.deepToString(age));
                    min_age = Integer.parseInt(age[0]);
                    max_age = Integer.parseInt(age[1]);
                    Log.e("min Age Value", min_age + "");
                    Log.e("max Age Value", max_age + "");
                    if(ageselect >= min_age && ageselect <= max_age){
                        Log.e("Selected",i+"");
                        age_group_tax= Array4.get(i);
                        Log.e("age_group_tax",age_group_tax);

                    }
                }
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                Log.e("ID",selectedId+"");
                radioSexButton = (RadioButton) findViewById(selectedId);
                if(radioSexButton.getText().equals("Male")){
                    gender = "1";
                    Log.e("MOre",gender+"");
                }else {
                    gender  = "2";
                    Log.e("MOre",gender+"");
                }
                try {
                    datapost();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                break;

        }
    }


    private void datapost(){
        StringRequest stringRequest;
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);


        stringRequest=new StringRequest(Request.Method.POST, Constants.create_profile,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            Log.e("MSG",msg+"");
                            if (Integer.parseInt(statusCode)==200){
                                String Query = "Insert into profile (pro_email,pro_age,pro_age_group,pro_tax_age_group,pro_moblie,pro_state,pro_city,pro_country,pro_pin) Values ('" + email + "','" + ageselect + "','" + age_group + "','" + age_group_tax + "','" + mobile + "','" + state_p + "','" + city_p + "','" + country_p + "','" + pin_p + "')";
                                sqlHandler.executeQuery(Query);
                                Log.e("Query", Query + "");

                                String query1 = "Select * from age_pattern";
                                Cursor c1 = sqlHandler.selectQuery(query1);
                                int b =c1.getCount();
                                Log.e("COUNT",b+"");
                                if(c1.getCount()==0){
                                    String qq = "Insert into age_pattern(age_pattern_email,age_pattern_api,age_pattern_age_group,age_pattern_tax_age_group) values ('"+email+"','"+API_KEY+"','"+age_group+"','"+age_group_tax+"')";
                                    sqlHandler.executeQuery(qq);
                                    Log.e("qq",qq+"");

                                }else {
//
                                    String query22 = "Update age_pattern set age_pattern_api = '" + API_KEY + "' , age_pattern_email = '"+email+"', age_pattern_age_group = '"+age_group+"', age_pattern_tax_age_group = '"+age_group_tax+"' where age_pattern_id =  "+1+" ";
                                    sqlHandler.executeQuery(query22);
                                    Log.e("query22",query22+"");
//
                                }
                                Intent intent = new Intent(getApplicationContext(),OTP.class);
                                intent.putExtra("mobile",mobile);
                                intent.putExtra("API_KEY",API_KEY);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ProfileActivity.this);
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
                    params.put("api_key", API_KEY);
                    params.put("age", String.valueOf(ageselect));
                    params.put("tax_age_group", age_group_tax);
                    params.put("age_group",age_group);
                    params.put("state",state_p);
                    params.put("country",country_p);
                    params.put("pin",pin_p);
                    params.put("city",city_p);
                    params.put("mobile",mobile);
                    params.put("gender",gender);
                    params.put("dob",dateofbirth);
                    Log.e("Details",API_KEY+"_"+ageselect+"_"+age_group_tax+"_"+age_group+"_"+state_p+"_"+country_p+"_"+pin_p+"_"+city_p+"_"+mobile+"_"+gender+"_"+dateofbirth);
                return params;
            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        requestQueue.add(stringRequest);
    }


}

