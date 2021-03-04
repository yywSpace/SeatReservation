package com.yywspace.module_home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.databinding.HomeActivityBinding
import com.yywspace.module_home.fragment.HomeFragment
import com.yywspace.module_home.statePattern.ReservationContext

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