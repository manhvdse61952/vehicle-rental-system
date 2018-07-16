package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractPreFinishCustomer extends AppCompatActivity {
    Button btn_pay, btn_complain;
    TextView txt_contract_complete_number, txt_contract_complete_overPrice, txt_contract_complete_startTime, txt_contract_complete_endTime, txt_contract_complete_rentTime,
            txt_contract_complete_rentFee, txt_contract_complete_deposit, txt_contract_complete_endRealTime,
            txt_contract_complete_total, txt_contract_insideFee, txt_contract_outsideFee;
    String contractID = "0";
    String contractStatus = ImmutableValue.CONTRACT_PRE_FINISHED;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_finish_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        btn_pay = (Button)findViewById(R.id.btn_pay);
        btn_complain = (Button)findViewById(R.id.btn_complain);
        txt_contract_complete_number = (TextView) findViewById(R.id.txt_contract_complete_number);
        txt_contract_complete_startTime = (TextView) findViewById(R.id.txt_contract_complete_startTime);
        txt_contract_complete_endTime = (TextView) findViewById(R.id.txt_contract_complete_endTime);
        txt_contract_complete_rentTime = (TextView) findViewById(R.id.txt_contract_complete_rentTime);
        txt_contract_complete_rentFee = (TextView) findViewById(R.id.txt_contract_complete_rentFee);
        txt_contract_complete_deposit = (TextView) findViewById(R.id.txt_contract_complete_deposit);
        txt_contract_complete_endRealTime = (TextView) findViewById(R.id.txt_contract_complete_endRealTime);
        txt_contract_complete_total = (TextView) findViewById(R.id.txt_contract_complete_total);
        txt_contract_complete_overPrice = (TextView) findViewById(R.id.txt_contract_complete_overPrice);
        txt_contract_insideFee = (TextView)findViewById(R.id.txt_contract_insideFee);
        txt_contract_outsideFee = (TextView)findViewById(R.id.txt_contract_outsideFee);

        initLayout();

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payAction();
            }
        });

        btn_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it  = new Intent(ContractPreFinishCustomer.this, ContractComplainActivity.class);
                startActivity(it);
            }
        });
    }

    private void payAction(){
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int contractID = Integer.parseInt(editor2.getString(ImmutableValue.MAIN_contractID, "0"));
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.finishContract(contractID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    String successResult = response.body().toString();
                    if (!successResult.equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContractPreFinishCustomer.this);
                        builder.setMessage("Hợp đồng hoàn tất! Cảm ơn bạn đã sử dụng VRS").setCancelable(false)
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences settings = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                        settings.edit().clear().commit();
                                        Intent it = new Intent(ContractPreFinishCustomer.this, MainActivity.class);
                                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(it);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                } else {
                    Toast.makeText(ContractPreFinishCustomer.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ContractPreFinishCustomer.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLayout(){
        dialog = ProgressDialog.show(ContractPreFinishCustomer.this, "Đang xử lý",
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
                        txt_contract_complete_endRealTime.setText(GeneralController.convertTime(obj.getEndRealTime()));
                        txt_contract_complete_rentTime.setText(obj.getRentDay() + " ngày " + obj.getRentHour() + " giờ");
                        txt_contract_complete_overPrice.setText(obj.getPenaltyOverTime() + "");
                        int depositFee = Integer.parseInt(obj.getDepositFee());
                        int totalFee = Integer.parseInt(obj.getTotalFee());
                        int rentFee = totalFee - depositFee;
                        String rentFeeConvert = String.valueOf(rentFee);
                        totalFee = totalFee + obj.getInsideFee() + obj.getOutsideFee() + obj.getPenaltyOverTime();
                        txt_contract_complete_rentFee.setText(GeneralController.convertPrice(rentFeeConvert));
                        txt_contract_complete_deposit.setText(GeneralController.convertPrice(obj.getDepositFee()));
                        txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalFee)));
                        txt_contract_insideFee.setText(GeneralController.convertPrice(String.valueOf(obj.getInsideFee())));
                        txt_contract_outsideFee.setText(GeneralController.convertPrice(String.valueOf(obj.getOutsideFee())));
                        contractStatus = obj.getContractStatus();
                        if (contractStatus.equals(ImmutableValue.CONTRACT_ISSUE)){
                            btn_complain.setVisibility(View.INVISIBLE);
                        }

                    } else {
                        Toast.makeText(ContractPreFinishCustomer.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ContractPreFinishCustomer.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractPreFinishCustomer.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (contractStatus.equals(ImmutableValue.CONTRACT_ISSUE)){
            Intent it = new Intent(ContractPreFinishCustomer.this, ContractComplainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        } else {
            Intent it = new Intent(ContractPreFinishCustomer.this, ManageContractActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }

    }
}
