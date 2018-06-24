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
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class SignupOwnerThree extends AppCompatActivity {

    Button btnNext;
    String imgPath_1 = "", imgPath_2 = "";
    ImageView btnTakePictureVehicle1, btnTakePictureVehicle2, btnSelectPictureVehicle1, btnSelectPictureVehicle2;
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

        btnSelectPictureVehicle1 = (ImageView) findViewById(R.id.btnSelectPictureVehicle1);
        btnSelectPictureVehicle2 = (ImageView) findViewById(R.id.btnSelectPictureVehicle2);

        imgShowVehicle1 = (ImageView) findViewById(R.id.imgShowVehicle1);
        imgShowVehicle2 = (ImageView) findViewById(R.id.imgShowVehicle2);


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
                cameraObj.checkPermission(SignupOwnerThree.this, SignupOwnerThree.this, ImmutableValue.CAMERA_VEHICLE_CODE_2);
            }
        });

        //Select gallery 1
        btnSelectPictureVehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1);
            }
        });

        //Select gallery 2
        btnSelectPictureVehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_2);
            }
        });


        //Button next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imgPath_1.equals("") == false && imgPath_2.equals("") == false){
                    //save value to shared preferences
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("img_vehicle_1", imgPath_1);
                    editor.putString("img_vehicle_2", imgPath_2);
                    editor.apply();

                    //Start SignupOwnerFour activity
                    Intent it = new Intent(SignupOwnerThree.this, SignupOwnerFour.class);
                    startActivity(it);

                }
                else {
                    Toast.makeText(SignupOwnerThree.this, "Vui lòng chụp đầy đủ ảnh xe", Toast.LENGTH_SHORT).show();
                }

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
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    imgPath_1 = cameraObj.showImageGallery(data, imgShowVehicle1, SignupOwnerThree.this);
                }
                break;
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_2:
                if (resultCode == RESULT_OK) {
                    imgPath_2 =cameraObj.showImageGallery(data, imgShowVehicle2, SignupOwnerThree.this);
                }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, ImmutableValue.CAMERA_OPEN_CODE);

                }
        }

    }

}
