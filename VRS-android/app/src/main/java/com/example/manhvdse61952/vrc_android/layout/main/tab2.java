package com.example.manhvdse61952.vrc_android.layout.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.searchModel.SearchItemNew;
import com.example.manhvdse61952.vrc_android.model.apiModel.VehicleInformation_New;
import com.example.manhvdse61952.vrc_android.model.apiModel.Vehicle_New;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

import java.util.ArrayList;
import java.util.List;

public class tab2 extends Fragment {
    ListView listView;
    SearchAdapter adapter;
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
            adapter = new SearchAdapter(tab2.this, activity_main_2.listPersonalCar, getActivity());
            listView.setAdapter(adapter);
        }
    }
}
