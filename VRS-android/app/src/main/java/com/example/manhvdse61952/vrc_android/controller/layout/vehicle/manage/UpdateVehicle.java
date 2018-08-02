package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.VehicleUpdate;
import com.example.manhvdse61952.vrc_android.model.search_model.DetailVehicleItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateVehicle extends AppCompatActivity {
    ImageView img_vehicle;
    TextView txt_vehicle_name, txt_vehicle_address, item_year, txt_fixed, item_engine, item_tranmission, item_plateNumber, item_seat, item_ownerName;
    EditText edtPriceHour, edtPriceDay, edtDepositFee, edt_vehicle_description;
    Switch swt_vehicle_address;
    LinearLayout ln_vehicle_address, ln_fixed;
    Button btn_current_address, btn_write_address, btn_update;
    CheckBox cbxHouseHold, cbxIdCard;
    ProgressDialog dialog;
    Boolean isOpen = true;
    String frameNumber = "";
    float rentFeePerHour = 0, rentFeePerDay = 0, depositFee = 0;

    LocationManager locationManager;
    LocationListener locationListener;
    double longitude = 0, latitude = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();

        edtDepositFee.addTextChangedListener(convertDepositFee(edtDepositFee));
        edtPriceHour.addTextChangedListener(convertRentFeePerHour(edtPriceHour));
        edtPriceDay.addTextChangedListener(convertRentFeePerDay(edtPriceDay));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();
        UpdateVehicle.this.finish();
        super.onBackPressed();
    }

    private void declareID() {
        img_vehicle = (ImageView) findViewById(R.id.img_vehicle);
        txt_vehicle_name = (TextView) findViewById(R.id.txt_vehicle_name);
        txt_vehicle_address = (TextView) findViewById(R.id.txt_vehicle_address);
        item_seat = (TextView) findViewById(R.id.item_seat);
        item_year = (TextView) findViewById(R.id.item_year);
        item_engine = (TextView) findViewById(R.id.item_engine);
        item_tranmission = (TextView) findViewById(R.id.item_tranmission);
        item_plateNumber = (TextView) findViewById(R.id.item_plateNumber);
        item_ownerName = (TextView) findViewById(R.id.item_ownerName);
        edtPriceHour = (EditText) findViewById(R.id.edtPriceHour);
        edtPriceDay = (EditText) findViewById(R.id.edtPriceDay);
        edtDepositFee = (EditText) findViewById(R.id.edtDepositFee);
        edt_vehicle_description = (EditText) findViewById(R.id.edt_vehicle_description);
        swt_vehicle_address = (Switch) findViewById(R.id.swt_vehicle_address);
        ln_vehicle_address = (LinearLayout) findViewById(R.id.ln_vehicle_address);
        btn_current_address = (Button) findViewById(R.id.btn_current_address);
        btn_write_address = (Button) findViewById(R.id.btn_write_address);
        cbxHouseHold = (CheckBox) findViewById(R.id.cbxHouseHold);
        cbxIdCard = (CheckBox) findViewById(R.id.cbxIdCard);
        txt_fixed = (TextView) findViewById(R.id.txt_fixed);
        ln_fixed = (LinearLayout) findViewById(R.id.ln_fixed);
        btn_update = (Button) findViewById(R.id.btn_update);
    }

    private void initLayout() {
        dialog = ProgressDialog.show(UpdateVehicle.this, "Hệ thống",
                "Vui lòng đợi ...", true);
        final SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        frameNumber = editor.getString(ImmutableValue.MAIN_vehicleID, "Empty");
        Retrofit test = RetrofitConfig.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<DetailVehicleItem> responseBodyCall = testAPI.getVehicleByFrameNumber(frameNumber);
        responseBodyCall.enqueue(new Callback<DetailVehicleItem>() {
            @Override
            public void onResponse(Call<DetailVehicleItem> call, Response<DetailVehicleItem> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        GeneralController.scaleView(ln_vehicle_address, 0);
                        swt_vehicle_address.setChecked(true);
                        DetailVehicleItem obj = response.body();
                        Picasso.get().load(obj.getImageLinkFront()).into(img_vehicle);
                        txt_vehicle_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
                        item_seat.setText(obj.getSeat() + "");
                        item_year.setText(obj.getModelYear() + "");
                        if (obj.getGasoline() == true) {
                            item_engine.setText("Xăng");
                        } else {
                            item_engine.setText("Dầu");
                        }
                        if (obj.getVehicleType().equals(ImmutableValue.XE_MAY)) {
                            if (obj.getScooter() == true) {
                                item_tranmission.setText("Xe tay ga");
                            } else {
                                item_tranmission.setText("Xe số");
                            }
                        } else {
                            if (obj.getManual() == true) {
                                item_tranmission.setText("Số tự động");
                            } else {
                                item_tranmission.setText("Số sàn");
                            }
                        }
                        item_plateNumber.setText(obj.getPlateNumber());
                        item_ownerName.setText(obj.getOwnerFullName());

                        NumberFormat nf = new DecimalFormat("#.####");
                        String priceHour = GeneralController.convertPrice(nf.format(obj.getRentFeePerHour()));
                        edtPriceHour.setText(priceHour);

                        String priceDay = GeneralController.convertPrice(nf.format(obj.getRentFeePerDay()));
                        edtPriceDay.setText(priceDay);

                        String deposit = GeneralController.convertPrice(nf.format(obj.getDeposit()));
                        edtDepositFee.setText(deposit);

                        if (obj.getRequireHouseHold() == true) {
                            cbxHouseHold.setChecked(true);
                        } else {
                            cbxHouseHold.setChecked(false);
                        }

                        if (obj.getRequireIdCard() == true) {
                            cbxIdCard.setChecked(true);
                        } else {
                            cbxIdCard.setChecked(false);
                        }

                        edt_vehicle_description.setText(obj.getDescription() + "");
                        swt_vehicle_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    GeneralController.scaleView(ln_vehicle_address, 0);
                                } else {
                                    GeneralController.scaleView(ln_vehicle_address, -70);
                                }
                            }
                        });


                        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        String address = editor.getString(ImmutableValue.MAIN_vehicleAddress, "Empty");
                        String latitudeReceive = editor.getString(ImmutableValue.MAIN_vehicleLat, "Empty");
                        String longitudeReceive = editor.getString(ImmutableValue.MAIN_vehicleLng, "Empty");
                        if (!latitudeReceive.equals("Empty") && !longitudeReceive.equals("Empty")) {
                            longitude = Double.parseDouble(longitudeReceive);
                            latitude = Double.parseDouble(latitudeReceive);
                        }
                        if (!address.equals("Empty")) {
                            txt_vehicle_address.setText(address);
                        } else {
                            String addressDB = PermissionDevice.getStringAddress(obj.getLongitude(), obj.getLatitude(), UpdateVehicle.this);
                            txt_vehicle_address.setText(addressDB + "");
                        }

                        btn_current_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showCurrentAddress();
                            }
                        });

                        btn_write_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(UpdateVehicle.this, SearchAddressVehicle.class);
                                startActivityForResult(it, 1);
                            }
                        });

                        txt_fixed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isOpen == false) {
                                    GeneralController.scaleView(ln_fixed, -260);
                                    isOpen = true;
                                } else {
                                    GeneralController.scaleView(ln_fixed, 0);
                                    isOpen = false;
                                }

                            }
                        });

                        btn_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateVehicle.this);
                                builder.setMessage("Bạn có chắc chắn muốn thay đổi không ?").setCancelable(false)
                                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                updateAction();
                                            }
                                        })
                                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                dialog.setCanceledOnTouchOutside(false);
                            }
                        });
                    }
                } else {
                    Toast.makeText(UpdateVehicle.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<DetailVehicleItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UpdateVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCurrentAddress() {
        dialog = ProgressDialog.show(UpdateVehicle.this, "Hệ thống",
                "Đang xử lý", true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
//                        longitude = location.getLongitude();
//                        latitude = location.getLatitude();
//                        String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, UpdateVehicle.this);
//                        txt_vehicle_address.setText(currentAddress);
//                        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
//                        editor.putString(ImmutableValue.MAIN_vehicleAddress, currentAddress);
//                        editor.putString(ImmutableValue.MAIN_vehicleLat, String.valueOf(location.getLatitude()));
//                        editor.putString(ImmutableValue.MAIN_vehicleLng, String.valueOf(location.getLongitude()));
//                        editor.apply();
//                        locationManager.removeUpdates(locationListener);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ActivityCompat.checkSelfPermission(UpdateVehicle.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    longitude = lastLocation.getLongitude();
                    latitude = lastLocation.getLatitude();
                    String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, UpdateVehicle.this);
                    txt_vehicle_address.setText(currentAddress);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.MAIN_vehicleAddress, currentAddress);
                    editor.putString(ImmutableValue.MAIN_vehicleLat, String.valueOf(latitude));
                    editor.putString(ImmutableValue.MAIN_vehicleLng, String.valueOf(longitude));
                    editor.apply();
                    dialog.dismiss();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = lastLocation.getLongitude();
                    latitude = lastLocation.getLatitude();
                    String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, UpdateVehicle.this);
                    txt_vehicle_address.setText(currentAddress);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.MAIN_vehicleAddress, currentAddress);
                    editor.putString(ImmutableValue.MAIN_vehicleLat, String.valueOf(latitude));
                    editor.putString(ImmutableValue.MAIN_vehicleLng, String.valueOf(longitude));
                    editor.apply();
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        }, 500);
    }

    private void updateAction() {
        if (edtPriceHour.getText().toString().trim().equals("")
                || edtPriceDay.getText().toString().trim().equals("")
                || edtDepositFee.getText().toString().trim().equals("")
                || depositFee < 10000 || rentFeePerHour < 10000 || rentFeePerDay < 10000) {
            Toast.makeText(this, "Số tiền cần lớn hơn 10,000", Toast.LENGTH_SHORT).show();
        } else {
            dialog = ProgressDialog.show(UpdateVehicle.this, "Hệ thống",
                    "Vui lòng đợi ...", true);
            VehicleUpdate obj = new VehicleUpdate();
            obj.setDepositFee(depositFee);
            obj.setRentFeeByDay(rentFeePerDay);
            obj.setRentFeeByHour(rentFeePerHour);
            if (cbxHouseHold.isChecked()) {
                obj.setRequiredHouseHold(true);
            } else {
                obj.setRequiredHouseHold(false);
            }
            if (cbxIdCard.isChecked()) {
                obj.setRequiredID(true);
            } else {
                obj.setRequiredID(false);
            }
            obj.setDescription(edt_vehicle_description.getText().toString() + "");
            obj.setAddress(txt_vehicle_address.getText().toString() + "");
            obj.setLongitude(String.valueOf(longitude));
            obj.setLatitude(String.valueOf(latitude));

            Retrofit test = RetrofitConfig.getClient();
            VehicleAPI vehicleAPI = test.create(VehicleAPI.class);
            Call<ResponseBody> responseBodyCall = vehicleAPI.updateVehicle(frameNumber, obj);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(UpdateVehicle.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        SharedPreferences settings = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        settings.edit().clear().commit();
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(UpdateVehicle.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(UpdateVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private TextWatcher convertRentFeePerDay(final EditText edt) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (!originalString.equals("")) {
                        rentFeePerDay = Float.parseFloat(originalString);
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt.setText(formattedString);
                    edt.setSelection(edt.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt.addTextChangedListener(this);
            }
        };
    }

    private TextWatcher convertRentFeePerHour(final EditText edt) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (!originalString.equals("")) {
                        rentFeePerHour = Float.parseFloat(originalString);
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt.setText(formattedString);
                    edt.setSelection(edt.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt.addTextChangedListener(this);
            }
        };
    }

    private TextWatcher convertDepositFee(final EditText edt) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (!originalString.equals("")) {
                        depositFee = Float.parseFloat(originalString);
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt.setText(formattedString);
                    edt.setSelection(edt.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt.addTextChangedListener(this);
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                initLayout();
            }
        }
    }
}
