package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PostDetailsModel implements Serializable {

    @SerializedName("PostDetail")
    private PostDetail PostDetail;

    public PostDetail getPostDetail() {
        return PostDetail;
    }

    public void setPostDetail(PostDetail PostDetail) {
        this.PostDetail = PostDetail;
    }

    public static class PostDetail implements Serializable {
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
        @SerializedName("Result")
        private boolean Result;
        @SerializedName("ProfilePicUrl")
        private String ProfilePicUrl;
        @SerializedName("PostWidth")
        private int PostWidth;
        @SerializedName("PostUrl")
        private String PostUrl;
        @SerializedName("PostId")
        private int PostId;
        @SerializedName("PostHeight")
        private int PostHeight;
        @SerializedName("PostCommentList")
        private List<PostCommentList> PostCommentList;
        @SerializedName("NotificationStatus")
        private boolean NotificationStatus;
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
        private List<GroupVideoList> GroupVideoList;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("Caption")
        private String Caption;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;

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

        public String getPostUrl() {
            return PostUrl;
        }

        public void setPostUrl(String PostUrl) {
            this.PostUrl = PostUrl;
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

        public List<PostCommentList> getPostCommentList() {
            return PostCommentList;
        }

        public void setPostCommentList(List<PostCommentList> PostCommentList) {
            this.PostCommentList = PostCommentList;
        }

        public boolean getNotificationStatus() {
            return NotificationStatus;
        }

        public void setNotificationStatus(boolean NotificationStatus) {
            this.NotificationStatus = NotificationStatus;
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

        public List<GroupVideoList> getGroupVideoList() {
            return GroupVideoList;
        }

        public void setGroupVideoList(List<GroupVideoList> GroupVideoList) {
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

    public static class PostCommentList implements Serializable {
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TimeDiff")
        private String TimeDiff;
        @SerializedName("PostId")
        private int PostId;
        @SerializedName("PostCommentId")
        private int PostCommentId;
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

        public int getPostId() {
            return PostId;
        }

        public void setPostId(int PostId) {
            this.PostId = PostId;
        }

        public int getPostCommentId() {
            return PostCommentId;
        }

        public void setPostCommentId(int PostCommentId) {
            this.PostCommentId = PostCommentId;
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
    }

    public static class GroupVideoList implements Serializable {
        @SerializedName("VideoThumbnailUrl")
        private String VideoThumbnailUrl;
        @SerializedName("PostWidth")
        private int PostWidth;
        @SerializedName("PostUrl")
        private String PostUrl;
        @SerializedName("PostId")
        private int PostId;
        @SerializedName("PostHeight")
        private int PostHeight;
        @SerializedName("Longitude")
        private String Longitude;
        @SerializedName("Latitude")
        private String Latitude;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("GroupVideoId")
        private int GroupVideoId;
        @SerializedName("CustomerName")
        private String CustomerName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("Caption")
        private String Caption;

        public String getVideoThumbnailUrl() {
            return VideoThumbnailUrl;
        }

        public void setVideoThumbnailUrl(String VideoThumbnailUrl) {
            this.VideoThumbnailUrl = VideoThumbnailUrl;
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

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }

        public String getInsertDateTime() {
            return InsertDateTime;
        }

        public void setInsertDateTime(String InsertDateTime) {
            this.InsertDateTime = InsertDateTime;
        }

        public int getGroupVideoId() {
            return GroupVideoId;
        }

        public void setGroupVideoId(int GroupVideoId) {
            this.GroupVideoId = GroupVideoId;
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
    }
}
