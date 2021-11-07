package com.visk.android.stockmanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

class DynamicHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                setContent {
                    coordinate()
                }
            }
        }
    }
    @Composable
    fun my() {
        Log.d("hjskwon","my")
        var shouldShowOnboarding by rememberSaveable  { mutableStateOf(true) }

        val expanded = remember { mutableStateOf(false) }
        val text = if(expanded.value) "hjskwon" else "false"
        MaterialTheme {

            // In Compose world
            Column {
                Text("Hello Compose!!")
                Text("Hello Compose!!")
                Text(text)
                OutlinedButton(
                    onClick = { expanded.value = !expanded.value }
                ) {
                    Text(if (expanded.value) "Show less" else "Show more")
                    Text("hola")
                }
            }

        }
    }

    @Preview
    @Composable
    fun Greeting() {
        my()
    }
    @Composable
    fun coordinate(){

        // here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn
        val toolbarHeight = 150.dp
        val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
// our offset to collapse toolbar
        val toolbarOffsetHeightPx =

            remember { mutableStateOf(0f) }
// now, let's create connection to the nested scroll system and listen to the scroll
// happening inside child LazyColumn
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-200f, 0f)
                    // here's the catch: let's pretend we consumed 0 in any case, since we want
                    // LazyColumn to scroll anyway for good UX
                    // We're basically watching scroll without taking it
                    return Offset.Zero
                }
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                // attach as a parent to the nested scroll system
                .nestedScroll(nestedScrollConnection)
        ) {
            // our list with build in nested scroll support that will notify us about its scroll
            LazyColumn(contentPadding = PaddingValues(top = toolbarHeight)) {
                items(100) { index ->
                    Text("I'm item $index", modifier = Modifier.fillMaxWidth().padding(16.dp))
                }
            }
            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                title = {

                    Text(modifier = Modifier.alpha(200 +toolbarOffsetHeightPx.value/200),text="toolbar offset is ${toolbarOffsetHeightPx.value}")
                    Text(modifier = Modifier.alpha(toolbarOffsetHeightPx.value/-200),text = "ya ${toolbarOffsetHeightPx.value}")
                }


            )
        }
    }

}