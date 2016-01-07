package com.vinelab.android.socialiteexample.comments.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vinelab.android.socialite.fbcomments.model.FBComment;
import com.vinelab.android.socialite.fbcomments.model.FBCommentAuthor;
import com.vinelab.android.socialite.fbcomments.utils.FBDateUtils;
import com.vinelab.android.socialite.fbcomments.utils.FBGraphUtils;
import com.vinelab.android.socialiteexample.R;

import java.util.ArrayList;

/**
 * Created by Nabil on 11/17/2015.
 */
public class CommentsAdapter extends BaseAdapter {
//    View vRow;
    Context context;
    LayoutInflater inflater;
    ArrayList<FBComment> arrayComments = new ArrayList<>();
    OnCommentRowListener rowListener;
    String strLike, strDeleteLike;
    int resAuthorPlaceholder = R.color.fb_comment_author_placeholder;

    public CommentsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.strLike = context.getResources().getString(R.string.fb_comment_like);
        this.strDeleteLike = context.getResources().getString(R.string.fb_comment_delete_like);
    }

    public void setRowListener(OnCommentRowListener listener) {
        this.rowListener = listener;
    }

    public void updateListWithNewData(ArrayList<FBComment> arrayNew) {
        if(arrayComments == null)   arrayComments = new ArrayList<>();
        if(arrayNew != null)    arrayComments.addAll(arrayNew);
        notifyDataSetChanged();
    }

    public void insertComment(FBComment comment, boolean isFirst) {
        if(comment != null) {
            if(arrayComments == null)   arrayComments = new ArrayList<>();
            if(isFirst) arrayComments.add(0, comment);
            else    arrayComments.add(comment);
            notifyDataSetChanged();
        }
    }

    public void updateCommentAtIndex(int index, FBComment comment) {
        if(arrayComments != null && getCount() > index && comment != null) {
            arrayComments.set(index, comment);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        if(arrayComments != null)   arrayComments.clear();
        arrayComments = null;
    }

    @Override
    public int getCount() {
        return arrayComments != null? arrayComments.size(): 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = inflater.inflate(R.layout.row_comment, null, false);
            holder = new ViewHolder();
            holder.ivAuthor = (ImageView) v.findViewById(R.id.ivAuthor);
            holder.tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);
            holder.tvMessage = (TextView) v.findViewById(R.id.tvMessage);
            holder.tvLike = (TextView) v.findViewById(R.id.tvLike);
            holder.tvLikes = (TextView) v.findViewById(R.id.tvLikes);
            holder.llLike = (LinearLayout) v.findViewById(R.id.llLike);
            holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        try {
            // get comment
            final FBComment comment = arrayComments.get(position);
            // get details
            FBCommentAuthor author = comment.getFrom();
            String id = author.getId();
            String name = author.getName();
            String message = comment.getMessage();
            final boolean isLiked = comment.isUserLikes();
            int likesCount = comment.getLikeCount();
            String date = getFormattedDate(comment.generateTimestamp());
            // display data
            holder.tvMessage.setText(message != null? message: "");
            holder.tvAuthorName.setText(name != null? name: "");
            // image
            String urlImage = FBGraphUtils.getProfilePicture(id, FBGraphUtils.PROFILE_PICTURE_TYPE.SQUARE);
            if(rowListener != null && urlImage != null) {
                rowListener.onLoadImage(holder.ivAuthor, urlImage, resAuthorPlaceholder);
            }
            holder.tvDate.setText(date != null? date: "");
            // likes
            if(likesCount > 0) {
                holder.llLike.setVisibility(View.VISIBLE);
                // set likes count
                holder.tvLikes.setText(String.valueOf(likesCount));
            }
            else {
                holder.llLike.setVisibility(View.GONE);
            }
            // set like action text
            holder.tvLike.setText(isLiked? strDeleteLike: strLike);
            // set click event
            holder.tvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowListener != null) {
                        if (isLiked) rowListener.onUnlike(comment);
                        else rowListener.onLike(comment);
                    }
                }
            });
        }
        catch (Exception e) {

        }

        return v;
    }

    private String getFormattedDate(long timestamp) {
        String formattedDate;
        try {
            long currentTimeMillis = System.currentTimeMillis();
            formattedDate = FBDateUtils.getRelativeTimeString(context.getResources(), currentTimeMillis, timestamp);
        }
        catch (Exception e) {
            formattedDate = null;
        }
        return formattedDate;
    }

    static class ViewHolder {
        ImageView ivAuthor;
        TextView tvAuthorName;
        TextView tvMessage;
        TextView tvLike, tvLikes;
        LinearLayout llLike;
        TextView tvDate;
    }

    public interface OnCommentRowListener {
        void onLike(FBComment comment);

        void onUnlike(FBComment comment);

        void onLoadImage(ImageView iv, String url, int placeholderResId);
    }
}