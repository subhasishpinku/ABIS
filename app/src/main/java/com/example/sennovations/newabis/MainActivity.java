package com.example.sennovations.newabis;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    Button signup;
    User user;
    Button login;
    String Md5password;
    String API_KEY;
    SqlHandler sqlHandler;
    TextView forgotpass;
    String email;
    private  Boolean exit = false;

    //    private SignInButton SignIn;
    Button SignIn;
    private Button signout;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private ProgressDialog progressDialog;
    EditText et_pass_login, et_email_login;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String age_group;
    String tax_age_group;
    String fullname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        sqlHandler = new SqlHandler(this);
        if(isNetworkAvailable()) {
            Portfolio();
            fdDetails();
            EMI();
            recfdDetails();
        }else {


        }
//        TextView textView = (TextView) SignIn.getChildAt(0);
//        textView.setText("Log in with Google");
        forgotpass = (TextView)findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(this);
        TextView textView = (TextView)findViewById(R.id.dontac);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"helvetica.otf");
        textView.setTypeface(typeface);


        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

//        signout = (Button) findViewById(R.id.signout);
//        signout.setOnClickListener(this);
//        signout.setVisibility(View.GONE);

        SignIn = (Button) findViewById(R.id.signin);
        SignIn.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Typeface tf = Typeface.createFromAsset(getAssets(), "helvetica.otf");
        et_email_login = (EditText) findViewById(R.id.et_email_login);
        et_email_login.setTypeface(tf);
        et_pass_login = (EditText) findViewById(R.id.et_pass_login);
        et_pass_login.setTypeface(tf);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        String email = et_email_login.getText().toString();
        String password = et_pass_login.getText().toString();
        sharedpreferences=getSharedPreferences("login",MODE_PRIVATE);


        if(sharedpreferences.contains(email) && sharedpreferences.contains(password)){
            loginCheck();
//            startActivity(new Intent(MainActivity.this,CalculatorActivity.class));
//            finish();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                if(isNetworkAvailable()){
                    onFblogin();
                }else {

                }

                break;
            case R.id.signin:

                if(isNetworkAvailable()){
                    signin();
                }else {

                }
                break;
