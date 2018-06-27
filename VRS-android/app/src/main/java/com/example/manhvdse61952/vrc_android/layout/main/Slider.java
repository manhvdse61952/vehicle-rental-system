package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.squareup.picasso.Picasso;
public class Slider extends PagerAdapter {

    private LayoutInflater inflater;
    private Context ctx;

    public Slider(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_main_item_slider, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.sliderImgItem);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        SharedPreferences editor = ctx.getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
        String imageFront = editor.getString("imageFront", "");
        String imageBack = editor.getString("imageBack", "");
        String vehicleName = editor.getString("vehicleName", "");
        if (position == 0) {
            if (imageFront.equals("")) {
                Picasso.get().load(R.drawable.img_default_image).into(img);
            } else {
                Picasso.get().load(imageFront).into(img);
            }

        } else if (position == 1) {
            if (imageBack.equals("")) {
                Picasso.get().load(R.drawable.img_default_image).into(img);
            } else {
                Picasso.get().load(imageBack).into(img);
            }
        }
        txtTitle.setText(vehicleName);
        txtTitle.setAllCaps(true);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
