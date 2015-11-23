package com.vinelab.android.socialite.fbcomments.actions;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;

/**
 * Created by Nabil Souk on 11/17/2015.
 *
 * <p>
 *     Class executing a Delete Like Graph request.
 * </p>
 */
public class FBDeleteLikeRequest extends FBPostLikeRequest {
    public FBDeleteLikeRequest(AccessToken accessToken) {
        super(accessToken);
        setMethod(HttpMethod.DELETE);
    }
}
