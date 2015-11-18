package com.vinelab.android.socialite.fbcomments.entities;

/**
 * Created by Nabil on 11/16/2015.
 */
public class FBPostCommentResponse extends FBGraphResponse {
    private String commentId;

    public FBPostCommentResponse(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }
}
