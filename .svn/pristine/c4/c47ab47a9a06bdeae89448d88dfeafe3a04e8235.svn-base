package com.app.whosnextapp.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    private String[] setting_menu_item = new String[]{Constants.WN_SUBSCRIBE_TO_ADVERTISE, Constants.WN_ADS, Constants.WN_GRATITUDE, Constants.WN_ADD_NEW_SNIPPETS};
    private SettingMenuClickListener settingMenuClickListener;

    SettingAdapter(SettingMenuClickListener settingMenuClickListener) {
        this.settingMenuClickListener = settingMenuClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_menu_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(setting_menu_item, i);
    }

    String getItem(int position) {
        return setting_menu_item != null ? setting_menu_item[position] : null;
    }

    @Override
    public int getItemCount() {
        return setting_menu_item.length;
    }

    public interface SettingMenuClickListener {
        void onSettingMenuClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_setting_menu)
        AppCompatTextView tv_setting_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(String[] setting_menu_item, final int i) {
            tv_setting_menu.setText(setting_menu_item[i]);
            tv_setting_menu.setOnClickListener(v -> settingMenuClickListener.onSettingMenuClick(v, i));
        }

    }
}
