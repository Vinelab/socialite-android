package com.vinelab.android.socialite.fbcomments.actions;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.vinelab.android.socialite.fbcomments.entities.FBComment;
import com.vinelab.android.socialite.fbcomments.entities.FBGetCommentsResponse;
import com.vinelab.android.socialite.fbcomments.entities.FBGraphResponse;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphEdge;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nabil Souk on 11/17/2015.
 */
public class FBGetCommentsRequest extends FBGraphRequest {

    public FBGetCommentsRequest(AccessToken accessToken) {
        super(accessToken);
        setEdge(FBGraphEdge.COMMENTS);
        setMethod(HttpMethod.GET);
    }

    public void setProperties(String fields, FBGraphUtils.COMMENTS_ORDER order, int limit, String after) {
        this.params = new Bundle();
        if(fields != null)  this.params.putString("fields", fields);
        if(order != FBGraphUtils.COMMENTS_ORDER.DEFAULT)    this.params.putString("order", order.getOrder());
        if(limit > 0)   this.params.putInt("limit", limit);
        if(after != null && !after.isEmpty())    params.putString("after", after);
        params.putBoolean("summary", true);
    }

    @Override
    public FBGraphResponse processResponse(JSONObject graphObject) {
        FBGraphResponse graphResponse;
        try {
            // create callback data
            ArrayList<FBComment> resultComments = new ArrayList<>();
            String after = null;
            boolean hasNext = false;
            int totalCount = 0;
            boolean canComment = false;

            // get comments
            JSONArray data = graphObject.getJSONArray("data");
            for(int i = 0; i < data.length(); i++) {
                JSONObject jsonComment = data.getJSONObject(i);
                FBComment comment = new FBComment(jsonComment);
                resultComments.add(comment);
            }
            // get paging
            JSONObject jsonPaging = graphObject.getJSONObject("paging");
            if(jsonPaging != null) {
                if(jsonPaging.has("cursors")) {
                    JSONObject jsonCursors = jsonPaging.getJSONObject("cursors");
                    if(jsonCursors != null && jsonCursors.has("after"))
                        after = jsonCursors.getString("after");
                }
                if(jsonPaging.has("next"))  hasNext = true;
            }
            // get total count, and if user can comment
            if(graphObject.has("summary")) {
                JSONObject jsonSummary = graphObject.getJSONObject("summary");
                if(jsonSummary.has("total_count"))  totalCount = jsonSummary.getInt("total_count");
                if(jsonSummary.has("can_comment"))  canComment = jsonSummary.getBoolean("can_comment");
            }
            // create response object
            graphResponse = new FBGetCommentsResponse(resultComments, hasNext, after, totalCount, canComment);
        }
        catch (Exception e) {
            graphResponse = null;
        }
        return graphResponse;
    }
}
