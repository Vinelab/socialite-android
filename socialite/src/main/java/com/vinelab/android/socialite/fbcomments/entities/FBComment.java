package com.vinelab.android.socialite.fbcomments.entities;

import org.json.JSONObject;

/**
 * Created by Nabil on 11/15/2015.
 */
public class FBComment {
    String createdTime = null;
    FBCommentAuthor from = null;
    String message = null;
    String id = null;
    int commentCount = 0;
    boolean canComment = false;
    boolean canLike = false;
    int likeCount = 0;
    boolean userLikes = false;
    FBComment parent = null;

    public FBComment(JSONObject json) {
        if(json != null) {
            try {
                if(json.has("created_time"))    this.createdTime = json.getString("created_time");
                if(json.has("from"))    this.from = new FBCommentAuthor(json.getJSONObject("from"));
                if(json.has("message")) this.message = json.getString("message");
                if(json.has("id"))  this.id = json.getString("id");
                if(json.has("comment_count")) this.commentCount = json.getInt("comment_count");
                if(json.has("like_count")) this.likeCount = json.getInt("like_count");
                if(json.has("can_comment")) this.canComment = json.getBoolean("can_comment");
                if(json.has("can_like"))    this.canLike = json.getBoolean("can_like");
                if(json.has("user_likes"))  this.userLikes = json.getBoolean("user_likes");
                if(json.has("parent"))  this.parent = new FBComment(json.getJSONObject("parent"));
            }
            catch (Exception e) {}
        }
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public FBCommentAuthor getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public boolean isCanLike() {
        return canLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isUserLikes() {
        return userLikes;
    }

    public FBComment getParent() {
        return parent;
    }
}
