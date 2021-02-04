package com.yywspace.module_login.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.yywspace.module_base.bean.User;
import com.yywspace.module_base.net.ServerUtils;
import com.yywspace.module_base.net.crypto.MD5Util;
import com.yywspace.module_base.util.JsonUtils;
import com.yywspace.module_login.emum.LoginInput;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserRegisterPre extends LoginPresenter.UserRegisterPresenter {

    @Override
    public void register(LifecycleOwner owner) {
        User user = getView().getRegisterUser();
        if (user == null) {
            getView().showError(LoginInput.RE_PASSWORD, "两次密码不一致");
            return;
        }
        if (user.getUsername().equals("")) {
            getView().showError(LoginInput.USERNAME, "用户名不能为空");
            return;
        }
        if (user.getPassword().equals("")) {
            getView().showError(LoginInput.USERNAME, "密码不能为空");
            return;
        }
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        RequestBody body = RequestBody.create(JsonUtils.getGson().toJson(user), MediaType.parse("application/json; charset=utf-8"));
        ServerUtils.getCommonApi().register(body).observe(owner, userBaseResponse -> {
            User retUser = getView().getRegisterUser();
            if (userBaseResponse.getCode() == 1)
                getView().finishRegister(retUser);
            else if (userBaseResponse.getCode() == 2002) // 用户已存在
                getView().showError(LoginInput.USERNAME, userBaseResponse.getMsg());
            else
                getView().showError(LoginInput.DEFAULT, userBaseResponse.getMsg());
        });
    }
}
