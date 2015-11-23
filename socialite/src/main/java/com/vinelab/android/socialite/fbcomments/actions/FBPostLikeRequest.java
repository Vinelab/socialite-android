package com.vinelab.android.socialite.fbcomments.actions;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBLikeResponse;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdge;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 11/17/2015.
 * <p>
 *     Class executing a Post Like Graph request.
 * </p>
 */
public class FBPostLikeRequest extends FBGraphRequest {
    public FBPostLikeRequest(AccessToken accessToken) {
        super(accessToken);
        setEdge(FBGraphEdge.LIKES);
        setMethod(HttpMethod.POST);
    }

    @Override
    protected FBGraphResponse processResponse(JSONObject graphObject) {
        FBGraphResponse graphResponse;
        try {
            boolean success = graphObject.getBoolean("success");
            graphResponse = new FBLikeResponse(success);
        }
        catch (Exception e) {
            graphResponse = null;
        }
        return graphResponse;
    }
}
