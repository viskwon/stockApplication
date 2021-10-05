package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.databinding.MyStockFragmentBinding
import com.visk.android.stockmanager.viewmodel.MyStockViewModel
import kotlinx.coroutines.launch

class MyStockFragment : Fragment() {
    lateinit var binding: MyStockFragmentBinding
    lateinit var viewModel:MyStockViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyStockFragmentBinding.inflate(inflater)
        initActionBar(binding.toolbar)
        viewModel = ViewModelProvider(this).get(MyStockViewModel::class.java)
        val adapter = MyStockAdapter()
        binding.mystockRecyclerView.adapter = adapter

        viewModel.myStocksLive().observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        return binding.root
    }

    private fun initActionBar(toolbar :Toolbar) {
        val activity = (activity as AppCompatActivity)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_stock_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.main_to_addtrade)
        return false
    }

    override fun onResume() {
        super.onResume()
        viewModel.autoRefresh()
    }
}