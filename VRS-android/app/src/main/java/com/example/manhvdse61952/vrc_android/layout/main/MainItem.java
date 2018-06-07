package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.order.MainOrderOne;

public class MainItem extends AppCompatActivity {
    Slider sld;
    ViewPager vpg;
    Button btnOrderRent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);

        //Use for slider
        vpg = (ViewPager)findViewById(R.id.vpg);
        sld = new Slider(this);
        vpg.setAdapter(sld);

        btnOrderRent = (Button)findViewById(R.id.btnOrderRent);
        btnOrderRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainItem.this, MainOrderOne.class);
                startActivity(it);
            }
        });
    }
}
