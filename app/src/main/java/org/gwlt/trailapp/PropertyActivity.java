package org.gwlt.trailapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static org.gwlt.trailapp.MainActivity.getPropertyWithName;
import static org.gwlt.trailapp.ReportActivity.REPORT_PROBLEM;
import static org.gwlt.trailapp.ReportActivity.REPORT_SIGHTING;
import static org.gwlt.trailapp.ReportActivity.REPORT_TYPE_ID;
import static org.gwlt.trailapp.Utilities.*;

/**
 * Class that represents PropertyActivity of GWLT app. This is the screen that applies when a user clicks on a Property name that drops down from the PopupMenu on the main screen.
 */
public final class PropertyActivity extends BaseActivity {
    private Toolbar jPropertyToolbar; // screen's toolbar
    private Button jReportButton; // button to report
    private Button jSeeMoreButton; // button to see more
    private ImageView jPropertyImageView; // image view to hold image of property map
    private Property property; // name of property
    private float scaleFactor; // scale factor for zooming
    private float minScaleFactor; // minimum scale factor for this activity
    private Matrix propertyScalingMatrix; // matrix to scale image
    private ScaleGestureDetector scaleDetector; // detector for scaling image
    private AlertDialog.Builder reportTypeDialog; // alert dialog for selecting report type

    public static final String PROPERTY_NAME_ID = "propertyName"; // name of the property name ID for passing between intents
    public static final int NO_IMG_ID = -1; // value to identify properties with no image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        property = getPropertyWithName(getIntent().getStringExtra(PROPERTY_NAME_ID)); // get name of property from extra data passed by Intent
        scaleDetector = new ScaleGestureDetector(this, new ZoomListener()); // initialize scale detector to use ZoomListener class
        setUpUIComponents();
    }

    /**
     * Helper class that listens for zoom actions and scales the image accordingly
     */
    private class ZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        /**
         * Override this method in order to update image when user makes a scaling gesture
         * @param detector - detector of scale gesture
         * @return whether or not the operation could be performed successfully
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor(); // update scale factor by multiplying by old scale factor
            /*
            Using Math.max() and Math.min() a longer if statement can be avoided, however this is what it would be:

            if the maximum scale factor is smaller than the current scale factor
                scale factor = maximum scale factor
            if the minimum scale factor is larger than the current scale factor
                scale factor = minimum scale factor
             */
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, MAX_SCALE_FACTOR));
            propertyScalingMatrix.setScale(scaleFactor, scaleFactor); // set x,y scales for scaling matrix
            jPropertyImageView.setImageMatrix(propertyScalingMatrix); // apply scale to image using matrix
            return true;
        }
    }

    /**
     * Enables the zoom listener to work on touch events on this activity
     * @param event - a motion event
     * @return whether or not the action could be performed
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void setUpUIComponents() {
        // set appropriate title for property screen
        jPropertyToolbar = findViewById(R.id.propertyToolbar);
        setSupportActionBar(jPropertyToolbar);
        getSupportActionBar().setTitle(property.getName());
        setLearnMoreToolbar(jPropertyToolbar);

        // set up report type dialog
        reportTypeDialog = new AlertDialog.Builder(this);
        reportTypeDialog.setTitle(R.string.reportDialogTitle);
        reportTypeDialog.setMessage(R.string.reportDialogInfo);
        /*
         Alert dialogs have 2 buttons that are referred to the positive and negative buttons
         For its use here, it does not matter which choice is positive or negative

         Positive: report type=sighting                 Negative: report type=problem

         These are passed through the Report Intent extra String
          */
        reportTypeDialog.setPositiveButton(R.string.reportDialogPositiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(Intent.createChooser(genBrowseIntent(getResources().getString(R.string.reportLink)),"Choose a browser.."));
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "A browser must be installed to complete this action.", Toast.LENGTH_LONG);
                }
            }
        });
        reportTypeDialog.setNegativeButton(R.string.reportDialogNegativeBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(Intent.createChooser(genEmailToGWLT("[Report Image] " + property.getName(), getResources().getString(R.string.reportImageBody)), "Choose email client.."));
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "An email client must be installed to complete this action.", Toast.LENGTH_LONG);
                }
            }
        });

        // set report button to launch report Intent when clicked
        jReportButton = findViewById(R.id.reportButton);
        jReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectedToInternet()) {
                    try {
                        reportTypeDialog.show();
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(PropertyActivity.this, "The report screen could not be opened.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PropertyActivity.this, "An Internet connection is required to complete this action.",Toast.LENGTH_LONG).show();
                }
            }
        });

        // set see more button to launch see more Intent
        jSeeMoreButton = findViewById(R.id.seeMoreButton);
        jSeeMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // set up see more Intent with extra String data being Property name
                    Intent seeMoreIntent = new Intent(PropertyActivity.this, SeeMoreActivity.class);
                    seeMoreIntent.putExtra(PROPERTY_NAME_ID, property.getName());
                    startActivity(seeMoreIntent);
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "The see more screen could not be opened.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        jPropertyImageView = findViewById(R.id.propertyImageView);
        int imgResID = property.getImgResID(); // get image resource id from property
        /*
        If the property's image resource id is not equal to the no id placeholder, set the image to the image resource with the provided id
        By default, the image resource is set to the default image (done in app/res/layout/activity_property.xml)
         */
        if (imgResID != PropertyActivity.NO_IMG_ID)
            jPropertyImageView.setImageResource(imgResID);
        minScaleFactor = calcMinScaleFactor(jPropertyImageView); // calculate minimum scale factor
        propertyScalingMatrix = new Matrix(); // set up matrix
        scaleFactor = minScaleFactor; // initialize scale factor with minimum scale factor
        propertyScalingMatrix.setScale(scaleFactor, scaleFactor); // set x,y scales for scaling matrix
        jPropertyImageView.setImageMatrix(propertyScalingMatrix); // scale image using scaling matrix
    }

}
