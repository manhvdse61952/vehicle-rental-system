package com.example.manhvdse61952.vrc_android.layout.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractCreate;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PayPalDetail extends AppCompatActivity {
    TextView txt_paypal_id, txt_paypal_money, txt_paypal_status;
    Button btn_paypal_next;
    String paypalOrderID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_paypal_detail);

        txt_paypal_id = (TextView) findViewById(R.id.txt_paypal_id);
        txt_paypal_money = (TextView) findViewById(R.id.txt_paypal_money);
        txt_paypal_status = (TextView) findViewById(R.id.txt_paypal_status);
        btn_paypal_next = (Button) findViewById(R.id.btn_paypal_next);

        Intent it = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(it.getStringExtra("PaymentDetails"));
            showDetail(jsonObject.getJSONObject("response"), it.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_paypal_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContractCreate obj = new ContractCreate();
                SharedPreferences editor1 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                SharedPreferences editor2 = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                //pass value to contract create object
                obj.setVehicleID(editor2.getString("ID", "aaaaaa"));
                obj.setUserID(editor1.getInt("userID", 0));
                obj.setPaypalOrderID(paypalOrderID);
                obj.setPaypalOwnerID("");
                obj.setRentFeePerHourID(editor2.getInt("rentFeePerHourID", 0));
                obj.setRentFeePerDayID(editor2.getInt("rentFeePerDayID", 0));
                obj.setStartTime(editor2.getLong("startDayLong", 0));
                obj.setEndTime(editor2.getLong("endDayLong", 0));
                obj.setHours(editor2.getInt("totalHour", 0));
                obj.setDays(editor2.getInt("totalDay", 0));
                obj.setRentFee(Float.valueOf(editor2.getString("rentFeeMoney", "0.0f")));

                Retrofit retrofit = RetrofitConnect.getClient();
                final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
                Call<ContractCreate> responseBodyCall = contractAPI.createContract(obj);
                responseBodyCall.enqueue(new Callback<ContractCreate>() {
                    @Override
                    public void onResponse(Call<ContractCreate> call, Response<ContractCreate> response) {
                            if (response.code() == 200){
                                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                                editor.putString("contractID", response.body().toString().trim());
                                Intent it = new Intent(PayPalDetail.this, OrderDetailActivity.class);
                                startActivity(it);
                            } else {
                                Toast.makeText(PayPalDetail.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }



                    }

                    @Override
                    public void onFailure(Call<ContractCreate> call, Throwable t) {
                        Toast.makeText(PayPalDetail.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showDetail(JSONObject jsonObject, String totalMoney) {
        try {
            txt_paypal_id.setText(jsonObject.getString("id"));
            paypalOrderID = jsonObject.getString("id");
            String status = jsonObject.getString("state");
            if (status.equals("approved")) {
                txt_paypal_status.setText("Thành công");
            }
            txt_paypal_money.setText(totalMoney);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Hợp đồng đã thanh toán xong! Bấm tiếp tục để xem chi tiết hợp đồng", Toast.LENGTH_SHORT).show();
    }

}
