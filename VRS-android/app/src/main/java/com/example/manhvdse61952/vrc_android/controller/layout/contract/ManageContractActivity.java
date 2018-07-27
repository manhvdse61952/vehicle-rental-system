package com.example.manhvdse61952.vrc_android.controller.layout.contract;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

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
    SwipeRefreshLayout swipeLayout;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contract);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Reload page
        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_dark);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        if (editor.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER).equals(ImmutableValue.ROLE_OWNER)) {
                            loadDataForOwner();
                        } else {
                            loadDataForCustomer();
                        }
                    }
                }, 500);
            }
        });


        txt_manage_contract_error = (TextView) findViewById(R.id.txt_manage_contract_error);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_contract_manage_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        if (editor.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER).equals(ImmutableValue.ROLE_OWNER)) {
            loadDataForOwner();
        } else {
            loadDataForCustomer();
        }

    }

    private void loadDataForOwner() {
        dialog = ProgressDialog.show(ManageContractActivity.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int ownerID = sharedPreferences.getInt(ImmutableValue.HOME_userID, 0);
        Call<List<ContractItem>> responseBodyCall = contractAPI.findContractByOwnerID(ownerID);
        responseBodyCall.enqueue(new Callback<List<ContractItem>>() {
            @Override
            public void onResponse(Call<List<ContractItem>> call, Response<List<ContractItem>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        txt_manage_contract_error.setVisibility(View.INVISIBLE);
                        contractItemList = response.body();
                        manageContractAdapter = new ManageContractAdapter(contractItemList, ManageContractActivity.this);
                        recyclerView.setAdapter(manageContractAdapter);
                        manageContractAdapter.notifyDataSetChanged();
                    } else {
                        txt_manage_contract_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    txt_manage_contract_error.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ManageContractActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataForCustomer() {
        dialog = ProgressDialog.show(ManageContractActivity.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int customerID = sharedPreferences.getInt(ImmutableValue.HOME_userID, 0);
        Call<List<ContractItem>> responseBodyCall = contractAPI.findContractByCustomerID(customerID);
        responseBodyCall.enqueue(new Callback<List<ContractItem>>() {
            @Override
            public void onResponse(Call<List<ContractItem>> call, Response<List<ContractItem>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        txt_manage_contract_error.setVisibility(View.INVISIBLE);
                        contractItemList = response.body();
                        manageContractAdapter = new ManageContractAdapter(contractItemList, ManageContractActivity.this);
                        recyclerView.setAdapter(manageContractAdapter);
                        manageContractAdapter.notifyDataSetChanged();
                    } else {
                        txt_manage_contract_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    txt_manage_contract_error.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                dialog.dismiss();
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
        SharedPreferences settings_3 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings_3.edit().clear().commit();
        ManageContractActivity.this.finish();
        super.onBackPressed();
    }
}
