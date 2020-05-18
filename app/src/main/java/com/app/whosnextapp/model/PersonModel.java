package com.app.whosnextapp.model;


import android.content.res.Resources;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.app.whosnextapp.pictures.MentionsLoader;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.tokenization.QueryToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PersonModel implements Mentionable {

    public static final Creator<PersonModel> CREATOR
            = new Creator<PersonModel>() {
        public PersonModel createFromParcel(Parcel in) {
            return new PersonModel(in);
        }

        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };
    private final String UserName;
    private final String FirstName;
    private final String LastName;
    private final String ProfilePicUrl;
    private final int CustomerId;

    public PersonModel(String userName, String firstName, String lastName, String profilePicUrl, int customerId) {
        UserName = userName;
        FirstName = firstName;
        LastName = lastName;
        ProfilePicUrl = profilePicUrl;
        CustomerId = customerId;
    }

    public PersonModel(String userName) {
        UserName = userName;
        FirstName = "";
        LastName = "";
        ProfilePicUrl = "";
        CustomerId = 0;
    }

    public PersonModel(Parcel in) {
        UserName = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        ProfilePicUrl = in.readString();
        CustomerId = in.readInt();
    }

    public String getUserName() {
        return UserName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return getUserName();
            case PARTIAL:
            case NONE:
            default:
                return "";
        }
    }

    @Override
    public MentionDeleteStyle getDeleteStyle() {
        return MentionDeleteStyle.FULL_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return getUserName().hashCode();
    }

    @Override
    public String getSuggestiblePrimaryText() {
        return getUserName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(ProfilePicUrl);
        dest.writeInt(CustomerId);
    }

    public static class PersonLoader extends MentionsLoader<PersonModel> {

        private static final String TAG = PersonLoader.class.getSimpleName();
        private PersonModel[] data;

        public PersonLoader(Resources res, SearchInFollowingFollowerModel getMygFollower) {
            super(res, getMygFollower);
        }

        @Override
        public PersonModel[] loadResponse(SearchInFollowingFollowerModel getMygFollower) {
            data = new PersonModel[getMygFollower.getCustomerList().size()];
            for (int i = 0; i < getMygFollower.getCustomerList().size(); i++) {
                data[i] = new PersonModel(getMygFollower.getCustomerList().get(i).getUserName(),
                        getMygFollower.getCustomerList().get(i).getFirstName(), getMygFollower.getCustomerList().get(i).getLastName(),
                        getMygFollower.getCustomerList().get(i).getProfilePicUrl(), getMygFollower.getCustomerList().get(i).getCustomerId());
            }
            return data;
        }

        public PersonModel[] getData() {
            return data;
        }


        // Modified to return suggestions based on both first and last name
        @Override
        public List<PersonModel> getSuggestions(QueryToken queryToken) {
            List<PersonModel> suggestions = new ArrayList<>();
            if (mData != null) {
                //                    String Name = suggestion.getUserName().toLowerCase();
                //                    if (Name.contains(queryToken.getKeywords().toLowerCase()))
                //                    {
                // }
                suggestions.addAll(Arrays.asList(mData));
            }
            return suggestions;
        }
    }

}
