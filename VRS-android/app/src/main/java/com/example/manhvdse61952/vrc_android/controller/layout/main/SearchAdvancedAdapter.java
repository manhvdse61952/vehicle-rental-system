package com.example.manhvdse61952.vrc_android.controller.layout.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class SearchAdvancedAdapter extends RecyclerView.Adapter<SearchAdvancedAdapter.RecycleViewHolder> {
    private List<SearchVehicleItem> vehicleItemList;
    private Context ctx;

    public SearchAdvancedAdapter(List<SearchVehicleItem> vehicleItemList, Context ctx) {
        this.vehicleItemList = vehicleItemList;
        this.ctx = ctx;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_search_item, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder viewHolder, int position) {
        final SearchVehicleItem obj = vehicleItemList.get(position);
        if (obj.getImageLinkFront().equals("")){
            Picasso.get().load(R.drawable.img_default_image).into(viewHolder.img_main);
        } else {
            Picasso.get().load(obj.getImageLinkFront()).into(viewHolder.img_main);
        }
        viewHolder.txt_main_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
        viewHolder.txt_main_seat.setText(obj.getSeat() + " chỗ");
        String districtName = getDistrictNameById(obj.getDistrictID());
        viewHolder.txt_main_address.setText(districtName);
        if (obj.getVehicleType().equals(ImmutableValue.XE_MAY)) {
            String[] temp = obj.getRentFeePerHours().split("[.]");
            String price = GeneralController.convertPrice(temp[0]);
            viewHolder.txt_main_price.setText(price + " / giờ");
        } else {
            String[] temp = obj.getRentFeePerDay().split("[.]");
            String price = GeneralController.convertPrice(temp[0]);
            viewHolder.txt_main_price.setText(price + " / ngày");
        }
        viewHolder.search_layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.MAIN_vehicleID, obj.getFrameNumber());
                editor.apply();

                Intent it = new Intent(ctx, VehicleDetail.class);
                ctx.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleItemList.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView img_main;
        TextView txt_main_name, txt_main_seat, txt_main_address, txt_main_price;
        RelativeLayout search_layout_item;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            img_main = (ImageView)itemView.findViewById(R.id.img_main);
            txt_main_name = (TextView)itemView.findViewById(R.id.txt_main_name);
            txt_main_seat = (TextView)itemView.findViewById(R.id.txt_main_seat);
            txt_main_address = (TextView)itemView.findViewById(R.id.txt_main_address);
            txt_main_price = (TextView)itemView.findViewById(R.id.txt_main_price);
            search_layout_item = (RelativeLayout)itemView.findViewById(R.id.search_layout_item);
        }
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
