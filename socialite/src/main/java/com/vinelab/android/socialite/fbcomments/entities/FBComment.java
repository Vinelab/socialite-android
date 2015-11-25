package com.vinelab.android.socialite.fbcomments.entities;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nabil Souk on 11/15/2015.
 *
 * <p>
 *     Class holding the details of a Facebook comment.
 * </p>
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

    /**
     * Updates if like flag of the current user on the comment,
     * and updates the likes count according to the user's action.
     * @param liked true if the user liked the comment, false if he
     *              deleted it.
     */
    public void updateUserLikeState(boolean liked) {
        if(liked) {
            // check if not liked previously
            if(!userLikes) {
                // set it and increment count
                userLikes = true;
                ++likeCount;
            }
        }
        else {
            // check if liked previously
            if(userLikes) {
                // set it and decrement count
                userLikes = false;
                --likeCount;
            }
        }
    }

    /**
     * Generates a timestamp value from the input String following the format
     * `yyyy-MM-dd'T'HH:mm:ssZ`. If the date String is not valid, timestamp will be 0.
     */
    public long generateTimestamp() {
        long timestamp = 0;
        // check if str not null
        if(createdTime != null && !createdTime.isEmpty()) {
            try {
                // get Date from String
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
                Date date = sdf.parse(createdTime);
                // get timestamp from date
                timestamp = date.getTime();
            }
            catch (Exception e) {}
        }
        return timestamp;
    }
}
