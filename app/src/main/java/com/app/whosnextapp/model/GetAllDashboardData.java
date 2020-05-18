package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllDashboardData implements Serializable {
    @SerializedName("TotalSnippetsCount")
    private int TotalSnippetsCount;
    @SerializedName("TotalPostCount")
    private int TotalPostCount;
    @SerializedName("TotalPost")
    private int TotalPost;
    @SerializedName("TotalFollwers")
    private int TotalFollwers;
    @SerializedName("TotalFollowings")
    private int TotalFollowings;
    @SerializedName("SubscriptionTypeId")
    private String SubscriptionTypeId;
    @SerializedName("SubscriptionEndDate")
    private String SubscriptionEndDate;
    @SerializedName("SnippetsList")
    private ArrayList<SnippetsList> SnippetsList;
    @SerializedName("NotificationCount")
    private int NotificationCount;
    @SerializedName("IsSubscribed")
    private boolean IsSubscribed;
    @SerializedName("DashboardList")
    private ArrayList<DashboardList> DashboardList;
    @SerializedName("CustomerId")
    private int CustomerId;

    public int getTotalSnippetsCount() {
        return TotalSnippetsCount;
    }

    public void setTotalSnippetsCount(int TotalSnippetsCount) {
        this.TotalSnippetsCount = TotalSnippetsCount;
    }

    public int getTotalPostCount() {
        return TotalPostCount;
    }

    public void setTotalPostCount(int TotalPostCount) {
        this.TotalPostCount = TotalPostCount;
    }

    public int getTotalPost() {
        return TotalPost;
    }

    public void setTotalPost(int TotalPost) {
        this.TotalPost = TotalPost;
    }

    public int getTotalFollwers() {
        return TotalFollwers;
    }

    public void setTotalFollwers(int TotalFollwers) {
        this.TotalFollwers = TotalFollwers;
    }

    public int getTotalFollowings() {
        return TotalFollowings;
    }

    public void setTotalFollowings(int TotalFollowings) {
        this.TotalFollowings = TotalFollowings;
    }

    public String getSubscriptionTypeId() {
        return SubscriptionTypeId;
    }

    public void setSubscriptionTypeId(String SubscriptionTypeId) {
        this.SubscriptionTypeId = SubscriptionTypeId;
    }

    public String getSubscriptionEndDate() {
        return SubscriptionEndDate;
    }

    public void setSubscriptionEndDate(String SubscriptionEndDate) {
        this.SubscriptionEndDate = SubscriptionEndDate;
    }

    public ArrayList<SnippetsList> getSnippetsList() {
        return SnippetsList;
    }

    public void setSnippetsList(ArrayList<SnippetsList> SnippetsList) {
        this.SnippetsList = SnippetsList;
    }

    public int getNotificationCount() {
        return NotificationCount;
    }

    public void setNotificationCount(int NotificationCount) {
        this.NotificationCount = NotificationCount;
    }

    public boolean getIsSubscribed() {
        return IsSubscribed;
    }

    public void setIsSubscribed(boolean IsSubscribed) {
        this.IsSubscribed = IsSubscribed;
    }

    public ArrayList<DashboardList> getDashboardList() {
        return DashboardList;
    }

    public void setDashboardList(ArrayList<DashboardList> DashboardList) {
        this.DashboardList = DashboardList;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public static class SnippetsList implements Serializable {
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("UserId")
        private String UserId;
        @SerializedName("TotalViews")
        private int TotalViews;
        @SerializedName("SnippetsVideoId")
        private String SnippetsVideoId;
        @SerializedName("SnippetsType")
        private int SnippetsType;
        @SerializedName("SnippetsThumbPath")
        private String SnippetsThumbPath;
        @SerializedName("SnippetsThumbImage")
        private String SnippetsThumbImage;
        @SerializedName("SnippetsProfileUrl")
        private String SnippetsProfileUrl;
        @SerializedName("SnippetsPath")
        private String SnippetsPath;
        @SerializedName("SnippetsName")
        private String SnippetsName;
        @SerializedName("SnippetsId")
        private int SnippetsId;
        @SerializedName("SnippetsCaption")
        private String SnippetsCaption;
        @SerializedName("SnippetsAudioImagePath")
        private String SnippetsAudioImagePath;
        @SerializedName("SnippetsAudioImage")
        private String SnippetsAudioImage;
        @SerializedName("Result")
        private boolean Result;
        @SerializedName("Message")
        private String Message;
        @SerializedName("LastName")
        private String LastName;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("FullName")
        private String FullName;
        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public int getTotalViews() {
            return TotalViews;
        }

        public void setTotalViews(int TotalViews) {
            this.TotalViews = TotalViews;
        }

        public String getSnippetsVideoId() {
            return SnippetsVideoId;
        }

        public void setSnippetsVideoId(String SnippetsVideoId) {
            this.SnippetsVideoId = SnippetsVideoId;
        }

        public int getSnippetsType() {
            return SnippetsType;
        }

        public void setSnippetsType(int SnippetsType) {
            this.SnippetsType = SnippetsType;
        }

        public String getSnippetsThumbPath() {
            return SnippetsThumbPath;
        }

        public void setSnippetsThumbPath(String SnippetsThumbPath) {
            this.SnippetsThumbPath = SnippetsThumbPath;
        }

        public String getSnippetsThumbImage() {
            return SnippetsThumbImage;
        }

        public void setSnippetsThumbImage(String SnippetsThumbImage) {
            this.SnippetsThumbImage = SnippetsThumbImage;
        }

        public String getSnippetsProfileUrl() {
            return SnippetsProfileUrl;
        }

        public void setSnippetsProfileUrl(String SnippetsProfileUrl) {
            this.SnippetsProfileUrl = SnippetsProfileUrl;
        }

        public String getSnippetsPath() {
            return SnippetsPath;
        }

        public void setSnippetsPath(String SnippetsPath) {
            this.SnippetsPath = SnippetsPath;
        }

        public String getSnippetsName() {
            return SnippetsName;
        }

        public void setSnippetsName(String SnippetsName) {
            this.SnippetsName = SnippetsName;
        }

        public int getSnippetsId() {
            return SnippetsId;
        }

        public void setSnippetsId(int SnippetsId) {
            this.SnippetsId = SnippetsId;
        }

        public String getSnippetsCaption() {
            return SnippetsCaption;
        }

        public void setSnippetsCaption(String SnippetsCaption) {
            this.SnippetsCaption = SnippetsCaption;
        }

        public String getSnippetsAudioImagePath() {
            return SnippetsAudioImagePath;
        }

        public void setSnippetsAudioImagePath(String SnippetsAudioImagePath) {
            this.SnippetsAudioImagePath = SnippetsAudioImagePath;
        }

        public String getSnippetsAudioImage() {
            return SnippetsAudioImage;
        }

        public void setSnippetsAudioImage(String SnippetsAudioImage) {
            this.SnippetsAudioImage = SnippetsAudioImage;
        }

        public boolean getResult() {
            return Result;
        }

        public void setResult(boolean Result) {
            this.Result = Result;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public String getInsertDateTime() {
            return InsertDateTime;
        }

        public void setInsertDateTime(String InsertDateTime) {
            this.InsertDateTime = InsertDateTime;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String FullName) {
            this.FullName = FullName;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int CustomerId) {
            this.CustomerId = CustomerId;
        }

        public String getBioVideoUrl() {
            return BioVideoUrl;
        }

        public void setBioVideoUrl(String BioVideoUrl) {
            this.BioVideoUrl = BioVideoUrl;
        }
    }

    public static class DashboardList implements Serializable {
        @SerializedName("VideoThumbnailUrl")
        private String VideoThumbnailUrl;
        @SerializedName("VideoID")
        private int VideoID;
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TotalViews")
        private int TotalViews;
        @SerializedName("TotalPost")
        private String TotalPost;
        @SerializedName("TotalLikes")
        private int TotalLikes;
        @SerializedName("TotalFollowings")
        private String TotalFollowings;
        @SerializedName("TotalFollowers")
        private String TotalFollowers;
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
        //        @SerializedName("PostTagList")
//        private ArrayList<String> PostTagList;
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
        @SerializedName("AdImageList")
        private ArrayList<AdImageList> AdImageList;
        @SerializedName("GroupVideoList")
        private ArrayList<GroupVideoModel> groupVideoList;

        public String getVideoThumbnailUrl() {
            return VideoThumbnailUrl;
        }

        public void setVideoThumbnailUrl(String VideoThumbnailUrl) {
            this.VideoThumbnailUrl = VideoThumbnailUrl;
        }

        public int getVideoID() {
            return VideoID;
        }

        public void setVideoID(int VideoID) {
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

        public String getTotalPost() {
            return TotalPost;
        }

        public void setTotalPost(String TotalPost) {
            this.TotalPost = TotalPost;
        }

        public int getTotalLikes() {
            return TotalLikes;
        }

        public void setTotalLikes(int TotalLikes) {
            this.TotalLikes = TotalLikes;
        }

        public String getTotalFollowings() {
            return TotalFollowings;
        }

        public void setTotalFollowings(String TotalFollowings) {
            this.TotalFollowings = TotalFollowings;
        }

        public String getTotalFollowers() {
            return TotalFollowers;
        }

        public void setTotalFollowers(String TotalFollowers) {
            this.TotalFollowers = TotalFollowers;
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

//        public ArrayList<String> getPostTagList() {
//            return PostTagList;
//        }
//
//        public void setPostTagList(ArrayList<String> PostTagList) {
//            this.PostTagList = PostTagList;
//        }

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

        public ArrayList<AdImageList> getAdImageList() {
            return AdImageList;
        }

        public void setAdImageList(ArrayList<AdImageList> adImageList) {
            this.AdImageList = adImageList;
        }

        public ArrayList<GroupVideoModel> getGroupVideoList() {
            return groupVideoList;
        }

        public void setGroupVideoList(ArrayList<GroupVideoModel> groupVideoList) {
            this.groupVideoList = groupVideoList;
        }
    }

    public static class AdImageList implements Serializable {
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


    public static class CommentList implements Serializable {
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("Name")
        private String Name;
        @SerializedName("IsText")
        private boolean IsText;
        @SerializedName("IsImage")
        private boolean IsImage;
        @SerializedName("IsAd")
        private boolean IsAd;
        @SerializedName("ImageUrl")
        private String ImageUrl;
        @SerializedName("ImageCaption")
        private String ImageCaption;
        @SerializedName(value = "0", alternate = {"Id", "PostId"})
        private int Id;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("CommentImageWidth")
        private int CommentImageWidth;
        @SerializedName("CommentImageHeight")
        private int CommentImageHeight;
        @SerializedName("CommentId")
        private int CommentId;
        @SerializedName("Comment")
        private String Comment;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
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

        public boolean getIsAd() {
            return IsAd;
        }

        public void setIsAd(boolean IsAd) {
            this.IsAd = IsAd;
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

        public int getCommentId() {
            return CommentId;
        }

        public void setCommentId(int CommentId) {
            this.CommentId = CommentId;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }
    }
}