//            case R.id.signout:
//                SignOut();
//                break;
            case R.id.login:

                if(isNetworkAvailable()){
                    doLogin();
                }else {

                }
                break;
            case R.id.forgotpass:

                if(isNetworkAvailable()){
                    forgot();
                }else {

                }
                break;


        }


    }

    public void forgot(){
        Intent intent = new Intent(getApplicationContext(),ForgotActivity.class);
        startActivity(intent);
    }


    public boolean validateEmail(String email) {

        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private String md5(String password) {
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


    private void doLogin() {
        String email = et_email_login.getText().toString();
        String password = et_pass_login.getText().toString();
        sharedpreferences=getSharedPreferences("login",MODE_PRIVATE);


        if(sharedpreferences.contains(email) && sharedpreferences.contains(password)){
//            loginCheck();
            startActivity(new Intent(MainActivity.this,CalculatorActivity.class));
            finish();   //finish current activity
        }





//        if (!validateEmail(email)) {
//            if (progressDialog!=null){
//                progressDialog.dismiss();
//            }
//            et_email_login.setFocusable(true);
//            et_email_login.setError("Invalid email");
//            et_email_login.requestFocus();
//        }
        if (email.equals("") || email == null /*|| (!validateEmail(email))*/)
        {
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
        }
        if (password.length() == 0) {
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
            et_pass_login.setFocusable(true);
            et_pass_login.setError("Invalid Password");
            et_pass_login.requestFocus();
        } else if (password.equals("") || password == null) {
            if (progressDialog!=null){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please enter Correct Password", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else {
            validateLogin(email, md5(password), "",1);

        }
    }

    void loginCheck() {
        if (!et_email_login.getText().toString().equals(null) && !et_pass_login.getText().toString().equals(null)) {
            SharedPreferences.Editor e = sharedpreferences.edit();
            String email = et_email_login.getText().toString();
            e.putString("email", et_email_login.getText().toString());
            e.putString("password", et_pass_login.getText().toString());
            e.commit();

//            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

            startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
            finish();

        } else {
        }
    }




    private void onFblogin()
    {
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("Success");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {



                                if (response.getError() != null) {

                                    System.out.println("ERROR");
                                } else {
                                    Log.e("ABIS","Success"+"");

                                    System.out.println("Success");
                                    try {

                                        String jsonresult = String.valueOf(json);
                                        System.out.println("JSON Result"+jsonresult);

                                        user = new User();
                                        user.facebookID = json.getString("id").toString();
//                                        user.email = json.getString("email").toString();
                                        user.name = json.getString("name").toString();
                                        user.gender = json.getString("gender").toString();
                                        try {
                                            String email = user.facebookID;
                                            String name = json.getString("name");
                                            int type =2;

                                            validateLogin(email,"",name,type);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                        Log.e("ABIS",user.facebookID+"");

//                                                Intent intent = new Intent(MainActivity.this,CalculatorActivity.class);
//                                                startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Log.e("ABIS FBID",user.facebookID+"");
                                Log.e("ABIS EMAIL",user.email+"");
                                Log.e("ABIS name",user.name+"");
                                Log.e("ABIS gender",user.gender+"");
                                String name = user.name;
                                String email = user.facebookID;
                                int type =2;
                                fbGoogleSignUp(email,name,type);
//                                        Intent intent=new Intent(MainActivity.this,CalculatorActivity.class);
//                                        intent.putExtra("API_KEY",API_KEY);
//                                        startActivity(intent);
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Log.e("Cancel",""+"");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Error",error.toString());
            }
        });
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signin(){
        if(isNetworkAvailable()){
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,REQ_CODE);
        }else {

        }


    }
    private void SignOut(){

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });




    }
    private void handelResult(GoogleSignInResult googleSignInResult){
        if(googleSignInResult.isSuccess()){
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            String name = account.getDisplayName();
            email = account.getEmail();
//            String img_url = account.getPhotoUrl().toString();
//            Glide.with(this).load(img_url);

            Log.e("ABIS NAme",name+"");
            Log.e("ABIS Email",email+"");
            int type =3;
//            Log.e("ABIS imgurl",img_url+"");
            fbGoogleSignUp(email,name,type);
            validateLogin(email,"",name,type);
//            Intent intent=new Intent(MainActivity.this,CalculatorActivity.class);
//            startActivity(intent);
            updateUI(true);
        }
        else {
            updateUI(false);
        }


    }
    private void updateUI(boolean isLogin){
        if(isLogin){

            SignIn.setVisibility(View.VISIBLE);
//            signout.setVisibility(View.VISIBLE);


        }
        else {
            SignIn.setVisibility(View.VISIBLE);
//            signout.setVisibility(View.GONE);

        }




    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handelResult(result);
        }
    }


    private void validateLogin(final String email, final String password, final String name, final int type) {
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.log_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response11",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            Log.e("Msg code",msg);
//                            if(Integer.parseInt(statusCode)==400){
//                                AlertDialog.Builder builder;
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//                                } else {
//                                    builder = new AlertDialog.Builder(MainActivity.this);
//                                }
//                                builder.setTitle("Sorry !")
//                                        .setMessage(msg)
//                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent  intent = new Intent(MainActivity.this,MainActivity.class);
//                                                startActivity(intent);
//                                            }
//                                        })
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .show();
//                            }
//                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                            Log.e("ABIS  Login Details ",statusCode + "--"+ msg+"--");
//                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                            if (Integer.parseInt(statusCode)==200){
                                fullname = jsonObject.getString("name");
                                API_KEY = jsonObject.getString("API_KEY");
                                Log.e("API_KEY",API_KEY+"");
                                String profile_status = jsonObject.getString("profile_status");
                                Log.e("profile_status",profile_status+"");


                                if (type==2){
                                    SignOut();
                                }

                                //--------------------------------------
                                String query = "Select * from API_KEY";
                                Cursor c = sqlHandler.selectQuery(query);
                                int a =c.getCount();
                                Log.e("COUNT",a+"");
                                if(c.getCount()==0){
                                    String q = "Insert into API_KEY(api_key_email,api_key) values ('"+email+"','"+API_KEY+"')";
                                    sqlHandler.executeQuery(q);
                                    Log.e("q",q+"");

                                }else {
//
                                    String query2 = "Update API_KEY set api_key = '" + API_KEY + "' , api_key_email = '"+email+"' where api_key_id =  "+1+" ";
                                    sqlHandler.executeQuery(query2);
                                    Log.e("query2",query2+"");
//
                                }
                                //--------------------------------
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(email, email);
                                editor.putString(name, name);
                                editor.putString(password, password);
//                                editor.putString(age_group,tax_age_group);
//                                editor.putString(tax_age_group,tax_age_group);
                                editor.commit();


                                if(Integer.parseInt(profile_status)==400) {
                                    String mobile = jsonObject.getString("mobile");
                                    Intent in = new Intent(getBaseContext(), ProfileActivity.class);
                                    in.putExtra("fullname",fullname);
                                    in.putExtra("email", email);
                                    in.putExtra("mobile",mobile);
                                    in.putExtra("API_KEY",API_KEY);
                                    Log.e("API_KEY",API_KEY+""+fullname+"");
                                    startActivity(in);
                                }
                                else if(Integer.parseInt(profile_status)==300){
                                    String data = jsonObject.getString("data");
                                    JSONObject obj1 = new JSONObject(data);
                                    String mobile = obj1.getString("mobile");
                                    String age = obj1.getString("age");
                                    String dob = obj1.getString("dob");
                                    String city = obj1.getString("city");
                                    String state = obj1.getString("state");
                                    String country = obj1.getString("country");
                                    String pin = obj1.getString("pin");
                                    age_group = obj1.getString("age_group");
                                    tax_age_group= obj1.getString("tax_age_group");
                                    String query1 = "Select * from age_pattern";
                                    Cursor c1 = sqlHandler.selectQuery(query1);
                                    int b =c1.getCount();
                                    Log.e("COUNT",b+"");
                                    if(c1.getCount()==0){
                                        String qq = "Insert into age_pattern(age_pattern_email,age_pattern_api,age_pattern_age_group,age_pattern_tax_age_group) values ('"+email+"','"+API_KEY+"','"+age_group+"','"+tax_age_group+"')";
                                        sqlHandler.executeQuery(qq);
                                        Log.e("qq",qq+"");

                                    }else {

                                        String query22 = "Update age_pattern set age_pattern_api = '" + API_KEY + "' , age_pattern_email = '"+email+"', age_pattern_age_group = '"+age_group+"', age_pattern_tax_age_group = '"+tax_age_group+"' where age_pattern_id =  "+1+" ";
                                        sqlHandler.executeQuery(query22);
                                        Log.e("query22",query22+"");
                                    }

                                    Log.e("Get All Details",age+""+dob+""+city+""+state+""+country+""+pin+""+fullname+"");
                                    Intent intent = new Intent(getApplicationContext(),OTP.class);
                                    intent.putExtra("fullname",fullname);
                                    intent.putExtra("mobile",mobile);
                                    intent.putExtra("email", email);
                                    intent.putExtra("API_KEY",API_KEY);
                                    Log.e("DE",mobile+""+API_KEY+"");
                                    startActivity(intent);

                                }else if(Integer.parseInt(profile_status)==200){
                                    String data = jsonObject.getString("data");
                                    JSONObject obj1 = new JSONObject(data);
                                    String mobile = obj1.getString("mobile");
                                    String age = obj1.getString("age");
                                    String dob = obj1.getString("dob");
                                    String city = obj1.getString("city");
                                    String state = obj1.getString("state");
                                    String country = obj1.getString("country");
                                    String pin = obj1.getString("pin");
                                    age_group = obj1.getString("age_group");
                                    tax_age_group= obj1.getString("tax_age_group");

                                    String query1 = "Select * from age_pattern";
                                    Cursor c1 = sqlHandler.selectQuery(query1);
                                    int b =c1.getCount();
                                    Log.e("COUNT",b+"");
                                    if(c1.getCount()==0){
                                        String qq = "Insert into age_pattern(age_pattern_email,age_pattern_api,age_pattern_age_group,age_pattern_tax_age_group) values ('"+email+"','"+API_KEY+"','"+age_group+"','"+tax_age_group+"')";
                                        sqlHandler.executeQuery(qq);
                                        Log.e("qq",qq+"");

                                    }else {

                                        String query22 = "Update age_pattern set age_pattern_api = '" + API_KEY + "' , age_pattern_email = '"+email+"', age_pattern_age_group = '"+age_group+"', age_pattern_tax_age_group = '"+tax_age_group+"' where age_pattern_id =  "+1+" ";
                                        sqlHandler.executeQuery(query22);
                                        Log.e("query22",query22+"");
                                    }
                                    Log.e("Get All Details",age+""+dob+""+city+""+state+""+country+""+pin+""+mobile+""+age_group+""+tax_age_group+""+fullname);

                                    Intent in = new Intent(getBaseContext(), Subscription.class);
                                    in.putExtra("email", email);
                                    in.putExtra("fullname",fullname);
                                    in.putExtra("age_group",age_group);
                                    in.putExtra("tax_age_group",tax_age_group);
                                    startActivity(in);
                                }
                            } else{

                                if (jsonObject.getString("msg").equalsIgnoreCase("Email and Password did not match")){
                                    if (type==2 || type==3){
                                        fbGoogleSignUp(email,name,type);
                                    } else {
//                                        new AlertDialog.Builder(MainActivity.this)
//                                                .setMessage(jsonObject.getString("msg"))
//                                                .setCancelable(true)
//                                                .setPositiveButton("OK",null)
//                                                .show();
                                    }
                                }
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(MainActivity.this);
                                }
                                builder.setTitle("Sorry !")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent  intent = new Intent(MainActivity.this,MainActivity.class);
//                                                startActivity(intent);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
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
                params.put("email", email);
                if (type == 1) {
                    params.put("pass", password);
                    params.put("channel", "application");
                } else if (type==2){
                    params.put("channel", "facebook");
                } else if (type==3){
                    params.put("channel", "google");
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void fbGoogleSignUp(final String email, final String name,final int type) {
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
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            API_KEY = jsonObject.getString("API_KEY");

                            Log.e("ABIS ALl Details",statusCode + "" + "" +msg+""+API_KEY+"");
                            if (Integer.parseInt(statusCode)==200){
                                validateLogin(email,"","",type);

                            }else {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(jsonObject.getString("msg"))

                                        .setCancelable(true)
                                        .setPositiveButton("OK",null)
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
                params.put("email", email);
                params.put("name", name);
                if (type==2){
                    params.put("channel", "facebook");
                } else if (type==3){
                    params.put("channel", "google");
                }
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

    private void Portfolio() {
//        String url = "http://solodiary.com/abis/webservice/thumbrule_portfolio/show_from_database.php";
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.thumbrule_portfolio,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            {
                                JSONArray age_group = jsonObject.getJSONArray("age_group");
                                Log.e("age_group", String.valueOf(age_group));
                                for (int i = 0; i < age_group.length(); i++) {
                                    JSONObject json_obj = age_group.getJSONObject(i);

                                    String NAME = json_obj.getString("NAME");
                                    String DESCRIPTION = json_obj.getString("DESCRIPTION");
                                    String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
                                    String MORE_INFO = json_obj.getString("MORE_INFO");

                                    Log.e("json_obj", NAME + ",," + DESCRIPTION + ",," + DEFAULT_VALUE + ",," + MORE_INFO);

                                    String Query = "Insert into age_group (age_group_NAME,age_group_DESCRIPTION,age_group_MORE,age_group_VALUE) Values ('" + NAME + "','" + DESCRIPTION + "','" + MORE_INFO + "','" + DEFAULT_VALUE + "')";
                                    sqlHandler.executeQuery(Query);
                                    Log.e("Query", Query + "");
                                }
                            }
                            {

                                JSONArray asset_group = jsonObject.getJSONArray("asset_group");
                                Log.e("asset_group", String.valueOf(asset_group));
                                for (int i = 0; i < asset_group.length(); i++) {
                                    JSONObject json_obj = asset_group.getJSONObject(i);

                                    String NAME = json_obj.getString("NAME");
                                    String DESCRIPTION = json_obj.getString("DESCRIPTION");
                                    String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
                                    String MORE_INFO = json_obj.getString("MORE_INFO");

                                    Log.e("json_obj", NAME + ",," + DESCRIPTION + ",," + DEFAULT_VALUE + ",," + MORE_INFO);

                                    String Query = "Insert into asset_group (asset_group_NAME,asset_group_DESCRIPTION,asset_group_MORE,asset_group_VALUE) Values ('" + NAME + "','" + DESCRIPTION + "','" + MORE_INFO + "','" + DEFAULT_VALUE + "')";
                                    sqlHandler.executeQuery(Query);
                                    Log.e("Query", Query + "");
                                }
                            }
                            {

                                JSONArray riskprofile = jsonObject.getJSONArray("return");
                                Log.e("return", String.valueOf(riskprofile));
                                for (int i = 0; i <riskprofile.length(); i++) {
                                    JSONObject json_obj = riskprofile.getJSONObject(i);

                                    String CATEGORY = json_obj.getString("CATEGORY");
                                    String DESCRIPTION = json_obj.getString("DESCRIPTION");
                                    String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");
                                    String MORE_INFO = json_obj.getString("MORE_INFO");
                                    String MIN_RANGE = json_obj.getString("MIN_RANGE");
                                    String MAX_RANGE = json_obj.getString("MAX_RANGE");

                                    Log.e("json_obj", CATEGORY + ",," + DESCRIPTION + ",," + DEFAULT_VALUE + ",," + MORE_INFO +", ,"+ MIN_RANGE+",,"+MAX_RANGE);

                                    String Query = "Insert into riskprofile (riskprofile_CATEGORY,riskprofile_DESCRIPTION,riskprofile_MORE,riskprofile_VALUE,riskprofile_MIN_RANGE,riskprofile_MAX_RANGE) Values ('" + CATEGORY + "','" + DESCRIPTION + "','" + MORE_INFO + "','" + DEFAULT_VALUE + "','" + MIN_RANGE + "','" + MAX_RANGE + "')";
                                    sqlHandler.executeQuery(Query);
                                    Log.e("Query", Query + "");
                                }
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


    private void fdDetails() {
//        String url = "http://solodiary.com/abis/webservice/calculator/?cal_type=fd";
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.fdDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            String type = "FD";
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String int_rate  =jsonObject.getString("int_rate");
                            //-------------------int_rate---------------------
                            JSONObject jsonObject1 = new JSONObject(int_rate);
                            int min_int_rate = jsonObject1.getInt("min_int_rate");
                            int max_int_rate = jsonObject1.getInt("max_int_rate");
                            int int_rate_default = jsonObject1.getInt("int_rate_default");

                            String query = "Insert into INTEREST (type,min_int_rate,max_int_rate,int_rate_default) values ('"+ type +"','"+ min_int_rate +"','"+ max_int_rate +"','"+ int_rate_default +"') ";
                            sqlHandler.executeQuery(query);
                            Log.e("Query",query);

                            //------------Interest Insert Completed  ----------------------------

                            String tenure = jsonObject.getString("tenure");
                            JSONObject jsonObject2 = new JSONObject(tenure);
                            int min_tenure = jsonObject2.getInt("min_tenure");
                            int max_tenure = jsonObject2.getInt("max_tenure");
                            int tenure_default = jsonObject2.getInt("tenure_default");
                            String query1 = "Insert into TENURE (tenure_type,min_tenure,max_tenure,tenure_default) values ('"+ type +"','"+ min_tenure +"','"+ max_tenure +"','"+ tenure_default +"') ";
                            sqlHandler.executeQuery(query1);
                            Log.e("Query",query1);
                            //------------------- Tenure Insert Completed----------------------------


                            JSONArray array_payout = jsonObject.getJSONArray("payout");
                            Log.e("array_payout", String.valueOf(array_payout));
                            for(int i=0;i<array_payout.length();i++){
                                JSONObject json_obj = array_payout.getJSONObject(i);

                                String NAME = json_obj.getString("NAME");
                                String pay_fd_DESCRIPTION = json_obj.getString("DESCRIPTION");
                                String MORE_INFO = json_obj.getString("MORE_INFO");
                                String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");

                                Log.e("Name json_obj_payout ",NAME+",,"+MORE_INFO+",,"+DEFAULT_VALUE);

                                String query2 = "Insert into PAYOUTTYPE_FD (pay_fd_name,pay_fd_DESCRIPTION,pay_fd_more,pay_fd_value) values ('"+ NAME +"','"+ pay_fd_DESCRIPTION +"','"+ MORE_INFO +"','"+ DEFAULT_VALUE +"') ";
                                sqlHandler.executeQuery(query2);
                                Log.e("Query",query2);


                            }

                            //------------------- PAYOUTTYPE_FD Insert Completed----------------------------

                            JSONArray array_compounding = jsonObject.getJSONArray("compounding");
                            Log.e("array_compounding", String.valueOf(array_compounding));
                            for(int i=0;i<array_compounding.length();i++){
                                JSONObject json_obj_compounding = array_compounding.getJSONObject(i);

                                String NAME = json_obj_compounding.getString("NAME");
                                String DESCRIPTION = json_obj_compounding.getString("DESCRIPTION");
                                String MORE_INFO = json_obj_compounding.getString("MORE_INFO");
                                String DEFAULT_VALUE = json_obj_compounding.getString("DEFAULT_VALUE");

                                Log.e("json_obj_compounding",NAME+",,"+MORE_INFO+",,"+DEFAULT_VALUE);

                                String query3 = "Insert into COMPOUNDING_FD (com_fd_name,com_fd_des,com_fd_more,com_fd_value) values ('"+ NAME +"','"+ DESCRIPTION +"','"+ MORE_INFO +"','"+ DEFAULT_VALUE +"') ";
                                sqlHandler.executeQuery(query3);
                                Log.e("Query",query3);

                            }

                            //------------------- COMPOUNDING_FD Insert Completed----------------------------

                            JSONArray taxable_inc = jsonObject.getJSONArray("taxable_inc");
                            for(int h = 0;h<taxable_inc.length();h++) {
                                JSONObject obj_taxable_inc = taxable_inc.getJSONObject(h);
                                String Name = obj_taxable_inc.getString("NAME");
                                String More = obj_taxable_inc.getString("MORE_INFO");
                                String Description = obj_taxable_inc.getString("DESCRIPTION");

                                Log.e("obj_taxable_inc", Name + ",," + More + ",," + Description);
                                String query4 = "Insert into taxable_inc(taxable_inc_NAME,taxable_inc_DESCRIPTION,taxable_inc_MORE) values ('" + Name + "','" + Description + "','" + More + "') ";
                                sqlHandler.executeQuery(query4);
                                Log.e("Query", query4);

                            }



                            Log.e("FD DETAILS",statusCode+" - - "+" - - "+int_rate_default+"----"+max_int_rate+"----"+min_int_rate+"");

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

    private void EMI() {
//        String url = "http://solodiary.com/abis/webservice/calculator/?cal_type=emi";
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.emi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            String type = "EMI";
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String int_rate  =jsonObject.getString("int_rate");
                            //-------------------int_rate---------------------
                            JSONObject jsonObject1 = new JSONObject(int_rate);
                            int min_int_rate = jsonObject1.getInt("min_int_rate");
                            int max_int_rate = jsonObject1.getInt("max_int_rate");
                            int int_rate_default = jsonObject1.getInt("int_rate_default");

                            String query = "Insert into INTEREST (type,min_int_rate,max_int_rate,int_rate_default) values ('"+ type +"','"+ min_int_rate +"','"+ max_int_rate +"','"+ int_rate_default +"') ";
                            sqlHandler.executeQuery(query);
                            Log.e("Query",query);

                            //------------Interest Insert Completed  ----------------------------


                            JSONArray emi_type = jsonObject.getJSONArray("emi_type");
                            Log.e("emi_type", String.valueOf(emi_type));
                            for(int i=0;i<emi_type.length();i++){
                                JSONObject json_obj_emi_type = emi_type.getJSONObject(i);

                                String NAME = json_obj_emi_type.getString("NAME");
                                String DESCRIPTION = json_obj_emi_type.getString("DESCRIPTION");
                                String DEFAULT_VALUE = json_obj_emi_type.getString("DEFAULT_VALUE");

                                Log.e("json_obj_compounding",NAME+",,"+DESCRIPTION+",,"+DEFAULT_VALUE);

                                String query3 = "Insert into emi_type (emi_type_NAME,emi_type_DESCRIPTION,emi_type_VALUE) values ('"+ NAME +"','"+ DESCRIPTION +"','"+ DEFAULT_VALUE +"') ";
                                sqlHandler.executeQuery(query3);
                                Log.e("Query",query3);

                            }

                            //------------------- COMPOUNDING_FD Insert Completed----------------------------




                            Log.e("FD DETAILS",statusCode+" - - "+" - - "+int_rate_default+"----"+max_int_rate+"----"+min_int_rate+"");



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

    private void recfdDetails() {
//        String url = "http://solodiary.com/abis/webservice/calculator/?cal_type=reccuring_fd";
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.recfdDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            String  type = "RecFD";
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String int_rate  =jsonObject.getString("int_rate");
                            //-------------------int_rate---------------------
                            JSONObject jsonObject1 = new JSONObject(int_rate);
                            int min_int_rate = jsonObject1.getInt("min_int_rate");
                            int max_int_rate = jsonObject1.getInt("max_int_rate");
                            int int_rate_default = jsonObject1.getInt("int_rate_default");

                            String query = "Insert into INTEREST (type,min_int_rate,max_int_rate,int_rate_default) values ('"+ type +"','"+ min_int_rate +"','"+ max_int_rate +"','"+ int_rate_default +"') ";
                            sqlHandler.executeQuery(query);
                            Log.e("Query",query);

                            //------------Interest Insert Completed  ----------------------------


                            JSONArray frequency_of_invest = jsonObject.getJSONArray("frequency_of_invest");
                            Log.e("frequency_of_invest", String.valueOf(frequency_of_invest));
                            for(int i=0;i<frequency_of_invest.length();i++){
                                JSONObject json_obj = frequency_of_invest.getJSONObject(i);

                                String NAME = json_obj.getString("DESCRIPTION");
                                String MORE_INFO = json_obj.getString("MORE_INFO");
                                String DEFAULT_VALUE = json_obj.getString("DEFAULT_VALUE");

                                Log.e("json_obj_compounding",NAME+",,"+MORE_INFO+",,"+DEFAULT_VALUE);

                                String query1 = "Insert into frequency_of_invest (fre_req_name,fre_req_more,fre_req_value) values ('"+ NAME +"','"+ MORE_INFO +"','"+ DEFAULT_VALUE +"') ";
                                sqlHandler.executeQuery(query1);
                                Log.e("Query",query1);

                            }

                            //------------------- frequency_of_invest Insert Completed----------------------------


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
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }







}

