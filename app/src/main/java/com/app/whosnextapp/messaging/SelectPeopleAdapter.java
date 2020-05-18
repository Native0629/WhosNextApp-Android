package com.app.whosnextapp.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.DiscoverModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SelectPeopleAdapter extends RecyclerView.Adapter<SelectPeopleAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DiscoverModel.CustomerList> customerLists;
    private ArrayList<DiscoverModel.CustomerList> mFilterCustomerLists;
    private SelectPeopleAdapter.OnCustomClickListener onCustomClickListener;

    SelectPeopleAdapter(Context context, SelectPeopleAdapter.OnCustomClickListener onCustomClickListener) {
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
    public SelectPeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discover_people_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPeopleAdapter.ViewHolder viewHolder, int i) {
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_profile_pic)
        CircleImageView iv_profile_pic;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_username)
        TextView tv_username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setDataToView(DiscoverModel.CustomerList customerList, int i) {
            tv_name.setText(customerList.getFirstName() + " " + customerList.getLastName());
            tv_username.setText(customerList.getUserName());
            Glide.with(context)
                    .load(customerList.getProfilePicUrl())
                    .into(iv_profile_pic);
        }

        @Override
        public void onClick(View v) {
            onCustomClickListener.onClickUsername(getAdapterPosition(), v);
        }
    }
}
