package com.vinelab.android.socialite.fbcomments.utils;

/**
 * Created by Nabil souk on 11/17/2015.
 *
 * <p>
 *     Enumeration of the Edges supported by GraphRequest.
 * </p>
 */
public enum FBGraphEdge {
    COMMENTS("comments"),
    LIKES("likes");

    private final String edge;

    FBGraphEdge(String edge) {
        this.edge = edge;
    }

    public String getString() {
        return this.edge;
    }
}
