package com.app.whosnextapp.drawer;

import android.widget.Spinner;

import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.NothingSelectedSpinnerAdapter;

import java.util.ArrayList;

public class CategorySpinnerAdapter extends NothingSelectedSpinnerAdapter<String> {

    private ArrayList<String> mDocuments;

    public CategorySpinnerAdapter(Spinner spinner) {
        super(spinner);
    }

    public void doRefresh(ArrayList<String> documents) {
        mDocuments = documents;
        notifyDataSetChanged();
    }

    @Override
    protected String getNothingSelectedText(String nothingSelectedDataItem) {
        return nothingSelectedDataItem;
    }

    @Override
    protected String getNothingSelectedText() {
        return Constants.WN_SELECT_CATEGORY;
    }

    @Override
    protected String getDataItemText(int position) {
        return getDataItem(position);
    }

    @Override
    protected int getDataItemCount() {
        return mDocuments == null ? 0 : mDocuments.size();
    }

    @Override
    public String getDataItem(int position) {
        return mDocuments.get(position);
    }
}


