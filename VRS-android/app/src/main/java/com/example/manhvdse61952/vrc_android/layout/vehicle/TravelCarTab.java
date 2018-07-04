package com.example.manhvdse61952.vrc_android.layout.vehicle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;

public class TravelCarTab extends Fragment {
    ListView listView;
    VehicleItem adapter;
    TextView errorTab3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ////////////////// Init to list view ///////////////////
        errorTab3 = (TextView)view.findViewById(R.id.errorTab3);
        listView = (ListView)view.findViewById(R.id.lvtab3);

        if (MainActivity.listTravelCar.size() == 0){
            errorTab3.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            errorTab3.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new VehicleItem(TravelCarTab.this, MainActivity.listTravelCar, getActivity());
            listView.setAdapter(adapter);
        }

    }
}
