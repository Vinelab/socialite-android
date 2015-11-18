package com.vinelab.android.socialite.fbcomments.entities;

/**
 * Created by Nabil on 11/17/2015.
 */
public class FBLikeResponse extends FBGraphResponse {
    boolean success = false; // default

    public FBLikeResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
