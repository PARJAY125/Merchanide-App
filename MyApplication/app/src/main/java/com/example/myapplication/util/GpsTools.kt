package com.example.myapplication.util

import android.webkit.*
import android.widget.TextView

class GpsTools {
    companion object {

        fun setGps(webView : WebView) {
            webView.settings.javaScriptEnabled = true
            webView.settings.setGeolocationEnabled(true)
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.loadUrl("file:///android_asset/index.html")
        }

        fun setGPSLocation(webView : WebView, textView: TextView) {
            webView.addJavascriptInterface(object {
                @JavascriptInterface
                fun processLocation(location: String) {
                    // The location variable contains the inner HTML content of the element with an id of "demo"
                    // Do something with the inner HTML content here
                    // Didnt work --> rd_tv_gps_location.text = location
                }
            }, "LocationInterface")

            // Use JavaScript to get the inner HTML content of the element with an id of "demo"
            webView.evaluateJavascript("(function() { return document.getElementById('demo').innerHTML; })();") { result ->
                // The result variable contains the inner HTML content of the element with an id of "demo"
                // Store the inner HTML content in the "location" variable and pass it to the LocationInterface interface
                val location = result?.replace("\"", "")
                webView.post { webView.loadUrl("javascript:window.LocationInterface.processLocation('$location');") }
                textView.text = location
            }
        }

        fun gpsSetting(webView : WebView) {
            webView.webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                    callback.invoke(origin, true, false)
                }
            }
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return true
                }
            }
        }
    }
}