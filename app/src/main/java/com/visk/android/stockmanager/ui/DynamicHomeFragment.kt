package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment

class DynamicHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                setContent {
                    my()
                }
            }
        }
    }
    @Composable
    fun my() {
        MaterialTheme {
            // In Compose world
            Column {
                Text("Hello Compose!!")
                Text("Hello Compose!!")
                Text("Hello Compose!!")
            }

        }
    }

    @Preview
    @Composable
    fun Greeting() {
        my()
    }

}