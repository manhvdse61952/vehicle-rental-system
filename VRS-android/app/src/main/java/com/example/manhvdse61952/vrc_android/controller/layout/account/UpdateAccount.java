package com.example.manhvdse61952.vrc_android.controller.layout.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.AccountUpdate;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateAccount extends AppCompatActivity {
    TextView txt_update_role, txt_update_username, txt_update_email, txt_update_cmnd
            ,txt_update_phone;
    CircleImageView img_update_profile;
    EditText edt_update_fullname, edt_old_email, edt_ChangePass, edt_RepeatPass;
    LinearLayout ln_changePass;
    Button btn_update;
    ImageView img_edit_name, img_edit_password;
    ScrollView scrollView;
    Boolean isOpen = false;
    ProgressDialog dialog;
    String fullNameFromServer;
    int id = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();
    }

    private void declareID(){
        txt_update_role = (TextView)findViewById(R.id.txt_update_role);
        txt_update_username = (TextView)findViewById(R.id.txt_update_username);
        txt_update_email = (TextView)findViewById(R.id.txt_update_email);
        txt_update_cmnd = (TextView)findViewById(R.id.txt_update_cmnd);
        txt_update_phone = (TextView)findViewById(R.id.txt_update_phone);
        img_update_profile = (CircleImageView)findViewById(R.id.img_update_profile);
        edt_update_fullname = (EditText)findViewById(R.id.edt_update_fullname);
        edt_old_email = (EditText)findViewById(R.id.edt_old_email);
        edt_ChangePass = (EditText)findViewById(R.id.edt_ChangePass);
        edt_RepeatPass = (EditText)findViewById(R.id.edt_RepeatPass);
        ln_changePass = (LinearLayout)findViewById(R.id.ln_changePass);
        btn_update = (Button)findViewById(R.id.btn_update);
        img_edit_name = (ImageView)findViewById(R.id.img_edit_name);
        img_edit_password  =(ImageView)findViewById(R.id.img_edit_password);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
    }

    @Override
    public void onBackPressed() {
        UpdateAccount.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    edt_update_fullname.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void initLayout(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        id = editor.getInt(ImmutableValue.HOME_userID, 0);
        String role = editor.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER);
        if (role.equals(ImmutableValue.ROLE_USER)){
            img_update_profile.setImageResource(R.drawable.img_customer_edit_2);
            txt_update_role.setText("Người thuê xe");
        } else {
            img_update_profile.setImageResource(R.drawable.img_owner);
            txt_update_role.setText("Chủ xe");
        }

        img_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_update_fullname.requestFocus();
                edt_update_fullname.setBackgroundResource(R.drawable.border_red_white);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_update_fullname, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        GeneralController.scaleView(ln_changePass, 0);

        img_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen == false){
                    GeneralController.scaleViewAndScroll(ln_changePass, -150, scrollView);
                    isOpen = true;
                } else {
                    GeneralController.scaleView(ln_changePass, 0);
                    isOpen = false;
                    //Footer.requestFocus();
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAction();
            }
        });
        dialog = ProgressDialog.show(UpdateAccount.this, "Hệ thống",
                "Đang xử lý...", true);
        Retrofit test = RetrofitConfig.getClient();
        AccountAPI accountAPI = test.create(AccountAPI.class);
        Call<AccountUpdate> responseBodyCall = accountAPI.getUserInfoByID(id);
        responseBodyCall.enqueue(new Callback<AccountUpdate>() {
            @Override
            public void onResponse(Call<AccountUpdate> call, Response<AccountUpdate> response) {
                if (response.code() == 200){
                    AccountUpdate obj = response.body();
                    txt_update_username.setText(obj.getUsername());
                    txt_update_email.setText(obj.getEmail());
                    txt_update_cmnd.setText(obj.getCmnd());
                    txt_update_phone.setText(obj.getPhone());
                    edt_update_fullname.setText(obj.getFullname());
                    fullNameFromServer = obj.getFullname();
                } else {
                    Toast.makeText(UpdateAccount.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<AccountUpdate> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UpdateAccount.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAction(){
        Boolean truePassword = false;
        Boolean trueFullName = false;
        String fullname = edt_update_fullname.getText().toString().trim();
        String password = edt_ChangePass.getText().toString().trim();
        String repeatPassword = edt_RepeatPass.getText().toString().trim();

        if (fullname.equals("") || fullname.length() < 3){
            Toast.makeText(this, "Tên phải ít nhất 3 kí tự", Toast.LENGTH_SHORT).show();
        } else if (fullname.equals(fullNameFromServer) && password.equals("")){
            Toast.makeText(this, "Không có thay đổi", Toast.LENGTH_SHORT).show();
        }
        else {
            trueFullName = true;
            if (password.equals("")&&repeatPassword.equals("")){
                password = "";
                truePassword = true;
            } else if (!password.equals("")){
                if (!repeatPassword.equals(password)){
                    Toast.makeText(this, "Nhập lại mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
                if (password.length() < 5){
                    Toast.makeText(this, "Mật khẩu từ 6 kí tự trở lên", Toast.LENGTH_SHORT).show();
                }
                if (password.length() > 5 && repeatPassword.equals(password)){
                    truePassword = true;
                }
            }
        }

        if (truePassword && trueFullName){
            dialog = ProgressDialog.show(UpdateAccount.this, "Hệ thống",
                    "Đang xử lý...", true);
            Retrofit test = RetrofitConfig.getClient();
            AccountAPI accountAPI = test.create(AccountAPI.class);
            Call<ResponseBody> responseBodyCall = accountAPI.updateUserByID(id, fullname, password);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        Toast.makeText(UpdateAccount.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    } else {
                        finish();
                        startActivity(getIntent());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(UpdateAccount.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
