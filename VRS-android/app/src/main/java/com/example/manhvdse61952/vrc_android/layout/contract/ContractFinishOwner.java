package com.example.manhvdse61952.vrc_android.layout.contract;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.ContractAPI;
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.apiModel.ContractFinish;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractFinishOwner extends AppCompatActivity {
    TextView txt_contract_complete_number;
    EditText edt_contract_complete_overTime, edt_contract_complete_inside, edt_contract_complete_outside;
    Button btn_pay;
    int contractID = 0;
    String overTime = "", inside = "", outside = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_finish_owner);
        edt_contract_complete_overTime = (EditText)findViewById(R.id.edt_contract_complete_overTime);
        edt_contract_complete_inside = (EditText)findViewById(R.id.edt_contract_complete_inside);
        edt_contract_complete_outside = (EditText)findViewById(R.id.edt_contract_complete_outside);
        txt_contract_complete_number = (TextView)findViewById(R.id.txt_contract_complete_number);
        btn_pay = (Button)findViewById(R.id.btn_pay);

        SharedPreferences editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        contractID = Integer.parseInt(editor.getString("contractID", "0"));
        txt_contract_complete_number.setText(editor.getString("contractID", "0"));
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overTime = edt_contract_complete_overTime.getText().toString();
                inside = edt_contract_complete_inside.getText().toString();
                outside = edt_contract_complete_outside.getText().toString();


                Retrofit retrofit = RetrofitConnect.getClient();
                final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
                Call<ResponseBody> responseBodyCall = contractAPI.completeContract(contractID, overTime, inside, outside);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200){
                            String contractStatus = response.body().toString();
                            Intent it = new Intent(ContractFinishOwner.this, MainActivity.class);
                            startActivity(it);
                        } else {
                            Toast.makeText(ContractFinishOwner.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ContractFinishOwner.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
