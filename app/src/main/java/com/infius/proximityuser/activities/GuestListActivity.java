package com.infius.proximityuser.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.infius.proximityuser.R;
import com.infius.proximityuser.adapters.PrimaryGuestAdapter;
import com.infius.proximityuser.model.DataModel;
import com.infius.proximityuser.model.GuestHistoryModel;
import com.infius.proximityuser.utilities.ApiRequestHelper;
import com.infius.proximityuser.utilities.AppConstants;
import com.infius.proximityuser.utilities.Utils;

public class GuestListActivity extends AppCompatActivity {

    int mType;
    private Dialog mProgressDialog;
    private ImageView backBtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);
        if (getIntent() != null && getIntent().hasExtra(AppConstants.GUEST_LIST_TYPE)) {
            mType = getIntent().getIntExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_HISTORY);
        }
        initViews();
        fetchGuestList();
    }

    private void initViews() {
        backBtn = (ImageView) findViewById(R.id.back_btn);
        recyclerView = (RecyclerView) findViewById(R.id.guest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuestListActivity.this));
    }

    private void fetchGuestList() {
        showProgressDialog();
        ApiRequestHelper.requestGuestList(this, getParamName(), new Response.Listener<DataModel>() {
            @Override
            public void onResponse(DataModel response) {
                if (response instanceof GuestHistoryModel) {
                    hideProgressDialog();
                    if (AppConstants.STATUS_SUCCESS.equalsIgnoreCase(((GuestHistoryModel) response).getStatus())) {
                        setData((GuestHistoryModel) response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                if (error != null) {
                    Toast.makeText(GuestListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setData(GuestHistoryModel response) {
        PrimaryGuestAdapter adapter = new PrimaryGuestAdapter(GuestListActivity.this, response.getGuestsList());
        recyclerView.setAdapter(adapter);
    }

    private String getParamName() {
        if (mType == AppConstants.TYPE_HISTORY){
            return AppConstants.GUEST_LIST_PARAM_HISTORY;
        } else if (mType == AppConstants.TYPE_PREFERRED){
            return AppConstants.GUEST_LIST_PARAM_PREFERRED;
        } else if (mType == AppConstants.TYPE_PRESENT){
            return AppConstants.GUEST_LIST_PARAM_PRESENT;
        } else if (mType == AppConstants.TYPE_UPCOMING){
            return AppConstants.GUEST_LIST_PARAM_UPCOMING;
        }
        return null;
    }

    protected void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = Utils.getProgressDialog(this);
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
}
