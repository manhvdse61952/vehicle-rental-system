package com.example.manhvdse61952.vrc_android.controller.layout.promotion;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.DiscountAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Discount;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PromotionActivity extends AppCompatActivity {
    RecyclerView rv_list_discount;
    PromotionAdapter promotionAdapter;
    SwipeRefreshLayout swipeLayout;
    List<Discount> discountList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();

        getListVehicleOfOwner();

        reloadData();
    }

    @Override
    public void onBackPressed() {
        PromotionActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void declareID() {
        rv_list_discount = (RecyclerView) findViewById(R.id.rv_list_discount);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }

    private void initLayout() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_list_discount.setLayoutManager(mLayoutManager);
    }


    private void getListVehicleOfOwner() {
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(PromotionActivity.this, "Hệ thống",
                "Đang xử lý ...", true);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int ownerID = editor.getInt(ImmutableValue.HOME_userID, 0);
        Retrofit retrofit = RetrofitConfig.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<List<SearchVehicleItem>> responseBodyCall = vehicleAPI.getVehicleByOwnerID(ownerID);
        responseBodyCall.enqueue(new Callback<List<SearchVehicleItem>>() {
            @Override
            public void onResponse(Call<List<SearchVehicleItem>> call, Response<List<SearchVehicleItem>> response) {
                if (response.code() == 200) {
                    List<SearchVehicleItem> listVehicle = response.body();
                    if (listVehicle.size() > 0) {
                        for (int i = 0; i < listVehicle.size(); i++) {
                            Discount obj = new Discount();
                            obj.setVehicleFrameNumber(listVehicle.get(i).getFrameNumber());
                            obj.setVehicleMaker(listVehicle.get(i).getVehicleMaker());
                            obj.setVehicleModel(listVehicle.get(i).getVehicleModel());
                            obj.setDiscountValue(0);
                            obj.setDiscountID(0);
                            obj.setDiscountStatus(ImmutableValue.DISCOUNT_DISABLE);
                            obj.setStartDay(0);
                            obj.setEndDay(0);
                            obj.setImageLinkFront(listVehicle.get(i).getImageLinkFront());
                            discountList.add(obj);
                        }
                    }
                    getListPromotion();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<SearchVehicleItem>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PromotionActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListPromotion() {
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(PromotionActivity.this, "Hệ thống",
                "Đang xử lý ...", true);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int ownerID = editor.getInt(ImmutableValue.HOME_userID, 0);
        Retrofit retrofit = RetrofitConfig.getClient();
        DiscountAPI discountAPI = retrofit.create(DiscountAPI.class);
        Call<List<Discount>> responseBodyCall = discountAPI.getListPromotion(ownerID);
        responseBodyCall.enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                if (response.code() == 200) {
                    List<Discount> listDiscountTemp = response.body();
                    if (listDiscountTemp.size() > 0) {
                        for (int i = 0; i < listDiscountTemp.size(); i++) {
                            for (int j = 0; j < discountList.size(); j++) {
                                if (listDiscountTemp.get(i).getVehicleFrameNumber()
                                        .equals(discountList.get(j).getVehicleFrameNumber())) {
                                    discountList.get(j).setDiscountID(listDiscountTemp.get(i).getDiscountID());
                                    discountList.get(j).setStartDay(listDiscountTemp.get(i).getStartDay());
                                    discountList.get(j).setEndDay(listDiscountTemp.get(i).getEndDay());
                                    discountList.get(j).setDiscountStatus(listDiscountTemp.get(i).getDiscountStatus());
                                    discountList.get(j).setDiscountValue(listDiscountTemp.get(i).getDiscountValue());
                                }
                            }
                        }
                        promotionAdapter = new PromotionAdapter(discountList, PromotionActivity.this, PromotionActivity.this);
                        rv_list_discount.setAdapter(promotionAdapter);
                        promotionAdapter.notifyDataSetChanged();

                    }
                }
                dialog.dismiss();
            }


            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PromotionActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                finish();
                startActivity(getIntent());
            }
        }
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
                        getListVehicleOfOwner();
                    }
                }, 500);
            }
        });
    }

}
