package com.example.appsocialparcial;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by delaroy on 9/8/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "image.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;

    public final static String COLUMN_NAME = "imagename";

    public final static String TABLE_NAME = "projetos";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContentResolver = context.getContentResolver();

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
         //       _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
         //       COLUMN_NAME + " BLOB NOT NULL " + " );";

        final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, COLUMN_NAME BLOB NOT NULL, voluntario_id INTERGER)";

        db.execSQL(SQL_CREATE_IMAGE_TABLE);

        Log.d(TAG, "Database Created Successfully" );

    }

    public void addToDb(byte[] image){
        ContentValues cv = new  ContentValues();
        cv.put(COLUMN_NAME,   image);
        db.insert( TABLE_NAME, null, cv );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}

