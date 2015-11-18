package com.vinelab.android.socialite.fbcomments.utils;

import com.vinelab.android.socialite.fbcomments.entities.FBComment;
import com.vinelab.android.socialite.fbcomments.entities.FBCommentAuthor;

/**
 * Created by Nabil on 11/17/2015.
 */
public class FBGraphUtils {
    public static final String PERMISSION_PUBLISH_ACTIONS = "publish_actions";
    public static final String COMMENT_FIELDS = "id,from,created_time,message,comment_count,can_comment,can_like,like_count,parent,user_likes";

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

    public static String getProfilePicture(String id, PROFILE_PICTURE_TYPE type) {
        String url = null;
        if(id != null && !id.isEmpty()) {
            url = "https://graph.facebook.com/" + id + "/picture?type=" + type.getValue();
        }
        return url;
    }
}
