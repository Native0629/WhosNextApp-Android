package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class AdCommentListModel implements Serializable {
    @SerializedName("AdCommentList")

    private ArrayList<AdCommentList> AdCommentList;

    public ArrayList<AdCommentListModel.AdCommentList> getAdCommentList() {
        return AdCommentList;
    }

    public void setAdCommentList(ArrayList<AdCommentListModel.AdCommentList> adCommentList) {
        AdCommentList = adCommentList;
    }

    public static class AdCommentList implements Serializable {


        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TimeDiff")
        private String TimeDiff;
        @SerializedName("IsText")
        private boolean IsText;
        @SerializedName("IsImage")
        private boolean IsImage;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("Comment")
        private String Comment;
        @SerializedName("AdId")
        private int AdId;
        @SerializedName("AdCommentId")
        private int AdCommentId;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getTimeDiff() {
            return TimeDiff;
        }

        public void setTimeDiff(String TimeDiff) {
            this.TimeDiff = TimeDiff;
        }

        public boolean getIsText() {
            return IsText;
        }

        public void setIsText(boolean IsText) {
            this.IsText = IsText;
        }

        public boolean getIsImage() {
            return IsImage;
        }

        public void setIsImage(boolean IsImage) {
            this.IsImage = IsImage;
        }

        public String getInsertDateTime() {
            return InsertDateTime;
        }

        public void setInsertDateTime(String InsertDateTime) {
            this.InsertDateTime = InsertDateTime;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String CustomerName) {
            this.CustomerName = CustomerName;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int CustomerId) {
            this.CustomerId = CustomerId;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }

        public int getAdId() {
            return AdId;
        }

        public void setAdId(int AdId) {
            this.AdId = AdId;
        }

        public int getAdCommentId() {
            return AdCommentId;
        }

        public void setAdCommentId(int AdCommentId) {
            this.AdCommentId = AdCommentId;
        }
    }
}
