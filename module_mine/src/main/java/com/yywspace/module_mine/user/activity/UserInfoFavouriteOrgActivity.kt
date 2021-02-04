package com.yywspace.module_mine.user.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yywspace.module_mine.databinding.MineUserInfoFavouriteOrgActivityBinding

class UserInfoFavouriteOrgActivity : AppCompatActivity() {
    lateinit var binding: MineUserInfoFavouriteOrgActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MineUserInfoFavouriteOrgActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}