package com.app.whosnextapp.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.utility.TimeUtils;
import com.bumptech.glide.Glide;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialog;
    private MessageAdapter.OnCustomClickListener onCustomClickListener;

    MessageAdapter(Context context, MessageAdapter.OnCustomClickListener onCustomClickListener) {
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
    }

    void doRefresh(ArrayList<QBChatDialog> qbChatDialog) {
        this.qbChatDialog = qbChatDialog;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_message_list, viewGroup, false);
        return new MessageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
        try {
            viewHolder.setDataToView(qbChatDialog.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return qbChatDialog == null ? 0 : qbChatDialog.size();
    }

    public interface OnCustomClickListener {
        void onClickItemView(int position, View v);

        void onClickUsername(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_profile)
        ImageView iv_profile;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_message)
        TextView tv_message;
        @BindView(R.id.tv_last_message_time)
        TextView tv_last_message_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onCustomClickListener.onClickItemView(getAdapterPosition(), v));
            tv_name.setOnClickListener(this);
        }

        public void setDataToView(final QBChatDialog qbChatDialog, int i) {
            tv_name.setText(qbChatDialog.getName());
            tv_message.setText(qbChatDialog.getLastMessage());
            tv_last_message_time.setText(TimeUtils.getFormattedLastSeenTiming(context, qbChatDialog.getLastMessageDateSent() * 1000));
            Glide.with(context).load(qbChatDialog.getPhoto()).into(iv_profile);
        }

        @Override
        public void onClick(View v) {
            onCustomClickListener.onClickUsername(getAdapterPosition(), v);
        }
    }
}