package com.yywspace.seatreservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.yywspace.module_base.path.RouterPath
import com.yywspace.seatreservation.databinding.ActivityAdminMainBinding
import com.yywspace.seatreservation.databinding.ActivityMainBinding

@Route(path = RouterPath.ADMIN_MAIN_PATH)
class AdminMainActivity : AppCompatActivity() {
    var mBinding: ActivityAdminMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentMap = initFragment()
        mBinding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        mBinding!!.bottomNavigationView.setOnNavigationItemSelectedListener {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.navigation_menu_home -> {
                    fragmentTransaction.replace(R.id.home_container, fragmentMap[R.id.navigation_menu_home]
                            ?: error("")).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_menu_mine -> {
                    fragmentTransaction.replace(R.id.home_container, fragmentMap[R.id.navigation_menu_mine]
                            ?: error("")).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        mBinding!!.bottomNavigationView.selectedItemId = R.id.navigation_menu_home
    }

    private fun initFragment(): Map<Int, Fragment> {
        val sceneFragment = ARouter.getInstance().build(RouterPath.SCENE_PATH).navigation() as Fragment
        val mineFragment = ARouter.getInstance().build(RouterPath.MINE_ADMIN_PATH).navigation() as Fragment
        return mapOf(R.id.navigation_menu_home to sceneFragment,
                R.id.navigation_menu_mine to mineFragment)
    }
}