package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.VehicleInformation;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchVehicleInfoActivity extends AppCompatActivity {
    List<VehicleInformation> vehicleInformationList = new ArrayList<>();
    List<VehicleInformation> vehicleInformationListAfterSearch = new ArrayList<>();
    RecyclerView recyclerView;
    SearchVehicleInfoAdapter searchVehicleInfoAdapter;
    ProgressDialog dialog;
    EditText edt_search_vehicle_info;
    ImageView img_delete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicle_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_vehicle_info_manage_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        edt_search_vehicle_info = (EditText)findViewById(R.id.edt_search_vehicle_info);
        img_delete = (ImageView)findViewById(R.id.img_delete);
        img_delete.setVisibility(View.INVISIBLE);
        LoadData();

        edt_search_vehicle_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && !s.toString().trim().equals("")){
                    ExecuteData(s.toString());
                    img_delete.setVisibility(View.VISIBLE);
                } else {
                    img_delete.setVisibility(View.INVISIBLE);
                    searchVehicleInfoAdapter = new SearchVehicleInfoAdapter(vehicleInformationList, SearchVehicleInfoActivity.this, SearchVehicleInfoActivity.this);
                    recyclerView.setAdapter(searchVehicleInfoAdapter);
                    searchVehicleInfoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search_vehicle_info.setText("");
                searchVehicleInfoAdapter = new SearchVehicleInfoAdapter(vehicleInformationList, SearchVehicleInfoActivity.this, SearchVehicleInfoActivity.this);
                recyclerView.setAdapter(searchVehicleInfoAdapter);
                searchVehicleInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void LoadData(){
        dialog = ProgressDialog.show(SearchVehicleInfoActivity.this, "Hệ thống",
                "Đang xử lý ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<List<VehicleInformation>> responseBodyCall = vehicleAPI.getAllVehicleInfo();
        responseBodyCall.enqueue(new Callback<List<VehicleInformation>>() {
            @Override
            public void onResponse(Call<List<VehicleInformation>> call, Response<List<VehicleInformation>> response) {
                if (response.code() == 200){
                    vehicleInformationList = response.body();
                } else {
                    Toast.makeText(SearchVehicleInfoActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                searchVehicleInfoAdapter = new SearchVehicleInfoAdapter(vehicleInformationList, SearchVehicleInfoActivity.this, SearchVehicleInfoActivity.this);
                recyclerView.setAdapter(searchVehicleInfoAdapter);
                searchVehicleInfoAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<VehicleInformation>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(SearchVehicleInfoActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ExecuteData(String value){
        vehicleInformationListAfterSearch = new ArrayList<>();
        if (value.trim().equals("")){

        } else {
            for (int i = 0; i < vehicleInformationList.size(); i++){
                String vehicleName = vehicleInformationList.get(i).getVehicleMaker() + " " + vehicleInformationList.get(i).getVehicleModel();
                if (vehicleName.toLowerCase().contains(value.toLowerCase())){
                    vehicleInformationListAfterSearch.add(vehicleInformationList.get(i));
                }
            }
        }

        searchVehicleInfoAdapter = new SearchVehicleInfoAdapter(vehicleInformationListAfterSearch, SearchVehicleInfoActivity.this, SearchVehicleInfoActivity.this);
        recyclerView.setAdapter(searchVehicleInfoAdapter);
        searchVehicleInfoAdapter.notifyDataSetChanged();
    }
}
