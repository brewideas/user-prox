package com.infius.proximityuser.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.infius.proximityuser.R;
import com.infius.proximityuser.utilities.AppConstants;
import com.infius.proximityuser.utilities.Utils;

public class ShowQRActivity extends AppCompatActivity implements View.OnClickListener {

    private String qrCodeId;
    ImageView backBtn;
    ImageView qrImageView;
    ImageView shareBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_qr_activity);
        initViews();
        setQR();
    }

    private void initViews() {
        qrImageView = (ImageView) findViewById(R.id.qr_code_image);
        setQR();
        backBtn = (ImageView) findViewById(R.id.back_btn);
        shareBtn = (ImageView) findViewById(R.id.share_btn);
        backBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
    }

    private void setQR() {
        if (getIntent() != null && getIntent().hasExtra(AppConstants.QR_CODE_ID)) {
            qrCodeId = getIntent().getStringExtra(AppConstants.QR_CODE_ID);
        }
        try {
            qrImageView.setImageBitmap(Utils.generateQR(qrCodeId, Utils.convertDpToPixel(300f, this), Utils.convertDpToPixel(300f, this)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn) {
            onBackPressed();
        } else if (v.getId() == R.id.share_btn) {
            share(qrImageView);
        }
    }

    private void share(View view){
        // Marshmallow Permission check
        if (Utils.isVersionMarshmallowAndAbove() && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Utils.requestWriteExternalPermission(this);
            Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //CJRSendGTMTag.sendCustomEvents(CJRGTMConstants.GTM_KEY_QR_REQUEST_QR_SHARE,this
            // .getActivity());
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");

            share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
            String body = getString(R.string.share_subject);
            share.putExtra(Intent.EXTRA_TEXT, body);

            Bitmap cacheBmp = viewToBitmap(view);
            String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), cacheBmp, getString(R.string.title), null);
            if (pathofBmp == null) return;
            Uri bmpUri = Uri.parse(pathofBmp);
            share.putExtra(Intent.EXTRA_STREAM, bmpUri);
            Intent chooserIntent = Intent.createChooser(share, getString(R.string
                    .share_title));
            if (chooserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooserIntent);
            } else {
                Toast.makeText(this, getString(R.string.no_app_found), Toast
                        .LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config
                .ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (Utils.verifyPermissions(grantResults)) {
            share(qrImageView);
        }
    }

}
