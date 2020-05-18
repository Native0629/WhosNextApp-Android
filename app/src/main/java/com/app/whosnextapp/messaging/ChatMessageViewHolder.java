package com.app.whosnextapp.messaging;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chat_message)
    AppCompatTextView chat_message;
    @BindView(R.id.ll_chat)
    LinearLayout ll_chat;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.iv_chat_massege_image)
    RoundedImageView iv_chat_message_image;
    @BindView(R.id.rl_attachment)
    RelativeLayout rl_attachment;
    @BindView(R.id.caption_message)
    AppCompatTextView tv_caption_message;
    @BindView(R.id.tv_share)
    AppCompatTextView tv_share;

    ChatMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
