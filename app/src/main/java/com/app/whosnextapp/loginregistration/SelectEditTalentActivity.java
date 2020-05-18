package com.app.whosnextapp.loginregistration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.CategoryModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectEditTalentActivity extends BaseAppCompatActivity implements TextWatcher, SelectTalentAdapter.OnFilterApplyListener, View.OnClickListener {

    @BindView(R.id.rv_select_talent)
    RecyclerView rv_select_talent;
    @BindView(R.id.et_search_talent)
    AppCompatEditText et_search_talent;
    @BindView(R.id.tv_add)
    AppCompatTextView tv_add;

    Globals globals;
    String selectedTalent = "";
    SelectEditTalentAdapter selectTalentAdapter;
    SelectTalentAdapter.OnFilterApplyListener listener;
    private ArrayList<CategoryModel.CategoryList> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_talent);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        listener = this;
        globals = (Globals) getApplicationContext();
        if (getIntent().getExtras() != null) {
            selectedTalent = getIntent().getStringExtra(Constants.WN_SELECTED_TALENT);
        }
        doRequestForTalentCategory();
        et_search_talent.addTextChangedListener(this);
        et_search_talent.setOnClickListener(this);
    }

    private void doRequestForTalentCategory() {
        new PostRequest(this, null, getString(R.string.get_talent_category_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                CategoryModel categoryModel = new Gson().fromJson(response, CategoryModel.class);
                if (categoryModel != null && categoryModel.getCategoryList() != null) {
                    data = categoryModel.getCategoryList();
                    if (globals.getAllCategory().size() > 0)
                        data.addAll(globals.getAllCategory());
                    if (!selectedTalent.isEmpty()) {
                        String[] arr_selectedTalent = selectedTalent.split(",");
                        for (CategoryModel.CategoryList list : data) {
                            for (String s : arr_selectedTalent) {
                                if (String.valueOf(list.getCategoryId()).equalsIgnoreCase(s)) {
                                    list.setSelected(true);
                                    break;
                                }
                            }
                        }
                    }
                    setCategoryAdapter();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(SelectEditTalentActivity.this, getString(R.string.msg_NO_INTERNET_TITLE));
            }
        }).execute();
    }

    public void setCategoryAdapter() {
        if (selectTalentAdapter == null) {
            selectTalentAdapter = new SelectEditTalentAdapter(this, listener);
        }
        if (rv_select_talent.getAdapter() == null) {
            rv_select_talent.setLayoutManager(new LinearLayoutManager(this));
            rv_select_talent.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            rv_select_talent.setAdapter(selectTalentAdapter);
        }
        selectTalentAdapter.doRefresh(data);
    }

    void searchName(String text) {
        if (selectTalentAdapter != null) {
            selectTalentAdapter.getFilter().filter(text);
        }
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        onBackPressed();
    }

    @OnClick(R.id.tv_add)
    public void onBtnAdd() {
        String newCategory = et_search_talent.getText().toString();
        globals.addCategory(newCategory);
        et_search_talent.setText("");
        data.addAll(globals.getAllCategory());
        selectTalentAdapter.doRefresh(data);
    }

    @OnClick(R.id.btn_done)
    public void onBtnDone() {
        StringBuilder stringCategoryName = new StringBuilder();
        StringBuilder stringCategoryId = new StringBuilder();
        for (int i = 0; i < selectTalentAdapter.getItems().size(); i++) {
            CategoryModel.CategoryList categoryList = selectTalentAdapter.getItems().get(i);
            if (categoryList.isSelected()) {
                if (!stringCategoryName.toString().isEmpty()) {
                    stringCategoryName.append(" , ");
                    stringCategoryId.append(",");
                }
                stringCategoryName.append(categoryList.getCategoryName());
                stringCategoryId.append(categoryList.getCategoryId());
            }
        }
        Intent i = new Intent();
        i.putExtra(Constants.WN_CATEGORY_SEL, stringCategoryName.toString());
        i.putExtra(Constants.WN_CATEGORY_ID_SEL, stringCategoryId.toString());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchName(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFilter(boolean isShow) {
        if (isShow) {
            tv_add.setVisibility(View.VISIBLE);
        } else {
            tv_add.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.max_limit_reached);
        builder.setPositiveButton(R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        et_search_talent.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_search_talent, InputMethodManager.SHOW_IMPLICIT);
        /* dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/
    }
}
