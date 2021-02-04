package com.yywspace.module_login.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.yywspace.module_base.bean.User;
import com.yywspace.module_base.net.ServerUtils;
import com.yywspace.module_base.net.crypto.MD5Util;
import com.yywspace.module_base.util.JsonUtils;
import com.yywspace.module_base.util.LogUtils;
import com.yywspace.module_login.emum.LoginInput;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AdminLoginPre extends LoginPresenter.UserLoginPresenter {

    @Override
    public void login(LifecycleOwner owner) {
        User loginUser = getView().getLoginUser();
        if (loginUser.getUsername().equals("")) {
            getView().showError(LoginInput.USERNAME, "用户名不能为空");
            return;
        }
        if (loginUser.getPassword().equals("")) {
            getView().showError(LoginInput.USERNAME, "密码不能为空");
            return;
        }
        // 密码md5加密
        loginUser.setPassword(MD5Util.encrypt(loginUser.getPassword()));
        LogUtils.d(loginUser.getPassword());
        RequestBody body = RequestBody.create(JsonUtils.getGson().toJson(loginUser), MediaType.parse("application/json; charset=utf-8"));
        ServerUtils.getCommonApi().adminLogin(body).observe(owner, userBaseResponse -> {
            User user = userBaseResponse.getData();
            if (user != null)
                LogUtils.d(user.getUsername());
            if (userBaseResponse.getCode() == 1)
                getView().getLoginUserResult(user);
            else if (userBaseResponse.getCode() == 2001 || userBaseResponse.getCode() == 2004) // 用户不存在
                getView().showError(LoginInput.USERNAME, userBaseResponse.getMsg());
            else if (userBaseResponse.getCode() == 2003) // 用户密码错误
                getView().showError(LoginInput.PASSWORD, userBaseResponse.getMsg());
            else
                getView().showError(LoginInput.DEFAULT, userBaseResponse.getMsg());

        });
    }
}
