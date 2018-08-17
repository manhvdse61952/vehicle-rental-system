package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.vehicleTab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.tracking.TrackingVehicle;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage.UpdateVehicle;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VehicleItemTab extends BaseAdapter {
    private List<SearchVehicleItem> vehicleItemList;
    private static LayoutInflater inf = null;
    Context ctx = null;

    public VehicleItemTab(List<SearchVehicleItem> vehicleItemList, Context ctx) {
        this.vehicleItemList = vehicleItemList;
        this.ctx = ctx;
        inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vehicleItemList.size();
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
        TextView txt_vehicle_name, txt_vehicle_frameNumber, txt_vehicle_status;
        ImageView imageManageVehicle;
        LinearLayout ln_vehicle_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final SearchVehicleItem searchVehicleItem = vehicleItemList.get(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inf.inflate(R.layout.manage_vehicle_item, null);
            viewHolder.txt_vehicle_name = (TextView)convertView.findViewById(R.id.txt_vehicle_name);
            viewHolder.txt_vehicle_frameNumber = (TextView)convertView.findViewById(R.id.txt_vehicle_frameNumber);
            viewHolder.txt_vehicle_status = (TextView)convertView.findViewById(R.id.txt_vehicle_status);
            viewHolder.imageManageVehicle = (ImageView)convertView.findViewById(R.id.manage_vehicle_image);
            viewHolder.ln_vehicle_item = (LinearLayout)convertView.findViewById(R.id.ln_vehicle_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_vehicle_name.setText(searchVehicleItem.getVehicleMaker() + " " + searchVehicleItem.getVehicleModel());
        viewHolder.txt_vehicle_frameNumber.setText(searchVehicleItem.getFrameNumber());
        if (searchVehicleItem.getCurrentStatus().equals(ImmutableValue.VEHICLE_STATUS_AVAILABLE)){
            viewHolder.txt_vehicle_status.setText("Rảnh rỗi");
            viewHolder.txt_vehicle_status.setTextColor(Color.parseColor("#1B5E20"));
        } else {
            viewHolder.txt_vehicle_status.setText("đang bận");
            viewHolder.txt_vehicle_status.setTextColor(Color.parseColor("#FF0000"));
        }

        if (searchVehicleItem.getImageLinkFront().equals("")){
            Picasso.get().load(R.drawable.img_default_image).into(viewHolder.imageManageVehicle);
        } else {
            Picasso.get().load(searchVehicleItem.getImageLinkFront()).into(viewHolder.imageManageVehicle);
        }
        viewHolder.ln_vehicle_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                SharedPreferences editor2 = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
                editor.putString(ImmutableValue.MAIN_vehicleID, searchVehicleItem.getFrameNumber());
                editor.putString(ImmutableValue.MAIN_vehicleAddress, "Empty");
                editor.putString(ImmutableValue.MAIN_vehicleLat, "Empty");
                editor.putString(ImmutableValue.MAIN_vehicleLng, "Empty");
                editor.putString(ImmutableValue.MAIN_isUpdateVehicle, "true");
                editor.apply();

                String isTracking = editor2.getString(ImmutableValue.MAIN_isTracking, "false");
                if (isTracking.equals("false")){
                    Intent it = new Intent(ctx, UpdateVehicle.class);
                    ctx.startActivity(it);
                } else {
                    Intent it = new Intent(ctx, TrackingVehicle.class);
                    ctx.startActivity(it);
                }
            }
        });

        return convertView;
    }
}
