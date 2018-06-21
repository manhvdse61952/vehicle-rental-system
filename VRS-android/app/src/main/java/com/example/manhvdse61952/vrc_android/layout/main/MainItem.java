package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.order.MainOrderOne;
import com.example.manhvdse61952.vrc_android.model.apiModel.VehicleInformation_New;
import com.example.manhvdse61952.vrc_android.model.apiModel.Vehicle_New;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

import java.util.ArrayList;
import java.util.List;

public class MainItem extends AppCompatActivity {
    Slider sld;
    ViewPager vpg;
    Button btnOrderRent;

    TextView item_price_unit, item_price_slot, item_price_day, item_seat, item_year, item_plateNumber, item_ownerName, item_description;
    CheckBox cbx1, cbx2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);

        //Use for slider
        vpg = (ViewPager)findViewById(R.id.vpg);
        sld = new Slider(this);
        vpg.setAdapter(sld);

        //Declare id
        item_price_unit = (TextView)findViewById(R.id.item_price_unit);
        item_price_slot = (TextView)findViewById(R.id.item_price_slot);
        item_price_day = (TextView)findViewById(R.id.item_price_day);
        item_seat = (TextView)findViewById(R.id.item_seat);
        item_year = (TextView)findViewById(R.id.item_year);
        item_plateNumber = (TextView)findViewById(R.id.item_plateNumber);
        item_ownerName = (TextView)findViewById(R.id.item_ownerName);
        item_description = (TextView)findViewById(R.id.item_description);

        ///////////////////// get information /////////////////////
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String framenumber = editor.getString("frameNumber", null);
        List<VehicleInformation_New> vehicleInformationNewList = new ArrayList<>();
        List<Vehicle_New> vehicleNewList = new ArrayList<>();
        ImmutableValue importantObj = new ImmutableValue();


        VehicleInformation_New tempObj = new VehicleInformation_New();
        for (int i = 0; i < vehicleNewList.size(); i++){
            if (framenumber.equalsIgnoreCase(vehicleNewList.get(i).getFrameNumber())){
                tempObj = importantObj.getVehicleInfo(vehicleNewList.get(i).getVehicleInformationID(), vehicleInformationNewList);
                if (tempObj.getVehicleType().equalsIgnoreCase("XE_MAY")){
                    item_price_unit.setText("Đơn giá / giờ");
                    item_price_slot.setText(vehicleNewList.get(i).getRentFeePerHours());
                }
                else {
                    item_price_unit.setText("Đơn giá / buổi");
                    item_price_slot.setText(vehicleNewList.get(i).getRentFeePerSlot());
                }

                item_price_day.setText(vehicleNewList.get(i).getRentFeePerDay());
                item_seat.setText(tempObj.getSeat() + "");
                item_year.setText(tempObj.getModelYear() + "");
                item_plateNumber.setText(vehicleNewList.get(i).getPlateNumber());
                if (vehicleNewList.get(i).getDescription().trim().equals("")){
                    item_description.setText("Không có");
                } else {
                    item_description.setText(vehicleNewList.get(i).getDescription());
                }

            }
        }


        btnOrderRent = (Button)findViewById(R.id.btnOrderRent);
        btnOrderRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainItem.this, MainOrderOne.class);
                startActivity(it);
            }
        });
    }
}
