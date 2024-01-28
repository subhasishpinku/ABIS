package com.example.sennovations.newabis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basic extends Fragment implements View.OnClickListener {
    ProgressDialog progressDialog;
    private  Button buy;
    LinearLayout ll1,ll2,ll3,ll4,ll5;
    TextView tv1,tv2,tv3,tv4,tv5,skip;
    ArrayList<String> basic  = new ArrayList<>();
    View view;
    ImageView iv1,iv2,iv3,iv4,iv5;
    SqlHandler sqlHandler;
    String api;
    Button subscription;
    List<String> first;
    ArrayList<String> assetarray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (view == null) {
            view =  inflater.inflate(R.layout.activity_basic,container,false);

        } else {

        }
        buy = (Button)view.findViewById(R.id.buy);
        ll1 = (LinearLayout)view.findViewById(R.id.ll1);
        ll2 = (LinearLayout)view.findViewById(R.id.ll2);
        ll3 = (LinearLayout)view.findViewById(R.id.ll3);
        ll4 = (LinearLayout)view.findViewById(R.id.ll4);
        ll5 = (LinearLayout)view.findViewById(R.id.ll5);
        tv1 = (TextView)view.findViewById(R.id.tv1);
        tv2 = (TextView)view.findViewById(R.id.tv2);
        tv3 = (TextView)view.findViewById(R.id.tv3);
        tv4 = (TextView)view.findViewById(R.id.tv4);
        tv5 = (TextView)view.findViewById(R.id.tv5);
//        skip = (TextView)view.findViewById(R.id.skip);
        iv1 = (ImageView)view.findViewById(R.id.iv1);
        iv2 = (ImageView)view.findViewById(R.id.iv2);
        iv3 = (ImageView)view.findViewById(R.id.iv3);
        iv4 = (ImageView)view.findViewById(R.id.iv4);
        iv5 = (ImageView)view.findViewById(R.id.iv5);
//        skip.setOnClickListener(this);
        subscription = (Button)view.findViewById(R.id.subscription);
        subscription.setOnClickListener(this);
        sqlHandler = new SqlHandler(getContext());

        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
        iv5.setVisibility(View.GONE);
        getapi();
        View_scrip();


//        Subscription activity = (Subscription) getActivity();
//        ArrayList<String> data = activity.getBasic();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Subscription)getActivity()).setViewpager(1);

            }
        });

        return view;

    }


    public  void getapi(){

        String query = "Select * from API_KEY";
        Cursor c =sqlHandler.selectQuery(query);
        if(c!=null){
            if(c.moveToFirst()) {
                do {
                    api = c.getString(c.getColumnIndex("api_key"));
                    Log.e("API_KEY",api+"");

                } while (c.moveToNext());
            }
        }
        c.close();
    }



       private void View_scrip() {
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.read_subscription,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject  = new JSONObject(response);

                            {
                                JSONObject json_obj11 = jsonObject.getJSONObject("BASIC");
                                String SUBSCRIPTION_ID = json_obj11.getString("SUBSCRIPTION_ID");
                                String NAME = json_obj11.getString("NAME");
                                String PRICE = json_obj11.getString("PRICE");
                                String DESCRIPTION = json_obj11.getString("DESCRIPTION");
                                Log.e("Details", SUBSCRIPTION_ID + " - " + NAME + " - " + PRICE + " - " + DESCRIPTION + " - " + "");
                                basic.add(SUBSCRIPTION_ID);
                                basic.add(NAME);
                                basic.add(PRICE);
                                basic.add(DESCRIPTION);
                                JSONArray FEATURES = json_obj11.getJSONArray("FEATURES");
                                Log.e("FEATURES", String.valueOf(FEATURES));
                                for (int j = 0; j < FEATURES.length(); j++) {
                                    JSONObject json_obj111 = FEATURES.getJSONObject(j);
                                    String NAME1 = json_obj111.getString("NAME");
                                    String DESCRIPTION1 = json_obj111.getString("DESCRIPTION");
                                    Log.e("De", NAME1 + " - " + DESCRIPTION1 + " - " + "");
                                    basic.add(DESCRIPTION1);
                                    Log.e("BASIC", basic.toString());

                                }
                            }
                            int a = basic.size();
                            Log.e("DATA",basic+"");
                            first = basic.subList(0, 4);
                            Log.e("First",first+"");
                            List<String> Second = basic.subList(4, a);
                            Log.e("Second",Second+"");

                            int b = Second.size();
                            if(b==1){
                                iv1.setVisibility(View.VISIBLE);
                                tv1.setText(Second.get(0));
                            }else if(b==2){
                                iv1.setVisibility(View.VISIBLE);
                                tv1.setText(Second.get(0));
                                iv2.setVisibility(View.VISIBLE);
                                tv2.setText(Second.get(1));
                            }else if(b==3){
                                iv1.setVisibility(View.VISIBLE);
                                tv1.setText(Second.get(0));
                                iv2.setVisibility(View.VISIBLE);
                                tv2.setText(Second.get(1));
                                iv3.setVisibility(View.VISIBLE);
                                tv3.setText(Second.get(2));
                            }else if(b==4){
                                iv1.setVisibility(View.VISIBLE);
                                tv1.setText(Second.get(0));
                                iv2.setVisibility(View.VISIBLE);
                                tv2.setText(Second.get(1));
                                iv3.setVisibility(View.VISIBLE);
                                tv3.setText(Second.get(2));
                                iv4.setVisibility(View.VISIBLE);
                                tv4.setText(Second.get(3));
                            }else if(b==5){
                                iv1.setVisibility(View.VISIBLE);
                                tv1.setText(Second.get(0));
                                iv2.setVisibility(View.VISIBLE);
                                tv2.setText(Second.get(1));
                                iv3.setVisibility(View.VISIBLE);
                                tv3.setText(Second.get(2));
                                iv4.setVisibility(View.VISIBLE);
                                tv4.setText(Second.get(3));
                                iv5.setVisibility(View.VISIBLE);
                                tv5.setText(Second.get(4));
                            }
//                            ll1.setVisibility(View.VISIBLE);
//                            tv1.setText(Second.get(0));
//                            ll2.setVisibility(View.VISIBLE);
//                            tv2.setText(Second.get(1));
//                            ll3.setVisibility(View.VISIBLE);
//                            tv3.setText(Second.get(2));
//                            ll4.setVisibility(View.VISIBLE);
//                            tv4.setText(Second.get(3));
//                            ll5.setVisibility(View.VISIBLE);
//                            tv5.setText(Second.get(4));
                            buy.setText(first.get(2));






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
    public void onDestroyView() {
        if (view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.subscription:

                postsubscription();
                break;
//            case R.id.skip:
//                Intent intent =  new Intent(getContext(), CalculatorActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    public void postsubscription(){
        if (progressDialog!=null){
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.update_subscription,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try{
                            String asset = "";
                            JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            Log.e("Response obj",jsonObject+"");
                            JSONArray jsonArray = jsonObject.getJSONArray("asset");
                            for (int i = 0;i<jsonArray.length();i++){
                                asset = jsonArray.getString(i);
                                assetarray.add(asset);
                            }
                            Log.e("asset",asset+"");
                            Log.e("assetarray",assetarray+"");

                            if (Integer.parseInt(statusCode)==200) {

                                final AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
                                builder.setTitle("Verify")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(getActivity(),InvestmentActivity.class);
//                                                intent.putExtra("asset",asset);
                                                intent.putExtra("assetarray",assetarray);
                                                Log.e("assetarray",assetarray +"");
                                                startActivity(intent);

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
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
                params.put("api_key",api );
                params.put("plan_name",first.get(1));



                return params;
            }
        };
        requestQueue.add(stringRequest);





    }
}
