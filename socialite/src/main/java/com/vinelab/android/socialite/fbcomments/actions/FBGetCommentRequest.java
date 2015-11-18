package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBComment;
import com.vinelab.android.socialite.fbcomments.entities.FBGetCommentResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBGetCommentsResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdges;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nabil on 11/17/2015.
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
