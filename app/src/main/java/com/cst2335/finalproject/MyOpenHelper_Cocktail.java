package com.cst2335.finalproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper_Cocktail extends SQLiteOpenHelper {
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

    public MyOpenHelper_Cocktail(Context ctx) {
        super(ctx, FILENAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("Create table %s(%s integer primary key autoincrement, %s text,  %s text, %s text, %s text, %s text, %s text);"
                ,TABLE_NAME, COL_ID, COL_Name, COL_Pic, COL_Instruction, COL_Ingredient1, COL_Ingredient2, COL_Ingredient3));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        this.onCreate(db);
    }
}
