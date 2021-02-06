package com.yywspace.module_mine.user.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yywspace.module_mine.databinding.MineReservationRuleActivityBinding

class ReservationRuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MineReservationRuleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.webView.loadUrl(url)
    }
}