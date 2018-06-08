package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class SignupOwnerTwoPlus extends AppCompatActivity {

    EditText edtVehicleFrameNumber, edtVehicleRent, edtVehicleDeposit;
    Button btnVehicleNext, btnVehicleBack;

    String frameNumber = "", rent = "", deposit = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_two_plus);

        //Decalre id
        edtVehicleFrameNumber = (EditText)findViewById(R.id.edtVehicleFrameNumber);
        edtVehicleRent = (EditText)findViewById(R.id.edtVehicleRent);
        edtVehicleDeposit = (EditText)findViewById(R.id.edtVehicleDeposit);
        btnVehicleNext = (Button)findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button)findViewById(R.id.btnVehicleBack);

        //Button next
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameNumber = edtVehicleFrameNumber.getText().toString();
                rent = edtVehicleRent.getText().toString();
                deposit = edtVehicleDeposit.getText().toString();

                //Declare shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("frameNumber", frameNumber);
                editor.putString("rent", rent);
                editor.putString("deposit", deposit);
                editor.apply();

                //Start signupOwnerThree activity
                Intent it = new Intent(SignupOwnerTwoPlus.this, SignupOwnerThree.class);
                startActivity(it);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerTwoPlus.this, SignupOwnerTwo.class);
        startActivity(it);
    }
}
