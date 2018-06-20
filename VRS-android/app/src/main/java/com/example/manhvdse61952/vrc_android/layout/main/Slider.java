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

public class Slider extends PagerAdapter {

    private int[] listImage = {R.drawable.img_car_toyota_innova, R.drawable.img_car_toyota_camry, R.drawable.img_bus_mercedes_sprinter};

    private LayoutInflater inflater;
    private Context ctx;

    public Slider(Context ctx) {
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
        View v = inflater.inflate(R.layout.activity_main_item_slider, container, false);
        ImageView img = (ImageView)v.findViewById(R.id.sliderImgItem);
        TextView txtTitle = (TextView)v.findViewById(R.id.txtTitle);
        img.setImageResource(listImage[position]);
        txtTitle.setText("Toyota Fortuner");
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
