package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractPreFinishOwner extends AppCompatActivity {
    TextView txt_contract_complete_number, txt_contract_complete_overPrice;
    EditText edt_contract_complete_inside, edt_contract_complete_outside;
    Button btn_pay;
    int contractID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_finish_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        txt_contract_complete_overPrice = (TextView)findViewById(R.id.txt_contract_complete_overPrice);
        edt_contract_complete_inside = (EditText)findViewById(R.id.edt_contract_complete_inside);
        edt_contract_complete_outside = (EditText)findViewById(R.id.edt_contract_complete_outside);
        txt_contract_complete_number = (TextView)findViewById(R.id.txt_contract_complete_number);
        btn_pay = (Button)findViewById(R.id.btn_pay);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        contractID = Integer.parseInt(editor.getString(ImmutableValue.MAIN_contractID, "0"));
        txt_contract_complete_number.setText(contractID + "");

        //edt_contract_complete_inside.addTextChangedListener(convertPriceRealTime(edt_contract_complete_inside));
        //edt_contract_complete_outside.addTextChangedListener(convertPriceRealTime(edt_contract_complete_outside));



        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String overTime = txt_contract_complete_overPrice.getText().toString();
                String insideFee = edt_contract_complete_inside.getText().toString().trim() + "";
                String outsideFee = edt_contract_complete_outside.getText().toString().trim() + "";
                if (insideFee.equals("")){
                    insideFee = "0";
                }
                if (outsideFee.equals("")){
                    outsideFee = "0";
                }
//                if ((insideFee.length() > 0 && insideFee.length() < 6)||
//                        (outsideFee.length() > 0 && outsideFee.length() < 6)){
//                    Toast.makeText(ContractPreFinishOwner.this, "Số tiền từ 10,000 trở lên", Toast.LENGTH_SHORT).show();
//                } else {
//
//                }
                sendAction(contractID, overTime, insideFee, outsideFee);

            }
        });
    }

    private TextWatcher convertPriceRealTime(final EditText edt) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt.setText(formattedString);
                    edt.setSelection(edt.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt.addTextChangedListener(this);
            }
        };
    }

    private void sendAction(int contractID, String overTime, String insideFee, String outsideFee){
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.preFinishContract(contractID, overTime, insideFee, outsideFee);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Intent it = new Intent(ContractPreFinishOwner.this, MainActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(ContractPreFinishOwner.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ContractPreFinishOwner.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
