package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.MyAdapter
import com.example.myapplication.db.MyDbManager
import android.widget.SearchView
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
        NumberOfElements()
    }

    override fun onPause() {
        super.onPause()
        myDbManager.closeDb()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    private fun init(){
        val rcViev : RecyclerView = findViewById(R.id.rcViev)
        rcViev.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcViev)
        rcViev.adapter = myAdapter
    }

    private fun initSearchView(){
        val searchView : SearchView = findViewById(R.id.searchV)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = myDbManager.readDbData(newText!!)
                myAdapter.updateAdapter(list)
                return true
            }

        })


    }

    fun fillAdapter(){
        val list = myDbManager.readDbData()
        myAdapter.updateAdapter(list)
        if (list.size > 0 ){
            val tvNoElement = findViewById<TextView>(R.id.tvNoElements)
            tvNoElement.visibility = View.GONE
        }
        else {
          val tvNoElement = findViewById<TextView>(R.id.tvNoElements)
            tvNoElement.visibility = View.VISIBLE
        }

    }

    fun NumberOfElements(){
        val tvCount = findViewById<TextView>(R.id.tvCount)
        val list = myDbManager.readDbData()
        val len =  list.size
        tvCount.text = "$len elements"
    }

    private fun getSwapMg() : ItemTouchHelper{
        return ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
                NumberOfElements()
            }
        })
    }
}