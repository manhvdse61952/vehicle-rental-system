package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.vehicleTab;

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
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage.ManageVehicleActivity;

public class WaitingTab extends Fragment{
    ListView listView;
    TextView errorTab2;
    VehicleItemTab adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.lvtab2);
        errorTab2 = (TextView)view.findViewById(R.id.errorTab2);
        errorTab2.setVisibility(View.INVISIBLE);
        adapter = new VehicleItemTab(ManageVehicleActivity.listWaiting, getActivity());
        listView.setAdapter(adapter);
    }
}
