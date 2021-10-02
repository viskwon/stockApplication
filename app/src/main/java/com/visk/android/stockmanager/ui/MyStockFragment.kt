package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.visk.android.stockmanager.databinding.MyStockFragmentBinding

class MyStockFragment : Fragment() {
    lateinit var binding: MyStockFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = MyStockFragmentBinding.inflate(inflater)
        return binding.root
    }
}