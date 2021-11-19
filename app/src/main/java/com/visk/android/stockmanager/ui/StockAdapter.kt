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
import com.visk.android.stockmanager.databinding.StockListItemBinding
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

        return stockViewHolder(StockListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false), listenerViewHolder)
    }

    override fun getItemCount(): Int {

        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: stockViewHolder, position: Int) {
        holder.bind(mDiffer.currentList.get(position))
    }

    class stockViewHolder(val binding: StockListItemBinding, val listenerViewHolder: ViewHolderOnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stockInfo: Stock) {
            binding.stock = stockInfo
            Log.d("hjskwon", stockInfo.name)
            itemView.setOnLongClickListener {
                listenerViewHolder.onItemLongClick()
                true
            }
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