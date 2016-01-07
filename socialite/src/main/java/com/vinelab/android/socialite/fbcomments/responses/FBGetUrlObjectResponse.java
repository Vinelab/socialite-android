package com.vinelab.android.socialite.fbcomments.responses;

import com.vinelab.android.socialite.fbcomments.model.FBUrlGraphObject;

/**
 * Created by Nabil Souk on 1/7/2016.
 *
 * <p>
 *     Class holding the data returned after successfully
 *     fetching a Graph object by id.
 * </p>
 */
public class FBGetUrlObjectResponse extends FBGraphResponse {
    private final FBUrlGraphObject openGraphObject;

    public FBGetUrlObjectResponse(FBUrlGraphObject openGraphObject) {
        this.openGraphObject = openGraphObject;
    }

    public FBUrlGraphObject getOpenGraphObject() {
        return openGraphObject;
    }
}
