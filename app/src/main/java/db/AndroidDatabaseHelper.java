
package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Atiar on 12/4/17.
 */


public class AndroidDatabaseHelper extends SQLiteOpenHelper {

    //switch for activate or deactivate for print log.d
    boolean status = true;

    public static final String DATABASE_NAME = "RM";
    public static final String TABLE_NAME = "rm_stop_list";
    public static final String RM_COLUMN_ID = "_id";
    public static final String RM_TRANSPORT_NAME = "transport_name";
    public static final String RM_BUS_STOP = "bus_stop";
    public static final String RM_LAT = "lat";
    public static final String RM_LANG = "lang";
    public static final String RM_BENGALI_NAME = "bn_name";

    public static final String TABLE1 = "bus_stops";
    public static final String TABLE1_ID = "_id";
    public static final String TABLE1_NAME = "name";

    public AndroidDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table  if not exists " +  TABLE_NAME+ " (" +
                RM_COLUMN_ID + " integer primary key AUTOINCREMENT NOT NULL,"+
                RM_TRANSPORT_NAME + " Text," +
                RM_BUS_STOP + " Text," +
                RM_LAT + " Text," +
                RM_LANG + " Text," +
                RM_BENGALI_NAME + " Text)"
        );

        db.execSQL("create table  if not exists " +  TABLE1+ " (" +
                TABLE1_ID + " integer primary key AUTOINCREMENT NOT NULL,"+
                TABLE1_NAME + " Text)"
        );

        //log.d generator
        Log.e("Atiar - ", TABLE_NAME+ " Created Successfully");
        Log.e("Atiar - ", TABLE1+ " Created Successfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        try{
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME);
            //log.d generator
            Log.e( "Atiar - ", TABLE_NAME+ " Upgraded from version" + i + " to version "+i1);

            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            //log.d generator
            Log.e("Atiar - ", TABLE_NAME+ " Upgraded from version" + i + " to version "+i1);

