package com.example.manhvdse61952.vrc_android.controller.layout.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail.VehicleDetail;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractCreate;
import com.example.manhvdse61952.vrc_android.remote.PaypalConfig;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
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
    ProgressDialog dialog;
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
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        totalMoney = editor.getString(ImmutableValue.MAIN_totalMoney, "100.00");
        double totalMoneyParse = Double.parseDouble(totalMoney);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalMoneyParse), "USD",
                "Thanh toán hợp đồng", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent it = new Intent(PaypalExecute.this, PaymentActivity.class);
        it.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        it.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(it, PermissionDevice.PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(PaypalExecute.this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionDevice.PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String details = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(details);
                        JSONObject jsonItem = jsonObject.getJSONObject("response");
                        if (jsonItem.getString("state").equals("approved")) {
                            dialog = ProgressDialog.show(PaypalExecute.this, "Đang xử lý",
                                    "Vui lòng đợi ...", true);
                            ContractCreate obj = new ContractCreate();
                            SharedPreferences editor1 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            //pass value to contract create object
                            obj.setUserID(editor1.getInt(ImmutableValue.HOME_userID, 0));
                            obj.setVehicleID(editor2.getString(ImmutableValue.MAIN_contractID, "aaaaaa"));
                            obj.setPaypalOrderID(jsonItem.getString("id"));
                            obj.setPaypalUserID("");
                            obj.setRentFeePerHourID(editor2.getInt(ImmutableValue.MAIN_rentFeePerHourID, 0));
                            obj.setRentFeePerDayID(editor2.getInt(ImmutableValue.MAIN_rentFeePerDayID, 0));
                            obj.setStartTime(editor2.getLong(ImmutableValue.MAIN_startDayLong, 0));
                            obj.setEndTime(editor2.getLong(ImmutableValue.MAIN_endDayLong, 0));
                            obj.setHours(editor2.getInt(ImmutableValue.MAIN_totalHour, 0));
                            obj.setDays(editor2.getInt(ImmutableValue.MAIN_totalDay, 0));
                            obj.setRentFee(Float.valueOf(editor2.getString(ImmutableValue.MAIN_rentFeeMoney, "0.0f")));
                            obj.setReceiveType(editor2.getInt(ImmutableValue.MAIN_receiveType, 0));

                            Retrofit retrofit = RetrofitConfig.getClient();
                            final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
                            Call<String> responseBodyCall = contractAPI.createContract(obj);
                            responseBodyCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.code() == 200) {
                                        //Clear temp shared preference
                                        SharedPreferences settings = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                        settings.edit().clear().commit();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PaypalExecute.this);
                                        builder.setMessage("Tạo hợp đồng thành công! Hãy vào menu -> kiểm tra hợp đồng để biết thêm chi tiết").setCancelable(false)
                                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent it = new Intent(PaypalExecute.this, MainActivity.class);
                                                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(it);
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        dialog.setCanceledOnTouchOutside(false);

                                    } else {
                                        Toast.makeText(PaypalExecute.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(PaypalExecute.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(PaypalExecute.this, VehicleDetail.class);
                            startActivity(it);
                        }

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
