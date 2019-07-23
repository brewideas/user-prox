package com.infius.proximityuser.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.infius.proximityuser.R;
import com.infius.proximityuser.adapters.OtherGuestAdapter;
import com.infius.proximityuser.adapters.VehiclesAdapter;
import com.infius.proximityuser.custom.CircularImageView;
import com.infius.proximityuser.custom.MyCropImageActivity;
import com.infius.proximityuser.listeners.GuestRemoveListener;
import com.infius.proximityuser.listeners.VehicleRemoveListener;
import com.infius.proximityuser.model.DataModel;
import com.infius.proximityuser.model.Guest;
import com.infius.proximityuser.model.InvitationModel;
import com.infius.proximityuser.model.Vehicle;
import com.infius.proximityuser.utilities.ApiRequestHelper;
import com.infius.proximityuser.utilities.AppConstants;
import com.infius.proximityuser.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddGuestActivity extends AppCompatActivity implements View.OnClickListener, VehicleRemoveListener, GuestRemoveListener {

    private static final int DATE_DIALOG_ID = 1;
    private static final int TIME_DIALOG_ID = 2;
    private RadioButton guestNormal;
    private RadioButton guestPreffered;
    private TextInputEditText guestCount;
    private TextInputEditText guestName;
    private TextInputEditText guestPhoneNo;
    private TextInputEditText guestEmail;
    private TextInputEditText dateOfArrival;
    private TextInputEditText inTime;
    private TextInputEditText outTime;

    private TextInputEditText nameEdit;
    private TextInputEditText mobileEdit;

    private TextInputLayout tilGuestName;
    private TextInputLayout tilPhone;
    private TextInputLayout tilEmail;
    private TextInputLayout tilArrivalDate;
    private TextInputLayout tilInTime;
    private TextInputLayout tilOutTime;

    private RelativeLayout addParking;
    private RelativeLayout addOtherGuest;
    private TextView sendInvitation;
    private LinearLayout uploadPic;
    private ImageView backBtn;
    private ImageView contactPicker;
    private ImageView datePicker;
    private ImageView inTimePicker;
    private ImageView outTimePicker;
    private Dialog mProgressDialog;
    private CheckBox mPreferredCheckbox;
    CircularImageView displayPic;


    private boolean pickingInTime = true;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    ArrayList<Vehicle> vehicles;
    ArrayList<Guest> otherGuests;
    Uri selectedImage;
    Bitmap bitmap;

    RecyclerView vehicleRecyclerView, guestRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);
        initViews();
        addListeners();
    }

    private void addListeners() {
        dateOfArrival.setOnClickListener(this);
        inTime.setOnClickListener(this);
        outTime.setOnClickListener(this);
        addOtherGuest.setOnClickListener(this);
        addParking.setOnClickListener(this);
        sendInvitation.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        contactPicker.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        inTimePicker.setOnClickListener(this);
        outTimePicker.setOnClickListener(this);
        uploadPic.setOnClickListener(this);
        displayPic.setOnClickListener(this);
    }

    private void initViews() {
        guestNormal = (RadioButton) findViewById(R.id.rad_normal_guest);
        guestPreffered = (RadioButton) findViewById(R.id.rad_pref_guest);
        guestCount = (TextInputEditText) findViewById(R.id.edit_guest_count);

        guestName = (TextInputEditText) findViewById(R.id.edit_prim_guest_name);
        tilGuestName = (TextInputLayout) findViewById(R.id.til_prime_guest_name);

        guestPhoneNo = (TextInputEditText) findViewById(R.id.edit_prim_guest_phone);
        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);

        guestEmail = (TextInputEditText) findViewById(R.id.edit_prim_guest_email);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);

        dateOfArrival = (TextInputEditText) findViewById(R.id.edit_expected_date);
        tilArrivalDate = (TextInputLayout) findViewById(R.id.til_arrival_date);

        inTime = (TextInputEditText) findViewById(R.id.edit_in_time);
        tilInTime = (TextInputLayout) findViewById(R.id.til_in_time);

        outTime = (TextInputEditText) findViewById(R.id.edit_out_time);
        tilOutTime = (TextInputLayout) findViewById(R.id.til_out_time);

        uploadPic = (LinearLayout) findViewById(R.id.upload_pic);
        addParking = (RelativeLayout) findViewById(R.id.add_parking);
        addOtherGuest = (RelativeLayout) findViewById(R.id.add_other_guest);
        sendInvitation = (TextView) findViewById(R.id.btn_send_invitation);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        contactPicker = (ImageView) findViewById(R.id.contact_picker);
        datePicker = (ImageView) findViewById(R.id.date_picker);
        inTimePicker = (ImageView) findViewById(R.id.in_time_picker);
        outTimePicker = (ImageView) findViewById(R.id.out_time_picker);
        mPreferredCheckbox = (CheckBox) findViewById(R.id.checkbox_preferred);
        vehicleRecyclerView = (RecyclerView) findViewById(R.id.vehicle_list);
        guestRecyclerView = (RecyclerView) findViewById(R.id.guest_list);

        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(AddGuestActivity.this));
        guestRecyclerView.setLayoutManager(new LinearLayoutManager(AddGuestActivity.this));
        displayPic = (CircularImageView) findViewById(R.id.display_selected_pic);

        setCurrentDate();
        updateDate();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_invitation) {
            if (TextUtils.isEmpty(guestName.getText())) {
                tilGuestName.setError(getString(R.string.error_enter_name));
                return;
            } else if (TextUtils.isEmpty(guestPhoneNo.getText()) || !Utils.isValidPhoneNo(guestPhoneNo.getText().toString())) {
                tilPhone.setError(getString(R.string.error_enter_phone));
                return;
            }
//            else if (TextUtils.isEmpty(guestEmail.getText()) || !Utils.isValidEmail(guestEmail.getText().toString())) {
//                tilEmail.setError(getString(R.string.error_enter_email));
//                return;
//            }
            else if (TextUtils.isEmpty(dateOfArrival.getText())) {
                tilArrivalDate.setError(getString(R.string.error_enter_date));
                return;
            } else if (TextUtils.isEmpty(inTime.getText())) {
                tilInTime.setError(getString(R.string.enter_intime));
                return;
            } else if (TextUtils.isEmpty(outTime.getText())) {
                tilOutTime.setError(getString(R.string.enter_outtime));
                return;
            }

            addGuestApiCall();

        } else if (v.getId() == R.id.add_other_guest) {
            addOtherGuestsBottomSheet();
        } else if (v.getId() == R.id.add_parking) {
            addParkingBottomSheet();
        } else if (v.getId() == R.id.date_picker) {
            pickDate();
        } else if (v.getId() == R.id.in_time_picker) {
            pickTime(true);
        } else if (v.getId() == R.id.out_time_picker) {
            pickTime(false);
        } else if (v.getId() == R.id.contact_picker) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, AppConstants.REQUEST_CODE_PICK_CONTACT);
        } else if (v.getId() == R.id.back_btn) {
            onBackPressed();
        } else if (v.getId() == R.id.upload_pic || v.getId() == R.id.display_selected_pic) {
            Intent cropImageIntent = new Intent(this, MyCropImageActivity.class);
            startActivityForResult(cropImageIntent, AppConstants.REQUEST_CODE_GALLARY);
        }

    }

    private void addParkingBottomSheet() {
        View parkingBottomSheetView = AddGuestActivity.this.getLayoutInflater().inflate(R.layout.add_parking_bottom_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddGuestActivity.this);
        bottomSheetDialog.setContentView(parkingBottomSheetView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parkingBottomSheetView.getParent());

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        bottomSheetBehavior.setPeekHeight(Utils.getScreenHeight(AddGuestActivity.this) - Utils.convertDpToPixel(50, AddGuestActivity.this));
        if (!isFinishing()) {
            bottomSheetDialog.show();
        }

        final Spinner makeSpinner = (Spinner) parkingBottomSheetView.findViewById(R.id.spinner_make);
        setSpinnerAdapter(makeSpinner, R.array.car_make);

        final Spinner modelSpinner = (Spinner) parkingBottomSheetView.findViewById(R.id.spinner_model);
        setSpinnerAdapter(modelSpinner, R.array.model);

        final TextInputEditText editVehicleNo = (TextInputEditText) parkingBottomSheetView.findViewById(R.id.edit_vehicle_no);
        final TextView addVehicle = (TextView) parkingBottomSheetView.findViewById(R.id.btn_add_vehicle);
        ImageView ivClose = (ImageView) parkingBottomSheetView.findViewById(R.id.close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String make = makeSpinner.getSelectedItem().toString();
                String model = modelSpinner.getSelectedItem().toString();
                String vehicleNo = editVehicleNo.getText().toString();
                addVehicle(make, model, vehicleNo);
                bottomSheetDialog.dismiss();
            }
        });

    }

    private void addOtherGuestsBottomSheet() {
        View otherGuestBottomSheetView = AddGuestActivity.this.getLayoutInflater().inflate(R.layout.add_other_guests_bottom_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddGuestActivity.this);
        bottomSheetDialog.setContentView(otherGuestBottomSheetView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) otherGuestBottomSheetView.getParent());

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        bottomSheetBehavior.setPeekHeight(Utils.getScreenHeight(AddGuestActivity.this) - Utils.convertDpToPixel(50, AddGuestActivity.this));
        if (!isFinishing()) {
            bottomSheetDialog.show();
        }

        final Spinner genderSpinner = (Spinner) otherGuestBottomSheetView.findViewById(R.id.spinner_gender);
        setSpinnerAdapter(genderSpinner, R.array.gender);

        nameEdit = (TextInputEditText) otherGuestBottomSheetView.findViewById(R.id.edit_name);
        final ImageView contactPickerOther = (ImageView) otherGuestBottomSheetView.findViewById(R.id.contact_picker);
        mobileEdit = (TextInputEditText) otherGuestBottomSheetView.findViewById(R.id.edit_mobile);
        final TextInputEditText ageEdit = (TextInputEditText) otherGuestBottomSheetView.findViewById(R.id.edit_age);
        final TextView addGuest = (TextView) otherGuestBottomSheetView.findViewById(R.id.btn_add_guest);
        ImageView ivClose = (ImageView) otherGuestBottomSheetView.findViewById(R.id.close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        addGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String mobile = mobileEdit.getText().toString();
                String age = ageEdit.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();
                addGuest(name, mobile, age, gender);
                bottomSheetDialog.dismiss();
            }
        });
        contactPickerOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, AppConstants.REQUEST_CODE_PICK_CONTACT_OTHER);
            }
        });
    }

    private void addGuest(String name, String mobile, String age, String gender) {
        if (otherGuests == null) {
            otherGuests = new ArrayList<>();
        }
        int mAge = !TextUtils.isEmpty(age) ? Integer.parseInt(age) : 0;
        Guest guest = new Guest(name, mobile, gender, mAge);
        otherGuests.add(guest);
        refreshOtherGuestList();
    }

    private void addVehicle(String make, String model, String vehicleNo) {
        if (vehicles == null) {
            vehicles = new ArrayList<>();
        }
        Vehicle vehicle = new Vehicle(make, model, vehicleNo);
        vehicles.add(vehicle);
        refreshVehicleList();
    }

    private void refreshVehicleList() {
        VehiclesAdapter adapter = new VehiclesAdapter(AddGuestActivity.this, vehicles, AddGuestActivity.this);
        vehicleRecyclerView.setAdapter(adapter);
    }

    private void refreshOtherGuestList() {
        OtherGuestAdapter adapter = new OtherGuestAdapter(AddGuestActivity.this, otherGuests, AddGuestActivity.this);
        guestRecyclerView.setAdapter(adapter);
    }

    private void setSpinnerAdapter(Spinner spinner, int array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                array, R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    private void pickDate() {
        showDialog(DATE_DIALOG_ID);
    }

    private void pickTime(boolean pickInTime) {
        pickingInTime = pickInTime;
        showDialog(TIME_DIALOG_ID);
    }

    private void setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void updateDate() {
        this.dateOfArrival.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("/")
                        .append(mMonth + 1).append("/")
                        .append(mYear));
    }

    private void updateTime(boolean updateInTime) {
        String hour = mHour < 10 ? "0" + mHour : "" + mHour;
        String minute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
        if (updateInTime) {
            this.inTime.setText(
                    new StringBuilder()
                            .append(hour).append(":")
                            .append(minute).append(":")
                            .append("00")
            );
        } else {
            this.outTime.setText(
                    new StringBuilder()
                            .append(hour).append(":")
                            .append(minute).append(":")
                            .append("00")
            );
        }
    }


    private void addGuestApiCall() {

        //DUmmny code now
        showProgressDialog();
        String requestBody = createRequestBody();
        ApiRequestHelper.requestInvitation(AddGuestActivity.this, requestBody, new Response.Listener<DataModel>() {
            @Override
            public void onResponse(DataModel response) {
                hideProgressDialog();
                if (response instanceof InvitationModel && AppConstants.STATUS_SUCCESS.equalsIgnoreCase(((InvitationModel) response).getStatus())) {
                    showQrScreen(((InvitationModel) response).getData());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                if (error != null && error.networkResponse!=null) {
                    String message = Utils.getErrorMessage(error);
                    if (error.networkResponse.statusCode == 401) {
                        Utils.handleSesionExpire(AddGuestActivity.this, message);
                    } else {
                        Toast.makeText(AddGuestActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private String createRequestBody() {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("name", guestName.getText().toString());
            requestJson.put("mobile", guestPhoneNo.getText().toString());
            requestJson.put("email", guestEmail.getText().toString());
            requestJson.put("age", 34);
            requestJson.put("gender", "MALE");
            requestJson.put("guestType", mPreferredCheckbox.isChecked() ? AppConstants.GUEST_TYPE_PREFERRED : AppConstants.GUEST_TYPE_NORMAL);

            requestJson.put("expectedIn", Utils.getTimeStamp(dateOfArrival.getText().toString(), inTime.getText().toString()));
            requestJson.put("expectedOut", Utils.getTimeStamp(dateOfArrival.getText().toString(), outTime.getText().toString()));
            if (selectedImage != null) {
                String pic = Utils.getBase64Image(this, selectedImage);
                if (!TextUtils.isEmpty(pic)) {
                    requestJson.put("guestPic", pic);
                    requestJson.put("imageContentType", "image/jpeg");
                }
            }

            if (bitmap != null) {
                String pic = Utils.getBase64Image(bitmap);
                if (!TextUtils.isEmpty(pic)) {
                    requestJson.put("guestPic", pic);
                    requestJson.put("imageContentType", "image/jpeg");
                }
            }

            if (vehicles != null) {
                JSONArray vehicleArr = new JSONArray();
                for (Vehicle vehicle : vehicles) {
                    vehicleArr.put(vehicle.toJsonObject());
                }
                requestJson.put("vehicles", vehicleArr);
            }
            if (otherGuests != null) {
                JSONArray guestArr = new JSONArray();
                for (Guest guest : otherGuests) {
                    guestArr.put(guest.toJsonObject());
                }
                requestJson.put("otherGuests", guestArr);
            }
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void showQrScreen(String qrCodeId) {
        Intent intent = new Intent(this, ShowQRActivity.class);
        intent.putExtra(AppConstants.QR_CODE_ID, qrCodeId);
        startActivity(intent);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDate();
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateTime(pickingInTime);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener,
                        0, 0, true);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConstants.REQUEST_CODE_PICK_CONTACT) {
            contactPicked(data);
        } if (resultCode == RESULT_OK && requestCode == AppConstants.REQUEST_CODE_GALLARY) {
            bitmap = (Bitmap) data.getParcelableExtra("BitmapImage");
            displayPic.setImageBitmap(bitmap);
            displayPic.setVisibility(View.VISIBLE);
            uploadPic.setVisibility(View.GONE);
        } if (resultCode == RESULT_OK && requestCode == AppConstants.REQUEST_CODE_PICK_CONTACT_OTHER) {
            contactPickedOther(data);
        }
    }


    private void contactPicked(Intent data) {
        Cursor cursor = null, emailCursor;
        try {
            String phoneNo = null;
            String name = null;
            String email = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            // column index of the contact email
            int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            email = cursor.getString(emailIndex);
            // Set the value to the textviews
            String filterNumber = Utils.removeSpaceAndBracket(phoneNo);
            if (filterNumber.length() > 10)
                filterNumber = Utils.filterMobileNumber(AddGuestActivity.this, filterNumber);

            if (!TextUtils.isEmpty(name)) {
                guestName.setText(name);
                guestName.setSelection(guestName.getText().length());
                guestPhoneNo.requestFocus();
            }

            if (Utils.isValidPhoneNo(filterNumber)) {
                guestPhoneNo.setText(filterNumber);
                guestPhoneNo.setSelection(guestPhoneNo.getText().length());
                guestEmail.requestFocus();
            }

            if (Utils.isValidEmail(email)) {
                guestEmail.setText(email);
                guestEmail.setSelection(guestEmail.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void contactPickedOther(Intent data) {
        Cursor cursor = null, emailCursor;
        try {
            String phoneNo = null;
            String name = null;
            String email = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            // column index of the contact email
            int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            email = cursor.getString(emailIndex);
            // Set the value to the textviews
            String filterNumber = Utils.removeSpaceAndBracket(phoneNo);
            if (filterNumber.length() > 10)
                filterNumber = Utils.filterMobileNumber(AddGuestActivity.this, filterNumber);

            if (!TextUtils.isEmpty(name)) {
                nameEdit.setText(name);
                nameEdit.setSelection(nameEdit.getText().length());
                mobileEdit.requestFocus();
            }

            if (Utils.isValidPhoneNo(filterNumber)) {
                mobileEdit.setText(filterNumber);
                mobileEdit.setSelection(mobileEdit.getText().length());
                mobileEdit.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVehicleRemoved(int position) {
//        vehicles.remove(position);
    }

    @Override
    public void onGuestRemoved(int position) {

    }
}
