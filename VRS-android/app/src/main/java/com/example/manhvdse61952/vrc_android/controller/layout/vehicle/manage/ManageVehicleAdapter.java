package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ManageVehicleAdapter extends RecyclerView.Adapter<ManageVehicleAdapter.RecyclerViewHolder> {
    private List<SearchVehicleItem> vehicleList;
    private Context ctx;
    @Override
    public ManageVehicleAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_vehicle_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    public ManageVehicleAdapter(List<SearchVehicleItem> vehicleList, Context ctx) {
        this.vehicleList = vehicleList;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(ManageVehicleAdapter.RecyclerViewHolder holder, final int position) {
        holder.txt_vehicle_name.setText(vehicleList.get(position).getVehicleMaker() + " "
                + vehicleList.get(position).getVehicleModel());
        holder.txt_vehicle_frameNumber.setText(vehicleList.get(position).getFrameNumber());
        if(vehicleList.get(position).getCurrentStatus().equals(ImmutableValue.VEHICLE_STATUS_AVAILABLE)){
            holder.txt_vehicle_status.setText("rảnh rỗi");
        } else {
            holder.txt_vehicle_status.setText("đang bận");
            holder.txt_vehicle_status.setTextColor(Color.parseColor("#FF0000"));
        }

        if (vehicleList.get(position).getImageLinkFront().equals("")){
            Picasso.get().load(R.drawable.img_default_image).into(holder.imageManageVehicle);
        }
        if (vehicleList.get(position).getImageLinkFront().equals("")){
            Picasso.get().load(R.drawable.img_default_image).into(holder.imageManageVehicle);
        } else {
            Picasso.get().load(vehicleList.get(position).getImageLinkFront()).into(holder.imageManageVehicle);
        }

        holder.ln_vehicle_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.MAIN_vehicleID, vehicleList.get(position).getFrameNumber());
                editor.putString(ImmutableValue.MAIN_vehicleAddress, "Empty");
                editor.putString(ImmutableValue.MAIN_vehicleLat, "Empty");
                editor.putString(ImmutableValue.MAIN_vehicleLng, "Empty");
                editor.putString(ImmutableValue.MAIN_isUpdateVehicle, "true");
                editor.apply();
                Intent it = new Intent(ctx, UpdateVehicle.class);
                ctx.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txt_vehicle_name, txt_vehicle_frameNumber, txt_vehicle_status;
        ImageView imageManageVehicle;
        LinearLayout ln_vehicle_item;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txt_vehicle_name = (TextView)itemView.findViewById(R.id.txt_vehicle_name);
            txt_vehicle_frameNumber = (TextView)itemView.findViewById(R.id.txt_vehicle_frameNumber);
            txt_vehicle_status = (TextView)itemView.findViewById(R.id.txt_vehicle_status);
            imageManageVehicle = (ImageView)itemView.findViewById(R.id.manage_vehicle_image);
            ln_vehicle_item = (LinearLayout)itemView.findViewById(R.id.ln_vehicle_item);
        }
    }
}
