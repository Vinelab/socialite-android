package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.responses.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.responses.FBPostCommentResponse;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdge;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphUtils;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 11/17/2015.
 * <p>
 *     Class executing a Post Comment Graph request (posting a comment on a post
 *     of a reply on a comment).
 * </p>
 */
public class FBPostCommentRequest extends FBGraphRequest {
    public FBPostCommentRequest(AccessToken accessToken) {
        super(accessToken);
        setEdge(FBGraphEdge.COMMENTS);
        setMethod(HttpMethod.POST);
    }

    public void setProperties(String message) {
        this.params = new Bundle();
        if(message != null) params.putString("message", message);
        params.putString("fields", FBGraphUtils.COMMENT_FIELDS);
    }

    @Override
    public FBGraphResponse processResponse(JSONObject graphObject) {
        FBGraphResponse graphResponse;
        try {
            String commentId = graphObject.getString("id");
            graphResponse = new FBPostCommentResponse(commentId);
        }
        catch (Exception e) {
            graphResponse = null;
        }
        return graphResponse;
    }
}
