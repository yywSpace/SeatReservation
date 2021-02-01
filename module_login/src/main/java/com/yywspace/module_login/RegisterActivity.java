package com.yywspace.module_login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.gyf.immersionbar.ImmersionBar;
import com.yywspace.module_base.base.BaseActivity;
import com.yywspace.module_base.bean.User;
import com.yywspace.module_login.databinding.LoginRegisterActivityBinding;
import com.yywspace.module_login.emum.LoginInput;
import com.yywspace.module_login.iview.LoginView;
import com.yywspace.module_login.presenter.LoginPresenter;
import com.yywspace.module_login.presenter.UserRegisterPre;

public class RegisterActivity extends BaseActivity<LoginView.IUserRegisterView, LoginPresenter.UserRegisterPresenter> implements LoginView.IUserRegisterView {
    LoginRegisterActivityBinding mBinding;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.login_left_to_mid, R.anim.login_mid_to_right);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View getLayout(@NonNull LayoutInflater inflater) {
        mBinding = LoginRegisterActivityBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public LoginPresenter.UserRegisterPresenter createPresenter() {
        return new UserRegisterPre();
    }

    @Override
    public LoginView.IUserRegisterView createView() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.login_left_to_mid, R.anim.login_mid_to_right);
    }

    @Override
    public void init() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
                .init();
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mBinding.registerButton.setOnClickListener(v -> {
            getPresenter().register(RegisterActivity.this);
//            finishRegister(null);
        });
    }

    @Override
    public User getRegisterUser() {
        String username = String.valueOf(mBinding.registerUsername.getText());
        String password = String.valueOf(mBinding.registerPassword.getText());
        String rePassword = String.valueOf(mBinding.registerRePassword.getText());
        if (!password.equals(rePassword))
            return null;
        return new User(username, password);
    }

    @Override
    public void finishRegister(User user) {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.login_left_to_mid, R.anim.login_mid_to_right);
    }

    @Override
    public void showError(LoginInput loginInput, String message) {
        switch (loginInput) {
            case USERNAME:
                mBinding.tilUsernameField.setError(message);
                break;
            case PASSWORD:
                mBinding.tilPasswordField.setError(message);
                break;
            case RE_PASSWORD:
                mBinding.tilRePasswordField.setError(message);
                break;
            default:
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
