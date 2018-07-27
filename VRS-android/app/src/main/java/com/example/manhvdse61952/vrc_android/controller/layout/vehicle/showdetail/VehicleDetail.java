package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.DiscountAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.order.PaypalExecute;
import com.example.manhvdse61952.vrc_android.model.api_model.DiscountGeneral;
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
    ScrollView srv_main_vehicle;
    Button btnOrderRent, btn_check_discount;
    FloatingActionButton btn_back;
    DetailVehicleItem mainObj = new DetailVehicleItem();
    int selectTab = 0, rentFeePerHourID = 0, rentFeePerDayID = 0, totalHour = 0, receiveType = 0, totalDay = 0;
    Double totalMoney = 0.0, rentFeeMoney = 0.0, usdConvert = 0.0;
    int viewDiscountMoney = 0;
    float discountVehicle = 0, discountGeneral = 0;

    TextView item_price_slot, item_price_day, item_seat, item_year,
            item_plateNumber, item_ownerName, item_engine, item_tranmission, txt_hours, txt_day_start,
            txt_hours_2, txt_day_end, txt_order_type, txt_day_rent, txt_hour_rent,
            txt_money_day_rent, txt_money_hour_rent, txt_money_total, item_price_deposit,
            txt_usd_convert, txt_money_deposit, txt_pickTime, txt_vehicle_owner, item_discount
            , txt_money_discount, txt_discount_general;

    EditText edt_discount;
    CheckBox cbx1, cbx2;
    LinearLayout ln_pickTime;
    Switch swt_order_type;
    ProgressDialog dialog;
    LinearLayout ln_hide_order_type, ln_hide_calculator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);

        declareID();

        initLayout();

        btnOrderRent = (Button) findViewById(R.id.btnOrderRent);
        btnOrderRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_day_rent.getText().toString().equals("0") && txt_hour_rent.getText().toString().equals("0") && txt_usd_convert.getText().toString().equals("0")){
                    Toast.makeText(VehicleDetail.this, "Vui lòng chọn ngày giờ thuê xe", Toast.LENGTH_SHORT).show();
                    txt_pickTime.setFocusable(true);
                    txt_pickTime.setTextColor(Color.RED);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            srv_main_vehicle.post(new Runnable() {
                                @Override
                                public void run() {
                                    srv_main_vehicle.scrollTo(0, srv_main_vehicle.getBottom());
                                }
                            });
                        }
                    }, 100);
                } else {
                    txt_pickTime.setTextColor(Color.parseColor("#212121"));
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.MAIN_totalMoney, Double.toString(usdConvert));
                    rentFeeMoney = rentFeeMoney - viewDiscountMoney;
                    editor.putString(ImmutableValue.MAIN_rentFeeMoney, Double.toString(rentFeeMoney));
                    editor.putInt(ImmutableValue.MAIN_rentFeePerDayID, rentFeePerDayID);
                    editor.putInt(ImmutableValue.MAIN_rentFeePerHourID, rentFeePerHourID);
                    editor.putInt(ImmutableValue.MAIN_receiveType, receiveType);
                    editor.putFloat(ImmutableValue.MAIN_discountVehicle, discountVehicle);
                    editor.putFloat(ImmutableValue.MAIN_discountGeneral, discountGeneral);
                    editor.apply();
                    Intent it = new Intent(VehicleDetail.this, PaypalExecute.class);
                    startActivity(it);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_check_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDiscountAction();
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
        VehicleDetail.this.finish();
        super.onBackPressed();
    }

    private void checkDiscountAction(){
        dialog = ProgressDialog.show(VehicleDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        String code = edt_discount.getText().toString().trim().toUpperCase();
        if (code.equals("")){
            Toast.makeText(this, "Vui lòng điền mã ", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit test = RetrofitConfig.getClient();
            DiscountAPI discountAPI = test.create(DiscountAPI.class);
            Call<DiscountGeneral> responseBodyCall = discountAPI.checkPromotion(code);
            responseBodyCall.enqueue(new Callback<DiscountGeneral>() {
                @Override
                public void onResponse(Call<DiscountGeneral> call, Response<DiscountGeneral> response) {
                    if (response.code() == 200){
                        DiscountGeneral obj = response.body();
                        if (obj.getStatus().equals(ImmutableValue.DISCOUNT_DISABLE)){
                            Toast.makeText(VehicleDetail.this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
                        } else {
                            discountGeneral = obj.getValue();
                            Toast.makeText(VehicleDetail.this, "Áp dụng thành công", Toast.LENGTH_SHORT).show();
                            initLayout();
                        }
                    } else {
                        Toast.makeText(VehicleDetail.this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<DiscountGeneral> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(VehicleDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }

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

    private void declareID(){
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
        btn_back = (FloatingActionButton)findViewById(R.id.btn_back);
        txt_vehicle_owner = (TextView)findViewById(R.id.txt_vehicle_owner);
        ln_hide_order_type = (LinearLayout)findViewById(R.id.ln_hide_order_type);
        ln_hide_calculator = (LinearLayout)findViewById(R.id.ln_hide_calculator);
        srv_main_vehicle = (ScrollView)findViewById(R.id.srv_main_vehicle);
        item_discount = (TextView)findViewById(R.id.item_discount);
        txt_money_discount = (TextView)findViewById(R.id.txt_money_discount);
        btn_check_discount = (Button)findViewById(R.id.btn_check_discount);
        txt_discount_general = (TextView)findViewById(R.id.txt_discount_general);
        edt_discount = (EditText)findViewById(R.id.edt_discount);
    }

    private void initLayout(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString(ImmutableValue.MAIN_vehicleID, "aaaaaa");
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
                        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        int userID = editor2.getInt(ImmutableValue.HOME_userID, 0);
                        if (userID == mainObj.getOwnerID()){
                            txt_vehicle_owner.setText("Đây là xe của bạn");
                            txt_vehicle_owner.setBackgroundResource(R.drawable.border_red);
                            txt_pickTime.setText("Xem lịch thuê của xe");
                            btnOrderRent.setEnabled(false);
                            btnOrderRent.setText("");
                            btnOrderRent.setBackgroundResource(R.drawable.border_red);
                            GeneralController.scaleView(ln_hide_order_type, 0);
                            GeneralController.scaleView(ln_hide_calculator, 0);
                        }

                        // use for init layout
                        final SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                        editor.putString(ImmutableValue.MAIN_vehicleImgFront, mainObj.getImageLinkFront());
                        editor.putString(ImmutableValue.MAIN_vehicleImgBack, mainObj.getImageLinkBack());
                        editor.putString(ImmutableValue.MAIN_vehicleName, mainObj.getVehicleMaker() + " " + mainObj.getVehicleModel());
                        editor.putInt(ImmutableValue.MAIN_ownerID, mainObj.getOwnerID());
                        editor.apply();

                        sld = new ImageSlider(VehicleDetail.this);
                        vpg.setAdapter(sld);
                        if (mainObj.getVehicleType().equals(ImmutableValue.XE_MAY)) {
                            selectTab = 0;
                            item_engine.setText("Xăng");
                            if (mainObj.getScooter() == true) {
                                item_tranmission.setText("Xe tay ga");
                            } else {
                                item_tranmission.setText("Xe số");
                            }
                        } else {
                            if (mainObj.getVehicleType().equals(ImmutableValue.XE_CA_NHAN)){
                                selectTab = 1;
                            } else if (mainObj.getVehicleType().equals(ImmutableValue.XE_DU_LICH)) {
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
                        if (mainObj.getDiscountValue() != 0){
                            float discountValue = mainObj.getDiscountValue() * 100;
                            String discountConvert = nf.format(discountValue);
                            discountVehicle = mainObj.getDiscountValue();
                            item_discount.setText(discountConvert + " %");
                        } else {
                            item_discount.setText("Không");
                        }
                        if (discountGeneral != 0){
                            float discountValue = discountGeneral * 100;
                            String discountConvert = nf.format(discountValue);
                            txt_discount_general.setText(discountConvert + " %");
                        }


                        String priceHour = GeneralController.convertPrice(nf.format(mainObj.getRentFeePerHour()));
                        item_price_slot.setText(priceHour);
                        String priceDay = GeneralController.convertPrice(nf.format(mainObj.getRentFeePerDay()));
                        item_price_day.setText(priceDay);
                        String deposit = GeneralController.convertPrice(nf.format(mainObj.getDeposit()));
                        editor.putString("depositFeeConvert", deposit);
                        item_price_deposit.setText(deposit);
                        txt_money_deposit.setText(deposit);

                        item_seat.setText(mainObj.getSeat() + "");
                        item_year.setText(mainObj.getModelYear() + "");
                        editor.putInt("vehicleYear", mainObj.getModelYear());
                        item_plateNumber.setText(mainObj.getPlateNumber());
                        item_ownerName.setText(mainObj.getOwnerFullName());
                        editor.putString("ownerName", mainObj.getOwnerFullName());
                        editor.apply();

                        if (mainObj.getRequireIdCard() == true) {
                            cbx1.setChecked(true);
                            cbx1.setEnabled(true);
                        } else {
                            cbx1.setChecked(false);
                            cbx1.setEnabled(false);
                        }
                        if (mainObj.getRequireHouseHold() == true) {
                            cbx2.setEnabled(true);
                            cbx2.setChecked(true);
                        } else {
                            cbx2.setChecked(false);
                            cbx2.setEnabled(false);
                        }

                        //Start pick date time intent
                        ln_pickTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(VehicleDetail.this, CalendarCustom.class);
//                                startActivity(it);
                                startActivityForResult(it, 1);
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

                        //Calculate money (magic)
                        totalMoney = totalDay * mainObj.getRentFeePerDay() + totalHour * mainObj.getRentFeePerHour()
                                + mainObj.getDeposit();
                        float discountPercent = 1 - ((1 - discountVehicle) - ((1 - discountVehicle)*discountGeneral));
                        double discountMoney = (totalDay * mainObj.getRentFeePerDay() + totalHour * mainObj.getRentFeePerHour()) * discountPercent;
                        viewDiscountMoney = (int)discountMoney;
                        totalMoney = totalMoney - viewDiscountMoney;

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
                                        rentFeeMoney = (totalDay * mainObj.getRentFeePerDay()) + (totalHour * mainObj.getRentFeePerHour());
                                        rentFeePerDayID = mainObj.getRentFeePerDayID();
                                        rentFeePerHourID = mainObj.getRentFeePerHourID();
                                        NumberFormat nf = new DecimalFormat("#.####");
                                        String showDayMoney = GeneralController.convertPrice(nf.format(totalDay * mainObj.getRentFeePerDay()));
                                        String showHourMoney = GeneralController.convertPrice(nf.format(totalHour * mainObj.getRentFeePerHour()));
                                        String showTotalMoney = GeneralController.convertPrice(nf.format(totalMoney));
                                        String showDiscountMoney = GeneralController.convertPrice(String.valueOf(viewDiscountMoney));
                                        String rentFeeConvert = GeneralController.convertPrice(nf.format(rentFeeMoney));
                                        editor.putString("rentFeeConvert", rentFeeConvert);
                                        editor.putString("totalFeeConvert", showTotalMoney);
                                        editor.apply();
                                        txt_money_day_rent.setText(showDayMoney);
                                        txt_money_hour_rent.setText(showHourMoney);
                                        txt_money_total.setText(showTotalMoney);
                                        txt_money_discount.setText(showDiscountMoney);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                initLayout();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
