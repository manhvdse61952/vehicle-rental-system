package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.City;
import com.example.manhvdse61952.vrc_android.model.api_model.District;
import com.example.manhvdse61952.vrc_android.model.api_model.Vehicle;
import com.example.manhvdse61952.vrc_android.model.api_model.VehicleInformation;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.example.manhvdse61952.vrc_android.controller.validate.ValidateInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateVehicle extends AppCompatActivity {

    VehicleInformation vehicleInfoObj;
    String vehicleType, vehicleTypeGeneral;
    TextView txtCarModel, txt_vehicle_address;
    Spinner spnEngine, spnTranmission, spnYear, spn_receiveType;
    List<String> listEngineType = new ArrayList<>();
    List<String> listTranmissionType = new ArrayList<>();
    String maker, model, year;
    List<String> vehicleYear;
    ProgressDialog dialog;
    EditText edtPlate, edtFrame, edtPriceHour, edtPriceDay, edtDepositFee;
    TextView txtSeat, txtImageFront, txtImageBack, txtImageFrame;
    ImageView imgFront, imgBack, imgFrame, imgCreateVehicle;
    PermissionDevice cameraObj = new PermissionDevice();
    RelativeLayout btnImageFront, btnImageBack, btnImageFrame;
    ValidateInput validObj = new ValidateInput();
    CheckBox cbxHouseHold, cbxIdCard, cbx_haveDriver;
    Button btnCreateVehicle, btn_current_address, btn_write_address;
    private DatabaseReference dbr;
    RadioButton rd_free_full, rd_free_specific;
    LinearLayout ln_free_dow;
    CheckBox cbx_monday, cbx_tuesday, cbx_wednesday, cbx_thursday, cbx_friday, cbx_saturday, cbx_sunday;

    //Value to save in shared preferences
    int required_household_registration = 0, required_id_card = 0;
    String picturePath1 = "", picturePath2 = "", picturePath3 = "", vehicleName = "";
    int districtID = 0, vehicleInfoID;
    LocationManager locationManager;
    LocationListener locationListener;
    double longitude = 0, latitude = 0;
    Boolean monday = true, tuesday = true, wednesday = true, thursday = true, friday = true,
    saturday = true, sunday = true;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void declareID(){
        // Declare id //
        spnEngine = (Spinner) findViewById(R.id.spnEngine);
        spnTranmission = (Spinner) findViewById(R.id.spnTranmission);
        spnYear = (Spinner) findViewById(R.id.spnYear);
        txtCarModel = (TextView) findViewById(R.id.txtCarModel);
        txtSeat = (TextView) findViewById(R.id.txtSeat);
        btnImageFront = (RelativeLayout) findViewById(R.id.btnImageFront);
        btnImageBack = (RelativeLayout) findViewById(R.id.btnImageBack);
        btnImageFrame = (RelativeLayout) findViewById(R.id.btnImageFrame);
        txtImageFront = (TextView) findViewById(R.id.txtImageFront);
        txtImageBack = (TextView) findViewById(R.id.txtImageBack);
        txtImageFrame = (TextView) findViewById(R.id.txtImageFrame);
        imgFront = (ImageView) findViewById(R.id.imgFront);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgFrame = (ImageView) findViewById(R.id.imgFrame);
        edtPlate = (EditText) findViewById(R.id.edtPlate);
        edtFrame = (EditText) findViewById(R.id.edtFrame);
        edtPriceHour = (EditText) findViewById(R.id.edtPriceHour);
        edtPriceDay = (EditText) findViewById(R.id.edtPriceDay);
        edtDepositFee = (EditText) findViewById(R.id.edtDepositFee);
        imgCreateVehicle = (ImageView) findViewById(R.id.imgCreateVehicle);
        cbxHouseHold = (CheckBox) findViewById(R.id.cbxHouseHold);
        cbxIdCard = (CheckBox) findViewById(R.id.cbxIdCard);
        btnCreateVehicle = (Button) findViewById(R.id.btnCreateVehicle);
        txt_vehicle_address = (TextView)findViewById(R.id.txt_vehicle_address);
        btn_current_address = (Button)findViewById(R.id.btn_current_address);
        btn_write_address = (Button)findViewById(R.id.btn_write_address);
        cbx_haveDriver = (CheckBox)findViewById(R.id.cbx_haveDriver);
        spn_receiveType = (Spinner)findViewById(R.id.spn_receiveType);
        rd_free_full = (RadioButton)findViewById(R.id.rd_free_full);
        rd_free_specific = (RadioButton)findViewById(R.id.rd_free_specific);
        ln_free_dow = (LinearLayout)findViewById(R.id.ln_free_dow);
        cbx_monday = (CheckBox)findViewById(R.id.cbx_monday);
        cbx_tuesday = (CheckBox)findViewById(R.id.cbx_tuesday);
        cbx_wednesday = (CheckBox)findViewById(R.id.cbx_wednesday);
        cbx_thursday = (CheckBox)findViewById(R.id.cbx_thursday);
        cbx_friday = (CheckBox)findViewById(R.id.cbx_friday);
        cbx_saturday = (CheckBox)findViewById(R.id.cbx_saturday);
        cbx_sunday = (CheckBox)findViewById(R.id.cbx_sunday);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();

    }

    private void initLayout(){
        // Init toolbar, engine spinner and tranmission spinner ///
        initToolbarAndSpinner();

        //Show car model search edit text
        txtCarModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.VEHICLE_frameNumber, edtFrame.getText().toString());
                editor.putString(ImmutableValue.VEHICLE_plateNumber, edtPlate.getText().toString());
                editor.putString(ImmutableValue.VEHICLE_rentFeePerHours, edtPriceHour.getText().toString());
                editor.putString(ImmutableValue.VEHICLE_rentFeePerDay, edtPriceDay.getText().toString());
                editor.putString(ImmutableValue.VEHICLE_depositFee, edtDepositFee.getText().toString());
                editor.apply();
                Intent it = new Intent(CreateVehicle.this, SearchVehicleInfoActivity.class);
                startActivityForResult(it, 666);
            }
        });

        //Convert price in real time
        edtPriceHour.addTextChangedListener(convertPriceRealTime(edtPriceHour));
        edtPriceDay.addTextChangedListener(convertPriceRealTime(edtPriceDay));
        edtDepositFee.addTextChangedListener(convertPriceRealTime(edtDepositFee));

        //Save value when user select in spinner
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = "";
                year = spnYear.getSelectedItem().toString();
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt(ImmutableValue.VEHICLE_year, position);
                editor.apply();
                getVehicleInfo(maker, model, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnEngine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt(ImmutableValue.VEHICLE_isGasoline, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnTranmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt(ImmutableValue.VEHICLE_isManual, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///// Camera //////
        // Image front
        btnImageFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeImageFront();
            }
        });
        //Image back
        btnImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeImageBack();
            }
        });
        //Image frame
        btnImageFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeImageFrame();
            }
        });

        //Init check box
        initCheckbox();

        // Accept button
        btnCreateVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAction();
            }
        });

        btn_current_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentAddress();
            }
        });

        btn_write_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CreateVehicle.this, SearchAddressVehicle.class);
                startActivityForResult(it, 777);
            }
        });

        //Revert value when user change the layout
        revertValue();

        rd_free_full.setChecked(true);
        GeneralController.scaleView(ln_free_dow, 0);
        rd_free_full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    GeneralController.scaleView(ln_free_dow, 0);
                }
            }
        });

        rd_free_specific.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    GeneralController.scaleView(ln_free_dow, -230);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putString(ImmutableValue.VEHICLE_frameNumber, edtFrame.getText().toString());
        editor.putString(ImmutableValue.VEHICLE_plateNumber, edtPlate.getText().toString());
        editor.putString(ImmutableValue.VEHICLE_rentFeePerHours, edtPriceHour.getText().toString());
        editor.putString(ImmutableValue.VEHICLE_rentFeePerDay, edtPriceDay.getText().toString());
        editor.putString(ImmutableValue.VEHICLE_depositFee, edtDepositFee.getText().toString());
        editor.apply();
        CreateVehicle.this.finish();
        super.onBackPressed();
    }

    /////////////////////////// EXECUTE CODE ////////////////////////////
    //Init toolbar header, engine spinner and tranmission spinner
    private void initToolbarAndSpinner() {
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        vehicleType = editor.getString(ImmutableValue.VEHICLE_vehicleType, ImmutableValue.XE_CA_NHAN);
        vehicleTypeGeneral = vehicleType;
        if (vehicleType.equals(ImmutableValue.XE_MAY)) {
            toolbar_title.setText("Đăng ký xe máy");
            listEngineType.add("XĂNG");
            ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(CreateVehicle.this, android.R.layout.simple_spinner_dropdown_item, listEngineType);
            spnEngine.setAdapter(engineAdapter);
            listTranmissionType.add("XE SỐ");
            listTranmissionType.add("XE TAY GA");
            ArrayAdapter<String> tranmissionAdapter = new ArrayAdapter<>(CreateVehicle.this, android.R.layout.simple_spinner_dropdown_item, listTranmissionType);
            spnTranmission.setAdapter(tranmissionAdapter);

        } else {
            listEngineType.add("XĂNG");
            listEngineType.add("DẦU");
            ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(CreateVehicle.this, android.R.layout.simple_spinner_dropdown_item, listEngineType);
            spnEngine.setAdapter(engineAdapter);
            listTranmissionType.add("SỐ SÀN");
            listTranmissionType.add("SỐ TỰ ĐỘNG");
            ArrayAdapter<String> tranmissionAdapter = new ArrayAdapter<>(CreateVehicle.this, android.R.layout.simple_spinner_dropdown_item, listTranmissionType);
            spnTranmission.setAdapter(tranmissionAdapter);
            if (vehicleType.equals(ImmutableValue.XE_CA_NHAN)) {
                toolbar_title.setText("Đăng ký xe cá nhân");
            } else {
                toolbar_title.setText("Đăng ký xe du lịch");
            }
        }

        if (editor.getInt(ImmutableValue.VEHICLE_isGasoline, -1) != -1 && (vehicleType.equals(ImmutableValue.XE_CA_NHAN) || vehicleType.equals(ImmutableValue.XE_DU_LICH))) {
            spnEngine.setSelection(editor.getInt(ImmutableValue.VEHICLE_isGasoline, -1));
        }
        if (editor.getInt(ImmutableValue.VEHICLE_isManual, -1) != -1 && (vehicleType.equals(ImmutableValue.XE_CA_NHAN) || vehicleType.equals(ImmutableValue.XE_DU_LICH))) {
            spnTranmission.setSelection(editor.getInt(ImmutableValue.VEHICLE_isManual, -1));
        }
    }

    //Init year spinner
    private void getVehicleYear(final String getMaker, final String getModel) {
        dialog = ProgressDialog.show(CreateVehicle.this, "Hệ thống",
                "Đang xử lý ...", true);
        vehicleYear = new ArrayList<>();
        Retrofit test = RetrofitConfig.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleYear(getMaker, getModel);
        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        dialog.dismiss();
                        for (int i = 0; i < response.body().size(); i++) {
                            vehicleYear.add(response.body().get(i).toString());
                        }

                        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(CreateVehicle.this, android.R.layout.simple_spinner_dropdown_item, vehicleYear);
                        spnYear.setAdapter(yearAdapter);
                        SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        int yearPosition = editor.getInt(ImmutableValue.VEHICLE_year, -1);
                        if (yearPosition != -1) {
                            spnYear.setSelection(yearPosition);
                        }
                    }
                } else {
                    Toast.makeText(CreateVehicle.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreateVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Init seat data - get vehicleInfo
    public void getVehicleInfo(final String getMaker, final String getModel, final String getYear) {
        dialog = ProgressDialog.show(CreateVehicle.this, "Hệ thống",
                "Đang xử lý ...", true);
        vehicleInfoID = 0;
        vehicleInfoObj = new VehicleInformation();
        Retrofit test = RetrofitConfig.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<VehicleInformation> responseBodyCall = testAPI.getVehicleInfo(getMaker, getModel, getYear);

        responseBodyCall.enqueue(new Callback<VehicleInformation>() {
            @Override
            public void onResponse(Call<VehicleInformation> call, Response<VehicleInformation> response) {
                if (response.body() != null) {
                    vehicleType = "";
                    vehicleInfoObj = response.body();
                    vehicleInfoID = vehicleInfoObj.getId();
                    txtSeat.setText(vehicleInfoObj.getSeat() + "");
                    vehicleType = vehicleInfoObj.getVehicleType();
                    if (vehicleType.equals("XE_MAY")) {
                        Boolean isScooter = vehicleInfoObj.getScooter();
                        if (isScooter == true) {
                            spnTranmission.setSelection(1);
                        } else {
                            spnTranmission.setSelection(0);
                        }
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<VehicleInformation> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreateVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Init checkbox
    private void initCheckbox() {
        cbxHouseHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    required_household_registration = 1;
                } else {
                    required_household_registration = 0;
                }
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt(ImmutableValue.VEHICLE_requireHouseHold, required_household_registration);
                editor.apply();
            }
        });
        cbxIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    required_id_card = 1;
                } else {
                    required_id_card = 0;
                }
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt(ImmutableValue.VEHICLE_requireIdCard, required_id_card);
                editor.apply();
            }
        });
    }

    //Send value to policy intent
    public void nextAction() {
        int isGasoline = 0, isManual = 0;
        String tranmission = spnTranmission.getSelectedItem().toString();
        String engineType = spnEngine.getSelectedItem().toString();
        if (tranmission.trim().equals("SỐ SÀN") || tranmission.trim().equals("XE SỐ")) {
            isManual = 1;
        } else {
            isManual = 0;
        }
        if (engineType.trim().equals("XĂNG")) {
            isGasoline = 1;
        } else {
            isGasoline = 0;
        }

        if (rd_free_full.isChecked()){
            cbx_monday.setChecked(false);
            cbx_tuesday.setChecked(false);
            cbx_wednesday.setChecked(false);
            cbx_thursday.setChecked(false);
            cbx_friday.setChecked(false);
            cbx_saturday.setChecked(false);
            cbx_sunday.setChecked(false);

            monday = false;
            tuesday = false;
            wednesday = false;
            thursday = false;
            friday = false;
            saturday = false;
            sunday = false;
        }

        if (cbx_monday.isChecked()){
            monday = false;
        }
        if (cbx_tuesday.isChecked()){
            tuesday = false;
        }
        if (cbx_wednesday.isChecked()){
            wednesday = false;
        }
        if (cbx_thursday.isChecked()){
            thursday = false;
        }
        if (cbx_friday.isChecked()){
            friday = false;
        }
        if (cbx_saturday.isChecked()){
            saturday = false;
        }
        if (cbx_sunday.isChecked()){
            sunday = false;
        }


        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        longitude = Double.parseDouble(editor2.getString(ImmutableValue.MAIN_vehicleLng, "0"));
        latitude = Double.parseDouble(editor2.getString(ImmutableValue.MAIN_vehicleLat, "0"));
        String district = PermissionDevice.getStringDistrict(longitude, latitude, CreateVehicle.this);
        getDistrictIdByName(district);


        String hourPriceTemp = edtPriceHour.getText().toString().trim().replaceAll(",", "");
        String dayPriceTemp = edtPriceDay.getText().toString().trim().replaceAll(",", "");
        String depositPriceTemp = edtDepositFee.getText().toString().trim().replaceAll(",", "");
        Boolean haveDriver = false;
        if (cbx_haveDriver.isChecked()){
            haveDriver = true;
        } else {
            haveDriver = false;
        }
        int receiveType = 0;
        receiveType = spn_receiveType.getSelectedItemPosition();


        Boolean checkPlateNumber = validObj.validFrameNumber(edtPlate.getText().toString().trim(), edtPlate);
        Boolean checkFrameNumber = validObj.validFrameNumber(edtFrame.getText().toString().trim(), edtFrame);
        Boolean checkPricePerHours = validObj.validPrice(hourPriceTemp, edtPriceHour);
        Boolean checkPricePerDay = validObj.validPrice(dayPriceTemp, edtPriceDay);
        Boolean checkDepositFee = validObj.validPrice(depositPriceTemp, edtDepositFee);
        Boolean checkImage1 = validObj.validImageLink(picturePath1, CreateVehicle.this);
        Boolean checkImage2 = validObj.validImageLink(picturePath2, CreateVehicle.this);
        Boolean checkImage3 = validObj.validImageLink(picturePath3, CreateVehicle.this);
        Boolean checkVehicleName = validObj.validVehicleName(vehicleName.trim(), CreateVehicle.this);

        if (!vehicleType.equals(vehicleTypeGeneral)) {
            Toast.makeText(this, "Không đúng loại xe! Hãy chọn xe khác", Toast.LENGTH_SHORT).show();
        } else {
            if (districtID == 0){
                Toast.makeText(this, "Hệ thống chỉ hỗ trợ ở HCM, Đà Nẵng và Hà Nội", Toast.LENGTH_SHORT).show();
            } else if (rd_free_specific.isChecked() && monday == true && tuesday == true
                    && wednesday == true && thursday == true && friday == true && saturday == true && sunday == true){
                Toast.makeText(this, "Vui lòng chọn ít nhất một thứ trong tuần", Toast.LENGTH_SHORT).show();
            }
            else {
                if (checkFrameNumber && checkPlateNumber && checkPricePerHours && checkPricePerDay
                        && checkDepositFee && checkImage1 && checkImage2 && checkImage3 && checkVehicleName
                        && districtID != 0) {
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_frameNumber, edtFrame.getText().toString());
                    editor.putString(ImmutableValue.VEHICLE_plateNumber, edtPlate.getText().toString());
                    editor.putString(ImmutableValue.VEHICLE_rentFeePerHours, edtPriceHour.getText().toString());
                    editor.putString(ImmutableValue.VEHICLE_rentFeePerDay, edtPriceDay.getText().toString());
                    editor.putString(ImmutableValue.VEHICLE_depositFee, edtDepositFee.getText().toString());
                    editor.apply();



                    ProgressDialog dialog = ProgressDialog.show(CreateVehicle.this, "Đang xử lý",
                            "Vui lòng đợi ...", true);
                    checkFrameNumber(edtFrame.getText().toString().trim(), dialog, edtFrame,
                            vehicleInfoID, districtID, dayPriceTemp,
                            hourPriceTemp, depositPriceTemp, edtPlate.getText().toString().trim(),
                            required_household_registration, required_id_card,
                            isGasoline, isManual, picturePath2, picturePath3, picturePath1, haveDriver, receiveType, monday,
                            tuesday, wednesday, thursday, friday, saturday, sunday);
                }
            }

        }

    }

    //Format price in real time
    private TextWatcher convertPriceRealTime(final EditText edt) {
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

    private void revertValue() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String vehicleMaker = editor.getString(ImmutableValue.VEHICLE_vehicleMaker, "Empty");
        String vehicleModel = editor.getString(ImmutableValue.VEHICLE_vehicleModel, "Empty");
        String picture1 = editor.getString(ImmutableValue.VEHICLE_img_vehicle_1, "");
        String picture2 = editor.getString(ImmutableValue.VEHICLE_img_vehicle_2, "");
        String picture3 = editor.getString(ImmutableValue.VEHICLE_img_frameNumber, "");
        String frameNumber = editor.getString(ImmutableValue.VEHICLE_frameNumber, "");
        String plateNumber = editor.getString(ImmutableValue.VEHICLE_plateNumber, "");
        String rentPerHour = editor.getString(ImmutableValue.VEHICLE_rentFeePerHours, "");
        String rentPerDay = editor.getString(ImmutableValue.VEHICLE_rentFeePerDay, "");
        String deposit = editor.getString(ImmutableValue.VEHICLE_depositFee, "");
        int requireHouseHold = editor.getInt(ImmutableValue.VEHICLE_requireHouseHold, 0);
        int requireIdCard = editor.getInt(ImmutableValue.VEHICLE_requireIdCard, 0);
        String vehicleAddress = editor2.getString(ImmutableValue.MAIN_vehicleAddress, "");
        if (requireHouseHold != 0) {
            cbxHouseHold.setChecked(true);
        }
        if (requireIdCard != 0) {
            cbxIdCard.setChecked(true);
        }
        if (!vehicleAddress.equals("")){
            txt_vehicle_address.setText(vehicleAddress);
        }
        if (vehicleMaker.equals("Empty") == false && vehicleModel.equals("Empty") == false) {
            txtCarModel.setText(vehicleMaker + " " + vehicleModel);
            vehicleName = vehicleMaker + " " + vehicleModel;
            getVehicleYear(vehicleMaker, vehicleModel);
            maker = vehicleMaker;
            model = vehicleModel;
        }
        if (!rentPerHour.equals("")) {
            edtPriceHour.setText(rentPerHour);
        }
        if (!rentPerDay.equals("")) {
            edtPriceDay.setText(rentPerDay);
        }
        if (!deposit.equals("")) {
            edtDepositFee.setText(deposit);
        }
        if (!frameNumber.equals("")) {
            edtFrame.setText(frameNumber);
        }
        if (!plateNumber.equals("")) {
            edtPlate.setText(plateNumber);
        }
        if (!picture1.equals("")) {
            picturePath1 = picture1;
            File imgFile = new File(picture1);
            Picasso.get().load(imgFile).into(imgFront);
            txtImageFront.setVisibility(View.INVISIBLE);
        }
        if (!picture2.equals("")) {
            picturePath2 = picture2;
            File imgFile = new File(picture2);
            Picasso.get().load(imgFile).into(imgBack);
            txtImageBack.setVisibility(View.INVISIBLE);
        }
        if (!picture3.equals("")) {
            picturePath3 = picture3;
            File imgFile = new File(picture3);
            Picasso.get().load(imgFile).into(imgFrame);
            txtImageFrame.setVisibility(View.INVISIBLE);
        }
    }

    /////////////////////////// THIS CODE USE FOR EXECUTE IMAGE ////////////////////////////////////
    //Execute image front
    public void executeImageFront() {
        final CharSequence[] items = {"Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateVehicle.this);
        builder.setTitle("Ảnh đầu xe");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")) {
                    cameraObj.takePicture(CreateVehicle.this, CreateVehicle.this, PermissionDevice.CAMERA_VEHICLE_CODE_1);
                } else if (items[which].equals("Chọn ảnh từ thư viện")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, PermissionDevice.CAMERA_SELECT_IMAGE_CODE_1);
                } else if (items[which].equals("Hủy")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Execute image back
    public void executeImageBack() {
        final CharSequence[] items = {"Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateVehicle.this);
        builder.setTitle("Ảnh đuôi xe");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")) {
                    cameraObj.takePicture(CreateVehicle.this, CreateVehicle.this, PermissionDevice.CAMERA_VEHICLE_CODE_2);
                } else if (items[which].equals("Chọn ảnh từ thư viện")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, PermissionDevice.CAMERA_SELECT_IMAGE_CODE_2);
                } else if (items[which].equals("Hủy")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Execute image frame
    public void executeImageFrame() {
        final CharSequence[] items = {"Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateVehicle.this);
        builder.setTitle("Ảnh giấy đăng ký xe");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")) {
                    cameraObj.takePicture(CreateVehicle.this, CreateVehicle.this, PermissionDevice.CAMERA_VEHICLE_CODE_3);
                } else if (items[which].equals("Chọn ảnh từ thư viện")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, PermissionDevice.CAMERA_SELECT_IMAGE_CODE_3);
                } else if (items[which].equals("Hủy")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PermissionDevice.CAMERA_VEHICLE_CODE_1:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgFront, CreateVehicle.this);
                    picturePath1 = PermissionDevice.picturePath;
                    txtImageFront.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_img_vehicle_1, picturePath1);
                    editor.apply();
                }
                break;
            case PermissionDevice.CAMERA_VEHICLE_CODE_2:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgBack, CreateVehicle.this);
                    picturePath2 = PermissionDevice.picturePath;
                    txtImageBack.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_img_vehicle_2, picturePath2);
                    editor.apply();
                }
                break;
            case PermissionDevice.CAMERA_VEHICLE_CODE_3:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgFrame, CreateVehicle.this);
                    picturePath3 = PermissionDevice.picturePath;
                    txtImageFrame.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_img_frameNumber, picturePath3);
                    editor.apply();
                }
                break;
            case PermissionDevice.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    picturePath1 = cameraObj.showImageGallery(data, imgFront, CreateVehicle.this);
                    txtImageFront.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_img_vehicle_1, picturePath1);
                    editor.apply();
                }
                break;
            case PermissionDevice.CAMERA_SELECT_IMAGE_CODE_2:
                if (resultCode == RESULT_OK) {
                    picturePath2 = cameraObj.showImageGallery(data, imgBack, CreateVehicle.this);
                    txtImageBack.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_img_vehicle_2, picturePath2);
                    editor.apply();
                }
                break;
            case PermissionDevice.CAMERA_SELECT_IMAGE_CODE_3:
                if (resultCode == RESULT_OK) {
                    picturePath3 = cameraObj.showImageGallery(data, imgFrame, CreateVehicle.this);
                    txtImageFrame.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.VEHICLE_img_frameNumber, picturePath3);
                    editor.apply();
                }
                break;
            case 666:
                if (resultCode == Activity.RESULT_OK){
                    revertValue();
                }
                break;
            case 777:
                if (resultCode == Activity.RESULT_OK){
                    initLayout();
                }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void checkFrameNumber(final String frameNumber,
                                 final ProgressDialog progressDialog, final EditText edt,
                                 final int vehicleInformationID, final int districtID,
                                 final String rentFeePerDay, final String rentFeePerHours, final String depositFee,
                                 final String plateNumber, final int requireHouseHold, final int requireIdCard,
                                 final int isGasoline, final int isManual, final String img_vehicle_1, final String img_vehicle_2,
                                 final String picture_path, final Boolean haveDriver, final int receiveType,
                                 final Boolean monday, final Boolean tuesday, final Boolean wednesday,
                                 final Boolean thursday, final Boolean friday, final Boolean saturday, final Boolean sunday) {
        Retrofit test = RetrofitConfig.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkDuplicatedFrameNumber(frameNumber);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    if (response.body().toString().equals("true")) {
                        edt.setError("Số khung đã có người sử dụng");
                        edt.requestFocus();
                        progressDialog.dismiss();
                    } else {
                        createNewVehicle(frameNumber, vehicleInformationID, rentFeePerDay, rentFeePerHours,
                                depositFee, plateNumber, requireHouseHold, requireIdCard, districtID, isGasoline,
                                isManual, picture_path, img_vehicle_1, img_vehicle_2, haveDriver, receiveType, monday,
                                tuesday, wednesday, thursday, friday, saturday, sunday);
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CreateVehicle.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewVehicle(final String frameNumber, int vehicleInformationID, String rentFeePerDay, String rentFeePerHours,
                                  String depositFee, String plateNumber, int requireHouseHold, int requireIdCard, int districtID,
                                  int isGasoline, int isManual, String img_vehicle_1, String img_vehicle_2,
                                  String picture_path, Boolean haveDriver, int receiveType, final Boolean monday, final Boolean tuesday, final Boolean wednesday,
                                  final Boolean thursday, final Boolean friday, final Boolean saturday, final Boolean sunday){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int userID = editor.getInt(ImmutableValue.HOME_userID, 0);
        String description = "";
        String rentFeePerSlot = "0";

        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicleObj = new Vehicle(frameNumber, userID, vehicleInformationID, description, Float.valueOf(rentFeePerSlot),
                Float.valueOf(rentFeePerDay), Float.valueOf(rentFeePerHours), Float.valueOf(depositFee), plateNumber,
                requireHouseHold, requireIdCard, districtID, isGasoline, isManual, longitude, latitude, haveDriver, receiveType
        , monday, tuesday, wednesday, thursday, friday, saturday, sunday);

        try {
            String json = objectMapper.writeValueAsString(vehicleObj);
            String IMG_JPEG = "image/jpeg";
            File imageVehicleFile1 = new File(img_vehicle_1);
            File imageVehicleFile2 = new File(img_vehicle_2);
            File imageFile = new File(picture_path);


            RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile1);
            RequestBody fileBody2 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile2);
            RequestBody fileBody3 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);



            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), json);

            MultipartBody.Part body = MultipartBody.Part.createFormData("files", imageVehicleFile1.getName(), fileBody);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("files", imageVehicleFile2.getName(), fileBody2);
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("files", imageFile.getName(), fileBody3);
            MultipartBody.Part[] imagesParts = new MultipartBody.Part[3];
            imagesParts[0] = body;
            imagesParts[1] = body2;
            imagesParts[2] = body3;

            Retrofit test = RetrofitConfig.getClient();
            final VehicleAPI testAPI = test.create(VehicleAPI.class);
            Call<ResponseBody> responseBodyCall = testAPI.createVehicle(data, imagesParts);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateVehicle.this);
                        builder.setMessage("Yêu cầu đăng kí xe thành công! Bạn vui lòng đợi từ 3-5 phút để chúng tôi xác nhận và gửi thông báo về thiết bị");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences settings = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                settings.edit().clear().commit();
                                SharedPreferences settings2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                settings2.edit().clear().commit();
                                dbr = FirebaseDatabase.getInstance().getReference("Locations").child(frameNumber);
                                dbr.child("latitude").setValue(latitude);
                                dbr.child("longitude").setValue(longitude);

                                CreateVehicle.this.finish();
                                Intent it = new Intent(CreateVehicle.this, MainActivity.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(it);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(CreateVehicle.this, "Đã xả ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(CreateVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void getDistrictIdByName(String districtName) {
        districtName = districtName.toLowerCase();
        districtName = GeneralController.removeAccentCharacter(districtName);
        districtName = districtName.replace("quan", "");
        districtName = districtName.replace("huyen", "");
        List<City> listCity = ImmutableValue.listGeneralAddress;
        districtID = 63;
        for (int i = 0; i < listCity.size(); i++) {
            List<District> listDistrict = listCity.get(i).getDistrict();
            for (int j = 0; j < listDistrict.size(); j++) {
                String districtConvert = listDistrict.get(j).getDistrictName().toLowerCase();
                districtConvert = GeneralController.removeAccentCharacter(districtConvert);
                districtConvert = districtConvert.replace("quan", "");
                districtConvert = districtConvert.replace("huyen", "");
                if (districtConvert.trim().equals(districtName)) {
                    districtID = listDistrict.get(j).getId();
                    break;
                }

            }
        }
    }

    private void showCurrentAddress() {
        dialog = ProgressDialog.show(CreateVehicle.this, "Hệ thống",
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
//                        String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, CreateVehicle.this);
//                        txt_vehicle_address.setText(currentAddress);
//                        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
//                        editor.putString(ImmutableValue.MAIN_vehicleAddress, currentAddress);
//                        editor.putString(ImmutableValue.MAIN_vehicleLat, String.valueOf(location.getLatitude()));
//                        editor.putString(ImmutableValue.MAIN_vehicleLng, String.valueOf(location.getLongitude()));
//                        editor.apply();
//                        locationManager.removeUpdates(locationListener);
//                        dialog.dismiss();
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
                if (ActivityCompat.checkSelfPermission(CreateVehicle.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    longitude = lastLocation.getLongitude();
                    latitude = lastLocation.getLatitude();
                    String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, CreateVehicle.this);
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
                    String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, CreateVehicle.this);
                    txt_vehicle_address.setText(currentAddress);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.MAIN_vehicleAddress, currentAddress);
                    editor.putString(ImmutableValue.MAIN_vehicleLat, String.valueOf(latitude));
                    editor.putString(ImmutableValue.MAIN_vehicleLng, String.valueOf(longitude));
                    editor.apply();
                    dialog.dismiss();
                }
            }
        }, 500);
    }

}
