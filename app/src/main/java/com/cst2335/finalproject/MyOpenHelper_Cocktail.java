package com.cst2335.finalproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * this is a class of database opener, it will defines methods which performs some database actions
 */
public class MyOpenHelper_Cocktail extends SQLiteOpenHelper {
    /**
     * These variables are the constants defined for use in this class.
     * they are all related to the database
     */
    public static final String FILENAME = "MyDatabase";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "MyData";
    public static final String COL_ID = "_id";
    public static final String COL_Name = "Name";
    public static final String COL_Pic = "Pic";
    public static final String COL_Instruction = "Instruction";
    public static final String COL_Ingredient1 = "Ingredient1";
    public static final String COL_Ingredient2 = "Ingredient2";
    public static final String COL_Ingredient3 = "Ingredient3";

    /**
     * constructor
     * @param ctx is a context
     */
    public MyOpenHelper_Cocktail(Context ctx) {
        super(ctx, FILENAME, null, VERSION_NUM);
    }

    /**
     * this function gets called if there is no database file exists.
     * @param db is the database connection
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("Create table %s(%s integer primary key autoincrement, %s text,  %s text, %s text, %s text, %s text, %s text);"
                ,TABLE_NAME, COL_ID, COL_Name, COL_Pic, COL_Instruction, COL_Ingredient1, COL_Ingredient2, COL_Ingredient3));
    }

    /**
     * this function gets called if the database version on your device is lower than VERSION_NUM
     * @param db is database connection
     * @param oldVersion is the old database version
     * @param newVersion is the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        this.onCreate(db);
    }
}
