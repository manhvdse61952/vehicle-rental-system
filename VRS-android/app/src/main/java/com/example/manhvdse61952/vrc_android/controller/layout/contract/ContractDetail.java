package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.tracking.TrackingVehicle;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.api_interface.TimeAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractFinish;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.model.api_model.Tracking;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractDetail extends AppCompatActivity {
    Button btn_contract_give_car, btn_remove_contract, btn_send_feedback, btn_contract_view_map, btn_confirm;
    TextView txt_contract_start_time, txt_contract_end_time, txt_contract_id, txt_contract_owner_name, txt_contract_vehicle_name,
            txt_contract_vehicle_year, txt_contract_vehicle_seat, txt_contract_customer_name,
            txt_contract_receive_type, txt_contract_rent_time, txt_contract_rent_fee, txt_contract_deposit_fee,
            txt_contract_total_fee, txt_contract_customer_cmnd, txt_contract_customer_phone,
            txt_contract_owner_phone, txt_secret_key, txt_clock_time, txt_clock_day,
            txt_secret_return_vehicle, txt_contract_complete_endRealTime, txt_contract_complete_overTime,
            txt_contract_complete_overPrice, txt_contract_insideFee, txt_contract_outsideFee, txt_detail_discount_vehicle, txt_detail_discount_general, txt_customer_location;
    LinearLayout ln_clock, ln_show_fee, ln_feedback;
    ProgressDialog dialog;
    RatingBar rt_feedback;
    EditText edt_feedback;
    int customerID = 0, contractIdGeneral = 0;
    long rentFee = 0;
    long currentTimeInServer = 0, startLongTime = 0, endLongTime = 0;
    Date currentTime = null, currentDay = null;
    Handler handler;
    Runnable r;
    double vehicleLat = 0, vehicleLng = 0, deliveryLat = 0, deliveryLng = 0;
    String vehicleFrameNumber  ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();

        //Execute give car button
        btn_contract_give_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromFireBase(vehicleFrameNumber);
                Location vehicleLocation = new Location("");
                Location deliveryLocation = new Location("");
                vehicleLocation.setLatitude(vehicleLat);
                vehicleLocation.setLongitude(vehicleLng);
                deliveryLocation.setLatitude(deliveryLat);
                deliveryLocation.setLongitude(deliveryLng);
                if (vehicleLocation.distanceTo(deliveryLocation) > 50){
                    Toast.makeText(ContractDetail.this, "Bạn cần lái xe đến vị trí nhận xe ban đầu trong bán kính 50m", Toast.LENGTH_SHORT).show();
                } else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContractDetail.this);
                    builder.setMessage("Xác nhận trả xe ?").setCancelable(false)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    giveCarAction();
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
            }
        });

        btn_remove_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String removeFeeConvert = GeneralController.convertPrice(String.valueOf(rentFee / 100 * 5));
                AlertDialog.Builder builder = new AlertDialog.Builder(ContractDetail.this);
                builder.setMessage("Nếu hủy hợp đồng bạn sẽ chịu 5% phí thuê xe, cụ thể là mất " + removeFeeConvert).setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeContractAction();
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
        });

        txt_secret_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTimeInServer = startLongTime - 1000 * 20;
            }
        });

        txt_secret_return_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTimeInServer = endLongTime + 1000 * 60 * 60;
                vehicleLat = deliveryLat;
                vehicleLng = deliveryLng;
            }
        });

    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings_3 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings_3.edit().clear().commit();
        ContractDetail.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void declareID() {
        btn_contract_give_car = (Button) findViewById(R.id.btn_contract_give_car);
        txt_contract_start_time = (TextView) findViewById(R.id.txt_contract_start_time);
        txt_contract_end_time = (TextView) findViewById(R.id.txt_contract_end_time);
        txt_contract_id = (TextView) findViewById(R.id.txt_contract_id);
        txt_contract_owner_name = (TextView) findViewById(R.id.txt_contract_owner_name);
        txt_contract_vehicle_name = (TextView) findViewById(R.id.txt_contract_vehicle_name);
        txt_contract_vehicle_year = (TextView) findViewById(R.id.txt_contract_vehicle_year);
        txt_contract_vehicle_seat = (TextView) findViewById(R.id.txt_contract_vehicle_seat);
        txt_contract_customer_name = (TextView) findViewById(R.id.txt_contract_customer_name);
        txt_contract_receive_type = (TextView) findViewById(R.id.txt_contract_receive_type);
        txt_contract_rent_time = (TextView) findViewById(R.id.txt_contract_rent_time);
        txt_contract_rent_fee = (TextView) findViewById(R.id.txt_contract_rent_fee);
        txt_contract_deposit_fee = (TextView) findViewById(R.id.txt_contract_deposit_fee);
        txt_contract_total_fee = (TextView) findViewById(R.id.txt_contract_total_fee);
        txt_contract_customer_cmnd = (TextView) findViewById(R.id.txt_contract_customer_cmnd);
        txt_contract_customer_phone = (TextView) findViewById(R.id.txt_contract_customer_phone);
        txt_contract_owner_phone = (TextView) findViewById(R.id.txt_contract_owner_phone);
        btn_remove_contract = (Button) findViewById(R.id.btn_remove_contract);
        txt_secret_key = (TextView) findViewById(R.id.txt_secret_start_contract);
        txt_clock_time = (TextView) findViewById(R.id.txt_clock_time);
        txt_clock_day = (TextView) findViewById(R.id.txt_clock_day);
        txt_secret_return_vehicle = (TextView) findViewById(R.id.txt_secret_return_vehicle);
        ln_clock = (LinearLayout) findViewById(R.id.ln_clock);
        txt_contract_complete_endRealTime = (TextView) findViewById(R.id.txt_contract_complete_endRealTime);
        txt_contract_complete_overTime = (TextView) findViewById(R.id.txt_contract_complete_overTime);
        txt_contract_complete_overPrice = (TextView) findViewById(R.id.txt_contract_complete_overPrice);
        txt_contract_insideFee = (TextView) findViewById(R.id.txt_contract_insideFee);
        txt_contract_outsideFee = (TextView) findViewById(R.id.txt_contract_outsideFee);
        ln_show_fee = (LinearLayout) findViewById(R.id.ln_show_fee);
        txt_detail_discount_vehicle = (TextView) findViewById(R.id.txt_detail_discount_vehicle);
        txt_detail_discount_general = (TextView) findViewById(R.id.txt_detail_discount_general);
        ln_feedback = (LinearLayout) findViewById(R.id.ln_feedback);
        edt_feedback = (EditText) findViewById(R.id.edt_feedback);
        rt_feedback = (RatingBar) findViewById(R.id.rt_feedback);
        btn_send_feedback = (Button) findViewById(R.id.btn_send_feedback);
        txt_customer_location = (TextView) findViewById(R.id.txt_customer_location);
        btn_contract_view_map = (Button) findViewById(R.id.btn_contract_view_map);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
    }

    private void initLayout() {
        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor1 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final int userID = editor1.getInt(ImmutableValue.HOME_userID, 0);
        final String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");
        contractIdGeneral = Integer.parseInt(contractID);
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ContractItem> responseBodyCall = contractAPI.findContractByID(contractID);
        responseBodyCall.enqueue(new Callback<ContractItem>() {
            @Override
            public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                if (response.code() == 200) {
                    ContractItem obj = new ContractItem();
                    obj = response.body();
                    if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)) {
                        dialog.dismiss();
                        Intent it = new Intent(ContractDetail.this, ManageContractActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
                    } else {
                        txt_contract_id.setText(contractID);
                        txt_contract_start_time.setText(GeneralController.convertTime(obj.getStartTime()));
                        txt_contract_end_time.setText(GeneralController.convertTime(obj.getEndTime()));
                        startLongTime = obj.getStartTime();
                        endLongTime = obj.getEndTime();
                        txt_contract_owner_name.setText(obj.getOwnerName());
                        txt_contract_owner_phone.setText(obj.getOwnerPhone());
                        txt_contract_vehicle_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
                        txt_contract_vehicle_year.setText(obj.getVehicleYear() + "");
                        txt_contract_vehicle_seat.setText(obj.getVehicleSeat() + "");
                        txt_contract_customer_name.setText(obj.getCustomerName());
                        txt_contract_customer_cmnd.setText(obj.getCustomerCMND());
                        txt_contract_customer_phone.setText(obj.getCustomerPhone());
                        deliveryLat = obj.getDeliveryLat();
                        deliveryLng = obj.getDeliveryLng();
                        vehicleFrameNumber = obj.getVehicleFrameNumber();
                        if (obj.getFeedbackContent() == null) {
                            edt_feedback.setText("");
                        } else {
                            edt_feedback.setText(obj.getFeedbackContent() + "");
                        }
                        rt_feedback.setRating((float) obj.getFeedbackStar());

                        NumberFormat nf = new DecimalFormat("#.####");
                        if (obj.getDiscountVehicle() != 0) {
                            float discountVehicleValue = obj.getDiscountVehicle() * 100;
                            txt_detail_discount_vehicle.setText(nf.format(discountVehicleValue) + " %");
                        }
                        if (obj.getDiscountGeneral() != 0) {
                            float discountGeneralValue = obj.getDiscountGeneral() * 100;
                            txt_detail_discount_general.setText(nf.format(discountGeneralValue) + " %");
                        }

                        double deliveryLat = 0, deliveryLng = 0;
                        if (obj.getDeliveryLat() == 0 && obj.getDeliveryLng() == 0) {
                            deliveryLat = obj.getLatitude();
                            deliveryLng = obj.getLongitude();
                        } else {
                            deliveryLat = obj.getDeliveryLat();
                            deliveryLng = obj.getDeliveryLng();
                        }
                        String deliveryAddress = PermissionDevice.getStringAddress(deliveryLng, deliveryLat, ContractDetail.this);
                        txt_customer_location.setText(deliveryAddress + "");

                        if (obj.getDeliveryType().equals(ImmutableValue.DELIVERY_CUSTOMER_PICK_UP)) {
                            txt_contract_receive_type.setText("Tự đến lấy xe");
                        } else {
                            txt_contract_receive_type.setText("Giao xe tại chỗ");
                        }
                        txt_contract_rent_time.setText(obj.getRentDay() + " ngày " + obj.getRentHour() + " tiếng");
                        txt_contract_complete_endRealTime.setText(GeneralController.convertTime(obj.getEndRealTime()));
                        //Calculate overtime
                        if (obj.getEndRealTime() > obj.getEndTime()) {
                            long diff = obj.getEndRealTime() - obj.getEndTime();
                            long diffDate = TimeUnit.MILLISECONDS.toDays(diff);
                            long diffHour = diff / (60 * 60 * 1000) % 24;
                            long diffMinute = diff / (60 * 1000) % 60;
                            if (diffMinute > 30) {
                                diffHour = diffHour + 1;
                            }
                            long overTimeFee = (diffHour * obj.getRentFeePerHour()) + (diffDate * obj.getRentFeePerDay());
                            txt_contract_complete_overPrice.setText(overTimeFee + "");
                            txt_contract_complete_overTime.setText(diffDate + " ngày " + diffHour + " giờ");
                        } else {
                            txt_contract_complete_overPrice.setText("0");
                        }

                        double depositFeeFromDB = Double.parseDouble(obj.getDepositFee());
                        double totalFeeFromDB = Double.parseDouble(obj.getTotalFee());
                        String depositFeeString = nf.format(depositFeeFromDB);
                        String totalFeeString = nf.format(totalFeeFromDB);
                        txt_contract_deposit_fee.setText(GeneralController.convertPrice(depositFeeString));

                        long depositFee = Long.parseLong(depositFeeString);
                        long totalFee = Long.parseLong(totalFeeString);
                        rentFee = totalFee - depositFee;
                        long overTimeFee = Long.parseLong(obj.getPenaltyOverTime());
                        long insideFee = Long.parseLong(obj.getInsideFee());
                        long outsideFee = Long.parseLong(obj.getOutsideFee());
                        totalFee = totalFee + overTimeFee + insideFee + outsideFee;
                        txt_contract_rent_fee.setText(GeneralController.convertPrice(String.valueOf(rentFee)));
                        txt_contract_complete_overPrice.setText(GeneralController.convertPrice(String.valueOf(overTimeFee)));
                        txt_contract_outsideFee.setText(GeneralController.convertPrice(String.valueOf(outsideFee)));
                        txt_contract_insideFee.setText(GeneralController.convertPrice(String.valueOf(insideFee)));
                        customerID = obj.getOwnerID();
                        txt_contract_total_fee.setText(GeneralController.convertPrice(String.valueOf(totalFee)));
                        //Disable delete contract button and return car button
                        if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE) && userID == obj.getCustomerID()) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_show_fee.getLayoutParams();
                            params.height = 0;
                            ln_show_fee.setLayoutParams(params);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln_feedback.getLayoutParams();
                            params2.height = 0;
                            ln_feedback.setLayoutParams(params2);
                            btn_confirm.setVisibility(View.VISIBLE);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE) && userID == obj.getOwnerID()) {
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_show_fee.getLayoutParams();
                            params.height = 0;
                            ln_show_fee.setLayoutParams(params);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln_feedback.getLayoutParams();
                            params2.height = 0;
                            btn_contract_view_map.setVisibility(View.VISIBLE);
                            ln_feedback.setLayoutParams(params2);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ACTIVE) && userID == obj.getCustomerID()) {
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_show_fee.getLayoutParams();
                            params.height = 0;
                            ln_show_fee.setLayoutParams(params);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln_feedback.getLayoutParams();
                            params2.height = 0;
                            ln_feedback.setLayoutParams(params2);
                            btn_contract_view_map.setVisibility(View.VISIBLE);
                            btn_confirm.setVisibility(View.VISIBLE);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ACTIVE) && userID == obj.getOwnerID()) {
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_show_fee.getLayoutParams();
                            params.height = 0;
                            ln_show_fee.setLayoutParams(params);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln_feedback.getLayoutParams();
                            params2.height = 0;
                            ln_feedback.setLayoutParams(params2);
                            btn_contract_view_map.setVisibility(View.VISIBLE);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_clock.getLayoutParams();
                            params.height = 0;
                            ln_clock.setLayoutParams(params);
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                            if (obj.getOwnerID() == userID) {
                                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln_feedback.getLayoutParams();
                                params2.height = 0;
                                ln_feedback.setLayoutParams(params2);
                            }

                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_clock.getLayoutParams();
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln_show_fee.getLayoutParams();
                            params.height = 0;
                            params2.height = 0;
                            ln_clock.setLayoutParams(params);
                            ln_show_fee.setLayoutParams(params2);
                            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) ln_feedback.getLayoutParams();
                            params3.height = 0;
                            ln_feedback.setLayoutParams(params3);
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                        }
                        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendFeedback();
                            }
                        });
                        final ContractItem finalObj = obj;
                        btn_contract_view_map.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                                editor.putString(ImmutableValue.MAIN_isTracking, "view_contract_map");
                                editor.putString(ImmutableValue.MAIN_vehicleID, finalObj.getVehicleFrameNumber());
                                editor.putString(ImmutableValue.MAIN_customer_Lat, String.valueOf(finalObj.getDeliveryLat()));
                                editor.putString(ImmutableValue.MAIN_customer_Lng, String.valueOf(finalObj.getDeliveryLng()));
                                editor.apply();
                                startActivity(new Intent(ContractDetail.this, TrackingVehicle.class));
                            }
                        });
                        if (obj.getStartRealTime() != 0){
                            btn_confirm.setVisibility(View.INVISIBLE);
                        }
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ContractDetail.this);
                                builder.setMessage("Bạn đã nhận được xe chưa ?").setCancelable(false)
                                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                checkConfirm();
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
                        });


                        initClock();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(ContractDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void initClock() {
        Retrofit retrofit = RetrofitConfig.getClient();
        TimeAPI timeAPI = retrofit.create(TimeAPI.class);
        Call<Long> responseBodyCall = timeAPI.getServerTime();
        responseBodyCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, final Response<Long> response) {
                if (response.code() == 200) {
                    final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                    final SimpleDateFormat sdfDay = new SimpleDateFormat("dd/MM/yyyy");
                    currentTimeInServer = response.body();
                    r = new Runnable() {
                        @Override
                        public void run() {
                            if (currentTimeInServer >= startLongTime) {
                                btn_remove_contract.setVisibility(View.INVISIBLE);
                            }
                            if (currentTimeInServer >= (endLongTime - 1000 * 60 * 15)) {
                                btn_remove_contract.setVisibility(View.INVISIBLE);
                                btn_contract_give_car.setBackgroundResource(R.drawable.border_green_primarygreen);
                                btn_contract_give_car.setEnabled(true);
                                btn_contract_give_car.setClickable(true);
                            } else if (currentTimeInServer < (endLongTime - 1000 * 60 * 15)) {
                                btn_contract_give_car.setBackgroundResource(R.drawable.border_green_hide);
                                btn_contract_give_car.setEnabled(false);
                                btn_contract_give_car.setClickable(false);
                            }


                            currentTimeInServer = currentTimeInServer + 1000;
                            currentTime = new Date(currentTimeInServer);
                            currentDay = new Date(currentTimeInServer);

                            String timeParse = sdfTime.format(currentTime);
                            txt_clock_time.setText(timeParse);
                            String dayParse = sdfDay.format(currentDay);
                            txt_clock_day.setText(dayParse);
                            handler.postDelayed(r, 1000);
                        }
                    };

                    handler = new Handler();
                    handler.postDelayed(r, 1000);
                } else {
                    Toast.makeText(ContractDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void giveCarAction() {


        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ContractFinish> responseBodyCall = contractAPI.returnVehicle(contractID, currentTimeInServer);
        responseBodyCall.enqueue(new Callback<ContractFinish>() {
            @Override
            public void onResponse(Call<ContractFinish> call, Response<ContractFinish> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContractDetail.this);
                        builder.setMessage("Trả xe thành công! Bạn hãy đợi chủ xe xác nhận").setCancelable(false)
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ContractDetail.this.finish();
                                        Intent it = new Intent(ContractDetail.this, MainActivity.class);
                                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(it);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                } else {
                    Toast.makeText(ContractDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContractFinish> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeContractAction() {
        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.removeContract(Integer.parseInt(contractID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(ContractDetail.this, "Hủy thành công", Toast.LENGTH_SHORT).show();
                    ContractDetail.this.finish();
                    startActivity(new Intent(ContractDetail.this, MainActivity.class));
                } else {
                    Toast.makeText(ContractDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFeedback() {
        final ProgressDialog dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        String content = edt_feedback.getText().toString();
        Date currentTime = new Date();
        long currentTimeLong = currentTime.getTime();
        float getRating = rt_feedback.getRating();
        int rating = (int) getRating;

        Retrofit retrofit = RetrofitConfig.getClient();
        ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.feedbackContract(contractIdGeneral, content, currentTimeLong, rating);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ContractDetail.this, "Đánh giá gửi thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLocationFromFireBase(String vehicleFrameNumber) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Locations").child(vehicleFrameNumber);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tracking obj = dataSnapshot.getValue(Tracking.class);
                vehicleLat = obj.getLatitude();
                vehicleLng = obj.getLongitude();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkConfirm(){
        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.checkConfirm(contractIdGeneral, startLongTime);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Toast.makeText(ContractDetail.this, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                    ContractDetail.this.finish();
                    startActivity(getIntent());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
