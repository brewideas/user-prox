package com.infius.proximityuser.listeners;

public interface AuthEventListener {
    void onSignupClicked();
    void onLoginClicked();
    void onForgotPasswordClicked();
    void onChangePasswordClicked();
    void onLoginSuccess();
}
