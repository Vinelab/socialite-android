package com.vinelab.android.socialite.fbcomments.model;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 1/7/2016.
 *
 * Class holding the details of a graph object.
 */
public class FBUrlGraphObject {
    String id;
    String description;
    String title;
    String type;
    String updatedTime;
    String url;
    int commentCount;
    int shareCount;

    public FBUrlGraphObject(JSONObject json) {
        if(json != null) {
            try {
                if(json.has("og_object")) {
                    JSONObject jsonOG = json.getJSONObject("og_object");
                    if(jsonOG.has("id"))  this.id = jsonOG.getString("id");
                    if(jsonOG.has("description"))  this.description = jsonOG.getString("description");
                    if(jsonOG.has("title"))  this.title = jsonOG.getString("title");
                    if(jsonOG.has("type"))  this.type = jsonOG.getString("type");
                    if(jsonOG.has("updatedTime"))  this.updatedTime = jsonOG.getString("updatedTime");
                    if(jsonOG.has("url"))  this.url = jsonOG.getString("url");
                }
                if(json.has("share")) {
                    JSONObject jsonShare = json.getJSONObject("share");
                    if(jsonShare.has("comment_count"))  this.commentCount = jsonShare.getInt("comment_count");
                    if(jsonShare.has("share_count"))  this.shareCount = jsonShare.getInt("share_count");
                }
            }
            catch (Exception e) {}
        }
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public String getUrl() {
        return url;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }
}
