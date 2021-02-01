package com.yywspace.module_login.presenter;


import androidx.lifecycle.LifecycleOwner;

import com.yywspace.module_base.base.BasePresenter;
import com.yywspace.module_login.iview.LoginView;

public interface LoginPresenter {
    abstract class UserLoginPresenter extends BasePresenter<LoginView.IUserLoginView> {
        public abstract void login(LifecycleOwner owner);
    }

    abstract class UserRegisterPresenter extends BasePresenter<LoginView.IUserRegisterView> {
        public abstract void register(LifecycleOwner owner);
    }
}
