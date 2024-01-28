package com.example.sennovations.newabis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sennovations on 02-06-2017.
 */

public class SqlDbHelper extends SQLiteOpenHelper {

    public static final String LOGIN = "LOGIN";
    public static final String INTEREST = "INTEREST";
    public static final String PAYOUTTYPE_FD = "PAYOUTTYPE_FD";
    public static final String TENURE= "TENURE";
    public static final String COMPOUNDING_FD = "COMPOUNDING_FD";
    public static final String frequency_of_invest = "frequency_of_invest";
    public static final String emi_type = "emi_type";
    public static final String age_group = "age_group";
    public static final String asset_group = "asset_group";
    public static final String riskprofile = "riskprofile";
    public static final String API_KEY = "API_KEY";
    public static final String profile = "profile";
    public static final String taxable_inc = "taxable_inc";
    public static final String age_pattern = "age_pattern";


//----------------------Login--------------------------

    public static final String COLUMN1 = "id";
    public static final String COLUMN2 = "email";
    public static final String COLUMN3 = "name";
    public static final String COLUMN4 = "password";
    public static final String COLUMN5 = "mobile";

    private static final String SCRIPT_CREATE_DATABASE1 = "create table " + LOGIN + " (" + COLUMN1 + " integer primary key autoincrement, " + COLUMN2 + " text not null unique, " + COLUMN3 + " text not null , " + COLUMN4 + " text not null, " + COLUMN5 + " text not null);";

    //----------------------INTEREST_Rate--------------------

    public static final  String id = "id";
    public static final  String type = "type";
    public static final  String min_int_rate = "min_int_rate";
    public static final  String max_int_rate = "max_int_rate";
    public static final  String int_rate_default = "int_rate_default";

    private static final String SCRIPT_CREATE_DATABASE2 = "create table " + INTEREST + " (" + id + " integer primary key autoincrement, " + type + " text not null unique, " + min_int_rate + " text not null , " + max_int_rate + " text not null, " + int_rate_default + " text not null);";

    //----------------------PAYOUTTYPE_FD--------------------------
    public static final  String pay_fd_id = "pay_fd_id";
    public static final  String pay_fd_name = "pay_fd_name";
    public static final  String pay_fd_DESCRIPTION = "pay_fd_DESCRIPTION";
    public static final  String pay_fd_more = "pay_fd_more";
    public static final  String pay_fd_value = "pay_fd_value";

    private static final String SCRIPT_CREATE_DATABASE3 = "create table " + PAYOUTTYPE_FD + " (" + pay_fd_id + " integer primary key autoincrement, " + pay_fd_name + " text not null unique," + pay_fd_DESCRIPTION + " text not null unique," + pay_fd_more + " text not null , " + pay_fd_value + " text not null);";

    //-----------------TENURE--------------------------------------

    public static final  String tenure_id = "tenure_id";
    public static final  String tenure_type = "tenure_type";
    public static final  String min_tenure = "min_tenure";
    public static final  String max_tenure = "max_tenure";
    public static final  String tenure_default = "tenure_default";

    private static final String SCRIPT_CREATE_DATABASE4 = "create table " + TENURE + " (" + tenure_id + " integer primary key autoincrement, " + tenure_type + " text not null unique, " + min_tenure + " text , " + max_tenure + " text , " + tenure_default + " text);";

    //-------------COMPOUNDING_FD-------------------------

    public static final  String com_fd_id = "com_fd_id";
    public static final  String com_fd_name = "com_fd_name";
    public static final  String com_fd_des = "com_fd_des";
    public static final  String com_fd_more = "com_fd_more";
    public static final  String com_fd_value = "com_fd_value";

    private static final String SCRIPT_CREATE_DATABASE5 = "create table " + COMPOUNDING_FD + " (" + com_fd_id + " integer primary key autoincrement, " + com_fd_name + " text not null unique, " + com_fd_des + " text not null unique, " + com_fd_more + " text not null , " + com_fd_value + " text not null);";

    //--------------------frequency_of_invest-----------------

    public static final  String fre_req_id = "fre_req_id";
    public static final  String fre_req_name = "fre_req_name";
    public static final  String fre_req_more = "fre_req_more";
    public static final  String fre_req_value = "fre_req_value";

    private static final String SCRIPT_CREATE_DATABASE6 = "create table " + frequency_of_invest + " (" + fre_req_id + " integer primary key autoincrement, " + fre_req_name + " text not null unique, " + fre_req_more + " text not null , " + fre_req_value + " text not null);";

    //----------------------emi_type--------------------------
    public static final  String emi_type_id = "emi_type_id";
    public static final  String emi_type_NAME = "emi_type_NAME";
    public static final  String emi_type_DESCRIPTION = "emi_type_DESCRIPTION";
    public static final  String emi_type_VALUE = "emi_type_VALUE";

