package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.searchModel.MainItemModel;
import com.example.manhvdse61952.vrc_android.model.searchModel.SearchItemNew;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchAdapter extends BaseAdapter {
    private List<SearchItemNew> searchItemList;
    private static LayoutInflater inf = null;
    Context context = null;

    public SearchAdapter(tab1 t, List<SearchItemNew> searchItemList, Context context) {
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchAdapter(tab2 t, List<SearchItemNew> searchItemList, Context context) {
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchAdapter(tab3 t, List<SearchItemNew> searchItemList, Context context) {
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return searchItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView img_main;
        TextView txt_main_name, txt_main_seat, txt_main_address, txt_main_price;
        RelativeLayout search_layout_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final SearchItemNew obj = searchItemList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inf.inflate(R.layout.custom_search_item, null);
            viewHolder.img_main = (ImageView) convertView.findViewById(R.id.img_main);
            viewHolder.txt_main_name = (TextView) convertView.findViewById(R.id.txt_main_name);
            viewHolder.txt_main_seat = (TextView) convertView.findViewById(R.id.txt_main_seat);
            viewHolder.txt_main_address = (TextView) convertView.findViewById(R.id.txt_main_address);
            viewHolder.txt_main_price = (TextView) convertView.findViewById(R.id.txt_main_price);
            viewHolder.search_layout_item = (RelativeLayout) convertView.findViewById(R.id.search_layout_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (obj.getImageLinkFront().trim().equals("")) {
            Picasso.get().load(R.drawable.img_default_image).into(viewHolder.img_main);
        } else {
            Picasso.get().load(obj.getImageLinkFront()).into(viewHolder.img_main);
        }
        viewHolder.txt_main_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
        viewHolder.txt_main_seat.setText(obj.getSeat() + " chỗ");

        viewHolder.txt_main_address.setText(obj.getDistrictID() + "");
        if (obj.getVehicleType().equals("XE_MAY")) {
            String price = ImmutableValue.convertPrice(obj.getRentFeePerHours());
            viewHolder.txt_main_price.setText(price);
        } else {
            String price = ImmutableValue.convertPrice(obj.getRentFeePerSlot());
            viewHolder.txt_main_price.setText(price);
        }

        viewHolder.search_layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MainItem.class);
                it.putExtra("ID", obj.getFrameNumber());
                it.putExtra("seat", obj.getSeat() + "");
                it.putExtra("type", obj.getVehicleType());
                context.startActivity(it);
            }
        });


        return convertView;
    }
}
