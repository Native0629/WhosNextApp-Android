package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllPictureOfYouModel implements Serializable {
    @SerializedName("PostList")
    private ArrayList<PostList> postLists;

    public ArrayList<PostList> getPostList() {
        return postLists;
    }

    public void setPostList(ArrayList<PostList> postList) {
        this.postLists = postList;
    }

    public static class PostList implements Serializable {
        @SerializedName("VimeoVideoId")
        private String VimeoVideoId;
        @SerializedName("VideoThumbnailUrl")
        private String VideoThumbnailUrl;
        @SerializedName("VideoThumbnailBase64String")
        private String VideoThumbnailBase64String;
        @SerializedName("VideoID")
        private String VideoID;
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
        @SerializedName("TagedCustomers")
        private String TagedCustomers;
        @SerializedName("Result")
        private boolean Result;
        @SerializedName("ProfilePicUrl")
        private String ProfilePicUrl;
        @SerializedName("PostWidth")
        private int PostWidth;
        @SerializedName("PostUrlBase64String")
        private String PostUrlBase64String;
        @SerializedName("PostUrl")
        private String PostUrl;
        @SerializedName("PostTagList")
        private String PostTagList;
        @SerializedName("PostId")
        private int PostId;
        @SerializedName("PostHeight")
        private int PostHeight;
        @SerializedName("PostCommentList")
        private String PostCommentList;
        @SerializedName("NotificationStatus")
        private boolean NotificationStatus;
        @SerializedName("Message")
        private String Message;
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
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("Id")
        private int Id;
        @SerializedName("GroupVideoList")
        private String GroupVideoList;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("Caption")
        private String Caption;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;

        public String getVimeoVideoId() {
            return VimeoVideoId;
        }

        public void setVimeoVideoId(String VimeoVideoId) {
            this.VimeoVideoId = VimeoVideoId;
        }

        public String getVideoThumbnailUrl() {
            return VideoThumbnailUrl;
        }

        public void setVideoThumbnailUrl(String VideoThumbnailUrl) {
            this.VideoThumbnailUrl = VideoThumbnailUrl;
        }

        public String getVideoThumbnailBase64String() {
            return VideoThumbnailBase64String;
        }

        public void setVideoThumbnailBase64String(String VideoThumbnailBase64String) {
            this.VideoThumbnailBase64String = VideoThumbnailBase64String;
        }

        public String getVideoID() {
            return VideoID;
        }

        public void setVideoID(String VideoID) {
            this.VideoID = VideoID;
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

        public String getTagedCustomers() {
            return TagedCustomers;
        }

        public void setTagedCustomers(String TagedCustomers) {
            this.TagedCustomers = TagedCustomers;
        }

        public boolean getResult() {
            return Result;
        }

        public void setResult(boolean Result) {
            this.Result = Result;
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

        public String getPostUrlBase64String() {
            return PostUrlBase64String;
        }

        public void setPostUrlBase64String(String PostUrlBase64String) {
            this.PostUrlBase64String = PostUrlBase64String;
        }

        public String getPostUrl() {
            return PostUrl;
        }

        public void setPostUrl(String PostUrl) {
            this.PostUrl = PostUrl;
        }

        public String getPostTagList() {
            return PostTagList;
        }

        public void setPostTagList(String PostTagList) {
            this.PostTagList = PostTagList;
        }

        public int getPostId() {
            return PostId;
        }

        public void setPostId(int PostId) {
            this.PostId = PostId;
        }

        public int getPostHeight() {
            return PostHeight;
        }

        public void setPostHeight(int PostHeight) {
            this.PostHeight = PostHeight;
        }

        public String getPostCommentList() {
            return PostCommentList;
        }

        public void setPostCommentList(String PostCommentList) {
            this.PostCommentList = PostCommentList;
        }

        public boolean getNotificationStatus() {
            return NotificationStatus;
        }

        public void setNotificationStatus(boolean NotificationStatus) {
            this.NotificationStatus = NotificationStatus;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
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

        public String getGroupVideoList() {
            return GroupVideoList;
        }

        public void setGroupVideoList(String GroupVideoList) {
            this.GroupVideoList = GroupVideoList;
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

        public String getBioVideoUrl() {
            return BioVideoUrl;
        }

        public void setBioVideoUrl(String BioVideoUrl) {
            this.BioVideoUrl = BioVideoUrl;
        }
    }
}
