package com.example.manhvdse61952.vrc_android.controller.layout.promotion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.DiscountAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Discount;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

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
        if (obj.getDiscountValue() != 0){
            holder.cbx_discount_item.setChecked(true);
            holder.cbx_discount_item.setEnabled(false);
            holder.swt_discount.setChecked(true);
        } else {
            holder.cbx_discount_item.setChecked(false);
            holder.cbx_discount_item.setEnabled(true);
            holder.swt_discount.setChecked(false);
            holder.swt_discount.setEnabled(false);
        }
        holder.txt_discount_vehicle.setText(obj.getVehicleMaker() + " " + obj.getVehicleModel());
        float discountValue = obj.getDiscountValue() * 100;
        NumberFormat nf = new DecimalFormat("#.####");
        String discountValueConvert = nf.format(discountValue);
        holder.txt_discount_value.setText(discountValueConvert);
        if (obj.getStartDay()!= 0 && obj.getEndDay()!= 0){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = new Date(obj.getStartDay());
            String startDateConvert = sdf.format(startDate);
            holder.txt_discount_start.setText(startDateConvert);
            Date endDate = new Date(obj.getEndDay());
            String endDateConvert = sdf.format(endDate);
            holder.txt_discount_end.setText(endDateConvert);
        } else {
            holder.txt_discount_start.setText("");
            holder.txt_discount_end.setText("");
        }
        holder.cbx_discount_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ImmutableValue.vehicleFrameNumberListGeneral.add(obj.getVehicleFrameNumber());
                } else {
                    ImmutableValue.vehicleFrameNumberListGeneral.remove(obj.getVehicleFrameNumber());
                }
            }
        });

        holder.swt_discount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("Bạn có chắc chắn muốn hủy khuyến mãi của xe này").setCancelable(false)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Retrofit retrofit = RetrofitConfig.getClient();
                                    DiscountAPI discountAPI = retrofit.create(DiscountAPI.class);
                                    Call<ResponseBody> responseBodyCall = discountAPI.removePromotion(obj.getVehicleFrameNumber());
                                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.code() == 200){
                                                Toast.makeText(ctx, "Hủy thành công", Toast.LENGTH_SHORT).show();
                                                atv.finish();
                                                ctx.startActivity(atv.getIntent());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.swt_discount.setChecked(true);
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listDiscount.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbx_discount_item;
        TextView txt_discount_vehicle, txt_discount_value, txt_discount_start,
                txt_discount_end;
        Switch swt_discount;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            cbx_discount_item = (CheckBox)itemView.findViewById(R.id.cbx_discount_item);
            txt_discount_vehicle = (TextView)itemView.findViewById(R.id.txt_discount_vehicle);
            txt_discount_value = (TextView)itemView.findViewById(R.id.txt_discount_value);
            txt_discount_start = (TextView)itemView.findViewById(R.id.txt_discount_start);
            txt_discount_end = (TextView)itemView.findViewById(R.id.txt_discount_end);
            swt_discount = (Switch)itemView.findViewById(R.id.swt_discount);
        }
    }
}
