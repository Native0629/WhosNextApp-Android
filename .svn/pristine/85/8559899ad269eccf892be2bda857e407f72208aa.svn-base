package com.app.whosnextapp.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.DiscoverModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.ViewHolder> {
    private ArrayList<DiscoverModel.CustomerList> customerLists;
    private Context context;
    private FollowRequestAdapter.OnCustomClickListener onCustomClickListener;

    FollowRequestAdapter(Context context, OnCustomClickListener onCustomClickListener) {
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
    }

    void doRefresh(ArrayList<DiscoverModel.CustomerList> customerLists) {
        this.customerLists = customerLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_follow_request_item, viewGroup, false);
        return new FollowRequestAdapter.ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowRequestAdapter.ViewHolder viewHolder, int i) {
        try {
            viewHolder.setDataToView(customerLists.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return customerLists.size();
    }

    public interface OnCustomClickListener {
        void onApproveClick(int position, View v);

        void onDisapproveClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.iv_approve)
        ImageView iv_approve;
        @BindView(R.id.iv_disapprove)
        ImageView iv_disapprove;

        public ViewHolder(@NonNull View itemView, FollowRequestAdapter followRequestAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_approve.setOnClickListener(this);
            iv_disapprove.setOnClickListener(this);
        }

        void setDataToView(DiscoverModel.CustomerList customerList, int i) {
            tv_name.setText(customerList.getFirstName() + " " + customerList.getLastName());
            tv_username.setText(customerList.getUserName());
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                switch (v.getId()) {
                    case R.id.iv_approve:
                        onCustomClickListener.onApproveClick(getAdapterPosition(), v);
                        break;
                    case R.id.iv_disapprove:
                        onCustomClickListener.onDisapproveClick(getAdapterPosition(), v);
                        break;
                }
            }
        }
    }
}
