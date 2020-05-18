package com.app.whosnextapp.pictures;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.GetAllPictureOfYouModel;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabPicturesOfYouFragment extends Fragment {

    @BindView(R.id.rv_pictures)
    RecyclerView rv_pictures;
    @BindView(R.id.tv_no_feed_found)
    AppCompatTextView tv_no_feed_found;

    Globals globals;
    ArrayList<GetAllPictureOfYouModel.PostList> postLists = new ArrayList<>();
    PictureOfYouAdapter pictureOfYouAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.tab_pictures_of_you, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init() {
        globals = (Globals) getActivity().getApplicationContext();
        doRequestForPicture();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.home_page_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.header_bell);
        View actionView = MenuItemCompat.getActionView(menuItem);
        // tv_noitfication_count = actionView.findViewById(R.id.tv_noitfication_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_bell).setVisible(true);
        menu.findItem(R.id.header_refresh).setVisible(false);
        menu.findItem(R.id.header_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        }
        return false;
    }

    private void doRequestForPicture() {
        new PostRequest(getActivity(), null, getString(R.string.get_all_picture_of_you), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllPictureOfYouModel getAllPictureOfYouModel = new Gson().fromJson(response, GetAllPictureOfYouModel.class);
                if (getAllPictureOfYouModel != null && getAllPictureOfYouModel.getPostList() != null) {
                    postLists = getAllPictureOfYouModel.getPostList();
                    setPictureAdapter(postLists);
                    if (postLists.size() == 0) {
                        tv_no_feed_found.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_feed_found.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getContext(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setPictureAdapter(ArrayList<GetAllPictureOfYouModel.PostList> postLists) {
        if (pictureOfYouAdapter == null) {
            pictureOfYouAdapter = new PictureOfYouAdapter(getActivity());
        }
        if (rv_pictures.getAdapter() == null) {
            rv_pictures.setHasFixedSize(true);
            rv_pictures.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rv_pictures.setAdapter(pictureOfYouAdapter);
        }
        pictureOfYouAdapter.doRefresh(postLists);
    }
}
