package org.gwlt.trailapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Generates an email in the mail client of the user's choice with the provided report message
     * @param reportMsg - message to send in report to GWLT
     */
    public void report(String reportMsg) {
        String recipients[] = {};
        String reportTitle = "Report: " + Utilities.getFormattedTime("MM:dd:yyyy hh:mm:ss");
        Intent emailIntent = Utilities.genEmail(recipients, reportTitle, reportMsg);

        try {
            // starts activity (opens mail app) based on user's choice from dialog created by createChooser()
            startActivity(Intent.createChooser(emailIntent, "Choose mail client.. "));
            finish(); // finishes activity
        }
        catch (android.content.ActivityNotFoundException ex) { // thrown by startActivity
            Log.i("MainActivity:","no mail client installed.", ex); // use Log in place of System.out.println()
        }
    }

    /**
     * Lets user choose which web browser to open the link to GWLT's home page to learn more
     */
    public void learnMore() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW); // specifies Intent is for viewing a URI
        browserIntent.setData(Uri.parse("gwlt.org")); // sets data the Intent will work on

        try {
            // starts activity (opens browser) based on user's choice from dialog created by createChooser()
            startActivity(Intent.createChooser(browserIntent, "Choose browser.. "));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) { // thrown by startActivity
            Log.i("MainActivity:", "no browser installed.", ex);
        }
    }
}
