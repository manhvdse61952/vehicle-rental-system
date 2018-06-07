package com.example.manhvdse61952.vrc_android.layout.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_android.R;

public class MainOrderTwo extends AppCompatActivity {

    Button btnOrderStart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_two);

        btnOrderStart = (Button)findViewById(R.id.btnOrderStart);
        btnOrderStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainOrderTwo.this,  MainOrderThree.class);
                startActivity(it);
            }
        });
    }
}
