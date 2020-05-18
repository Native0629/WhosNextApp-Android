package com.app.whosnextapp.messaging.quickblox.utils;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class QbDialogHolder {

    private static QbDialogHolder instance;
    private String currentChatDialogId = null;
    private Intent intentUpdateTotalUnreads = new Intent(Constants.WN_actionUpdateTotalUnread);
    private Intent intentUpdateDialogs = new Intent(Constants.WN_actionUpdateDialogs);
    private Map<String, QBChatDialog> privateDialogsMap, groupDialogsMap, dialogMap;


    private QbDialogHolder() {
        privateDialogsMap = new TreeMap<>();
        groupDialogsMap = new TreeMap<>();
        dialogMap = new TreeMap<>();
    }

    public static synchronized QbDialogHolder getInstance() {
        if (instance == null) {
            instance = new QbDialogHolder();
        }
        return instance;
    }

    public QBChatDialog getChatDialogById(String dialogId) {
        return dialogMap.get(dialogId);
    }

    private String getCurrentChatDialogId() {
        return currentChatDialogId;
    }

    public void setCurrentChatDialogId(String dialogId) {
        this.currentChatDialogId = dialogId;
    }

    public void addDialog(QBChatDialog dialog, boolean isPrivateDialog) {
        if (dialog != null) {
            if (isPrivateDialog)
                privateDialogsMap.put(dialog.getDialogId(), dialog);
            else
                groupDialogsMap.put(dialog.getDialogId(), dialog);
            dialogMap.put(dialog.getDialogId(), dialog);
        }
    }

    public boolean dialogPresent(String dialogId, boolean isPrivateDialog) {
        if (isPrivateDialog)
            return privateDialogsMap.containsKey(dialogId);
        else
            return groupDialogsMap.containsKey(dialogId);
    }

    public void clearDialogs() {
        if (privateDialogsMap != null)
            privateDialogsMap.clear();
        if (groupDialogsMap != null)
            groupDialogsMap.clear();
        if (dialogMap != null)
            dialogMap.clear();
    }

    public Map<String, QBChatDialog> getDialogs(boolean isPrivateDialog) {
        if (isPrivateDialog)
            return getSortedMap(privateDialogsMap);
        else
            return getSortedMap(groupDialogsMap);
    }

    private Map<String, QBChatDialog> getAllDialogs() {
        return getSortedMap(dialogMap);
    }

    private Map<String, QBChatDialog> getSortedMap(Map<String, QBChatDialog> unsortedMap) {
        Map<String, QBChatDialog> sortedMap = new TreeMap(new LastMessageDateSentComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public int getTotalUnreadDialogs() {
        int totalUnreads = 0;
        try {
            ArrayList<QBChatDialog> qbChatDialogs = new ArrayList<>(QbDialogHolder.getInstance().getDialogs(true).values());
            for (QBChatDialog qbChatDialog : qbChatDialogs) {
                if (qbChatDialog.getUnreadMessageCount() > 0)
                    totalUnreads = totalUnreads + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalUnreads;
    }

    public QBChatDialog getDialogFromOpponentId(Integer opponentID) {
        ArrayList<QBChatDialog> qbChatDialogs = new ArrayList<>(getAllDialogs().values());
        for (QBChatDialog qbChatDialog : qbChatDialogs) {
            if (qbChatDialog.getRecipientId().equals(opponentID))
                return qbChatDialog;
        }
        return null;
    }

    public void updateDialog(QBChatMessage qbChatMessage) {
        QBChatDialog updatedDialog = getChatDialogById(qbChatMessage.getDialogId());
        if (updatedDialog == null)
            return;
        updatedDialog.setLastMessage(qbChatMessage.getBody());
        updatedDialog.setLastMessageDateSent(qbChatMessage.getDateSent());
        if (Globals.isAppIsInBackground(Globals.getContext()) || (getCurrentChatDialogId() == null || !getCurrentChatDialogId().equalsIgnoreCase(qbChatMessage.getDialogId()))) {
            if (updatedDialog.getUnreadMessageCount() != null && updatedDialog.getUnreadMessageCount() == 0)
                LocalBroadcastManager.getInstance(Globals.getContext()).sendBroadcast(intentUpdateTotalUnreads);

            updatedDialog.setUnreadMessageCount(updatedDialog.getUnreadMessageCount() != null
                    ? updatedDialog.getUnreadMessageCount() + 1 : 1);
            if (updatedDialog.getType() == QBDialogType.PRIVATE) {
                if (!Globals.isAppIsInBackground(Globals.getContext()))
                    LocalBroadcastManager.getInstance(Globals.getContext()).sendBroadcast(new Intent(Constants.WN_UpdateNotificationBroadcast));
            }
        }
        LocalBroadcastManager.getInstance(Globals.getContext()).sendBroadcast(intentUpdateDialogs);
        dialogMap.put(updatedDialog.getDialogId(), updatedDialog);
        if (updatedDialog.getType() == QBDialogType.PRIVATE) {
            privateDialogsMap.put(updatedDialog.getDialogId(), updatedDialog);
        } else if (updatedDialog.getType() == QBDialogType.PUBLIC_GROUP) {
            groupDialogsMap.put(updatedDialog.getDialogId(), updatedDialog);
        }
    }

    public void updateUnreadToZero(String dialogId) {
        QBChatDialog updatedDialog = getChatDialogById(dialogId);
        if (updatedDialog != null) {
            updatedDialog.setUnreadMessageCount(0);
            dialogMap.put(updatedDialog.getDialogId(), updatedDialog);
            if (updatedDialog.getType() == QBDialogType.PRIVATE) {
                privateDialogsMap.put(updatedDialog.getDialogId(), updatedDialog);
            } else if (updatedDialog.getType() == QBDialogType.PUBLIC_GROUP) {
                groupDialogsMap.put(updatedDialog.getDialogId(), updatedDialog);
            }
            LocalBroadcastManager.getInstance(Globals.getContext()).sendBroadcast(intentUpdateTotalUnreads);
        }
    }

    static class LastMessageDateSentComparator implements Comparator<String> {
        Map<String, QBChatDialog> map;

        LastMessageDateSentComparator(Map<String, QBChatDialog> map) {
            this.map = map;
        }

        public int compare(String keyA, String keyB) {
            long valueA = map.get(keyA).getLastMessageDateSent();
            long valueB = map.get(keyB).getLastMessageDateSent();

            if (valueB < valueA) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
