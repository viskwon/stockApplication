package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    lateinit var binding: MainFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        super.onCreateView(inflater, container, savedInstanceState)
        binding = MainFragmentBinding.inflate(inflater)
        binding.mainTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> StockFragment()
                    1 -> MyStockFragment()
                    else -> null
                }?.let {
                    parentFragmentManager.commit {
                        replace(R.id.main_content, it)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        parentFragmentManager.commit { replace(R.id.main_content, StockFragment()) }
        return binding.root
    }

}