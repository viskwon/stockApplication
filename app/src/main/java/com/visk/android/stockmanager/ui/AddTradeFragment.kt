package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.visk.android.stockmanager.Constant
import com.visk.android.stockmanager.databinding.AddTradeFragmentBinding
import com.visk.android.stockmanager.viewmodel.AddTradeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddTradeFragment : Fragment() {
    lateinit var binding: AddTradeFragmentBinding
    val viewModel: AddTradeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddTradeFragmentBinding.inflate(inflater)
        binding.buyTrade.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addTrade(
                    binding.stockidEdit.text.toString(),
                    binding.priceEdit.text.toString(),
                    binding.volEdit.text.toString(),
                    binding.addTradeCalendar.date,
                    Constant.Trade.BUY
                )
                findNavController().popBackStack()
            }
        }

        binding.sellTrade.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addTrade(
                    binding.stockidEdit.text.toString(),
                    binding.priceEdit.text.toString(),
                    binding.volEdit.text.toString(),
                    binding.addTradeCalendar.date,
                    Constant.Trade.SELL
                )
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

}