package com.visk.android.stockmanager.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.domain.MyStock
import com.visk.android.stockmanager.domain.Stock


class MyStockAdapter() :
    RecyclerView.Adapter<MyStockAdapter.MyStockViewHolder>() {

    private val mDiffer: AsyncListDiffer<MyStock> = AsyncListDiffer(this, StockListDiffCallback())


    interface ViewHolderOnItemClickListener {

        fun onItemClick()
        fun onItemLongClick()
    }

    fun setData(list : List<MyStock>){
        mDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStockViewHolder {
        val viewGroup = LayoutInflater.from(parent.context)
            .inflate(R.layout.mystock_list_item, parent, false)
        return MyStockViewHolder(viewGroup)
    }

    override fun getItemCount(): Int {

        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: MyStockViewHolder, position: Int) {
        holder.bind(mDiffer.currentList.get(position))
    }

    class MyStockViewHolder(v: View) :
        RecyclerView.ViewHolder(v) {

        val nameText = itemView.findViewById<TextView>(R.id.mystock_name)
        val currentPriceText = itemView.findViewById<TextView>(R.id.mystock_currnet_price)
        val buyPrice = itemView.findViewById<TextView>(R.id.mystock_byprice)
        val volmun = itemView.findViewById<TextView>(R.id.mystock_volumn)
        val total = itemView.findViewById<TextView>(R.id.mystock_total)


        fun bind(myStock: MyStock) {
            nameText.text = myStock.name
            currentPriceText.text = myStock.currentPrice.toString()
            buyPrice.text = myStock.buyPrice.toString()
            volmun.text = myStock.volume.toString()
            total.text = (myStock.buyPrice * myStock.volume).toString()
        }

    }

    class StockListDiffCallback :
        DiffUtil.ItemCallback<MyStock>() { // DiffUtil use these method to acquire datas are changed.

        override fun areItemsTheSame(oldItem: MyStock, newItem: MyStock): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MyStock, newItem: MyStock): Boolean {
            return oldItem == newItem
        }

    }
}