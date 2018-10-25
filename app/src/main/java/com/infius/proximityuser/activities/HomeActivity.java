package com.infius.proximityuser.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.infius.proximityuser.R;
import com.infius.proximityuser.model.DataModel;
import com.infius.proximityuser.model.DrawerItem;
import com.infius.proximityuser.model.GuestHistoryModel;
import com.infius.proximityuser.utilities.ApiRequestHelper;
import com.infius.proximityuser.utilities.AppConstants;
import com.infius.proximityuser.utilities.ProfileUtils;
import com.infius.proximityuser.utilities.Utils;

public class HomeActivity extends BaseDrawerActivity {

    FrameLayout mContentFrame;
    RelativeLayout present, upcoming, preferred, history;
    TextView addGuest, greetingMessage, propertyName;
    ImageView profilePic;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mContentFrame.addView(getLayoutInflater().inflate(R.layout.activity_home, null));
        setupToolbar();

        initViews();
    }

    private void setupToolbar() {
        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.toolbar_layout);
    }

    private void initViews() {
        present = (RelativeLayout) findViewById(R.id.layout_present);
        preferred = (RelativeLayout) findViewById(R.id.layout_preferred);
        upcoming = (RelativeLayout) findViewById(R.id.layout_upcoming);
        history = (RelativeLayout) findViewById(R.id.layout_history);
        addGuest = (TextView) findViewById(R.id.add_guest_btn);

        greetingMessage = (TextView) findViewById(R.id.greeting);
        propertyName = (TextView) findViewById(R.id.property_number_home);
        profilePic = (ImageView) findViewById(R.id.profile_pic_home);

        present.setOnClickListener(this);
        preferred.setOnClickListener(this);
        upcoming.setOnClickListener(this);
        history.setOnClickListener(this);
        addGuest.setOnClickListener(this);

        setData();
    }

    public void setData() {
        super.setProfilData();;
        if (Utils.isUserLoggedIn(HomeActivity.this)) {
            String userName = ProfileUtils.getUserName(HomeActivity.this);
            String ownerShipType = ProfileUtils.getOwnershipType(HomeActivity.this);
            String property = ProfileUtils.getPropertyName(HomeActivity.this);

            if (ProfileUtils.getProfileImageUrl(HomeActivity.this).equalsIgnoreCase("mock")) {
                profilePic.setVisibility(View.VISIBLE);
                profilePic.setImageResource(R.drawable.pic);
            }

            if (!TextUtils.isEmpty(userName)) {
                greetingMessage.setText(getString(R.string.greeting_message, userName));
            }

            if (!TextUtils.isEmpty(property)) {
                propertyName.setVisibility(View.VISIBLE);
                propertyName.setText(property + " (" + ownerShipType +  ")");
            }

        } else {
            greetingMessage.setText(getString(R.string.greeting_message, ""));
            propertyName.setVisibility(View.GONE);
            profilePic.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.layout_present) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, GuestListActivity.class);
                intent.putExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_PRESENT);
                startActivity(intent);
            }

        } else if (view.getId() == R.id.layout_preferred) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, GuestListActivity.class);
                intent.putExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_PREFERRED);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.layout_upcoming) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, GuestListActivity.class);
                intent.putExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_UPCOMING);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.layout_history) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, GuestListActivity.class);
                intent.putExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_HISTORY);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.add_guest_btn) {
            Intent intent = new Intent(HomeActivity.this, AddGuestActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_LOGIN) {
                setData();
            }
        }
    }

    @Override
    public void onItemClick(DrawerItem item) {
        if (item.getAction() == 1) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean(AppConstants.SP_IS_LOGGEDIN, false).commit();
            setProfilData();
            setData();
        }
    }

}
