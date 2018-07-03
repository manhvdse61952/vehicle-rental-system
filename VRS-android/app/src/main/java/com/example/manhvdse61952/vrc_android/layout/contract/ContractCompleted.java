package com.example.manhvdse61952.vrc_android.layout.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class ContractDetailActivity extends AppCompatActivity {

    Button btn_pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        btn_pay = (Button)findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                settings.edit().clear().commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(ContractDetailActivity.this);
                builder.setMessage("Hợp đồng hoàn tất! Bạn có thể xem lại lịch sử hợp đồng ở menu. Cảm ơn bạn đã sử dụng dịch vụ của VRS");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(ContractDetailActivity.this, activity_main_2.class);
                        startActivity(it);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "Bạn cần phải thanh toán hợp đồng", Toast.LENGTH_SHORT).show();
    }
}
