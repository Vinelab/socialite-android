package com.vinelab.android.socialite.fbcomments.actions;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBLikeResponse;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdges;

import org.json.JSONObject;

/**
 * Created by Nabil on 11/17/2015.
 */
public class FBDeleteLikeListener extends FBGraphRequest {
    public FBDeleteLikeListener(AccessToken accessToken) {
        super(accessToken);
        setEdge(FBGraphEdges.LIKES);
        setMethod(HttpMethod.DELETE);
    }

    @Override
    public void setTarget(String objectId) {
        this.target = objectId;
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
