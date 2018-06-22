package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.Validate;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;

public class SignupOwnerTwoPlus extends AppCompatActivity {

    private ImmutableValue cameraObj = new ImmutableValue();
    EditText edtVehicleFrameNumber, edtVehiclePlate;
    Button btnVehicleNext, btnVehicleBack;
    CheckBox cbxHouseHold, cbxIdCard;
    ImageView btnTakePictureVehicle, btnSelectPictureVehicle, imgSignupOwner;
    Validate validObj;

    int required_household_registration = 0, required_id_card = 0;
    String frameNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_two_plus);

        //Decalre id
        edtVehicleFrameNumber = (EditText)findViewById(R.id.edtVehicleFrameNumber);
        edtVehiclePlate = (EditText)findViewById(R.id.edtVehiclePlate);
        btnVehicleNext = (Button)findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button)findViewById(R.id.btnVehicleBack);
        cbxHouseHold = (CheckBox)findViewById(R.id.cbxHouseHold);
        cbxIdCard = (CheckBox)findViewById(R.id.cbxIdCard);
        btnTakePictureVehicle = (ImageView)findViewById(R.id.btnTakePictureVehicle);
        btnSelectPictureVehicle = (ImageView)findViewById(R.id.btnSelectPictureVehicle);
        imgSignupOwner = (ImageView)findViewById(R.id.imgSignupOwner);

        //house_hold checkbox
        cbxHouseHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    required_household_registration = 1;
                }
                else {
                    required_household_registration = 0;
                }
            }
        });

        //id_card_registration
        cbxIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    required_id_card = 1;
                }
                else {
                    required_id_card = 0;
                }
            }
        });

        //Button next
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameNumber = edtVehicleFrameNumber.getText().toString();
                validObj = new Validate();
                Boolean checkFrameNumber = validObj.validFrameNumber(frameNumber, edtVehicleFrameNumber);
                Boolean checkPlate = validObj.validFrameNumber(edtVehiclePlate.getText().toString(), edtVehiclePlate);

                Boolean checkImageLink = validObj.validImageLink(ImmutableValue.picturePath, SignupOwnerTwoPlus.this);

                if (checkFrameNumber && checkImageLink && checkPlate){
                    //Declare shared preferences
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("frameNumber", frameNumber);
                    editor.putInt("requireHouseHold", required_household_registration);
                    editor.putInt("requireIdCard", required_id_card);
                    editor.putString("plateNumber", edtVehiclePlate.getText().toString());
                    editor.putString("picture_path", ImmutableValue.picturePath);
                    editor.apply();

                    //Start signupOwnerThree activity
                    Intent it = new Intent(SignupOwnerTwoPlus.this, SignupOwnerThree.class);
                    startActivity(it);
                }

            }
        });

        //Button Back
        btnVehicleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerTwoPlus.this, SignupOwnerTwo.class);
                startActivity(it);
            }
        });

        //Button take picture
        btnTakePictureVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraObj.checkPermission(SignupOwnerTwoPlus.this, SignupOwnerTwoPlus.this, ImmutableValue.CAMERA_OPEN_CODE);
            }
        });

        //button select picture from gallery
        btnSelectPictureVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageGallery(data, imgSignupOwner, SignupOwnerTwoPlus.this);
                }
                break;

            case ImmutableValue.CAMERA_OPEN_CODE:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgSignupOwner, SignupOwnerTwoPlus.this);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraObj.takePicture(SignupOwnerTwoPlus.this, SignupOwnerTwoPlus.this, ImmutableValue.CAMERA_OPEN_CODE );
                } else {
                    Toast.makeText(this, "Permission not granted !", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerTwoPlus.this, SignupOwnerTwo.class);
        startActivity(it);
    }

}
