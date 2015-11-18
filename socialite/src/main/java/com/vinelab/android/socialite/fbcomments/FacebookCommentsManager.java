package com.vinelab.android.socialite.fbcomments;

import com.facebook.AccessToken;
import com.vinelab.android.socialite.fbcomments.actions.FBDeleteLikeRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBGetCommentRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBGetCommentsRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBPostCommentRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBPostLikeRequest;
import com.vinelab.android.socialite.fbcomments.listeners.OnGetCommentListener;
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
        FBGetCommentsRequest request = generateGetCommentsRequest(objectId, limit, after, listener);
        request.execute();
    }

    public void postComment(String postId, String comment, OnPostCommentListener listener) {
        FBPostCommentRequest request = generatePostCommentRequest(postId, comment, listener);
        request.execute();
    }

    public void getComment(String commentId, OnGetCommentListener listener) {
        FBGetCommentRequest request = generateGetCommentRequest(commentId, listener);
        request.execute();
    }

    public void postLike(String objectId, OnLikeRequestListener listener) {
        FBPostLikeRequest request = generatePostLikeRequest(objectId, listener);
        request.execute();
    }

    public void deleteLike(String objectId, OnLikeRequestListener listener) {
        FBDeleteLikeRequest request = generateDeleteLikeRequest(objectId, listener);
        request.execute();
    }

    public FBGetCommentsRequest generateGetCommentsRequest(String objectId, int limit, final String after, OnGetCommentsListener listener) {
        FBGetCommentsRequest request = new FBGetCommentsRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setProperties(FBGraphUtils.COMMENT_FIELDS, FBGraphUtils.COMMENTS_ORDER.CHRONOLOGICAL, limit, after);
        request.setGraphRequestListener(listener);
        return request;
    }

    public FBGetCommentRequest generateGetCommentRequest(String commentId, OnGetCommentListener listener) {
        FBGetCommentRequest request = new FBGetCommentRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(commentId);
        request.setProperties(FBGraphUtils.COMMENT_FIELDS);
        request.setGraphRequestListener(listener);
        return request;
    }

    public FBPostCommentRequest generatePostCommentRequest(String postId, String comment, OnPostCommentListener listener) {
        FBPostCommentRequest request = new FBPostCommentRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(postId);
        request.setProperties(comment);
        request.setGraphRequestListener(listener);
        return request;
    }

    public FBPostLikeRequest generatePostLikeRequest(String objectId, OnLikeRequestListener listener) {
        FBPostLikeRequest request = new FBPostLikeRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setGraphRequestListener(listener);
        return request;
    }

    public FBDeleteLikeRequest generateDeleteLikeRequest(String objectId, OnLikeRequestListener listener) {
        FBDeleteLikeRequest request = new FBDeleteLikeRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setGraphRequestListener(listener);
        return request;
    }
}
