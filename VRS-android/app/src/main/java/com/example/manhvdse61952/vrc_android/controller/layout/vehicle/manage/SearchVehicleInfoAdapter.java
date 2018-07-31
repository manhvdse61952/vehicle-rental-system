package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.VehicleInformation;

import java.util.List;

public class SearchVehicleInfoAdapter extends RecyclerView.Adapter<SearchVehicleInfoAdapter.RecyclerViewHolder>{
    private List<VehicleInformation> vehicleInformationList;
    private Context ctx;
    private Activity atv;
    @Override
    public SearchVehicleInfoAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_vehicle_information_item, parent, false);
        return new SearchVehicleInfoAdapter.RecyclerViewHolder(view);
    }

    public SearchVehicleInfoAdapter(List<VehicleInformation> vehicleInformationList, Context ctx, Activity atv) {
        this.vehicleInformationList = vehicleInformationList;
        this.ctx = ctx;
        this.atv = atv;
    }

    @Override
    public void onBindViewHolder(SearchVehicleInfoAdapter.RecyclerViewHolder holder, final int position) {
        holder.txt_manage_vi_name.setText(vehicleInformationList.get(position).getVehicleMaker() + " " + vehicleInformationList.get(position).getVehicleModel());
        holder.ln_manage_vehicle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.VEHICLE_vehicleMaker, vehicleInformationList.get(position).getVehicleMaker());
                editor.putString(ImmutableValue.VEHICLE_vehicleModel, vehicleInformationList.get(position).getVehicleModel());
                editor.apply();
                Intent returnIntent = new Intent();
                atv.setResult(Activity.RESULT_OK, returnIntent);
                atv.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleInformationList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txt_manage_vi_name;
        LinearLayout ln_manage_vehicle_info;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txt_manage_vi_name = (TextView)itemView.findViewById(R.id.txt_manage_vi_name);
            ln_manage_vehicle_info = (LinearLayout)itemView.findViewById(R.id.ln_manage_vehicle_info);
        }
    }
}
