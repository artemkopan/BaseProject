package com.artemkopan.baseproject.utils;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class IntentHelper {


    public static void intentSharingText(Context context, String text, String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooserIntent = Intent.createChooser(shareIntent, title);
        chooserIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }

    public static void intentSendSMS(Context context, String smsBody) throws ActivityNotFoundException {
        intentSendSMS(context, "", smsBody);
    }

    public static void intentSendSMS(Context context, String smsBody, String... phoneNumbers) throws ActivityNotFoundException {
        intentSendSMS(context, TextUtils.join("; ", phoneNumbers), smsBody);
    }

    public static void intentSendSMS(Context context, String phoneNumbers, String smsBody) throws ActivityNotFoundException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent it = new Intent(Intent.ACTION_SENDTO,
                                   TextUtils.isEmpty(phoneNumbers) ? null : Uri.parse(
                                           "sms:" + phoneNumbers));

            it.putExtra("sms_body", smsBody);
            it.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else {
            Intent sendIntent = new Intent(android.content.Intent.ACTION_VIEW);
            sendIntent.putExtra("address", phoneNumbers);
            sendIntent.putExtra("sms_body", smsBody);
            sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sendIntent);
        }
//        //At least KitKat
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //Need to change the build to API 19
//            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND, Uri.parse("smsto:" + phoneNumbers));
////            sendIntent.setData();
//            sendIntent.setType("text/plain");
//            sendIntent.putExtra("exit_on_sent", true);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
//
//            // Can be null in case that there is no default, then the user would be able
//            // to choose any app that support this intent.
//            if (defaultSmsPackageName != null) {
//                sendIntent.setPackage(defaultSmsPackageName);
//            }
//            sendIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(sendIntent);
//        } else {
//            // For early versions we just use ACTION_VIEW
//            Intent sendIntent = new Intent(android.content.Intent.ACTION_VIEW);
//            sendIntent.putExtra("address", phoneNumbers);
//            sendIntent.putExtra("sms_body", smsBody);
//            sendIntent.setType("vnd.android-dir/mms-sms");
//            sendIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(sendIntent);
//        }
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static void intentCall(Context context, String phoneNumber) throws ActivityNotFoundException {
        if (TextUtils.isEmpty(phoneNumber)) {
            Log.e("intentCall: Phone number is null or empty");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void sendEmail(Context context, String subject, String body, String... emails) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        if (!StringUtils.isEmpty(subject)) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (!StringUtils.isEmpty(body)) intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setData(Uri.parse("mailto:"));
        context.startActivity(intent);
    }
}
