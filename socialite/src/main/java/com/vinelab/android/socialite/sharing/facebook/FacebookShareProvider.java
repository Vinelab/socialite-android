package com.vinelab.android.socialite.sharing.facebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.vinelab.android.socialite.FacebookConfig;
import com.vinelab.android.socialite.R;
import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.sharing.listeners.SocialiteShareListener;

/**
 * Created by Nabil Souk on 11/2/2015.
 *
 * <p>
 *     Class providing Facebook share operations.
 * </p>
 */
public class FacebookShareProvider {
    private static FacebookShareProvider shareProvider;
    private CallbackManager callbackManager;
    private SocialiteShareListener shareListener;

    private FacebookShareProvider() {
        initialize();
    }

    public static FacebookShareProvider getInstance() {
        if(shareProvider == null)   shareProvider = new FacebookShareProvider();
        return shareProvider;
    }

    /**
     * Initializes the tools needed.
     */
    private void initialize() {
        // Create the callback manager to handle the share responses
        callbackManager = CallbackManager.Factory.create();
    }

    /**
     * Requests to share a content on the user's feed. It will trigger the Share Dialog to be opened.
     * If the Facebook app is not installed, a webpage will be open to login before sharing
     * @param activity The activity requesting the operation.
     * @param contentUrl The content's url.
     * @param contentTitle The content's title.
     * @param imageUrl The url of the thumbnail image of the content.
     * @param contentDescription The content's description.
     * @param listener The callback listener.
     */
    public void postOnMeFeed(Activity activity, @NonNull String contentUrl, String contentTitle, String imageUrl, String contentDescription, SocialiteShareListener listener) {
        // set listener
        shareListener = listener;
        // check if the SDK can display the share dialog
        if(ShareDialog.canShow(ShareLinkContent.class)) {
            // create content and show dialog
            ShareLinkContent linkContent = createLinkContent(contentUrl, contentTitle, imageUrl, contentDescription);
            ShareDialog shareDialog = new ShareDialog(activity);
            // set the request code to trigger the callback
            shareDialog.registerCallback(callbackManager, shareCallback, FacebookConfig.SHARE_REQUEST_CODE);
            shareDialog.show(linkContent);
        }
        else {
            // broadcast
            broadcastShareError(activity.getString(R.string.fb_share_present_error));
        }
    }

    /**
     * Requests to sent a link via Messenger. It will trigger the Message Dialog to be opened.
     * If the Facebook app is not installed, a webpage will be open to login before sharing
     * @param activity The activity requesting the operation.
     * @param contentUrl The content's url.
     * @param contentTitle The content's title.
     * @param imageUrl The url of the thumbnail image of the content.
     * @param contentDescription The content's description.
     * @param listener The callback listener.
     */
    public void sendMessage(Activity activity, String contentUrl, String contentTitle, String imageUrl, String contentDescription, SocialiteShareListener listener) {
        // set listener
        shareListener = listener;
        // check if the SDK can display the share dialog
        if(MessageDialog.canShow(ShareLinkContent.class)) {
            // create content and show dialog
            ShareLinkContent linkContent = createLinkContent(contentUrl, contentTitle, imageUrl, contentDescription);
            MessageDialog messageDialog = new MessageDialog(activity);
            // set the request code to trigger the callback
            messageDialog.registerCallback(callbackManager, shareCallback, FacebookConfig.SHARE_REQUEST_CODE);
            messageDialog.show(linkContent);
        }
        else {
            // broadcast
            broadcastShareError(activity.getString(R.string.fb_share_present_error));
        }
    }

    /**
     * Create a ShareLinkContent object containing the listed data.
     * @param contentUrl The content's url.
     * @param contentTitle The content's title.
     * @param imageUrl The url of the thumbnail image of the content.
     * @param contentDescription The content's description.
     */
    private ShareLinkContent createLinkContent(@NonNull String contentUrl, String contentTitle, String imageUrl, String contentDescription) {
        ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
        builder.setContentUrl(Uri.parse(contentUrl));
        if(contentTitle != null)    builder.setContentTitle(contentTitle);
        if(imageUrl != null)    builder.setImageUrl(Uri.parse(imageUrl));
        if(contentDescription != null)  builder.setContentDescription(contentDescription);
        return builder.build();
    }

    /**
     * Intercepts the share callbacks from the SDK.
     */
    FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            if(result != null) {
                String postId = result.getPostId();
                broadcastShareSuccess(postId);
            }
            else    broadcastShareError(null);
        }

        @Override
        public void onCancel() {
            broadcastShareCancel();
        }

        @Override
        public void onError(FacebookException error) {
            broadcastShareError(error != null? error.getMessage(): null);
        }
    };

    /**
     * Called to forward the share results for the CallbackManager.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // provider callbacks //

    /**
     * Broadcasts a share success flag.
     * @param postId The ID of the post shared.
     */
    private void broadcastShareSuccess(String postId) {
        if(shareListener != null) {
            // create extras
            FacebookShareExtras extras = new FacebookShareExtras(postId);
            // broadcast
            shareListener.onSuccess(FacebookConfig.provider, extras);
        }
    }

    /**
     * Broadcasts a share error flag.
     * @param error The error message returned by the SDK.
     */
    private void broadcastShareError(@Nullable String error) {
        if(shareListener != null)   shareListener.onError(FacebookConfig.provider, error);
    }

    /**
     * Broadcasts a share cancelled flag.
     */
    private void broadcastShareCancel() {
        if(shareListener != null)   shareListener.onCancel(FacebookConfig.provider);
    }
}
