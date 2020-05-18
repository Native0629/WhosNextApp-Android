package com.app.whosnextapp.pictures;

import android.content.res.Resources;

import com.app.whosnextapp.model.SearchInFollowingFollowerModel;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.tokenization.QueryToken;

import java.util.ArrayList;
import java.util.List;

public abstract class MentionsLoader<T extends Mentionable> {

    protected T[] mData;

    public MentionsLoader(final Resources res, SearchInFollowingFollowerModel getMygFollower) {
        mData = loadResponse(getMygFollower);
    }

    public abstract T[] loadResponse(SearchInFollowingFollowerModel getMygFollower);

    public List<T> getSuggestions(QueryToken queryToken) {
        String prefix = queryToken.getKeywords().toLowerCase();
        List<T> suggestions = new ArrayList<>();
        if (mData != null) {
            for (T suggestion : mData) {
                String name = suggestion.getSuggestiblePrimaryText().toLowerCase();
                if (name.startsWith(prefix)) {
                    suggestions.add(suggestion);
                }
            }
        }
        return suggestions;
    }
}
