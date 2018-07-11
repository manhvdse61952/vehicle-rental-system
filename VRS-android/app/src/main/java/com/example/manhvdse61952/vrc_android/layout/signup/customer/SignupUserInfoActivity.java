package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.model.apiModel.City;
import com.example.manhvdse61952.vrc_android.model.apiModel.District;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.Validate;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SignupUserInfoActivity extends AppCompatActivity {

    Button btnSignupAccountClean, btnNext;
    ImageView imgShowCMND;
    EditText edtSignupName, edtSignupPhone, edtSignupCNMD;
    TextInputLayout signup_name_txt, signup_phone_txt, signup_cmnd_txt;
    String name = "", phone = "", cmnd = "", picturePath = "";
    Validate validObj;
    TextView txt_cmnd_error;
    RelativeLayout ln_image_execute;

    private ImmutableValue cameraObj = new ImmutableValue();
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_userinfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        btnSignupAccountClean = (Button) findViewById(R.id.btnSignupAccountClean);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
        imgShowCMND = (ImageView) findViewById(R.id.imgSignupCMND);
        edtSignupCNMD = (EditText) findViewById(R.id.edtSignupCMND);
        signup_name_txt = (TextInputLayout) findViewById(R.id.signup_name_txt);
        signup_phone_txt = (TextInputLayout) findViewById(R.id.signup_phone_txt);
        signup_cmnd_txt = (TextInputLayout) findViewById(R.id.signup_cmnd_txt);
        txt_cmnd_error = (TextView)findViewById(R.id.txt_cmnd_error);
        ln_image_execute = (RelativeLayout)findViewById(R.id.ln_image_execute);
        edtSignupName = (EditText) findViewById(R.id.edtSignupName);
        edtSignupPhone = (EditText) findViewById(R.id.edtSignupPhone);

        revertValue();

        //button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextAction();
            }
        });

        //button Back
        btnSignupAccountClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSignupName.setText("");
                edtSignupPhone.setText("");
                edtSignupCNMD.setText("");
            }
        });

        ln_image_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeImage();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    picturePath = ImmutableValue.picturePath;
                    txt_cmnd_error.setVisibility(View.INVISIBLE);
                    String filename = cameraObj.showImageGallery(data, imgShowCMND, SignupUserInfoActivity.this);
                    String getCMND = cameraObj.getCmndFromImage(filename, SignupUserInfoActivity.this);
                    if (getCMND.length() >= 7){
                        edtSignupCNMD.setText(getCMND + "");
                    } else {
                        Toast.makeText(this, "Máy không hỗ trợ tự động lấy số CMND. Hãy tự điền bằng tay", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case ImmutableValue.CAMERA_OPEN_CODE:
                if (resultCode == RESULT_OK) {
                    picturePath = ImmutableValue.picturePath;
                    txt_cmnd_error.setVisibility(View.INVISIBLE);
                    String filename = cameraObj.showImageCamera(imgShowCMND, SignupUserInfoActivity.this);
                    String getCMND = cameraObj.getCmndFromImage(filename, SignupUserInfoActivity.this);
                    if (getCMND.length() >= 7){
                        edtSignupCNMD.setText(getCMND + "");
                    } else {
                        Toast.makeText(this, "Máy không hỗ trợ tự động lấy số CMND. Hãy tự điền bằng tay", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }


    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putString("name", edtSignupName.getText().toString());
        editor.putString("phone", edtSignupPhone.getText().toString());
        editor.putString("cmnd", edtSignupCNMD.getText().toString());
        editor.putString("CMND_image_path", picturePath);
        editor.apply();
        Intent it = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
        startActivity(it);
    }

    //Use for accept camera in phone when permission granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraObj.takePicture(SignupUserInfoActivity.this, SignupUserInfoActivity.this, ImmutableValue.CAMERA_OPEN_CODE);
                } else {
                    Toast.makeText(this, "Permission not granted !", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void executeImage(){
        final CharSequence[] items = { "Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupUserInfoActivity.this);
        builder.setTitle("Hãy chọn các hành động bên dưới");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Chụp ảnh")){
                    cameraObj.checkPermission(SignupUserInfoActivity.this, SignupUserInfoActivity.this, ImmutableValue.CAMERA_OPEN_CODE);
                } else if (items[which].equals("Chọn ảnh từ thư viện")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1);
                } else if (items[which].equals("Hủy")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void revertValue(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String getName = editor.getString("name", "");
        String getPhone = editor.getString("phone", "");
        String getCmnd = editor.getString("cmnd", "");
        String getImage = editor.getString("CMND_image_path", "");
        if (!getName.trim().equals("")){
            edtSignupName.setText(getName);
        }
        if (!getPhone.trim().equals("")){
            edtSignupPhone.setText(getPhone);
        }
        if (!getCmnd.trim().equals("")){
            edtSignupCNMD.setText(getCmnd);
        }
        if (!getImage.equals("")){
            picturePath = getImage;
            File imgFile = new File(picturePath);
            Picasso.get().load(imgFile).into(imgShowCMND);
            txt_cmnd_error.setVisibility(View.INVISIBLE);
        }
    }

    private void nextAction(){
        //Get value from edittext
        name = edtSignupName.getText().toString();
        phone = edtSignupPhone.getText().toString();
        cmnd = edtSignupCNMD.getText().toString();

        validObj = new Validate();
        Boolean checkName = validObj.validName(name, edtSignupName);
        Boolean checkPhone = validObj.validPhone(phone, edtSignupPhone);
        Boolean checkCMND = validObj.validCMND(cmnd, edtSignupCNMD);
        Boolean checkImage = validObj.validImageLink(ImmutableValue.picturePath, SignupUserInfoActivity.this);
        if (checkName && checkPhone && checkCMND && checkImage) {
            dialog = ProgressDialog.show(SignupUserInfoActivity.this, "Đăng ký",
                    "Đang kiểm tra ...", true);
            RetrofitCallAPI testApi = new RetrofitCallAPI();
            testApi.checkExistedCmnd(cmnd, name, phone,
                    picturePath, SignupUserInfoActivity.this, edtSignupCNMD, dialog);
        }
    }
}
