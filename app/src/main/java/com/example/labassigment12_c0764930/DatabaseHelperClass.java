package com.example.labassigment12_c0764930;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class  DatabaseHelperClass extends SQLiteOpenHelper {

    //using Constant for column names

    private static  final String DATABASE_NAME = "NearbyPlaces";

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "places";
    private static final String  COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LONG = "longitude";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_DATE = "date";

    public DatabaseHelperClass(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER NOT NULL CONSTRAINT place_pk PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " varchar(200),"+
                COLUMN_ADDRESS + " varchar(200) , " +
                COLUMN_DATE + " varchar(200) NOT NULL," +
                COLUMN_LAT + " double NOT NULL, " +
                COLUMN_LONG + " double NOT NULL);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }


    boolean savePlaces(String name, String address, double latitude, double longitude,String date) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_ADDRESS,address);
        contentValues.put(COLUMN_LAT,latitude);
        contentValues.put(COLUMN_LONG,longitude);
        contentValues.put(COLUMN_DATE,date);

        return  sqLiteDatabase.insert(TABLE_NAME,null,contentValues)!= -1;

    }

    Cursor getPlaces(){
        SQLiteDatabase sqLiteDatabase =getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);

    }
    boolean updatePlaces(int id,String address, String nameoffavrtplace, double latitude, double longitude){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,nameoffavrtplace);
        contentValues.put(COLUMN_ADDRESS,address);
        contentValues.put(COLUMN_LAT,latitude);
        contentValues.put(COLUMN_LONG,longitude);

        return  sqLiteDatabase.update(TABLE_NAME,contentValues,COLUMN_ID+" = ? ",new String[]{String.valueOf(id)}) >0 ;
    }

    boolean deletePlaces(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return  sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID+" = ? ",new String[]{String.valueOf(id)}) >0;

    }



}

