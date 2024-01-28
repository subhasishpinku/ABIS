package com.example.sennovations.newabis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sennovations on 27-06-2017.
 */

public class PiechartActivity extends Activity implements OnChartValueSelectedListener {

    PieChart pieChart;
    ProgressDialog progressDialog;
    SqlHandler sqlHandler;
    int value;
    String api_key;
    ArrayList<Entry> yvalues = new ArrayList<Entry>();
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<String>x = new ArrayList<String>();
    String age,assate,risk;
    TextView et1,et2,et3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);
        Intent intent = getIntent();
        age = intent.getStringExtra("age");
        assate = intent.getStringExtra("assate");
        risk = intent.getStringExtra("risk");
        et1 = (TextView)findViewById(R.id.et1);
        et2 = (TextView)findViewById(R.id.et2);
        et3 = (TextView)findViewById(R.id.et3);
        et1.setText(age);
        et2.setText(assate);
        et3.setText(risk);
        Typeface tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
        TextView textview = (TextView)findViewById(R.id.textview);
        textview.setTypeface(tf);
        sqlHandler = new SqlHandler(this);
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        show();
        getAssate();



    }
    //------------------------------
public void pie(){
    Typeface tf = Typeface.createFromAsset(getAssets(), "helvetica.otf");
    pieChart.animateXY(1500, 1500);
    pieChart.setUsePercentValues(true);
    pieChart.setDescription("Powered By ABIS");
    pieChart.setExtraOffsets(5, 10, 5, 5);
    pieChart.setDragDecelerationFrictionCoef(0.95f);
    pieChart.setDrawHoleEnabled(true);
    pieChart.setTransparentCircleColor(Color.WHITE);
    pieChart.setTransparentCircleAlpha(120);
    pieChart.setHoleRadius(10f);
    pieChart.setTransparentCircleRadius(40f);
    pieChart.setDrawCenterText(true);
    pieChart.setRotationAngle(0);
    pieChart.setRotationEnabled(true);
    pieChart.setHighlightPerTapEnabled(true);
    pieChart.setOnChartValueSelectedListener(this);

    Legend l = pieChart.getLegend();
    l.setFormSize(13f);
    l.setForm(Legend.LegendForm.SQUARE);
    l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
    l.setTextSize(13f);
    l.setTextColor(Color.BLACK);
    l.setXEntrySpace(7f);
    l.setYEntrySpace(5f);
    l.setYOffset(0f);
    l.setTypeface(tf);

    l.setWordWrapEnabled(true);


        PieDataSet dataSet = new PieDataSet(yvalues,"");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
//        dataSet.setDrawValues(false);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLUE);
        pieChart.setDrawSliceText(false);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.setVisibility(View.VISIBLE);
        pieChart.invalidate();


}

            //------------------------------
//    public void pie(ArrayList<String> xVals, ArrayList<Entry> yvalues){
//
//
//        this.xVals = xVals;
//        this.yvalues = yvalues;
//        Log.e("ALL",xVals+""+yvalues+"");
//
//        pieChart.setRotationEnabled(true);
//        pieChart.setHoleRadius(10f);
//        pieChart.setTransparentCircleAlpha(0);
////        pieChart.setCenterText("PieChart");
//        pieChart.setCenterTextSize(20);
//
//        PieDataSet dataSet = new PieDataSet(yvalues,"");
//        dataSet.setSliceSpace(0);
//        dataSet.setSelectionShift(5f);
//        Log.e("dataSet",dataSet+"");
//
//        PieData data = new PieData(xVals, dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        pieChart.setData(data);
//
//        pieChart.setDescription("Powered by ABIS");
//        pieChart.setDescriptionTextSize(13);
//        pieChart.setBackgroundColor(Color.WHITE);
//
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setTransparentCircleRadius(10f);
//        pieChart.setHoleRadius(10f);
//
//
//        final int[] MY_COLORS = {Color.rgb(192,0,0),Color.rgb(133, 21, 142),Color.rgb(155, 133, 21),Color.rgb(255,192,0),
//                Color.rgb(127,127,127), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189),Color.rgb(255,0,0),Color.rgb(197, 6, 140),};
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for(int c: MY_COLORS) colors.add(c);
//
//        dataSet.setColors(colors);
////        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        data.setValueTextSize(13f);
////        data.setValueTextColor(Color.DKGRAY);
//        data.setValueTextColor(Color.GRAY);
//        data.setValueTextColor(Color.parseColor("#333333"));
//
//        pieChart.setOnChartValueSelectedListener(PiechartActivity.this);
//
//        pieChart.animateXY(700, 700);
//    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.e("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);



    }

    @Override
    public void onNothingSelected() {

    }



    private void getAssate() {

        Intent intent = getIntent();
        value = intent.getIntExtra("value",value);
        String url = "http://solodiary.com/abis/webservice/thumbrule_portfolio/output.php?pattern="+value+"&api_key="+api_key+"";

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
                            String data=jsonObject.getString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String ASSET_TYPE = jsonObject1.getString("ASSET_TYPE");
                                float ALLOCATION = (float) jsonObject1.getDouble("ALLOCATION");
                                Log.e("ALLOCATION",ALLOCATION+""+ASSET_TYPE+"");

                                if(ALLOCATION == 0.0){

                                }else {
                                    xVals.add(ASSET_TYPE);
                                    yvalues.add(new Entry(ALLOCATION, i));
                                }
                                Log.e("xVals",xVals+"");

                            }
                            pie();

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

    public  void show(){
        String query = "Select * from API_KEY";
        Cursor c1 = sqlHandler.selectQuery(query);
        int a = c1.getCount();
        Log.e("Count __",a+"");
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    api_key = c1.getString(c1.getColumnIndex("api_key"));
                    Log.e("api_key",api_key+"");

                }while (c1.moveToNext());
            }
        }
        c1.close();
    }




}



