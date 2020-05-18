package com.app.whosnextapp.messaging.quickblox.listener;

import com.app.whosnextapp.messaging.quickblox.helper.ChatHelper;
import com.app.whosnextapp.messaging.quickblox.utils.ConstantEnum;
import com.orhanobut.logger.Logger;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatMessage;


public class QBSystemMessageListeners {
    QBSystemMessagesManager systemMessagesManager;
    ConstantEnum.QuickBloxDialogType quickBloxDialogType;

    public void listen(ConstantEnum.QuickBloxDialogType quickBloxDialogType) {
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
        this.quickBloxDialogType = quickBloxDialogType;
        QBSystemMessageListener systemMessageListener = new QBSystemMessageListener() {
            @Override
            public void processMessage(QBChatMessage systemMessage) {
                Logger.e("dialog notification");
                getDialogByID(String.valueOf(systemMessage.getProperty("_id")));
            }

            @Override
            public void processError(QBChatException e, QBChatMessage qbChatMessage) {
                Logger.e("dialog notification error");
            }
        };
        if (systemMessagesManager == null)
            return;
        systemMessagesManager.addSystemMessageListener(systemMessageListener);
    }

    private void getDialogByID(String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, quickBloxDialogType);
    }
}
