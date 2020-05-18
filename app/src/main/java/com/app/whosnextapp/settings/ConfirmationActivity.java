package com.app.whosnextapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.AddSubscriptionsModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmationActivity extends BaseAppCompatActivity {

    @BindView(R.id.paymentId)
    TextView paymentId;
    @BindView(R.id.paymentStatus)
    TextView paymentStatus;
    @BindView(R.id.paymentAmount)
    TextView paymentAmt;

    String Status;
    Globals globals;
    JSONObject jsonDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        try {
            jsonDetails = new JSONObject(getIntent().getStringExtra(Constants.WN_PAYMENT_DETAILS));
            showDetails(jsonDetails.getJSONObject(getString(R.string.response)), getIntent().getStringExtra(Constants.WN_PAYMENT_AMOUNT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            paymentId.setText(response.getString(getString(R.string.id)));
            Status = response.getString(getString(R.string.state));
            paymentStatus.setText(Status);
            paymentAmt.setText("$" + paymentAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (Status.equals(Constants.WN_APPROVED)) {
//                        Intent intent = new Intent(ConfirmationActivity.this, SubscribeActivity.class);
//                        startActivity(intent);
//                    }
//                }
//            }, 2000);

        if (Status.equals(Constants.WN_APPROVED)) {
            JSONObject PostData = HttpRequestHandler.getInstance()
                    .getAddSubscriptionsJson(getIntent().getStringExtra(Constants.WN_SUBSCRIPTION_TYPE_ID), jsonDetails);

            new PostRequest(this, PostData, getString(R.string.add_subscriptions), true, true, new ResponseListener() {
                @Override
                public void onSucceedToPostCall(String response) {
                    AddSubscriptionsModel addSubscriptionsModel = new Gson().fromJson(response, AddSubscriptionsModel.class);
                    if (addSubscriptionsModel != null) {
                        Intent intent = new Intent(ConfirmationActivity.this, SubscribeActivity.class);
                        Globals.showToast(ConfirmationActivity.this, getString(R.string.your_payment));
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailedToPostCall(int statusCode, String msg) {

                }
            }).execute(globals.getUserDetails().getStatus().getUserId());
        }
    }
}
