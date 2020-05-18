package com.app.whosnextapp.messaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.NothingSelectedSpinnerAdapter;
import com.app.whosnextapp.messaging.quickblox.helper.ChatHelper;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.model.CategoryModel;
import com.app.whosnextapp.model.DiscoverModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;
import com.quickblox.chat.model.QBChatDialog;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.whosnextapp.utility.Globals.getContext;

public class SelectPeopleActivity extends BaseAppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher, SelectPeopleAdapter.OnCustomClickListener, ChatHelper.OnQBChatServicesListener {

    @BindView(R.id.rv_people)
    RecyclerView rv_people;
    @BindView(R.id.tv_cancel)
    AppCompatTextView tv_cancel;
    @BindView(R.id.sp_category)
    AppCompatSpinner sp_category;
    @BindView(R.id.et_search_people)
    AppCompatEditText et_search_people;

    ArrayList<CategoryModel.CategoryList> categoryLists = new ArrayList<>();
    ArrayList<DiscoverModel.CustomerList> customerLists = new ArrayList<>();
    SelectPeopleAdapter selectPeopleAdapter;
    Globals globals;
    int chat_id;
    ChatHelper.OnQBChatServicesListener onQBChatServicesListener;
    String dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);
        init();
    }

    private Activity getActivity() {
        return SelectPeopleActivity.this;
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        onQBChatServicesListener = this;
        doRequestForCategory();
        et_search_people.addTextChangedListener(this);
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
        ArrayAdapter<String> adapter;
        if (getApplicationContext() != null) {
            adapter = new ArrayAdapter<>(getActivity(), R.layout.category_spinner_item, items);
            adapter.setDropDownViewResource(R.layout.category_spinner_item);
            sp_category.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.layout_nothing_selected_item_spinner, getActivity()));
            sp_category.setOnItemSelectedListener(this);
        }
    }

    private void doRequestForSearchPeople(Integer category_id) {
        JSONObject postData = HttpRequestHandler.getInstance().getDiscoverPeopleJson(1, category_id, "");
        new PostRequest(getActivity(), postData, getString(R.string.get_discover_people_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                DiscoverModel discoverModel = new Gson().fromJson(response, DiscoverModel.class);
                if (discoverModel.getCustomerList() != null) {
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
        if (selectPeopleAdapter == null) {
            selectPeopleAdapter = new SelectPeopleAdapter(getContext(), this);
        }
        if (rv_people.getAdapter() == null) {
            rv_people.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_people.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            rv_people.setAdapter(selectPeopleAdapter);
        }
        selectPeopleAdapter.doRefresh(customerLists);
    }


    @OnClick(R.id.tv_cancel)
    public void onCancelClick() {
        onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            doRequestForSearchPeople(categoryLists.get(position - 1).getCategoryId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        if (selectPeopleAdapter != null) {
            selectPeopleAdapter.getFilter().filter(text);
        }
    }

    @Override
    public void onFilter(boolean IsShow) {
    }

    @Override
    public void onClickUsername(int position, View v) {
        if (selectPeopleAdapter != null && ConnectionDetector.internetCheck(getActivity(), true)) {
            Intent intent = new Intent(getActivity(), MessagingChatActivity.class);
            if (selectPeopleAdapter.getItems().get(position).getChatId().equals("")) {
                chat_id = 0;
            } else {
                chat_id = Integer.valueOf(selectPeopleAdapter.getItems().get(position).getChatId());
            }
            intent.putExtra(Constants.WN_USER_ID, selectPeopleAdapter.getItems().get(position).getUserId());
            intent.putExtra(Constants.WN_USERNAME, selectPeopleAdapter.getItems().get(position).getUserName());
            intent.putExtra(Constants.WN_NAME, selectPeopleAdapter.getItems().get(position).getFirstName() + " " + selectPeopleAdapter.getItems().get(position).getLastName());
            intent.putExtra(Constants.WN_EMAIL, selectPeopleAdapter.getItems().get(position).getEmail());
            intent.putExtra(Constants.WN_QbId, chat_id);
            //ChatHelper.getInstance().createDialogWithSelectedUsers(getActivity(), chat_id, onQBChatServicesListener);
            QBChatDialog qbChatDialog = QbDialogHolder.getInstance().getDialogFromOpponentId(chat_id);
            if (qbChatDialog == null) {
                dialog = "";
            } else {
                dialog = qbChatDialog.getDialogId();
            }
            intent.putExtra(Constants.WN_DIALOG_ID, dialog);
            if (!String.valueOf(chat_id).equals("0") && dialog.isEmpty()) {
                intent.putExtra(Constants.WN_From_request, true);
                intent.putExtra(Constants.WN_opponentID, chat_id);
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onServiceProcessSuccess(ArrayList<QBChatDialog> qbChatDialogs) {
    }

    @Override
    public void onServiceProcessFailed(String errorMessage) {
        Globals.showToast(getActivity(), errorMessage);
    }
}
