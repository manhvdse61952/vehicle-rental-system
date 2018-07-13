package com.example.manhvdse61952.vrc_android.controller.layout.contract;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.api_model.ComplainChat;

import java.util.List;

public class ContractComplainAdapter extends RecyclerView.Adapter<ContractComplainAdapter.RecyclerViewHolder>{
    private List<ComplainChat> listChat;

    @Override
    public ContractComplainAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_chat_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txt_chat_username.setText(listChat.get(position).getSenderID() + "");
        holder.txt_chat_content.setText(listChat.get(position).getChatContent());
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView txt_chat_username, txt_chat_content;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txt_chat_username = (TextView)itemView.findViewById(R.id.txt_chat_username);
            txt_chat_content = (TextView)itemView.findViewById(R.id.txt_chat_content);
        }
    }
}
