package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail;

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
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.squareup.picasso.Picasso;
public class ImageSlider extends PagerAdapter {

    private LayoutInflater inflater;
    private Context ctx;

    public ImageSlider(Context ctx) {
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
        TextView txtCount = (TextView)v.findViewById(R.id.txtCount);
        SharedPreferences editor = ctx.getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
        String imageFront = editor.getString(ImmutableValue.MAIN_vehicleImgFront, "");
        String imageBack = editor.getString(ImmutableValue.MAIN_vehicleImgBack, "");
        String vehicleName = editor.getString(ImmutableValue.MAIN_vehicleName, "");
        if (position == 0) {
            txtCount.setText("1/2");
            if (imageFront.equals("")) {
                Picasso.get().load(R.drawable.img_default_image).into(img);
            } else {
                Picasso.get().load(imageFront).into(img);
            }

        } else if (position == 1) {
            txtCount.setText("2/2");
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
