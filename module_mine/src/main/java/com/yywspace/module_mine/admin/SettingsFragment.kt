package com.yywspace.module_mine.admin

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Setting
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_mine.R
import com.yywspace.module_mine.iview.ISettingView
import com.yywspace.module_mine.presenter.SettingPresenter

class SettingsFragment : PreferenceFragmentCompat(), ISettingView {
    private val presenter: SettingPresenter = SettingPresenter()


    init {
        presenter.attachView(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.mine_preference, rootKey)
        presenter.getSetting(requireActivity(), User.currentUser?.username!!)
    }

    private fun initPreferences(setting: Setting) {
        LogUtils.d(setting.toString())
        val reservationTime: Preference? = findPreference("reservationTime")
        reservationTime?.setDefaultValue(setting.reservationTime.toString())
        reservationTime?.setOnPreferenceChangeListener { preference, newValue ->
            setting.reservationTime = newValue.toString().toInt()
            preference.setDefaultValue(newValue)
            presenter.insertSetting(requireActivity(), setting)
            true
        }
        val afkTime: Preference? = findPreference("afkTime")
        afkTime?.setDefaultValue(setting.afkTime.toString())
        afkTime?.setOnPreferenceChangeListener { preference, newValue ->
            setting.afkTime =  newValue.toString().toInt()
            preference.setDefaultValue(newValue)
            presenter.insertSetting(requireActivity(), setting)
            true
        }
    }

    override fun getSettingResult(setting: Setting) {
        initPreferences(setting)
    }

    override fun insertSettingResult(response: BaseResponse<Any>) {

    }
}
