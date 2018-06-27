package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.order.MainOrderOne;
import com.example.manhvdse61952.vrc_android.model.searchModel.MainItemModel;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainItem extends AppCompatActivity {
    Slider sld;
    ViewPager vpg;
    Button btnOrderRent;
    MainItemModel mainObj = new MainItemModel();

    TextView item_price_unit, item_price_slot, item_price_day, item_seat, item_year,
            item_plateNumber, item_ownerName, item_engine, item_tranmission, txt_hours, txt_day_start,
            txt_hours_2, txt_day_end;
    CheckBox cbx1, cbx2;
    LinearLayout ln_pickTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);
        //Declare id
        item_price_unit = (TextView) findViewById(R.id.item_price_unit);
        item_price_slot = (TextView) findViewById(R.id.item_price_slot);
        item_price_day = (TextView) findViewById(R.id.item_price_day);
        item_seat = (TextView) findViewById(R.id.item_seat);
        item_year = (TextView) findViewById(R.id.item_year);
        item_plateNumber = (TextView) findViewById(R.id.item_plateNumber);
        item_ownerName = (TextView) findViewById(R.id.item_ownerName);
        item_engine = (TextView) findViewById(R.id.item_engine);
        item_tranmission = (TextView) findViewById(R.id.item_tranmission);
        cbx1 = (CheckBox) findViewById(R.id.cbx1);
        cbx2 = (CheckBox) findViewById(R.id.cbx2);
        ln_pickTime = (LinearLayout)findViewById(R.id.ln_pickTime);
        txt_hours = (TextView)findViewById(R.id.txt_hours);
        txt_day_start = (TextView)findViewById(R.id.txt_day_start);
        txt_hours_2 = (TextView)findViewById(R.id.txt_hours_2);
        txt_day_end = (TextView)findViewById(R.id.txt_day_end);

        vpg = (ViewPager) findViewById(R.id.vpg);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString("ID", "aaaaaa");
        final String vehicleType = editor.getString("type", "XE_MAY");
        final String vehicleSeat = editor.getString("seat", "0");
        String startHour = editor.getString("startHour", "--");
        String endHour = editor.getString("endHour", "--");
        String startMinute = editor.getString("startMinute", "--");
        String endMinute = editor.getString("endMinute", "--");
        String startDate = editor.getString("startDate", "--/--/----");
        String endDate = editor.getString("endDate", "--/--/----");

        //Show start date and end date
        txt_hours.setText(startHour + " : " + startMinute);
        txt_day_start.setText(startDate);
        txt_hours_2.setText(endHour + " : " + endMinute);
        txt_day_end.setText(endDate);


        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<MainItemModel> responseBodyCall = testAPI.getVehicleByFrameNumber(frameNumber);
        responseBodyCall.enqueue(new Callback<MainItemModel>() {
            @Override
            public void onResponse(Call<MainItemModel> call, Response<MainItemModel> response) {
                if (response.body() != null) {
                    mainObj = new MainItemModel();
                    mainObj = response.body();
                    // use for init layout
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("imageFront", mainObj.getImageLinkFront());
                    editor.putString("imageBack", mainObj.getImageLinkBack());
                    editor.putString("vehicleName", mainObj.getVehicleMaker() + " " + mainObj.getVehicleModel());
                    editor.apply();

                    sld = new Slider(MainItem.this);
                    vpg.setAdapter(sld);
                    if (vehicleType.equals("XE_MAY")) {
                        item_price_unit.setText("Đơn giá / giờ");
                        NumberFormat nf = new DecimalFormat("#.####");
                        String price = ImmutableValue.convertPrice(nf.format(mainObj.getRentFeePerHour()));
                        item_price_slot.setText(price + " vnđ");
                        item_engine.setText("Xăng");
                        if (mainObj.getScooter() == true) {
                            item_tranmission.setText("Xe tay ga");
                        } else {
                            item_tranmission.setText("Xe số");
                        }
                    } else {
                        item_price_unit.setText("Đơn giá / buổi");
                        NumberFormat nf = new DecimalFormat("#.####");
                        String price = ImmutableValue.convertPrice(nf.format(mainObj.getRentFeePerSlot()));
                        item_price_slot.setText(price + " vnđ");
                        if (mainObj.getGasoline() == true) {
                            item_engine.setText("Xăng");
                        } else {
                            item_engine.setText("Dầu");
                        }
                        if (mainObj.getManual() == true) {
                            item_tranmission.setText("Số sàn");
                        } else {
                            item_tranmission.setText("Số tự động");
                        }
                    }

                    NumberFormat nf = new DecimalFormat("#.####");
                    String price = ImmutableValue.convertPrice(nf.format(mainObj.getRentFeePerDay()));
                    item_price_day.setText(price + " vnđ");
                    item_seat.setText(vehicleSeat);
                    item_year.setText(mainObj.getModelYear() + "");
                    item_plateNumber.setText(mainObj.getPlateNumber());
                    item_ownerName.setText(mainObj.getOwnerFullName());
                    if (mainObj.getRequireIdCard() == true) {
                        cbx1.setChecked(true);
                    } else {
                        cbx1.setChecked(false);
                    }
                    if (mainObj.getRequireHouseHold() == true) {
                        cbx2.setChecked(true);
                    } else {
                        cbx2.setChecked(false);
                    }

                    ln_pickTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(MainItem.this, CalendarCustom.class);
                            startActivity(it);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MainItemModel> call, Throwable t) {
                Toast.makeText(MainItem.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });


        btnOrderRent = (Button) findViewById(R.id.btnOrderRent);
        btnOrderRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainItem.this, MainOrderOne.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();
        super.onBackPressed();
    }
}
