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
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;

public class CarTab extends Fragment {
    ListView listView;
    VehicleItem adapter;
    TextView errorTab2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ////////////////// Init to list view ///////////////////
        errorTab2 = (TextView)view.findViewById(R.id.errorTab2);
        listView = (ListView)view.findViewById(R.id.lvtab2);

        if (activity_main_2.listPersonalCar.size() == 0){
            errorTab2.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            errorTab2.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new VehicleItem(CarTab.this, activity_main_2.listPersonalCar, getActivity());
            listView.setAdapter(adapter);
        }
    }
}
