package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;

import java.util.List;

public class ManageContractAdapter extends RecyclerView.Adapter<ManageContractAdapter.RecyclerViewHolder>{
    private List<ContractItem> contractItemList;
    private Context ctx;
    @Override
    public ManageContractAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_contract_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    public ManageContractAdapter(List<ContractItem> contractItemList, Context ctx) {
        this.contractItemList = contractItemList;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ContractItem obj = contractItemList.get(position);
        holder.txt_manage_contract_id.setText("Hợp đồng #" + obj.getContractID());
        if(obj.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE)){
            holder.txt_manage_contract_status.setText("Chưa bắt đầu");
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ACTIVE)){
            holder.txt_manage_contract_status.setText("Đang hoạt động");
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING)){
            holder.txt_manage_contract_status.setText("Đợi trả xe");
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PRE_FINISHED)){
            holder.txt_manage_contract_status.setText("Đang thỏa thuận");
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)){
            holder.txt_manage_contract_status.setText("Hoàn thành");
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
            holder.txt_manage_contract_status.setText("Khiếu nại");
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)){
            holder.txt_manage_contract_status.setText("Hủy hợp đồng");
        }

        if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)){
            holder.ln_manage_contract.setBackgroundResource(R.drawable.border_green);
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)) {
            holder.ln_manage_contract.setBackgroundResource(R.drawable.border_red);
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE)){
            holder.ln_manage_contract.setBackgroundResource(R.drawable.border_gray);
        } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
            holder.ln_manage_contract.setBackgroundResource(R.drawable.border_light_green);
        } else {
            holder.ln_manage_contract.setBackgroundResource(R.drawable.border_blue);
        }

        holder.txt_manage_contract_start_time.setText(GeneralController.convertTime(obj.getStartTime()));
        holder.txt_manage_contract_end_time.setText(GeneralController.convertTime(obj.getEndTime()));
        int depositFee = Integer.parseInt(obj.getDepositFee());
        int totalFee = Integer.parseInt(obj.getTotalFee());
        int rentFee = totalFee - depositFee;
        int insideFee = contractItemList.get(position).getInsideFee();
        int outsideFee= contractItemList.get(position).getOutsideFee();
        int overTimeFee = contractItemList.get(position).getPenaltyOverTime();
        totalFee = totalFee + insideFee + outsideFee + overTimeFee;
        holder.txt_manage_contract_rent_fee.setText(GeneralController.convertPrice(String.valueOf(rentFee)));
        holder.txt_manage_contract_total_fee.setText(GeneralController.convertPrice(String.valueOf(totalFee)));
        holder.ln_manage_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.MAIN_contractID, obj.getContractID());
                editor.putString(ImmutableValue.MAIN_contractStatus, obj.getContractStatus());
                editor.putInt(ImmutableValue.MAIN_ownerID, obj.getOwnerID());
                editor.putInt(ImmutableValue.MAIN_customerID, obj.getCustomerID());
                editor.apply();
                SharedPreferences editor2 = ctx.getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
                int userID = editor2.getInt(ImmutableValue.HOME_userID, 0);
                String userRole = editor2.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER);
                if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING) && userID == obj.getOwnerID()){
                    Intent it = new Intent(ctx, ContractPreFinishOwner.class);
                    ctx.startActivity(it);
                } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING) && userRole.equals(ImmutableValue.ROLE_USER)){
                    Toast.makeText(ctx, "Bạn vui lòng đợi chủ xe đồng ý", Toast.LENGTH_SHORT).show();
                } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PRE_FINISHED) && userRole.equals(ImmutableValue.ROLE_USER)){
                    Intent it = new Intent(ctx, ContractPreFinishCustomer.class);
                    ctx.startActivity(it);
                } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_PRE_FINISHED) && userID == obj.getOwnerID()){
                    Toast.makeText(ctx, "Bạn vui lòng đợi khách hàng xác nhận", Toast.LENGTH_SHORT).show();
                } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
                    Intent it = new Intent(ctx, ContractComplainActivity.class);
                    ctx.startActivity(it);
                } else {
                    Intent it = new Intent(ctx, ContractDetail.class);
                    ctx.startActivity(it);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return contractItemList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txt_manage_contract_id, txt_manage_contract_status, txt_manage_contract_start_time,
                txt_manage_contract_end_time, txt_manage_contract_rent_fee, txt_manage_contract_total_fee;
        LinearLayout ln_manage_contract;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ln_manage_contract = (LinearLayout)itemView.findViewById(R.id.ln_manage_contract);
            txt_manage_contract_id = (TextView)itemView.findViewById(R.id.txt_manage_contract_id);
            txt_manage_contract_status = (TextView)itemView.findViewById(R.id.txt_manage_contract_status);
            txt_manage_contract_start_time = (TextView)itemView.findViewById(R.id.txt_manage_contract_start_time);
            txt_manage_contract_end_time = (TextView)itemView.findViewById(R.id.txt_manage_contract_end_time);
            txt_manage_contract_rent_fee = (TextView)itemView.findViewById(R.id.txt_manage_contract_rent_fee);
            txt_manage_contract_total_fee = (TextView)itemView.findViewById(R.id.txt_manage_contract_total_fee);
        }
    }

}
