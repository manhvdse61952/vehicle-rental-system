package com.example.manhvdse61952.vrc_android.layout.vehicle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.searchModel.SearchItemNew;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageVehicleActivity extends AppCompatActivity {
    List<SearchItemNew> vehicleList = new ArrayList<>();
    RecyclerView recyclerView;
    ManageVehicleAdapter manageVehicleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicle);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_vehicle_manage_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE,MODE_PRIVATE);
        int ownerID = editor.getInt("userID",0);
        Retrofit retrofit = RetrofitConnect.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<List<SearchItemNew>> responseBodyCall = vehicleAPI.getVehicleByOwnerID(ownerID);
        responseBodyCall.enqueue(new Callback<List<SearchItemNew>>() {
            @Override
            public void onResponse(Call<List<SearchItemNew>> call, Response<List<SearchItemNew>> response) {
                if (response.code() == 200){
                    vehicleList = response.body();
                    manageVehicleAdapter = new ManageVehicleAdapter(vehicleList);
                    recyclerView.setAdapter(manageVehicleAdapter);
                    manageVehicleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SearchItemNew>> call, Throwable t) {
                Toast.makeText(ManageVehicleActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
