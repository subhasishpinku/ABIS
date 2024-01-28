package com.example.sennovations.newabis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sennovations on 04-07-2017.
 */

public class OTP extends AppCompatActivity implements View.OnClickListener {
    String mobile,API_KEY,email;
    EditText otp;
    Button submit;
    String  code;
    TextView number,resend,change;
    ProgressDialog  progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Intent  intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
        API_KEY = intent.getStringExtra("API_KEY");
        Log.e("mobile",mobile+""+API_KEY+"");
        otp = (EditText)findViewById(R.id.otp);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        resend = (TextView)findViewById(R.id.resend);
        change = (TextView)findViewById(R.id.change);
        String ch = "Change Moblie Number";
        SpannableString spanString1 = new SpannableString(ch);
        spanString1.setSpan(new UnderlineSpan(), 0, spanString1.length(), 0);
        change.setText(spanString1);

        String tempString="If not received the OTP. Please Click here to resend";
        SpannableString spanString = new SpannableString(tempString);
        spanString.setSpan(new UnderlineSpan(), 25, spanString.length(), 0);
        resend.setText(spanString);
        number.setText(mobile);
        resend.setOnClickListener(this);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                code = otp.getText().toString();
                if(isNetworkAvailable()){
                    OTPVERIFY();

                }else {

                }

                break;
            case R.id.resend:
                if(isNetworkAvailable()){
                    resendotp();
                }
                else {

                }

                break;
            case R.id.change:
                Toast.makeText(getApplicationContext(),"Fetures will comming soon",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void resendotp(){
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.resend_otp,
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
                                    builder = new AlertDialog.Builder(OTP.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(OTP.this);
                                }
                                builder.setTitle("Verify")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

//                                                Intent  intent = new Intent(OTP.this,OTP.class);
//                                                intent.putExtra("email", email);
//                                                startActivity(intent);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(OTP.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(OTP.this);
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
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }




    public void OTPVERIFY(){
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.otp,
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
                                    builder = new AlertDialog.Builder(OTP.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(OTP.this);
                                }
                                builder.setTitle("Verify")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent  intent = new Intent(OTP.this,Subscription.class);
                                                intent.putExtra("email", email);
                                                startActivity(intent);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(OTP.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(OTP.this);
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
                params.put("otp",code);



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
