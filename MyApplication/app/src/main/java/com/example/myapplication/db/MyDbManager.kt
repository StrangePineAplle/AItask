package com.example.myapplication.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(val context: Context) {
    val myDbChangeManager  = MyDbChangeManager("")
    var DATABASE_NAME = myDbChangeManager.DATABASE_NAME

    val myDbHelper = MyDbHelper(context,DATABASE_NAME)
    var db : SQLiteDatabase? = null
    fun openDb(){
        db = myDbHelper.writableDatabase
    }
    suspend fun incertToDb(title : String, adress : String, date : String, content : String) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_ADRESS, adress)
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null, values)
    }

    fun removeItemFromDb(id : String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME,selection, null)

    }

    suspend fun updateItem(title : String, adress : String, date : String, content : String, id:Int) = withContext(Dispatchers.IO) {
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_ADRESS, adress)
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)

    }

    @SuppressLint("Range")
    suspend fun readDbData(searchText : String = "") : ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, selection, arrayOf("%$searchText%"), null, null, null)


        while (cursor?.moveToNext()!!){
            val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val dataText = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
            val dataDate = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE))
            val dataAdres = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_ADRESS))
            val dataContent = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
            val item = ListItem()
            item.id = dataId
            item.title = dataText
            item.date = dataDate
            item.adress = dataAdres
            item.content = dataContent

            dataList.add(item)
        }
        cursor.close()
        return@withContext dataList
    }

    fun closeDb(){
        myDbHelper.close()
    }

}