package com.example.manhvdse61952.vrc_android.controller.layout.contract.contract_tab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ManageContractActivity;

public class ExecuteContractTab extends Fragment{
    ListView listView;
    TextView errorTab1;
    ContractItemTab adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorTab1 = (TextView)view.findViewById(R.id.errorTab1);
        listView = (ListView)view.findViewById(R.id.lvtab1);

        if (ManageContractActivity.listContractAnother.size() == 0){
            errorTab1.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            errorTab1.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new ContractItemTab(ManageContractActivity.listContractAnother, getActivity());
            listView.setAdapter(adapter);
        }
    }
}
