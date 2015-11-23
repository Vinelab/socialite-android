package com.vinelab.android.socialite.fbcomments.listeners;

import com.vinelab.android.socialite.fbcomments.utils.FBGraphError;

/**
 * Created by Nabil Souk on 11/16/2015.
 *
 * <p>
 *     Interface definition for callbacks to be invoked when a FBGraphRequest is done.
 * </p>
 */
public abstract class OnGraphRequestListener<FBGraphResponse> {
    /**
     * Triggered when the request was successful.
     * @param response The object containing the returned data.
     */
    public void onComplete(FBGraphResponse response) {}

    /**
     * Triggered when the request failes.
     * @param error The error that caused the failure.
     */
    public void onError(FBGraphError.ERROR error) {}
}
