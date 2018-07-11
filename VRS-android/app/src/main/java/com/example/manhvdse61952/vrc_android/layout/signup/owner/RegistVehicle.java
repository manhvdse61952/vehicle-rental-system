package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.main.SearchAddressModel;
import com.example.manhvdse61952.vrc_android.model.apiModel.City;
import com.example.manhvdse61952.vrc_android.model.apiModel.District;
import com.example.manhvdse61952.vrc_android.model.apiModel.VehicleInformation;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.example.manhvdse61952.vrc_android.remote.Validate;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistVehicle extends AppCompatActivity {

    VehicleInformation vehicleInfoObj;
    String vehicleType, vehicleTypeGeneral;
    TextView txtCarModel;
    Spinner spnEngine, spnTranmission, spnYear, spnCity, spnDistrict;
    List<String> listEngineType = new ArrayList<>();
    List<String> listTranmissionType = new ArrayList<>();
    String maker, model, year;
    List<String> vehicleYear;
    ProgressDialog dialog;
    EditText edtPlate, edtFrame, edtPriceHour, edtPriceDay, edtDepositFee;
    TextView txtSeat, txtImageFront, txtImageBack, txtImageFrame;
    ImageView imgFront, imgBack, imgFrame, imgCreateVehicle;
    ImmutableValue cameraObj = new ImmutableValue();
    RelativeLayout btnImageFront, btnImageBack, btnImageFrame;
    Validate validObj = new Validate();
    CheckBox cbxHouseHold, cbxIdCard;
    Button btnCreateVehicle;

    //Value to save in shared preferences
    int required_household_registration = 0, required_id_card = 0;
    String picturePath1 = "", picturePath2 = "", picturePath3 = "", vehicleName = "";
    int cityPosition = 0, districtID, vehicleInfoID;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

        // Declare id //
        spnEngine = (Spinner) findViewById(R.id.spnEngine);
        spnTranmission = (Spinner) findViewById(R.id.spnTranmission);
        spnYear = (Spinner) findViewById(R.id.spnYear);
        txtCarModel = (TextView) findViewById(R.id.txtCarModel);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnDistrict = (Spinner) findViewById(R.id.spnDistrict);
        txtSeat = (TextView) findViewById(R.id.txtSeat);
        btnImageFront = (RelativeLayout) findViewById(R.id.btnImageFront);
        btnImageBack = (RelativeLayout) findViewById(R.id.btnImageBack);
        btnImageFrame = (RelativeLayout) findViewById(R.id.btnImageFrame);
        txtImageFront = (TextView) findViewById(R.id.txtImageFront);
        txtImageBack = (TextView)findViewById(R.id.txtImageBack);
        txtImageFrame = (TextView)findViewById(R.id.txtImageFrame);
        imgFront = (ImageView)findViewById(R.id.imgFront);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgFrame = (ImageView)findViewById(R.id.imgFrame);
        edtPlate = (EditText)findViewById(R.id.edtPlate);
        edtFrame = (EditText)findViewById(R.id.edtFrame);
        edtPriceHour = (EditText)findViewById(R.id.edtPriceHour);
        edtPriceDay = (EditText)findViewById(R.id.edtPriceDay);
        edtDepositFee = (EditText)findViewById(R.id.edtDepositFee);
        imgCreateVehicle = (ImageView)findViewById(R.id.imgCreateVehicle);
        cbxHouseHold = (CheckBox)findViewById(R.id.cbxHouseHold);
        cbxIdCard = (CheckBox)findViewById(R.id.cbxIdCard);
        btnCreateVehicle = (Button)findViewById(R.id.btnCreateVehicle);

        // Init toolbar, engine spinner and tranmission spinner ///
        initToolbarAndSpinner();

        //Show car model search edit text
        txtCarModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("frameNumber", edtFrame.getText().toString());
                editor.putString("plateNumber", edtPlate.getText().toString());
                editor.putString("rentFeePerHours", edtPriceHour.getText().toString());
                editor.putString("rentFeePerDay", edtPriceDay.getText().toString());
                editor.putString("depositFee", edtDepositFee.getText().toString());
                editor.apply();
                Intent it = new Intent(RegistVehicle.this, SearchVehicleInfoActivity.class);
                startActivity(it);
            }
        });

        //Init district and city spinner
        initDistrictAndCity();

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
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt("yearPosition", position);
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
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt("isGasoline", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnTranmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt("isManual", position);
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

        //Revert value when user change the layout
        revertValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putString("frameNumber", edtFrame.getText().toString());
        editor.putString("plateNumber", edtPlate.getText().toString());
        editor.putString("rentFeePerHours", edtPriceHour.getText().toString());
        editor.putString("rentFeePerDay", edtPriceDay.getText().toString());
        editor.putString("depositFee", edtDepositFee.getText().toString());
        editor.apply();
        Intent it = new Intent(RegistVehicle.this, SignupOwnerOne.class);
        startActivity(it);
    }

    /////////////////////////// EXECUTE CODE ////////////////////////////
    //Init toolbar header, engine spinner and tranmission spinner
    private void initToolbarAndSpinner() {
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        vehicleType = editor.getString("vehicleType", "XE_CA_NHAN");
        vehicleTypeGeneral = vehicleType;
        if (vehicleType.equals("XE_MAY")) {
            toolbar_title.setText("Đăng ký xe máy");
            listEngineType.add("XĂNG");
            ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, listEngineType);
            spnEngine.setAdapter(engineAdapter);
            listTranmissionType.add("XE SỐ");
            listTranmissionType.add("XE TAY GA");
            ArrayAdapter<String> tranmissionAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, listTranmissionType);
            spnTranmission.setAdapter(tranmissionAdapter);

        } else {
            listEngineType.add("XĂNG");
            listEngineType.add("DẦU");
            ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, listEngineType);
            spnEngine.setAdapter(engineAdapter);
            listTranmissionType.add("SỐ SÀN");
            listTranmissionType.add("SỐ TỰ ĐỘNG");
            ArrayAdapter<String> tranmissionAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, listTranmissionType);
            spnTranmission.setAdapter(tranmissionAdapter);
            if (vehicleType.equals("XE_CA_NHAN")) {
                toolbar_title.setText("Đăng ký xe cá nhân");
            } else {
                toolbar_title.setText("Đăng ký xe du lịch");
            }
        }

        if (editor.getInt("isGasoline", -1) != -1 && (vehicleType.equals("XE_CA_NHAN")||vehicleType.equals("XE_DU_LICH"))){
            spnEngine.setSelection(editor.getInt("isGasoline", -1));
        }
        if (editor.getInt("isManual", -1) != -1 && (vehicleType.equals("XE_CA_NHAN")||vehicleType.equals("XE_DU_LICH"))){
            spnTranmission.setSelection(editor.getInt("isManual", -1));
        }
    }

    //Init district and city spinner
    private void initDistrictAndCity() {
        if (RetrofitCallAPI.lisCityTest.size() == 0){
            dialog = ProgressDialog.show(RegistVehicle.this, "Đang xử lý",
                    "Vui lòng đợi ...", true);
            RetrofitCallAPI testAPI = new RetrofitCallAPI();
            testAPI.getAllAddress(dialog, RegistVehicle.this);
        } else {
            final List<City> listAddress = RetrofitCallAPI.lisCityTest;
            ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAddress);
            cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCity.setAdapter(cityArrayAdapter);
            SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
            int citySelectPostion = editor.getInt("cityPosition", -1);
            final int districtSelectPosition = editor.getInt("districtPosition", -1);
            if (citySelectPostion != -1){
                spnCity.setSelection(citySelectPostion);
            }
            spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<District> districts = listAddress.get(position).getDistrict();
                    cityPosition = position;
                    ArrayAdapter<District> districtAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, districts);
                    spnDistrict.setAdapter(districtAdapter);
                    if (districtSelectPosition != 1){
                        spnDistrict.setSelection(districtSelectPosition);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    districtID = 0;
                    District district = listAddress.get(cityPosition).getDistrict().get(position);
                    districtID = district.getId();
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putInt("cityPosition", cityPosition);
                    editor.putInt("districtPosition", position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

    //Init year spinner
    private void getVehicleYear(final String getMaker, final String getModel) {
        dialog = ProgressDialog.show(RegistVehicle.this, "Hệ thống",
                "Đang xử lý ...", true);
        vehicleYear = new ArrayList<>();
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleYear(getMaker, getModel);
        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.code() == 200){
                    if (response.body() != null) {
                        dialog.dismiss();
                        for (int i = 0; i < response.body().size(); i++) {
                            vehicleYear.add(response.body().get(i).toString());
                        }

                        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, vehicleYear);
                        spnYear.setAdapter(yearAdapter);
                        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        int yearPosition = editor.getInt("yearPosition", -1);
                        if (yearPosition != -1){
                            spnYear.setSelection(yearPosition);
                        }
                    }
                } else {
                    Toast.makeText(RegistVehicle.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RegistVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Init seat data - get vehicleInfo
    public void getVehicleInfo(final String getMaker, final String getModel, final String getYear) {
        dialog = ProgressDialog.show(RegistVehicle.this, "Hệ thống",
                "Đang xử lý ...", true);
        vehicleInfoID = 0;
        vehicleInfoObj = new VehicleInformation();
        Retrofit test = RetrofitConnect.getClient();
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
                Toast.makeText(RegistVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Init checkbox
    private void initCheckbox(){
        cbxHouseHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    required_household_registration = 1;
                }
                else {
                    required_household_registration = 0;
                }
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt("requireHouseHold", required_household_registration);
                editor.apply();
            }
        });
        cbxIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    required_id_card = 1;
                }
                else {
                    required_id_card = 0;
                }
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putInt("requireIdCard", required_id_card);
                editor.apply();
            }
        });
    }

    //Send value to policy intent
    public void nextAction(){
        int isGasoline = 0, isManual = 0;
        String tranmission = spnTranmission.getSelectedItem().toString();
        String engineType = spnEngine.getSelectedItem().toString();
        if (tranmission.trim().equals("SỐ SÀN")){
            isManual = 1;
        } else {
            isManual = 0;
        }
        if (engineType.trim().equals("XĂNG")){
            isGasoline = 1;
        } else {
            isGasoline = 0;
        }

        String hourPriceTemp = edtPriceHour.getText().toString().trim().replaceAll(",", "");
        String dayPriceTemp = edtPriceDay.getText().toString().trim().replaceAll(",", "");
        String depositPriceTemp = edtDepositFee.getText().toString().trim().replaceAll(",", "");


        Boolean checkPlateNumber = validObj.validFrameNumber(edtPlate.getText().toString().trim(), edtPlate);
        Boolean checkFrameNumber = validObj.validFrameNumber(edtFrame.getText().toString().trim(), edtFrame);
        Boolean checkPricePerHours = validObj.validPrice(hourPriceTemp, edtPriceHour);
        Boolean checkPricePerDay = validObj.validPrice(dayPriceTemp, edtPriceDay);
        Boolean checkDepositFee = validObj.validPrice(depositPriceTemp, edtDepositFee);
        Boolean checkImage1 = validObj.validImageLink(picturePath1, RegistVehicle.this);
        Boolean checkImage2 = validObj.validImageLink(picturePath2, RegistVehicle.this);
        Boolean checkImage3 = validObj.validImageLink(picturePath3, RegistVehicle.this);
        Boolean checkVehicleName = validObj.validVehicleName(vehicleName.trim(), RegistVehicle.this);

        if (!vehicleType.equals(vehicleTypeGeneral)){
            Toast.makeText(this, "Không đúng loại xe! Hãy chọn xe khác", Toast.LENGTH_SHORT).show();
        } else {
            if (checkFrameNumber && checkPlateNumber && checkPricePerHours && checkPricePerDay
                    && checkDepositFee && checkImage1 && checkImage2 && checkImage3 && checkVehicleName){
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("frameNumber", edtFrame.getText().toString());
                editor.putString("plateNumber", edtPlate.getText().toString());
                editor.putString("rentFeePerHours", edtPriceHour.getText().toString());
                editor.putString("rentFeePerDay", edtPriceDay.getText().toString());
                editor.putString("depositFee", edtDepositFee.getText().toString());
                editor.apply();

                RetrofitCallAPI testAPI = new RetrofitCallAPI();
                ProgressDialog dialog = ProgressDialog.show(RegistVehicle.this, "Đang xử lý",
                        "Vui lòng đợi ...", true);
                testAPI.checkFrameNumber(edtFrame.getText().toString().trim(), RegistVehicle.this,
                        dialog, edtFrame, vehicleInfoID, districtID, "0", dayPriceTemp,
                        hourPriceTemp, depositPriceTemp, edtPlate.getText().toString().trim(),
                        required_household_registration, required_id_card,
                        isGasoline, isManual, picturePath3, picturePath1, picturePath2);

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

    private void revertValue(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String vehicleMaker = editor.getString("vehicleMaker", "Empty");
        String vehicleModel = editor.getString("vehicleModel", "Empty");
        String picture1 = editor.getString("img_vehicle_1", "");
        String picture2 = editor.getString("img_vehicle_2", "");
        String picture3 = editor.getString("picture_path", "");
        String frameNumber = editor.getString("frameNumber", "");
        String plateNumber = editor.getString("plateNumber", "");
        String rentPerHour = editor.getString("rentFeePerHours", "");
        String rentPerDay = editor.getString("rentFeePerDay", "");
        String deposit = editor.getString("depositFee", "");
        int requireHouseHold = editor.getInt("requireHouseHold", 0);
        int requireIdCard = editor.getInt("requireIdCard", 0);
        if (requireHouseHold != 0){
            cbxHouseHold.setChecked(true);
        }
        if (requireIdCard != 0){
            cbxIdCard.setChecked(true);
        }
        if (vehicleMaker.equals("Empty") == false && vehicleModel.equals("Empty") == false){
            txtCarModel.setText(vehicleMaker + " " + vehicleModel);
            vehicleName = vehicleMaker + " " + vehicleModel;
            getVehicleYear(vehicleMaker, vehicleModel);
            maker = vehicleMaker;
            model = vehicleModel;
        }
        if (!rentPerHour.equals("")){
            edtPriceHour.setText(rentPerHour);
        }
        if (!rentPerDay.equals("")){
            edtPriceDay.setText(rentPerDay);
        }
        if (!deposit.equals("")){
            edtDepositFee.setText(deposit);
        }
        if (!frameNumber.equals("")){
            edtFrame.setText(frameNumber);
        }
        if (!plateNumber.equals("")){
            edtPlate.setText(plateNumber);
        }
        if (!picture1.equals("")){
            picturePath1 = picture1;
            File imgFile = new File(picture1);
            Picasso.get().load(imgFile).into(imgFront);
            txtImageFront.setVisibility(View.INVISIBLE);
        }
        if (!picture2.equals("")){
            picturePath2 = picture2;
            File imgFile = new File(picture2);
            Picasso.get().load(imgFile).into(imgBack);
            txtImageBack.setVisibility(View.INVISIBLE);
        }
        if (!picture3.equals("")){
            picturePath3 = picture3;
            File imgFile = new File(picture3);
            Picasso.get().load(imgFile).into(imgFrame);
            txtImageFrame.setVisibility(View.INVISIBLE);
        }
    }

    /////////////////////////// THIS CODE USE FOR EXECUTE IMAGE ////////////////////////////////////
    //Execute image front
    public void executeImageFront() {
        final CharSequence[] items = { "Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistVehicle.this);
        builder.setTitle("Ảnh dầu xe");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")){
                    cameraObj.checkPermission(RegistVehicle.this, RegistVehicle.this, ImmutableValue.CAMERA_VEHICLE_CODE_1);
                } else if (items[which].equals("Chọn ảnh từ thư viện")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1);
                } else if (items[which].equals("Hủy")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Execute image back
    public void executeImageBack() {
        final CharSequence[] items = { "Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistVehicle.this);
        builder.setTitle("Ảnh đuôi xe");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")){
                    cameraObj.checkPermission(RegistVehicle.this, RegistVehicle.this, ImmutableValue.CAMERA_VEHICLE_CODE_2);
                } else if (items[which].equals("Chọn ảnh từ thư viện")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_2);
                } else if (items[which].equals("Hủy")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Execute image frame
    public void executeImageFrame() {
        final CharSequence[] items = { "Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistVehicle.this);
        builder.setTitle("Ảnh giấy đăng ký xe");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")){
                    cameraObj.checkPermission(RegistVehicle.this, RegistVehicle.this, ImmutableValue.CAMERA_VEHICLE_CODE_3);
                } else if (items[which].equals("Chọn ảnh từ thư viện")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_3);
                } else if (items[which].equals("Hủy")){
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
            case ImmutableValue.CAMERA_VEHICLE_CODE_1:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgFront, RegistVehicle.this);
                    picturePath1 = ImmutableValue.picturePath;
                    txtImageFront.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("img_vehicle_1", picturePath1);
                    editor.apply();
                }
                break;
            case ImmutableValue.CAMERA_VEHICLE_CODE_2:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgBack, RegistVehicle.this);
                    picturePath2 = ImmutableValue.picturePath;
                    txtImageBack.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("img_vehicle_2", picturePath2);
                    editor.apply();
                }
                break;
            case ImmutableValue.CAMERA_VEHICLE_CODE_3:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgFrame, RegistVehicle.this);
                    picturePath3 = ImmutableValue.picturePath;
                    txtImageFrame.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("picture_path", picturePath3);
                    editor.apply();
                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    picturePath1 = cameraObj.showImageGallery(data, imgFront, RegistVehicle.this);
                    txtImageFront.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("img_vehicle_1", picturePath1);
                    editor.apply();
                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_2:
                if (resultCode == RESULT_OK) {
                    picturePath2 = cameraObj.showImageGallery(data, imgBack, RegistVehicle.this);
                    txtImageBack.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("img_vehicle_2", picturePath2);
                    editor.apply();
                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_3:
                if (resultCode == RESULT_OK) {
                    picturePath3 = cameraObj.showImageGallery(data, imgFrame, RegistVehicle.this);
                    txtImageFrame.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("picture_path", picturePath3);
                    editor.apply();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, ImmutableValue.CAMERA_OPEN_CODE);

                }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}
