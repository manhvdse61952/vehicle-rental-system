package com.example.manhvdse61952.vrc_test_1.layout.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_test_1.R;

public class MainOrderThree extends AppCompatActivity {

    Button btnOrderReturnCar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_three);

        btnOrderReturnCar = (Button)findViewById(R.id.btnOrderReturnCar);
        btnOrderReturnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainOrderThree.this, MainOrderFour.class);
                startActivity(it);
            }
        });
    }
}
