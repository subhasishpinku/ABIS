package com.example.sennovations.newabis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Sennovations on 02-06-2017.
 */

public class SqlHandler {
    public static final String DATABASE_NAME = "MY_ABIS_DATABASE.db";
    public static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase sqlDatabase;
    SqlDbHelper dbHelper;

    public SqlHandler(Context context) {

        Log.i("","In sqlHandler - before sqlDBHelper");
        dbHelper = new SqlDbHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
        sqlDatabase = dbHelper.getWritableDatabase();
    }

    public void executeQuery(String query) {
        try {

            if (sqlDatabase.isOpen()) {
                sqlDatabase.close();
            }

            sqlDatabase = dbHelper.getWritableDatabase();
            sqlDatabase.execSQL(query);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR " + e);
        }
    }
    public Cursor selectQuery(String query) {
        Cursor c1 = null;
        try {

            if (sqlDatabase.isOpen()) {
                sqlDatabase.close();

            }
            sqlDatabase = dbHelper.getWritableDatabase();
            c1 = sqlDatabase.rawQuery(query, null);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR " + e);

        }
        return c1;

    }

}