package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBComment;
import com.vinelab.android.socialite.fbcomments.entities.FBGetCommentResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 11/17/2015.
 *
 * <p>
 *     Class executing a Get Comment Graph request (fetching the details
 *     of a comment).
 * </p>
 */
public class FBGetCommentRequest extends FBGraphRequest {

    public FBGetCommentRequest(AccessToken accessToken) {
        super(accessToken);
        setMethod(HttpMethod.GET);
    }

    public void setProperties(String fields) {
        this.params = new Bundle();
        if(fields != null)  this.params.putString("fields", fields);
    }

    @Override
    public FBGraphResponse processResponse(JSONObject graphObject) {
        FBGraphResponse graphResponse;
        try {
            FBComment comment = new FBComment(graphObject);
            // create response object
            graphResponse = new FBGetCommentResponse(comment);
        }
        catch (Exception e) {
            graphResponse = null;
        }
        return graphResponse;
    }
}
