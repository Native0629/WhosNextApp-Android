package com.app.whosnextapp.messaging.quickblox.helper;

import android.app.Activity;
import android.os.Bundle;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.UserDetailModel;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.orhanobut.logger.Logger;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

public class RegisterNewUserTOQB {
    private static final String TAG = RegisterNewUserTOQB.class.getSimpleName();
    private static RegisterNewUserTOQB instance;
    private Globals globals = (Globals) Globals.getContext();
    private boolean isUpdatingQBToDB = false;

    public static synchronized RegisterNewUserTOQB getInstance() {
        if (instance == null) {
            //QBSettings.getInstance().setLogLevel(LogLevel.DEBUG);
            QBChatService.setDebugEnabled(true);
            QBChatService.setConfigurationBuilder(ChatHelper.buildChatConfigs());
            instance = new RegisterNewUserTOQB();
        }
        return instance;
    }

    public void doRequestForRefereshUser(final Activity activity, UserDetailModel userDetailModel) {
        setUpQBUser(activity, userDetailModel);
    }

    private void setUpQBUser(Activity activity, UserDetailModel userDetailModel) {
        QBUser qbUser = new QBUser();
        qbUser.setFullName(userDetailModel.getStatus().getName());
        qbUser.setLogin(userDetailModel.getStatus().getUserName());
        qbUser.setPassword(Constants.WN_QB_PASSWORD);
        qbUser.setEmail(userDetailModel.getStatus().getEmail());
        registerNupdateToDB(activity, qbUser);
    }

    private void registerNupdateToDB(final Activity activity, final QBUser qbUser) {
        if (!isUpdatingQBToDB) {
            isUpdatingQBToDB = true;
            ChatHelper.getInstance().signUpQBUser(qbUser, new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    doRequestForUpdateQBId(activity, qbUser.getId());
                }

                @Override
                public void onError(QBResponseException e) {
                    Logger.e(TAG, e.getMessage());
                    if (e.getHttpStatusCode() == Constants.WN_ERR_QB_LOGIN_TAKEN) {
                        ChatHelper.getInstance().getExistingQBUser(qbUser, new QBEntityCallback<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbuser, Bundle bundle) {
                                doRequestForUpdateQBId(activity, qbuser.getId());
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                isUpdatingQBToDB = false;
                                Logger.e(TAG, e.getMessage());
                            }
                        });
                    }
                }
            });
        }
    }

    private void doRequestForUpdateQBId(final Activity activity, final Integer qbUserId) {
        String requestURL = String.format(activity.getString(R.string.get_update_chatId_by_customerId), qbUserId, globals.getUserDetails().getStatus().getCustomerId());

        new PostRequest(activity, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                isUpdatingQBToDB = false;
                UserDetailModel userDetailModel = globals.getUserDetails();
                userDetailModel.getStatus().setChatId(qbUserId);
                globals.setUserDetails(userDetailModel);
                Logger.e("QBId updated to DB");
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                isUpdatingQBToDB = false;
                Logger.e("QBId update to DB failed");
            }
        }).execute();
    }
}