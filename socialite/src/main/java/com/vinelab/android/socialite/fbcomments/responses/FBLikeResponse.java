package com.vinelab.android.socialite.fbcomments.responses;

/**
 * Created by Nabil Souk on 11/17/2015.
 * <p>
 *     Class holding the data returned after a successful
 *     Like request, whether it's a Post or a Delete one.
 * </p>
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
