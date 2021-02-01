package com.yywspace.module_login.fragment

import android.content.Intent
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
import com.yywspace.module_login.R
import com.yywspace.module_login.RegisterActivity
import com.yywspace.module_login.databinding.LoginFragmentUserBinding
import com.yywspace.module_login.emum.LoginInput
import com.yywspace.module_login.iview.LoginView.IUserLoginView
import com.yywspace.module_login.presenter.LoginPresenter.UserLoginPresenter
import com.yywspace.module_login.presenter.UserLoginPre
import kotlinx.coroutines.launch

class UserLoginFragment : BaseFragment<IUserLoginView?, UserLoginPresenter?>(), IUserLoginView {
    var binding: LoginFragmentUserBinding? = null
    override fun getLayout(inflater: LayoutInflater): View {
        binding = LoginFragmentUserBinding.inflate(inflater)
        return binding!!.root
    }

    override fun createPresenter(): UserLoginPresenter {
        return UserLoginPre()
    }

    override fun createView(): IUserLoginView {
        return this
    }

    override fun init() {
        binding!!.loginButton.setOnClickListener { v: View? -> presenter!!.login(this) }
        binding!!.registerButton.setOnClickListener { v: View? ->
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.login_right_to_mid, R.anim.login_mid_to_left)
        }
    }

    override fun getLoginUser(): User {
        val username = binding!!.loginUsername.text.toString()
        val password = binding!!.loginPassword.text.toString()
        return User(username, password)
    }

    override fun getLoginUserResult(user: User) {
        val dataStore: DataStore<Preferences> = context!!.createDataStore(
                name = AppConfig.SETTING_PREF
        )
        viewLifecycleOwner.lifecycleScope.launch {
            dataStore.setValue(preferencesKey<String>("username"), user.username)
            dataStore.setValue(preferencesKey("isLogin"), true)
            dataStore.setValue(preferencesKey("isAdmin"), false)
        }

        // 跳转界面
        Toast.makeText(mContext, "跳转界面$user", Toast.LENGTH_SHORT).show()
    }

    override fun showError(loginInput: LoginInput, message: String) {
        when (loginInput) {
            LoginInput.USERNAME -> binding!!.tilUsernameField.error = message
            LoginInput.PASSWORD -> binding!!.tilPasswordField.error = message
            else -> Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}