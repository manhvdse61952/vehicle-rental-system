package com.example.manhvdse61952.vrc_android.layout.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.searchModel.SearchItemNew;
import com.example.manhvdse61952.vrc_android.model.apiModel.VehicleInformation_New;
import com.example.manhvdse61952.vrc_android.model.apiModel.Vehicle_New;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

import java.util.ArrayList;
import java.util.List;

public class tab3 extends Fragment {
    ListView listView;
    SearchAdapter adapter;

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
        List<VehicleInformation_New> vehicleInformationNewList = new ArrayList<>();
        List<Vehicle_New> vehicleNewList = new ArrayList<>();

        ImmutableValue importantObj = new ImmutableValue();


        ImmutableValue.searchItemNewList3 = new ArrayList<>();
//        for (int i=0; i< vehicleNewList.size(); i++){
//            VehicleInformation_New checkObj = importantObj.getVehicleInfo(vehicleNewList.get(i).getVehicleInformationID(), vehicleInformationNewList);
//            if (checkObj.getVehicleType().equals("XE_DU_LICH")){
//                SearchItemNew obj = new SearchItemNew();
//                String vehicleName = checkObj.getVehicleMaker() + " " + checkObj.getVehicleModel();
//                int vehicleSeat = checkObj.getSeat();
//                //String address = ImmutableValue.getAddressName(vehicleNewList.get(i).getDistrictID());
//
//                obj.setVehicleName(vehicleName);
//                obj.setVehicleSeat(vehicleSeat);
//                //obj.setAddress(address);
//                obj.setFrameNumber(vehicleNewList.get(i).getFrameNumber());
//                obj.setVehiclePrice(vehicleNewList.get(i).getRentFeePerSlot());
//                obj.setImageLinkFront(vehicleNewList.get(i).getImageLinkFront());
//
//                ImmutableValue.searchItemNewList3.add(obj);
//            }
//        }

        listView = (ListView)view.findViewById(R.id.lvtab3);
        adapter = new SearchAdapter(tab3.this, ImmutableValue.searchItemNewList3, getActivity());
        listView.setAdapter(adapter);

    }
}
