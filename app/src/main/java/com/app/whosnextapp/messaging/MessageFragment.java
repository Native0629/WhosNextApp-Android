package com.app.whosnextapp.messaging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.messaging.quickblox.helper.ChatHelper;
import com.app.whosnextapp.messaging.quickblox.utils.ConstantEnum;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.model.DiscoverModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressBaseDialog;

public class MessageFragment extends Fragment implements ChatHelper.OnQBChatServicesListener, MessageAdapter.OnCustomClickListener {

    @BindView(R.id.rv_message)
    RecyclerView rv_message;
    @BindView(R.id.tv_no_feed_found)
    AppCompatTextView tv_no_feed_found;

    MessageAdapter messageAdapter;
    ArrayList<QBChatDialog> qbChatDialogs;
    Globals globals;
    ACProgressBaseDialog acProgressBaseDialog;
    ChatHelper.OnQBChatServicesListener onQBChatServicesListener;
    String chat_id_list;
    DiscoverModel discoverModel;
    Activity activity = getActivity();
    ArrayList<DiscoverModel.CustomerList> customerLists;
    boolean isLoaderRequire = true;

    private BroadcastReceiver mQBDialogUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction().equals(Constants.WN_actionUpdateDialogs)) {
                setQbDialog();
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init() {
        registerBroadcasts();
        onQBChatServicesListener = this;
        globals = (Globals) activity.getApplicationContext();
        doRequestForGetChatProfile();
    }

    public void registerBroadcasts() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mQBDialogUpdateReceiver,
                new IntentFilter(Constants.WN_actionUpdateDialogs));
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
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header_bell:
                startActivity(new Intent(getContext(), NotificationActivity.class));
                break;
            case R.id.header_refresh:
                init();
                break;
        }
        return false;
    }

    @OnClick(R.id.fab_new_chat)
    public void onNewChat() {
        startActivity(new Intent(getContext(), SelectPeopleActivity.class));
    }

    public void setQbDialog() {
        if (dialogExist()) {
            if (qbChatDialogs == null)
                qbChatDialogs = new ArrayList<>();
            new loadDialogData().execute();
        } else {
            loadDialogs();
        }
    }

    @Override
    public void onClickItemView(int position, View v) {
        Intent intent = new Intent(getContext(), MessagingChatActivity.class);
        intent.putExtra(Constants.WN_DIALOG_ID, qbChatDialogs.get(position).getDialogId());
        intent.putExtra(Constants.WN_RECIPIENT_ID, qbChatDialogs.get(position).getRecipientId());
        intent.putExtra(Constants.WN_NAME, qbChatDialogs.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onClickUsername(int position, View v) {
        Intent intent = new Intent(getContext(), OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, Integer.valueOf(String.valueOf(qbChatDialogs.get(position).getCustomData().get(Constants.WN_CUSTOMER_ID))));
        startActivity(intent);
    }

    private void doRequestForGetChatProfile() {
        if (qbChatDialogs == null)
            qbChatDialogs = new ArrayList<>(QbDialogHolder.getInstance().getDialogs(true).values());
        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < qbChatDialogs.size(); j++) {
            list.add(qbChatDialogs.get(j).getRecipientId());
        }
        chat_id_list = TextUtils.join(",", list);

        String requestUrl = String.format(getString(R.string.get_all_customer_by_chatId), chat_id_list);
        new PostRequest(activity, null, requestUrl, isLoaderRequire, isLoaderRequire, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                discoverModel = new Gson().fromJson(response, DiscoverModel.class);
                if (discoverModel.getCustomerList() != null) {
                    customerLists = discoverModel.getCustomerList();
                    if (customerLists.size() > 0)
                        setQbDialog();
                    else {
                        dismissDialog();
                        setAdapter();
                    }
                } else {
                    dismissDialog();
                    setAdapter();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                dismissDialog();
                setAdapter();
                // Globals.showToast(getContext(), msg);
                tv_no_feed_found.setVisibility(View.VISIBLE);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    boolean dialogExist() {
        return new ArrayList<>(QbDialogHolder.getInstance().getDialogs(true).values()).size() > 0;
    }

    void loadDialogs() {
        showDialog();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (globals.getUserDetails() != null && activity != null) {
                    ChatHelper.getInstance().init(activity, null, ConstantEnum.QuickBloxDialogType.all, true, onQBChatServicesListener, false);
                }
            }
        }, 300);
    }

    private void showDialog() {
        if (acProgressBaseDialog == null)
            acProgressBaseDialog = ProgressUtil.initProgressBar(activity);
        if (activity != null && !acProgressBaseDialog.isShowing())
            acProgressBaseDialog.show();
    }

    private void dismissDialog() {
        if (activity != null && acProgressBaseDialog != null && acProgressBaseDialog.isShowing())
            acProgressBaseDialog.dismiss();
    }

    private void setAdapter() {
        if (qbChatDialogs != null && qbChatDialogs.size() > 0) {
            if (messageAdapter == null) {
                messageAdapter = new MessageAdapter(activity, this);
            }
            if (rv_message.getAdapter() == null && getContext() != null) {
                rv_message.setHasFixedSize(false);
                rv_message.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_message.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                rv_message.setAdapter(messageAdapter);
            }
            messageAdapter.doRefresh(qbChatDialogs);
        }
    }

    @Override
    public void onServiceProcessSuccess(ArrayList<QBChatDialog> qbChatDialogs) {
        if (activity == null)
            return;
        dismissDialog();
        if (this.qbChatDialogs == null)
            this.qbChatDialogs = new ArrayList<>();
        this.qbChatDialogs.clear();
        this.qbChatDialogs.addAll(ChatHelper.getInstance().setUpIndividualDialogList(customerLists, new ArrayList<>(QbDialogHolder.getInstance().getDialogs(true).values())));
        setAdapter();
    }

    @Override
    public void onServiceProcessFailed(String errorMessage) {
        if (activity == null)
            return;
        dismissDialog();
        Globals.showToast(activity, getString(R.string.msg_server_error));
        setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoaderRequire = false;
        doRequestForGetChatProfile();
    }

    @SuppressLint("StaticFieldLeak")
    public class loadDialogData extends AsyncTask<Void, Void, ArrayList<QBChatDialog>> {
        ArrayList<QBChatDialog> qbChatDialogsList = new ArrayList<>();

        @Override
        protected ArrayList<QBChatDialog> doInBackground(Void... voids) {
            try {
                qbChatDialogsList = ChatHelper.getInstance().setUpIndividualDialogList(customerLists, new ArrayList<>(QbDialogHolder.getInstance().getDialogs(true).values()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return qbChatDialogsList;
        }

        @Override
        protected void onPostExecute(ArrayList<QBChatDialog> qbChatDialog) {
            super.onPostExecute(qbChatDialog);
            dismissDialog();
            if (qbChatDialog.size() > 0) {
                qbChatDialogs.clear();
                qbChatDialogs.addAll(qbChatDialog);
            }
            setAdapter();
        }
    }
}
