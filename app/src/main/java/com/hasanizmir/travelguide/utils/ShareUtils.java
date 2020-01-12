package com.hasanizmir.travelguide.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareUtils {

    public static void shareOnWhatsApp(Context context, String placeName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, placeName);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        try {
            context.startActivity(sendIntent);
        } catch (Exception ex) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
            }
        }
    }
}
