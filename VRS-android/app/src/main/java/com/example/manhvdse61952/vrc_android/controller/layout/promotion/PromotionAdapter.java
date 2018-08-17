package com.example.manhvdse61952.vrc_android.controller.layout.promotion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.DiscountAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Discount;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.RecyclerViewHolder> {
    private List<Discount> listDiscount;
    private Context ctx;
    private Activity atv;
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_discount_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    public PromotionAdapter(List<Discount> listDiscount, Context ctx, Activity atv) {
        this.listDiscount = listDiscount;
        this.ctx = ctx;
        this.atv = atv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final Discount obj = listDiscount.get(position);
        holder.txt_discount_name.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
        float discountValue = obj.getDiscountValue() * 100;
        NumberFormat nf = new DecimalFormat("#.####");
        final String discountValueConvert = nf.format(discountValue);
        if (discountValueConvert.equals("0")){
            holder.txt_discount_value.setText("Không có khuyến mãi");
            holder.txt_discount_value.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.txt_discount_value.setText("Khuyến mãi " + discountValueConvert + " %");
            holder.txt_discount_value.setTextColor(Color.parseColor("#1B5E20"));
        }

        if (obj.getStartDay()!= 0 && obj.getEndDay()!= 0){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = new Date(obj.getStartDay());
            String startDateConvert = sdf.format(startDate);
            Date endDate = new Date(obj.getEndDay());
            String endDateConvert = sdf.format(endDate);
            holder.txt_discount_time.setText(startDateConvert + " - " + endDateConvert);
            holder.img_calendar.setVisibility(View.VISIBLE);
        } else {
            holder.img_calendar.setVisibility(View.INVISIBLE);
            holder.txt_discount_time.setVisibility(View.INVISIBLE);
        }
        if(obj.getImageLinkFront().equals("")) {
            Picasso.get().load(R.drawable.img_default_image).into(holder.img_discount);
        } else {
            Picasso.get().load(obj.getImageLinkFront()).into(holder.img_discount);
        }
        holder.ln_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.DISCOUNT_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.DISCOUNT_vehicle_frame, obj.getVehicleFrameNumber());
                editor.putString(ImmutableValue.DISCOUNT_imageFront, obj.getImageLinkFront());
                editor.putString(ImmutableValue.DISCOUNT_vehicle_name, obj.getVehicleMaker() + " " + obj.getVehicleModel());
                editor.putString(ImmutableValue.DISCOUNT_value, discountValueConvert);
                if (obj.getStartDay()!= 0 && obj.getEndDay()!= 0){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date startDate = new Date(obj.getStartDay());
                    String startDateConvert = sdf.format(startDate);
                    Date endDate = new Date(obj.getEndDay());
                    String endDateConvert = sdf.format(endDate);
                    editor.putString(ImmutableValue.DISCOUNT_date, startDateConvert + " - " + endDateConvert);
                } else {
                    editor.putString(ImmutableValue.DISCOUNT_date, "0");
                }
                editor.apply();
                Intent it = new Intent(ctx, PromotionDetail.class);
                atv.startActivityForResult(it, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDiscount.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ln_discount;
        ImageView img_discount, img_calendar;
        TextView txt_discount_name, txt_discount_value, txt_discount_time;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ln_discount = (LinearLayout)itemView.findViewById(R.id.ln_discount);
            img_discount = (ImageView)itemView.findViewById(R.id.img_discount);
            img_calendar = (ImageView)itemView.findViewById(R.id.img_calendar);
            txt_discount_name = (TextView)itemView.findViewById(R.id.txt_discount_name);
            txt_discount_time = (TextView)itemView.findViewById(R.id.txt_discount_time);
            txt_discount_value = (TextView)itemView.findViewById(R.id.txt_discount_value);

        }
    }
}
