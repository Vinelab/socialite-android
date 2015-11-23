package com.vinelab.android.socialite.fbcomments.entities;

/**
 * Created by Nabil Souk on 11/16/2015.
 *
 * <p>
 *     Class holding the data returned after successfully
 *     fetching a Facebook comment.
 * </p>
 */
public class FBGetCommentResponse extends FBGraphResponse{
    FBComment comment;

    public FBGetCommentResponse(FBComment comment) {
        this.comment = comment;
    }

    public FBComment getComment() {
        return comment;
    }
}
