package com.example.manhvdse61952.vrc_test_1.layout.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_test_1.R;

public class Slider extends PagerAdapter {

    private int[] listImage = {R.drawable.xe_oto, R.drawable.img_customer, R.drawable.img_owner, R.drawable.ic_user};

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
        img.setImageResource(listImage[position]);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
