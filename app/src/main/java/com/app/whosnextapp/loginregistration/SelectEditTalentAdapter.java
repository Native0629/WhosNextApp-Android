package com.app.whosnextapp.loginregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.CategoryModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectEditTalentAdapter extends RecyclerView.Adapter<SelectEditTalentAdapter.CustomViewHolder> implements Filterable {
    private int selectedItemCounter = 0;
    private Context context;
    private ArrayList<CategoryModel.CategoryList> categoryModels;
    private ArrayList<CategoryModel.CategoryList> mFilterCategoryModels;
    private SelectTalentAdapter.OnFilterApplyListener listener;

    SelectEditTalentAdapter(Context context, SelectTalentAdapter.OnFilterApplyListener listener) {
        this.context = context;
        this.listener = listener;
    }

    void doRefresh(ArrayList<CategoryModel.CategoryList> categoryModels) {
        for (int i = 0; i < categoryModels.size(); i++) {
            if (categoryModels.get(i).isSelected) {
                selectedItemCounter++;
            }
        }
        this.categoryModels = categoryModels;
        this.mFilterCategoryModels = categoryModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_select_talent_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int i) {
        CategoryModel.CategoryList categoryList = mFilterCategoryModels.get(i);
        try {
            holder.setDataToView(categoryList, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mFilterCategoryModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilterCategoryModels = categoryModels;
                } else {
                    ArrayList<CategoryModel.CategoryList> filteredList = new ArrayList<>();
                    for (CategoryModel.CategoryList l : categoryModels) {
                        if (l.getCategoryName().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(l);
                        }
                    }
                    mFilterCategoryModels = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterCategoryModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilterCategoryModels = (ArrayList<CategoryModel.CategoryList>) results.values;
                listener.onFilter(mFilterCategoryModels.isEmpty());
                notifyDataSetChanged();
            }
        };
    }

    ArrayList<CategoryModel.CategoryList> getItems() {
        return categoryModels;
    }

    interface OnFilterApplyListener {
        void onFilter(boolean isShow);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chk_category)
        AppCompatCheckBox chk_category;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setDataToView(final CategoryModel.CategoryList categoryList, int i) {
            chk_category.setText(categoryList.getCategoryName());
            chk_category.setOnCheckedChangeListener(null);
            if (categoryList.isSelected()) {
                chk_category.setChecked(true);
            } else {
                chk_category.setChecked(false);
            }

            chk_category.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedItemCounter++;
                } else {
                    selectedItemCounter--;
                }
//                if (selectedItemCounter > 3) {
//                    chk_category.setChecked(false);
//                    Toast.makeText(context, R.string.max_limit_reached, Toast.LENGTH_SHORT).show();
//                    selectedItemCounter--;
//                } else {
                categoryList.setSelected(isChecked);
                // }
            });
        }
    }
}
