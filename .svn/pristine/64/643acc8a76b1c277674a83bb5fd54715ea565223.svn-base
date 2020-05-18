package com.app.whosnextapp.drawer.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.messaging.quickblox.helper.ChatHelper;
import com.app.whosnextapp.messaging.quickblox.helper.QbChatDialogMessageListenerImp;
import com.app.whosnextapp.messaging.quickblox.helper.QbUsersHolder;
import com.app.whosnextapp.messaging.quickblox.utils.ConstantEnum;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.model.GetFollowingFollowerModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import org.jivesoftware.smack.SmackException;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SharePostActivity extends BaseAppCompatActivity implements View.OnClickListener, ChatHelper.OnQBChatServicesListener, ChatHelper.OnQBChatMeesageListener, FetchListener {
    public Fetch fetch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_chat)
    RecyclerView rv_chat;
    @BindView(R.id.et_message)
    AppCompatEditText et_message;
    Globals globals;
    SharePostAdapter sharePostAdapter;
    String dialogId, postPath, postId, messageId, downloadPostPath;
    QBChatDialog qbChatDialog;
    ACProgressFlower acProgressFlower;
    ArrayList<QBChatMessage> qbChatMessages;
    ChatHelper.OnQBChatServicesListener onQBChatServicesListener;
    ChatHelper.OnQBChatMeesageListener onQBChatMeesageListener;
    private ChatQBMessageListener chatMessageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_chat);
        init();
    }

    private Activity getActivity() {
        return SharePostActivity.this;
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionBar();
        initDownloadManager();
        if (getIntent() != null) {
            postPath = getIntent().getStringExtra(Constants.WN_PICTURE_PATH);
            postId = getIntent().getStringExtra(Constants.WN_POST_ID);
        }
        messageId = String.valueOf(System.currentTimeMillis() / 1000);
        downloadPostPath = Environment.getExternalStorageDirectory().getPath() + "/" + Constants.WN_SHARE_FOLDER_PATH + messageId + Constants.WN_EXT_IMAGE_JPG;
        addEnqueueDownload(new Request(postPath, downloadPostPath));

        onQBChatServicesListener = this;
        onQBChatMeesageListener = this;
        chatMessageListener = new ChatQBMessageListener();
        acProgressFlower = ProgressUtil.initProgressBar(getActivity());
        doRequestForGetFollowersAndFollowing();
    }

    public void initDownloadManager() {
        if (fetch == null) {
            fetch = Fetch.newInstance(getActivity());
            fetch.addFetchListener(this);
            fetch.setConcurrentDownloadsLimit(1);
        }
    }

    public void addEnqueueDownload(Request request) {
        if (fetch == null) {
            initDownloadManager();
        }
        //fetch.removeAll();
        fetch.enqueue(request);
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(Constants.WN_FOLLOWERS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    // Display Following Follower People List

    public void doRequestForGetFollowersAndFollowing() {
        final String requestURL = String.format(getString(R.string.get_following_following_list), 1);
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetFollowingFollowerModel getFollowingFollowerModel = new Gson().fromJson(response, GetFollowingFollowerModel.class);
                if (getFollowingFollowerModel != null && getFollowingFollowerModel.getCustomerList() != null)
                    setSharePostAdapter(getFollowingFollowerModel.getCustomerList());
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setSharePostAdapter(ArrayList<GetFollowingFollowerModel.CustomerList> customerList) {
        if (sharePostAdapter == null) {
            sharePostAdapter = new SharePostAdapter(getActivity());
        }
        if (rv_chat.getAdapter() == null) {
            rv_chat.setHasFixedSize(true);
            rv_chat.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_chat.setAdapter(sharePostAdapter);
        }
        sharePostAdapter.doRefresh(customerList);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.iv_send)
    public void onClickSend() {
        if (!ConnectionDetector.internetCheck(getActivity(), true))
            return;
        ArrayList<GetFollowingFollowerModel.CustomerList> customerLists = new ArrayList<>(sharePostAdapter.getSelectedPeople());
        if (!customerLists.isEmpty()) {
            int chat_id;
            for (int i = 0; i < customerLists.size(); i++) {
                if (customerLists.get(i).getChatId().equals("")) {
                    chat_id = 0;
                } else {
                    chat_id = Integer.valueOf(customerLists.get(i).getChatId());
                }
                sendMessage(chat_id);
            }
        } else {
            Globals.showToast(getActivity(), getString(R.string.msg_select_person));
        }
    }

    private void sendMessage(int chat_id) {
        QBChatDialog qbChatDialog = QbDialogHolder.getInstance().getDialogFromOpponentId(chat_id);
        if (qbChatDialog == null) {
            dialogId = "";
        } else {
            dialogId = qbChatDialog.getDialogId();
        }
        if (dialogId == null || dialogId.isEmpty()) {
            checkNmakeDialog(chat_id);
        } else {
            getAndLoadMessages();
        }

        QBContent.uploadFileTask(new File(downloadPostPath), true, null, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                final QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setDialogId(dialogId);
                qbChatMessage.setSenderId(ChatHelper.getCurrentUser().getId());
                qbChatMessage.setBody(et_message.getText().toString().trim());
                qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
                qbChatMessage.setMarkable(true);
                qbChatMessage.setId(messageId);
                qbChatMessage.setProperty(Constants.WN_FILE_PATH, downloadPostPath);
                qbChatMessage.setProperty(Constants.WN_HAS_IMAGE, "true");
                qbChatMessage.setProperty(Constants.WN_HAS_MEDIA, "true");

                qbChatMessage.setProperty(Constants.WN_PROPERTY_SAVE_TO_HISTORY, "1");
                qbChatMessage.setProperty(Constants.WN_SEND_TO_CHAT, "1");
                qbChatMessage.setProperty(Constants.WN_Username, globals.getUserDetails().getStatus().getUserName());
                qbChatMessage.setProperty(Constants.WN_Post_Type, getIntent().getStringExtra(Constants.WN_Post_Type));
                qbChatMessage.setProperty(Constants.WN_Post_Id, postId);
                qbChatMessage.setProperty(Constants.WN_IS_VIDO_THUMB, "NO");
                qbChatMessage.setProperty(Constants.WN_IS_POST_MSG, "YES");
                qbChatMessage.setProperty(Constants.WN_IS_DIRECT_MSG, "YES");
                qbChatMessage.setProperty(Constants.WN_DIRECT_MSG_URL, postPath);

                ArrayList<QBAttachment> qbAttachments = new ArrayList<>();
                final QBAttachment attachment = new QBAttachment(Constants.WN_QBChat_Msg_Att_Image);
                attachment.setId(String.valueOf(qbFile.getId()));
                qbAttachments.add(attachment);
                qbChatMessage.setAttachments(qbAttachments);
                try {
                    if (qbChatDialog != null) {
                        qbChatDialog.sendMessage(qbChatMessage);
                        QbDialogHolder.getInstance().updateDialog(qbChatMessage);
                    }
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
            }
        });
    }

    private void checkNmakeDialog(int chat_id) {
        qbChatDialog = QbDialogHolder.getInstance().getDialogFromOpponentId(chat_id);
        if (qbChatDialog != null) {
            dialogId = qbChatDialog.getDialogId();
            QbDialogHolder.getInstance().setCurrentChatDialogId(dialogId);
            loadMessages();
        } else {
            createDialog(chat_id);
        }
    }

    private void loadMessages() {
        QbDialogHolder.getInstance().updateUnreadToZero(qbChatDialog.getDialogId());
        initDialogForChat();
        loadDialogUsers();
    }

    void createDialog(int quickBoxID) {
        showDialog();
        ChatHelper.getInstance().createDialogWithSelectedUsers(getActivity(), quickBoxID, onQBChatServicesListener);
    }

    private void initDialogForChat() {
        qbChatDialog.initForChat(QBChatService.getInstance());
        qbChatDialog.addMessageListener(chatMessageListener);
    }

    private void loadDialogUsers() {
        showDialog();
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                QbUsersHolder.getInstance().putUsers(users);
                if (qbChatDialog != null) {
                    getMessage(qbChatDialog, false);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                dismissDialog();
                Globals.showToast(getActivity(), getString(R.string.msg_server_error));
            }
        });
    }

    void getMessage(QBChatDialog qbChatDialog, boolean forReFresh) {
        long dateSent = 0;
        if (qbChatMessages != null && qbChatMessages.size() > 0)
            dateSent = qbChatMessages.get(qbChatMessages.size() - 1).getDateSent();
        else
            forReFresh = false;
        ChatHelper.getInstance().getDialogMessage(qbChatDialog, forReFresh, dateSent, onQBChatMeesageListener);
    }

    private void showDialog() {
        if (acProgressFlower == null)
            acProgressFlower = ProgressUtil.initProgressBar(getActivity());
        if (acProgressFlower != null && !acProgressFlower.isShowing())
            acProgressFlower.show();
    }

    private void dismissDialog() {
        if (acProgressFlower != null && acProgressFlower.isShowing())
            acProgressFlower.dismiss();
    }

    private void getAndLoadMessages() {
        QbDialogHolder.getInstance().setCurrentChatDialogId(dialogId);
        qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(dialogId);
        if (qbChatDialog != null) {
            loadMessages();
        } else {
            showDialog();
            ChatHelper.getInstance().init(getActivity(), dialogId,
                    ConstantEnum.QuickBloxDialogType.private_chat, false,
                    onQBChatServicesListener, false);
        }
    }

    @Override
    public void onServiceProcessSuccess(ArrayList<QBChatDialog> qbChatDialogs) {
        this.qbChatDialog = qbChatDialogs.get(0);
        QbDialogHolder.getInstance().setCurrentChatDialogId(qbChatDialog.getDialogId());
        initDialogForChat();
        loadDialogUsers();
    }

    @Override
    public void onServiceProcessFailed(String errorMessage) {
        dismissDialog();
        Globals.showToast(getActivity(), getString(R.string.msg_server_error));
    }

    @Override
    public void messageReSuccess(ArrayList<QBChatMessage> qbChatMessages) {
        if (qbChatMessages.size() > 0) {
            if (this.qbChatMessages == null)
                this.qbChatMessages = new ArrayList<>();
            this.qbChatMessages.addAll(qbChatMessages);
        }
        dismissDialog();
        Globals.showToast(getActivity(), getString(R.string.msg_sent_successfully));
        startActivity(new Intent(getActivity(), HomePageActivity.class));
    }

    @Override
    public void messageReFailed(String message) {
        dismissDialog();
        Globals.showToast(getActivity(), getString(R.string.msg_server_error));
    }

    @Override
    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
        switch (status) {
            case Fetch.STATUS_DOWNLOADING:
                showDialog();
                break;
            case Fetch.STATUS_DONE:
                dismissDialog();
                break;
        }
    }

    private class ChatQBMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            if (qbChatMessage != null) {
                if (qbChatMessages == null)
                    qbChatMessages = new ArrayList<>();
                qbChatMessages.add(0, qbChatMessage);
            }
        }
    }
}