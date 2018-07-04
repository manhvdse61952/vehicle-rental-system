package com.example.manhvdse61952.vrc_android.layout.contract;

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
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

import java.util.ArrayList;
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
        holder.txt_manage_contract_id.setText(obj.getContractID() + "");
        holder.txt_manage_contract_status.setText(obj.getContractStatus() + "");
        holder.txt_manage_contract_start_time.setText(ImmutableValue.convertTime(obj.getStartTime()));
        holder.txt_manage_contract_end_time.setText(ImmutableValue.convertTime(obj.getEndTime()));
        int depositFee = Integer.parseInt(obj.getDepositFee());
        int totalFee = Integer.parseInt(obj.getTotalFee());
        int rentFee = totalFee - depositFee;
        holder.txt_manage_contract_rent_fee.setText(ImmutableValue.convertPrice(String.valueOf(rentFee)));
        holder.txt_manage_contract_total_fee.setText(ImmutableValue.convertPrice(obj.getTotalFee()));
        holder.ln_manage_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString("contractID", obj.getContractID());
                editor.apply();
                Intent it = new Intent(ctx, ContractDetail.class);
                ctx.startActivity(it);
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
