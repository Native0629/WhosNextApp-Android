package com.app.whosnextapp.videos;

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

public class SelectPeopleToTagAdapter extends RecyclerView.Adapter<SelectPeopleToTagAdapter.PeopleToTagHolder> {
    public Context context;
    private ArrayList<GetFollowingFollowerModel.CustomerList> customerLists;
    private ArrayList<GetFollowingFollowerModel.CustomerList> selectedPeople = new ArrayList<>();

    SelectPeopleToTagAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<GetFollowingFollowerModel.CustomerList> customerLists) {
        this.customerLists = customerLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeopleToTagHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_select_talent_item, viewGroup, false);
        return new SelectPeopleToTagAdapter.PeopleToTagHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleToTagHolder peopleToTagHolder, int i) {
        try {
            peopleToTagHolder.setDataToView(customerLists.get(i), i);
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

    class PeopleToTagHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chk_category)
        AppCompatCheckBox chk_category;

        PeopleToTagHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setDataToView(final GetFollowingFollowerModel.CustomerList customerList, int i) {
            chk_category.setText(customerList.getFirstName() + " " + customerList.getLastName());
            chk_category.setTypeface(ResourcesCompat.getFont(context, R.font.ayearwithoutrain));

            if (customerList.isSelected()) {
                chk_category.setChecked(true);
            } else {
                chk_category.setChecked(false);
            }

            chk_category.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPeople.add(customerLists.get(i));
                } else {
                    selectedPeople.remove(customerLists.get(i));
                }
            });
        }
    }
}
