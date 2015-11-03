package com.vinelab.android.socialite.sharing.whatsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by Nabil Souk on 11/2/2015.
 *
 * <p>
 *     Class providing sharing functionality on Whatsapp.
 * </p>
 */
public class WhatsappShareProvider {
    private static final String PACKAGE_NAME = "com.whatsapp";

    /**
     * Requests the system to share a message using Whatsapp.
     * @param activity The activity starting the share.
     * @param message The text message to be shared.
     * @return true if the Whatsapp application is installed and the intent was started, false otherwise.
     */
    public static boolean shareMessage(Activity activity, String message) {
        try {
            // Check if package exists or not. If not then code
            activity.getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_META_DATA);
            // package installed, start the share intent
            activity.startActivity(createShareIntent(message));
            // flag success
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            // flag package not installed
            return false;
        }
    }

    /**
     * Creates the proper share intent.
     */
    private static Intent createShareIntent(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(PACKAGE_NAME);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        return intent;
    }
}
