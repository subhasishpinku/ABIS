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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {
    Button submit,proccede;
    LinearLayout ll1,ll2;
    EditText email,code,newpass,renewpass;
    String  uniquecode,password,repassword;
    ProgressDialog  progressDialog;
    TextView emailid;
    String Md5password;
    String email_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ll1 = (LinearLayout)findViewById(R.id.ll1);
        ll2 = (LinearLayout)findViewById(R.id.ll2);
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);emailid = (TextView)findViewById(R.id.emailid);
        code = (EditText)findViewById(R.id.code);
        newpass = (EditText)findViewById(R.id.newpass);
        renewpass = (EditText)findViewById(R.id.renewpass);
        email = (EditText)findViewById(R.id.email);

        submit  = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        proccede = (Button)findViewById(R.id.procede);
        proccede.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submit:
                email_id = email.getText().toString();
                Log.e("Email",email.getText().toString()+"");
                if(email_id.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter your email id",Toast.LENGTH_SHORT).show();

                }else {

                    if (isNetworkAvailable()) {
                        post();
                    } else {

                    }
                }
                break;

            case R.id.procede:
                uniquecode = code.getText().toString();
                email_id = email.getText().toString();
                password = newpass.getText().toString();
                md5(password);
                repassword = renewpass.getText().toString();
                Log.e("Details",uniquecode+""+email_id+""+password+""+repassword+"");

                if(isNetworkAvailable()){
                    update();
                }else {

                }
                break;


        }
    }

    public void post(){
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.forgot_pass,
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
                                ll2.setVisibility(View.VISIBLE);
                                ll1.setVisibility(View.GONE);
                                emailid.setText(email_id);

                                final AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(ForgotActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ForgotActivity.this);
                                }
                                builder.setTitle("")
                                        .setMessage("Please enter the unique code send to your email id ")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }else {
                                ll2.setVisibility(View.GONE);
                                ll1.setVisibility(View.VISIBLE);
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(ForgotActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ForgotActivity.this);
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

                params.put("email",email_id );

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void update(){
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.update_pass,
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
                                    builder = new AlertDialog.Builder(ForgotActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ForgotActivity.this);
                                }
                                builder.setTitle("Change Your Password")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent  intent = new Intent(ForgotActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }else {
                                ll2.setVisibility(View.GONE);
                                ll1.setVisibility(View.VISIBLE);
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(ForgotActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ForgotActivity.this);
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

                params.put("email",email_id );
                params.put("pass",Md5password );
                params.put("code",uniquecode );



                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private  String md5(String password) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Md5password = hexString.toString();
//            Log.e("Password",hexString.toString()+"");
            return hexString.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
