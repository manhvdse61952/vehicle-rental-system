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
import com.example.manhvdse61952.vrc_android.remote.Validate;

public class SignupOwnerFour extends AppCompatActivity {

    Button btnVehicleNext, btnVehicleBack;
    EditText edtRentPerSlot, edtRentPerDay, edtRentPerHours, edtSignupVehicleDescription, edtDepositFee;
    Validate validObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_four);


        btnVehicleNext = (Button) findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button) findViewById(R.id.btnVehicleBack);
        edtRentPerSlot = (EditText)findViewById(R.id.edtRentPerSlot);
        edtRentPerDay = (EditText)findViewById(R.id.edtRentPerDay);
        edtRentPerHours = (EditText)findViewById(R.id.edtRentPerHours);
        edtDepositFee = (EditText)findViewById(R.id.edtDepositFee);
        edtSignupVehicleDescription = (EditText)findViewById(R.id.edtSignupVehicleDescription);

        //Next button
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validObj = new Validate();
                Boolean checkSlotFee = validObj.validPrice(edtRentPerSlot.getText().toString(), edtRentPerSlot);
                Boolean checkDayFee = validObj.validPrice(edtRentPerDay.getText().toString(), edtRentPerDay);
                Boolean checkHourFee = validObj.validPrice(edtRentPerHours.getText().toString(), edtRentPerHours);
                Boolean checkDeposit = validObj.validPrice(edtDepositFee.getText().toString(), edtDepositFee);
                if (checkSlotFee && checkDayFee && checkHourFee && checkDeposit){
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("rentFeePerSlot", edtRentPerSlot.getText().toString());
                    editor.putString("rentFeePerDay", edtRentPerDay.getText().toString());
                    editor.putString("rentFeePerHours", edtRentPerHours.getText().toString());
                    editor.putString("depositFee", edtDepositFee.getText().toString());
                    editor.putString("description", edtSignupVehicleDescription.getText().toString());
                    editor.apply();
                    Intent it = new Intent(SignupOwnerFour.this, SignupOwnerPolicy.class);
                    startActivity(it);
                }



            }
        });

        //Back button
        btnVehicleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerFour.this, SignupOwnerThree.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerFour.this, SignupOwnerThree.class);
        startActivity(it);
    }

}
