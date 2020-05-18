package com.app.whosnextapp.loginregistration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.UserDetailModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseAppCompatActivity {
    @BindView(R.id.et_email)
    AppCompatEditText et_email;
    @BindView(R.id.et_confirm_email)
    AppCompatEditText et_confirm_email;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        globals = (Globals) getApplicationContext();
    }

    @OnClick(R.id.iv_back)
    public void onBackClick(View v) {
        onBackPressed();
    }

    private boolean isValid() {
        if (et_email.getText().toString().trim().isEmpty()) {
            Globals.showToast(ForgotPasswordActivity.this, getString(R.string.err_empty_emailId));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString().trim()).matches()) {
            Globals.showToast(ForgotPasswordActivity.this, getString(R.string.err_valid_emailId));
            return false;
        }
        if (et_confirm_email.getText().toString().trim().isEmpty()) {
            Globals.showToast(ForgotPasswordActivity.this, getString(R.string.err_empty_emailId));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(et_confirm_email.getText().toString().trim()).matches()) {
            Globals.showToast(ForgotPasswordActivity.this, getString(R.string.err_valid_emailId));
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_submit)
    public void onForgotPasswordClick() {
        if (isValid()) {
            if (et_email.getText().toString().equals(et_confirm_email.getText().toString())) {
                doRequestForForgotPassword();
            } else {
                Globals.showToast(ForgotPasswordActivity.this, getString(R.string.err_check_confirm_emailId));
            }
        }
    }

    public void doRequestForForgotPassword() {
        final String requestURL = String.format(getString(R.string.get_send_verification_code_url), et_email.getText().toString());
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                UserDetailModel userDetailModel = new Gson().fromJson(response, UserDetailModel.class);
                if (userDetailModel.getStatus().getResult().equals(Constants.WN_SUCCESS)) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, VerificationCodeActivity.class);
                    intent.putExtra(Constants.WN_VERIFICATION_CODE, userDetailModel.getStatus().getVerificationCode());
                    intent.putExtra(Constants.WN_USER_ID, userDetailModel.getStatus().getUserId());
                    startActivity(intent);
                    finish();
                } else {
                    Globals.showToast(ForgotPasswordActivity.this, getString(R.string.your_confirm_email_id_does_not_match));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(ForgotPasswordActivity.this, msg);
            }
        }).execute();
    }
}
