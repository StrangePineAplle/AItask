package com.example.myapplication

import android.annotation.SuppressLint
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
import androidx.core.view.GravityCompat
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)
    private var job :Job? = null
    private var job1 :Job? = null ///

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {


            openMenu.setOnClickListener{
                Drawer.openDrawer(GravityCompat.START)
            }

        }

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

    fun onClickCloseMenu(view: View){
        binding.Drawer.closeDrawer(GravityCompat.START)
    }


    private fun init(){
        val rcViev : RecyclerView = findViewById(R.id.rcViev)
        rcViev.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcViev)
        rcViev.adapter = myAdapter
    }

    private fun initSearchView(){
        binding.searchV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fillAdapter(newText!!)
                return true
            }

        })


    }

    private fun fillAdapter(text : String = ""){

        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch{

            val list = myDbManager.readDbData(text)
            myAdapter.updateAdapter(list)
            if (list.size > 0 ){
                binding.tvNoElements.visibility = View.GONE

            }
            else {
                val tvNoElement = findViewById<TextView>(R.id.tvNoElements)
                tvNoElement.visibility = View.VISIBLE
            }

        }
    }

    @SuppressLint("SetTextI18n")
    fun NumberOfElements(){
        job1?.cancel()
        job1 = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDbData()
            val len = list.size
            binding.tvCount.text = "$len elements"
        }
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