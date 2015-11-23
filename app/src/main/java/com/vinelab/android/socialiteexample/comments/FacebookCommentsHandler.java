package com.vinelab.android.socialiteexample.comments;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.vinelab.android.socialite.SocialiteCredentials;
import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.fbcomments.FBGraphRequestGenerator;
import com.vinelab.android.socialite.fbcomments.actions.FBDeleteLikeRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBGetCommentRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBGetCommentsRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBGraphRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBPostCommentRequest;
import com.vinelab.android.socialite.fbcomments.actions.FBPostLikeRequest;
import com.vinelab.android.socialite.fbcomments.entities.FBComment;
import com.vinelab.android.socialite.fbcomments.entities.FBGetCommentResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBLikeResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBPostCommentResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBGetCommentsResponse;
import com.vinelab.android.socialite.fbcomments.listeners.OnGetCommentListener;
import com.vinelab.android.socialite.fbcomments.listeners.OnGetCommentsListener;
import com.vinelab.android.socialite.fbcomments.listeners.OnLikeRequestListener;
import com.vinelab.android.socialite.fbcomments.listeners.OnPostCommentListener;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphError;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphUtils;
import com.vinelab.android.socialite.login.facebook.FacebookLoginProvider;
import com.vinelab.android.socialite.login.listeners.SocialiteLoginListener;
import com.vinelab.android.socialiteexample.R;
import com.vinelab.android.socialiteexample.comments.ui.CommentsAdapter;
import com.vinelab.android.socialiteexample.images.ImageHandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Nabil on 11/17/2015.
 */
public class FacebookCommentsHandler {
    Activity activity;
    ListView lvComments;
    String objectId;
    View listFooter;
    ProgressBar pbFooterLoading;
    Button btnFooterLoading;
    // tools
    CommentsAdapter commentsAdapter;
    FBGraphRequestGenerator commentsManager;
    FBRequestQueue requestQueue;
    OnFacebookCommentsListener fbCommentsListener;
    // comments
    int limit = 10;
    ArrayList<FBComment> arrayComments = null;
    String after = null;
    boolean fetchingComments = false;

    public FacebookCommentsHandler(Activity activity) {
        this.activity = activity;
        this.commentsManager = new FBGraphRequestGenerator();
        this.requestQueue = new FBRequestQueue();
    }

    public void setConfiguration(String objectId, int limit) {
        this.objectId = objectId;
        this.limit = limit;
    }

    public void setUIElements(ListView lvComments) {
        this.lvComments = lvComments;
        setupList();
    }

    public void setListener(OnFacebookCommentsListener fbCommentsListener) {
        this.fbCommentsListener = fbCommentsListener;
    }

