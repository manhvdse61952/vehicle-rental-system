package com.example.manhvdse61952.vrc_android.controller.layout.contract.contract_tab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractComplainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractDetail;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractPreFinishCustomer;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractPreFinishOwner;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;

import java.util.List;

public class ContractItemTab extends BaseAdapter {
    private List<ContractItem> contractItemList;
    private static LayoutInflater inf = null;
    Context ctx = null;

    public ContractItemTab(List<ContractItem> contractItemList, Context context) {
        this.contractItemList = contractItemList;
        this.ctx = context;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return contractItemList.size();
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
        TextView txt_manage_contract_id, txt_manage_contract_status, txt_manage_contract_start_time,
                txt_manage_contract_end_time, txt_manage_contract_rent_fee, txt_manage_contract_total_fee;
        LinearLayout ln_manage_contract;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final ContractItem contractItem = contractItemList.get(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inf.inflate(R.layout.manage_contract_item, null);
            viewHolder.ln_manage_contract = (LinearLayout)convertView.findViewById(R.id.ln_manage_contract);
            viewHolder.txt_manage_contract_id = (TextView)convertView.findViewById(R.id.txt_manage_contract_id);
            viewHolder.txt_manage_contract_status = (TextView)convertView.findViewById(R.id.txt_manage_contract_status);
            viewHolder.txt_manage_contract_start_time = (TextView)convertView.findViewById(R.id.txt_manage_contract_start_time);
            viewHolder.txt_manage_contract_end_time = (TextView)convertView.findViewById(R.id.txt_manage_contract_end_time);
            viewHolder.txt_manage_contract_rent_fee = (TextView)convertView.findViewById(R.id.txt_manage_contract_rent_fee);
            viewHolder.txt_manage_contract_total_fee = (TextView)convertView.findViewById(R.id.txt_manage_contract_total_fee);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_manage_contract_id.setText("Hợp đồng #" + contractItem.getContractID());
        if(contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE)){
            viewHolder.txt_manage_contract_status.setText("Chưa bắt đầu");
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_ACTIVE)){
            viewHolder.txt_manage_contract_status.setText("Đang hoạt động");
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING)){
            viewHolder.txt_manage_contract_status.setText("Đợi trả xe");
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_PRE_FINISHED)){
            viewHolder.txt_manage_contract_status.setText("Đang thỏa thuận");
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)){
            viewHolder.txt_manage_contract_status.setText("Hoàn thành");
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
            viewHolder.txt_manage_contract_status.setText("Khiếu nại");
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)){
            viewHolder.txt_manage_contract_status.setText("Hủy hợp đồng");
        }

        if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)){
            viewHolder.ln_manage_contract.setBackgroundResource(R.drawable.border_green);
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)) {
            viewHolder.ln_manage_contract.setBackgroundResource(R.drawable.border_red);
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_INACTIVE)){
            viewHolder.ln_manage_contract.setBackgroundResource(R.drawable.border_blue);
        } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
            viewHolder.ln_manage_contract.setBackgroundResource(R.drawable.border_high_blue);
        } else {
            viewHolder.ln_manage_contract.setBackgroundResource(R.drawable.border_blue);
        }

        viewHolder.txt_manage_contract_start_time.setText(GeneralController.convertTime(contractItem.getStartTime()));
        viewHolder.txt_manage_contract_end_time.setText(GeneralController.convertTime(contractItem.getEndTime()));
        int depositFee = Integer.parseInt(contractItem.getDepositFee());
        int totalFee = Integer.parseInt(contractItem.getTotalFee());
        int rentFee = totalFee - depositFee;
        int insideFee = contractItemList.get(position).getInsideFee();
        int outsideFee= contractItemList.get(position).getOutsideFee();
        int overTimeFee = contractItemList.get(position).getPenaltyOverTime();
        totalFee = totalFee + insideFee + outsideFee + overTimeFee;
        viewHolder.txt_manage_contract_rent_fee.setText(GeneralController.convertPrice(String.valueOf(rentFee)));
        viewHolder.txt_manage_contract_total_fee.setText(GeneralController.convertPrice(String.valueOf(totalFee)));
        viewHolder.ln_manage_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.MAIN_contractID, contractItem.getContractID());
                editor.putString(ImmutableValue.MAIN_contractStatus, contractItem.getContractStatus());
                editor.putInt(ImmutableValue.MAIN_ownerID, contractItem.getOwnerID());
                editor.putInt(ImmutableValue.MAIN_customerID, contractItem.getCustomerID());
                editor.apply();
                SharedPreferences editor2 = ctx.getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
                int userID = editor2.getInt(ImmutableValue.HOME_userID, 0);
                String userRole = editor2.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER);
                if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING) && userID == contractItem.getOwnerID()){
                    Intent it = new Intent(ctx, ContractPreFinishOwner.class);
                    ctx.startActivity(it);
                } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_PENDING) && userRole.equals(ImmutableValue.ROLE_USER)){
                    Toast.makeText(ctx, "Bạn vui lòng đợi chủ xe đồng ý", Toast.LENGTH_SHORT).show();
                } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_PRE_FINISHED) && userRole.equals(ImmutableValue.ROLE_USER)){
                    Intent it = new Intent(ctx, ContractPreFinishCustomer.class);
                    ctx.startActivity(it);
                } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_PRE_FINISHED) && userID == contractItem.getOwnerID()){
                    Toast.makeText(ctx, "Bạn vui lòng đợi khách hàng xác nhận", Toast.LENGTH_SHORT).show();
                } else if (contractItem.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
                    Intent it = new Intent(ctx, ContractComplainActivity.class);
                    ctx.startActivity(it);
                } else {
                    Intent it = new Intent(ctx, ContractDetail.class);
                    ctx.startActivity(it);
                }

            }
        });

        return convertView;
    }
}
