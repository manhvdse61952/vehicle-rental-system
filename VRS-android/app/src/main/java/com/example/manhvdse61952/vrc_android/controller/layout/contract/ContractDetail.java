package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractFinish;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

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
            txt_contract_owner_phone, txt_secret_key;
    PermissionDevice locationObj = new PermissionDevice();
    long returnTime = 0;
    ProgressDialog dialog;
    int customerID;

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


        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        //Declare id
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
        txt_secret_key = (TextView) findViewById(R.id.txt_secret_key);

        btn_contract_give_car.setBackgroundResource(R.drawable.border_green_hide);
        btn_contract_give_car.setEnabled(false);
        btn_contract_give_car.setClickable(false);
        txt_secret_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_remove_contract.setVisibility(View.INVISIBLE);
                btn_contract_give_car.setBackgroundResource(R.drawable.border_green);
                btn_contract_give_car.setEnabled(true);
                btn_contract_give_car.setClickable(true);
                btn_remove_contract.setVisibility(View.INVISIBLE);
            }
        });

        initLayout();

        //Execute give car button
        btn_contract_give_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                final String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");
                Retrofit retrofit = RetrofitConfig.getClient();
                final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
                Call<ContractFinish> responseBodyCall = contractAPI.returnVehicle(contractID, returnTime);
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
                    }
                    @Override
                    public void onFailure(Call<ContractFinish> call, Throwable t) {
                        Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void initLayout() {
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
                    txt_contract_id.setText(contractID);
                    txt_contract_start_time.setText(GeneralController.convertTime(obj.getStartTime()));
                    txt_contract_end_time.setText(GeneralController.convertTime(obj.getEndTime()));
                    returnTime = obj.getEndTime();
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
                    int rentFee = totalFee - depositFee;
                    txt_contract_rent_fee.setText(GeneralController.convertPrice(String.valueOf(rentFee)));
                    customerID = obj.getOwnerID();

                    //Disable delete contract button and return car button
                    if (userID == obj.getOwnerID() && (obj.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE)
                    || obj.getContractStatus().equals(ImmutableValue.CONTRACT_ACTIVE))) {
                        btn_contract_give_car.setVisibility(View.INVISIBLE);
                        btn_remove_contract.setVisibility(View.INVISIBLE);
                    } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)) {
                        btn_contract_give_car.setVisibility(View.INVISIBLE);
                        btn_remove_contract.setVisibility(View.INVISIBLE);
                    } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING)){
                        Intent it = new Intent(ContractDetail.this, ManageContractActivity.class);
                        startActivity(it);
                    }
                } else {
                    Toast.makeText(ContractDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
