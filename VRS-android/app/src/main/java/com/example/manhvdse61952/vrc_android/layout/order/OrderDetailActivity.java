package com.example.manhvdse61952.vrc_android.layout.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OrderDetailActivity extends AppCompatActivity {
    Button btn_contract_give_car;
    TextView txt_contract_start_time, txt_contract_end_time, txt_contract_id, txt_contract_owner_name, txt_contract_vehicle_name,
            txt_contract_vehicle_year, txt_contract_vehicle_seat, txt_contract_customer_name, txt_contract_customer_address,
            txt_contract_receive_type, txt_contract_rent_time, txt_contract_rent_fee, txt_contract_deposit_fee, txt_contract_total_fee;
    ImmutableValue locationObj = new ImmutableValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        //Declare id
        btn_contract_give_car = (Button)findViewById(R.id.btn_contract_give_car);
        txt_contract_start_time = (TextView)findViewById(R.id.txt_contract_start_time);
        txt_contract_end_time = (TextView)findViewById(R.id.txt_contract_end_time);
        txt_contract_id = (TextView)findViewById(R.id.txt_contract_id);
        txt_contract_owner_name = (TextView)findViewById(R.id.txt_contract_owner_name);
        txt_contract_vehicle_name = (TextView)findViewById(R.id.txt_contract_vehicle_name);
        txt_contract_vehicle_year = (TextView)findViewById(R.id.txt_contract_vehicle_year);
        txt_contract_vehicle_seat = (TextView)findViewById(R.id.txt_contract_vehicle_seat);
        txt_contract_customer_name = (TextView)findViewById(R.id.txt_contract_customer_name);
        txt_contract_customer_address = (TextView)findViewById(R.id.txt_contract_customer_address);
        txt_contract_receive_type = (TextView)findViewById(R.id.txt_contract_receive_type);
        txt_contract_rent_time = (TextView)findViewById(R.id.txt_contract_rent_time);
        txt_contract_rent_fee = (TextView)findViewById(R.id.txt_contract_rent_fee);
        txt_contract_deposit_fee = (TextView)findViewById(R.id.txt_contract_deposit_fee);
        txt_contract_total_fee = (TextView)findViewById(R.id.txt_contract_total_fee);

        //Init layout
        SharedPreferences editor1 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);

        txt_contract_start_time.setText(editor2.getString("startHour", "00") + "h" + editor2.getString("startMinute", "00") +
        ", " + editor2.getString("startDate", "--/--/----"));
        txt_contract_end_time.setText(editor2.getString("endHour", "00") + "h" + editor2.getString("endMinute", "00") +
                editor2.getString("endDate", "--/--/----"));
        txt_contract_owner_name.setText(editor2.getString("ownerName", "example owner A"));
        txt_contract_vehicle_name.setText(editor2.getString("vehicleName", "example vehicle A"));
        txt_contract_vehicle_year.setText(editor2.getInt("vehicleYear", 2000) + "");
        txt_contract_vehicle_seat.setText(editor2.getString("seat", "0"));
        txt_contract_customer_name.setText(editor1.getString("fullName", "example customer A"));
        locationObj.checkAddressPermission(OrderDetailActivity.this, OrderDetailActivity.this);
        txt_contract_customer_address.setText(locationObj.address + "");
        if (editor2.getInt("receiveType", 0) == 0){
            txt_contract_receive_type.setText("Tự đến lấy xe");
        } else {
            txt_contract_receive_type.setText("Giao xe tại chỗ");
        }
        txt_contract_rent_time.setText(editor2.getInt("totalDay", 0) + " ngày " + editor2.getInt("totalHour", 0) + " tiếng");
        txt_contract_rent_fee.setText(editor2.getString("rentFeeConvert", "0"));
        txt_contract_deposit_fee.setText(editor2.getString("depositFeeConvert", "0"));
        txt_contract_total_fee.setText(editor2.getString("totalFeeConvert", "0"));


        btn_contract_give_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OrderDetailActivity.this, ContractDetailActivity.class);
                startActivity(it);
            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "Hợp đồng đang có hiệu lực, không thể quay lại!", Toast.LENGTH_SHORT).show();
    }
}
