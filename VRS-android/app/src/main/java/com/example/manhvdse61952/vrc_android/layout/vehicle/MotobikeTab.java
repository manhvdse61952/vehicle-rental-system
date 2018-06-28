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

public class MotobikeTab extends Fragment {
    ListView listView;
    VehicleItem adapter;
    TextView errorTab1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ////////////////// Init to list view ///////////////////
        errorTab1 = (TextView)view.findViewById(R.id.errorTab1);
        listView = (ListView)view.findViewById(R.id.lvtab1);

        if (activity_main_2.listMotorbike.size() == 0){
            errorTab1.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            errorTab1.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new VehicleItem(MotobikeTab.this, activity_main_2.listMotorbike, getActivity());
            listView.setAdapter(adapter);
        }
    }
}
