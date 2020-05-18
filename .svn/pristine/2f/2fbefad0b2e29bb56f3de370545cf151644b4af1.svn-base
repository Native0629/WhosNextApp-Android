package com.app.whosnextapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends Fragment implements SettingAdapter.SettingMenuClickListener {

    @BindView(R.id.rv_setting_menu)
    RecyclerView rv_setting_menu;

    SettingAdapter settingAdapter;
    Globals globals;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);
        init();
        setHasOptionsMenu(true);
        return v;
    }

    private void init() {
        if (getActivity() != null)
            globals = (Globals) getActivity().getApplicationContext();
        setAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_page_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_bell).setVisible(false);
        menu.findItem(R.id.header_search).setVisible(false);
        menu.findItem(R.id.header_refresh).setVisible(false);
    }

    private void setAdapter() {
        if (settingAdapter == null) {
            settingAdapter = new SettingAdapter(this);
        }
        if (rv_setting_menu.getAdapter() == null) {
            rv_setting_menu.setLayoutManager(new LinearLayoutManager(getContext()));
            settingAdapter = new SettingAdapter(this);
            rv_setting_menu.setAdapter(settingAdapter);
        }
    }

    @Override
    public void onSettingMenuClick(View v, int position) {
        switch (settingAdapter.getItem(position)) {
            case Constants.WN_SUBSCRIBE_TO_ADVERTISE:
                startActivity(new Intent(getContext(), SubscribeActivity.class));
                break;

            case Constants.WN_ADS:
                startActivity(new Intent(getContext(), AdsActivity.class));
                break;

            case Constants.WN_GRATITUDE:
                startActivity(new Intent(getContext(), GratitudeActivity.class));
                break;

            case Constants.WN_ADD_NEW_SNIPPETS:
                startActivity(new Intent(getContext(), AddSnippetActivity.class));
                break;
        }
    }
}
