package com.example.manhvdse61952.vrc_android.layout.contract;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractItem;

import java.util.ArrayList;
import java.util.List;

public class ManageContractAdapter extends RecyclerView.Adapter<ManageContractAdapter.RecyclerViewHolder>{
    private List<ContractItem> contractItemList = new ArrayList<>();

    @Override
    public ManageContractAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_contract_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    public ManageContractAdapter(List<ContractItem> contractItemList){
        this.contractItemList = contractItemList;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txt_manage_contract_id.setText(contractItemList.get(position).getContractID());
        //////
    }

    @Override
    public int getItemCount() {
        return contractItemList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txt_manage_contract_id, txt_manage_contract_status, txt_manage_contract_start_time,
                txt_manage_contract_end_time, txt_manage_contract_rent_fee, txt_manage_contract_total_fee;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txt_manage_contract_id = (TextView)itemView.findViewById(R.id.txt_manage_contract_id);
            txt_manage_contract_status = (TextView)itemView.findViewById(R.id.txt_manage_contract_status);
            txt_manage_contract_start_time = (TextView)itemView.findViewById(R.id.txt_manage_contract_start_time);
            txt_manage_contract_end_time = (TextView)itemView.findViewById(R.id.txt_manage_contract_end_time);
            txt_manage_contract_rent_fee = (TextView)itemView.findViewById(R.id.txt_manage_contract_rent_fee);
            txt_manage_contract_total_fee = (TextView)itemView.findViewById(R.id.txt_manage_contract_total_fee);
        }
    }

}
