package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail.VehicleDetail;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.City;
import com.example.manhvdse61952.vrc_android.model.api_model.District;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VehicleItem extends BaseAdapter {
    private List<SearchVehicleItem> searchItemList;
    private static LayoutInflater inf = null;
    Context context = null;

    public VehicleItem(MotobikeTab t, List<SearchVehicleItem> searchItemList, Context context) {
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public VehicleItem(CarTab t, List<SearchVehicleItem> searchItemList, Context context) {
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public VehicleItem(TravelCarTab t, List<SearchVehicleItem> searchItemList, Context context) {
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return searchItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView img_main;
        TextView txt_main_name, txt_main_seat, txt_main_address, txt_main_price;
        RelativeLayout search_layout_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final SearchVehicleItem obj = searchItemList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inf.inflate(R.layout.custom_search_item, null);
            viewHolder.img_main = (ImageView) convertView.findViewById(R.id.img_main);
            viewHolder.txt_main_name = (TextView) convertView.findViewById(R.id.txt_main_name);
            viewHolder.txt_main_seat = (TextView) convertView.findViewById(R.id.txt_main_seat);
            viewHolder.txt_main_address = (TextView) convertView.findViewById(R.id.txt_main_address);
            viewHolder.txt_main_price = (TextView) convertView.findViewById(R.id.txt_main_price);
            viewHolder.search_layout_item = (RelativeLayout) convertView.findViewById(R.id.search_layout_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProgressBar pgb = new ProgressBar(context);
        if (obj.getImageLinkFront().trim().equals("")) {
            Picasso.get().load(R.drawable.img_default_image).into(viewHolder.img_main);
        } else {
            Picasso.get().load(obj.getImageLinkFront()).into(viewHolder.img_main);
        }
        viewHolder.txt_main_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
        viewHolder.txt_main_seat.setText(obj.getSeat() + " chỗ");

        String districtName = getDistrictNameById(obj.getDistrictID());
        viewHolder.txt_main_address.setText(districtName);
        if (obj.getVehicleType().equals(ImmutableValue.XE_MAY)) {
            String price = GeneralController.convertPrice(obj.getRentFeePerHours());
            viewHolder.txt_main_price.setText(price + " / giờ");
        } else {
            String price = GeneralController.convertPrice(obj.getRentFeePerDay());
            viewHolder.txt_main_price.setText(price + " / ngày");
        }

        viewHolder.search_layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, context.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.MAIN_vehicleID, obj.getFrameNumber());
                editor.apply();

                Intent it = new Intent(context, VehicleDetail.class);
                context.startActivity(it);
            }
        });
        return convertView;
    }

    private String getDistrictNameById(int id){
        List<City> listCity = ImmutableValue.listGeneralAddress;
        String districtName = "";
        for (int i = 0; i< listCity.size(); i++){
            List<District> listDistrict = listCity.get(i).getDistrict();
            for (int j=0; j<listDistrict.size();j++){
                if (listDistrict.get(j).getId() == id){
                    String cityCompress = "HCM";
                    if (listCity.get(i).getCityName().toLowerCase().equals("hà nội")){
                        cityCompress = "HN";
                    } else if (listCity.get(i).getCityName().toLowerCase().equals("hồ chí minh")){
                        cityCompress = "HCM";
                    } else if (listCity.get(i).getCityName().toLowerCase().equals("đà nẵng")){
                        cityCompress = "Đ.Nẵng";
                    }
                    districtName = listDistrict.get(j).getDistrictName() + ", " + cityCompress;
                    break;
                }
            }
        }
        return districtName;
    }
}
