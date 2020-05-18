package com.app.whosnextapp.videos;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.GetFollowingFollowerModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPeopleToTagActivity extends BaseAppCompatActivity {
    @BindView(R.id.rv_select_people)
    RecyclerView rv_select_people;

    Globals globals;
    SelectPeopleToTagAdapter selectPeopleToTagAdapter;
    ArrayList<GetFollowingFollowerModel.CustomerList> selectedCustomerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people_to_tag);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        if (getIntent().getExtras() != null) {
            selectedCustomerList = (ArrayList<GetFollowingFollowerModel.CustomerList>) getIntent().getSerializableExtra(Constants.WN_CUSTOMER_LIST);
        }
        doRequestForGetFollowersAndFollowing();
    }

    public void doRequestForGetFollowersAndFollowing() {
        final String requestURL = String.format(getString(R.string.get_following_following_list), 1);
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetFollowingFollowerModel getFollowingFollowerModel = new Gson().fromJson(response, GetFollowingFollowerModel.class);
                if (getFollowingFollowerModel != null && getFollowingFollowerModel.getCustomerList() != null)
                    if (selectedCustomerList != null) {
                        for (int i = 0; i < selectedCustomerList.size(); i++) {
                            getFollowingFollowerModel.getCustomerList().get(i).setSelected(true);
                        }
                    }
                setSelectPeopleToTagAdapter(getFollowingFollowerModel.getCustomerList());
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    public void setSelectPeopleToTagAdapter(ArrayList<GetFollowingFollowerModel.CustomerList> customerList) {
        if (selectPeopleToTagAdapter == null) {
            selectPeopleToTagAdapter = new SelectPeopleToTagAdapter(this);
        }
        if (rv_select_people.getAdapter() == null) {
            rv_select_people.setHasFixedSize(true);
            rv_select_people.setLayoutManager(new LinearLayoutManager(this));
            rv_select_people.setAdapter(selectPeopleToTagAdapter);
        }
        selectPeopleToTagAdapter.doRefresh(customerList);
    }

    @OnClick(R.id.tv_done)
    public void onDoneClick() {
        ArrayList<GetFollowingFollowerModel.CustomerList> customerLists = new ArrayList<>(selectPeopleToTagAdapter.getSelectedPeople());
        Intent i = new Intent();
        i.putExtra(Constants.WN_CUSTOMER_LIST, customerLists);
        setResult(RESULT_OK, i);
        finish();
    }
}
