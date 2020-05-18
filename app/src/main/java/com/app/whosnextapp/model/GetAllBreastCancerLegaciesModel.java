package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllBreastCancerLegaciesModel implements Serializable {

    @SerializedName("BreastCancerLegaciesList")
    private ArrayList<BreastCancerLegaciesList> breastCancerLegaciesListArrayList;

    public ArrayList<BreastCancerLegaciesList> getBreastCancerLegaciesListArrayList() {
        return breastCancerLegaciesListArrayList;
    }

    public void setBreastCancerLegaciesListArrayList(ArrayList<BreastCancerLegaciesList> breastCancerLegaciesListArrayList) {
        this.breastCancerLegaciesListArrayList = breastCancerLegaciesListArrayList;
    }

    public static class BreastCancerLegaciesList implements Serializable {
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TotalLikes")
        private int TotalLikes;
        @SerializedName("TotalComments")
        private int TotalComments;
        @SerializedName("TimeDiff")
        private String TimeDiff;
        @SerializedName("SubPostUserName")
        private String SubPostUserName;
        @SerializedName("SubPostProfilePicUrl")
        private String SubPostProfilePicUrl;
        @SerializedName("SubPostCustomerId")
        private int SubPostCustomerId;
        @SerializedName("SubPostBioVideoUrl")
        private String SubPostBioVideoUrl;
        @SerializedName("ProfilePicUrl")
        private String ProfilePicUrl;
        @SerializedName("Name")
        private String Name;
        @SerializedName("MainPostId")
        private int MainPostId;
        @SerializedName("IsWhiteCarnation")
        private boolean IsWhiteCarnation;
        @SerializedName("IsStripedCarnation")
        private boolean IsStripedCarnation;
        @SerializedName("IsStar")
        private int IsStar;
        @SerializedName("IsPinkWhiteCarnation")
        private boolean IsPinkWhiteCarnation;
        @SerializedName("IsLiked")
        private boolean IsLiked;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("ImageUrl")
        private String ImageUrl;
        @SerializedName("FlowerType")
        private int FlowerType;
        @SerializedName("Description")
        private String Description;
        @SerializedName("DOP")
        private String DOP;
        @SerializedName("DOB")
        private String DOB;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("Caption")
        private String Caption;
        @SerializedName("BreastCancerLegacyId")
        private int BreastCancerLegacyId;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;
        @SerializedName("BCLWidth")
        private int BCLWidth;
        @SerializedName("BCLHeight")
        private int BCLHeight;
        @SerializedName("BCLCommentList")
        private ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public int getTotalLikes() {
            return TotalLikes;
        }

        public void setTotalLikes(int TotalLikes) {
            this.TotalLikes = TotalLikes;
        }

        public int getTotalComments() {
            return TotalComments;
        }

        public void setTotalComments(int TotalComments) {
            this.TotalComments = TotalComments;
        }

        public String getTimeDiff() {
            return TimeDiff;
        }

        public void setTimeDiff(String TimeDiff) {
            this.TimeDiff = TimeDiff;
        }

        public String getSubPostUserName() {
            return SubPostUserName;
        }

        public void setSubPostUserName(String SubPostUserName) {
            this.SubPostUserName = SubPostUserName;
        }

        public String getSubPostProfilePicUrl() {
            return SubPostProfilePicUrl;
        }

        public void setSubPostProfilePicUrl(String SubPostProfilePicUrl) {
            this.SubPostProfilePicUrl = SubPostProfilePicUrl;
        }

        public int getSubPostCustomerId() {
            return SubPostCustomerId;
        }

        public void setSubPostCustomerId(int SubPostCustomerId) {
            this.SubPostCustomerId = SubPostCustomerId;
        }

        public String getSubPostBioVideoUrl() {
            return SubPostBioVideoUrl;
        }

        public void setSubPostBioVideoUrl(String SubPostBioVideoUrl) {
            this.SubPostBioVideoUrl = SubPostBioVideoUrl;
        }

        public String getProfilePicUrl() {
            return ProfilePicUrl;
        }

        public void setProfilePicUrl(String ProfilePicUrl) {
            this.ProfilePicUrl = ProfilePicUrl;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getMainPostId() {
            return MainPostId;
        }

        public void setMainPostId(int MainPostId) {
            this.MainPostId = MainPostId;
        }

        public boolean getIsWhiteCarnation() {
            return IsWhiteCarnation;
        }

        public void setIsWhiteCarnation(boolean IsWhiteCarnation) {
            this.IsWhiteCarnation = IsWhiteCarnation;
        }

        public boolean getIsStripedCarnation() {
            return IsStripedCarnation;
        }

        public void setIsStripedCarnation(boolean IsStripedCarnation) {
            this.IsStripedCarnation = IsStripedCarnation;
        }

        public int getIsStar() {
            return IsStar;
        }

        public void setIsStar(int IsStar) {
            this.IsStar = IsStar;
        }

        public boolean getIsPinkWhiteCarnation() {
            return IsPinkWhiteCarnation;
        }

        public void setIsPinkWhiteCarnation(boolean IsPinkWhiteCarnation) {
            this.IsPinkWhiteCarnation = IsPinkWhiteCarnation;
        }

        public boolean getIsLiked() {
            return IsLiked;
        }

        public void setIsLiked(boolean IsLiked) {
            this.IsLiked = IsLiked;
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

        public int getFlowerType() {
            return FlowerType;
        }

        public void setFlowerType(int FlowerType) {
            this.FlowerType = FlowerType;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getDOP() {
            return DOP;
        }

        public void setDOP(String DOP) {
            this.DOP = DOP;
        }

        public String getDOB() {
            return DOB;
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
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

        public String getCaption() {
            return Caption;
        }

        public void setCaption(String Caption) {
            this.Caption = Caption;
        }

        public int getBreastCancerLegacyId() {
            return BreastCancerLegacyId;
        }

        public void setBreastCancerLegacyId(int BreastCancerLegacyId) {
            this.BreastCancerLegacyId = BreastCancerLegacyId;
        }

        public String getBioVideoUrl() {
            return BioVideoUrl;
        }

        public void setBioVideoUrl(String BioVideoUrl) {
            this.BioVideoUrl = BioVideoUrl;
        }

        public int getBCLWidth() {
            return BCLWidth;
        }

        public void setBCLWidth(int BCLWidth) {
            this.BCLWidth = BCLWidth;
        }

        public int getBCLHeight() {
            return BCLHeight;
        }

        public void setBCLHeight(int BCLHeight) {
            this.BCLHeight = BCLHeight;
        }

        public ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> getBCLCommentList() {
            return BCLCommentList;
        }

        public void setBCLCommentList(ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList) {
            this.BCLCommentList = BCLCommentList;
        }

        public static class BCLCommentList implements Serializable {

            @SerializedName("UserName")
            private String UserName;
            @SerializedName("IsText")
            private boolean IsText;
            @SerializedName("IsImage")
            private boolean IsImage;
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
}
