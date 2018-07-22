package com.example.manhvdse61952.vrc_android.controller.layout.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.Manifest;
import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        setUp();
    }

//    private void setUp() {
//        // check permission.
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                TedPermission.with(getApplicationContext()).setPermissionListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        // Start Splash
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                // Navigate to Login activity
//                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
//
//                                // Close this activity
//                                SplashScreen.this.finish();
//                            }
//                        }, 500);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                        Toast.makeText(SplashScreen.this, "Permission denied!", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                })
//                        .setGotoSettingButtonText("Cài đặt")
//                        .setDeniedCloseButtonText("Hủy thao tác")
//                        .setDeniedMessage("Ứng dụng cần quyền truy cập để hoạt động")
//                        .setPermissions(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)
//                        .check();
//            }
//        }, 1000);
//
//    }

    private void setUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus++;
                    android.os.SystemClock.sleep(50);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
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
                                startActivity(new Intent(SplashScreen.this, MainActivity.class));



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


}
