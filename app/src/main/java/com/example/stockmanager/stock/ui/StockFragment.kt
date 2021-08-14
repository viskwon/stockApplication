package com.example.stockmanager.stock.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmanager.R
import com.example.stockmanager.domain.Stock
import com.example.stockmanager.viewmodel.StockViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout

class StockFragment : Fragment() {
    lateinit var viewModel: StockViewModel
    val actionMode = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.customView = View.inflate(context, R.layout.actionbar_selectall, null)

            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {

          //  (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            return false

        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false

        }

        override fun onDestroyActionMode(mode: ActionMode?) {
          //  (activity as AppCompatActivity?)!!.supportActionBar!!.show()


        }

    }


    private fun initActionBar(view: View) {
        val activity = (activity as AppCompatActivity)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val collasingtoolbar = view.findViewById<CollapsingToolbarLayout>(R.id.collasingtoolbar)
        activity.setSupportActionBar(toolbar)

        activity.supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.stock_list_layout, container, false)
        viewModel = ViewModelProvider(this).get(StockViewModel::class.java)

        initActionBar(view)
        val recyclerView = view.findViewById<RecyclerView>(R.id.stock_recycler_view)
        val adapter = StockAdapter(object : StockAdapter.ViewHolderOnItemClickListener {
            override fun onItemClick() {
                (activity as AppCompatActivity).startSupportActionMode(actionMode)

            }

            override fun onItemLongClick() {

                (activity as AppCompatActivity).startSupportActionMode(actionMode)

            }
        })
        recyclerView.adapter = adapter

        viewModel.stockLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("hjskwon", "2222")
            adapter.stockDatas = it
        })


        return view
    }


    class StockAdapter(val listenerViewHolder: ViewHolderOnItemClickListener) :
        RecyclerView.Adapter<StockAdapter.stockViewHolder>() {

        interface ViewHolderOnItemClickListener {

            fun onItemClick()
            fun onItemLongClick()
        }

        var stockDatas = listOf<Stock>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): stockViewHolder {
            val viewGroup = LayoutInflater.from(parent.context)
                .inflate(R.layout.stock_list_item, parent, false)
            return stockViewHolder(viewGroup, listenerViewHolder)
        }

        override fun getItemCount(): Int {
            return stockDatas.size
        }

        override fun onBindViewHolder(holder: stockViewHolder, position: Int) {
            holder.bind(stockDatas.get(position))
        }

        class stockViewHolder(v: View, val listenerViewHolder: ViewHolderOnItemClickListener) :
            RecyclerView.ViewHolder(v) {

            val nameText = itemView.findViewById<TextView>(R.id.stock_name)
            val currentPriceText = itemView.findViewById<TextView>(R.id.stock_currnet_price)
            val tradeVolume = itemView.findViewById<TextView>(R.id.stock_currnet_trade_volume)


            fun bind(stockInfo: Stock) {
                itemView.setOnLongClickListener {

                    Log.d("hjskwon", "bind")
                    listenerViewHolder.onItemLongClick()
/*
                    (activity as AppCompatActivity).startSupportActionMode(actionMode)*/
                    true
                }
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