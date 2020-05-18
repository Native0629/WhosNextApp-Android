package com.app.whosnextapp.pictures;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.messaging.quickblox.helper.ChatHelper;
import com.app.whosnextapp.messaging.quickblox.helper.QbChatDialogMessageListenerImp;
import com.app.whosnextapp.messaging.quickblox.helper.QbUsersHolder;
import com.app.whosnextapp.messaging.quickblox.utils.ConstantEnum;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.model.GetFollowingFollowerModel;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.videos.SelectPeopleToTagActivity;
import com.bumptech.glide.Glide;
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

import org.jivesoftware.smack.SmackException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressFlower;

import static android.app.Activity.RESULT_OK;

public class MessagePictureFragment extends Fragment implements ChatHelper.OnQBChatServicesListener, ChatHelper.OnQBChatMeesageListener {
    @BindView(R.id.iv_picture_thumbnail)
    AppCompatImageView iv_picture_thumbnail;
    @BindView(R.id.tv_write_caption)
    AppCompatTextView tv_write_caption;
    @BindView(R.id.tv_tag_people)
    TextView tv_tag_people;

    String camera_picture, userName, caption;
    ShareToPictureActivity mContext;
    Globals globals;
    ACProgressFlower acProgressFlower;
    QBChatDialog qbChatDialog;
    ArrayList<QBChatMessage> qbChatMessages;
    String dialogId;
    ChatHelper.OnQBChatServicesListener onQBChatServicesListener;
    ChatHelper.OnQBChatMeesageListener onQBChatMeesageListener;
    ArrayList<GetFollowingFollowerModel.CustomerList> customerLists = new ArrayList<>();
    private ChatQBMessageListener chatMessageListener;

    public static MessagePictureFragment newInstance() {
        return new MessagePictureFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (ShareToPictureActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_follower_picture, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init() {
        onQBChatServicesListener = this;
        onQBChatMeesageListener = this;
        chatMessageListener = new ChatQBMessageListener();
        acProgressFlower = ProgressUtil.initProgressBar(getActivity());
        globals = (Globals) mContext.getApplicationContext();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContext.getIntent() != null) {
            camera_picture = mContext.getIntent().getStringExtra(Constants.WN_PICTURE_PATH);
            if (camera_picture != null) {
                Glide.with(mContext).load(camera_picture).into(iv_picture_thumbnail);
            }
        }
    }

    @OnClick(R.id.tv_tag_people)
    public void onTagPeopleClick() {
        Intent i = new Intent(getContext(), SelectPeopleToTagActivity.class);
        i.putExtra(Constants.WN_USERNAME, userName);
        i.putExtra(Constants.WN_CUSTOMER_LIST, customerLists);
        startActivityForResult(i, Constants.WN_SELECT_USER);
    }


    @OnClick(R.id.tv_write_caption)
    public void onCaptionClick() {
        Intent i = new Intent(getContext(), CaptionActivity.class);
        i.putExtra(Constants.WN_SHARED_VIDEO_CAPTION, caption);
        startActivityForResult(i, Constants.WN_CAPTION);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.WN_SELECT_USER) {
                customerLists = (ArrayList<GetFollowingFollowerModel.CustomerList>) data.getSerializableExtra(Constants.WN_CUSTOMER_LIST);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < customerLists.size(); i++) {
                    list.add(customerLists.get(i).getUserName());
                }
                String username = TextUtils.join(", ", list);
                if (username.equals("")) {
                    tv_tag_people.setText(getString(R.string.text_tap_tag_people));
                } else {
                    tv_tag_people.setText(username);
                }
            }

            if (requestCode == Constants.WN_CAPTION && data != null) {
                caption = data.getStringExtra(Constants.WN_SHARED_VIDEO_CAPTION);
                tv_write_caption.setText(caption);
            }
        }
    }

    @OnClick(R.id.btn_share)
    public void onShareClick() {
        if (!ConnectionDetector.internetCheck(getActivity(), true))
            return;
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
            Globals.showToast(getContext(), getString(R.string.msg_select_person));
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

        QBContent.uploadFileTask(new File(camera_picture), true, null, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                final String messageID = String.valueOf(System.currentTimeMillis() / 1000);
                final QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setDialogId(dialogId);
                qbChatMessage.setSenderId(ChatHelper.getCurrentUser().getId());
                qbChatMessage.setBody(caption.trim());
                qbChatMessage.setProperty(Constants.WN_PROPERTY_SAVE_TO_HISTORY, "1");
                qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
                qbChatMessage.setMarkable(true);
                qbChatMessage.setId(messageID);
                qbChatMessage.setProperty(Constants.WN_HAS_IMAGE, "true");
                qbChatMessage.setProperty(Constants.WN_HAS_MEDIA, "true");
                qbChatMessage.setProperty(Constants.WN_FILE_PATH, camera_picture);

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
            public void onError(QBResponseException error) {

            }
        });
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

    void createDialog(int quickBoxID) {
        showDialog();
        ChatHelper.getInstance().createDialogWithSelectedUsers(getActivity(), quickBoxID, onQBChatServicesListener);
    }

    private void loadMessages() {
        QbDialogHolder.getInstance().updateUnreadToZero(qbChatDialog.getDialogId());
        initDialogForChat();
        loadDialogUsers();
    }

    private void initDialogForChat() {
        qbChatDialog.initForChat(QBChatService.getInstance());
        qbChatDialog.addMessageListener(chatMessageListener);
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
        Globals.showToast(getContext(), getString(R.string.msg_server_error));
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
        startActivity(new Intent(getContext(), HomePageActivity.class));
    }

    @Override
    public void messageReFailed(String message) {
        dismissDialog();
        Globals.showToast(getContext(), getString(R.string.msg_server_error));
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

