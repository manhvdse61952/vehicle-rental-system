package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.contract_tab.ExecuteContractTab;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.contract_tab.FinishContractTab;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.contract_tab.RemoveContractTab;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.main.SectionPageAdapter;
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
    public static List<ContractItem> listContractFinish = new ArrayList<>();
    public static List<ContractItem> listContractRemove = new ArrayList<>();
    public static List<ContractItem> listContractAnother = new ArrayList<>();
    public static SectionPageAdapter secAdapter;
    public static ViewPager viewPager;
    private TabLayout tabLayout;

    TextView txt_manage_contract_error;
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

        txt_manage_contract_error = (TextView) findViewById(R.id.txt_manage_contract_error);
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
                        listContractFinish = new ArrayList<>();
                        listContractRemove = new ArrayList<>();
                        listContractAnother = new ArrayList<>();
                        for (int i = 0; i < contractItemList.size();i++){
                            if (contractItemList.get(i).getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)){
                                listContractFinish.add(contractItemList.get(i));
                            } else if (contractItemList.get(i).getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)){
                                listContractRemove.add(contractItemList.get(i));
                            } else {
                                listContractAnother.add(contractItemList.get(i));
                            }
                        }
                        viewPager = (ViewPager) findViewById(R.id.container);
                        setupViewPager(viewPager);
                        tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                        createTabIcons();
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
                        listContractFinish = new ArrayList<>();
                        listContractRemove = new ArrayList<>();
                        listContractAnother = new ArrayList<>();
                        for (int i = 0; i < contractItemList.size();i++){
                            if (contractItemList.get(i).getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)){
                                listContractFinish.add(contractItemList.get(i));
                            } else if (contractItemList.get(i).getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)){
                                listContractRemove.add(contractItemList.get(i));
                            } else {
                                listContractAnother.add(contractItemList.get(i));
                            }
                        }
                        viewPager = (ViewPager) findViewById(R.id.container);
                        setupViewPager(viewPager);
                        tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                        createTabIcons();
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

    private void setupViewPager(ViewPager viewPager) {
        secAdapter = new SectionPageAdapter(getSupportFragmentManager());
        secAdapter.addFragment(new ExecuteContractTab(), "Cần xử lý");
        secAdapter.addFragment(new FinishContractTab(), "Hoàn thành");
        secAdapter.addFragment(new RemoveContractTab(), "Hủy");
        viewPager.setAdapter(secAdapter);
    }

    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Cần xử lý");
        tabOne.setTextSize(16);
        tabOne.setTextColor(Color.parseColor("#0288D1"));
        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0288D1"));

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Hoàn thành");
        tabTwo.setTextSize(16);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Hủy");
        tabThree.setTextColor(Color.RED);
        tabThree.setTextSize(16);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0288D1"));
                } else if (tab.getPosition() == 1){
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#009688"));
                } else {
                    tabLayout.setSelectedTabIndicatorColor(Color.RED);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
