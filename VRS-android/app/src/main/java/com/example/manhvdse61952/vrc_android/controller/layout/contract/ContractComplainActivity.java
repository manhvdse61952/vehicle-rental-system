package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainChat;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainIssue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractComplainActivity extends AppCompatActivity {

    CheckBox cbx_issue_inside, cbx_issue_outside, cbx_issue_owner;
    EditText edt_issue_content, edt_chat_content;
    Button btn_send_complain;
    LinearLayout ln_chat;
    ScrollView scrollView;
    ProgressDialog dialog;
    FloatingActionButton fab;
    int userID = 0;
    String contractID = "0";

    ListView lv_chat;
    private FirebaseListAdapter<ComplainChat> adapter;
    private DatabaseReference dbr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_complain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        cbx_issue_inside = (CheckBox) findViewById(R.id.cbx_issue_inside);
        cbx_issue_outside = (CheckBox) findViewById(R.id.cbx_issue_outside);
        cbx_issue_owner = (CheckBox) findViewById(R.id.cbx_issue_owner);
        edt_issue_content = (EditText) findViewById(R.id.edt_issue_content);
        btn_send_complain = (Button) findViewById(R.id.btn_send_complain);
        ln_chat = (LinearLayout) findViewById(R.id.ln_chat);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        edt_chat_content = (EditText) findViewById(R.id.edt_chat_content);
        lv_chat = (ListView) findViewById(R.id.lv_chat);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        SharedPreferences editor1 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        userID = editor1.getInt(ImmutableValue.HOME_userID, 0);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        contractID = editor2.getString(ImmutableValue.MAIN_contractID, "0");
        dbr = FirebaseDatabase.getInstance().getReference("Complain").child(contractID);
        revertValue();

        btn_send_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComplainAction();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    private void sendComplainAction() {
        Boolean isIssueInside = false;
        Boolean isIssueOutside = false;
        Boolean isIssueOwner = false;
        ComplainIssue obj = new ComplainIssue();

        if (cbx_issue_inside.isChecked()) {
            isIssueInside = true;
        }
        if (cbx_issue_outside.isChecked()) {
            isIssueOutside = true;
        }
        if (cbx_issue_owner.isChecked()) {
            isIssueOwner = true;
        }
        String complainContent = edt_issue_content.getText().toString();


        if (isIssueInside == false && isIssueOutside == false && isIssueOwner == false) {
            Toast.makeText(ContractComplainActivity.this, "Vui lòng chọn ít nhất 1 lí do", Toast.LENGTH_SHORT).show();
        } else {
            obj.setInsideIssue(isIssueInside);
            obj.setOutsideIssue(isIssueOutside);
            obj.setOwnerIssue(isIssueOwner);
            obj.setIssueContent(complainContent);
            dialog = ProgressDialog.show(ContractComplainActivity.this, "Đang xử lý",
                    "Vui lòng đợi ...", true);
            SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
            int contractID = Integer.parseInt(editor2.getString(ImmutableValue.MAIN_contractID, "0"));
            Retrofit retrofit = RetrofitConfig.getClient();
            final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
            Call<ResponseBody> responseBodyCall = contractAPI.issueContract(contractID, obj);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        ln_chat.setVisibility(View.VISIBLE);
                        btn_send_complain.setVisibility(View.INVISIBLE);
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, scrollView.getBottom());
                            }
                        });
                    } else {
                        Toast.makeText(ContractComplainActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(ContractComplainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void revertValue() {
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String contractStatus = editor2.getString(ImmutableValue.MAIN_contractStatus, ImmutableValue.CONTRACT_ISSUE);
        if (contractStatus.equals(ImmutableValue.CONTRACT_ISSUE)) {
            ln_chat.setVisibility(View.VISIBLE);
            btn_send_complain.setVisibility(View.INVISIBLE);
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scrollView.getBottom());
                }
            });
            displayMessage();
        }
    }

    private void displayMessage() {
        adapter = new ContractComplainAdapter(ContractComplainActivity.this,
                this, ComplainChat.class, R.layout.manage_chat_item,
                dbr);
        lv_chat.setAdapter(adapter);
    }

    private void sendMessage() {
        long dateValue = new Date().getTime();
        String currentTime = GeneralController.convertFullTime(dateValue);
        String childInFDB = GeneralController.generateChildFDB(dateValue);
        dbr.child(childInFDB).setValue(new ComplainChat(contractID, userID,
                edt_chat_content.getText().toString(), currentTime));
        edt_chat_content.setText("");
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
