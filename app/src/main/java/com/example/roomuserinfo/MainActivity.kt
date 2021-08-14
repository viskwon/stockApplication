package com.example.roomuserinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.roomuserinfo.db.User
import com.example.roomuserinfo.stock.ui.StockActivity
import com.example.roomuserinfo.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModel.UserViewModelFactory((application as StockApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}