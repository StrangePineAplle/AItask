package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.myapplication.db.MyDbManager
import com.example.myapplication.db.MyIntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private val myDbManager = MyDbManager(this)
    var id = 0
    var isEditState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        getMyIntents()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()

    }

    override fun onPause() {
        super.onPause()
        myDbManager.closeDb()
    }

    fun onClickSave(view: View) {
        val edTitle = findViewById<TextView>(R.id.edTitle)
        val edDate = findViewById<TextView>(R.id.edDate)
        val edAdres = findViewById<TextView>(R.id.edAdres)
        val edDirection = findViewById<TextView>(R.id.edDirection)

        val myTitle = edTitle.text.toString()
        val myDate = edDate.text.toString()
        val myAdres = edAdres.text.toString()
        val myDirection = edDirection.text.toString()

        if (myTitle != "") {
        CoroutineScope(Dispatchers.Main).launch {
                if (isEditState) {
                    myDbManager.updateItem(myTitle, myAdres, myDate, myDirection, id)
                } else {
                    myDbManager.incertToDb(myTitle, myAdres, myDate, myDirection)
                }
                finish()
            }

        }
    }

    fun getMyIntents(){

        val i = intent

        if (i != null){

            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){

                val edTitle = findViewById<TextView>(R.id.edTitle)
                val edDate = findViewById<TextView>(R.id.edDate)
                val edAdres = findViewById<TextView>(R.id.edAdres)
                val edDirection = findViewById<TextView>(R.id.edDirection)

                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                edDate.setText(i.getStringExtra(MyIntentConstants.I_DATE_KEY))
                edAdres.setText(i.getStringExtra(MyIntentConstants.I_ADRES_KEY))
                edDirection.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                isEditState = true

            }

        }

    }
}