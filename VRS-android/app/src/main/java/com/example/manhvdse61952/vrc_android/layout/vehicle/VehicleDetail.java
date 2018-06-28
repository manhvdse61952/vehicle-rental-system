package com.example.manhvdse61952.vrc_android.layout.vehicle;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.model.searchModel.MainItemModel;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

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
    MainItemModel mainObj = new MainItemModel();

    TextView item_price_slot, item_price_day, item_seat, item_year,
            item_plateNumber, item_ownerName, item_engine, item_tranmission, txt_hours, txt_day_start,
            txt_hours_2, txt_day_end, txt_order_type, txt_day_rent, txt_hour_rent,
            txt_minute_rent, txt_money_day_rent, txt_money_hour_rent, txt_money_minute_rent, txt_money_total;
    CheckBox cbx1, cbx2;
    LinearLayout ln_pickTime;
    Switch swt_order_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);
        //Declare id
        txt_day_rent = (TextView)findViewById(R.id.txt_day_rent);
        txt_hour_rent = (TextView)findViewById(R.id.txt_hour_rent);
        txt_minute_rent = (TextView)findViewById(R.id.txt_minute_rent);
        txt_money_day_rent = (TextView)findViewById(R.id.txt_money_day_rent);
        txt_money_hour_rent = (TextView)findViewById(R.id.txt_money_hour_rent);
        txt_money_minute_rent = (TextView)findViewById(R.id.txt_money_minute_rent);
        txt_money_total = (TextView)findViewById(R.id.txt_money_total);
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
        swt_order_type = (Switch)findViewById(R.id.swt_order_type);
        txt_order_type = (TextView)findViewById(R.id.txt_order_type);
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
        final int totalDay = editor.getInt("totalDay", 0);
        final int totalHour = editor.getInt("totalHour", 0);
        final int totalMinute = editor.getInt("totalMinute", 0);

        //Show start date and end date + price
        txt_hours.setText(startHour + " : " + startMinute);
        txt_day_start.setText(startDate);
        txt_hours_2.setText(endHour + " : " + endMinute);
        txt_day_end.setText(endDate);
        txt_day_rent.setText(totalDay + "");
        txt_hour_rent.setText(totalHour + "");
        txt_minute_rent.setText(totalMinute + "");


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

                    sld = new ImageSlider(VehicleDetail.this);
                    vpg.setAdapter(sld);
                    if (vehicleType.equals("XE_MAY")) {
                        item_engine.setText("Xăng");
                        if (mainObj.getScooter() == true) {
                            item_tranmission.setText("Xe tay ga");
                        } else {
                            item_tranmission.setText("Xe số");
                        }
                    } else {
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
                    String priceHour = ImmutableValue.convertPrice(nf.format(mainObj.getRentFeePerHour()));
                    item_price_slot.setText(priceHour + " vnđ");
                    String priceDay = ImmutableValue.convertPrice(nf.format(mainObj.getRentFeePerDay()));
                    item_price_day.setText(priceDay + " vnđ");
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
                            if (isChecked){
                                textColorAnimated(swt_order_type, Color.parseColor("#000000"));
                                scaleView(txt_order_type, 0f, 0f);
                            } else {
                                textColorAnimated(swt_order_type, Color.parseColor("#cccccc"));
                                scaleView(txt_order_type, 0f, 1f);
                            }
                        }
                    });

                    //Calculate money
                    Double dayMoney = totalDay * mainObj.getRentFeePerDay();
                    Double hourMoney = totalHour * mainObj.getRentFeePerHour();
                    Double minuteMoney = 0.0;
                    if (totalMinute >= 0 && totalMinute <= 7){
                        minuteMoney = 0.0;
                    } else if (totalMinute > 7 && totalMinute <= 22){
                        minuteMoney = mainObj.getRentFeePerHour() / 4;
                    } else if (totalMinute > 22 && totalMinute <= 37){
                        minuteMoney = mainObj.getRentFeePerHour() / 2;
                    } else if(totalMinute > 37 && totalMinute <= 50){
                        minuteMoney = mainObj.getRentFeePerHour() / 4 * 3;
                    } else if (totalMinute > 50 && totalMinute <= 59){
                        minuteMoney = mainObj.getRentFeePerHour();
                    }
                    Double totalMoney = dayMoney + hourMoney + minuteMoney;
                    String showDayMoney = ImmutableValue.convertPrice(nf.format(dayMoney));
                    String showHourMoney = ImmutableValue.convertPrice(nf.format(hourMoney));
                    String showMinuteMoney = ImmutableValue.convertPrice(nf.format(minuteMoney));
                    String showTotalMoney = ImmutableValue.convertPrice(nf.format(totalMoney));
                    txt_money_day_rent.setText(showDayMoney);
                    txt_money_hour_rent.setText(showHourMoney);
                    txt_money_minute_rent.setText(showMinuteMoney);
                    txt_money_total.setText(showTotalMoney);
                }
            }

            @Override
            public void onFailure(Call<MainItemModel> call, Throwable t) {
                Toast.makeText(VehicleDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });


        btnOrderRent = (Button) findViewById(R.id.btnOrderRent);
        btnOrderRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent it = new Intent(VehicleDetail.this, MainOrderOne.class);
//                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();
        super.onBackPressed();
        Intent it = new Intent(VehicleDetail.this, activity_main_2.class);
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

    public void textColorAnimated(Switch v, int colorValue){
        final ObjectAnimator animator = ObjectAnimator.ofInt(v, "textColor", colorValue);
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.start();
    }
}
