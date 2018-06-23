package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.main.SearchAddressModel;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
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

public class SignupOwnerTwo extends AppCompatActivity {

    int cityPosition = 0;
    Button btnVehicleNext, btnVehicleBack;
    TextView edtSignupVehicleName, txtVehicleSeat, txtVehicleType;
    Spinner spnVehicleEngineType, spnVehicleTranmission, spnVehicleCity, spnVehicleDistrict, spnVehicleYear;

    String maker, model, year;
    List<String> vehicleYear;
    ProgressDialog dialog;
    VehicleInformation_New vehicleInfoObj;

    int vehicleInfoID, districtID;

    String vehicleType = "", vehicleTypeTranslate = "", receiveVehicleType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_two);

        //Declare id
        btnVehicleNext = (Button) findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button) findViewById(R.id.btnVehicleBack);

        txtVehicleSeat = (TextView)findViewById(R.id.txtVehicleSeat);
        edtSignupVehicleName = (TextView) findViewById(R.id.edtSignupVehicleName);
        txtVehicleType = (TextView)findViewById(R.id.txtVehicleType);

        spnVehicleYear = (Spinner) findViewById(R.id.spnVehicleYear);
        spnVehicleEngineType = (Spinner) findViewById(R.id.spnVehicleEngineType);
        spnVehicleTranmission = (Spinner) findViewById(R.id.spnVehicleTranmission);
        spnVehicleCity = (Spinner) findViewById(R.id.spnVehicleCity);
        spnVehicleDistrict = (Spinner) findViewById(R.id.spnVehicleDistrict);

        //Init tranmission spinner and engine type spinner
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        vehicleType = editor.getString("vehicleType", "XE_CA_NHAN");
        if (vehicleType.equals("XE_MAY")){
            vehicleTypeTranslate = "xe máy";
            txtVehicleType.setText("Thông tin của xe máy");
            List<String> listEngineType = new ArrayList<>();
            listEngineType.add("XĂNG");
            ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(SignupOwnerTwo.this, android.R.layout.simple_spinner_dropdown_item, listEngineType);
            spnVehicleEngineType.setAdapter(engineAdapter);
            List<String> listTranmission = new ArrayList<>();
            listTranmission.add("XE SỐ");
            listTranmission.add("XE TAY GA");
            ArrayAdapter<String> tranmissionAdapter = new ArrayAdapter<>(SignupOwnerTwo.this, android.R.layout.simple_spinner_dropdown_item, listTranmission);
            spnVehicleTranmission.setAdapter(tranmissionAdapter);
        } else {
            List<String> listEngineType = new ArrayList<>();
            listEngineType.add("XĂNG");
            listEngineType.add("DẦU");
            ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(SignupOwnerTwo.this, android.R.layout.simple_spinner_dropdown_item, listEngineType);
            spnVehicleEngineType.setAdapter(engineAdapter);

            List<String> listTranmission = new ArrayList<>();
            listTranmission.add("SỐ SÀN");
            listTranmission.add("SỐ TỰ ĐỘNG");
            ArrayAdapter<String> tranmissionAdapter = new ArrayAdapter<>(SignupOwnerTwo.this, android.R.layout.simple_spinner_dropdown_item, listTranmission);
            spnVehicleTranmission.setAdapter(tranmissionAdapter);

            if (vehicleType.equals("XE_CA_NHAN")){
                vehicleTypeTranslate = "xe cá nhân";
                txtVehicleType.setText("Thông tin của xe cá nhân");
            } else {
                vehicleTypeTranslate = "xe du lịch";
                txtVehicleType.setText("Thông tin của xe du lịch");
            }
        }




        //Init search edittext
        edtSignupVehicleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maker = "";
                model = "";
                new SimpleSearchDialogCompat(SignupOwnerTwo.this, "", "Nhập đia điểm cần kiếm xe", null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        if (!searchable.getTitle().equals("")) {
                            edtSignupVehicleName.setText("" + searchable.getTitle());
                            String[] temp = searchable.getTitle().split(" ");
                            maker = temp[0];
                            if (temp.length > 2) {
                                for (int j = 1; j < temp.length; j++) {
                                    model += temp[j].toString() + " ";
                                }
                            } else {
                                model = temp[1];
                            }
                            dialog = ProgressDialog.show(SignupOwnerTwo.this, "Đang xử lý",
                                    "Vui lòng đợi ...", true);
                            getVehicleYear(maker, model.trim());
                        }
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });


        //Init district in spinner
        final List<City> listAddress = RetrofitCallAPI.lisCityTest;
        ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAddress);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVehicleCity.setAdapter(cityArrayAdapter);
        spnVehicleCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<District> districts = listAddress.get(position).getDistrict();
                cityPosition = position;
                ArrayAdapter<District> districtAdapter = new ArrayAdapter<>(SignupOwnerTwo.this, android.R.layout.simple_spinner_dropdown_item, districts);
                spnVehicleDistrict.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnVehicleDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ///////////////////////////////////////////////////////////////

        ///get vehicle info by maker, model and year
        spnVehicleYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = "";
                year = spnVehicleYear.getSelectedItem().toString();
                getVehicleInfo(maker, model, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Button next
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (vehicleInfoID != 0 && districtID != 0 && edtSignupVehicleName.equals("Nhấn để chọn xe ...") == false) {
                    if (receiveVehicleType.equals(vehicleType) == false){
                        Toast.makeText(SignupOwnerTwo.this, "Hình như đây không phải là " + vehicleTypeTranslate
                                +", vui lòng chọn lại", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                        editor.putInt("vehicleInformationID", vehicleInfoID);
                        editor.putInt("districtID", districtID);
                        editor.apply();
                        Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerTwoPlus.class);
                        startActivity(it);
                    }

                } else {
                    Toast.makeText(SignupOwnerTwo.this, "Vui lòng chọn xe", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //button Back
        btnVehicleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerOne.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerOne.class);
        startActivity(it);
    }

    private ArrayList<SearchAddressModel> initData() {
        ArrayList<SearchAddressModel> items = new ArrayList<>();
        if (ImmutableValue.listVehicleModelOne.size() > 0) {
            for (int i = 0; i < ImmutableValue.listVehicleModelOne.size(); i++) {
                items.add(new SearchAddressModel(ImmutableValue.listVehicleModelOne.get(i).trim()));
            }
        }
        if (ImmutableValue.listVehicleModelTwo.size() > 0) {
            for (int j = 0; j < ImmutableValue.listVehicleModelTwo.size(); j++) {
                items.add(new SearchAddressModel(ImmutableValue.listVehicleModelTwo.get(j).trim()));
            }
        }
        if (ImmutableValue.listVehicleModelThree.size() > 0){
            for (int k = 0; k < ImmutableValue.listVehicleModelThree.size(); k++){
                items.add(new SearchAddressModel(ImmutableValue.listVehicleModelThree.get(k).trim()));
            }
        }
        return items;
    }

    public void getVehicleYear(final String getMaker, final String getModel) {
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

                    ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(SignupOwnerTwo.this, android.R.layout.simple_spinner_dropdown_item, vehicleYear);
                    spnVehicleYear.setAdapter(yearAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(SignupOwnerTwo.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                    receiveVehicleType = "";
                    vehicleInfoObj = response.body();
                    vehicleInfoID = vehicleInfoObj.getId();
                    txtVehicleSeat.setText(vehicleInfoObj.getSeat() + "");
                    receiveVehicleType = vehicleInfoObj.getVehicleType();
                    if (receiveVehicleType.equals("XE_MAY")){
                        Boolean isScooter = vehicleInfoObj.getScooter();
                        if (isScooter == true){
                            spnVehicleTranmission.setSelection(1);
                        }
                        else {
                            spnVehicleTranmission.setSelection(0);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleInformation_New> call, Throwable t) {

            }
        });
    }
}
