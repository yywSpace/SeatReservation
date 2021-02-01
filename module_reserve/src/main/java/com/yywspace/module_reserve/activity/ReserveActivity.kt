package com.yywspace.module_reserve.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.databinding.ReserveActivityBinding
import com.yywspace.module_reserve.fragment.ReserveFragment

class ReserveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReserveActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //添加fragment
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.fragment, ReserveFragment())
        tx.commit()
    }
}