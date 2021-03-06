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
        ImageView imgSearchItem;
        TextView txtSearchItemName, txtSearchItemSeat, txtSearchItemPrice, txtSearchItemDescription;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final SearchItem obj = listSearchItem.get(i);
        if (view == null){
            holder = new ViewHolder();
            view = inf.inflate(R.layout.activity_main_list_search_item, null);
            holder.imgSearchItem = (ImageView)view.findViewById(R.id.imgSearchItem);
            holder.txtSearchItemName = (TextView)view.findViewById(R.id.txtSearchItemName);
            holder.txtSearchItemSeat = (TextView)view.findViewById(R.id.txtSearchItemSeat);
            holder.txtSearchItemPrice = (TextView)view.findViewById(R.id.txtSearchItemPrice);
            holder.txtSearchItemDescription = (TextView)view.findViewById(R.id.txtSearchItemDescription);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        //holder.imgSearchItem.setImageResource(view.getResources().getIdentifier(obj.getImg_vehicle(), "drawable", view.getContext().getPackageName()));
        holder.imgSearchItem.setImageResource(R.drawable.xe_oto);
        holder.txtSearchItemName.setText(obj.getVehicle_name());
        holder.txtSearchItemSeat.setText(obj.getSeat() + " chỗ");
        holder.txtSearchItemPrice.setText(obj.getRent_price());
        holder.txtSearchItemDescription.setText(obj.getVehicle_description());
        return view;
    }
}
