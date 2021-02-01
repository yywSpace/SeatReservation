package com.yywspace.module_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_home.databinding.HomeActivityBinding
import com.yywspace.module_home.fragment.HomeFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: HomeActivityBinding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //添加fragment
        val fm = supportFragmentManager
        val tx = fm.beginTransaction()
        tx.replace(R.id.fragment, HomeFragment())
        tx.commit()
    }
}