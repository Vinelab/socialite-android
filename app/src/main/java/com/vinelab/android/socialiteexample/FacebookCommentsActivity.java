package com.vinelab.android.socialiteexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.vinelab.android.socialiteexample.comments.FacebookCommentsHandler;
import com.vinelab.android.socialiteexample.comments.OnFacebookCommentsListener;

public class FacebookCommentsActivity extends Activity {
    FacebookCommentsHandler commentsHandler;
    LinearLayout llContainer;
    ListView lvComments;
    EditText etComment;
    Button btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_comments);

        init();
        configureCommentsHandler();
        triggerCommentsHandler();
    }

    void init() {
        lvComments = (ListView) findViewById(R.id.lvComments);
        etComment = (EditText) findViewById(R.id.etComment);
        btnComment = (Button) findViewById(R.id.btnComment);
        llContainer = (LinearLayout) findViewById(R.id.llContainer);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etComment.getEditableText().toString();
                if(!message.trim().isEmpty()) {
                    commentsHandler.postNewComment(message.trim(), false);
                    etComment.setText("");
                }
            }
        });
    }

    void configureCommentsHandler() {
        commentsHandler = new FacebookCommentsHandler(this);
        commentsHandler.setConfiguration("1600885026851099_1618045101801758", 10);
        /*commentsHandler.setUrlConfiguration("url", 10);*/
        commentsHandler.setUIElements(lvComments);
        commentsHandler.setListener(facebookCommentsListener);
    }

    void triggerCommentsHandler() {
        commentsHandler.start(true);
    }

    OnFacebookCommentsListener facebookCommentsListener = new OnFacebookCommentsListener() {
        @Override
        public void onLoginNeeded() {
            commentsHandler.requestLogin();
        }

        @Override
        public void onLoginSuccess() {
            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoginCancelled() {
            Toast.makeText(getApplicationContext(), "Operation Cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoginFailed() {
            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPublishPermissionNeeded() {
            showPublishPermissionsNeeded();
        }

        @Override
        public void onPostCommentResponse(boolean success) {
            Toast.makeText(getApplicationContext(), "Posting comment " + (success? "success!":"failed!"), Toast.LENGTH_SHORT).show();
        }
    };

    private void showPublishPermissionsNeeded() {
        Snackbar.make(llContainer, "Publish permissions needed", Snackbar.LENGTH_SHORT)
                .setAction("Request", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentsHandler.requestPublishPermissions();
                    }
                })
                .setDuration(Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(commentsHandler != null) {
            commentsHandler.onActivityResult(requestCode, resultCode, data);
        }
    }
}
