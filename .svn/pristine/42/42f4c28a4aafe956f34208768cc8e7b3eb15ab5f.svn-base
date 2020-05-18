package com.app.whosnextapp.pictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.GetCustomerProfileByUserName;
import com.app.whosnextapp.model.PersonModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;
import com.linkedin.android.spyglass.mentions.MentionSpan;
import com.linkedin.android.spyglass.mentions.MentionSpanConfig;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.orhanobut.logger.Logger;

public class MentionHelper {
    public static SpannableString decodeComments(final Activity context, final String comment, int color) {
        SpannableString encode_str = new SpannableString(comment);
        int lastIndex = 0;

        while (lastIndex < comment.length() &&
                comment.indexOf(Constants.WN_MentionPrefix, lastIndex) != -1) {
            int startIndex = comment.indexOf(Constants.WN_MentionPrefix, lastIndex);
            int endIndex = comment.indexOf(Constants.AW_MentionSuffix, startIndex);
            endIndex = endIndex == -1 ? comment.length() : endIndex;
            if (lastIndex == endIndex) //prevent infinite
                break;
            if (endIndex > startIndex) {
                String subStr =
                        comment.substring(startIndex, endIndex);
                if (!subStr.contains(" ")) {
                    Logger.e("sub comment", subStr);
                    try {
                        Mentionable mentionable = new PersonModel(subStr);
                        MentionSpanConfig.Builder builder = new MentionSpanConfig.Builder().setSelectedMentionTextColor(color);

                        MentionSpan span = new MentionSpan(mentionable, builder.build(), new MentionSpan.OnMentionCLickEvent() {
                            @Override
                            public void OnMentionClick(Mentionable mention) {
                                doRequestToCustomerProfileDetails(context, mention);
                            }
                        });
                        encode_str.setSpan(new ForegroundColorSpan(Color.parseColor("#FE2E1C")), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        encode_str.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } catch (NumberFormatException ignored) {

                    }
                } else endIndex = comment.indexOf(" ", startIndex);
            }
            lastIndex = endIndex;
        }
        return encode_str;
    }

    public static SpannableStringBuilder decodeComment(final Activity context, final String comment, String userName, int color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString encode_str_userName = new SpannableString(userName);
        encode_str_userName.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6699")), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString encode_str = new SpannableString(comment);

        int lastIndex = 0;

        while (lastIndex < comment.length() &&
                comment.indexOf(Constants.WN_MentionPrefix, lastIndex) != -1) {
            int startIndex = comment.indexOf(Constants.WN_MentionPrefix, lastIndex);
            int endIndex = comment.indexOf(Constants.AW_MentionSuffix, startIndex);
            endIndex = endIndex == -1 ? comment.length() : endIndex;
            if (lastIndex == endIndex) //prevent infinite
                break;
            if (endIndex > startIndex) {
                String subStr =
                        comment.substring(startIndex, endIndex);
                if (!subStr.contains(" ")) {
                    Logger.e("sub comment", subStr);
                    try {
                        Mentionable mentionable = new PersonModel(subStr);
                        MentionSpanConfig.Builder builder = new MentionSpanConfig.Builder().setSelectedMentionTextColor(color);

                        MentionSpan span = new MentionSpan(mentionable, builder.build(), new MentionSpan.OnMentionCLickEvent() {
                            @Override
                            public void OnMentionClick(Mentionable mention) {
                                doRequestToCustomerProfileDetails(context, mention);
                            }
                        });

                        encode_str.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6699")), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        encode_str.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } catch (NumberFormatException ignored) {

                    }
                } else endIndex = comment.indexOf(" ", startIndex);
            }
            lastIndex = endIndex;
        }
        spannableStringBuilder.append(encode_str_userName);
        spannableStringBuilder.append(" ");
        spannableStringBuilder.append(encode_str);
        return spannableStringBuilder;
    }

    private static void doRequestToCustomerProfileDetails(final Activity context, Mentionable mention) {
        Globals globals = (Globals) context.getApplicationContext();
        final String requestURL = String.format(context.getString(R.string.get_customer_profileBy_Username), String.valueOf(((PersonModel) mention).getUserName().substring(1)));
        new PostRequest(context, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetCustomerProfileByUserName customerProfileByUserName = new Gson().fromJson(response, GetCustomerProfileByUserName.class);
                if (customerProfileByUserName.getCustomerDetail() != null) {
                    Intent intent = new Intent(context, OtherUserProfileActivity.class);
                    intent.putExtra(Constants.WN_CUSTOMER_ID, customerProfileByUserName.getCustomerDetail().getCustomerId());
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(context, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }
}
