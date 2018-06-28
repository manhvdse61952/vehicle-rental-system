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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.manhvdse61952.vrc_android.model.apiModel.VehicleInformation_New;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.example.manhvdse61952.vrc_android.remote.Validate;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistVehicle extends AppCompatActivity {

    VehicleInformation_New vehicleInfoObj;
    String vehicleType;
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

    //Value to save in shared preferences
    int required_household_registration = 0, required_id_card = 0;
    String picturePath1 = "", picturePath2 = "", picturePath3 = "";
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

        // Init toolbar, engine spinner and tranmission spinner ///
        initToolbarAndSpinner();

        //Show car model search edit text
        txtCarModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearchCarModel();
            }
        });

        //Init district and city spinner
        initDistrictAndCity();

        //Init seat text view by year spinner
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = "";
                year = spnYear.getSelectedItem().toString();
                getVehicleInfo(maker, model, year);
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
        imgCreateVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAction();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(RegistVehicle.this, SignupOwnerOne.class);
        startActivity(it);
    }

    /////////////////////////// EXECUTE CODE ////////////////////////////
    //Init toolbar header, engine spinner and tranmission spinner
    private void initToolbarAndSpinner() {
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        vehicleType = editor.getString("vehicleType", "XE_CA_NHAN");
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
    }

    //Init data to car model edit text
    private ArrayList<SearchAddressModel> initData() {
        ArrayList<SearchAddressModel> items = new ArrayList<>();
        if (ImmutableValue.listVehicleModelTwo.size() > 0) {
            for (int j = 0; j < ImmutableValue.listVehicleModelTwo.size(); j++) {
                items.add(new SearchAddressModel(ImmutableValue.listVehicleModelTwo.get(j).trim()));
            }
        }
        if (ImmutableValue.listVehicleModelThree.size() > 0) {
            for (int k = 0; k < ImmutableValue.listVehicleModelThree.size(); k++) {
                items.add(new SearchAddressModel(ImmutableValue.listVehicleModelThree.get(k).trim()));
            }
        }
        return items;
    }

    //Init search data to car model edit text
    private void initSearchCarModel() {
        maker = "";
        model = "";
        new SimpleSearchDialogCompat(RegistVehicle.this, "", "Nhập đia điểm cần kiếm xe", null, initData(), new SearchResultListener<Searchable>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                if (!searchable.getTitle().equals("")) {
                    txtCarModel.setText("" + searchable.getTitle());
                    String[] temp = searchable.getTitle().split(" ");
                    maker = temp[0];
                    if (temp.length > 2) {
                        for (int j = 1; j < temp.length; j++) {
                            model += temp[j].toString() + " ";
                        }
                    } else {
                        model = temp[1];
                    }
                    dialog = ProgressDialog.show(RegistVehicle.this, "Đang xử lý",
                            "Vui lòng đợi ...", true);
                    getVehicleYear(maker, model.trim());
                }
                baseSearchDialogCompat.dismiss();
            }
        }).show();
    }

    //Init district and city spinner
    private void initDistrictAndCity() {
        final List<City> listAddress = RetrofitCallAPI.lisCityTest;
        ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAddress);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(cityArrayAdapter);
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<District> districts = listAddress.get(position).getDistrict();
                cityPosition = position;
                ArrayAdapter<District> districtAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, districts);
                spnDistrict.setAdapter(districtAdapter);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Init year spinner
    private void getVehicleYear(final String getMaker, final String getModel) {
        vehicleYear = new ArrayList<>();
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleYear(getMaker, getModel);

        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    dialog.dismiss();
                    for (int i = 0; i < response.body().size(); i++) {
                        vehicleYear.add(response.body().get(i).toString());
                    }

                    ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(RegistVehicle.this, android.R.layout.simple_spinner_dropdown_item, vehicleYear);
                    spnYear.setAdapter(yearAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RegistVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Init seat data - get vehicleInfor
    public void getVehicleInfo(final String getMaker, final String getModel, final String getYear) {
        vehicleInfoID = 0;
        vehicleInfoObj = new VehicleInformation_New();
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<VehicleInformation_New> responseBodyCall = testAPI.getVehicleInfo(getMaker, getModel, getYear);

        responseBodyCall.enqueue(new Callback<VehicleInformation_New>() {
            @Override
            public void onResponse(Call<VehicleInformation_New> call, Response<VehicleInformation_New> response) {
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
            }

            @Override
            public void onFailure(Call<VehicleInformation_New> call, Throwable t) {

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

        Boolean checkPlateNumber = validObj.validFrameNumber(edtPlate.getText().toString().trim(), edtPlate);
        Boolean checkFrameNumber = validObj.validFrameNumber(edtFrame.getText().toString().trim(), edtFrame);
        Boolean checkPricePerHours = validObj.validPrice(edtPriceHour.getText().toString().trim(), edtPriceHour);
        Boolean checkPricePerDay = validObj.validPrice(edtPriceDay.getText().toString().trim(), edtPriceDay);
        Boolean checkDepositFee = validObj.validPrice(edtDepositFee.getText().toString().trim(), edtDepositFee);
        Boolean checkImage1 = validObj.validImageLink(picturePath1, RegistVehicle.this);
        Boolean checkImage2 = validObj.validImageLink(picturePath2, RegistVehicle.this);
        Boolean checkImage3 = validObj.validImageLink(picturePath3, RegistVehicle.this);

        if (checkFrameNumber && checkPlateNumber && checkPricePerHours && checkPricePerDay
                && checkDepositFee && checkImage1 && checkImage2 && checkImage3){
            SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
            editor.putString("frameNumber", edtFrame.getText().toString().trim());
            editor.putInt("vehicleInformationID", vehicleInfoID);
            editor.putInt("districtID", districtID);
            editor.putString("rentFeePerSlot", "0");
            editor.putString("rentFeePerDay", edtPriceDay.getText().toString().trim());
            editor.putString("rentFeePerHours", edtPriceHour.getText().toString().trim());
            editor.putString("depositFee", edtDepositFee.getText().toString().trim());
            editor.putString("plateNumber", edtPlate.getText().toString().trim());
            editor.putInt("requireHouseHold", required_household_registration);
            editor.putInt("requireIdCard", required_id_card);
            editor.putInt("isGasoline", isGasoline);
            editor.putInt("isManual", isManual);
            editor.putString("picture_path", picturePath3);
            editor.putString("img_vehicle_1", picturePath1);
            editor.putString("img_vehicle_2", picturePath2);
            editor.apply();

            Intent it = new Intent(RegistVehicle.this, SignupOwnerPolicy.class);
            startActivity(it);
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

                }
                break;
            case ImmutableValue.CAMERA_VEHICLE_CODE_2:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgBack, RegistVehicle.this);
                    picturePath2 = ImmutableValue.picturePath;
                    txtImageBack.setVisibility(View.INVISIBLE);

                }
                break;
            case ImmutableValue.CAMERA_VEHICLE_CODE_3:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgFrame, RegistVehicle.this);
                    picturePath3 = ImmutableValue.picturePath;
                    txtImageFrame.setVisibility(View.INVISIBLE);

                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    picturePath1 = cameraObj.showImageGallery(data, imgFront, RegistVehicle.this);
                    txtImageFront.setVisibility(View.INVISIBLE);

                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_2:
                if (resultCode == RESULT_OK) {
                    picturePath2 = cameraObj.showImageGallery(data, imgBack, RegistVehicle.this);
                    txtImageBack.setVisibility(View.INVISIBLE);

                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_3:
                if (resultCode == RESULT_OK) {
                    picturePath3 = cameraObj.showImageGallery(data, imgFrame, RegistVehicle.this);
                    txtImageFrame.setVisibility(View.INVISIBLE);

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
