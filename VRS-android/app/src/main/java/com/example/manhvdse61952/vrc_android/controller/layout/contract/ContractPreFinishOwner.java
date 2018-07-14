package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
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
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
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
    TextView txt_contract_complete_number, txt_contract_complete_overPrice, txt_contract_complete_startTime, txt_contract_complete_endTime, txt_contract_complete_rentTime,
            txt_contract_complete_rentFee, txt_contract_complete_deposit, txt_contract_complete_endRealTime,
            txt_contract_complete_total;
    EditText edt_contract_complete_inside, edt_contract_complete_outside;
    Button btn_pay;
    ProgressDialog dialog;
    String contractID = "0";
    int totalFee = 0, insideFee = 0, outsideFee = 0;

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

        //Declare id
        txt_contract_complete_overPrice = (TextView) findViewById(R.id.txt_contract_complete_overPrice);
        edt_contract_complete_inside = (EditText) findViewById(R.id.edt_contract_complete_inside);
        edt_contract_complete_outside = (EditText) findViewById(R.id.edt_contract_complete_outside);
        txt_contract_complete_number = (TextView) findViewById(R.id.txt_contract_complete_number);
        txt_contract_complete_startTime = (TextView) findViewById(R.id.txt_contract_complete_startTime);
        txt_contract_complete_endTime = (TextView) findViewById(R.id.txt_contract_complete_endTime);
        txt_contract_complete_rentTime = (TextView) findViewById(R.id.txt_contract_complete_rentTime);
        txt_contract_complete_rentFee = (TextView) findViewById(R.id.txt_contract_complete_rentFee);
        txt_contract_complete_deposit = (TextView) findViewById(R.id.txt_contract_complete_deposit);
        txt_contract_complete_endRealTime = (TextView) findViewById(R.id.txt_contract_complete_endRealTime);
        txt_contract_complete_total = (TextView) findViewById(R.id.txt_contract_complete_total);
        btn_pay = (Button) findViewById(R.id.btn_pay);

        initLayout();
        edt_contract_complete_inside.addTextChangedListener(convertInsideFeeRealTime(edt_contract_complete_inside));
        edt_contract_complete_outside.addTextChangedListener(convertOutsideFeeRealTime(edt_contract_complete_outside));
        edt_contract_complete_inside.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    txt_contract_complete_total.setText((totalFee + insideFee + outsideFee) + "");
                }
            }
        });
        edt_contract_complete_outside.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    txt_contract_complete_total.setText((totalFee + insideFee + outsideFee) + "");
                }
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String overTime = txt_contract_complete_overPrice.getText().toString();
                sendAction(Integer.parseInt(contractID), overTime, String.valueOf(insideFee), String.valueOf(outsideFee));

            }
        });
    }

    private TextWatcher convertInsideFeeRealTime(final EditText edt) {
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
                    if (!originalString.equals("")) {
                        insideFee = Integer.parseInt(originalString);
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

    private TextWatcher convertOutsideFeeRealTime(final EditText edt) {
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
                    if (!originalString.equals("")) {
                        outsideFee = Integer.parseInt(originalString);
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

    private void sendAction(int contractID, String overTime, String insideFee, String outsideFee) {
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.preFinishContract(contractID, overTime, insideFee, outsideFee);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
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

    private void initLayout() {
        dialog = ProgressDialog.show(ContractPreFinishOwner.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        contractID = editor.getString(ImmutableValue.MAIN_contractID, "0");
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ContractItem> responseBodyCall = contractAPI.findContractByID(contractID);
        responseBodyCall.enqueue(new Callback<ContractItem>() {
            @Override
            public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        ContractItem obj = response.body();
                        txt_contract_complete_number.setText(contractID);
                        txt_contract_complete_startTime.setText(GeneralController.convertTime(obj.getStartTime()));
                        txt_contract_complete_endTime.setText(GeneralController.convertTime(obj.getEndTime()));
                        txt_contract_complete_rentTime.setText(obj.getRentDay() + " ngày " + obj.getRentHour() + " giờ");
                        int depositFee = Integer.parseInt(obj.getDepositFee());
                        totalFee = Integer.parseInt(obj.getTotalFee());
                        int rentFee = totalFee - depositFee;
                        String rentFeeConvert = String.valueOf(rentFee);
                        txt_contract_complete_rentFee.setText(GeneralController.convertPrice(rentFeeConvert));
                        txt_contract_complete_deposit.setText(GeneralController.convertPrice(obj.getDepositFee()));
                        txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalFee)));
                    } else {
                        Toast.makeText(ContractPreFinishOwner.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ContractPreFinishOwner.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractPreFinishOwner.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
