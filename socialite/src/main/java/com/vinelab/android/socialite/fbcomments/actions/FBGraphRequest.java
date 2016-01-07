package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.responses.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.listeners.OnGraphRequestListener;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdge;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphError;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 11/17/2015.
 *
 * <p>
 *     Class wrapping a Facebook GraphRequest, with execution and callbacks.
 * </p>
 */
public abstract class FBGraphRequest {
    AccessToken accessToken = null;
    String target = ""; // default empty
    FBGraphEdge edge = null;
    Bundle params = null;
    HttpMethod method = HttpMethod.GET; // default
    OnGraphRequestListener graphRequestListener = null;

    public FBGraphRequest(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sets the callback listener.
     */
    public void setGraphRequestListener(@Nullable OnGraphRequestListener listener) {
        this.graphRequestListener = listener;
    }

    /**
     * Sets the AccessToken of the current active Facebook session.
     */
    public void setAccessToken(@NonNull AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sets the Facebook target of this request, the post or comment ID for example.
     */
    public void setTarget(@NonNull String target) {
        this.target = target;
    }

    /**
     * Sets the current GraphRequest edge, if any.
     */
    protected void setEdge(@Nullable FBGraphEdge edge) {
        this.edge = edge;
    }

    /**
     * Sets the current GraphRequest HTTP method. Get is the default.
     */
    protected void setMethod(@NonNull HttpMethod method) {
        this.method = method;
    }

    /**
     * Sets the GraphRequest parameters set, if any.
     */
    protected void setParameters(@Nullable Bundle params) {
        this.params = params;
    }

    /**
     * Returns the current GraphRequest path. It's a combination of the
     * target and the edge if set.
     */
    private String getGraphPath() {
        return target + (edge != null? "/" + edge.getString(): "/");
    }

    /**
     * The callback used by the GraphRequest when done.
     */
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
                    broadcastFail(FBGraphError.ERROR.FAILED);
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

    /**
     * Returns an understandable Graph error from the given error code.
     */
    private FBGraphError.ERROR processError(int errorCode) {
        FBGraphError.ERROR error = FBGraphError.ERROR.FAILED; // default
        if(errorCode == 104) { // check if access token needed
            error = FBGraphError.ERROR.LOGIN_NEEDED;
        }
        else if(errorCode == 200) { // check if permissions missing
            error = FBGraphError.ERROR.PERMISSIONS_NEEDED;
        }
        return error;
    }

    /**
     * Processes the JSON object returned after a successful request, and returns
     * a specific FBGraphResponse depending on the request type.
     */
    protected abstract FBGraphResponse processResponse(JSONObject graphObject);

    /**
     * Triggers the execution of the current request.
     */
    public void execute() {
        new GraphRequest(accessToken, getGraphPath(), params, method, callback).executeAsync();
    }

    /**
     * Broadcasts the result of a successful request.
     */
    private void broadcastComplete(FBGraphResponse response) {
        if(graphRequestListener != null)    graphRequestListener.onComplete(response);
    }

    /**
     * Broadcasts the error of an unsuccessful request.
     */
    private void broadcastFail(FBGraphError.ERROR error) {
        if(graphRequestListener != null)    graphRequestListener.onError(error);
    }
}
