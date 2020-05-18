package com.app.whosnextapp.drawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.menuHolder> {

    private Context context;
    private String[] menu_item = new String[]{Constants.WN_FEATURED_PROFILE, Constants.WN_BREAST_CANCER_LEGACIES, Constants.WN_DISCOVER, Constants.WN_HOME_PAGE, Constants.WN_MY_PROFILE, Constants.WN_MESSAGING, Constants.WN_PICTURES, Constants.WN_VIDEOS, Constants.WN_CHANGE_PASSWORD, Constants.WN_SETTING, Constants.WN_LOGOUT};
    private MenuAdapterListener menuAdapterListener;


    DrawerMenuAdapter(Context context, MenuAdapterListener menuAdapterListener) {
        this.context = context;
        this.menuAdapterListener = menuAdapterListener;
    }

    String getItem(int position) {
        return menu_item != null ? menu_item[position] : null;
    }

    @NonNull
    @Override
    public menuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.navigation_menu_item, viewGroup, false);
        return new DrawerMenuAdapter.menuHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull menuHolder menuHolder, @SuppressLint("RecyclerView") final int i) {
        menuHolder.setDataToView(menu_item[i], i);
    }

    @Override
    public int getItemCount() {
        return menu_item.length;
    }

    public interface MenuAdapterListener {
        void menuItemOnClick(View v, int position);
    }


    class menuHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_menu)
        AppCompatTextView tv_menu;

        menuHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setDataToView(String menuItem, final int i) {
            tv_menu.setText(menuItem);

            if (menuItem.contains(Constants.WN_FEATURED_PROFILE)) {
                tv_menu.setTextColor(ContextCompat.getColor(context, R.color.sky));
            }

            if (menuItem.contains(Constants.WN_BREAST_CANCER_LEGACIES)) {
                tv_menu.setTextColor(ContextCompat.getColor(context, R.color.pink));
                tv_menu.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ribbon_selected), null);
                tv_menu.setCompoundDrawablePadding(20);
            }

            Globals.setAnimationToView(tv_menu);
            tv_menu.setOnClickListener(v -> menuAdapterListener.menuItemOnClick(v, i));
        }
    }
}
