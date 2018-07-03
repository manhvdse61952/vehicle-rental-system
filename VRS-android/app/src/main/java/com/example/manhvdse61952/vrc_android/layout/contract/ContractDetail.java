package com.example.manhvdse61952.vrc_android.layout.contract;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.ContractAPI;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractDetail extends AppCompatActivity {
    Button btn_contract_give_car, btn_contract_back_to_menu;
    TextView txt_contract_start_time, txt_contract_end_time, txt_contract_id, txt_contract_owner_name, txt_contract_vehicle_name,
            txt_contract_vehicle_year, txt_contract_vehicle_seat, txt_contract_customer_name,
            txt_contract_receive_type, txt_contract_rent_time, txt_contract_rent_fee, txt_contract_deposit_fee, txt_contract_total_fee;
    ImmutableValue locationObj = new ImmutableValue();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        dialog = ProgressDialog.show(ContractDetail.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        //Declare id
        btn_contract_give_car = (Button) findViewById(R.id.btn_contract_give_car);
        btn_contract_back_to_menu = (Button) findViewById(R.id.btn_contract_back_to_menu);
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

        //Init layout
        //SharedPreferences editor1 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final String contractID = editor2.getString("contractID", "Empty");
        Log.d("Checkcontract", contractID);
        Retrofit retrofit = RetrofitConnect.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ContractItem> responseBodyCall = contractAPI.findContractByID(contractID);
        responseBodyCall.enqueue(new Callback<ContractItem>() {
            @Override
            public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                if (response.code() == 200) {
                    ContractItem obj = new ContractItem();
                    obj = response.body();
                    txt_contract_id.setText(contractID);
                    txt_contract_start_time.setText(obj.getStartTime() + "");
                    txt_contract_end_time.setText(obj.getEndTime() + "");
                    txt_contract_owner_name.setText(obj.getOwnerName());
                    txt_contract_vehicle_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
                    txt_contract_vehicle_year.setText(obj.getVehicleYear() + "");
                    txt_contract_vehicle_seat.setText(obj.getVehicleSeat() + "");
                    txt_contract_customer_name.setText(obj.getCustomerName());
                    if (obj.getReceiveType() == 0) {
                        txt_contract_receive_type.setText("Tự đến lấy xe");
                    } else {
                        txt_contract_receive_type.setText("Giao xe tại chỗ");
                    }
                    txt_contract_rent_time.setText(obj.getRentDay() + " ngày " + obj.getRentHour() + " tiếng");
                    txt_contract_deposit_fee.setText(obj.getDepositFee() + "");
                    txt_contract_total_fee.setText(obj.getTotalFee() + "");
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


//        txt_contract_start_time.setText(editor2.getString("startHour", "00") + "h" + editor2.getString("startMinute", "00") +
//        ", " + editor2.getString("startDate", "--/--/----"));
//        txt_contract_end_time.setText(editor2.getString("endHour", "00") + "h" + editor2.getString("endMinute", "00") +
//                editor2.getString("endDate", "--/--/----"));
//        txt_contract_owner_name.setText(editor2.getString("ownerName", "example owner A"));
//        txt_contract_vehicle_name.setText(editor2.getString("vehicleName", "example vehicle A"));
//        txt_contract_vehicle_year.setText(editor2.getInt("vehicleYear", 2000) + "");
//        txt_contract_vehicle_seat.setText(editor2.getString("seat", "0"));
//        txt_contract_customer_name.setText(editor1.getString("fullName", "example customer A"));
//        locationObj.checkAddressPermission(ContractDetail.this, ContractDetail.this);
//        txt_contract_customer_address.setText(locationObj.address + "");
//        if (editor2.getInt("receiveType", 0) == 0){
//            txt_contract_receive_type.setText("Tự đến lấy xe");
//        } else {
//            txt_contract_receive_type.setText("Giao xe tại chỗ");
//        }
//        txt_contract_rent_time.setText(editor2.getInt("totalDay", 0) + " ngày " + editor2.getInt("totalHour", 0) + " tiếng");
//        txt_contract_rent_fee.setText(editor2.getString("rentFeeConvert", "0"));
//        txt_contract_deposit_fee.setText(editor2.getString("depositFeeConvert", "0"));
//        txt_contract_total_fee.setText(editor2.getString("totalFeeConvert", "0"));


        btn_contract_give_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ContractDetail.this, ContractCompleted.class);
                startActivity(it);
            }
        });

        btn_contract_back_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContractDetail.this);
                builder.setMessage("Bạn có thể xem lại hợp đồng bằng cách vào menu quản lý hợp đồng").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences settings = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                settings.edit().clear().commit();
                                Intent it = new Intent(ContractDetail.this, activity_main_2.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(it);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có thể xem lại hợp đồng bằng cách vào menu quản lý hợp đồng").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        settings.edit().clear().commit();
                        Intent it = new Intent(ContractDetail.this, activity_main_2.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}
