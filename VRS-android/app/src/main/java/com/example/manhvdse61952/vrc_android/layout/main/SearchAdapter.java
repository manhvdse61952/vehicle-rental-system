package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.SearchItemNew;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private List<SearchItemNew> searchItemList;
    private static LayoutInflater inf = null;
    Context context = null;

    public SearchAdapter(tab1 t, List<SearchItemNew> searchItemList, Context context){
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchAdapter(tab2 t, List<SearchItemNew> searchItemList, Context context){
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchAdapter(tab3 t, List<SearchItemNew> searchItemList, Context context){
        this.context = context;
        this.searchItemList = searchItemList;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private class ViewHolder{
        ImageView img_main;
        TextView txt_main_name, txt_main_seat, txt_main_address, txt_main_price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final SearchItemNew obj = searchItemList.get(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inf.inflate(R.layout.custom_search_item, null);
            viewHolder.img_main = (ImageView)convertView.findViewById(R.id.img_main);
            viewHolder.txt_main_name = (TextView)convertView.findViewById(R.id.txt_main_name);
            viewHolder.txt_main_seat = (TextView)convertView.findViewById(R.id.txt_main_seat);
            viewHolder.txt_main_address = (TextView)convertView.findViewById(R.id.txt_main_address);
            viewHolder.txt_main_price = (TextView)convertView.findViewById(R.id.txt_main_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.img_main.setImageResource(R.drawable.img_bus_huyndai_univer);
        viewHolder.txt_main_name.setText(obj.getVehicleName());
        viewHolder.txt_main_seat.setText(obj.getVehicleSeat() + " chỗ");
        viewHolder.txt_main_address.setText(obj.getAddress());
        viewHolder.txt_main_price.setText(obj.getVehiclePrice());
        return convertView;
    }
}
