package org.gwlt.trailapp;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

public final class Utilities {

    public static void openEmailInDefaultMailApp(ArrayList<String> recipients, String subject, String body) {
        try {
            // String.join() added in Java 8; not supported in Android; use TextUtils.join() instead
            String formattedRecipients = TextUtils.join(",", recipients);
            String encodedSubject = URLEncoder.encode(subject, "UTF-8");
            String encodedBody = URLEncoder.encode(body, "UTF-8");
            // encode() replaces spaces with + signs, but for email URI format spaces should be replaced with %20
            String formattedString = String.format("mailto:%s?subject=%s&body=%s", formattedRecipients, encodedSubject, encodedBody).replace("+", "%20");
        }
        catch (UnsupportedEncodingException uee) {
            System.out.println("Encoding type not supported");
        }
    }

    public static Intent openLinkInDefaultBrowser(String uri) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }

}
