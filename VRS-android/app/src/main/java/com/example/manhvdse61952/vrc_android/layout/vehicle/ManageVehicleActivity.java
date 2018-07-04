package com.example.manhvdse61952.vrc_android.layout.vehicle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.searchModel.SearchVehicleItem;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

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

        txt_manage_vehicle_empty = (TextView)findViewById(R.id.txt_manage_vehicle_empty);
        txt_manage_vehicle_empty.setEnabled(false);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_vehicle_manage_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        loadData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE,MODE_PRIVATE);
        int ownerID = editor.getInt("userID",0);
        Retrofit retrofit = RetrofitConnect.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<List<SearchVehicleItem>> responseBodyCall = vehicleAPI.getVehicleByOwnerID(ownerID);
        responseBodyCall.enqueue(new Callback<List<SearchVehicleItem>>() {
            @Override
            public void onResponse(Call<List<SearchVehicleItem>> call, Response<List<SearchVehicleItem>> response) {
                if (response.code() == 200){
                    vehicleList = response.body();
                    if (vehicleList.size() == 0){
                        txt_manage_vehicle_empty.setEnabled(true);
                    } else {
                        txt_manage_vehicle_empty.setEnabled(false);
                        manageVehicleAdapter = new ManageVehicleAdapter(vehicleList);
                        recyclerView.setAdapter(manageVehicleAdapter);
                        manageVehicleAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<SearchVehicleItem>> call, Throwable t) {
                Toast.makeText(ManageVehicleActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
