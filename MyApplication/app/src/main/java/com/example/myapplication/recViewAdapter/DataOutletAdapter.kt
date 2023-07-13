package com.example.myapplication.recViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.Outlet

class DataOutletAdapter(private var dataList: List<Outlet>) :
    RecyclerView.Adapter<DataOutletAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daftar_nama_outlet, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.outletName.text = data.outletName
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val outletName: TextView = itemView.findViewById(R.id.IDNO_tv_nama_outlet)
    }

    fun setData(newDataList: List<Outlet>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}