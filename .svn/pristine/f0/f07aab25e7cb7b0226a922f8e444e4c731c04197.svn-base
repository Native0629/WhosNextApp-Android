package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryModel implements Serializable {

    @SerializedName("CategoryList")
    private ArrayList<CategoryList> categoryList = null;

    public ArrayList<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryList> categoryList) {
        this.categoryList = categoryList;
    }

    public static class CategoryList implements Serializable {

        public boolean isSelected = false;

        @SerializedName("CategoryId")
        private Integer categoryId;
        @SerializedName("CategoryName")
        private String categoryName;

        public CategoryList(Integer categoryId, String categoryName, boolean isSelected) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.isSelected = isSelected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
