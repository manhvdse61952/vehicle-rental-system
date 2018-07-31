package com.example.manhvdse61952.vrc_android.controller.layout.signup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.controller.validate.ValidateInput;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupUserInfoActivity extends AppCompatActivity {

    Button btnSignupAccountClean, btnNext;
    ImageView imgShowCMND;
    EditText edtSignupName, edtSignupPhone, edtSignupCNMD;
    TextInputLayout signup_name_txt, signup_phone_txt, signup_cmnd_txt;
    String name = "", phone = "", cmnd = "", picturePath = "";
    ValidateInput validObj;
    TextView txt_cmnd_error;
    RadioButton rd_user, rd_owner;
    RelativeLayout ln_image_execute;

    private PermissionDevice cameraObj = new PermissionDevice();
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

        declareID();

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

        rd_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rd_user.setChecked(true);
                    rd_owner.setChecked(false);
                }
            }
        });

        rd_owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rd_user.setChecked(false);
                    rd_owner.setChecked(true);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PermissionDevice.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    txt_cmnd_error.setVisibility(View.INVISIBLE);
                    String filename = cameraObj.showImageGallery(data, imgShowCMND, SignupUserInfoActivity.this);
                    picturePath = PermissionDevice.picturePath;
                    String getCMND = cameraObj.getCmndFromImage(filename, SignupUserInfoActivity.this);
                    if (getCMND.length() >= 7){
                        edtSignupCNMD.setText(getCMND + "");
                    } else {
                        Toast.makeText(this, "Máy không hỗ trợ tự động lấy số CMND. Hãy tự điền bằng tay", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case PermissionDevice.CAMERA_OPEN_CODE:
                if (resultCode == RESULT_OK) {
                    txt_cmnd_error.setVisibility(View.INVISIBLE);
                    String filename = cameraObj.showImageCamera(imgShowCMND, SignupUserInfoActivity.this);
                    picturePath = PermissionDevice.picturePath;
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
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putString(ImmutableValue.SIGNUP_fullName, edtSignupName.getText().toString());
        editor.putString(ImmutableValue.SIGNUP_phone, edtSignupPhone.getText().toString());
        editor.putString(ImmutableValue.SIGNUP_cmnd, edtSignupCNMD.getText().toString());
        editor.putString(ImmutableValue.SIGNUP_img_CMND, picturePath);
        editor.apply();
        SignupUserInfoActivity.this.finish();
        super.onBackPressed();
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
                    cameraObj.takePicture(SignupUserInfoActivity.this, SignupUserInfoActivity.this, PermissionDevice.CAMERA_OPEN_CODE);
                } else if (items[which].equals("Chọn ảnh từ thư viện")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, PermissionDevice.CAMERA_SELECT_IMAGE_CODE_1);
                } else if (items[which].equals("Hủy")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void revertValue(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String getName = editor.getString(ImmutableValue.SIGNUP_fullName, "");
        String getPhone = editor.getString(ImmutableValue.SIGNUP_phone, "");
        String getCmnd = editor.getString(ImmutableValue.SIGNUP_cmnd, "");
        String getImage = editor.getString(ImmutableValue.SIGNUP_img_CMND, "");
        String getRole = editor.getString(ImmutableValue.SIGNUP_role, ImmutableValue.ROLE_OWNER);
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

        if (getRole.equals(ImmutableValue.ROLE_USER)){
            rd_user.setChecked(true);
            rd_owner.setChecked(false);
        } else if (getRole.equals(ImmutableValue.ROLE_OWNER)){
            rd_user.setChecked(false);
            rd_owner.setChecked(true);
        }
    }

    private void nextAction(){
        //Get value from edittext
        name = edtSignupName.getText().toString();
        phone = edtSignupPhone.getText().toString();
        cmnd = edtSignupCNMD.getText().toString();
        String role = "";
        if (rd_user.isChecked()){
            role = ImmutableValue.ROLE_USER;
        } else {
            role = ImmutableValue.ROLE_OWNER;
        }

        validObj = new ValidateInput();
        Boolean checkName = validObj.validName(name, edtSignupName);
        Boolean checkPhone = validObj.validPhone(phone, edtSignupPhone);
        Boolean checkCMND = validObj.validCMND(cmnd, edtSignupCNMD);
        Boolean checkImage = validObj.validImageLink(PermissionDevice.picturePath, SignupUserInfoActivity.this);
        if (checkName && checkPhone && checkCMND && checkImage) {
            dialog = ProgressDialog.show(SignupUserInfoActivity.this, "Đăng ký",
                    "Đang kiểm tra ...", true);
            checkExistedCmnd(cmnd, name, phone,
                    picturePath, edtSignupCNMD, dialog, role);
        }
    }

    public void checkExistedCmnd(final String cmnd, final String name, final String phone,
                                 final String imagePath, final EditText input,
                                 final ProgressDialog progressDialog, final String role) {
        Retrofit test = RetrofitConfig.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkCmnd(cmnd);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200){
                    if (response.body().toString().equals("true")) {
                        input.setError("CMND đã có người sử dụng");
                        input.requestFocus();
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                        editor.putString(ImmutableValue.SIGNUP_fullName, name);
                        editor.putString(ImmutableValue.SIGNUP_phone, phone);
                        editor.putString(ImmutableValue.SIGNUP_cmnd, cmnd);
                        editor.putString(ImmutableValue.SIGNUP_img_CMND, imagePath);
                        editor.putString(ImmutableValue.SIGNUP_role, role);
                        editor.apply();

                        Intent it = new Intent(SignupUserInfoActivity.this, SignupPolicyActivity.class);
                        startActivity(it);
                    }
                } else {
                    Toast.makeText(SignupUserInfoActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignupUserInfoActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void declareID(){
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
        rd_user = (RadioButton)findViewById(R.id.rd_user);
        rd_owner = (RadioButton)findViewById(R.id.rd_owner);
    }
}
