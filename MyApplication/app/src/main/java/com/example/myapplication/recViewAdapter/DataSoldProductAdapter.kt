package com.example.myapplication.recViewAdapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.Goods
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DataSoldProductAdapter(private var dataList: List<Goods>) :
    RecyclerView.Adapter<DataSoldProductAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report_goods_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.productSold.text = Editable.Factory.getInstance().newEditable(data.goodsPrice.toString())
        holder.productHint.hint = data.goodsName
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productSold: TextInputEditText = itemView.findViewById(R.id.IRGL_tiet_product_sold)
        val productHint: TextInputLayout = itemView.findViewById(R.id.IRGL_til_product_name_hint)
    }

    fun setData(newDataList: List<Goods>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}