package com.app.whosnextapp.videos.groupvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.DiscoverModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class InvitePeopleAdapter extends RecyclerView.Adapter<InvitePeopleAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DiscoverModel.CustomerList> customerLists;
    private ArrayList<DiscoverModel.CustomerList> mFilterCustomerLists;
    private InvitePeopleAdapter.OnCustomClickListener onCustomClickListener;


    InvitePeopleAdapter(Context context, InvitePeopleAdapter.OnCustomClickListener onCustomClickListener) {
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
    }

    void doRefresh(ArrayList<DiscoverModel.CustomerList> customerLists) {
        this.customerLists = customerLists;
        this.mFilterCustomerLists = customerLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvitePeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_invite_people, viewGroup, false);
        return new InvitePeopleAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitePeopleAdapter.ViewHolder viewHolder, int i) {
        try {
            viewHolder.setDataToView(mFilterCustomerLists.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mFilterCustomerLists.size();
    }

    ArrayList<DiscoverModel.CustomerList> getItems() {
        return mFilterCustomerLists;
    }

    Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilterCustomerLists = customerLists;
                } else {
                    ArrayList<DiscoverModel.CustomerList> filteredList = new ArrayList<>();
                    for (DiscoverModel.CustomerList row : customerLists) {
                        String full_name = row.getFirstName() + " " + row.getLastName();
                        if (full_name.toLowerCase().contains(charString.toLowerCase()) ||
                                row.getUserName().toLowerCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    mFilterCustomerLists = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterCustomerLists;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilterCustomerLists = (ArrayList<DiscoverModel.CustomerList>) results.values;
                onCustomClickListener.onFilter(mFilterCustomerLists != null);
                notifyDataSetChanged();
            }
        };
    }

    public interface OnCustomClickListener {
        void onFilter(boolean IsShow);

        void onClickUsername(int position, View v);

        void onClickProfile(int position, View v);

        void onClickInvite(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_profile_pic)
        CircleImageView iv_profile_pic;
        @BindView(R.id.tv_username)
        AppCompatTextView tv_username;
        @BindView(R.id.btn_invite)
        AppCompatButton btn_invite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_profile_pic.setOnClickListener(this);
            tv_username.setOnClickListener(this);
            btn_invite.setOnClickListener(this);
        }

        public void setDataToView(DiscoverModel.CustomerList customerList, int i) {
            Glide.with(context).load(customerList.getProfilePicUrl()).into(iv_profile_pic);
            tv_username.setText(customerList.getUserName());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    onCustomClickListener.onClickProfile(getAdapterPosition(), v);
                    break;
                case R.id.tv_username:
                    onCustomClickListener.onClickUsername(getAdapterPosition(), v);
                    break;
                case R.id.btn_invite:
                    onCustomClickListener.onClickInvite(getAdapterPosition(), v);
            }
        }
    }
}
