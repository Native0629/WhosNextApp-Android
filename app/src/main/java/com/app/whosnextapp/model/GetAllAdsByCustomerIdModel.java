package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetAllAdsByCustomerIdModel implements Serializable {
    @SerializedName("AdsList")
    private ArrayList<AdsList> adsLists;

    public ArrayList<GetAllAdsByCustomerIdModel.AdsList> getAdsLists() {
        return adsLists;
    }

    public void setAdsLists(ArrayList<GetAllAdsByCustomerIdModel.AdsList> adsLists) {
        this.adsLists = adsLists;
    }

    public static class AdsList implements Serializable {
        @SerializedName("VideoWidth")
        private int VideoWidth;
        @SerializedName("VideoUrl")
        private String VideoUrl;
        @SerializedName("VideoThumbnailUrl")
        private String VideoThumbnailUrl;
        @SerializedName("VideoHeight")
        private int VideoHeight;
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TotalLikes")
        private int TotalLikes;
        @SerializedName("TotalComments")
        private int TotalComments;
        @SerializedName("TimeDiff")
        private String TimeDiff;
        @SerializedName("ProfilePicUrl")
        private String ProfilePicUrl;
        @SerializedName("OnFeaturedPage")
        private boolean OnFeaturedPage;
        @SerializedName("OnDashboard")
        private boolean OnDashboard;
        @SerializedName("IsVideo")
        private boolean IsVideo;
        @SerializedName("IsStar")
        private int IsStar;
        @SerializedName("IsPublish")
        private boolean IsPublish;
        @SerializedName("IsLiked")
        private boolean IsLiked;
        @SerializedName("IsImage")
        private boolean IsImage;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("ImageList")
        private List<ImageList> ImageList;
        @SerializedName("Description")
        private String Description;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("ButtonName")
        private String ButtonName;
        @SerializedName("ButtonLink")
        private String ButtonLink;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;
        @SerializedName("AdId")
        private String AdId;
        @SerializedName("AdCommentList")
        private ArrayList<AdCommentListModel.AdCommentList> AdCommentList;

        public int getVideoWidth() {
            return VideoWidth;
        }

        public void setVideoWidth(int VideoWidth) {
            this.VideoWidth = VideoWidth;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String VideoUrl) {
            this.VideoUrl = VideoUrl;
        }

        public String getVideoThumbnailUrl() {
            return VideoThumbnailUrl;
        }

        public void setVideoThumbnailUrl(String VideoThumbnailUrl) {
            this.VideoThumbnailUrl = VideoThumbnailUrl;
        }

        public int getVideoHeight() {
            return VideoHeight;
        }

        public void setVideoHeight(int VideoHeight) {
            this.VideoHeight = VideoHeight;
        }

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

        public String getProfilePicUrl() {
            return ProfilePicUrl;
        }

        public void setProfilePicUrl(String ProfilePicUrl) {
            this.ProfilePicUrl = ProfilePicUrl;
        }

        public boolean getOnFeaturedPage() {
            return OnFeaturedPage;
        }

        public void setOnFeaturedPage(boolean OnFeaturedPage) {
            this.OnFeaturedPage = OnFeaturedPage;
        }

        public boolean getOnDashboard() {
            return OnDashboard;
        }

        public void setOnDashboard(boolean OnDashboard) {
            this.OnDashboard = OnDashboard;
        }

        public boolean getIsVideo() {
            return IsVideo;
        }

        public void setIsVideo(boolean IsVideo) {
            this.IsVideo = IsVideo;
        }

        public int getIsStar() {
            return IsStar;
        }

        public void setIsStar(int IsStar) {
            this.IsStar = IsStar;
        }

        public boolean getIsPublish() {
            return IsPublish;
        }

        public void setIsPublish(boolean IsPublish) {
            this.IsPublish = IsPublish;
        }

        public boolean getIsLiked() {
            return IsLiked;
        }

        public void setIsLiked(boolean IsLiked) {
            this.IsLiked = IsLiked;
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

        public List<ImageList> getImageList() {
            return ImageList;
        }

        public void setImageList(List<ImageList> ImageList) {
            this.ImageList = ImageList;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
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

        public String getButtonName() {
            return ButtonName;
        }

        public void setButtonName(String ButtonName) {
            this.ButtonName = ButtonName;
        }

        public String getButtonLink() {
            return ButtonLink;
        }

        public void setButtonLink(String ButtonLink) {
            this.ButtonLink = ButtonLink;
        }

        public String getBioVideoUrl() {
            return BioVideoUrl;
        }

        public void setBioVideoUrl(String BioVideoUrl) {
            this.BioVideoUrl = BioVideoUrl;
        }

        public String getAdId() {
            return AdId;
        }

        public void setAdId(String AdId) {
            this.AdId = AdId;
        }

        public ArrayList<AdCommentListModel.AdCommentList> getAdCommentList() {
            return AdCommentList;
        }

        public void setAdCommentList(ArrayList<AdCommentListModel.AdCommentList> AdCommentList) {
            this.AdCommentList = AdCommentList;
        }

        public static class AdCommentList implements Serializable {
            @SerializedName("UserName")
            private String UserName;
            @SerializedName("IsText")
            private boolean IsText;
            @SerializedName("IsImage")
            private boolean IsImage;
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


        public static class ImageList implements Serializable {
            @SerializedName("ImageWidth")
            private int ImageWidth;
            @SerializedName("ImageUrl")
            private String ImageUrl;
            @SerializedName("ImageHeight")
            private int ImageHeight;
            @SerializedName("AdImageId")
            private int AdImageId;
            @SerializedName("AdId")
            private int AdId;

            public int getImageWidth() {
                return ImageWidth;
            }

            public void setImageWidth(int ImageWidth) {
                this.ImageWidth = ImageWidth;
            }

            public String getImageUrl() {
                return ImageUrl;
            }

            public void setImageUrl(String ImageUrl) {
                this.ImageUrl = ImageUrl;
            }

            public int getImageHeight() {
                return ImageHeight;
            }

            public void setImageHeight(int ImageHeight) {
                this.ImageHeight = ImageHeight;
            }

            public int getAdImageId() {
                return AdImageId;
            }

            public void setAdImageId(int AdImageId) {
                this.AdImageId = AdImageId;
            }

            public int getAdId() {
                return AdId;
            }

            public void setAdId(int AdId) {
                this.AdId = AdId;
            }
        }
    }
}
