package com.vinelab.android.socialiteexample.comments;

/**
 * Created by Nabil on 11/19/2015.
 */
public abstract class OnFacebookCommentsListener {
    public abstract void onLoginNeeded() ;
    public abstract void onLoginSuccess() ;
    public abstract void onLoginCancelled() ;
    public abstract void onLoginFailed() ;
    public abstract void onPublishPermissionNeeded() ;
    public abstract void onPostCommentResponse(boolean success);
}
