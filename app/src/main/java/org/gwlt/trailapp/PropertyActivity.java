package org.gwlt.trailapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static org.gwlt.trailapp.Utilities.genBrowseIntent;
import static org.gwlt.trailapp.Utilities.genEmailToGWLT;

/**
 * Class that represents PropertyActivity of GWLT app. This is the screen that applies when a user clicks on a Property name that drops down from the PopupMenu on the main screen.
 */
public final class PropertyActivity extends BaseActivity {
    private Toolbar jPropertyToolbar; // screen's toolbar
    private Button jReportButton; // button to report
    private Button jSeeMoreButton; // button to see more
    private ImageView jPropertyImageView; // image view to hold image of property map
    private Property property; // name of property
    private AlertDialog.Builder reportTypeDialog; // alert dialog for selecting report type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        loadProperty();
        setUpUIComponents();
    }

    /**
     * Loading the activity's property using the regional map name and property name provided with the Intent.
     */
    private void loadProperty() {
        RegionalMap propertysParentMap = MainActivity.getRegionalMapWithName(getIntent().getStringExtra(RegionalMap.REGIONAL_MAP_NAME_ID));
        property = propertysParentMap.getPropertyWithName(getIntent().getStringExtra(Property.PROPERTY_NAME_ID));
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
                    Toast.makeText(PropertyActivity.this, "A browser must be installed to complete this action.", Toast.LENGTH_LONG).show();
                }
            }
        });
        reportTypeDialog.setNegativeButton(R.string.reportDialogNegativeBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(Intent.createChooser(genEmailToGWLT("[Report Image] " + property.getName(), getResources().getString(R.string.reportImageBody) + property.getName() + "."), "Choose email client.."));
                    finish();
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "An email client must be installed to complete this action.", Toast.LENGTH_LONG).show();
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
                if (connectedToInternet()) {
                    try {
                        int seeMoreLink = property.getSeeMoreResID();
                        if (seeMoreLink != Property.PROPERTY_NO_SEE_MORE_ID)
                            startActivity(Intent.createChooser(genBrowseIntent(getResources().getString(seeMoreLink)), "Choose browser.."));
                        else
                            Toast.makeText(PropertyActivity.this, R.string.defaultSeeMoreTxt + property.getName() + ".", Toast.LENGTH_LONG).show();
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(PropertyActivity.this, "A browser must be installed to complete this action.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PropertyActivity.this, "An Internet connection is required to  complete this action.", Toast.LENGTH_LONG).show();
                }
            }
        });

        jPropertyImageView = findViewById(R.id.propertyImageView);
        int imgResID = property.getImgResID(); // get image resource id from property
        /*
        If the property's image resource id is not equal to the no id placeholder, set the image to the image resource with the provided id
        By default, the image resource is set to the default image (done in app/res/layout/activity_property.xml)
         */
        if (imgResID != Property.PROPERTY_NO_IMG_ID)
            jPropertyImageView.setImageResource(imgResID);
    }

}
