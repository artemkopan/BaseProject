package com.artemkopan.baseproject.helper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;

public class IntentHelper {


    public static void intentSharingText(Context context, String text, String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooserIntent = Intent.createChooser(shareIntent, title);
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }

    public static void intentSendSMS(Activity activity, String smsBody) {
        //At least KitKat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Need to change the build to API 19
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(activity);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, smsBody);

            // Can be null in case that there is no default, then the user would be able
            // to choose any app that support this intent.
            if (defaultSmsPackageName != null) {
                sendIntent.setPackage(defaultSmsPackageName);
            }

            activity.startActivity(sendIntent);
        } else {
            // For early versions we just use ACTION_VIEW
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", smsBody);
            activity.startActivity(sendIntent);
        }
    }
}
