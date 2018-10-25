package com.infius.proximityuser.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.infius.proximityuser.R;
import com.infius.proximityuser.fragments.ForgotPasswordFragment;
import com.infius.proximityuser.fragments.LoginFragment;
import com.infius.proximityuser.fragments.SignupFragment;
import com.infius.proximityuser.listeners.AuthEventListener;
import com.infius.proximityuser.utilities.AppConstants;

public class AuthActivity extends AppCompatActivity implements AuthEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initViews();
    }

    private void initViews() {
        replaceFragment(new LoginFragment());
    }

    @Override
    public void onSignupClicked() {
        replaceFragment(new SignupFragment());
    }

    @Override
    public void onLoginClicked() {
        replaceFragment(new LoginFragment());
    }

    @Override
    public void onForgotPasswordClicked() {
        replaceFragment(new ForgotPasswordFragment());
    }

    @Override
    public void onChangePasswordClicked() {

    }

    @Override
    public void onLoginSuccess() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putBoolean(AppConstants.SP_IS_LOGGEDIN, true).commit();
        setResult(RESULT_OK);
        finish();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}
