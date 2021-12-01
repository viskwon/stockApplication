package com.visk.android.stockmanager.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AlertDialogLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.visk.android.stockmanager.viewmodel.StockViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.visk.android.stockmanager.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockFragment : Fragment() {
    val viewModel: StockViewModel by viewModels()

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
                viewModel.addStock(editText.text.toString())
            }).setNegativeButton("cancel",null).create().show()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.stock_list_layout, container, false)

        initActionBar(view)
        val recyclerView = view.findViewById<RecyclerView>(R.id.stock_recycler_view)
        val adapter = StockAdapter(object : StockAdapter.ViewHolderOnItemClickListener {
            override fun onItemClick() {
            }

            override fun onItemLongClick() {
            }
        })
        recyclerView.adapter = adapter
        recyclerView.recycledViewPool.setMaxRecycledViews(0,0)

        viewModel.stockLiveData.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
        viewModel.autoRefresh()
        WorkManager.getInstance(requireContext()).getWorkInfosForUniqueWork("periodic").get().forEach {
            Log.d("hjskwon" ,"workmanager ${it.state} ${it.progress}")
        }
        return view
    }


    override fun onResume() {
        super.onResume()
        viewModel.getStockInfo()
    }
}