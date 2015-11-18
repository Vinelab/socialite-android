package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.listeners.OnGraphRequestListener;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphErrors;

import org.json.JSONObject;

/**
 * Created by Nabil on 11/17/2015.
 */
public abstract class FBGraphRequest {
    AccessToken accessToken = null;
    String target = "me"; // default
    String edge = null;
    Bundle params = null;
    HttpMethod method = HttpMethod.GET; // default
    OnGraphRequestListener graphRequestListener = null;

    public FBGraphRequest(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setGraphRequestListener(OnGraphRequestListener listener) {
        this.graphRequestListener = listener;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    protected void setEdge(String edge) {
        this.edge = edge;
    }

    protected void setMethod(HttpMethod method) {
        this.method = method;
    }

    protected void setParameters(Bundle params) {
        this.params = params;
    }

//    protected abstract void set

    public String getGraphPath() {
        return target + (edge != null? "/" + edge: "");
    }

    private GraphRequest.Callback callback = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            // get response error
            FacebookRequestError requestError = response.getError();
            // check if error
            if(requestError != null) {
                // get error code
                int errorCode = requestError.getErrorCode();
                broadcastFail(processError(errorCode));
            }
            else {
                // check if raw message empty
                if(response.getRawResponse() == null || response.getRawResponse().isEmpty()) {
                    broadcastFail(FBGraphErrors.ERROR.FAILED);
                }
                else {
                    // get response object
                    FBGraphResponse graphResponse = processResponse(response.getJSONObject());
                    // broadcast
                    broadcastComplete(graphResponse);
                }
            }
        }
    };

    private FBGraphErrors.ERROR processError(int errorCode) {
        FBGraphErrors.ERROR error = FBGraphErrors.ERROR.FAILED; // default
        if(errorCode == 104) { // check if access token needed
            error = FBGraphErrors.ERROR.LOGIN_NEEDED;
        }
        else if(errorCode == 200) { // check if permissions missing
            error = FBGraphErrors.ERROR.PERMISSIONS_NEEDED;
        }
        return error;
    }

    protected abstract FBGraphResponse processResponse(JSONObject graphObject);

    public void execute() {
        new GraphRequest(accessToken, getGraphPath(), params, method, callback).executeAsync();
    }

    private void broadcastComplete(FBGraphResponse response) {
        if(graphRequestListener != null)    graphRequestListener.onComplete(response);
    }

    private void broadcastFail(FBGraphErrors.ERROR error) {
        if(graphRequestListener != null)    graphRequestListener.onError(error);
    }
}
