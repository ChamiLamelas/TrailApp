package org.gwlt.trailapp;

import android.content.Intent;
import android.net.Uri;

/**
 * Utilities Library to be used by GWLT trail app Activities.
 */
public final class Utilities {
    public static final String LOG_TAG = "[GWLT Trail App]"; // log tag to be used by Log

    public static Intent genBrowseIntent(String uriString) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW); // specifies Intent is for viewing a URI
        browserIntent.setData(Uri.parse(uriString)); // sets data the Intent will work on
        return browserIntent;
    }

    /**
     * Generates an Intent that holds an email to GWLT with the provided information:
     * @param subject - subject of the email
     * @param body - body of the email
     * @return - email Intent that holds the data
     */
    public static Intent genEmailToGWLT(String subject, String body) {
        String recipients[] = {"trailapp@gwlt.org"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND); // specifies that the Intent is for email
        emailIntent.setData(Uri.parse("mailto:")); // sets the type of data Intent will be handling
        emailIntent.setType("text/plain"); // sets MIME type (just text, text w/ attachments, etc.)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients); // puts in recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject); // puts in subject
        emailIntent.putExtra(Intent.EXTRA_TEXT, body); // puts in body
        return emailIntent;
    }
}
