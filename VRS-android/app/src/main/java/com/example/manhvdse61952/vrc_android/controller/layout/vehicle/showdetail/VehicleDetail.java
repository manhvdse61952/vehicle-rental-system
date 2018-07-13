package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.order.PaypalExecute;
import com.example.manhvdse61952.vrc_android.model.search_model.DetailVehicleItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VehicleDetail extends AppCompatActivity {
    ImageSlider sld;
    ViewPager vpg;
    Button btnOrderRent;
    DetailVehicleItem mainObj = new DetailVehicleItem();
    int selectTab = 0, rentFeePerHourID = 0, rentFeePerDayID = 0, totalHour = 0, receiveType = 0, totalDay = 0;
    Double totalMoney = 0.0, rentFeeMoney = 0.0, usdConvert = 0.0;

    TextView item_price_slot, item_price_day, item_seat, item_year,
            item_plateNumber, item_ownerName, item_engine, item_tranmission, txt_hours, txt_day_start,
            txt_hours_2, txt_day_end, txt_order_type, txt_day_rent, txt_hour_rent,
            txt_money_day_rent, txt_money_hour_rent, txt_money_total, item_price_deposit,
            txt_usd_convert, txt_money_deposit, txt_pickTime;
    CheckBox cbx1, cbx2;
    LinearLayout ln_pickTime;
    Switch swt_order_type;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);
        //Declare id
        txt_day_rent = (TextView) findViewById(R.id.txt_day_rent);
        txt_hour_rent = (TextView) findViewById(R.id.txt_hour_rent);
        txt_money_day_rent = (TextView) findViewById(R.id.txt_money_day_rent);
        txt_money_hour_rent = (TextView) findViewById(R.id.txt_money_hour_rent);
        txt_money_total = (TextView) findViewById(R.id.txt_money_total);
        item_price_slot = (TextView) findViewById(R.id.item_price_slot);
        item_price_day = (TextView) findViewById(R.id.item_price_day);
        item_seat = (TextView) findViewById(R.id.item_seat);
        item_year = (TextView) findViewById(R.id.item_year);
        item_plateNumber = (TextView) findViewById(R.id.item_plateNumber);
        item_ownerName = (TextView) findViewById(R.id.item_ownerName);
        item_engine = (TextView) findViewById(R.id.item_engine);
        item_tranmission = (TextView) findViewById(R.id.item_tranmission);
        item_price_deposit = (TextView)findViewById(R.id.item_price_deposit);
        cbx1 = (CheckBox) findViewById(R.id.cbx1);
        cbx2 = (CheckBox) findViewById(R.id.cbx2);
        ln_pickTime = (LinearLayout) findViewById(R.id.ln_pickTime);
        txt_hours = (TextView) findViewById(R.id.txt_hours);
        txt_day_start = (TextView) findViewById(R.id.txt_day_start);
        txt_hours_2 = (TextView) findViewById(R.id.txt_hours_2);
        txt_day_end = (TextView) findViewById(R.id.txt_day_end);
        swt_order_type = (Switch) findViewById(R.id.swt_order_type);
        txt_order_type = (TextView) findViewById(R.id.txt_order_type);
        txt_usd_convert = (TextView)findViewById(R.id.txt_usd_convert);
        txt_money_deposit = (TextView)findViewById(R.id.txt_money_deposit);
        vpg = (ViewPager) findViewById(R.id.vpg);
        txt_pickTime = (TextView)findViewById(R.id.txt_pickTime);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString(ImmutableValue.MAIN_vehicleID, "aaaaaa");
        final String vehicleType = editor.getString(ImmutableValue.MAIN_vehicleType, ImmutableValue.XE_MAY);
        final String vehicleSeat = editor.getString(ImmutableValue.MAIN_vehicleSeat, "0");
        String startHour = editor.getString(ImmutableValue.MAIN_startHour, "--");
        String endHour = editor.getString(ImmutableValue.MAIN_endHour, "--");
        String startMinute = editor.getString(ImmutableValue.MAIN_startMinute, "--");
        String endMinute = editor.getString(ImmutableValue.MAIN_endMinute, "--");
        String startDate = editor.getString(ImmutableValue.MAIN_startDate, "--/--/----");
        String endDate = editor.getString(ImmutableValue.MAIN_endDate, "--/--/----");
        totalDay = editor.getInt(ImmutableValue.MAIN_totalDay, 0);
        totalHour = editor.getInt(ImmutableValue.MAIN_totalHour, 0);
        final int totalMinute = editor.getInt(ImmutableValue.MAIN_totalMinute, 0);

        //Show start date and end date + price
        txt_hours.setText(startHour + " : " + startMinute);
        txt_day_start.setText(startDate);
        txt_hours_2.setText(endHour + " : " + endMinute);
        txt_day_end.setText(endDate);
        txt_day_rent.setText(totalDay + "");
        if (totalMinute > 20 && totalMinute <= 59) {
            totalHour = totalHour + 1;
        }
        txt_hour_rent.setText(totalHour + "");
        if (totalHour == 23){
            totalHour = 0;
            totalDay = totalDay + 1;
        }

        Retrofit test = RetrofitConfig.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<DetailVehicleItem> responseBodyCall = testAPI.getVehicleByFrameNumber(frameNumber);
        responseBodyCall.enqueue(new Callback<DetailVehicleItem>() {
            @Override
            public void onResponse(Call<DetailVehicleItem> call, Response<DetailVehicleItem> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        mainObj = new DetailVehicleItem();
                        mainObj = response.body();
                        // use for init layout
                        final SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                        editor.putString(ImmutableValue.MAIN_vehicleImgFront, mainObj.getImageLinkFront());
                        editor.putString(ImmutableValue.MAIN_vehicleImgBack, mainObj.getImageLinkBack());
                        editor.putString(ImmutableValue.MAIN_vehicleName, mainObj.getVehicleMaker() + " " + mainObj.getVehicleModel());
                        editor.apply();

                        sld = new ImageSlider(VehicleDetail.this);
                        vpg.setAdapter(sld);
                        if (vehicleType.equals(ImmutableValue.XE_MAY)) {
                            selectTab = 0;
                            item_engine.setText("Xăng");
                            if (mainObj.getScooter() == true) {
                                item_tranmission.setText("Xe tay ga");
                            } else {
                                item_tranmission.setText("Xe số");
                            }
                        } else {
                            if (vehicleType.equals(ImmutableValue.XE_CA_NHAN)){
                                selectTab = 1;
                            } else if (vehicleType.equals(ImmutableValue.XE_DU_LICH)) {
                                selectTab = 2;
                            }


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
                        String priceHour = PermissionDevice.convertPrice(nf.format(mainObj.getRentFeePerHour()));
                        item_price_slot.setText(priceHour);
                        String priceDay = PermissionDevice.convertPrice(nf.format(mainObj.getRentFeePerDay()));
                        item_price_day.setText(priceDay);
                        String deposit = PermissionDevice.convertPrice(nf.format(mainObj.getDeposit()));
                        editor.putString("depositFeeConvert", deposit);
                        item_price_deposit.setText(deposit);
                        txt_money_deposit.setText(deposit);

                        item_seat.setText(vehicleSeat);
                        item_year.setText(mainObj.getModelYear() + "");
                        editor.putInt("vehicleYear", mainObj.getModelYear());
                        item_plateNumber.setText(mainObj.getPlateNumber());
                        item_ownerName.setText(mainObj.getOwnerFullName());
                        editor.putString("ownerName", mainObj.getOwnerFullName());
                        editor.apply();

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

                        //Start pick date time intent
                        ln_pickTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(VehicleDetail.this, CalendarCustom.class);
                                startActivity(it);
                            }
                        });

                        //Use for switch
                        scaleView(txt_order_type, 0f, 0f);
                        swt_order_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    receiveType = 0;
                                    textColorAnimated(swt_order_type, Color.parseColor("#000000"));
                                    scaleView(txt_order_type, 0f, 0f);
                                } else {
                                    receiveType = 1;
                                    textColorAnimated(swt_order_type, Color.parseColor("#cccccc"));
                                    scaleView(txt_order_type, 0f, 1f);
                                }
                            }
                        });

                        totalMoney = totalDay * mainObj.getRentFeePerDay() + totalHour * mainObj.getRentFeePerHour()
                                + mainObj.getDeposit();

                        if (totalDay ==  0 && totalHour == 0){
                            txt_day_rent.setText("0");
                            txt_hour_rent.setText("0");
                        } else {
                            dialog = ProgressDialog.show(VehicleDetail.this, "Đang xử lý",
                                    "Vui lòng đợi ...", true);
                            Retrofit test = RetrofitConfig.getClient();
                            final ContractAPI testAPI = test.create(ContractAPI.class);
                            Call<Double> responseBodyCall = testAPI.convertUSD(totalMoney.intValue());
                            responseBodyCall.enqueue(new Callback<Double>() {
                                @Override
                                public void onResponse(Call<Double> call, Response<Double> response) {
                                    if (response.code() == 200){
                                        usdConvert = response.body();
                                        rentFeeMoney = totalDay * mainObj.getRentFeePerDay() + totalHour * mainObj.getRentFeePerHour() ;
                                        rentFeePerDayID = mainObj.getRentFeePerDayID();
                                        rentFeePerHourID = mainObj.getRentFeePerHourID();
                                        NumberFormat nf = new DecimalFormat("#.####");
                                        String showDayMoney = PermissionDevice.convertPrice(nf.format(totalDay * mainObj.getRentFeePerDay()));
                                        String showHourMoney = PermissionDevice.convertPrice(nf.format(totalHour * mainObj.getRentFeePerHour()));
                                        String showTotalMoney = PermissionDevice.convertPrice(nf.format(totalMoney));
                                        String rentFeeConvert = PermissionDevice.convertPrice(nf.format(rentFeeMoney));
                                        editor.putString("rentFeeConvert", rentFeeConvert);
                                        editor.putString("totalFeeConvert", showTotalMoney);
                                        editor.apply();
                                        txt_money_day_rent.setText(showDayMoney);
                                        txt_money_hour_rent.setText(showHourMoney);
                                        txt_money_total.setText(showTotalMoney);
                                        txt_usd_convert.setText(usdConvert + "");
                                    } else {
                                        Toast.makeText(VehicleDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Double> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(VehicleDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(VehicleDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DetailVehicleItem> call, Throwable t) {
                Toast.makeText(VehicleDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });


        btnOrderRent = (Button) findViewById(R.id.btnOrderRent);
        btnOrderRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_day_rent.getText().toString().equals("0") && txt_hour_rent.getText().toString().equals("0") && txt_usd_convert.getText().toString().equals("0")){
                    Toast.makeText(VehicleDetail.this, "Vui lòng chọn ngày giờ thuê xe", Toast.LENGTH_SHORT).show();
                    txt_pickTime.setFocusable(true);
                    txt_pickTime.setTextColor(Color.RED);
                } else {
                    txt_pickTime.setTextColor(Color.parseColor("#212121"));
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.MAIN_totalMoney, Double.toString(usdConvert));
                    editor.putString(ImmutableValue.MAIN_rentFeeMoney, Double.toString(rentFeeMoney));
                    editor.putInt(ImmutableValue.MAIN_rentFeePerDayID, rentFeePerDayID);
                    editor.putInt(ImmutableValue.MAIN_rentFeePerHourID, rentFeePerHourID);
                    editor.putInt(ImmutableValue.MAIN_receiveType, receiveType);
                    editor.apply();
                    Intent it = new Intent(VehicleDetail.this, PaypalExecute.class);
                    startActivity(it);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putInt(ImmutableValue.HOME_tabIndex, selectTab);
        editor.apply();
        Intent it = new Intent(VehicleDetail.this, MainActivity.class);
        startActivity(it);
    }

    public void scaleView(TextView v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        anim.setFillAfter(true);
        anim.setDuration(500);
        v.startAnimation(anim);
    }

    public void textColorAnimated(Switch v, int colorValue) {
        final ObjectAnimator animator = ObjectAnimator.ofInt(v, "textColor", colorValue);
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.start();
    }
}
