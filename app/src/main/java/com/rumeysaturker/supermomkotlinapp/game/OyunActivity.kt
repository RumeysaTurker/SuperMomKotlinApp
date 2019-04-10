package com.rumeysaturker.supermomkotlinapp.game

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rumeysaturker.supermomkotlinapp.R

class OyunActivity : AppCompatActivity() {
    var mywebview: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oyun)
        mywebview = findViewById<WebView>(R.id.webview)
        mywebview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        mywebview!!.loadUrl("https://benanneyim.com/ebeveynlik/cocuk-yetistirmek/cocuklarin-karalama-yapmasi/")
        mywebview!!.webViewClient = WebViewClient()
        mywebview!!.settings.javaScriptEnabled = true
    }
}
