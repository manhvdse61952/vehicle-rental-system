package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainIssue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractComplainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContractComplainAdapter adapter;
    CheckBox cbx_issue_inside, cbx_issue_outside, cbx_issue_owner;
    EditText edt_issue_content;
    Button btn_send_complain;
    LinearLayout ln_chat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_complain);

        //Declare id
        cbx_issue_inside = (CheckBox)findViewById(R.id.cbx_issue_inside);
        cbx_issue_outside = (CheckBox)findViewById(R.id.cbx_issue_outside);
        cbx_issue_owner = (CheckBox)findViewById(R.id.cbx_issue_owner);
        edt_issue_content = (EditText)findViewById(R.id.edt_issue_content);
        btn_send_complain = (Button)findViewById(R.id.btn_send_complain);
        ln_chat = (LinearLayout)findViewById(R.id.ln_chat);

        btn_send_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isIssueInside = false;
                Boolean isIssueOutside = false;
                Boolean isIssueOwner = false;
                ComplainIssue obj = new ComplainIssue();

                if (cbx_issue_inside.isChecked()){
                    isIssueInside = true;
                }
                if (cbx_issue_outside.isChecked()){
                    isIssueOutside = true;
                }
                if (cbx_issue_owner.isChecked()){
                    isIssueOwner = true;
                }
                String complainContent = edt_issue_content.getText().toString();


                if (isIssueInside == false && isIssueOutside == false && isIssueOwner == false){
                    Toast.makeText(ContractComplainActivity.this, "Vui lòng chọn ít nhất 1 lí do", Toast.LENGTH_SHORT).show();
                } else {
                    obj.setInsideIssue(isIssueInside);
                    obj.setOutsideIssue(isIssueOutside);
                    obj.setOwnerIssue(isIssueOwner);
                    obj.setIssueContent(complainContent);
                    sendAction(obj);
                }
            }
        });

    }

    private void sendAction(ComplainIssue obj){
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int contractID = Integer.parseInt(editor2.getString(ImmutableValue.MAIN_contractID, "0"));
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.issueContract(contractID, obj);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    ln_chat.setVisibility(View.VISIBLE);
                    btn_send_complain.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ContractComplainActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ContractComplainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
