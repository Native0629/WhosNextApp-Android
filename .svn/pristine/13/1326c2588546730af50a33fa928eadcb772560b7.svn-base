package com.app.whosnextapp.messaging.quickblox.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.utility.Constants;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.io.File;
import java.util.ArrayList;

public class ChatMessageUploadFile {
    private Activity context;
    private File file;
    private QBChatMessage qbChatMessage;
    private QBChatDialog qbChatDialog;
    private String DbId, Message;
    private String type = Constants.WN_QBChat_Msg_Att_Image;

    public ChatMessageUploadFile(Activity context, File file, QBChatDialog qbChatDialog, QBChatMessage qbChatMessage, String DbId, String message) {
        this.context = context;
        this.file = file;
        this.qbChatDialog = qbChatDialog;
        this.DbId = DbId;
        this.qbChatMessage = qbChatMessage;
        this.Message = message;
    }

    public void uploadFile() {
        if (!ConnectionDetector.internetCheck(context, true))
            return;
        sendBroadcast(0);
        QBContent.uploadFileTask(file, true, null, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
                sendBroadcast(progress);
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                sendMessage(String.valueOf(qbFile.getId()), Message);
            }

            @Override
            public void onError(QBResponseException error) {
                Intent intentNotify = new Intent(Constants.WN_NotifyAdapter).putExtra(Constants.WN_ChatDBID, DbId);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intentNotify);
            }
        });
    }

    private void sendMessage(final String UId, String message) {
        QBRestChatService.deleteMessage(qbChatMessage.getId(), true).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                final QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setDialogId(qbChatDialog.getDialogId());
                qbChatMessage.setSenderId(ChatHelper.getCurrentUser().getId());
                //qbChatMessage.setBody(context.getString(R.string.msg_for_image_attachment));
                qbChatMessage.setBody(message);
                qbChatMessage.setProperty(Constants.WN_PROPERTY_SAVE_TO_HISTORY, "1");
                qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
                qbChatMessage.setMarkable(true);
                qbChatMessage.setId(DbId);
                qbChatMessage.setProperty(Constants.WN_ChatDBID, DbId);
                qbChatMessage.setProperty(Constants.WN_HAS_IMAGE, "true");
                qbChatMessage.setProperty(Constants.WN_HAS_MEDIA, "true");
                qbChatMessage.setProperty(Constants.WN_FILE_PATH, file.getAbsolutePath());

                ArrayList<QBAttachment> qbAttachments = new ArrayList<>();
                final QBAttachment attachment = new QBAttachment(type);
                attachment.setId(UId);
                //attachment.setId(DbId);
                qbAttachments.add(attachment);
                qbChatMessage.setAttachments(qbAttachments);
                try {
                    qbChatDialog.sendMessage(qbChatMessage);
                    QbDialogHolder.getInstance().updateDialog(qbChatMessage);
                    sendBroadcast(100);
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

    private void sendBroadcast(int progress) {
        Intent intentNotify = new Intent(Constants.WN_NotifyAdapter).putExtra(Constants.WN_ChatDBID, DbId).putExtra(Constants.WN_Media_Progress, progress);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentNotify);
    }
}
