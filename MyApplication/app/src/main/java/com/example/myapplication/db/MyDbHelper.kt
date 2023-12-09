package com.example.myapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MyDbHelper(context : Context,DATABASE_NAME : String) : SQLiteOpenHelper(context, DATABASE_NAME, null, MyDbNameClass.DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(MyDbNameClass.CREATE_TABLE)  //MyDbNameClass.CREATE_TABLE
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE) //MyDbNameClass.SQL_DELETE_TABLE
        onCreate(db)
    }

/*
    fun MyDbCreateString(TABLE_NAME : String = "my_table") : String{
        return "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" + MyDbNameClass.CREATE_TABLE
    }

    fun MyDbCDeleteString(TABLE_NAME : String = "my_table") : String{
        return "DROP TABLE IF EXISTS $TABLE_NAME"
    }
*/

}