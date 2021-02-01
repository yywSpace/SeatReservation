package com.yywspace.module_login.iview;

import com.yywspace.module_base.base.BaseViewImp;
import com.yywspace.module_base.bean.User;
import com.yywspace.module_login.emum.LoginInput;

public interface LoginView {
    interface IUserLoginView extends BaseViewImp {
        User getLoginUser();
        void getLoginUserResult(User user);
        void showError(LoginInput loginInput, String message);
    }

    interface IUserRegisterView extends BaseViewImp {
        User getRegisterUser();
        void finishRegister(User user);
        void showError(LoginInput loginInput, String message);
    }
}
