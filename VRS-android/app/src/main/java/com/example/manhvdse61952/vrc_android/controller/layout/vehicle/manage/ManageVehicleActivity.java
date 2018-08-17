package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.main.SectionPageAdapter;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.vehicleTab.ApprovedTab;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.vehicleTab.RejectTab;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.vehicleTab.WaitingTab;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageVehicleActivity extends AppCompatActivity {
    List<SearchVehicleItem> vehicleList = new ArrayList<>();
    public static List<SearchVehicleItem> listApproved = new ArrayList<>();
    public static List<SearchVehicleItem> listWaiting = new ArrayList<>();
    public static List<SearchVehicleItem> listRejected = new ArrayList<>();
    public static SectionPageAdapter secAdapter;
    public static ViewPager viewPager;
    private TabLayout tabLayout;
    FloatingActionButton fab;

    ProgressDialog dialog;
    ImageView imgCreateVehicle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        declareID();

        loadData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

//        reloadData();

        imgCreateVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                SharedPreferences.Editor editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.VEHICLE_vehicleMaker, "Empty");
                editor.putString(ImmutableValue.VEHICLE_vehicleModel, "Empty");
                editor.putString(ImmutableValue.VEHICLE_img_vehicle_1, "");
                editor.putString(ImmutableValue.VEHICLE_img_vehicle_2, "");
                editor.putString(ImmutableValue.VEHICLE_img_frameNumber, "");
                editor.putInt(ImmutableValue.VEHICLE_year, -1);
                editor.putString(ImmutableValue.VEHICLE_frameNumber, "");
                editor.putString(ImmutableValue.VEHICLE_plateNumber, "");
                editor.putInt(ImmutableValue.VEHICLE_city, -1);
                editor.putInt(ImmutableValue.VEHICLE_district, -1);
                editor.putString(ImmutableValue.VEHICLE_rentFeePerHours, "");
                editor.putString(ImmutableValue.VEHICLE_rentFeePerDay, "");
                editor.putString(ImmutableValue.VEHICLE_depositFee, "");
                editor.putInt(ImmutableValue.VEHICLE_isGasoline, -1);
                editor.putInt(ImmutableValue.VEHICLE_isManual, -1);
                editor.putInt(ImmutableValue.VEHICLE_requireHouseHold, 0);
                editor.putInt(ImmutableValue.VEHICLE_requireIdCard, 0);
                editor2.putString(ImmutableValue.MAIN_vehicleAddress, "");
                editor2.putString(ImmutableValue.MAIN_vehicleLat, "");
                editor2.putString(ImmutableValue.MAIN_vehicleLng, "");
                editor.apply();
                editor2.apply();
                startActivity(new Intent(ManageVehicleActivity.this, CreateVehicleType.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        ManageVehicleActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void declareID(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        imgCreateVehicle = (ImageView)findViewById(R.id.imgCreateVehicle);
        fab = (FloatingActionButton)findViewById(R.  id.fab);
    }

    private void loadData(){
        dialog = ProgressDialog.show(ManageVehicleActivity.this, "Hệ thống",
                "Đang xử lý ...", true);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE,MODE_PRIVATE);
        int ownerID = editor.getInt(ImmutableValue.HOME_userID,0);
        Retrofit retrofit = RetrofitConfig.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<List<SearchVehicleItem>> responseBodyCall = vehicleAPI.getVehicleByOwnerID(ownerID);
        responseBodyCall.enqueue(new Callback<List<SearchVehicleItem>>() {
            @Override
            public void onResponse(Call<List<SearchVehicleItem>> call, Response<List<SearchVehicleItem>> response) {
                if (response.code() == 200){
                    vehicleList = response.body();
                    if (vehicleList.size() > 0){
                        listApproved = new ArrayList<>();
                        listWaiting = new ArrayList<>();
                        listRejected = new ArrayList<>();
                        for (int i = 0; i < vehicleList.size();i++){
                            if (vehicleList.get(i).getApproveStatus().equals(ImmutableValue.VEHICLE_STATUS_WAITING)){
                                listWaiting.add(vehicleList.get(i));
                            }
                            else if (vehicleList.get(i).getApproveStatus().equals(ImmutableValue.VEHICLE_STATUS_REJECTED)){
                                listRejected.add(vehicleList.get(i));
                            } else {
                                listApproved.add(vehicleList.get(i));
                            }
                        }
                    }
                    viewPager = (ViewPager) findViewById(R.id.container);
                    setupViewPager(viewPager);
                    tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                    createTabIcons();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<SearchVehicleItem>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ManageVehicleActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        secAdapter = new SectionPageAdapter(getSupportFragmentManager());
        secAdapter.addFragment(new ApprovedTab(), "Đã kiểm duyệt");
        secAdapter.addFragment(new WaitingTab(), "Chờ kiểm duyệt");
        secAdapter.addFragment(new RejectTab(), "Bị loại bỏ");
        viewPager.setAdapter(secAdapter);
    }

    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Đã kiểm duyệt");
        tabOne.setTextSize(12);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Chờ kiểm duyệt");
        tabTwo.setTextSize(12);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Bị loại bỏ");
        tabThree.setTextSize(12);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }
}
