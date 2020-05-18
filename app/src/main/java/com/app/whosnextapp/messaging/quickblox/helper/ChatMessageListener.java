package com.app.whosnextapp.messaging.quickblox.helper;

import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.quickblox.chat.model.QBChatMessage;


public class ChatMessageListener extends QbChatDialogMessageListenerImp {

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        QbDialogHolder.getInstance().updateDialog(qbChatMessage);
    }
}