            db.execSQL("DROP TABLE IF EXISTS "+ TABLE1);
            //log.d generator
            Log.e("Atiar - ", TABLE1+ " Upgraded from version" + i + " to version "+i1);



        }catch (SQLException e){
            //log.d generator
            Log.e("Atiar - ", TABLE_NAME+ " Failled from version" + i + " to version "+i1);
        }finally {

            onCreate(db);

            Log.e("Atiar - ", "Tables created after onUpgrade");

        }

    }

    public boolean insertBusStopDetails(String transportName, String busStop, String lat, String lang, String bn_name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(RM_TRANSPORT_NAME, transportName);
        contentValues.put(RM_BUS_STOP, busStop);
        contentValues.put(RM_LAT, lat);
        contentValues.put(RM_LANG, lang);
        contentValues.put(RM_BENGALI_NAME, bn_name);

        db.insert(TABLE_NAME, null, contentValues);

        //Print log.d
        Log.e("Atiar - ", "Data Inserted to "+ TABLE_NAME + transportName + busStop + lat + lang + bn_name );

        return true;
    }

    public ArrayList<DataModel> getData() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<DataModel> busStopDetails= new ArrayList<DataModel>();

        Cursor result = db.rawQuery("select * from "+TABLE_NAME , null);

        while((result.moveToNext())){

            //car.add( new DataModel(result1.getString(result1.getColumnIndex(RM_COLUMN_NAME)), result1.getString(result1.getColumnIndex(RM_COLUMN_NUMBER)),result2.getString(result2.getColumnIndex(RM_COLUMN_NUMBER))));

            busStopDetails.add( new DataModel(
                    result.getString(result.getColumnIndex(RM_TRANSPORT_NAME)),
                    result.getString(result.getColumnIndex(RM_BUS_STOP)),
                    result.getString(result.getColumnIndex(RM_LAT)),
                    result.getString(result.getColumnIndex(RM_LANG)),
                    result.getString(result.getColumnIndex(RM_BENGALI_NAME))
                    ));

        }

        return busStopDetails;
    }

    public String getRmLat(String busStop) {
        //Print log.d
        Log.e("Atiar - ", "Command received to extract mobile number - " + busStop);

        SQLiteDatabase db = this.getReadableDatabase();

        String tempPostion ="";

        String lat = "";

        final Cursor cursorPositino = db.rawQuery("SELECT DISTINCT " + RM_LAT + " FROM " +TABLE_NAME+ " WHERE " + RM_BUS_STOP + " = " + busStop +";",null);
        /*SELECT DISTINCT `bus_id` from fare_list where from_place = "Airpoart" */

        cursorPositino.moveToFirst();

        if (cursorPositino != null) {

            try {
                    lat = cursorPositino.getString(0);

                    //Print log.d
                    Log.e("Atiar - ", "the latitude for " +busStop+ " = " + lat);

            } catch (Exception e){

                e.printStackTrace();

            } finally {

                cursorPositino.close();

            }
        }


        return lat;


/*
        SELECT Number,_id
        FROM CustomContactsB
        WHERE _id = (SELECT Position
                    FROM CustomCommandsB
                    WHERE Commands =Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor)) "Atiar ke call dao");

         */

    }

    public String getRmLang(String busStop) {
        //Print log.d
        Log.e("Atiar - ", "Command received to extract mobile number - " + busStop);

        SQLiteDatabase db = this.getReadableDatabase();

        String tempPostion ="";

        String lang = "";

        final Cursor cursorPositino = db.rawQuery("SELECT DISTINCT " + RM_LANG + " FROM " +TABLE_NAME+ " WHERE " + RM_BUS_STOP + " = " + busStop +";",null);
        /*SELECT DISTINCT `bus_id` from fare_list where from_place = "Airpoart" */

        cursorPositino.moveToFirst();

        if (cursorPositino != null) {

            try {
                    lang = cursorPositino.getString(0);

                    //Print log.d
                    Log.e("Atiar - ", "the longitude for " +busStop+ " = " + lang);

            } catch (Exception e){

                e.printStackTrace();

            } finally {

                cursorPositino.close();

            }
        }

        return lang;


/*
        SELECT Number,_id
        FROM CustomContactsB
        WHERE _id = (SELECT Position
                    FROM CustomCommandsB
                    WHERE Commands =Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor)) "Atiar ke call dao");

         */

    }

    public String getRmBengaliName(String busStop) {
        //Print log.d
        Log.e("Atiar - ", "Command received to extract mobile number - " + busStop);

        SQLiteDatabase db = this.getReadableDatabase();

        String tempPostion ="";

        String bnName = "";

        final Cursor cursorPositino = db.rawQuery("SELECT DISTINCT " + RM_BENGALI_NAME + " FROM " +TABLE_NAME+ " WHERE " + RM_BUS_STOP + " = " + busStop +";",null);
        /*SELECT DISTINCT `bus_id` from fare_list where from_place = "Airpoart" */

        cursorPositino.moveToFirst();

        if (cursorPositino != null) {

            try {
                    bnName = cursorPositino.getString(0);

                    //Print log.d
                    Log.e("Atiar - ", "the longitude for " +busStop+ " = " + bnName);

            } catch (Exception e){

                e.printStackTrace();

            } finally {

                cursorPositino.close();

            }
        }

        return bnName;

    }

    public boolean isBusStopContain(String busStop){
        SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursorPositino = db.rawQuery("SELECT DISTINCT " + RM_COLUMN_ID + " FROM " +TABLE_NAME+ " WHERE " + RM_BUS_STOP + " = " + busStop +";",null);

        if (cursorPositino != null)
            return true;

        return false;

    }


    public boolean insertBusStop(String busStop) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE1_NAME, busStop);

        //db.insert(TABLE1, null, contentValues);
        db.insertWithOnConflict(TABLE1, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        //Print log.d
        //Log.e("Atiar - ", "Data Inserted to "+ TABLE1  + busStop );

        return true;
    }


    public ArrayList<String> getAllBusStops() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> busStopDetails= new ArrayList<String>();

        Cursor result = db.rawQuery("select DISTINCT " +  TABLE1_NAME + " from " + TABLE1 + ";"  , null);

        while((result.moveToNext())){

            //car.add( new DataModel(result1.getString(result1.getColumnIndex(RM_COLUMN_NAME)), result1.getString(result1.getColumnIndex(RM_COLUMN_NUMBER)),result2.getString(result2.getColumnIndex(RM_COLUMN_NUMBER))));
            Log.e("stops - Atiar", result.getString(result.getColumnIndex(TABLE1_NAME)));

            busStopDetails.add(result.getString(result.getColumnIndex(TABLE1_NAME))
            );

        }

        return busStopDetails;
    }
}

