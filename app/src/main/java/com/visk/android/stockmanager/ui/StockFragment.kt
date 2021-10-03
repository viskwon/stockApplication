package com.visk.android.stockmanager.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AlertDialogLayout
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
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.stock_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val editText = EditText(context).apply {  }
        AlertDialog.Builder(requireActivity()).setTitle("주식 ID 를 입력하세요").setView(editText).setPositiveButton("ok",
            { dialogInterface, i ->

            }).setNegativeButton("cancel",null).create().show()
        return super.onOptionsItemSelected(item)
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