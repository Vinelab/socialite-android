package com.vinelab.android.socialite.sharing.email;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Nabil Souk on 11/2/2015.
 *
 * <p>
 *     Class providing sharing functionality through any mail client on the device.
 * </p>
 */
public class EmailShareProvider {

    /**
     * Requests the system to share some content via email (compose a new email). By default, the system
     * will open a dialog to choose one of the installed mail client apps.
     * @param activity The activity requesting the share.
     * @param dialogTitle The title of the mail client chooser dialog.
     * @param subject The subject of the email to be preset.
     * @param body The body of the email to be preset.
     */
    public static void shareMessage(Activity activity, @Nullable String dialogTitle, String subject, String body) {
        shareMessage(activity, dialogTitle, "", subject, body);
    }

    /**
     * Requests the system to share some content via email (compose a new email). By default, the system
     * will open a dialog to choose one of the installed mail client apps.
     * @param activity The activity requesting the share.
     * @param dialogTitle The title of the mail client chooser dialog.
     * @param mailTo The recipient email.
     * @param subject The subject of the email to be preset.
     * @param body The body of the email to be preset.
     */
    public static void shareMessage(Activity activity, @Nullable String dialogTitle, @Nullable String mailTo, String subject, String body) {
        Intent intent = createComposeIntent(mailTo, subject, body);
        activity.startActivity(Intent.createChooser(intent, dialogTitle));
    }

    /**
     * Returns a compose email intent.
     * @param mailTo The recipient email.
     * @param subject The subject of the email to be preset.
     * @param body The body of the email to be preset.
     */
    private static Intent createComposeIntent(String mailTo, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mailTo != null? mailTo: "", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject != null? subject: "");
        intent.putExtra(Intent.EXTRA_TEXT, body != null? body: "");
        return intent;
    }
}
