package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.visk.android.stockmanager.databinding.AddTradeFragmentBinding
import com.visk.android.stockmanager.viewmodel.AddTradeViewModel
import com.visk.android.stockmanager.viewmodel.StockViewModel
import kotlinx.coroutines.launch


class AddTradeFragment : Fragment() {
    lateinit var binding: AddTradeFragmentBinding
    lateinit var viewModel: AddTradeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddTradeFragmentBinding.inflate(inflater)
        binding.saveTrade.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addTrade(
                    binding.stockidEdit.text.toString(),
                    binding.priceEdit.text.toString(),
                    binding.volEdit.text.toString(),
                    binding.addTradeCalendar.date
                )
                findNavController().popBackStack()
            }
        }
        viewModel = ViewModelProvider(this).get(AddTradeViewModel::class.java)
        return binding.root
    }


}