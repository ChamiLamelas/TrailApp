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

    /**
     * Generates an Intent that holds an email to GWLT with the provided information:
     * @param subject - subject of the email
     * @param body - body of the email
     * @return - email Intent that holds the data
     */
    public static Intent genEmailToGWLT(String subject, String body) {
        String recipients[] = {"cstephenson@bancroftschool.org","slamelas@bancroftschool.org"};
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
     * @param name - name of the property being reported
     * @param type - type of the report
     * @param selectedCommonIssues - common issues that were selected if it's a problem report
     * @param msg - message of the report
     * @return email Intent that holds report data
     */
    public static Intent genReport(String name, boolean type, ArrayList<String> selectedCommonIssues, String msg) {
        String reportBody = "";
        String reportTitle = "[";
        if (type == BaseActivity.REPORT_PROBLEM)
            reportTitle += "PROBLEM";
        else
            reportTitle += "SIGHTING";
        reportTitle += "] " + name;
        for (String selectedCommonIssue : selectedCommonIssues)
            reportBody += selectedCommonIssue + ":Yes\n";
        reportBody += msg;
        return genEmailToGWLT(reportTitle, reportBody);
    }
}
