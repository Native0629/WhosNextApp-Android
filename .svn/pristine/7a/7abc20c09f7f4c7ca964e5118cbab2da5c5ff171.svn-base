package com.app.whosnextapp.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectMultipleImageAdapter extends RecyclerView.Adapter<SelectMultipleImageAdapter.selectMultipleImageViewHolder> {

    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ArrayList<File> images;

    public SelectMultipleImageAdapter(Context context) {
        this.context = context;
    }

    void doRefresh(ArrayList<File> dataSet) {
        images = dataSet;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public selectMultipleImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_adsitem_list, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(350, 300));
        return new SelectMultipleImageAdapter.selectMultipleImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull selectMultipleImageViewHolder holder, int position) {


        Glide.with(context)
                .load(images.get(position).getAbsolutePath())
                .into(holder.iv_upload);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class selectMultipleImageViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_upload)
        AppCompatImageView iv_upload;


        public selectMultipleImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
