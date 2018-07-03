package com.example.manhvdse61952.vrc_android.layout.vehicle;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.apiModel.Vehicle_New;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageVehicleActivity extends AppCompatActivity {
    private List<Vehicle_New> vehicleList;
    RecyclerView mRecyclerView;
    ManageVehicleAdapter mRcvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicle);
        mRecyclerView = findViewById(R.id.recycler_vehicle_manage_view);
        loadJSON();

    }
    private void loadJSON(){
        Retrofit retrofit = RetrofitConnect.getClient();
        final VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE,MODE_PRIVATE);
        int ownerID = sharedPreferences.getInt("userID",0);
        Call<List<Vehicle_New>> call = vehicleAPI.getVehicleListByOwner(ownerID);
        call.enqueue(new Callback<List<Vehicle_New>>() {
            @Override
            public void onResponse(Call<List<Vehicle_New>> call, Response<List<Vehicle_New>> response) {
                if (response.code() == 200){
                    vehicleList = response.body();
                    mRcvAdapter = new ManageVehicleAdapter(vehicleList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(mRcvAdapter);
                } else {
                    Toast.makeText(ManageVehicleActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle_New>> call, Throwable t) {
                Toast.makeText(ManageVehicleActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                Log.d("ManageVehicleActivity",t.getMessage());
            }
        });
    }
}
