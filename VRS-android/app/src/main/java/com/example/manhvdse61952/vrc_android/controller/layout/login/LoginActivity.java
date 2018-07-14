package com.example.manhvdse61952.vrc_android.controller.layout.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ManageContractActivity;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractDetail;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.signup.customer.SignupAccountActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail.VehicleDetail;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralAPI;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.controller.validate.ValidateInput;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Account;
import com.example.manhvdse61952.vrc_android.model.api_model.Login;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private final String TOPIC = "JavaSampleApproach";
    TextView txtSignUp;
    Button btnLogin;

    String username = "";
    String password = "";
    ProgressDialog dialog;
    ValidateInput validObj;

    TextInputLayout username_txt, password_txt;
    EditText edtUsername, edtPassword;

    PermissionDevice locationObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Declare id
        username_txt = (TextInputLayout) findViewById(R.id.username_txt);
        password_txt = (TextInputLayout) findViewById(R.id.password_txt);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);

        //Check location of user
        locationObj = new PermissionDevice();
        locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);

        revertLayout();

        //Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAction();
            }
        });

        //Sign up link
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupAction();
            }
        });

        //FCM
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionDevice.REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationObj = new PermissionDevice();
                    locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);
                } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void revertLayout(){
        // get address data
        if (GeneralAPI.listAddressFromDB.size() == 0) {
            dialog = ProgressDialog.show(LoginActivity.this, "Đang xử lý",
                    "Vui lòng đợi ...", true);
            GeneralAPI testAPI = new GeneralAPI();
            testAPI.getAllAddress(dialog, LoginActivity.this);
        }

        //Revert layout
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int usernameID = editor.getInt(ImmutableValue.HOME_userID, 0);
        String vehicleID = editor2.getString(ImmutableValue.MAIN_vehicleID, "Empty");
        String contractStatus = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");
//        if (usernameID != 0 && !contractStatus.equals("Empty")){
//            Intent it = new Intent(LoginActivity.this, ContractDetail.class);
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(it);
//        } else
        if (usernameID != 0 && !vehicleID.equals("Empty")) {
            Intent it = new Intent(LoginActivity.this, VehicleDetail.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        } else if (usernameID != 0 && vehicleID.equals("Empty")) {
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }
    }

    private void loginAction(){
        //Check address if empty
        if (GeneralAPI.listAddressFromDB.size() == 0) {
            dialog = ProgressDialog.show(LoginActivity.this, "Đang xử lý",
                    "Vui lòng đợi ...", true);
            GeneralAPI testAPI = new GeneralAPI();
            testAPI.getAllAddress(dialog, LoginActivity.this);
        }
        //Check login
        username = edtUsername.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        validObj = new ValidateInput();
        locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);
        Boolean checkUsername = validObj.validUsername(username, edtUsername);
        Boolean checkPassword = validObj.validPassword(password, edtPassword);
        if (checkUsername && checkPassword) {
            dialog = ProgressDialog.show(LoginActivity.this, "Đăng nhập",
                    "Đang xác thực ...", true);
            checkLogin(username, password, LoginActivity.this, dialog);
        }
    }

    private void signupAction(){
        if (GeneralAPI.listAddressFromDB.size() != 0) {
            SharedPreferences settings_2 = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
            settings_2.edit().clear().commit();
            SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
            editor.putString(ImmutableValue.SIGNUP_username, "");
            editor.putString(ImmutableValue.SIGNUP_password, "");
            editor.putString(ImmutableValue.SIGNUP_email, "");
            editor.putString(ImmutableValue.SIGNUP_fullName, "");
            editor.putString(ImmutableValue.SIGNUP_phone, "");
            editor.putString(ImmutableValue.SIGNUP_cmnd, "");
            editor.putString(ImmutableValue.SIGNUP_img_CMND, "");
            editor.apply();
            Intent it = new Intent(LoginActivity.this, SignupAccountActivity.class);
            startActivity(it);
        } else {
            dialog = ProgressDialog.show(LoginActivity.this, "Đang xử lý",
                    "Vui lòng đợi ...", true);
            GeneralAPI testAPI = new GeneralAPI();
            testAPI.getAllAddress(dialog, LoginActivity.this);
        }
    }

    private void checkLogin(final String username, String password, final Context ctx, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConfig.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Account> responseBodyCall = testAPI.login(new Login(username, password));
        responseBodyCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.code() == 401) {
                    Toast.makeText(ctx, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(ctx, "Tài khoản chưa được chấp nhận! Vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
                } else {
                    Account accObj = new Account();
                    accObj = response.body();
                    SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                    editor.putString(ImmutableValue.HOME_username, username);
                    editor.putInt(ImmutableValue.HOME_userID, accObj.getUserID());
                    editor.putString(ImmutableValue.HOME_accessToken, accObj.getAccessToken());
                    editor.putString(ImmutableValue.HOME_role, accObj.getRoleName());
                    editor.putString(ImmutableValue.HOME_fullName, accObj.getFullname());
                    editor.apply();

                    SharedPreferences.Editor tempEditor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                    tempEditor.putString(ImmutableValue.MAIN_vehicleID, "Empty");
                    tempEditor.putString(ImmutableValue.MAIN_contractID, "Empty");
                    tempEditor.apply();
                    Intent it = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(it);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
