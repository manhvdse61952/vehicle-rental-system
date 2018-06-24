package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    int cityPosition = 0, districtID, vehicleInfoID;
    VehicleInformation_New vehicleInfoObj;
    String vehicleType;

    EditText edtCarModel;
    Spinner spnEngine, spnTranmission, spnYear, spnCity, spnDistrict;
    List<String> listEngineType = new ArrayList<>();
    List<String> listTranmissionType = new ArrayList<>();
    String maker, model, year;
    List<String> vehicleYear;
    ProgressDialog dialog;
    TextView txtSeat;

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

        //Declare id //
        spnEngine = (Spinner)findViewById(R.id.spnEngine);
        spnTranmission = (Spinner)findViewById(R.id.spnTranmission);
        spnYear = (Spinner)findViewById(R.id.spnYear);
        edtCarModel = (EditText)findViewById(R.id.edtCarModel);
        spnCity = (Spinner)findViewById(R.id.spnCity);
        spnDistrict = (Spinner)findViewById(R.id.spnDistrict);
        txtSeat = (TextView)findViewById(R.id.txtSeat);

        // Init toolbar, engine spinner and tranmission spinner ///
        initToolbarAndSpinner();

        //Show car model search edit text
        edtCarModel.setOnClickListener(new View.OnClickListener() {
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



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(RegistVehicle.this, SignupOwnerOne.class);
        startActivity(it);
    }

    /////////////////////////// EXECUTE CODE ////////////////////////////
    //Init toolbar header, engine spinner and tranmission spinner
    private void initToolbarAndSpinner(){
        TextView toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        vehicleType = editor.getString("vehicleType", "XE_CA_NHAN");
        if (vehicleType.equals("XE_MAY")){
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
            if (vehicleType.equals("XE_CA_NHAN")){
                toolbar_title.setText("Đăng ký xe cá nhân");
            } else {
                toolbar_title.setText("Đăng ký xe du lịch");
            }
        }
    }

    //Init data to car model edit text
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

    //Init search data to car model edit text
    private void initSearchCarModel(){
        maker = "";
        model = "";
        new SimpleSearchDialogCompat(RegistVehicle.this, "", "Nhập đia điểm cần kiếm xe", null, initData(), new SearchResultListener<Searchable>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                if (!searchable.getTitle().equals("")) {
                    edtCarModel.setText("" + searchable.getTitle());
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
    private void initDistrictAndCity(){
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
                    if (vehicleType.equals("XE_MAY")){
                        Boolean isScooter = vehicleInfoObj.getScooter();
                        if (isScooter == true){
                            spnTranmission.setSelection(1);
                        }
                        else {
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

}
