package com.example.manhvdse61952.vrc_test_1.layout.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_test_1.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_test_1.R;
import com.example.manhvdse61952.vrc_test_1.remote.ImmutableValue;

public class MainActivity extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener, SlidingPaneLayout.OnTouchListener{

    RelativeLayout ib_more_info;
    ImageView btnMore;
    TextView txtWelcome;
    String username = "";
    SlidingPaneLayout sliding_pane_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        //Use for slider
        SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        assert slidingPaneLayout != null;
        slidingPaneLayout.setPanelSlideListener(this);
        slidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);
        slidingPaneLayout.setEnabled(false);
        slidingPaneLayout.setActivated(false);
        slidingPaneLayout.setHovered(false);
        slidingPaneLayout.setClickable(false);


        //search button
        ib_more_info = (RelativeLayout)findViewById(R.id.ib_more_info);
        ib_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, MainListSearch.class);
                startActivity(it);
            }
        });

        //Login api - get username
        txtWelcome = (TextView)findViewById(R.id.txtWelcome);
        Intent receiveIt = getIntent();
        username = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
        txtWelcome.setText("Xin chào " + username);

    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {
        btnMore = (ImageView)findViewById(R.id.btnMore);
        final SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingPaneLayout.closePane();
            }
        });
    }

    @Override
    public void onPanelClosed(View panel) {
        btnMore = (ImageView)findViewById(R.id.btnMore);
        final SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingPaneLayout.openPane();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        sliding_pane_layout = (SlidingPaneLayout)findViewById(R.id.sliding_pane_layout);
        if (sliding_pane_layout.isOpen()){
            sliding_pane_layout.closePane();
        }
        return false;
    }
}
