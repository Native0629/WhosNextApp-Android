package com.app.whosnextapp.loginregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseAppCompatActivity {

    @BindView(R.id.et_new_password)
    AppCompatEditText et_new_password;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText et_confirm_password;

    Globals globals;

    private Activity getActivity() {
        return ResetPasswordActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        globals = (Globals) getApplicationContext();
    }

    @OnClick(R.id.btn_submit)
    public void onVerificationCodeClick() {
        if (isValid()) {
            if (et_new_password.getText().toString().equals(et_confirm_password.getText().toString())) {
                doRequestForSetNewPassword();
            } else {
                Globals.showToast(ResetPasswordActivity.this, getString(R.string.err_check_confirm_new_password));
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onBackClick(View v) {
        onBackPressed();
    }

    private boolean isValid() {
        if (et_new_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(ResetPasswordActivity.this, getString(R.string.err_empty_new_password));
            return false;
        }
        if (et_confirm_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(ResetPasswordActivity.this, getString(R.string.err_empty_Confirm_new_password));
            return false;
        }
        return true;
    }

    public void doRequestForSetNewPassword() {
        JSONObject postData = HttpRequestHandler.getInstance()
                .getResetPasswordJson(et_new_password.getText().toString());

        new PostRequest(this, postData, getString(R.string.get_reset_password_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel resetPasswordModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (resetPasswordModel != null) {
                    if (resetPasswordModel.getResult().equals(Constants.WN_SUCCESS)) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        finish();
                    } else {
                        Globals.showToast(ResetPasswordActivity.this, getString(R.string.msg_server_error));
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(ResetPasswordActivity.this, msg);
            }
        }).execute(getIntent().getStringExtra(Constants.WN_USER_ID));
    }
}






