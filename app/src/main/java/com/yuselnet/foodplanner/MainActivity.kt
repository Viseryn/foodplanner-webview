package com.yuselnet.foodplanner

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.yuselnet.foodplanner.ui.theme.FoodPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodPlannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoodPlannerWebView()
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Preview(showSystemUi = true)
@Composable
fun FoodPlannerWebView() {
    val url = when (false) {
        true -> "192.168.178.64:8000/recipe/add"
        else -> "https://foodplanner.yusel.net/"
    }

    var webView: WebView? by remember { mutableStateOf(null) }
    var backEnabled by remember { mutableStateOf(true) }

    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Enable back button
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        backEnabled = view.canGoBack()
                    }
                }

                // More settings for the client
                webViewClient = WebViewClient()
                val settings: WebSettings = this.settings

                settings.javaScriptEnabled = true
                settings.allowContentAccess = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true

                // Disable overscroll bouncing and make loading screen transparent
                this.overScrollMode = View.OVER_SCROLL_NEVER
                this.setBackgroundColor(Color.TRANSPARENT)

                // Load WebApp
                loadUrl(url)
                webView = this
            }
        },
        update = { webView = it }
    )

    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}
