package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.Validate;

import java.util.ArrayList;
import java.util.List;

public class SignupOwnerTwo extends AppCompatActivity {

    Button btnVehicleNext, btnVehicleBack;
    EditText edtVehicleYear, edtVehicleSeat;
    Spinner spnVehicleEngineType, spnVehicleTranmission, spnVehicleMaker, spnVehicleModel;
    RetrofitCallAPI testAPI = new RetrofitCallAPI();

    String vehicleMaker = "", vehicleModel = "", vehicleYear = "";

    //Validate validObj;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_two);

        //Declare id
        btnVehicleNext = (Button) findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button) findViewById(R.id.btnVehicleBack);

        edtVehicleSeat = (EditText) findViewById(R.id.txtVehicleSeat);
        edtVehicleYear = (EditText) findViewById(R.id.edtVehicleYear);
        spnVehicleEngineType = (Spinner) findViewById(R.id.spnVehicleEngineType);
        spnVehicleTranmission = (Spinner) findViewById(R.id.spnVehicleTranmission);
        spnVehicleMaker = (Spinner)findViewById(R.id.spnVehicleMaker);
        spnVehicleModel = (Spinner)findViewById(R.id.spnVehicleModel);


        ///// Spinner ////
        final List<String> vehicleMakerList = testAPI.getAllVehicleMaker();
        ArrayAdapter<String> vehicleMakerAdaper = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, vehicleMakerList);
        vehicleMakerAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVehicleMaker.setAdapter(vehicleMakerAdaper);

        spnVehicleMaker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                vehicleModelList = new ArrayList<>();
//                vehicleModelList = testAPI.getAllVehicleModel(vehicleMakerList.get(position).toString());
//                vehicleMaker = vehicleMakerList.get(position).toString();
//                Log.d("MAker dc chon la:", vehicleMaker);
                ((TextView) view).setTextColor(Color.RED);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<String> vehicleModelList = testAPI.getAllVehicleModel(vehicleMaker);
        ArrayAdapter<String> vehicleModelAdapter = new ArrayAdapter<>(SignupOwnerTwo.this,
                android.R.layout.simple_spinner_dropdown_item, vehicleModelList);
        spnVehicleModel.setAdapter(vehicleModelAdapter);
        spnVehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleModel = vehicleModelList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Button next
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                vehicleYear = edtVehicleYear.getText().toString();
//                engine = spnVehicleEngineType.getSelectedItem().toString();
//                tranmission = spnVehicleTranmission.getSelectedItem().toString();
//                seat = edtVehicleSeat.getText().toString();

                //validObj = new Validate();
                //Boolean checkVehicleName = validObj.validVehicleName(vehicleName, edtVehicleName);
                //if (checkVehicleName) {
                    //Declare shared preferences
//                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
//                    //editor.putString("vehicleName", vehicleName);
//                    editor.putString("vehicleYear", vehicleYear);
//                    editor.putString("engine", engine);
//                    editor.putString("tranmission", tranmission);
//                    editor.putString("seat", seat);
//                    editor.apply();

                    //Start signupOwnerTwoPlus activity
                    Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerTwoPlus.class);
                    startActivity(it);
                //}
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
