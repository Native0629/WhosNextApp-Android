package com.app.whosnextapp.pictures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.app.whosnextapp.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesFragment extends Fragment {

    @BindView(R.id.vp_pictures)
    ViewPager vp_pictures;
    @BindView(R.id.tb_pictures)
    TabLayout tb_pictures;

    PagerAdapter pagerAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pictures, container, false);
        ButterKnife.bind(this, v);
        setViewPager();
        return v;
    }

    public void setViewPager() {
        if (getActivity() != null) {
            pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
            pagerAdapter.addFragment(new TabPicturesFragment(), getString(R.string.pictures));
            pagerAdapter.addFragment(new TabPicturesOfYouFragment(), getString(R.string.pictures_of_you));
            vp_pictures.setAdapter(pagerAdapter);
            tb_pictures.setupWithViewPager(vp_pictures);
            vp_pictures.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        }
    }
}
