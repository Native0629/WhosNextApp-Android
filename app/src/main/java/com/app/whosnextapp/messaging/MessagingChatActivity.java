package com.app.whosnextapp.messaging;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.drawer.homepage.paginationProgressBarAdapter;
import com.app.whosnextapp.messaging.quickblox.helper.ChatHelper;
import com.app.whosnextapp.messaging.quickblox.helper.ChatMessageUploadFile;
import com.app.whosnextapp.messaging.quickblox.helper.QbChatDialogMessageListenerImp;
import com.app.whosnextapp.messaging.quickblox.helper.QbUsersHolder;
import com.app.whosnextapp.messaging.quickblox.utils.ConstantEnum;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.messaging.videocall.fragment.CallActivity;
import com.app.whosnextapp.messaging.videocall.utils.PushNotificationSender;
import com.app.whosnextapp.model.UserDetailModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.PermissionsChecker;
import com.app.whosnextapp.utility.WebRtcSessionManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.orhanobut.logger.Logger;
import com.paginate.Paginate;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressFlower;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MessagingChatActivity extends BaseAppCompatActivity implements ChatHelper.OnQBCreateUserListener, ChatHelper.OnQBChatServicesListener, ChatHelper.OnQBChatMeesageListener, View.OnClickListener, Paginate.Callbacks, TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_chat)
    RecyclerView rv_chat;
    @BindView(R.id.iv_send)
    AppCompatImageView iv_send;
    @BindView(R.id.et_message)
    AppCompatEditText et_message;
    @BindView(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    Globals globals;
    ACProgressFlower acProgressFlower;
    Bundle extra;
    QBChatDialog qbChatDialog;
    String dialogId, message = "";
    ArrayList<QBChatMessage> qbChatMessages;
    ChatHelper.OnQBChatServicesListener onQBChatServicesListener;
    ChatMessageAdapter chatMessageAdapter;
    ChatHelper.OnQBChatMeesageListener onQBChatMeesageListener;
    boolean loading = false, isPageLoaded = false;
    private PermissionsChecker checker;
    private QBUser currentUser;
    private boolean isRunForCall;
    private ChatQBMessageListener chatMessageListener;
    private Paginate paginate;

    private WebRtcSessionManager webRtcSessionManager;
    private BroadcastReceiver mQBMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, final Intent intent) {
            try {
                if (intent.getAction() != null && intent.getAction().equals(Constants.WN_NotifyAdapter)) {
                    if (chatMessageAdapter != null) {
                        String dbId = intent.getStringExtra(Constants.WN_ChatDBID);
                        if (dbId != null && !dbId.isEmpty()) {
                            for (int i = 0; i < qbChatMessages.size(); i++) {
                                if (qbChatMessages.get(i).getProperty(Constants.WN_ChatDBID) != null && dbId.equals(qbChatMessages.get(i).getProperty(Constants.WN_ChatDBID))) {
                                    chatMessageAdapter.notifyItemChanged(getNotifyItemPosition(qbChatMessages.get(i)), intent.getIntExtra(Constants.WN_Media_Progress, 0));
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.d("TAG", ex.getMessage());
            }
        }
    };

    private Activity getActivity() {
        return MessagingChatActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_chat);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        checker = new PermissionsChecker(getApplicationContext());
        setActionbar();
        registerBroadcasts();
        extra = getIntent().getExtras();
        if (getIntent().hasExtra(Constants.WN_QbId) && extra.getInt(Constants.WN_QbId) == 0) {
            UserDetailModel.Status userDetailModel = new UserDetailModel.Status();
            userDetailModel.setName(getIntent().getStringExtra(Constants.WN_NAME));
            userDetailModel.setUserName(getIntent().getStringExtra(Constants.WN_USERNAME));
            userDetailModel.setEmail(getIntent().getStringExtra(Constants.WN_EMAIL));
            userDetailModel.setUserId(getIntent().getStringExtra(Constants.WN_USER_ID));
            showDialog();
            ChatHelper.setUpQBUser(getActivity(), userDetailModel, this);
        } else {
            initChat();
        }

        if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
            CallActivity.start(MessagingChatActivity.this, true);
        }

        checker = new PermissionsChecker(getApplicationContext());
        initFields();
    }

    private void initChat() {
        onQBChatServicesListener = this;
        onQBChatMeesageListener = this;
        chatMessageListener = new ChatQBMessageListener();

        checkFieldsForEmptyValues();
        et_message.addTextChangedListener(this);
        if (extra == null || globals.getUserDetails() == null) {
            finish();
            return;
        }
        if (acProgressFlower == null)
            acProgressFlower = ProgressUtil.initProgressBar(getActivity());

        if (extra.containsKey(Constants.WN_From_request)) {
            checkNmakeDialog(extra.getInt(Constants.WN_opponentID));
        }
        dialogId = extra.getString(Constants.WN_DIALOG_ID);
        if (dialogId == null || dialogId.isEmpty()) {
            checkNmakeDialog(extra.getInt(Constants.WN_QbId));
        } else {
            getAndLoadMessages();
        }
    }

    private void getAndLoadMessages() {
        QbDialogHolder.getInstance().setCurrentChatDialogId(dialogId);
        qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(dialogId);
        if (qbChatDialog != null) {
            setUserIdForDialog();
            loadMessages();
        } else {
            showDialog();
            ChatHelper.getInstance().init(this, dialogId,
                    ConstantEnum.QuickBloxDialogType.private_chat, false,
                    onQBChatServicesListener, false);
        }
    }

    private void checkNmakeDialog(int quickBloxID) {
        qbChatDialog = QbDialogHolder.getInstance().getDialogFromOpponentId(quickBloxID);
        if (qbChatDialog != null) {
            dialogId = qbChatDialog.getDialogId();
            setUserIdForDialog();
            QbDialogHolder.getInstance().setCurrentChatDialogId(dialogId);
            loadMessages();
        } else {
            createDialog(quickBloxID);
        }
    }

    void createDialog(int quickBoxID) {
        showDialog();
        ChatHelper.getInstance().createDialogWithSelectedUsers(this, quickBoxID, onQBChatServicesListener);
    }

    private void loadMessages() {
        QbDialogHolder.getInstance().updateUnreadToZero(qbChatDialog.getDialogId());
        initDialogForChat();
        loadDialogUsers();
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

    private void initDialogForChat() {
        qbChatDialog.initForChat(QBChatService.getInstance());
        qbChatDialog.addMessageListener(chatMessageListener);
        //addStatusListeners();
    }

    public void setUserIdForDialog() {
        if (extra != null && extra.containsKey(Constants.WN_USERNAME)) {
            QBDialogCustomData qbDialogCustomData = new QBDialogCustomData();
            qbDialogCustomData.putString(Constants.WN_USERNAME, extra.getString(Constants.WN_USERNAME));
            qbChatDialog.setCustomData(qbDialogCustomData);
        }
    }

    private void showDialog() {
        if (acProgressFlower == null)
            acProgressFlower = ProgressUtil.initProgressBar(getActivity());
        if (!this.isFinishing() && acProgressFlower != null && !acProgressFlower.isShowing())
            acProgressFlower.show();
    }

    private void dismissDialog() {
        if (!this.isFinishing() && acProgressFlower != null && acProgressFlower.isShowing())
            acProgressFlower.dismiss();
    }

    private void checkFieldsForEmptyValues() {
        String s1 = et_message.getText().toString();
        if (Globals.isWhitespace(s1) || s1.isEmpty()) {
            iv_send.setEnabled(false);
            iv_send.setClickable(false);
            iv_send.setFocusable(false);
        } else {
            iv_send.setEnabled(true);
            iv_send.setClickable(true);
            iv_send.setFocusable(true);
        }
    }

    public void setActionbar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar_title.setText(getIntent().getStringExtra(Constants.WN_NAME));
        toolbar.setNavigationIcon(R.drawable.backarrow);
        //  toolbar.setNavigationOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessagingChatActivity.this, HomePageActivity.class);
                intent.putExtra(Constants.WN_START_FRAGMENT, Constants.WN_MESSAGING);
                startActivity(intent);
                finish();
            }
        });
    }

    @OnClick(R.id.iv_send)
    public void sendMessage() {
        if (qbChatDialog != null) {
            QBChatMessage qbChatMessage = new QBChatMessage();
            qbChatMessage.setBody(et_message.getText().toString().trim());
            qbChatMessage.setSenderId(ChatHelper.getCurrentUser().getId());
            qbChatMessage.setProperty(Constants.WN_PROPERTY_SAVE_TO_HISTORY, "1");
            qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
            qbChatMessage.setMarkable(true);
            try {
                qbChatDialog.sendMessage(qbChatMessage);
                if (qbChatMessages == null)
                    qbChatMessages = new ArrayList<>();
                qbChatMessages.add(0, qbChatMessage);
                if (chatMessageAdapter != null) {
                    chatMessageAdapter.doRefreshSendMessage();
                } else {
                    setAdapter();
                }
                QbDialogHolder.getInstance().updateDialog(qbChatMessage);
                scrollMessageListDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
            et_message.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_page_menu, menu);
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkFieldsForEmptyValues();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onQBCreateUserSuccess(int QuickBloxId) {
        getIntent().putExtra(Constants.WN_QuickBloxId, String.valueOf(QuickBloxId));
        init();
    }

    @Override
    public void onQBCreateUserFailed() {
        dismissDialog();
        Globals.showToast(getActivity(), getString(R.string.msg_server_error));
        finish();
    }

    @Override
    public void onServiceProcessSuccess(ArrayList<QBChatDialog> qbChatDialogs) {
        this.qbChatDialog = qbChatDialogs.get(0);
        QbDialogHolder.getInstance().setCurrentChatDialogId(qbChatDialog.getDialogId());
        setUserIdForDialog();
        initDialogForChat();
        loadDialogUsers();
    }

    @Override
    public void onServiceProcessFailed(String errorMessage) {
        dismissDialog();
        Globals.showToast(this, getString(R.string.msg_server_error));
        finish();
    }

    private void scrollMessageListDown() {
        try {
            rv_chat.scrollToPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setAdapter() {
        if (qbChatMessages != null && qbChatMessages.size() > 0) {
            if (chatMessageAdapter == null) {
                if (paginate != null) {
                    paginate.unbind();
                }
                chatMessageAdapter = new ChatMessageAdapter(this, qbChatDialog);
            }
            loading = false;
            chatMessageAdapter.doRefresh(qbChatMessages);
            if (rv_chat.getAdapter() == null) {
                rv_chat.setHasFixedSize(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setReverseLayout(true);
                rv_chat.setLayoutManager(layoutManager);
                rv_chat.setAdapter(chatMessageAdapter);
                rv_chat.setItemViewCacheSize(20);
                DefaultItemAnimator animator = new DefaultItemAnimator() {
                    @Override
                    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return true;
                    }
                };
                rv_chat.setItemAnimator(animator);

                if (qbChatMessages.size() >= Constants.WN_MessageCount) {
                    paginate = Paginate.with(rv_chat, this)
                            .setLoadingTriggerThreshold(Constants.WN_chat_threshold)
                            .addLoadingListItem(Constants.WN_addLoadingRow)
                            .setLoadingListItemCreator(new paginationProgressBarAdapter())
                            .setLoadingListItemSpanSizeLookup(() -> Constants.WN_GRID_SPAN).build();
                }
            }
        }
        handleEmptyList();
    }

    public void handleEmptyList() {
        if (tv_no_data != null) {
            if (qbChatMessages == null || qbChatMessages.isEmpty()) {
                tv_no_data.setVisibility(View.VISIBLE);
                rv_chat.setVisibility(View.GONE);
            } else {
                tv_no_data.setVisibility(View.GONE);
                rv_chat.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void messageReSuccess(ArrayList<QBChatMessage> qbChatMessages) {
        if (qbChatMessages.size() > 0) {
            if (this.qbChatMessages == null)
                this.qbChatMessages = new ArrayList<>();
            /*this.qbChatMessages.addAll(removeUnUseAttachmentMessage(qbChatMessages));*/
            this.qbChatMessages.addAll(qbChatMessages);
        } else {
            isPageLoaded = true;
            loading = false;
        }
        dismissDialog();
        setAdapter();
    }

    @Override
    public void messageReFailed(String message) {
        isPageLoaded = true;
        loading = false;
        dismissDialog();
        Globals.showToast(this, getString(R.string.msg_server_error));
        setAdapter();
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
        if (qbChatDialog != null)
            QbDialogHolder.getInstance().updateUnreadToZero(qbChatDialog.getDialogId());
        QbDialogHolder.getInstance().setCurrentChatDialogId(null);
        finish();
    }

    @Override
    public void onLoadMore() {
        loading = true;
        if (qbChatDialog != null) {
            getMessage(qbChatDialog, true);
        }
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return isPageLoaded;
    }

    private void getPermissionForCamera() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showImageDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(getActivity(), getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showImageDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_camera), getResources().getString(R.string.text_gallery)};
        globals.show_dialog(getActivity(), getString(R.string.text_browse_image), "", options, true, (dialog, item) -> {
            switch (item) {
                case 0:
                    EasyImage.openCameraForImage(getActivity(), 0);
//                    EasyImage.openCamera(getActivity(), 0);
                    break;
                case 1:
                    EasyImage.openGallery(getActivity(), 0);
                    break;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!imageFiles.isEmpty()) {
                    sendAttachmentMessage(imageFiles.get(0), Constants.WN_QBChat_Msg_Att_Image);
                }
            }
        });
    }

    private void sendAttachmentMessage(File file, String attachmentType) {
        if (!ConnectionDetector.internetCheck(getActivity(), true))
            return;

        if (qbChatDialog != null) {
            final String messageID = String.valueOf(System.currentTimeMillis() / 1000);
            final QBChatMessage qbChatMessage = new QBChatMessage();
            //qbChatMessage.setBody(getString(R.string.msg_for_image_attachment));
            qbChatMessage.setBody(message);
            qbChatMessage.setDialogId(dialogId);
            qbChatMessage.setSenderId(ChatHelper.getCurrentUser().getId());
            qbChatMessage.setProperty(Constants.WN_PROPERTY_SAVE_TO_HISTORY, "1");
            qbChatMessage.setMarkable(true);
            qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
            qbChatMessage.setId(messageID);
            qbChatMessage.setProperty(Constants.WN_HAS_IMAGE, "true");
            qbChatMessage.setProperty(Constants.WN_HAS_MEDIA, "true");
            qbChatMessage.setProperty(Constants.WN_ChatDBID, messageID);
            qbChatMessage.setProperty(Constants.WN_FILE_PATH, file.getAbsolutePath());

            final QBAttachment attachment = new QBAttachment(attachmentType);
            attachment.setId(messageID);
            qbChatMessage.addAttachment(attachment);
            showDialog();

            if (attachmentType.equals(Constants.WN_QBChat_Msg_Att_Image)) {
                createMessage(qbChatMessage, file, attachmentType, messageID, message);
            }
        }
    }

    private void createMessage(final QBChatMessage qbChatMessage, final File destFile, final String attachmentType, String messageID, String message) {
        QBRestChatService.createMessage(qbChatMessage, false).performAsync(new QBEntityCallback<QBChatMessage>() {
            @Override
            public void onSuccess(QBChatMessage qbChatMssage, Bundle bundle) {
                readMessage(qbChatMessage);
                if (qbChatMessages == null)
                    qbChatMessages = new ArrayList<>();
                qbChatMessages.add(0, qbChatMessage);
                if (chatMessageAdapter != null) {
                    chatMessageAdapter.doRefreshSendMessage();
                    //chatMessageAdapter.notifyItemInserted(0);
                } else {
                    setAdapter();
                }
                QbDialogHolder.getInstance().updateDialog(qbChatMessage);
                //setAdapter();
                dismissDialog();
                scrollMessageListDown();
                ChatMessageUploadFile chatMessageUploadFile = new ChatMessageUploadFile(getActivity(), destFile, qbChatDialog, qbChatMssage, messageID, message);
                chatMessageUploadFile.uploadFile();
            }

            @Override
            public void onError(QBResponseException e) {
                dismissDialog();
                Globals.showToast(getActivity(), getString(R.string.err_msg_unble_to_send));
            }
        });
    }

    private void readMessage(QBChatMessage qbChatMessage) {
        try {
            qbChatDialog.readMessage(qbChatMessage);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Logger.e("TAG", e.getMessage());
        }
    }

    public void registerBroadcasts() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mQBMessageReceiver,
                new IntentFilter(Constants.WN_NotifyAdapter));
    }

    int getNotifyItemPosition(QBChatMessage qbChatMessage) {
        if (qbChatMessages.contains(qbChatMessage)) {
            return qbChatMessages.indexOf(qbChatMessage);
        }
        return 0;
    }

    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Globals.showToast(getActivity(), getString(R.string.dlg_signal_error));
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void initFields() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Constants.EXTRA_IS_STARTED_FOR_CALL);
        }
        currentUser = ChatHelper.getCurrentUser();
        webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
    }

    private void tryReLoginToChat() {
    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Constants.PERMISSIONS);
    }

    private void startCall(boolean isVideoCall) {
        ArrayList<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(extra.getInt(Constants.WN_RECIPIENT_ID));
        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;


        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

        PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());

        CallActivity.start(this, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            isRunForCall = intent.getExtras().getBoolean(Constants.EXTRA_IS_STARTED_FOR_CALL);
            if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                CallActivity.start(MessagingChatActivity.this, true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header_camera:
                getPermissionForCamera();
                break;
            case R.id.header_video:
                if (isLoggedInChat()) {
                    startCall(true);
                }
                if (checker.lacksPermissions(Constants.PERMISSIONS)) {
                    startPermissionsActivity(false);
                }
                return true;
        }
        return false;
    }

    public class ChatQBMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            if (qbChatMessage != null) {
                if (qbChatMessages == null)
                    qbChatMessages = new ArrayList<>();
                qbChatMessages.add(0, qbChatMessage);
                if (rv_chat != null && rv_chat.getLayoutManager() != null && chatMessageAdapter != null) {
                    chatMessageAdapter.doRefreshSendMessage();
                    if (((LinearLayoutManager) rv_chat.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                        scrollMessageListDown();
                    }
                } else {
                    setAdapter();
                }
            }
        }
    }
}
