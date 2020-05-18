package com.app.whosnextapp.navigationmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetFollowersListModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetFollowersListModel.CustomerList> getCustomerLists;
    private ArrayList<GetFollowersListModel.CustomerList> mFilterCustomerLists;
    private FollowingAdapter.OnCustomClickListener onCustomClickListener;

    FollowingAdapter(Context context, FollowingAdapter.OnCustomClickListener onCustomClickListener) {
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
    }

    void doRefresh(ArrayList<GetFollowersListModel.CustomerList> getCustomerLists) {
        this.getCustomerLists = getCustomerLists;
        this.mFilterCustomerLists = getCustomerLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.follower_list_item, viewGroup, false);
        return new FollowingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            viewHolder.setDataToView(mFilterCustomerLists.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mFilterCustomerLists.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilterCustomerLists = getCustomerLists;
                } else {
                    ArrayList<GetFollowersListModel.CustomerList> filteredList = new ArrayList<>();
                    for (GetFollowersListModel.CustomerList row : getCustomerLists) {
                        String full_name = row.getFirstName() + " " + row.getLastName();
                        if (full_name.toLowerCase().contains(charString.toLowerCase()) ||
                                row.getUserName().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilterCustomerLists = (ArrayList<GetFollowersListModel.CustomerList>) results.values;
                onCustomClickListener.onFilter(mFilterCustomerLists != null);
                notifyDataSetChanged();
            }
        };
    }

    public interface OnCustomClickListener {
        void onClickProfile(int position, View v);

        void onClickUsername(int position, View v);

        void onClickFollowButton(int position, View v);

        void onClickFollowingButton(int position, View v);

        void onFilter(boolean IsShow);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_profile_pic)
        CircleImageView iv_profile_pic;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.btn_follower)
        Button btn_follower;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_profile_pic.setOnClickListener(this);
            tv_name.setOnClickListener(this);
            btn_follower.setOnClickListener(this);
        }

        void setDataToView(GetFollowersListModel.CustomerList customerList) {
            Glide.with(context)
                    .load(customerList.getProfilePicUrl())
                    .into(iv_profile_pic);
            tv_name.setText(customerList.getFirstName() + " " + customerList.getLastName());

            if (customerList.getIsFollowing()) {
                btn_follower.setText(R.string.following);
            } else {
                btn_follower.setText(R.string.follow);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    onCustomClickListener.onClickProfile(getAdapterPosition(), v);
                    break;
                case R.id.tv_name:
                    onCustomClickListener.onClickUsername(getAdapterPosition(), v);
                    break;
                case R.id.btn_follower:
                    if (btn_follower.getText().toString().equals(context.getResources().getString(R.string.follow)))
                        onCustomClickListener.onClickFollowButton(getAdapterPosition(), v);
                    else if (btn_follower.getText().equals(context.getResources().getString(R.string.following)))
                        onCustomClickListener.onClickFollowingButton(getAdapterPosition(), v);
                    break;
            }
        }
    }
}
