package com.vinelab.android.socialite.fbcomments.entities;

/**
 * Created by Nabil Souk on 11/16/2015.
 * <p>
 *     Class holding the data returned after successfully posting
 *     a Facebook comment.
 * </p>
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
