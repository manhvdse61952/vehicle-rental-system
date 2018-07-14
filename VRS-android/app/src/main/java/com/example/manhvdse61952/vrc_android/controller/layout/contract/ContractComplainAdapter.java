package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainChat;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;


public class ContractComplainAdapter extends FirebaseListAdapter<ComplainChat> {
    private ContractComplainActivity activity;
    private Context ctx;

    public ContractComplainAdapter(Context ctx, ContractComplainActivity activity, Class<ComplainChat> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        this.ctx = ctx;
    }


    @Override
    protected void populateView(View v, ComplainChat model, int position) {
        TextView txt_chat_time = (TextView)v.findViewById(R.id.txt_chat_time);
        TextView txt_chat_content = (TextView)v.findViewById(R.id.txt_chat_content);

        txt_chat_time.setText("Đã gửi lúc " + model.getChatTime());
        txt_chat_content.setText(model.getChatContent());
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ComplainChat obj = getItem(position);
        SharedPreferences editor = ctx.getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE);
        int userID = editor.getInt(ImmutableValue.HOME_userID, 0);
        if (obj.getSenderID() == userID){
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
        } else {
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        }

        populateView(view, obj, position);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
