package com.example.manhvdse61952.vrc_android.layout.contract;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageContractActivity extends AppCompatActivity {
    private List<ContractItem> contractItemList = new ArrayList<>();
    RecyclerView recyclerView;
    ManageContractAdapter manageContractAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contract);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_contract_manage_view);

    }

    private void loadData(){
        Retrofit retrofit = RetrofitConnect.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE,MODE_PRIVATE);
        int ownerID = sharedPreferences.getInt("userID",0);
        Call<List<ContractItem>> responseBodyCall = contractAPI.findContractByUserID(ownerID);
        responseBodyCall.enqueue(new Callback<List<ContractItem>>() {
            @Override
            public void onResponse(Call<List<ContractItem>> call, Response<List<ContractItem>> response) {
                if (response.code() == 200){
                    contractItemList = response.body();
                    manageContractAdapter = new ManageContractAdapter(contractItemList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(manageContractAdapter);
                } else {
                    Toast.makeText(ManageContractActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                Toast.makeText(ManageContractActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
