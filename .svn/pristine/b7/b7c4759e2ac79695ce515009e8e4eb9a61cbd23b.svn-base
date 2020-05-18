package com.app.whosnextapp.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.CategoryModel;
import com.app.whosnextapp.model.DiscoverModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.notification.ProfileVideoPlayerActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.NothingSelectedSpinnerAdapter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher, DiscoverAdapter.OnCustomClickListener {
    @BindView(R.id.rv_discover_people)
    RecyclerView rv_discover_people;
    @BindView(R.id.sp_category)
    Spinner sp_category;
    @BindView(R.id.et_search_people)
    EditText et_search_people;

    DiscoverAdapter discoverAdapter;
    Globals globals;
    private ArrayList<CategoryModel.CategoryList> categoryLists = new ArrayList<>();
    private ArrayList<DiscoverModel.CustomerList> customerLists = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        init();
        return v;
    }

    private void init() {
        if (getActivity() != null) {
            globals = (Globals) getActivity().getApplicationContext();
            doRequestForCategory();
            et_search_people.addTextChangedListener(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_page_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_bell).setVisible(false);
        menu.findItem(R.id.header_refresh).setVisible(false);
        menu.findItem(R.id.header_search).setVisible(false);
    }

    private void doRequestForCategory() {
        new PostRequest(getActivity(), null, getString(R.string.get_talent_category_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                CategoryModel categoryModel = new Gson().fromJson(response, CategoryModel.class);
                if (categoryModel != null && categoryModel.getCategoryList() != null) {
                    categoryLists = categoryModel.getCategoryList();
                    showListInSpinner();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute();
    }

    private void showListInSpinner() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < categoryLists.size(); i++) {
            items.add(categoryLists.get(i).getCategoryName());
        }
        final NothingSelectedSpinnerAdapter<String> adapter;
        adapter = new CategorySpinnerAdapter(sp_category);
        sp_category.setAdapter(adapter);
        ((CategorySpinnerAdapter) adapter).doRefresh(items);
        sp_category.setOnItemSelectedListener(this);
    }

    private void doRequestForDiscoverPeople(Integer category_id) {
        JSONObject postData = HttpRequestHandler.getInstance()
                .getDiscoverPeopleJson(1, category_id, "");
        new PostRequest(getActivity(), postData, getString(R.string.get_discover_people_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                DiscoverModel discoverModel = new Gson().fromJson(response, DiscoverModel.class);
                if (discoverModel != null && discoverModel.getCustomerList() != null) {
                    customerLists = discoverModel.getCustomerList();
                    setDiscoverAdapter();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setDiscoverAdapter() {
        if (discoverAdapter == null) {
            discoverAdapter = new DiscoverAdapter(getContext(), this);
        }
        if (rv_discover_people.getAdapter() == null) {
            rv_discover_people.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_discover_people.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            rv_discover_people.setAdapter(discoverAdapter);
        }
        discoverAdapter.doRefresh(customerLists);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchPeople(s.toString().trim());
    }

    private void searchPeople(String text) {
        if (discoverAdapter != null) {
            discoverAdapter.getFilter().filter(text);
        }
    }

    @Override
    public void onClickProfile(int position, View v) {
        Intent intent = new Intent(getContext(), ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, discoverAdapter.getItems().get(position).getCustomerId());
        startActivity(intent);
    }

    @Override
    public void onClickUsername(int position, View v) {
        Intent intent = new Intent(getContext(), OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, discoverAdapter.getItems().get(position).getCustomerId());
        startActivity(intent);
    }

    @Override
    public void onFilter(boolean IsShow) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1) {
            rv_discover_people.setBackgroundResource(R.drawable.background);
        } else {
            rv_discover_people.setBackgroundColor(getResources().getColor(android.R.color.white));
            rv_discover_people.setVisibility(View.VISIBLE);
            doRequestForDiscoverPeople(categoryLists.get(position - 1).getCategoryId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
