package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;

public class SignupAccountActivity extends AppCompatActivity {

    Button btnBack, btnNext;
    EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;
    private String username = "", password = "", email = "";

    /////////////// use for test ////////////
    //TextView txtSignupEmail;
    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);

        //Declare id
        edtSignupUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);

        //button Next
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get value from edittext
                username = edtSignupUsername.getText().toString().trim();
                password = edtSignupPassword.getText().toString().trim();
                email = edtSignupEmail.getText().toString().trim();

                if (username.length()!= 0 || password.length() != 0 || email.length() != 0){
                    //shared preferences
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("email", email);
                    editor.apply();

                    //Call API
                    RetrofitCallAPI rfCall = new RetrofitCallAPI();
                    rfCall.checkExistedUsername(username, password, email, SignupAccountActivity.this);
                }
                else {
                    Toast.makeText(SignupAccountActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Back button
        btnBack = (Button) findViewById(R.id.btnSignupAccountBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupAccountActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });


        /////////////////////////// Use for test ////////////////////////////
//        txtSignupEmail = (TextView)findViewById(R.id.txtSignupEmail);
//        txtSignupEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                email = edtSignupEmail.getText().toString();
//                email = email + "@gmail.com";
//                edtSignupEmail.setText(email);
//
//            }
//        });
        /////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupAccountActivity.this, LoginActivity.class);
        startActivity(it);
    }
}
