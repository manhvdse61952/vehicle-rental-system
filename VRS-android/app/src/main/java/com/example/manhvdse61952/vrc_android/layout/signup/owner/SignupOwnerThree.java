package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class SignupOwnerThree extends AppCompatActivity {

    Button btnNext;
    String imgPath_1 = "", imgPath_2 = "";
    ImageView btnTakePictureVehicle1, btnTakePictureVehicle2;
    ImageView imgShowVehicle1, imgShowVehicle2;

    //Testttttt /////////////
    private ImmutableValue cameraObj = new ImmutableValue();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_three);


        //Declare id
        btnNext = (Button) findViewById(R.id.btnVehicleNext);
        btnTakePictureVehicle1 = (ImageView) findViewById(R.id.btnTakePictureVehicle1);
        btnTakePictureVehicle2 = (ImageView) findViewById(R.id.btnTakePictureVehicle2);
//        btnTakePictureVehicle3 = (ImageView) findViewById(R.id.btnTakePictureVehicle3);
//        btnTakePictureVehicle4 = (ImageView) findViewById(R.id.btnTakePictureVehicle4);
//        btnTakePictureVehicle5 = (ImageView) findViewById(R.id.btnTakePictureVehicle5);
//        btnTakePictureVehicle6 = (ImageView) findViewById(R.id.btnTakePictureVehicle6);
        imgShowVehicle1 = (ImageView) findViewById(R.id.imgShowVehicle1);
        imgShowVehicle2 = (ImageView) findViewById(R.id.imgShowVehicle2);
//        imgShowVehicle3 = (ImageView) findViewById(R.id.imgShowVehicle3);
//        imgShowVehicle4 = (ImageView) findViewById(R.id.imgShowVehicle4);
//        imgShowVehicle5 = (ImageView) findViewById(R.id.imgShowVehicle5);
//        imgShowVehicle6 = (ImageView) findViewById(R.id.imgShowVehicle6);

        //take picture 1
        btnTakePictureVehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this, ImmutableValue.CAMERA_VEHICLE_CODE_1);
            }

        });

        //Take picture 2
        btnTakePictureVehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this,ImmutableValue.CAMERA_VEHICLE_CODE_2);
            }
        });

//        //Take picture 3
//        btnTakePictureVehicle3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this, ImmutableValue.CAMERA_VEHICLE_CODE_3);
//            }
//        });
//
//        //Take picture 4
//        btnTakePictureVehicle4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this, ImmutableValue.CAMERA_VEHICLE_CODE_4);
//            }
//        });
//
//        //Take picture 5
//        btnTakePictureVehicle5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this, ImmutableValue.CAMERA_VEHICLE_CODE_5);
//            }
//        });
//
//        //Take picture 6
//        btnTakePictureVehicle6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this, ImmutableValue.CAMERA_VEHICLE_CODE_6);
//            }
//        });

        //Button next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //save value to shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("img_vehicle_1", imgPath_1);
                editor.putString("img_vehicle_2", imgPath_2);
//                editor.putString("img_vehicle_3", imgPath_3);
//                editor.putString("img_vehicle_4", imgPath_4);
//                editor.putString("img_vehicle_5", imgPath_5);
//                editor.putString("img_vehicle_6", imgPath_6);
                editor.apply();

                //Start SignupOwnerFour activity
                Intent it = new Intent(SignupOwnerThree.this, SignupOwnerFour.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerThree.this, SignupOwnerTwoPlus.class);
        startActivity(it);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImmutableValue.CAMERA_VEHICLE_CODE_1:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgShowVehicle1, SignupOwnerThree.this);
                    imgPath_1 = ImmutableValue.picturePath;
                }
                break;
            case ImmutableValue.CAMERA_VEHICLE_CODE_2:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgShowVehicle2, SignupOwnerThree.this);
                    imgPath_2 = ImmutableValue.picturePath;
                }
                break;
//            case ImmutableValue.CAMERA_VEHICLE_CODE_3:
//                if (resultCode == RESULT_OK) {
//                    cameraObj.showImageCamera(imgShowVehicle3, SignupOwnerThree.this);
//                    imgPath_3 = ImmutableValue.picturePath;
//                }
//                break;
//            case ImmutableValue.CAMERA_VEHICLE_CODE_4:
//                if (resultCode == RESULT_OK) {
//                    cameraObj.showImageCamera(imgShowVehicle4, SignupOwnerThree.this);
//                    imgPath_4 = ImmutableValue.picturePath;
//                }
//                break;
//            case ImmutableValue.CAMERA_VEHICLE_CODE_5:
//                if (resultCode == RESULT_OK) {
//                    cameraObj.showImageCamera(imgShowVehicle5, SignupOwnerThree.this);
//                    imgPath_5 = ImmutableValue.picturePath;
//                }
//                break;
//            case ImmutableValue.CAMERA_VEHICLE_CODE_6:
//                if (resultCode == RESULT_OK) {
//                    cameraObj.showImageCamera(imgShowVehicle6, SignupOwnerThree.this);
//                    imgPath_6 = ImmutableValue.picturePath;
//                }
//                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, ImmutableValue.CAMERA_OPEN_CODE);

                }
        }

    }

}
