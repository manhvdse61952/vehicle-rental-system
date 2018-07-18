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
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainChat;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractPreFinishOwner extends AppCompatActivity {
    TextView txt_contract_complete_number, txt_contract_complete_overPrice, txt_contract_complete_startTime, txt_contract_complete_endTime, txt_contract_complete_rentTime,
            txt_contract_complete_rentFee, txt_contract_complete_deposit, txt_contract_complete_endRealTime,
            txt_contract_complete_total, txt_contract_complete_overTime;
    EditText edt_contract_complete_inside, edt_contract_complete_outside;
    Button btn_pay;
    ProgressDialog dialog;
    String contractID = "0";
    int totalFee = 0, insideFee = 0, outsideFee = 0, userID = 0;
    String contractStatus = ImmutableValue.CONTRACT_PRE_FINISHED;
    private DatabaseReference dbr;

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
        txt_contract_complete_overTime = (TextView)findViewById(R.id.txt_contract_complete_overTime);
        btn_pay = (Button) findViewById(R.id.btn_pay);

        initLayout();
        edt_contract_complete_inside.addTextChangedListener(convertInsideFeeRealTime(edt_contract_complete_inside));
        edt_contract_complete_outside.addTextChangedListener(convertOutsideFeeRealTime(edt_contract_complete_outside));
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String overTime = txt_contract_complete_overPrice.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ContractPreFinishOwner.this);
                builder.setMessage("Xác nhận sửa tiền phí ?").setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendAction(Integer.parseInt(contractID), overTime, String.valueOf(insideFee), String.valueOf(outsideFee));
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

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
                        int totalFeeTemp = totalFee + insideFee + outsideFee;
                        txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalFeeTemp)));
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
                        int totalFeeTemp = totalFee + insideFee + outsideFee;
                        txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalFeeTemp)));
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

    private void sendAction(final int contractID, String overTime, String insideFee, String outsideFee) {
        if (!contractStatus.equals(ImmutableValue.CONTRACT_ISSUE)) {
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
        } else {
            Retrofit retrofit = RetrofitConfig.getClient();
            final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
            Call<ResponseBody> responseBodyCall = contractAPI.issueChangeFee(contractID, overTime, insideFee, outsideFee);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        long dateValue = new Date().getTime();
                        String currentTime = GeneralController.convertFullTime(dateValue);
                        String childInFDB = GeneralController.generateChildFDB(dateValue);
                        dbr = FirebaseDatabase.getInstance().getReference("Complain").child(String.valueOf(contractID));
                        dbr.child(childInFDB).setValue(new ComplainChat(String.valueOf(contractID), userID,
                                "Tổng tiền phạt thay đổi! Vui lòng vào kiểm tra", currentTime));
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

    }

    @Override
    public void onBackPressed() {
        if (contractStatus.equals(ImmutableValue.CONTRACT_ISSUE)) {
            Intent it = new Intent(ContractPreFinishOwner.this, ContractComplainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        } else {
            Intent it = new Intent(ContractPreFinishOwner.this, ManageContractActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }
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
                        if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_FINISHED)) {
                            Intent it = new Intent(ContractPreFinishOwner.this, ManageContractActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(it);
                        } else {
                            txt_contract_complete_number.setText(contractID);
                            txt_contract_complete_startTime.setText(GeneralController.convertTime(obj.getStartTime()));
                            txt_contract_complete_endTime.setText(GeneralController.convertTime(obj.getEndTime()));
                            txt_contract_complete_endRealTime.setText(GeneralController.convertTime(obj.getEndRealTime()));

                            //Calculate overtime
                            if (obj.getEndRealTime() > obj.getEndTime()) {
                                long diff = obj.getEndRealTime() - obj.getEndTime();
                                long diffDate = TimeUnit.MILLISECONDS.toDays(diff);
                                long diffHour = diff / (60 * 60 * 1000) % 24;
                                long diffMinute = diff / (60 * 1000) % 60;
                                if (diffMinute > 30) {
                                    diffHour = diffHour + 1;
                                }
                                long overTimeFee = (diffHour*obj.getRentFeePerHour()) + (diffDate*obj.getRentFeePerDay());
                                txt_contract_complete_overPrice.setText(overTimeFee + "");
                                txt_contract_complete_overTime.setText(diffDate + " ngày " + diffHour + " giờ");
                            } else {
                                txt_contract_complete_overPrice.setText("0");
                            }
                            txt_contract_complete_rentTime.setText(obj.getRentDay() + " ngày " + obj.getRentHour() + " giờ");
                            int depositFee = Integer.parseInt(obj.getDepositFee());
                            totalFee = Integer.parseInt(obj.getTotalFee());
                            totalFee = totalFee + obj.getPenaltyOverTime();
                            int rentFee = totalFee - depositFee;
                            String rentFeeConvert = String.valueOf(rentFee);
                            txt_contract_complete_rentFee.setText(GeneralController.convertPrice(rentFeeConvert));
                            txt_contract_complete_deposit.setText(GeneralController.convertPrice(obj.getDepositFee()));
                            txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalFee)));
                            if (obj.getInsideFee() != 0) {
                                insideFee = obj.getInsideFee();
                                edt_contract_complete_inside.setText(GeneralController.convertPrice(String.valueOf(obj.getInsideFee())));
                                int totalTemp = totalFee + obj.getInsideFee() + obj.getOutsideFee();
                                txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalTemp)));
                            }
                            if (obj.getOutsideFee() != 0) {
                                outsideFee = obj.getOutsideFee();
                                edt_contract_complete_outside.setText(GeneralController.convertPrice(String.valueOf(obj.getOutsideFee())));
                                int totalTemp = totalFee + obj.getInsideFee() + obj.getOutsideFee();
                                txt_contract_complete_total.setText(GeneralController.convertPrice(String.valueOf(totalTemp)));
                            }
                            contractStatus = obj.getContractStatus();
                            userID = obj.getOwnerID();
                        }
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
