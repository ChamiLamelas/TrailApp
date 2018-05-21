package org.gwlt.trailapp;

import android.content.Intent;
import android.net.Uri;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Utilities Library to be used by Activity classes
 */
public final class Utilities {

    public static final String PROPERTY_NAME_ID = "propertyName"; // name of the property name ID for passing extra data between intents

    /**
     * Generates an Intent that holds an email with the provided information:
     * @param recipients - who to send the email to
     * @param subject - subject of the email
     * @param body - body of the email
     * @return - email Intent that holds the data
     */
    public static Intent genEmail(String[] recipients, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND); // specifies that the Intent is for email
        emailIntent.setData(Uri.parse("mailto:")); // sets the type of data Intent will be handling
        emailIntent.setType("text/plain"); // sets MIME type (just text, text w/ attachments, etc.)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients); // puts in recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject); // puts in subject
        emailIntent.putExtra(Intent.EXTRA_TEXT, body); // puts in body
        return emailIntent;
    }

    /**
     * Generates a report email using genEmail() and getFormattedTime() as helper methods
     * @param reportTitle - title of the report
     * @param selectedCommonIssues - selected common issues
     * @param reportMsg - user's own comment
     * @return email Intent that holds report data
     */
    public static Intent genReport(String reportTitle, ArrayList<String> selectedCommonIssues, String reportMsg) {
        String reportRecipients[] = {"slamelas@bancroftschool.org","cstephenson@bancroftschool.org"};
        String reportBody = "";
        for (String selectedCommonIssue : selectedCommonIssues)
            reportBody += selectedCommonIssue + ":Yes\n";
        reportBody += reportMsg;
        return genEmail(reportRecipients, reportTitle, reportBody);
    }

    /**
     * Gets the current time using a provided pattern
     * @param pattern - e.g. "dd:MM:yyyy"
     * @return - current time formatted to fit the provided pattern
     */
    public static String getFormattedTime(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern); // creates new date format using pattern
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()); // formats calendar data provided in previous line
    }
}
