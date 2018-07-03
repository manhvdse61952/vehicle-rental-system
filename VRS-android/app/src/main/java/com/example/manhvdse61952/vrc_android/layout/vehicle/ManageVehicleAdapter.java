package com.example.manhvdse61952.vrc_android.layout.vehicle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.apiModel.Vehicle_New;

import java.util.ArrayList;
import java.util.List;

public class ManageVehicleAdapter extends RecyclerView.Adapter<ManageVehicleAdapter.RecyclerViewHolder> {
    private List<Vehicle_New> vehicleList = new ArrayList<>();
    @Override
    public ManageVehicleAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_vehicle_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    public ManageVehicleAdapter(List<Vehicle_New> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @Override
    public void onBindViewHolder(ManageVehicleAdapter.RecyclerViewHolder holder, int position) {
        holder.txtframeNumber.setText(vehicleList.get(position).getFrameNumber());
//        holder.txtStatus.setText(vehicleList.get(position).get);
//        holder.imageManageVehicle.setImageURI(vehicleList.get(position).);
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtframeNumber;
        TextView txtStatus;
        ImageView imageManageVehicle;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtframeNumber = itemView.findViewById(R.id.vehicle_frame_number);
            txtStatus = itemView.findViewById(R.id.vehicle_status);
            imageManageVehicle = itemView.findViewById(R.id.manage_vehicle_image);
        }
    }
}
