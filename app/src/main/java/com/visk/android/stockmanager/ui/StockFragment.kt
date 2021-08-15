package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.visk.android.stockmanager.viewmodel.StockViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.visk.android.stockmanager.R

class StockFragment : Fragment() {
    lateinit var viewModel: StockViewModel


    private fun initActionBar(view: View) {
        val activity = (activity as AppCompatActivity)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val collasingtoolbar = view.findViewById<CollapsingToolbarLayout>(R.id.collasingtoolbar)
        activity.setSupportActionBar(toolbar)

        activity.supportActionBar?.apply {
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
            }

            override fun onItemLongClick() {
            }
        })
        recyclerView.adapter = adapter

        viewModel.stockLiveData.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
        viewModel.autoRefresh()
        return view
    }


    override fun onResume() {
        super.onResume()
        viewModel.getStockInfo()
    }
}