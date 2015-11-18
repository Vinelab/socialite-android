package com.vinelab.android.socialite.fbcomments.entities;

import java.util.ArrayList;

/**
 * Created by Nabil on 11/16/2015.
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
