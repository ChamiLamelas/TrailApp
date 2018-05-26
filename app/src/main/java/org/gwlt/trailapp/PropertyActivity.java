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

/**
 * Class that represents PropertyActivity of GWLT app.
 */
public final class PropertyActivity extends BaseActivity {
    private Toolbar jPropertyToolbar; // screen's toolbar
    private Button jReportButton; // button to report
    private Button jSeeMoreButton; // button to see more
    private ImageView jPropertyImageView; // image view to hold image of property map
    private Property property; // name of property
    private Intent reportIntent; // Intent to pass report data to ReportActivity
    private float scaleFactor; // scale factor for zooming
    private float minScaleFactor; // minimum scale factor for this activity
    private Matrix propertyScalingMatrix; // matrix to scale image
    private ScaleGestureDetector scaleDetector; // detector for scaling image
    public static final String PROPERTY_NAME_ID = "propertyName"; // name of the property name ID for passing between intents

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        property = MainActivity.getPropertyWithName(getIntent().getStringExtra(PropertyActivity.PROPERTY_NAME_ID)); // get name of property from extra data passed by Intent
        reportIntent = new Intent(PropertyActivity.this, ReportActivity.class);
        scaleDetector = new ScaleGestureDetector(this, new ZoomListener()); // initialize scale detector to use ZoomListener class
        setUpUIComponents();
    }

    /**
     * Helper class that listens for zoom actions and scales the image accordingly
     */
    private class ZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, BaseActivity.MAX_SCALE_FACTOR));
            propertyScalingMatrix.setScale(scaleFactor, scaleFactor);
            jPropertyImageView.setImageMatrix(propertyScalingMatrix);
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

        // set report button to launch report Intent when clicked
        jReportButton = findViewById(R.id.reportButton);
        jReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectedToInternet()) {
                    try {
                        reportIntent.putExtra(PropertyActivity.PROPERTY_NAME_ID, property.getName());

                        AlertDialog.Builder reportTypeDialog = new AlertDialog.Builder(PropertyActivity.this);
                        reportTypeDialog.setTitle(R.string.reportDialogTitle);
                        reportTypeDialog.setMessage(R.string.reportDialogInfo);
                        reportTypeDialog.setPositiveButton(R.string.reportDialogPositiveBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reportIntent.putExtra(ReportActivity.REPORT_TYPE_ID, ReportActivity.REPORT_SIGHTING);
                                startActivity(reportIntent);
                            }
                        });
                        reportTypeDialog.setNegativeButton(R.string.reportDialogNegativeBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reportIntent.putExtra(ReportActivity.REPORT_TYPE_ID, ReportActivity.REPORT_PROBLEM);
                                startActivity(reportIntent);
                            }
                        });
                        reportTypeDialog.show();

                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(PropertyActivity.this, "could not open report tab", Toast.LENGTH_SHORT).show();
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
                    Intent seeMoreIntent = new Intent(PropertyActivity.this, SeeMoreActivity.class);
                    seeMoreIntent.putExtra(PropertyActivity.PROPERTY_NAME_ID, property.getName());
                    startActivity(seeMoreIntent);
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "could not open see more tab",Toast.LENGTH_SHORT).show();
                }
            }
        });

        jPropertyImageView = findViewById(R.id.propertyImageView);
        int imgResID = property.getImgResID();
        if (imgResID != BaseActivity.NO_IMG_ID)
            jPropertyImageView.setImageResource(imgResID);
        else
            jPropertyImageView.setImageResource(BaseActivity.DEFAULT_IMG_ID);
        minScaleFactor = Utilities.calcMinScaleFactor(jPropertyImageView);
        propertyScalingMatrix = new Matrix();
        scaleFactor = minScaleFactor;
        propertyScalingMatrix.setScale(scaleFactor, scaleFactor);
        jPropertyImageView.setImageMatrix(propertyScalingMatrix);
    }

}
