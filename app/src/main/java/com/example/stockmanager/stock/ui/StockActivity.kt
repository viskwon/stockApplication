package com.example.stockmanager.stock.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmanager.R
import com.example.stockmanager.domain.Stock
import com.example.stockmanager.viewmodel.StockViewModel


class StockActivity : AppCompatActivity() {
    lateinit var viewModel: StockViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_list_layout)
        viewModel = ViewModelProvider(this).get(StockViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.stock_recycler_view)
        val adapter = StockAdapter()
        recyclerView.adapter = adapter
        viewModel.stockLiveData.observe(this, Observer {
            Log.d("hjswon", "2222")
            adapter.stockDatas = it
        })
    }


    class StockAdapter : RecyclerView.Adapter<StockAdapter.stockViewHolder>() {
        var stockDatas = listOf<Stock>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): stockViewHolder {
            val viewGroup = LayoutInflater.from(parent.context)
                .inflate(R.layout.stock_list_item, parent, false)
            return stockViewHolder(viewGroup)
        }

        override fun getItemCount(): Int {
            return stockDatas.size
        }

        override fun onBindViewHolder(holder: stockViewHolder, position: Int) {
            holder.bind(stockDatas.get(position))
        }

        class stockViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            val nameText = itemView.findViewById<TextView>(R.id.stock_name)
            val currentPriceText = itemView.findViewById<TextView>(R.id.stock_currnet_price)
            val tradeVolume = itemView.findViewById<TextView>(R.id.stock_currnet_trade_volume)


            fun bind(stockInfo: Stock) {
                nameText.text = stockInfo.name
                currentPriceText.text = stockInfo.currentPrice.toString()
                tradeVolume.text = stockInfo.tradeVolume.toString()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStockInfo()
    }
}

