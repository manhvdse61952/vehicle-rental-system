package com.example.manhvdse61952.vrc_android.layout.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetail extends AppCompatActivity {
    TextView txt_paypal_id, txt_paypal_money, txt_paypal_status;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_paypal_detail);


        //setContentView(R.layout);

        txt_paypal_id = (TextView)findViewById(R.id.txt_paypal_id);
        txt_paypal_money = (TextView)findViewById(R.id.txt_paypal_money);
        txt_paypal_status = (TextView)findViewById(R.id.txt_paypal_status);

        Intent it = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(it.getStringExtra("PaymentDetails"));
            showDetail(jsonObject.getJSONObject("response"), it.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetail(JSONObject jsonObject, String totalMoney){
        try {
            txt_paypal_id.setText(jsonObject.getString("id"));
            String status  =jsonObject.getString("state");
            if (status.equals("approved")){
                txt_paypal_status.setText("Thành công");
            }
            txt_paypal_money.setText(totalMoney);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Hợp đồng đã thanh toán xong! Bấm xem chi tiết hợp đồng để biết thêm chi tiết", Toast.LENGTH_SHORT).show();
    }
}
