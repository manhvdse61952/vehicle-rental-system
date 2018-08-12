package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainChat;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainIssue;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractComplainActivity extends AppCompatActivity {

    CheckBox cbx_issue_inside, cbx_issue_outside, cbx_issue_owner;
    EditText edt_issue_content;
    Button btn_send_complain, btn_view_complain, btn_open_chat;
    ProgressDialog dialog;
    FloatingActionButton btn_callCenter;
    int userID = 0, ownerID = 0, customerID = 0;
    LinearLayout ln_parent_layout;
    String contractID = "0";

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

        declareID();

        initLayout();

        btn_send_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComplainAction();
            }
        });

        btn_open_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ContractComplainActivity.this, ContractComplainChat.class);
                startActivity(it);
            }
        });


        btn_view_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID == ownerID) {
                    Intent it = new Intent(ContractComplainActivity.this, ContractPreFinishOwner.class);
                    startActivity(it);
                } else {
                    Intent it = new Intent(ContractComplainActivity.this, ContractPreFinishCustomer.class);
                    startActivity(it);
                }
            }
        });

        btn_callCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContractComplainActivity.this);
                builder.setMessage("Nhờ hỗ trợ từ hệ thống: khi hai bên xảy ra tranh chấp không thể giải quyết, hệ thống sẽ cử nhân viên xuống hỗ trợ, bạn có đồng ý không ?").setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callSupport();
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

    private void declareID(){
        //Declare id
        cbx_issue_inside = (CheckBox) findViewById(R.id.cbx_issue_inside);
        cbx_issue_outside = (CheckBox) findViewById(R.id.cbx_issue_outside);
        cbx_issue_owner = (CheckBox) findViewById(R.id.cbx_issue_owner);
        edt_issue_content = (EditText) findViewById(R.id.edt_issue_content);
        btn_send_complain = (Button) findViewById(R.id.btn_send_complain);
        btn_view_complain = (Button) findViewById(R.id.btn_view_complain);
        btn_open_chat = (Button) findViewById(R.id.btn_open_chat);
        btn_callCenter = (FloatingActionButton)findViewById(R.id.btn_callCenter);
        ln_parent_layout = (LinearLayout)findViewById(R.id.ln_parent_layout);
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
                        Toast.makeText(ContractComplainActivity.this, "Khiếu nại thành công! Hãy vô kiểm tra lại", Toast.LENGTH_SHORT).show();
                        ContractComplainActivity.this.finish();
                        Intent it = new Intent(ContractComplainActivity.this, MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
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

    private void initLayout() {
        //Call share preferences
        SharedPreferences editor1 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        userID = editor1.getInt(ImmutableValue.HOME_userID, 0);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        contractID = editor2.getString(ImmutableValue.MAIN_contractID, "0");
        ownerID = editor2.getInt(ImmutableValue.MAIN_ownerID, 0);
        customerID = editor2.getInt(ImmutableValue.MAIN_customerID, 0);

        dialog = ProgressDialog.show(ContractComplainActivity.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ContractItem> responseBodyCall = contractAPI.findContractByID(contractID);
        responseBodyCall.enqueue(new Callback<ContractItem>() {
            @Override
            public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                if (response.code() == 200) {
                    ContractItem obj = response.body();
                    if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_ISSUE)){
                        btn_callCenter.setVisibility(View.VISIBLE);
                        btn_open_chat.setVisibility(View.VISIBLE);
                        btn_send_complain.setVisibility(View.INVISIBLE);

                        if (obj.getOwnerIssue() == true){
                            cbx_issue_owner.setChecked(true);
                        } else if (obj.getOwnerIssue() == false){
                            cbx_issue_owner.setEnabled(false);
                        }
                        if (obj.getInsideIssue() == true){
                            cbx_issue_inside.setChecked(true);
                        } else if (obj.getInsideIssue() == false){
                            cbx_issue_inside.setEnabled(false);
                        }
                        if (obj.getOutsideIssue() == true){
                            cbx_issue_outside.setChecked(true);
                        } else if (obj.getOutsideIssue() == false){
                            cbx_issue_outside.setEnabled(false);
                        }
                        if (!obj.getIssueContent().trim().equals("")){
                            edt_issue_content.setText(obj.getIssueContent());
                        }

                        edt_issue_content.setClickable(false);
                        edt_issue_content.setFocusable(false);

                        cbx_issue_inside.setClickable(false);
                        cbx_issue_inside.setFocusable(false);

                        cbx_issue_outside.setClickable(false);
                        cbx_issue_outside.setFocusable(false);

                        cbx_issue_owner.setClickable(false);
                        cbx_issue_owner.setFocusable(false);

                    } else if (obj.getContractStatus().equals(ImmutableValue.CONTRACT_NEED_SUPPORT)){
                        if (obj.getOwnerIssue() == true){
                            cbx_issue_owner.setChecked(true);
                        } else if (obj.getOwnerIssue() == false){
                            cbx_issue_owner.setEnabled(false);
                        }
                        if (obj.getInsideIssue() == true){
                            cbx_issue_inside.setChecked(true);
                        } else if (obj.getInsideIssue() == false){
                            cbx_issue_inside.setEnabled(false);
                        }
                        if (obj.getOutsideIssue() == true){
                            cbx_issue_outside.setChecked(true);
                        } else if (obj.getOutsideIssue() == false){
                            cbx_issue_outside.setEnabled(false);
                        }
                        if (!obj.getIssueContent().trim().equals("")){
                            edt_issue_content.setText(obj.getIssueContent());
                        }

                        edt_issue_content.setClickable(false);
                        edt_issue_content.setFocusable(false);

                        cbx_issue_inside.setClickable(false);
                        cbx_issue_inside.setFocusable(false);

                        cbx_issue_outside.setClickable(false);
                        cbx_issue_outside.setFocusable(false);

                        cbx_issue_owner.setClickable(false);
                        cbx_issue_owner.setFocusable(false);
                        btn_callCenter.setVisibility(View.INVISIBLE);
                        btn_open_chat.setVisibility(View.VISIBLE);
                        btn_send_complain.setVisibility(View.INVISIBLE);
                        Snackbar snackbar = Snackbar.make(ln_parent_layout, "Hợp đồng đang được trung tâm xử lý, vui lòng đợi",Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                    } else {
                        btn_callCenter.setVisibility(View.INVISIBLE);
                        btn_open_chat.setVisibility(View.INVISIBLE);
                        btn_send_complain.setVisibility(View.VISIBLE);
                    }
                } else{
                    Toast.makeText(ContractComplainActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ContractComplainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ContractComplainActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void callSupport(){
        dialog = ProgressDialog.show(ContractComplainActivity.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<ResponseBody> responseBodyCall = contractAPI.callSupport(Integer.parseInt(contractID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Toast.makeText(ContractComplainActivity.this, "Gọi hỗ trợ thành công", Toast.LENGTH_SHORT).show();
                    ContractComplainActivity.this.finish();
                    Intent it = new Intent(ContractComplainActivity.this, MainActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
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