    private static final String SCRIPT_CREATE_DATABASE7 = "create table " + emi_type + " (" + emi_type_id + " integer primary key autoincrement, " + emi_type_NAME + " text not null unique, " + emi_type_DESCRIPTION + " text not null , " + emi_type_VALUE + " text not null);";

    //-----------------age_group---------------------------------------
    public static final  String age_group_id = "age_group_id";
    public static final  String age_group_NAME = "age_group_NAME";
    public static final  String age_group_DESCRIPTION = "age_group_DESCRIPTION";
    public static final  String age_group_MORE= "age_group_MORE";
    public static final  String age_group_VALUE = "age_group_VALUE";

    private static final String SCRIPT_CREATE_DATABASE8 = "create table " + age_group + " (" + age_group_id + " integer primary key autoincrement, " + age_group_NAME + " text not null unique, " + age_group_DESCRIPTION + " text not null ," + age_group_MORE + " text not null , " + age_group_VALUE + " text not null);";

    //-----------------------asset_group---------------------------------------
    public static final  String asset_group_id = "asset_group_id";
    public static final  String asset_group_NAME = "asset_group_NAME";
    public static final  String asset_group_DESCRIPTION = "asset_group_DESCRIPTION";
    public static final  String asset_group_MORE= "asset_group_MORE";
    public static final  String asset_group_VALUE = "asset_group_VALUE";

    private static final String SCRIPT_CREATE_DATABASE9 = "create table " + asset_group + " (" + asset_group_id + " integer primary key autoincrement, " + asset_group_NAME + " text not null unique, " + asset_group_DESCRIPTION + " text not null ," + asset_group_MORE + " text not null , " + asset_group_VALUE + " text not null);";

    //---------------------------riskprofile------------------------------
        public static final  String riskprofile_id = "riskprofile_id";
        public static final  String riskprofile_CATEGORY = "riskprofile_CATEGORY";
        public static final  String riskprofile_DESCRIPTION = "riskprofile_DESCRIPTION";
        public static final  String riskprofile_MORE= "riskprofile_MORE";
        public static final  String riskprofile_VALUE = "riskprofile_VALUE";
        public static final  String riskprofile_MIN_RANGE = "riskprofile_MIN_RANGE";
        public static final  String riskprofile_MAX_RANGE = "riskprofile_MAX_RANGE";

        private static final String SCRIPT_CREATE_DATABASE10 = "create table " + riskprofile + " (" + riskprofile_id + " integer primary key autoincrement, " + riskprofile_CATEGORY + " text not null unique, " + riskprofile_DESCRIPTION + " text not null ," + riskprofile_MORE + " text not null , " + riskprofile_VALUE + " text not null, " + riskprofile_MIN_RANGE + " text not null, " + riskprofile_MAX_RANGE + " text not null);";
//------------------------API_KEY----------------------------------
    public static final  String api_key_id = "api_key_id";
    public static final  String api_key_email = "api_key_email";
    public static final  String api_key = "api_key";

    private static final String SCRIPT_CREATE_DATABASE11 = "create table " + API_KEY + " (" + api_key_id + " integer primary key autoincrement, " + api_key_email + " text not null unique, " + api_key + " text not null unique);";

//-------------------profile-----------------------------------
    public static final  String pro_id = "pro_id";
    public static final  String pro_email = "pro_email";
    public static final  String pro_age = "pro_age";
    public static final  String pro_age_group= "pro_age_group";
    public static final  String pro_tax_age_group = "pro_tax_age_group";
    public static final  String pro_moblie = "pro_moblie";
    public static final  String pro_state = "pro_state";
    public static final  String pro_city = "pro_city";
    public static final  String pro_country= "pro_country";
    public static final  String pro_pin = "pro_pin";


    private static final String SCRIPT_CREATE_DATABASE12 = "create table " + profile + " (" + pro_id + " integer primary key autoincrement, " + pro_email + " text not null unique, " + pro_age + " text not null ," + pro_age_group+ " text not null , " + pro_tax_age_group + " text not null , " + pro_moblie + " text not null, " + pro_state + " text not null, " + pro_country + " text not null, " + pro_city + " text not null, " + pro_pin + " text not null);";
//-------------------taxable_inc-----------------------------------
    public static final  String taxable_inc_id = "taxable_inc_id";
    public static final  String taxable_inc_NAME = "taxable_inc_NAME";
    public static final  String taxable_inc_DESCRIPTION = "taxable_inc_DESCRIPTION";
    public static final  String taxable_inc_MORE= "taxable_inc_MORE";

