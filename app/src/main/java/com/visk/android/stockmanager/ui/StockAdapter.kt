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
import com.visk.android.stockmanager.domain.Stock


class StockAdapter(val listenerViewHolder: ViewHolderOnItemClickListener) :
    RecyclerView.Adapter<StockAdapter.stockViewHolder>() {

    private val mDiffer: AsyncListDiffer<Stock> = AsyncListDiffer(this, StockListDiffCallback())


    interface ViewHolderOnItemClickListener {

        fun onItemClick()
        fun onItemLongClick()
    }

    fun setData(list : List<Stock>){
        mDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): stockViewHolder {
        val viewGroup = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_list_item, parent, false)
        return stockViewHolder(viewGroup, listenerViewHolder)
    }

    override fun getItemCount(): Int {

        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: stockViewHolder, position: Int) {
        holder.bind(mDiffer.currentList.get(position))
    }

    class stockViewHolder(v: View, val listenerViewHolder: ViewHolderOnItemClickListener) :
        RecyclerView.ViewHolder(v) {

        val nameText = itemView.findViewById<TextView>(R.id.stock_name)
        val currentPriceText = itemView.findViewById<TextView>(R.id.stock_currnet_price)
        val tradeVolume = itemView.findViewById<TextView>(R.id.stock_currnet_trade_volume)


        fun bind(stockInfo: Stock) {
            Log.d("hjskwon", stockInfo.name)
            itemView.setOnLongClickListener {
                listenerViewHolder.onItemLongClick()
                true
            }
            nameText.text = stockInfo.name
            currentPriceText.text = stockInfo.currentPrice.toString()
            tradeVolume.text = stockInfo.updateTime
        }

    }

    class StockListDiffCallback :
        DiffUtil.ItemCallback<Stock>() { // DiffUtil use these method to acquire datas are changed.

        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem == newItem
        }

    }
}