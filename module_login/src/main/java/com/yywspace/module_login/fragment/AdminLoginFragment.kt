package com.yywspace.module_login.fragment

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.base.BaseFragment
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.extensions.setValue
import com.yywspace.module_login.databinding.LoginFragmentAdminBinding
import com.yywspace.module_login.emum.LoginInput
import com.yywspace.module_login.iview.LoginView.IUserLoginView
import com.yywspace.module_login.presenter.AdminLoginPre
import com.yywspace.module_login.presenter.LoginPresenter.UserLoginPresenter
import kotlinx.coroutines.launch

class AdminLoginFragment : BaseFragment<IUserLoginView?, UserLoginPresenter?>(), IUserLoginView {
    var mBinding: LoginFragmentAdminBinding? = null
    override fun getLayout(inflater: LayoutInflater): View {
        mBinding = LoginFragmentAdminBinding.inflate(inflater)
        return mBinding!!.root
    }

    override fun createPresenter(): UserLoginPresenter {
        return AdminLoginPre()
    }

    override fun createView(): IUserLoginView {
        return this
    }

    override fun init() {
        mBinding!!.loginButton.setOnClickListener { presenter!!.login(this) }
    }

    override fun getLoginUser(): User {
        val username = mBinding!!.loginUsername.text.toString()
        val password = mBinding!!.loginPassword.text.toString()
        return User(username, password)
    }

    override fun getLoginUserResult(user: User) {
        val dataStore: DataStore<Preferences> = requireContext().createDataStore(
                name = AppConfig.SETTING_PREF
        )
        viewLifecycleOwner.lifecycleScope.launch {
            dataStore.setValue(preferencesKey<String>("username"), user.username)
            dataStore.setValue(preferencesKey("isLogin"), true)
            dataStore.setValue(preferencesKey("isAdmin"), true)
        }
        // 跳转界面
        Toast.makeText(mContext, "跳转界面$user", Toast.LENGTH_SHORT).show()
    }

    override fun showError(loginInput: LoginInput, message: String) {
        when (loginInput) {
            LoginInput.USERNAME -> mBinding!!.tilUsernameField.error = message
            LoginInput.PASSWORD -> mBinding!!.tilPasswordField.error = message
            else -> Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}