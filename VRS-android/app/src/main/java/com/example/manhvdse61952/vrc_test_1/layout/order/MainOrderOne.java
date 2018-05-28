package com.example.manhvdse61952.vrc_test_1.layout.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_test_1.R;

public class MainOrderOne extends AppCompatActivity{

    Button btnSignupOwner1, btnSignupOwner2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_one);

        btnSignupOwner1 = (Button)findViewById(R.id.btnSignupOwner1);
        btnSignupOwner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainOrderOne.this, MainOrderTwo.class);
                startActivity(it);
            }
        });
    }
}
