package com.example.myapplication.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EditActivity
import com.example.myapplication.R

class MyAdapter(ListMain:ArrayList<ListItem>, contextM:Context) : RecyclerView.Adapter<MyAdapter.Myholder>() {
    var listArray = ListMain
    var context = contextM


    class Myholder(itemView: View,contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val context = contextV

        fun setData(item:ListItem){

            tvTitle.text = item.title
            itemView.setOnClickListener{

                val intent = Intent(context,EditActivity::class.java).apply {

                    putExtra(MyIntentConstants.I_TITLE_KEY,item.title)
                    putExtra(MyIntentConstants.I_DATE_KEY,item.date)
                    putExtra(MyIntentConstants.I_ADRES_KEY,item.adress)
                    putExtra(MyIntentConstants.I_DESC_KEY,item.content)
                    putExtra(MyIntentConstants.I_ID_KEY,item.id)

                }
                context.startActivity(intent)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
       val inflater = LayoutInflater.from(parent.context)
        return Myholder(inflater.inflate(R.layout.rc_item,parent,false),context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: Myholder, position: Int) {
        holder.setData(listArray.get(position))
    }

    fun updateAdapter(listItems:List<ListItem>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()

    }

    fun removeItem(pos:Int,dbManager: MyDbManager){
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(pos)
    }
}