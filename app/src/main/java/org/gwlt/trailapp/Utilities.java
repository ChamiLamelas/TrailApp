package org.gwlt.trailapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

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

    @Deprecated
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
        if (type == ReportActivity.REPORT_PROBLEM)
            reportTitle += "PROBLEM";
        else
            reportTitle += "SIGHTING";
        reportTitle += "] " + name;
        for (String selectedCommonIssue : selectedCommonIssues)
            reportBody += selectedCommonIssue + ":Yes\n";
        reportBody += msg;
        return genEmailToGWLT(reportTitle, reportBody);
    }

    /**
     * Calculates the minimum scale factor for the provided image view based on the resource being stored in the ImageView and the display's screen size
     * @param imageView - provided image view that holds the image to be scaled
     * @return minimum scale factor as a float
     */
    public static float calcMinScaleFactor(ImageView imageView) {
        float initWidth = imageView.getDrawable().getIntrinsicWidth(); // get default width of image view's image resource in pixels
        float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // get screen width in pixels
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams(); // must account for image view's margins
        float leftMarginInPixels = layoutParams.leftMargin;
        float rightMarginInPixels = layoutParams.rightMargin;
        return (screenWidth-(rightMarginInPixels+leftMarginInPixels))/initWidth; // scale factor = (screen width - margins)/initial width (i.e. how much to scale initial image dimensions)
    }

    /**
     * Gets the action bar height of a Context.
     * @param context - context with action bar to be measured
     * @return height of the action bar or -1.0f if the Context has no action bar
     */
    public static float getActionBarHeight(Context context) {
        TypedValue outValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize,outValue,true))
            return TypedValue.complexToDimension(outValue.data, context.getResources().getDisplayMetrics());
        return -1.0f;
    }

    /**
     * Gets the maximum x-value this image can have
     * @param imageView - image view holding the image
     * @param xScaleFactor - x scale factor being applied to the image
     * @return the maximum x-value this image can have
     */
    public static float maxX(ImageView imageView, float xScaleFactor) {
        float initWidth = imageView.getDrawable().getIntrinsicWidth();
        return initWidth * xScaleFactor;
    }

    /**
     * Gets the maximum y-value this image can have
     * @param imageView - image view holding the image
     * @param yScaleFactor - y scale factor being applied to the image
     * @return the maximum y-value this image can have
     */
    public static float maxY(ImageView imageView, float yScaleFactor) {
        float initHeight = imageView.getDrawable().getIntrinsicHeight();
        return initHeight * yScaleFactor;
    }

    /**
     * Determines whether or not the point is on this image
     * @param context - Context the ImageView is on
     * @param imageView - the ImageView holding the image
     * @param x - the x-value of the point
     * @param y - the y-value of the point
     * @param scaleFactor - scale factor being applied to the x and y of the image
     * @return boolean state of whether or not the point is on the image
     */
    public static boolean pointIsOnImage(Context context, ImageView imageView, float x, float y, float scaleFactor) {
        float minX = ((ViewGroup.MarginLayoutParams) imageView.getLayoutParams()).leftMargin;
        float minY = getActionBarHeight(context);
        boolean xIsValid = x > minX && x < maxX(imageView, scaleFactor);
        boolean yIsValid = y > minY && y < maxY(imageView, scaleFactor);
        return xIsValid && yIsValid;
    }

    /**
     * Checks whether a move is valid given a change in x and y would exceed the image boundaries.
     * @param imageView - the ImageView holding the image
     * @param dx - change in x
     * @param dy - change in y
     * @param scaleFactor - scale factor being applied to the x and y of the image
     * @return boolean state of whether or not the move is valid
     */
    public static boolean moveIsValid(ImageView imageView, float dx, float dy, float scaleFactor) {
        float initWidth = imageView.getDrawable().getIntrinsicWidth();
        float initHeight = imageView.getDrawable().getIntrinsicHeight();
        float finalX = initWidth*scaleFactor + Math.abs(dx);
        float finalY = initHeight*scaleFactor + Math.abs(dy);
        boolean xMoveIsValid = finalX < maxX(imageView, scaleFactor);
        boolean yMoveIsValid = finalY < maxY(imageView, scaleFactor);
        Log.i(LOG_TAG, "finalX="+finalX+"\tfinalY="+finalY+"\tmaxX="+maxX(imageView, scaleFactor)+"\tmaxY="+maxY(imageView, scaleFactor));
        return xMoveIsValid && yMoveIsValid;
    }
}
