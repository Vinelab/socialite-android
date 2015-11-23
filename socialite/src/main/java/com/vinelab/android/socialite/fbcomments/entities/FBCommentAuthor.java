package com.vinelab.android.socialite.fbcomments.entities;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 11/15/2015.
 *
 * <p>
 *     Class holding the details of a Facebook comment author.
 * </p>
 */
public class FBCommentAuthor {
    String name;
    String id;

    public FBCommentAuthor(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public FBCommentAuthor(JSONObject json) {
        if(json != null) {
            try {
                if(json.has("name"))    name = json.getString("name");
                if(json.has("id"))  id = json.getString("id");
            }
            catch (Exception e) {
                name = null;
                id = null;
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
