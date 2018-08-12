package com.example.manhvdse61952.vrc_android.controller.layout.promotion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.DiscountAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.squareup.picasso.Picasso;

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

public class PromotionDetail extends AppCompatActivity {
    ImageView img_discount, img_calendar;
    TextView txt_discount_name, txt_discount_value, txt_discount_time, txtStartTimePromo, txtEndTimePromo;
    LinearLayout ln_hide_remove, ln_discount_create;
    Button btn_remove_discount, btn_create_discount;
    Spinner spnPromoQuantity;

    Calendar calendar;
    int currentDay = 0, currentMonth = 0, currentYear = 0;
    long startDateLong = 0, endDateLong = 0;
    float discountValue = 0;
    String frameNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();

        btn_create_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDiscount();
            }
        });

        btn_remove_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDiscount();
            }
        });

        txtStartTimePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendarStartDate(txtStartTimePromo);
            }
        });

        txtEndTimePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendarEndDate(txtEndTimePromo);
            }
        });
    }

    private void declareID() {
        img_discount = (ImageView) findViewById(R.id.img_discount);
        img_calendar = (ImageView) findViewById(R.id.img_calendar);
        txt_discount_name = (TextView) findViewById(R.id.txt_discount_name);
        txt_discount_value = (TextView) findViewById(R.id.txt_discount_value);
        txt_discount_time = (TextView) findViewById(R.id.txt_discount_time);
        ln_hide_remove = (LinearLayout) findViewById(R.id.ln_hide_remove);
        ln_discount_create = (LinearLayout) findViewById(R.id.ln_discount_create);
        btn_remove_discount = (Button) findViewById(R.id.btn_remove_discount);
        btn_create_discount = (Button) findViewById(R.id.btn_create_discount);
        txtStartTimePromo = (TextView) findViewById(R.id.txtStartTimePromo);
        txtEndTimePromo = (TextView) findViewById(R.id.txtEndTimePromo);
        spnPromoQuantity = (Spinner) findViewById(R.id.spnPromoQuantity);
    }

    private void initLayout() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.DISCOUNT_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        frameNumber = editor.getString(ImmutableValue.DISCOUNT_vehicle_frame, "");
        if (editor.getString(ImmutableValue.DISCOUNT_imageFront, "").equals("")){
            Picasso.get().load(R.drawable.img_default_image).into(img_discount);
        } else {
            Picasso.get().load(editor.getString(ImmutableValue.DISCOUNT_imageFront, "")).into(img_discount);
        }
        txt_discount_name.setText(editor.getString(ImmutableValue.DISCOUNT_vehicle_name, ""));
        if (!editor.getString(ImmutableValue.DISCOUNT_value, "0").equals("0")) {
            txt_discount_value.setText("Khuyến mãi" + editor.getString(ImmutableValue.DISCOUNT_value, "0") + " %");
            txt_discount_time.setText(editor.getString(ImmutableValue.DISCOUNT_date, ""));
            ln_discount_create.setVisibility(View.INVISIBLE);
            btn_create_discount.setVisibility(View.INVISIBLE);
        } else {
            txt_discount_value.setText("Không có khuyến mãi");
            txt_discount_value.setTextColor(Color.parseColor("#FF0000"));
            img_calendar.setVisibility(View.INVISIBLE);
            txt_discount_time.setVisibility(View.INVISIBLE);
            GeneralController.scaleView(ln_hide_remove, 0);
        }

        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PromotionDetail.this, android.R.layout.simple_spinner_dropdown_item, listValue);
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

    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences(ImmutableValue.DISCOUNT_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();
        PromotionDetail.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setCalendarStartDate(final TextView startTextView) {
        DatePickerDialog dd = new DatePickerDialog(PromotionDetail.this,
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
        DatePickerDialog dd = new DatePickerDialog(PromotionDetail.this,
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

    private void removeDiscount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PromotionDetail.this);
        builder.setMessage("Bạn có chắc chắn muốn hủy khuyến mãi của xe này").setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Retrofit retrofit = RetrofitConfig.getClient();
                        DiscountAPI discountAPI = retrofit.create(DiscountAPI.class);
                        Call<ResponseBody> responseBodyCall = discountAPI.removePromotion(frameNumber);
                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    SharedPreferences settings = getSharedPreferences(ImmutableValue.DISCOUNT_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                    settings.edit().clear().commit();
                                    Toast.makeText(PromotionDetail.this, "Hủy thành công", Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    PromotionDetail.this.finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(PromotionDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void createDiscount() {
        if (startDateLong == 0 || endDateLong == 0 || startDateLong > endDateLong) {
            Toast.makeText(this, "Vui lòng chọn ngày hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (startDateLong != 0 && endDateLong != 0 && discountValue == 0) {
            Toast.makeText(this, "Vui lòng chọn mức giảm giá", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = RetrofitConfig.getClient();
            DiscountAPI discountAPI = retrofit.create(DiscountAPI.class);
            Call<ResponseBody> responseBodyCall = discountAPI.updatePromotion(frameNumber, discountValue, startDateLong, endDateLong);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        SharedPreferences settings = getSharedPreferences(ImmutableValue.DISCOUNT_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        settings.edit().clear().commit();
                        Toast.makeText(PromotionDetail.this, "Tạo khuyến mãi thành công", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        PromotionDetail.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PromotionDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
