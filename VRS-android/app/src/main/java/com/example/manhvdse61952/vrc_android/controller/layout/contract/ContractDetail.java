package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.api_interface.TimeAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractFinish;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractDetail extends AppCompatActivity {
    Button btn_contract_give_car, btn_remove_contract;
    TextView txt_contract_start_time, txt_contract_end_time, txt_contract_id, txt_contract_owner_name, txt_contract_vehicle_name,
            txt_contract_vehicle_year, txt_contract_vehicle_seat, txt_contract_customer_name,
            txt_contract_receive_type, txt_contract_rent_time, txt_contract_rent_fee, txt_contract_deposit_fee,
            txt_contract_total_fee, txt_contract_customer_cmnd, txt_contract_customer_phone,
            txt_contract_owner_phone, txt_secret_key, txt_clock_time, txt_clock_day,
            txt_secret_return_vehicle;
    PermissionDevice locationObj = new PermissionDevice();
    LinearLayout ln_clock;
    ProgressDialog dialog;
    int customerID = 0, rentFee = 0;
    long currentTimeInServer = 0, startLongTime = 0, endLongTime = 0;
    Date currentTime = null, currentDay = null;
    Handler handler;
    Runnable r;

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
                currentTimeInServer = startLongTime - 1000*20;
            }
        });

        txt_secret_return_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTimeInServer = endLongTime + 1000*60*60;
            }
        });

    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings_3 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings_3.edit().clear().commit();
        Intent it = new Intent(ContractDetail.this, ManageContractActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
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
        ln_clock = (LinearLayout)findViewById(R.id.ln_clock);
    }

    private void initLayout() {
        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor1 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final int userID = editor1.getInt(ImmutableValue.HOME_userID, 0);
        final String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");
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
                        if (obj.getReceiveType() == 0) {
                            txt_contract_receive_type.setText("Tự đến lấy xe");
                        } else {
                            txt_contract_receive_type.setText("Giao xe tại chỗ");
                        }
                        txt_contract_rent_time.setText(obj.getRentDay() + " ngày " + obj.getRentHour() + " tiếng");
                        txt_contract_deposit_fee.setText(GeneralController.convertPrice(obj.getDepositFee()));
                        txt_contract_total_fee.setText(GeneralController.convertPrice(obj.getTotalFee()));
                        int depositFee = Integer.parseInt(obj.getDepositFee());
                        int totalFee = Integer.parseInt(obj.getTotalFee());
                        rentFee = totalFee - depositFee;
                        txt_contract_rent_fee.setText(GeneralController.convertPrice(String.valueOf(rentFee)));
                        customerID = obj.getOwnerID();

                        //Disable delete contract button and return car button
                        if (userID == obj.getOwnerID() && (obj.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE)
                                || obj.getContractStatus().equals(ImmutableValue.CONTRACT_ACTIVE))) {
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_clock.getLayoutParams();
                            params.height = 0;
                            ln_clock.setLayoutParams(params);
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln_clock.getLayoutParams();
                            params.height = 0;
                            ln_clock.setLayoutParams(params);
                            btn_contract_give_car.setVisibility(View.INVISIBLE);
                            btn_remove_contract.setVisibility(View.INVISIBLE);
                        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING)) {
                            Intent it = new Intent(ContractDetail.this, ManageContractActivity.class);
                            startActivity(it);
                        }

                        initClock();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(ContractDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    backToPreviousScreen();
                }
            }

            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                backToPreviousScreen();
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
                                btn_contract_give_car.setBackgroundResource(R.drawable.border_green);
                                btn_contract_give_car.setEnabled(true);
                                btn_contract_give_car.setClickable(true);
                            } else if (currentTimeInServer < startLongTime){
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
                    backToPreviousScreen();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                backToPreviousScreen();
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
                                        Intent it = new Intent(ContractDetail.this, MainActivity.class);
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

    private void backToPreviousScreen() {
        Intent it = new Intent(ContractDetail.this, ManageContractActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

}
