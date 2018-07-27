package com.example.manhvdse61952.vrc_android.controller.layout.login;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.AddressAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.City;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    ImageView img_logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        img_logo = (ImageView)findViewById(R.id.img_logo);
        scaleImage(0);
        setUp();
    }


    private void setUp() {
        ImmutableValue.listGeneralAddress = new ArrayList<>();
        Retrofit test = RetrofitConfig.getClient();
        final AddressAPI testAPI = test.create(AddressAPI.class);
        Call<List<City>> responseBodyCall = testAPI.getDistrict();
        responseBodyCall.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.code() == 200){
                    ImmutableValue.listGeneralAddress = response.body();
                    progressStatus = progressStatus + 10;
                    progressBar.setProgress(progressStatus);
                    initProgressBar();
                } else {
                    Toast.makeText(SplashScreen.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void scaleImage(int value){
        ValueAnimator anim = ValueAnimator.ofInt(img_logo.getMeasuredHeight(), value);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = img_logo.getLayoutParams();
                layoutParams.height = val;
                img_logo.setLayoutParams(layoutParams);
            }
        });
        anim.start();
    }

    private void initProgressBar(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus++;
                    SystemClock.sleep(20);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    if (progressStatus == 40){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                scaleImage(100);
                            }
                        });

                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TedPermission.with(getApplicationContext()).setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                // Close this activity
                                SplashScreen.this.finish();
                                // Navigate to Login activity
                                if (ImmutableValue.listGeneralAddress.size() != 0){
                                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                } else {
                                    Toast.makeText(SplashScreen.this, "Đã xảy ra lỗi từ hệ thống! Xin vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(SplashScreen.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                                .setGotoSettingButtonText("Cài đặt")
                                .setDeniedCloseButtonText("Hủy thao tác")
                                .setDeniedMessage("Ứng dụng cần quyền truy cập để hoạt động")
                                .setPermissions(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)
                                .check();
                    }

                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
