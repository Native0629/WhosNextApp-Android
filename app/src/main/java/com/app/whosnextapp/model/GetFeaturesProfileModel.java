package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetFeaturesProfileModel implements Serializable {

    @SerializedName("DashboardList")
    private ArrayList<DashboardList> DashboardList;

    public ArrayList<GetFeaturesProfileModel.DashboardList> getDashboardList() {
        return DashboardList;
    }

    public void setDashboardList(ArrayList<GetFeaturesProfileModel.DashboardList> dashboardList) {
        DashboardList = dashboardList;
    }

    public static class DashboardList implements Serializable {

        @SerializedName("VideoThumbnailUrl")
        private String VideoThumbnailUrl;
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TotalViews")
        private int TotalViews;
        @SerializedName("TotalLikes")
        private int TotalLikes;
        @SerializedName("TotalComments")
        private int TotalComments;
        @SerializedName("TimeDiff")
        private String TimeDiff;
        @SerializedName("TaggedCustomers")
        private String TaggedCustomers;
        @SerializedName("ProfilePicUrl")
        private String ProfilePicUrl;
        @SerializedName("PostWidth")
        private int PostWidth;
        @SerializedName("PostUrl")
        private String PostUrl;
        @SerializedName("PostHeight")
        private int PostHeight;
        @SerializedName("NotificationStatus")
        private boolean NotificationStatus;
        @SerializedName("Name")
        private String Name;
        @SerializedName("Longitude")
        private String Longitude;
        @SerializedName("Location")
        private String Location;
        @SerializedName("Latitude")
        private String Latitude;
        @SerializedName("IsVideo")
        private boolean IsVideo;
        @SerializedName("IsStar")
        private int IsStar;
        @SerializedName("IsPrivate")
        private boolean IsPrivate;
        @SerializedName("IsLiked")
        private boolean IsLiked;
        @SerializedName("IsImage")
        private boolean IsImage;
        @SerializedName("IsGroupVideo")
        private boolean IsGroupVideo;
        @SerializedName("IsAd")
        private boolean IsAd;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("Id")
        private int Id;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("CommentList")
        // private List<String> CommentList;
        private ArrayList<PostCommentListModel.PostCommentList> CommentList;
        @SerializedName("Caption")
        private String Caption;
        @SerializedName("ButtonName")
        private String ButtonName;
        @SerializedName("ButtonLink")
        private String ButtonLink;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;
        @SerializedName("AdsDescription")
        private String AdsDescription;
        @SerializedName("AdVideoUrl")
        private String AdVideoUrl;
        @SerializedName("GroupVideoList")
        private ArrayList<GroupVideoModel> GroupVideoList;
        @SerializedName("AdImageList")
        private List<String> AdImageList;

        public String getVideoThumbnailUrl() {
            return VideoThumbnailUrl;
        }

        public void setVideoThumbnailUrl(String VideoThumbnailUrl) {
            this.VideoThumbnailUrl = VideoThumbnailUrl;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public int getTotalViews() {
            return TotalViews;
        }

        public void setTotalViews(int TotalViews) {
            this.TotalViews = TotalViews;
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

        public String getTaggedCustomers() {
            return TaggedCustomers;
        }

        public void setTaggedCustomers(String TaggedCustomers) {
            this.TaggedCustomers = TaggedCustomers;
        }

        public String getProfilePicUrl() {
            return ProfilePicUrl;
        }

        public void setProfilePicUrl(String ProfilePicUrl) {
            this.ProfilePicUrl = ProfilePicUrl;
        }

        public int getPostWidth() {
            return PostWidth;
        }

        public void setPostWidth(int PostWidth) {
            this.PostWidth = PostWidth;
        }

        public String getPostUrl() {
            return PostUrl;
        }

        public void setPostUrl(String PostUrl) {
            this.PostUrl = PostUrl;
        }

        public int getPostHeight() {
            return PostHeight;
        }

        public void setPostHeight(int PostHeight) {
            this.PostHeight = PostHeight;
        }

        public boolean getNotificationStatus() {
            return NotificationStatus;
        }

        public void setNotificationStatus(boolean NotificationStatus) {
            this.NotificationStatus = NotificationStatus;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
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

        public boolean getIsPrivate() {
            return IsPrivate;
        }

        public void setIsPrivate(boolean IsPrivate) {
            this.IsPrivate = IsPrivate;
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

        public boolean getIsGroupVideo() {
            return IsGroupVideo;
        }

        public void setIsGroupVideo(boolean IsGroupVideo) {
            this.IsGroupVideo = IsGroupVideo;
        }

        public boolean getIsAd() {
            return IsAd;
        }

        public void setIsAd(boolean IsAd) {
            this.IsAd = IsAd;
        }

        public String getInsertDateTime() {
            return InsertDateTime;
        }

        public void setInsertDateTime(String InsertDateTime) {
            this.InsertDateTime = InsertDateTime;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int CustomerId) {
            this.CustomerId = CustomerId;
        }

//        public List<String> getCommentList() {
//            return CommentList;
//        }
//
//        public void setCommentList(List<String> CommentList) {
//            this.CommentList = CommentList;
//        }

        public ArrayList<PostCommentListModel.PostCommentList> getCommentList() {
            return CommentList;
        }

        public void setCommentList(ArrayList<PostCommentListModel.PostCommentList> CommentList) {
            this.CommentList = CommentList;
        }

        public String getCaption() {
            return Caption;
        }

        public void setCaption(String Caption) {
            this.Caption = Caption;
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

        public String getAdsDescription() {
            return AdsDescription;
        }

        public void setAdsDescription(String AdsDescription) {
            this.AdsDescription = AdsDescription;
        }

        public String getAdVideoUrl() {
            return AdVideoUrl;
        }

        public void setAdVideoUrl(String AdVideoUrl) {
            this.AdVideoUrl = AdVideoUrl;
        }

        public ArrayList<GroupVideoModel> getGroupVideoList() {
            return GroupVideoList;
        }

        public void setGroupVideoList(ArrayList<GroupVideoModel> GroupVideoList) {
            this.GroupVideoList = GroupVideoList;
        }

        public List<String> getAdImageList() {
            return AdImageList;
        }

        public void setAdImageList(List<String> AdImageList) {
            this.AdImageList = AdImageList;
        }
    }
}
