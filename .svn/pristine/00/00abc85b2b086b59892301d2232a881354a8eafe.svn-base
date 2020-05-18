package com.app.whosnextapp.pictures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.AllPicturesModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.PictureHolder> {
    public Context context;
    private ArrayList<AllPicturesModel.PostList> ImageList = new ArrayList<>();
    private OnItemClick onItemClick;

    PicturesAdapter(Context context, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
    }

    public void doRefresh(ArrayList<AllPicturesModel.PostList> ImageList) {
        this.ImageList = ImageList;
        notifyDataSetChanged();
    }

    ArrayList<AllPicturesModel.PostList> ImageList() {
        return ImageList;
    }

    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pictures, viewGroup, false);
        return new PicturesAdapter.PictureHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureHolder pictureHolder, int i) {
        pictureHolder.setPictureAdapter(ImageList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    public interface OnItemClick {
        void selectImage(int i);
    }

    class PictureHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_uploadImage)
        AppCompatImageView iv_uploadImage;
        @BindView(R.id.iv_volume)
        AppCompatImageView iv_volume;

        PictureHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setPictureAdapter(final AllPicturesModel.PostList postList, final int i) {
            Glide.with(context).load(postList.getPostUrl()).into(iv_uploadImage);
            iv_volume.setVisibility(View.GONE);
            iv_uploadImage.setOnClickListener(v -> {
                if (onItemClick != null) {
                    onItemClick.selectImage(i);
                }
            });
        }
    }
}
