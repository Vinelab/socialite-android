package com.vinelab.android.socialite.fbcomments.listeners;

import com.vinelab.android.socialite.fbcomments.utils.FBGraphErrors;

/**
 * Created by Nabil on 11/16/2015.
 */
public abstract class OnGraphRequestListener<FBGraphResponse> {
    public void onComplete(FBGraphResponse response) {}

    public void onError(FBGraphErrors.ERROR error) {}
}
