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

public class SliderPersonalCar extends PagerAdapter {
    private int[] listImage = {R.drawable.img_car_toyota_camry, R.drawable.img_car_ford_everest, R.drawable.img_car_toyota_innova};
    private String[] listName = {"Toyota camry 2.4", "Ford everest đen", "Toyota innova 2016"};
    private String[] listSeat = {"4 chỗ", "7 chỗ", "7 chỗ"};
    private String[] listPrice = {"400.000", "350.000", "370.000"};

    private LayoutInflater inflater;
    private Context ctx;

    public SliderPersonalCar(Context ctx) {
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
