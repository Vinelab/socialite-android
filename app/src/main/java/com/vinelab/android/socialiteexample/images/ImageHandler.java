package com.vinelab.android.socialiteexample.images;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nabil on 11/19/2015.
 */
public class ImageHandler {
    private static ImageHandler imageHandler = null;
    private static Picasso imageLoader;

    private ImageHandler(Context context) {
        imageLoader = Picasso.with(context);
    }

    public static ImageHandler getInstance(Context context) {
        if(imageHandler == null) {
            imageHandler = new ImageHandler(context);
        }
        return imageHandler;
    }

    public void loadImageRectCenterCrop(ImageView iv, String url, int placeholderResId) {
        if(imageLoader != null && url != null) {
            imageLoader.load(url)
                    .placeholder(placeholderResId)
                    .fit()
                    .centerCrop()
                    .into(iv);
        }
    }

    public void loadImageCircularCenterCrop(ImageView iv, String url, int placeholderResId) {
        if(imageLoader != null && url != null) {
            imageLoader.load(url)
                    .placeholder(placeholderResId)
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(iv);
        }
    }
}
