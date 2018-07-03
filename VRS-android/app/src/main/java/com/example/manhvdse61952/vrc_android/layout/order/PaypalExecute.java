package com.example.manhvdse61952.vrc_android.layout.order;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.ContractAPI;
import com.example.manhvdse61952.vrc_android.layout.contract.ContractDetail;
import com.example.manhvdse61952.vrc_android.layout.vehicle.VehicleDetail;
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractCreate;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.PaypalConfig;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaypalExecute extends AppCompatActivity {

    String totalMoney = "";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_paypal_one);

        //Activity for result
        processPayment();
        Intent it = new Intent(PaypalExecute.this, PayPalService.class);
        it.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(it);
    }

    private void processPayment() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        totalMoney = editor.getString("totalMoney", "100.00");
        double totalMoneyParse = Double.parseDouble(totalMoney);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalMoneyParse), "USD",
                "Thanh toán hợp đồng", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent it = new Intent(PaypalExecute.this, PaymentActivity.class);
        it.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        it.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(it, ImmutableValue.PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(PaypalExecute.this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImmutableValue.PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String details = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(details);
                        JSONObject jsonItem = jsonObject.getJSONObject("response");
                        if (jsonItem.getString("state").equals("approved")){
                            ContractCreate obj = new ContractCreate();
                            SharedPreferences editor1 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            SharedPreferences editor2 = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            //pass value to contract create object
                            obj.setVehicleID(editor2.getString("ID", "aaaaaa"));
                            obj.setUserID(editor1.getInt("userID", 0));
                            obj.setPaypalOrderID(jsonItem.getString("id"));
                            obj.setPaypalOwnerID("");
                            obj.setRentFeePerHourID(editor2.getInt("rentFeePerHourID", 0));
                            obj.setRentFeePerDayID(editor2.getInt("rentFeePerDayID", 0));
                            obj.setStartTime(editor2.getLong("startDayLong", 0));
                            obj.setEndTime(editor2.getLong("endDayLong", 0));
                            obj.setHours(editor2.getInt("totalHour", 0));
                            obj.setDays(editor2.getInt("totalDay", 0));
                            obj.setRentFee(Float.valueOf(editor2.getString("rentFeeMoney", "0.0f")));
                            obj.setReceiveType(editor2.getInt("receiveType", 0));

                            Retrofit retrofit = RetrofitConnect.getClient();
                            final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
                            Call<String> responseBodyCall = contractAPI.createContract(obj);
                            responseBodyCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.code() == 200) {
                                        //Clear temp shared preference
                                        SharedPreferences settings = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                        settings.edit().clear().commit();
                                        //Create new shared preference and pass contractID value
                                        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                                        editor.putString("contractID", response.body().toString().trim());
                                        editor.apply();
                                        Intent it = new Intent(PaypalExecute.this, ContractDetail.class);
                                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(it);
                                    } else {
                                        Toast.makeText(PaypalExecute.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(PaypalExecute.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(PaypalExecute.this, VehicleDetail.class);
                            startActivity(it);
                        }


//                        startActivity(new Intent(PaypalExecute.this, PayPalDetail.class)
//                                .putExtra("PaymentDetails", details)
//                                .putExtra("PaymentAmount", totalMoney));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Intent it = new Intent(PaypalExecute.this, VehicleDetail.class);
                startActivity(it);
                Toast.makeText(this, "Hủy yêu cầu", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Intent it = new Intent(PaypalExecute.this, VehicleDetail.class);
                startActivity(it);
                Toast.makeText(this, "Kiểm tra lại tài khoản paypal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(PaypalExecute.this, VehicleDetail.class);
        startActivity(it);
    }
}
