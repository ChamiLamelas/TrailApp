package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Represents the Base Activity of Activities of the GWLT app, this is why this class does not have an associated layout XML file.
 *
 * All GWLT app activities will have a shared toolbar with "learn more" and help buttons.
 *      Title of toolbar can be edited after toolbar is set as the supportActionBar.
 *      To edit toolbar items, subclass should override onOptionsItemSelected()
 * Subclass activities should override onCreate() to setContentView() on their respective layout XML files.
 *      Only reason this is not abstract is because AppCompatActivity.onCreate() has to be called in it.
 * Subclass activities must override setUpUIComponents() to implement the activity's UI components.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // moved these variables from Utilities because they seemed more suited to be properties of GWLT app activities rather than utilities to be used by GWLT activities
    public static final String PROPERTY_NAME_ID = "propertyName"; // name of the property name ID for passing between intents
    public static final String REPORT_TYPE_ID = "reportType"; // name of the report type ID for passing between intents
    public static final boolean REPORT_SIGHTING = false; // report type: sighting
    public static final boolean REPORT_PROBLEM = true; // report type: problem
    public static final boolean DEFAULT_REPORT_TYPE = REPORT_SIGHTING; // default report type
    public static final int NO_IMG_ID = -1; // value to identify properties with no image
    public static final int DEFAULT_IMG_ID = R.drawable.gwlt_mission_img; // default image
    public static final int NO_SEE_MORE_ID = -2; // value to identify properties with no see more
    public static final float MIN_SCALE_FACTOR = 0.1f; // minimum possible scale factor to be used in image scaling in MainActivity
    public static final float MAX_SCALE_FACTOR = 5.0f; // minimum possible scale factor to be used in image scaling in MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Detects clicks on Toolbar menu items and runs desired functions
     * @param item - menu item that has been triggered
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.learnMoreButton) {
            if (connectedToInternet()) {
                try {
                    // starts activity (opens browser) based on user's choice from dialog created by createChooser()
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW); // specifies Intent is for viewing a URI
                    browserIntent.setData(Uri.parse("http://www.gwlt.org")); // sets data the Intent will work on
                    startActivity(Intent.createChooser(browserIntent, "Choose browser.. "));
                } catch (android.content.ActivityNotFoundException ex) { // thrown by startActivity
                    Toast.makeText(this, "no browser installed.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "An Internet connection is required to complete this action.",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        else if (id == R.id.helpButton) {
            try {
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
            }
            catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "could not open help", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Adds menu to the toolbar
     * @param menu - menu being added to the toolbar
     * @return true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trailapp_menu, menu);
        return true;
    }

    /**
     * If the Context (and thus the Activity) has an active Internet connection
     * @return whether not Content is connected to the Internet
     */
    public boolean connectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connectedNetworkInfo = connMgr.getActiveNetworkInfo();
        return connectedNetworkInfo != null && connectedNetworkInfo.isConnected();
    }

    public abstract void setUpUIComponents(); // all child classes must implement this
}