    private static final String SCRIPT_CREATE_DATABASE13 = "create table " + taxable_inc + " (" + taxable_inc_id + " integer primary key autoincrement, " + taxable_inc_NAME + " text not null unique, " + taxable_inc_DESCRIPTION + " text not null ," + taxable_inc_MORE + " text not null);";

    //----------------------age_pattern-------------------------------
    public static final String age_pattern_id = "age_pattern_id";
    public static final String age_pattern_email = "age_pattern_email";
    public static final String age_pattern_api = "age_pattern_api";
    public static final String age_pattern_age_group = "age_pattern_age_group";
    public static final String age_pattern_tax_age_group = "age_pattern_tax_age_group";

    private static final String SCRIPT_CREATE_DATABASE14 = "create table " + age_pattern + " (" + age_pattern_id + " integer primary key autoincrement, " + age_pattern_email + " text not null unique, " + age_pattern_api + " text not null ," + age_pattern_age_group + " text not null," + age_pattern_tax_age_group + " text not null);";


    public SqlDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.i("", "Inside SqlDBHelper ");
        Log.d("ABIS LOGIN TABLE CREATED .....", SCRIPT_CREATE_DATABASE1);
        Log.d("ABIS Interest_rate TABLE CREATED .....", SCRIPT_CREATE_DATABASE2);
        Log.d("ABIS PAYOUTTYPE_FD TABLE CREATED .....", SCRIPT_CREATE_DATABASE3);
        Log.d("ABIS TENURE TABLE CREATED .....", SCRIPT_CREATE_DATABASE4);
        Log.d("ABIS COMPOUNDING_FD TABLE CREATED .....", SCRIPT_CREATE_DATABASE5);
        Log.d("ABIS frequency_of_invest TABLE CREATED .....", SCRIPT_CREATE_DATABASE6);
        Log.d("ABIS emi_type TABLE CREATED .....", SCRIPT_CREATE_DATABASE7);
        Log.d("ABIS age_group TABLE CREATED .....", SCRIPT_CREATE_DATABASE8);
        Log.d("ABIS asset_group TABLE CREATED .....", SCRIPT_CREATE_DATABASE9);
        Log.d("ABIS riskprofile TABLE CREATED .....", SCRIPT_CREATE_DATABASE10);
        Log.d("ABIS API_KEY TABLE CREATED .....", SCRIPT_CREATE_DATABASE11);
        Log.d("ABIS profile TABLE CREATED .....", SCRIPT_CREATE_DATABASE12);
        Log.d("ABIS taxable_inc TABLE CREATED .....", SCRIPT_CREATE_DATABASE13);
        Log.d("ABIS age_pattern  TABLE CREATED .....", SCRIPT_CREATE_DATABASE14);



    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_DATABASE1);
        db.execSQL(SCRIPT_CREATE_DATABASE2);
        db.execSQL(SCRIPT_CREATE_DATABASE3);
        db.execSQL(SCRIPT_CREATE_DATABASE4);
        db.execSQL(SCRIPT_CREATE_DATABASE5);
        db.execSQL(SCRIPT_CREATE_DATABASE6);
        db.execSQL(SCRIPT_CREATE_DATABASE7);
        db.execSQL(SCRIPT_CREATE_DATABASE8);
        db.execSQL(SCRIPT_CREATE_DATABASE9);
        db.execSQL(SCRIPT_CREATE_DATABASE10);
        db.execSQL(SCRIPT_CREATE_DATABASE11);
        db.execSQL(SCRIPT_CREATE_DATABASE12);
        db.execSQL(SCRIPT_CREATE_DATABASE13);
        db.execSQL(SCRIPT_CREATE_DATABASE14);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + INTEREST);
        db.execSQL("DROP TABLE IF EXISTS " + PAYOUTTYPE_FD);
        db.execSQL("DROP TABLE IF EXISTS " + TENURE);
        db.execSQL("DROP TABLE IF EXISTS " + COMPOUNDING_FD);
        db.execSQL("DROP TABLE IF EXISTS " + frequency_of_invest);
        db.execSQL("DROP TABLE IF EXISTS " + emi_type);
        db.execSQL("DROP TABLE IF EXISTS " + age_group);
        db.execSQL("DROP TABLE IF EXISTS " + asset_group);
        db.execSQL("DROP TABLE IF EXISTS " + riskprofile);
        db.execSQL("DROP TABLE IF EXISTS " + API_KEY);
        db.execSQL("DROP TABLE IF EXISTS " + profile);
        db.execSQL("DROP TABLE IF EXISTS " + taxable_inc);
        db.execSQL("DROP TABLE IF EXISTS " + age_pattern);



        onCreate(db);
    }
}