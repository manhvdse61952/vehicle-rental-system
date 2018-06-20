package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.VehicleInformation_New;
import com.example.manhvdse61952.vrc_android.model.Vehicle_New;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Slider extends PagerAdapter {


    private LayoutInflater inflater;
    private Context ctx;

    public Slider(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        SharedPreferences editor = ctx.getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
        String framenumber = editor.getString("frameNumber", null);
        ////////// Get Image and vehicle name ///////////
        List<VehicleInformation_New> vehicleInformationNewList = new ArrayList<>();
        List<Vehicle_New> vehicleNewList = new ArrayList<>();
        ImmutableValue importantObj = new ImmutableValue();
        vehicleInformationNewList = importantObj.readVehicleInforFile(ctx);
        vehicleNewList = importantObj.readVehicleFile(ctx);

        /////// Object ////
        VehicleInformation_New tempObj = new VehicleInformation_New();
        String imageLinkFront = "", imageLinkBack = "";

        for (int i = 0; i < vehicleNewList.size();i++){
            if (framenumber.equalsIgnoreCase(vehicleNewList.get(i).getFrameNumber())){
                imageLinkFront = vehicleNewList.get(i).getImageLinkFront();
                imageLinkBack = vehicleNewList.get(i).getImageLinkBack();
                tempObj = importantObj.getVehicleInfo(vehicleNewList.get(i).getVehicleInformationID(), vehicleInformationNewList);
            }
        }




        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_main_item_slider, container, false);
        ImageView img = (ImageView)v.findViewById(R.id.sliderImgItem);
        TextView txtTitle = (TextView)v.findViewById(R.id.txtTitle);

        if (position == 0){
            Picasso.get().load(imageLinkFront).into(img);
        } else if (position == 1){
            Picasso.get().load(imageLinkBack).into(img);
        }

        txtTitle.setText(tempObj.getVehicleMaker() + " " + tempObj.getVehicleModel());
        txtTitle.setAllCaps(true);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
