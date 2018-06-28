package com.example.manhvdse61952.vrc_android.layout.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;

public class OrderDetailActivity extends AppCompatActivity {
    Button btn_give_car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        btn_give_car = (Button)findViewById(R.id.btn_give_car);
        btn_give_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OrderDetailActivity.this, PaymentActivity.class);
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
