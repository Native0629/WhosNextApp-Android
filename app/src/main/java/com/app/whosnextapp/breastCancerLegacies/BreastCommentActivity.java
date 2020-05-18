package com.app.whosnextapp.breastCancerLegacies;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.SearchInFollowingFollowerAdapter;
import com.app.whosnextapp.model.AddEditBCLCommentModel;
import com.app.whosnextapp.model.GetAllCommentsByBreastCancerLegacyModel;
import com.app.whosnextapp.model.PersonModel;
import com.app.whosnextapp.model.SearchInFollowingFollowerModel;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.pictures.MentionHelper;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BreastCommentActivity extends BaseAppCompatActivity implements QueryTokenReceiver, SuggestionsResultListener, SuggestionsVisibilityManager, SearchInFollowingFollowerAdapter.OnCommentSuggestionEvent, View.OnClickListener {
    public PersonModel.PersonLoader personLoader;
    Globals globals;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.et_comment)
    MentionsEditText et_comment;
    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;
    @BindView(R.id.rv_search_people)
    RecyclerView rv_search_people;
    ArrayList<SpannableString> spannableStrings = new ArrayList<>();
    BreastCommentAdapter breastCommentAdapter;
    SearchInFollowingFollowerAdapter searchFollowerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        globals = (Globals) getApplicationContext();
        setActionbar();
        doRequestForDisplayComment();
        et_comment.setTokenizer(new WordTokenizer(Globals.getTokenizerConfig(", ")));
        et_comment.setQueryTokenReceiver(this);
        et_comment.setSuggestionsVisibilityManager(this);
    }

    public void setActionbar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar_title.setText(R.string.text_comment);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }


    @OnClick(R.id.btn_send)
    public void onSendCommentClick() {
        if (!et_comment.getText().toString().equals("")) {
            doRequestForAddComment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_bell).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header_refresh:
                setActionbar();
                doRequestForDisplayComment();
                break;
            case R.id.header_bell:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }
        return false;
    }

    public void doRequestForAddComment() {
        String Id = getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID);
        JSONObject PostData = HttpRequestHandler.getInstance()
                .getAddBCLCommentJson("0", Id, et_comment.getText().toString(), 0, 1);

        new PostRequest(this, PostData, getString(R.string.add_edit_bcl_comment), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                AddEditBCLCommentModel addEditBCLCommentModel = new Gson().fromJson(response, AddEditBCLCommentModel.class);
                if (addEditBCLCommentModel != null) {
                    doRequestForDisplayComment();
                    et_comment.setText("");
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(BreastCommentActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }


    public void setCommentAdapter(ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList) {
        if (breastCommentAdapter == null) {
            breastCommentAdapter = new BreastCommentAdapter(this);
        }
        if (rv_comment.getAdapter() == null) {
            rv_comment.setHasFixedSize(true);
            rv_comment.setLayoutManager(new LinearLayoutManager(this));
            rv_comment.setAdapter(breastCommentAdapter);
        }
        breastCommentAdapter.doRefresh(BCLCommentList, spannableStrings);
    }

    public void doRequestForDisplayComment() {
        String Id = getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID);
        String requestURL = String.format(getString(R.string.get_all_comment_by_breast_cancer), Id, "1");
        new PostRequest(this, null, requestURL, false, false, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllCommentsByBreastCancerLegacyModel getAllCommentsByBreastCancerLegacyModel = new Gson().fromJson(response, GetAllCommentsByBreastCancerLegacyModel.class);
                if (getAllCommentsByBreastCancerLegacyModel != null && getAllCommentsByBreastCancerLegacyModel.getBCLCommentList() != null && !getAllCommentsByBreastCancerLegacyModel.getBCLCommentList().isEmpty()) {
                    spannableStrings.clear();
                    for (int i = 0; i < getAllCommentsByBreastCancerLegacyModel.getBCLCommentList().size(); i++) {
                        spannableStrings.add(MentionHelper.decodeComments(BreastCommentActivity.this,
                                getAllCommentsByBreastCancerLegacyModel.getBCLCommentList().get(i).getComment(), getResources().getColor(R.color.pink)));
                    }
                    setCommentAdapter(getAllCommentsByBreastCancerLegacyModel.getBCLCommentList());
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(BreastCommentActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    public void setSearchFollowerAdapter(List<? extends Suggestible> follower) {
        if (searchFollowerAdapter == null) {
            searchFollowerAdapter = new SearchInFollowingFollowerAdapter(this, this);
        }
        if (rv_search_people.getAdapter() == null) {
            rv_search_people.setHasFixedSize(true);
            rv_search_people.setLayoutManager(new LinearLayoutManager(this));
            rv_search_people.setAdapter(searchFollowerAdapter);
        }
        searchFollowerAdapter.doRefresh(follower);
    }

    public void doRequestForSearchPeople(final String queryToken) {
        JSONObject PostData = HttpRequestHandler.getInstance().getSearchKeywordJson(queryToken);
        new PostRequest(this, PostData, getString(R.string.search_in_following), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                SearchInFollowingFollowerModel followingFollowerModel = new Gson().fromJson(response, SearchInFollowingFollowerModel.class);
                if (followingFollowerModel != null && followingFollowerModel.getCustomerList() != null && !followingFollowerModel.getCustomerList().isEmpty()) {
                    personLoader = new PersonModel.PersonLoader(getResources(), followingFollowerModel);
                    setSearchFollowerAdapter(personLoader.getSuggestions(new QueryToken("")));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    @Override
    public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
        if (!queryToken.getTokenString().equals(Constants.WN_MentionPrefix)) {
            if (queryToken.getTokenString().contains(Constants.WN_MentionPrefix)) {
                doRequestForSearchPeople(queryToken.getTokenString().replace(Constants.WN_MentionPrefix, " "));
                rv_comment.setVisibility(View.GONE);
                rv_search_people.setVisibility(View.VISIBLE);
            }
        }
        if (personLoader != null) {
            List<String> buckets = Arrays.asList(Constants.WN_CUSTOMER_LIST);
            List<PersonModel> suggestions = personLoader.getSuggestions(queryToken);
            SuggestionsResult result = new SuggestionsResult(queryToken, suggestions);
            if (queryToken.getTokenString().length() > 1)
                onReceiveSuggestionsResult(result, Constants.WN_CUSTOMER_LIST);
            else
                displaySuggestions(false);
            return buckets;
        }
        return null;
    }

    @Override
    public void onReceiveSuggestionsResult(@NonNull SuggestionsResult result, @NonNull String bucket) {
        List<? extends Suggestible> suggestions = result.getSuggestions();
        setSearchFollowerAdapter(suggestions);
        rv_comment.setVisibility(View.GONE);
        rv_search_people.setVisibility(View.VISIBLE);
        displaySuggestions(suggestions != null && suggestions.size() > 0);
    }

    @Override
    public void displaySuggestions(boolean display) {
        if (et_comment.isCurrentlyExplicit() && display) {
            rv_comment.setVisibility(View.GONE);
            rv_search_people.setVisibility(View.VISIBLE);
        } else {
            rv_search_people.setVisibility(View.GONE);
            rv_comment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean isDisplayingSuggestions() {
        return rv_search_people.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onSuggestionClick(PersonModel customerList) {
        et_comment.insertMention((Mentionable) customerList);
        setSearchFollowerAdapter(new ArrayList<PersonModel>());
        displaySuggestions(false);
        et_comment.getText().append(" ");
        et_comment.requestFocus();
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}