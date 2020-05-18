package com.app.whosnextapp.apis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import com.app.whosnextapp.R;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class ConnectionDetector {

    private static AlertDialog alert = null;
    private static AlertDialog.Builder builder = null;
    private final ConnectivityManager connectivityManager;
    private Set<OnConnectivityChangedListener> listeners = new CopyOnWriteArraySet<>();

    public ConnectionDetector(Application context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(new NetworkStateReceiver(), intentFilter);
    }

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    // for video call

    //This method will return true if internet available else return false.
    private static boolean checkInternetConnection(Context mContext) {
        ConnectivityManager connection = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connection != null) {
            if (connection.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                    connection.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                    connection.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ||
                    connection.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else if (connection.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                    connection.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
                return false;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean internetCheck(Activity context, boolean showDialog) {
        if (isConnectingToInternet(context))
            return true;
        if (checkInternetConnection(context))
            return true;
        if (showDialog) {
            showAlertDialog(context, context.getString(R.string.msg_NO_INTERNET_TITLE), context.getString(R.string.msg_NO_INTERNET_MSG), false);
        }
        return false;
    }

    public static void showAlertDialog(final Activity context, String pTitle, final String pMsg, Boolean status) {
        try {

            if (alert != null && alert.isShowing())
                return;

            builder = new AlertDialog.Builder(context);
            builder.setTitle(pTitle);
            builder.setMessage(pMsg);
            builder.setCancelable(false);
            builder.setPositiveButton(context.getString(R.string.msg_goto_settings),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(intent);
                        }
                    });

            alert = builder.create();

            if (!alert.isShowing())
                alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnectedNow() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface OnConnectivityChangedListener {

        void connectivityChanged(boolean availableNow);

    }

    private class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnectedNow = isConnectedNow();

            for (OnConnectivityChangedListener listener : listeners) {
                listener.connectivityChanged(isConnectedNow);
            }
        }
    }
}
