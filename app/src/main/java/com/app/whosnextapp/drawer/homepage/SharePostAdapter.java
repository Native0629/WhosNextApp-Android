package com.app.whosnextapp.drawer.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetFollowingFollowerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SharePostAdapter extends RecyclerView.Adapter<SharePostAdapter.ViewHolder> {
    public Context context;
    private ArrayList<GetFollowingFollowerModel.CustomerList> customerLists;
    private ArrayList<GetFollowingFollowerModel.CustomerList> selectedPeople = new ArrayList<>();

    SharePostAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<GetFollowingFollowerModel.CustomerList> customerList) {
        this.customerLists = customerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SharePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_select_talent_item, viewGroup, false);
        return new SharePostAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SharePostAdapter.ViewHolder viewHolder, int i) {
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

    ArrayList<GetFollowingFollowerModel.CustomerList> getSelectedPeople() {
        return selectedPeople;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chk_category)
        AppCompatCheckBox chk_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDataToView(GetFollowingFollowerModel.CustomerList customerList, int i) {
            chk_category.setText(customerList.getFirstName() + " " + customerList.getLastName());
            chk_category.setTypeface(ResourcesCompat.getFont(context, R.font.ayearwithoutrain));

            chk_category.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPeople.add(customerList);
                } else {
                    selectedPeople.remove(customerList);
                }
            });
        }
    }
}
