package com.yywspace.module_mine.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yywspace.module_mine.databinding.MineReservationRuleActivityBinding

class ReservationRuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MineReservationRuleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "预约规则"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.webView.loadUrl("file:///android_asset/云计算相关知识.html");
    }
}