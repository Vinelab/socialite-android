package com.vinelab.android.socialite.fbcomments.entities;

import java.util.ArrayList;

/**
 * Created by Nabil on 11/16/2015.
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
