package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddEditBCLCommentModel implements Serializable {

    @SerializedName("BCLCommentDetail")
    private BCLCommentDetail BCLCommentDetail;

    public BCLCommentDetail getBCLCommentDetail() {
        return BCLCommentDetail;
    }

    public void setBCLCommentDetail(BCLCommentDetail BCLCommentDetail) {
        this.BCLCommentDetail = BCLCommentDetail;
    }

    public static class BCLCommentDetail implements Serializable {
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
        @SerializedName("ImageUrl")
        private String ImageUrl;
        @SerializedName("ImageCaption")
        private String ImageCaption;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("CommentImageWidth")
        private int CommentImageWidth;
        @SerializedName("CommentImageHeight")
        private int CommentImageHeight;
        @SerializedName("Comment")
        private String Comment;
        @SerializedName("BreastCancerLegacyId")
        private int BreastCancerLegacyId;
        @SerializedName("BCLCommentId")
        private int BCLCommentId;

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

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public String getImageCaption() {
            return ImageCaption;
        }

        public void setImageCaption(String ImageCaption) {
            this.ImageCaption = ImageCaption;
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

        public int getCommentImageWidth() {
            return CommentImageWidth;
        }

        public void setCommentImageWidth(int CommentImageWidth) {
            this.CommentImageWidth = CommentImageWidth;
        }

        public int getCommentImageHeight() {
            return CommentImageHeight;
        }

        public void setCommentImageHeight(int CommentImageHeight) {
            this.CommentImageHeight = CommentImageHeight;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }

        public int getBreastCancerLegacyId() {
            return BreastCancerLegacyId;
        }

        public void setBreastCancerLegacyId(int BreastCancerLegacyId) {
            this.BreastCancerLegacyId = BreastCancerLegacyId;
        }

        public int getBCLCommentId() {
            return BCLCommentId;
        }

        public void setBCLCommentId(int BCLCommentId) {
            this.BCLCommentId = BCLCommentId;
        }
    }
}
