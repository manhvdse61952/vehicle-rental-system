package com.example.manhvdse61952.vrc_android.layout.order;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.vehicle.VehicleDetail;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.PaypalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaypalLogin extends AppCompatActivity {

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
        Intent it = new Intent(PaypalLogin.this, PayPalService.class);
        it.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(it);
    }

    private void processPayment() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        totalMoney = editor.getString("totalMoney", "100.00");
        double totalMoneyParse = Double.parseDouble(totalMoney);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalMoneyParse), "USD",
                "Thanh toán hợp đồng", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent it = new Intent(PaypalLogin.this, PaymentActivity.class);
        it.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        it.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(it, ImmutableValue.PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(PaypalLogin.this, PayPalService.class));
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
                        startActivity(new Intent(PaypalLogin.this, PayPalDetail.class)
                                .putExtra("PaymentDetails", details)
                                .putExtra("PaymentAmount", totalMoney));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Intent it = new Intent(PaypalLogin.this, VehicleDetail.class);
                startActivity(it);
                Toast.makeText(this, "Hủy yêu cầu", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Intent it = new Intent(PaypalLogin.this, VehicleDetail.class);
                startActivity(it);
                Toast.makeText(this, "Kiểm tra lại tài khoản paypal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(PaypalLogin.this, VehicleDetail.class);
        startActivity(it);
    }
}
