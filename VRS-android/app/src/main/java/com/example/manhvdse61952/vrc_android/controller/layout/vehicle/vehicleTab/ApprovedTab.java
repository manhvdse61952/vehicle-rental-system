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

public class ApprovedTab extends Fragment {
    ListView listView;
    TextView errorTab1;
    VehicleItemTab adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.lvtab1);
        errorTab1 = (TextView)view.findViewById(R.id.errorTab1);
        errorTab1.setVisibility(View.INVISIBLE);
        adapter = new VehicleItemTab(ManageVehicleActivity.listApproved, getActivity());
        listView.setAdapter(adapter);
    }
}
