package com.app.whosnextapp.loginregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.app.whosnextapp.R;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationCodeActivity extends BaseAppCompatActivity {

    @BindView(R.id.et_verification_code)
    AppCompatEditText et_verification_code;

    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        globals = (Globals) getApplicationContext();
    }

    @OnClick(R.id.btn_submit)
    public void onVerificationCodeClick() {
        if (et_verification_code.getText() != null && !et_verification_code.getText().toString().isEmpty()) {
            forVerificationCode();
        } else {
            Globals.showToast(VerificationCodeActivity.this, getString(R.string.err_empty_verification_code));
        }
    }

    @OnClick(R.id.iv_back)
    public void onBackClick(View v) {
        onBackPressed();
    }

    public void forVerificationCode() {
        String VCode = getIntent().getStringExtra(Constants.WN_VERIFICATION_CODE);
        String UserID = getIntent().getStringExtra(Constants.WN_USER_ID);
        if (et_verification_code.getText().toString().equals(VCode)) {
            Intent intent = new Intent(VerificationCodeActivity.this, ResetPasswordActivity.class);
            intent.putExtra(Constants.WN_USER_ID, UserID);
            startActivity(intent);
            finish();
        } else {
            Globals.showToast(VerificationCodeActivity.this, getString(R.string.It_is_not_valid_code));
        }
    }
}
