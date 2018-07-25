package com.example.manhvdse61952.vrc_android.controller.layout.promotion;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
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
    TextView txtStartTimePromo, txtEndTimePromo;
    Spinner spnPromoQuantity;
    RecyclerView check_list;
    Button btnCreatePromo;
    PromotionAdapter promotionAdapter;
    ProgressDialog dialogTemp;

    Calendar calendar;
    int currentDay = 0, currentMonth = 0, currentYear = 0, checkLoop = -1;;
    float discountValue = 0;
    long startDateLong = 0, endDateLong = 0;

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

        getListPromotion();

        btnCreatePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPromotionAction();
            }
        });

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
        txtStartTimePromo = (TextView) findViewById(R.id.txtStartTimePromo);
        txtEndTimePromo = (TextView) findViewById(R.id.txtEndTimePromo);
        spnPromoQuantity = (Spinner) findViewById(R.id.spnPromoQuantity);
        check_list = (RecyclerView) findViewById(R.id.check_list);
        btnCreatePromo = (Button) findViewById(R.id.btnCreatePromo);
    }

    private void initLayout() {
        ImmutableValue.vehicleFrameNumberListGeneral = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        check_list.setLayoutManager(mLayoutManager);

        // set data for calendar
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Init spinner value
        List<String> listValue = new ArrayList<>();
        listValue.add(0, "Chọn mức giảm");
        listValue.add("5%");
        listValue.add("10%");
        listValue.add("15%");
        listValue.add("20%");
        listValue.add("25%");
        listValue.add("30%");
        listValue.add("35%");
        listValue.add("50%");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PromotionActivity.this, android.R.layout.simple_spinner_dropdown_item, listValue);
        spnPromoQuantity.setAdapter(dataAdapter);
        spnPromoQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if (adapterView.getItemAtPosition(i).equals("Chọn mức giảm")) {
                    discountValue = 0;
                } else {
                    switch (adapterView.getItemAtPosition(i).toString()) {
                        case "5%":
                            discountValue = (float) 0.05;
                            break;
                        case "10%":
                            discountValue = (float) 0.1;
                            break;
                        case "15%":
                            discountValue = (float) 0.15;
                            break;
                        case "20%":
                            discountValue = (float) 0.2;
                            break;
                        case "25%":
                            discountValue = (float) 0.25;
                            break;
                        case "30%":
                            discountValue = (float) 0.3;
                            break;
                        case "35%":
                            discountValue = (float) 0.35;
                            break;
                        case "50%":
                            discountValue = (float) 0.5;
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Show calendar
        txtStartTimePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendarStartDate(txtStartTimePromo);
            }
        });
        txtEndTimePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendarEndDate(txtEndTimePromo);
            }
        });

    }

    private void setCalendarStartDate(final TextView startTextView) {
        DatePickerDialog dd = new DatePickerDialog(PromotionActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);
                            startTextView.setText(formatter.format(date).toString());
                            startDateLong = date.getTime();
                        } catch (Exception ex) {

                        }
                    }
                }, currentYear, currentMonth, currentDay);
        Date date = Calendar.getInstance().getTime();
        dd.getDatePicker().setMinDate(date.getTime());
        dd.show();
    }

    private void setCalendarEndDate(final TextView endTextView) {
        DatePickerDialog dd = new DatePickerDialog(PromotionActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);
                            endTextView.setText(formatter.format(date).toString());
                            endDateLong = date.getTime();
                        } catch (Exception ex) {

                        }
                    }
                }, currentYear, currentMonth, currentDay);
        Date date = Calendar.getInstance().getTime();
        dd.getDatePicker().setMinDate(date.getTime());
        dd.show();
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
                    List<Discount> discountList = response.body();
                    promotionAdapter = new PromotionAdapter(discountList, PromotionActivity.this, PromotionActivity.this);
                    check_list.setAdapter(promotionAdapter);
                    promotionAdapter.notifyDataSetChanged();
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

    private void createPromotionAction(){
        if (startDateLong == 0 || endDateLong == 0 || startDateLong > endDateLong){
            Toast.makeText(this, "Vui lòng chọn ngày hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (startDateLong != 0 && endDateLong != 0 && discountValue == 0){
            Toast.makeText(this, "Vui lòng chọn mức giảm giá", Toast.LENGTH_SHORT).show();
        } else if (startDateLong != 0 && endDateLong != 0 && discountValue != 0 && ImmutableValue.vehicleFrameNumberListGeneral.size() == 0){
            Toast.makeText(this, "Vui lòng chọn xe cần thêm giảm giá", Toast.LENGTH_SHORT).show();
        } else {

            dialogTemp = ProgressDialog.show(PromotionActivity.this, "Hệ thống",
                    "Đang xử lý ...", true);
            forwardLoop();
        }
    }

    private void forwardLoop(){
        if (checkLoop >= ImmutableValue.vehicleFrameNumberListGeneral.size()-1){
            dialogTemp.dismiss();
            Toast.makeText(this, "Tạo thành công", Toast.LENGTH_SHORT).show();
            PromotionActivity.this.finish();
            startActivity(getIntent());
            return;
        }
        checkLoop++;
        Retrofit retrofit = RetrofitConfig.getClient();
        DiscountAPI discountAPI = retrofit.create(DiscountAPI.class);
        Call<ResponseBody> responseBodyCall = discountAPI.updatePromotion(ImmutableValue.vehicleFrameNumberListGeneral.get(checkLoop)
                ,discountValue, startDateLong, endDateLong);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    forwardLoop();
                } else {
                    dialogTemp.dismiss();
                    Toast.makeText(PromotionActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialogTemp.dismiss();
                Toast.makeText(PromotionActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
