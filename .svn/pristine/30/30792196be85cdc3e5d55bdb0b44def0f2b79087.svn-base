package com.app.whosnextapp.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.loginregistration.LoginActivity;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordFragment extends Fragment {
    @BindView(R.id.et_current_password)
    AppCompatEditText et_current_password;
    @BindView(R.id.et_new_password)
    AppCompatEditText et_new_password;
    @BindView(R.id.et_retype_new_password)
    AppCompatEditText et_retype_new_password;

    DrawerLayout drawer;
    Globals globals;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init() {
        if (getActivity() != null) {
            globals = (Globals) getActivity().getApplicationContext();
            drawer = getActivity().findViewById(R.id.drawer_layout);
        }
    }

    @OnClick(R.id.btn_update)
    public void onUpdatePassword() {
        if (IsValid()) {
            doRequestToUpdatePassword();
        }
    }

    private void doRequestToUpdatePassword() {
        if (!ConnectionDetector.internetCheck(getActivity(), true))
            return;

        JSONObject postData = HttpRequestHandler.getInstance().getChangePasswordJson(et_current_password.getText().toString(), et_new_password.getText().toString());

        new PostRequest(getActivity(), postData, getString(R.string.get_change_password_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel resetPasswordModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (resetPasswordModel != null) {
                    if (resetPasswordModel.getResult().equals(Constants.WN_SUCCESS)) {
                        globals.setUserDetails(null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        Globals.showToast(getActivity(), getString(R.string.msg_password_change_success));
                        getActivity().finishAffinity();
                    } else {
                        Globals.showToast(getActivity(), getString(R.string.msg_server_error_wrong_password));
                    }
                } else {
                    Globals.showToast(getActivity(), getString(R.string.msg_server_error));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    @OnClick(R.id.iv_menu)
    public void onMenuClick() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private boolean IsValid() {
        if (et_current_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(getActivity(), getString(R.string.err_empty_current_password));
            return false;
        }
        if (et_new_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(getActivity(), getString(R.string.err_empty_password));
            return false;
        }
        if (et_retype_new_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(getActivity(), getString(R.string.err_empty_retype_password));
            return false;
        }
        if (et_new_password.length() < 6) {
            Globals.showToast(getActivity(), getString(R.string.err_min_length_password));
            return false;
        }
        if (!et_retype_new_password.getText().toString().trim().equals(et_new_password.getText().toString().trim())) {
            Globals.showToast(getActivity(), getString(R.string.err_password_not_equals));
            return false;
        }
        if (et_current_password.getText().toString().trim().equals(et_new_password.getText().toString().trim())) {
            Globals.showToast(getActivity(), getString(R.string.err_password_and_new_password_are_same));
            return false;
        }
        return true;
    }
}
