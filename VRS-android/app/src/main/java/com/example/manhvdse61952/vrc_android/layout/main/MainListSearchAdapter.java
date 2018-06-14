package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.SearchItem;

import java.util.List;

public class MainListSearchAdapter extends BaseAdapter{
    private List<SearchItem> listSearchItem;
    private static LayoutInflater inf = null;
    Context context = null;

    public MainListSearchAdapter(MainListSearch t, List<SearchItem> listSearchItem, Context context){
        this.context = context;
        this.listSearchItem = listSearchItem;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listSearchItem.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        ImageView img_main;
        TextView txt_main_name, txt_main_seat, txt_main_price;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final SearchItem obj = listSearchItem.get(i);
        if (view == null){
            holder = new ViewHolder();
            view = inf.inflate(R.layout.main_slider, null);
            holder.img_main = (ImageView)view.findViewById(R.id.img_main);
            holder.txt_main_name = (TextView)view.findViewById(R.id.txt_main_name);
            holder.txt_main_seat = (TextView)view.findViewById(R.id.txt_main_seat);
            holder.txt_main_price = (TextView)view.findViewById(R.id.txt_main_price);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        //holder.imgSearchItem.setImageResource(view.getResources().getIdentifier(obj.getImg_vehicle(), "drawable", view.getContext().getPackageName()));
        holder.img_main.setImageResource(R.drawable.img_bus_huyndai_univer);
        holder.txt_main_name.setText(obj.getVehicle_name());
        holder.txt_main_seat.setText(obj.getSeat() + " chỗ");
        holder.txt_main_price.setText(obj.getRent_price());
        return view;
    }
}
