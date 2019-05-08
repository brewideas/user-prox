package com.infius.proximityuser.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.infius.proximityuser.R;
import com.infius.proximityuser.activities.AddGuestActivity;
import com.infius.proximityuser.listeners.AuthEventListener;
import com.infius.proximityuser.model.AccessTokenModel;
import com.infius.proximityuser.model.DataModel;
import com.infius.proximityuser.model.InvitationModel;
import com.infius.proximityuser.model.ProfileInfo;
import com.infius.proximityuser.model.UserSessionDetails;
import com.infius.proximityuser.utilities.ApiRequestHelper;
import com.infius.proximityuser.utilities.AppConstants;
import com.infius.proximityuser.utilities.ProfileUtils;
import com.infius.proximityuser.utilities.Utils;

public class LoginFragment extends Fragment implements View.OnClickListener {

    AuthEventListener listener;
    TextView login, forgotPassword, signup, mockLogin;
    ImageView displayPassword;
    TextInputEditText user, password;
    TextInputLayout tilUser, tilPassword;
    private Dialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        login = (TextView) rootView.findViewById(R.id.btn_login);
        forgotPassword = (TextView) rootView.findViewById(R.id.forgot_password);
        signup = (TextView) rootView.findViewById(R.id.sign_up);
        displayPassword = (ImageView) rootView.findViewById(R.id.eye);
        mockLogin = (TextView) rootView.findViewById(R.id.mock_login);
        user = (TextInputEditText) rootView.findViewById(R.id.edit_user_id);
        password = (TextInputEditText) rootView.findViewById(R.id.edit_password);
        tilUser = (TextInputLayout) rootView.findViewById(R.id.til_user_id);
        tilPassword = (TextInputLayout) rootView.findViewById(R.id.til_password);

        login.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signup.setOnClickListener(this);
        displayPassword.setOnClickListener(this);
        mockLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            boolean credentials = true;
            if (TextUtils.isEmpty(user.getText())) {
                tilUser.setError(getString(R.string.enter_mobile_no));
                credentials = false;
            }
            if (TextUtils.isEmpty(password.getText())) {
                password.setError(getString(R.string.enter_password));
                credentials = false;
            }
            if (credentials) {
                loginApiCall();
            } else {
                return;
            }
        } else if (view.getId() == R.id.forgot_password) {
            listener.onForgotPasswordClicked();
        } else if (view.getId() == R.id.sign_up) {
            listener.onSignupClicked();
        } else if (view.getId() == R.id.eye) {

        } else if (view.getId() == R.id.mock_login) {
            listener.onLoginSuccess();
        }
    }

    protected void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = Utils.getProgressDialog(getActivity());
            }
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginApiCall() {
        showProgressDialog();
        ApiRequestHelper.requestSignin(getActivity(), user.getText().toString(), password.getText().toString(), new Response.Listener<DataModel>() {
            @Override
            public void onResponse(DataModel response) {
                hideProgressDialog();
                if (response instanceof AccessTokenModel) {
                    Utils.saveString(getActivity(), AppConstants.KEY_TOKEN, ((AccessTokenModel) response).getAccess_token());
                    fetchUserProfile();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "System Error : " + Utils.getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserProfile() {
        showProgressDialog();
        ApiRequestHelper.requestUserDetails(getActivity(), new Response.Listener<DataModel>() {
            @Override
            public void onResponse(DataModel response) {
                hideProgressDialog();
                if (response instanceof ProfileInfo && ((ProfileInfo) response).getUserSessionDetails()!=null) {
                    UserSessionDetails profileDetails = ((ProfileInfo) response).getUserSessionDetails();
                    if (AppConstants.ROLE_RESIDENT.equalsIgnoreCase(profileDetails.getPrimaryRole())) {
                        Toast.makeText(getActivity(), getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                        ProfileUtils.saveProfileDetails(getActivity(), profileDetails);
                        listener.onLoginSuccess();
                    } else {
                        Toast.makeText(getActivity(), "System Error : " + getString(R.string.not_authorized), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Utils.saveString(getActivity(), AppConstants.KEY_TOKEN, "");
                Toast.makeText(getActivity(), "System Error : " + Utils.getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        listener = (AuthEventListener) activity;
    }
}
