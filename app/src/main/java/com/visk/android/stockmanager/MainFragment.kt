package com.visk.android.stockmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
Toast.makeText(context , "dd" ,Toast.LENGTH_SHORT).show()
        view.findViewById<Button>(R.id.btn_start).setOnClickListener {
            findNavController().navigate(R.id.main_to_stock)
        }
        return view
    }


}