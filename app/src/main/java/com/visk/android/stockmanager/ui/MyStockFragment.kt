package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.databinding.MyStockFragmentBinding
import com.visk.android.stockmanager.viewmodel.MyStockViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MyStockFragment : Fragment() {
    lateinit var binding: MyStockFragmentBinding
    val viewModel:MyStockViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyStockFragmentBinding.inflate(inflater)
        setHasOptionsMenu(true)
        initActionBar(binding.toolbar)
        val adapter = MyStockAdapter()
        binding.mystockRecyclerView.adapter = adapter
coordinateMotion()
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




    private fun coordinateMotion() {
        val appBarLayout: AppBarLayout = binding.appbar
        val motionLayout: MotionLayout = binding.appbarMotionlayout

        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            Log.d("hjskwon","progress $seekPosition")
            motionLayout.progress = seekPosition
        }

        appBarLayout.addOnOffsetChangedListener(listener)
    }
}