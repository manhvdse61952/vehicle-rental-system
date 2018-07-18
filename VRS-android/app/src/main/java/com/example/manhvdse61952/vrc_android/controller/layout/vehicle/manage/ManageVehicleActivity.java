package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
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
    RecyclerView recyclerView;
    ManageVehicleAdapter manageVehicleAdapter;
    TextView txt_manage_vehicle_empty;
    ProgressDialog dialog;
    SwipeRefreshLayout swipeLayout;

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

        reloadData();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(ManageVehicleActivity.this, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void declareID(){
        txt_manage_vehicle_empty = (TextView)findViewById(R.id.txt_manage_vehicle_empty);
        txt_manage_vehicle_empty.setEnabled(false);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_vehicle_manage_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
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
                    if (vehicleList.size() == 0){
                        txt_manage_vehicle_empty.setVisibility(View.VISIBLE);
                    } else {
                        txt_manage_vehicle_empty.setVisibility(View.INVISIBLE);
                        manageVehicleAdapter = new ManageVehicleAdapter(vehicleList, ManageVehicleActivity.this);
                        recyclerView.setAdapter(manageVehicleAdapter);
                        manageVehicleAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(ManageVehicleActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
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

    private void reloadData(){
        swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_dark);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        loadData();
                    }
                }, 500);
            }
        });
    }
}
