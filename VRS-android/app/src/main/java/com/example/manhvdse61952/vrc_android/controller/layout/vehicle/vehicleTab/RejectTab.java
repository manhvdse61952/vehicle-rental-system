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

public class RejectTab extends Fragment {
    ListView listView;
    TextView errorTab3;
    VehicleItemTab adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.lvtab3);
        errorTab3 = (TextView)view.findViewById(R.id.errorTab3);
        errorTab3.setVisibility(View.INVISIBLE);
        adapter = new VehicleItemTab(ManageVehicleActivity.listRejected, getActivity());
        listView.setAdapter(adapter);
    }
}
