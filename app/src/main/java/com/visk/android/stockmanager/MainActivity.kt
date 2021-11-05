package com.visk.android.stockmanager

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("hjskwon","onConfigurationChanged activity")
        super.onConfigurationChanged(newConfig)
    }
}
