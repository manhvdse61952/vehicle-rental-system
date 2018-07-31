package com.example.manhvdse61952.vrc_android.controller.layout.contract.contract_tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ManageContractActivity;

public class FinishContractTab extends Fragment{
    ListView listView;
    TextView errorTab2;
    ContractItemTab adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorTab2 = (TextView)view.findViewById(R.id.errorTab2);
        listView = (ListView)view.findViewById(R.id.lvtab2);

        if (ManageContractActivity.listContractFinish.size() == 0){
            errorTab2.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            errorTab2.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new ContractItemTab(ManageContractActivity.listContractFinish, getActivity());
            listView.setAdapter(adapter);
        }
    }
}