    private void setupList() {
        // check if footer is not inflated yet
        if(listFooter == null) {
            listFooter = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.layout_comments_list_footer, null);
            pbFooterLoading = (ProgressBar) listFooter.findViewById(R.id.pbLoading);
            btnFooterLoading = (Button) listFooter.findViewById(R.id.btnMore);
            // setup button click
            btnFooterLoading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleFooterButtonClick();
                }
            });
        }
        // check if footer not added previously then add it
        if(lvComments.getFooterViewsCount() == 0) {
            // set view params inside the list
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            listFooter.setLayoutParams(params);
            // add it as list footer
            lvComments.addFooterView(listFooter);
        }
        // hide footer
        updateFooter(false, false);
        // setup adapter
        if(commentsAdapter == null) {
            commentsAdapter = new CommentsAdapter(activity.getApplicationContext());
            commentsAdapter.setRowListener(new CommentsAdapter.OnCommentRowListener() {
                @Override
                public void onLike(FBComment comment) {
                    postLike(comment);
                }

                @Override
                public void onUnlike(FBComment comment) {
                    deleteLike(comment);
                }

                @Override
                public void onLoadImage(ImageView iv, String url, int placeholderResId) {
                    broadcastLoadImage(iv, url, placeholderResId);
                }
            });
        }
        lvComments.setAdapter(commentsAdapter);
    }

    /**
     * Resets the components used.
     */
    private void reset() {
        if(commentsAdapter != null) commentsAdapter.clearData();
        if(arrayComments != null)   arrayComments.clear();
        arrayComments = null;
        after = null;
        clearQueue();
    }

    /**
     *
     */
    private void handleFooterButtonClick() {
        // fetch comments & show loading
        requestNextCommentsSet(true);
    }

    /**
     * Updates the visibility of the footer elements.
     * @param showLoader
     * @param showLoadMore
     */
    private void updateFooter(boolean showLoader, boolean showLoadMore) {
        pbFooterLoading.setVisibility(showLoader ? View.VISIBLE : View.GONE);
        btnFooterLoading.setVisibility(showLoadMore ? View.VISIBLE : View.GONE);
    }

    /**
     * Triggers the loading of the first set of comments. If the user did not log in
     * previously to Facebook, a callback will be triggered.
     */
    public void start(boolean requestLoginIfNeeded) {
        reset();
        // check if user is logged
        if(isUserLoggedIn()) {
            // clear requests queues
            clearQueue();
            // fetch comments & show loading
            requestNextCommentsSet(true);
        }
        else {
            if(requestLoginIfNeeded) {
                // queue fetching comments request
                queueNextCommentsSet();
                // request login
                requestLogin(loginListener);
            }
            else {
                // broadcast that a login is needed
                broadcastLoginNeeded();
                // hide footer
                updateFooter(false, false);
            }
        }
    }

    /**
     * Triggers Facebook to request login from the current user.
     */
    public void requestLogin() {
        requestLogin(loginListener);
    }

    /**
     * Callbacks after a requested login.
     */
    SocialiteLoginListener loginListener = new SocialiteLoginListener() {
        @Override
        public void onSuccess(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
            // trigger any queued requests
            executeFirstQueuedRequest();
            // broadcast result
            broadcastLoginSuccess();
        }

        @Override
        public void onCancel(SocialiteUtils.SOCIALITE_PROVIDER provider) {
            // clear any queued requests
            clearQueue();
            // check if not loading comments than hide footer
            if(!fetchingComments)   updateFooter(false, false);
            // broadcast result
            broadcastLoginCancelled();
        }

        @Override
        public void onError(SocialiteUtils.SOCIALITE_PROVIDER provider, @Nullable String error) {
            // clear any queued requests
            clearQueue();
            // check if not loading comments than hide footer
            if(!fetchingComments)   updateFooter(false, false);
            // broadcast result
            broadcastLoginFailed();
        }
    };

    /**
     * Executes a new fetch commments request.
     * @param showLoadingUI Wheteher or not to show the loading UI.
     */
    private void requestNextCommentsSet(boolean showLoadingUI) {
        // show loading in list footer
        if(showLoadingUI)   updateFooter(true, false);
        // set flag
        fetchingComments = true;
        // execute request
        generateGetCommentsRequest().execute();
    }

    /**
     * Queues a request to fetch the next set of comments.
     */
    private void queueNextCommentsSet() {
        queueRequest(generateGetCommentsRequest());
    }

    /**
     * Callback for fetching comments request.
     */
    OnGetCommentsListener getCommentsListener = new OnGetCommentsListener() {
        @Override
        public void onComplete(FBGetCommentsResponse fbCommentsResponse) {
            // unflag
            fetchingComments = false;
            // new set of comments received
            processNewSet(fbCommentsResponse);
        }

        @Override
        public void onError(FBGraphError.ERROR error) {
            // unflag
            fetchingComments = false;
            // show load more in footer
            updateFooter(false, true);
        }
    };

    private void processNewSet(FBGetCommentsResponse fbCommentsResponse) {
        if(fbCommentsResponse != null) {
            // get list
            ArrayList<FBComment> arrayNew = fbCommentsResponse.getComments();
            if(arrayComments == null)   arrayComments = new ArrayList<>();
            arrayComments.addAll(arrayNew);
            // check if next
            boolean hasNext = fbCommentsResponse.hasNext();
            after = hasNext? fbCommentsResponse.getAfter(): null;
            // update adapter & ui
            commentsAdapter.updateListWithNewData(arrayNew);
            updateFooter(false, hasNext);
        }
    }

    public void postNewComment(String message, final boolean requestPermissionIfNeeded) {
        // generate graph request
        final FBPostCommentRequest request = commentsManager.generatePostCommentRequest(objectId, message, new OnPostCommentListener() {
            @Override
            public void onComplete(FBPostCommentResponse fbCommentResponse) {
                // handle the new comment added
                handlePostCommentSuccess(fbCommentResponse.getCommentId());
                // broadcast success
                broadcastPostCommentResponse(true);
            }

            @Override
            public void onError(FBGraphError.ERROR error) {
                // broadcast failed
                broadcastPostCommentResponse(false);
            }
        });
        // check if user is logged
        if(!isUserLoggedIn()) {
            queueRequest(request);
            if(requestPermissionIfNeeded) {
                // request needed permission
                requestPublishPermission(loginListener);
            }
            else {
                // broadcast needed permission
                broadcastPublishPermissionNeeded();
            }
        }
        else {
            // check if permissions
            if(isPublishPermissionEnabled()) {
                request.execute();
            }
            else {
                // queue reques
                queueRequest(request);
                if(requestPermissionIfNeeded) {
                    // request needed permission
                    requestPublishPermission(loginListener);
                }
                else {
                    // broadcast needed permission
                    broadcastPublishPermissionNeeded();
                }
            }
        }
    }

    /**
     * Triggers Facebook to request the publish permissions from
     * the current app user.
     */
    public void requestPublishPermissions() {
        // request needed permission
        requestPublishPermission(loginListener);
    }

    /**
     * Requests the full comment object.
     * When the object is returned, it will request to handle it.
     * @param commentId The id of the requested comment
     */
    private void handlePostCommentSuccess(String commentId) {
        if(commentId == null)   return;
        // fetch comment object
        generateGetCommentRequest(commentId, new OnGetCommentListener() {
            @Override
            public void onComplete(FBGetCommentResponse fbGetCommentResponse) {
                // get comment
                FBComment comment = fbGetCommentResponse.getComment();
                // handle it
                handlePostedComment(comment);
            }
        }).execute();
    }

    /**
     * Updates the UI with the new posted comment.
     * @param comment
     */
    private void handlePostedComment(FBComment comment) {
        if(arrayComments == null) {
            arrayComments = new ArrayList<>();
        }
        arrayComments.add(0, comment);
        commentsAdapter.insertComment(comment, true);
    }

    /**
     * Requests to post a like on the given comment. If the use haven't
     * proided the needed permissions, a notification will be sent instead.
     */
    private void postLike(final FBComment comment) {
        // check if valid
        if(comment != null && comment.getId() != null) {
            // check if has permissions
            if(isPublishPermissionEnabled()) {
                // get comment Id
                String commentId = comment.getId();
                // graph request
                FBPostLikeRequest request = generatePostLikeRequest(commentId, new OnLikeRequestListener(){
                    @Override
                    public void onComplete(FBLikeResponse fbLikeResponse) {
                        boolean success = fbLikeResponse != null ? fbLikeResponse.isSuccess() : false;
                        handleLikeRequestSuccess(comment, true, success);
                    }
                });
                request.execute();
            }
            else {
                // broadcast that permissions needed
                broadcastPublishPermissionNeeded();
            }
        }
    }

    /**
     * Requests to delete a like on the given comment. If the use haven't
     * proided the needed permissions, a notification will be sent instead.
     */
    private void deleteLike(final FBComment comment) {
        // check if valid
        if(comment != null && comment.getId() != null) {
            // check if has permissions
            if(isPublishPermissionEnabled()) {
                // get comment Id
                String commentId = comment.getId();
                // graph request
                FBDeleteLikeRequest request = generateDeleteLikeRequest(commentId, new OnLikeRequestListener() {
                    @Override
                    public void onComplete(FBLikeResponse fbLikeResponse) {
                        boolean success = fbLikeResponse != null ? fbLikeResponse.isSuccess() : false;
                        handleLikeRequestSuccess(comment, false, success);
                    }
                });
                request.execute();
            }
            else {
                // broadcast that permissions needed
                broadcastPublishPermissionNeeded();
            }
        }
    }

    /**
     * Updates the comment and its display if the like operation was successful.
     * @param comment The comment affected.
     * @param isPost Whether it's a Post or a Delete like request.
     * @param success Whether the operation was successful or not
     */
    private void handleLikeRequestSuccess(FBComment comment, boolean isPost, boolean success) {
        if(success) {
            // get index of comment
            int index = arrayComments.indexOf(comment);
            if(index > -1) {
                // update object
                comment.updateUserLikeState(isPost);
                // update array
                arrayComments.set(index, comment);
                // update adapter
                commentsAdapter.updateCommentAtIndex(index, comment);
            }
        }
    }

    // callbacks

    private void broadcastLoginNeeded() {
        if(fbCommentsListener != null)  fbCommentsListener.onLoginNeeded();
    }

    private void broadcastLoginSuccess() {
        if(fbCommentsListener != null)  fbCommentsListener.onLoginSuccess();
    }

    private void broadcastLoginCancelled() {
        if(fbCommentsListener != null)  fbCommentsListener.onLoginCancelled();
    }

    private void broadcastLoginFailed() {
        if(fbCommentsListener != null)  fbCommentsListener.onLoginFailed();
    }

    private void broadcastPublishPermissionNeeded() {
        if(fbCommentsListener != null)  fbCommentsListener.onPublishPermissionNeeded();
    }

    private void broadcastPostCommentResponse(boolean success) {
        if(fbCommentsListener != null)  fbCommentsListener.onPostCommentResponse(success);
    }

    private void broadcastLoadImage(ImageView iv, String url, int placeholderResId) {
        ImageHandler.getInstance(activity.getApplicationContext()).loadImageCircularCenterCrop(iv, url, placeholderResId);
    }

    // fb graph requests

    private FBGetCommentsRequest generateGetCommentsRequest() {
        FBGetCommentsRequest request = new FBGetCommentsRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setProperties(FBGraphUtils.COMMENT_FIELDS, FBGraphUtils.COMMENTS_ORDER.REVERSE_CHRONOLOGICAL, limit, after);
        request.setGraphRequestListener(getCommentsListener);
        return request;
    }

    private FBPostCommentRequest generatePostCommentRequest(String comment, OnPostCommentListener listener) {
        FBPostCommentRequest request = new FBPostCommentRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(objectId);
        request.setProperties(comment);
        request.setGraphRequestListener(listener);
        return request;
    }
    private FBGetCommentRequest generateGetCommentRequest(String commentId, OnGetCommentListener listener) {
        FBGetCommentRequest request = new FBGetCommentRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(commentId);
        request.setProperties(FBGraphUtils.COMMENT_FIELDS);
        request.setGraphRequestListener(listener);
        return request;
    }

    private FBPostLikeRequest generatePostLikeRequest(String commentId, OnLikeRequestListener listener) {
        FBPostLikeRequest request = new FBPostLikeRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(commentId);
        request.setGraphRequestListener(listener);
        return request;
    }

    private FBDeleteLikeRequest generateDeleteLikeRequest(String commentId, OnLikeRequestListener listener) {
        FBDeleteLikeRequest request = new FBDeleteLikeRequest(AccessToken.getCurrentAccessToken());
        request.setTarget(commentId);
        request.setGraphRequestListener(listener);
        return request;
    }


    // fb session requests

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookLoginProvider.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    public boolean isUserLoggedIn() {
        return FacebookLoginProvider.getInstance().isUserLoggedIn();
    }

    private void requestLogin(SocialiteLoginListener loginListener) {
        FacebookLoginProvider.getInstance().logInWithReadPermissions(activity, null, loginListener);
    }

    /**
     * Checks if the user granted the publish actions permissions to the app.
     */
    private boolean isPublishPermissionEnabled() {
        boolean result = false;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Collection<String> permissions = accessToken.getPermissions();
        if(permissions != null && permissions.contains(FBGraphUtils.PERMISSION_PUBLISH_ACTIONS)) {
            result = true;
        }
        return result;
    }

    private void requestPublishPermission(SocialiteLoginListener loginListener) {
        Collection<String> permissions = new ArrayList<>();
        permissions.add(FBGraphUtils.PERMISSION_PUBLISH_ACTIONS);
        FacebookLoginProvider.getInstance().loginWithPublishPermissions(activity, permissions, loginListener);
    }

    // queue

    /**
     * Adds the given item to requests queue.
     */
    private void queueRequest(FBGraphRequest request) {
        requestQueue.put(request);
    }

    /**
     * Clears the requests queue.
     */
    private void clearQueue() {
        requestQueue.clear();
    }

    /**
     * Returns the first request in the queue, could be null.
     */
    private FBGraphRequest getFirstQueuedRequest() {
        return requestQueue.pop();
    }

    /**
     * Removed a request from the queue.
     */
    private void dequeueRequest(FBGraphRequest request) {
        requestQueue.dequeue(request);
    }

    /**
     * Pops the first queued request and execute it.
     * @return true if there was a queued item, false otherwise.
     */
    private boolean executeFirstQueuedRequest() {
        FBGraphRequest request = getFirstQueuedRequest();
        if(request != null) {
            // set current token
            request.setAccessToken(AccessToken.getCurrentAccessToken());
            request.execute();
            return true;
        }
        return false;
    }

    class FBRequestQueue {
        private ArrayList<FBGraphRequest> queue;

        /**
         * Adds a new request to the queue.
         */
        public void put(FBGraphRequest request) {
            // init queue if needed
            if(queue == null)   queue = new ArrayList<>();
            // add item if valid
            if(request != null)     queue.add(request);
        }

        /**
         * Returns the size of the queue.
         */
        public int getSize() {
            return queue == null? 0: queue.size();
        }

        /**
         * Returns the first request in the queue,
         * null if the queue is empty.
         */
        public FBGraphRequest pop() {
            // remove and return first item in queue (FIFO)
            return getSize() > 0? queue.remove(0): null;
        }

        /**
         * Removes an item from the queue, if found.
         */
        public void dequeue(FBGraphRequest request) {
            if(request != null && queue != null) {
                queue.remove(request);
            }
        }

        /**
         * Removes all requests available in the queue, if any.
         */
        public void clear() {
            if(queue != null) {
                queue.clear();
            }
            queue = null;
        }
    }
}
