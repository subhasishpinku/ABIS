package com.example.sennovations.newabis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sennovations on 02-06-2017.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    String email,password,repassword,name,moblie;
    Button Reg;
    EditText emailid,fullname,pass,repass,mob;
    String Md5password;
    private ProgressDialog progressDialog;
    SqlHandler sqlHandler;
    String statusCode;
    TextView link_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);
        link_to_login = (TextView)findViewById(R.id.link_to_login);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"helvetica.otf");
        link_to_login.setTypeface(typeface);

        String tempString="Already has account ! Login here";
        SpannableString spanString = new SpannableString(tempString);
        spanString.setSpan(new UnderlineSpan(), 22, spanString.length(), 0);
        link_to_login.setText(spanString);
        emailid = (EditText)findViewById(R.id.email);
        fullname = (EditText)findViewById(R.id.name);
        pass = (EditText)findViewById(R.id.password);
        repass= (EditText)findViewById(R.id.repassword);
        mob = (EditText)findViewById(R.id.mobile);
        sqlHandler = new SqlHandler(this);



        Reg = (Button)findViewById(R.id.register);

        if(isNetworkAvailable()){
            Reg.setOnClickListener(this);
            link_to_login.setOnClickListener(this);
        }else {

        }


    }

    public boolean validateEmail(String email) {

        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
    public static boolean isValidPhone(String moblie)
    {
        CharSequence inputString = moblie;
        Pattern pattern =  Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
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
                            finish();
//                            dialogInterface.dismiss();
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


        switch(v.getId()){
            case R.id.register:
                email = emailid.getText().toString().trim();
                name = fullname.getText().toString();
                password = pass.getText().toString();
                repassword= repass.getText().toString();
                moblie = mob.getText().toString();
                if ((!validateEmail(email)) || name.length() == 0 || password.equals("") || repassword.equals("") || !repassword.equalsIgnoreCase(password) || !isValidPhone(moblie)) {
                    if (!validateEmail(email)) {
                        emailid.setFocusable(true);
                        emailid.setError("Invalid email");
                        emailid.requestFocus();
                    }

                    if (isValidPhone(moblie)) {

//                        Toast.makeText(v.getContext(), "Phone number is valid", Toast.LENGTH_LONG).show();
                    } else {
                        mob.setFocusable(true);
                        mob.setError("Invalid Phone");
                        mob.requestFocus();
//                        Toast.makeText(v.getContext(), "Phone number is invalid", Toast.LENGTH_LONG).show();
                    }

                    if (name.length() == 0) {
                        fullname.setFocusable(true);
                        fullname.setError("Invalid Name");
                        fullname.requestFocus();

                    }
                    if (password.equals("")) {
                        pass.setFocusable(true);
                        pass.setError("Invalid Password");
                        pass.requestFocus();
                        Toast.makeText(getApplicationContext(), "Enter a Valid Password", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!repassword.equalsIgnoreCase(password)) {
                        repass.setFocusable(true);
                        repass.setError("Invalid Re_Password");
                        repass.requestFocus();
                        Toast.makeText(getApplicationContext(), "Password Does Not Matches", Toast.LENGTH_LONG).show();
                        return;
                    }

                }else {
                    Log.e("validation Dome","here");
                        md5(password);

                }
                Log.e("Welcome","Welcome"+"");
                Log.e("ABIS all",email+"");
                Log.e("ABIS all",name+"");
                Log.e("ABIS all",password+"");
                Log.e("ABIS all",repassword+"");
                Log.e("ABIS all",moblie+"");
                Log.e("Password MD5",Md5password+"");
                int type =1;

                jsonPost(email,name,Md5password,moblie,type);
                break;

            case R.id.link_to_login:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);



        }

    }

    private void jsonPost(final String email, final String name, final String Md5password, final String moblie, final int type) {
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.reg_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            JSONObject jsonObject=new JSONObject(response);
                            statusCode=jsonObject.getString("status");
                            Log.e("StatusCode",statusCode+"");
                            String msg = jsonObject.getString("msg");
//                            String TEMP_API_KEY = jsonObject.getString("TEMP_API_KEY");
//                            String otp =jsonObject.getString("otp");
//
                            Log.e("ABIS ALl Details",statusCode + "" + "" +msg+"");

                            if (Integer.parseInt(statusCode)==200){
                                String query = "Insert into LOGIN(email,name,password,mobile) values ('"+ email +"','"+ name +"','"+ password +"','"+ moblie +"') ";
                                sqlHandler.executeQuery(query);
                                Log.e("Query",query);
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(RegistrationActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(RegistrationActivity.this);
                                }
                                builder.setTitle("Congratulations")
                                        .setMessage("Thank For Registering With Us.An activation link has been sent to your email. Please click on the link to activate your account and then proceed to Login.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent  intent = new Intent(RegistrationActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
//                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                // do nothing
//                                            }
//                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(RegistrationActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(RegistrationActivity.this);
                                }
                                builder.setTitle("Sorry !")
                                        .setMessage(msg + " Please Login to continue .")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent  intent = new Intent(RegistrationActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
//                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                // do nothing
//                                            }
//                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//                                new AlertDialog.Builder(RegistrationActivity.this)
//                                        .setMessage(jsonObject.getString("msg"))
//                                        .setCancelable(true)
//                                        .setPositiveButton("OK",null)
//                                        .show();

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
                params.put("email", RegistrationActivity.this.email);
                params.put("name", RegistrationActivity.this.name);
                if (type == 1) {
                    params.put("channel", "application");
                    params.put("email",email);
                    params.put("pass", Md5password);
                    params.put("name",name);
                    params.put("mobile",moblie);
                    Log.e("Details",email+""+Md5password+""+name+""+moblie+"");
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
