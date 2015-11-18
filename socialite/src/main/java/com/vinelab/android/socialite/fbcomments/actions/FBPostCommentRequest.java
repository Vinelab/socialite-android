package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBCommentResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdges;

import org.json.JSONObject;

/**
 * Created by Nabil on 11/17/2015.
 */
public class FBPostCommentRequest extends FBGraphRequest {
    public FBPostCommentRequest(AccessToken accessToken) {
        super(accessToken);
        setEdge(FBGraphEdges.COMMENTS);
        setMethod(HttpMethod.POST);
    }

    public void setProperties(String message) {
        this.params = new Bundle();
        if(message != null) params.putString("message", message);
    }

    @Override
    public FBGraphResponse processResponse(JSONObject graphObject) {
        FBGraphResponse graphResponse;
        try {
            String commentId = graphObject.getString("id");
            graphResponse = new FBCommentResponse(commentId);
        }
        catch (Exception e) {
            graphResponse = null;
        }
        return graphResponse;
    }
}
