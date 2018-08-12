package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.api_model.Feedback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.RecycleViewHolder>{
    private List<Feedback> feedbackList;

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manage_feedback_item, parent, false);
        return new RecycleViewHolder(view);
    }

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        Feedback obj = feedbackList.get(position);
        String textImg = obj.getUsername().substring(0,1);
        holder.txt_img_feedback.setText(textImg);
        holder.txt_feedback_name.setText(obj.getUsername());
        holder.txt_feedback_content.setText(obj.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(obj.getCreateTime());
        String convertDate = sdf.format(date);
        holder.txt_feedback_time.setText(convertDate + "");
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView txt_img_feedback, txt_feedback_name, txt_feedback_content, txt_feedback_time;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            txt_img_feedback = (TextView)itemView.findViewById(R.id.txt_img_feedback);
            txt_feedback_name = (TextView)itemView.findViewById(R.id.txt_feedback_name);
            txt_feedback_content = (TextView)itemView.findViewById(R.id.txt_feedback_content);
            txt_feedback_time = (TextView)itemView.findViewById(R.id.txt_feedback_time);
        }
    }
}
