package com.example.manhvdse61952.vrc_android.layout.contract;

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
    List<ContractItem> contractItemList = new ArrayList<>();
    RecyclerView recyclerView;
    ManageContractAdapter manageContractAdapter;
    TextView txt_manage_contract_error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contract);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        txt_manage_contract_error = (TextView) findViewById(R.id.txt_manage_contract_error);
        txt_manage_contract_error.setEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_contract_manage_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        if (editor.getString("roleName", "ROLE_USER").equals("ROLE_USER")) {
            loadDataForCustomer();
        } else {
            loadDataForOwner();
        }
    }

    private void loadDataForOwner() {
        Retrofit retrofit = RetrofitConnect.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int ownerID = sharedPreferences.getInt("userID", 0);
        Call<List<ContractItem>> responseBodyCall = contractAPI.findContractByOwnerID(ownerID);
        responseBodyCall.enqueue(new Callback<List<ContractItem>>() {
            @Override
            public void onResponse(Call<List<ContractItem>> call, Response<List<ContractItem>> response) {
                if (response.code() == 200) {
                    txt_manage_contract_error.setEnabled(false);
                    contractItemList = response.body();
                    manageContractAdapter = new ManageContractAdapter(contractItemList, ManageContractActivity.this);
                    recyclerView.setAdapter(manageContractAdapter);
                    manageContractAdapter.notifyDataSetChanged();
                } else if (response.code() == 404) {
                    txt_manage_contract_error.setEnabled(true);
                } else {
                    txt_manage_contract_error.setEnabled(true);
                    Toast.makeText(ManageContractActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                Toast.makeText(ManageContractActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataForCustomer() {
        Retrofit retrofit = RetrofitConnect.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int customerID = sharedPreferences.getInt("userID", 0);
        Call<List<ContractItem>> responseBodyCall = contractAPI.findContractByCustomerID(customerID);
        responseBodyCall.enqueue(new Callback<List<ContractItem>>() {
            @Override
            public void onResponse(Call<List<ContractItem>> call, Response<List<ContractItem>> response) {
                if (response.code() == 200) {
                    txt_manage_contract_error.setEnabled(false);
                    contractItemList = response.body();
                    manageContractAdapter = new ManageContractAdapter(contractItemList, ManageContractActivity.this);
                    recyclerView.setAdapter(manageContractAdapter);
                    manageContractAdapter.notifyDataSetChanged();
                } else if (response.code() == 404) {
                    txt_manage_contract_error.setEnabled(true);
                } else {
                    txt_manage_contract_error.setEnabled(true);
                    Toast.makeText(ManageContractActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                Toast.makeText(ManageContractActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
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
}
