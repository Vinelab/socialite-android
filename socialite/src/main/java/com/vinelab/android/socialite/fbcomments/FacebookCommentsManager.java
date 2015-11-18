package com.vinelab.android.socialite.fbcomments;

import com.facebook.AccessToken;
import com.vinelab.android.socialite.fbcomments.actions.FBDeleteLikeListener;
import com.vinelab.android.socialite.fbcomments.actions.FBGetCommentsRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBPostCommentRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBPostLikeRequest;
import com.vinelab.android.socialite.fbcomments.listeners.OnGetCommentsListener;
import com.vinelab.android.socialite.fbcomments.listeners.OnLikeRequestListener;
import com.vinelab.android.socialite.fbcomments.listeners.OnPostCommentListener;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphUtils;

/**
 * Created by Nabil on 11/15/2015.
 */
public class FacebookCommentsManager {
    // comments requests

    public void requestComments(String objectId, int limit, final String after, OnGetCommentsListener listener) {
        FBGetCommentsRequest request = new FBGetCommentsRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setProperties(FBGraphUtils.COMMENT_FIELDS, FBGraphUtils.COMMENTS_ORDER.CHRONOLOGICAL, limit, after);
        request.setGraphRequestListener(listener);
        request.execute();
    }

    public void postComment(String postId, String comment, OnPostCommentListener listener) {
        FBPostCommentRequest request = new FBPostCommentRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(postId);
        request.setProperties(comment);
        request.setGraphRequestListener(listener);
        request.execute();
    }

    public void postLike(String objectId, OnLikeRequestListener listener) {
        FBPostLikeRequest request = new FBPostLikeRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setGraphRequestListener(listener);
        request.execute();
    }

    public void deleteLike(String objectId, OnLikeRequestListener listener) {
        FBDeleteLikeListener request = new FBDeleteLikeListener(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setGraphRequestListener(listener);
        request.execute();
    }
}
