package com.infius.proximityuser.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.infius.proximityuser.R;
import com.infius.proximityuser.utilities.AppConstants;

public class DeepLinkActivity extends AppCompatActivity {

    private TextView deepLinkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        deepLinkUrl =  findViewById(R.id.deep_link_url);
        handleIntent();
    }

    private void handleIntent() {
        Intent intentData = getIntent();

        // Get the uri from the intent
        Uri uri = intentData.getData();

        // Do not continue if the uri does not exist
        if (uri == null) {
            finish();
            return;
        }

        String action = uri.getQueryParameter(AppConstants.DEEP_LINK_PARAM_ACTION);
        String id = uri.getQueryParameter(AppConstants.DEEP_LINK_PARAM_ID);

        if (action != null && !action.isEmpty() && id != null && !id.isEmpty()) {
            switch (action) {
                case AppConstants.DEEP_LINK_ACTION_1:
                    deepLinkUrl.setText("action:" + action + "\n" + "id:" + id);
                    //handelActivity(id, MyActivity.class);
                    break;

                case AppConstants.DEEP_LINK_ACTION_2:
                    deepLinkUrl.setText("action:" + action + "\n" + "id:" + id);
                    //handelActivity(id, MyActivity.class);
                    break;

                case AppConstants.DEEP_LINK_ACTION_3:
                    deepLinkUrl.setText("action:" + action + "\n" + "id:" + id);
                    //handelActivity(id, MyActivity.class);
                    break;

                default:
                    Toast.makeText(this, "Please verify the URL", Toast.LENGTH_SHORT).show();
                    finish();
            }
        } else {
            Toast.makeText(this, "Please verify the URL", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * @param cls The component class that is to be used for the intent.
     */
    private void handelActivity(String id, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConstants.DEEP_LINK_PARAM_ID, id);
        startActivity(intent);
        finish();
    }
}
