package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class SignupOwnerTwo extends AppCompatActivity {

    Button btnVehicleNext, btnVehicleBack;
    EditText edtVehicleName, edtVehicleYear;
    Spinner spnVehicleEngineType, spnVehicleTranmission, spnVehicleSeat;

    String vehicleName = "", vehicleYear = "", engine = "", tranmission = "", seat = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_two);

        //Declare id
        btnVehicleNext = (Button)findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button)findViewById(R.id.btnVehicleBack);
        edtVehicleName = (EditText)findViewById(R.id.edtVehicleName);
        edtVehicleYear = (EditText)findViewById(R.id.edtVehicleYear);
        spnVehicleEngineType = (Spinner)findViewById(R.id.spnVehicleEngineType);
        spnVehicleTranmission = (Spinner)findViewById(R.id.spnVehicleTranmission);
        spnVehicleSeat = (Spinner)findViewById(R.id.spnVehicleSeat);

        //save value when user press back button


        //Button next
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleName = edtVehicleName.getText().toString();
                vehicleYear = edtVehicleYear.getText().toString();
                engine = spnVehicleEngineType.getSelectedItem().toString();
                tranmission = spnVehicleTranmission.getSelectedItem().toString();
                seat = spnVehicleSeat.getSelectedItem().toString();

                //Declare shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("vehicleName", vehicleName);
                editor.putString("vehicleYear", vehicleYear);
                editor.putString("engine", engine);
                editor.putString("tranmission", tranmission);
                editor.putString("seat", seat);
                editor.apply();

                //Start signupOwnerTwoPlus activity
                Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerTwoPlus.class);
                startActivity(it);
            }
        });

        //button Back
        btnVehicleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerOne.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerOne.class);
        startActivity(it);
    }
}
