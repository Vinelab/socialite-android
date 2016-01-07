package com.vinelab.android.socialite.fbcomments.responses;

import com.vinelab.android.socialite.fbcomments.model.FBComment;

import java.util.ArrayList;

/**
 * Created by Nabil Souk on 11/16/2015.
 *
 * <p>
 *     Class holding the data returned after successfully fetching
 *     a list of Facebook comments.
 * </p>
 */
public class FBGetCommentsResponse extends FBGraphResponse{
    final ArrayList<FBComment> comments;
    final boolean hasNext;
    final String after;
    final int totalCount;
    final boolean canComment;

    public FBGetCommentsResponse(ArrayList<FBComment> comments, boolean hasNext, String after, int totalCount, boolean canComment) {
        this.comments = comments;
        this.hasNext = hasNext;
        this.after = after;
        this.totalCount = totalCount;
        this.canComment = canComment;
    }

    public ArrayList<FBComment> getComments() {
        return comments;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public String getAfter() {
        return after;
    }
}
