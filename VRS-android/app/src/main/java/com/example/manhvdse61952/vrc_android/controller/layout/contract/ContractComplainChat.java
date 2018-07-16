package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainChat;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ContractComplainChat extends AppCompatActivity {
    FloatingActionButton fab;
    int userID = 0;
    String contractID = "0";
    ListView lv_chat;
    private FirebaseListAdapter<ComplainChat> adapter;
    private DatabaseReference dbr;
    EditText edt_chat_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_complain_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        edt_chat_content = (EditText)findViewById(R.id.edt_chat_content);
        lv_chat = (ListView) findViewById(R.id.lv_chat);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        //Get global value
        SharedPreferences editor1 = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        userID = editor1.getInt(ImmutableValue.HOME_userID, 0);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        contractID = editor2.getString(ImmutableValue.MAIN_contractID, "0");
        dbr = FirebaseDatabase.getInstance().getReference("Complain").child(contractID);

        loadMessage();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(ContractComplainChat.this, ContractComplainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadMessage(){
        adapter = new ContractComplainAdapter(ContractComplainChat.this,
                this, ComplainChat.class, R.layout.manage_chat_item,
                dbr);
        lv_chat.setAdapter(adapter);
    }

    private void sendMessage(){
        if (edt_chat_content.getText().toString().trim().equals("")){
            Toast.makeText(this, "Vui lòng điền nội dung", Toast.LENGTH_SHORT).show();
        } else {
            long dateValue = new Date().getTime();
            String currentTime = GeneralController.convertFullTime(dateValue);
            String childInFDB = GeneralController.generateChildFDB(dateValue);
            dbr.child(childInFDB).setValue(new ComplainChat(contractID, userID,
                    edt_chat_content.getText().toString(), currentTime));
            edt_chat_content.setText("");
        }
    }
}
