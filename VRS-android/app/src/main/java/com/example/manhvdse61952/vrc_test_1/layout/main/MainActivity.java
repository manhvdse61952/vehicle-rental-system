package com.example.manhvdse61952.vrc_test_1.layout.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.manhvdse61952.vrc_test_1.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_test_1.R;

public class MainActivity extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener{

    RelativeLayout ib_more_info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        //Use for slider
        SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        assert slidingPaneLayout != null;
        slidingPaneLayout.setPanelSlideListener(this);
        slidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);

        //
        ib_more_info = (RelativeLayout)findViewById(R.id.ib_more_info);
        ib_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, MainListSearch.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {

    }

    @Override
    public void onPanelClosed(View panel) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(it);
    }
}
