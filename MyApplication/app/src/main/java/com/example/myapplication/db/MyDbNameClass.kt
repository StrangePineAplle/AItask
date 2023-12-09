package com.example.myapplication.db

import android.provider.BaseColumns

object MyDbNameClass {
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_NAME_ADRESS = "adress"
    const val COLUMN_NAME_CONTENT = "content"

    const val DATABASE_VERSION = 3
    const val DATABASE_NAME = "MyDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY,$COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_CONTENT TEXT, $COLUMN_NAME_DATE TEXT, $COLUMN_NAME_ADRESS TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}