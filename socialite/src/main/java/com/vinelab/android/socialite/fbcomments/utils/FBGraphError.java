package com.vinelab.android.socialite.fbcomments.utils;

/**
 * Created by Nabil Souk on 11/17/2015.
 *
 * <p>
 *     Enumeration of the necessary errors when a GraphRequest fails.
 * </p>
 */
public class FBGraphError {
    public enum ERROR {
        /**
         * The request failed for unspecified reasons.
         */
        FAILED,

        /**
         * The user needs a valid session to accomplish the request.
         */
        LOGIN_NEEDED,

        /**
         * The current valid session is missing some needed permissions
         * to acoomplish the request.
         */
        PERMISSIONS_NEEDED}
}
