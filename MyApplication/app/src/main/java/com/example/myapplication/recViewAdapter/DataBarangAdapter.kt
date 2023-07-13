package com.example.myapplication.recViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.Goods

class DataBarangAdapter(private var dataList: List<Goods>) :
    RecyclerView.Adapter<DataBarangAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daftar_barang, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val data = dataList[position]
            holder.productName.text = data.goodsName
            holder.productPrice.text = data.goodsPrice.toString()
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val productName: TextView = itemView.findViewById(R.id.IDB_tv_nama_barang)
            val productPrice: TextView = itemView.findViewById(R.id.IDB_tv_harga_barang)
        }

        fun setData(newDataList: List<Goods>) {
            dataList = newDataList
            notifyDataSetChanged()
        }
    }