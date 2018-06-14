package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;

public class SliderMotorbycle extends PagerAdapter {
    private int[] listImage = {R.drawable.img_motorcycle_airblade, R.drawable.img_motorcycle_shmode, R.drawable.img_motorcycle_exciter};
    private String[] listName = {"Honda Airblade 2017", "SH-mode trắng", "Exciter tay côn 2015"};
    private String[] listSeat = {"2 chỗ", "2 chỗ", "2 chỗ"};
    private String[] listPrice = {"100.000", "200.000", "150.000"};

    private LayoutInflater inflater;
    private Context ctx;

    public SliderMotorbycle(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listImage.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.main_slider, container, false);
        ImageView img = (ImageView)v.findViewById(R.id.img_main);
        TextView name = (TextView)v.findViewById(R.id.txt_main_name);
        TextView seat = (TextView)v.findViewById(R.id.txt_main_seat);
        TextView price = (TextView)v.findViewById(R.id.txt_main_price);

        img.setImageResource(listImage[position]);
        name.setText(listName[position]);
        seat.setText(listSeat[position]);
        price.setText(listPrice[position]);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
