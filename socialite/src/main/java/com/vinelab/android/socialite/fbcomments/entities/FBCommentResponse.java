package com.vinelab.android.socialite.fbcomments.entities;

/**
 * Created by Nabil on 11/16/2015.
 */
public class FBCommentResponse extends FBGraphResponse {
    private String commentId;

    public FBCommentResponse(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }
}
