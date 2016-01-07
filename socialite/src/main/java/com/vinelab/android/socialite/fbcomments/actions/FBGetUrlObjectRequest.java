package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.model.FBUrlGraphObject;
import com.vinelab.android.socialite.fbcomments.responses.FBGetUrlObjectResponse;
import com.vinelab.android.socialite.fbcomments.responses.FBGraphResponse;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 1/7/2016.
 *
 * <p>
 *     Class executing a Get Graph request, to fetch the graph object
 *     of an external URL.
 * </p>
 */
public class FBGetUrlObjectRequest extends FBGraphRequest{

    public FBGetUrlObjectRequest(AccessToken accessToken) {
        super(accessToken);
        setMethod(HttpMethod.GET);
    }

    public void setProperties(String url) {
        this.params = new Bundle();
        if(url != null)     this.params.putString("id", url);
    }


    @Override
    public FBGraphResponse processResponse(JSONObject graphObject) {
        FBGraphResponse graphResponse;
        try {
            FBUrlGraphObject openGraphObject = new FBUrlGraphObject(graphObject);
            // create response object
            graphResponse = new FBGetUrlObjectResponse(openGraphObject);
        }
        catch (Exception e) {
            graphResponse = null;
        }
        return graphResponse;
    }

}
