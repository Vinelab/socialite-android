package com.vinelab.android.socialite.fbcomments.utils;

import com.vinelab.android.socialite.fbcomments.entities.FBComment;
import com.vinelab.android.socialite.fbcomments.entities.FBCommentAuthor;

/**
 * Created by Nabil on 11/17/2015.
 *
 * <p>
 *     Utility class used by the Facebook Graph operations.
 * </p>
 */
public class FBGraphUtils {
    public static final String PERMISSION_PUBLISH_ACTIONS = "publish_actions";
    /**
     * The collection of required fields composing a comment object.
     */
    public static final String COMMENT_FIELDS = "id,from,created_time,message,comment_count,can_comment,can_like,like_count,parent,user_likes";

    /**
     * Enumeration defining the order of a fetched comments set.
     */
    public enum COMMENTS_ORDER {
        CHRONOLOGICAL("chronological"),
        RANKED("ranker"),
        REVERSE_CHRONOLOGICAL("reverse_chronological"),
        DEFAULT("default");

        private final String order;

        COMMENTS_ORDER(String order) {
            this.order = order;
        }

        public String getOrder() {
            return this.order;
        }
    }

    /**
     * Enumeration of the possible Facebook profile picture types.
     */
    public enum PROFILE_PICTURE_TYPE {
        SQUARE("square"),
        SMALL("small"),
        NORMAL("normal"),
        LARGE("large");

        private String value;

        PROFILE_PICTURE_TYPE(String text) {
            this.value = text;
        }

        public String getValue() {
            return this.value;
        }

        public static PROFILE_PICTURE_TYPE fromString(String text) {
            if (text != null) {
                for (PROFILE_PICTURE_TYPE category : PROFILE_PICTURE_TYPE.values()) {
                    if (text.equalsIgnoreCase(category.value)) {
                        return category;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Returns a Facebook profile picture url.
     * @param id The id of the profile.
     * @param type The type of the picture.
     */
    public static String getProfilePicture(String id, PROFILE_PICTURE_TYPE type) {
        String url = null;
        if(id != null && !id.isEmpty()) {
            url = "https://graph.facebook.com/" + id + "/picture?type=" + type.getValue();
        }
        return url;
    }
}
