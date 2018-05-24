package org.gwlt.trailapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Class that represents PropertyActivity of GWLT app.
 */
public class PropertyActivity extends BaseActivity {
    private Toolbar jPropertyToolbar; // screen's toolbar
    private Button jReportButton; // button to report
    private Button jSeeMoreButton; // button to see more
    private ImageView jPropertyImageView; // image view to hold image of property map
    private String propertyName; // name of property
    private Intent reportIntent; // Intent to pass report data to ReportActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        propertyName = getIntent().getStringExtra(BaseActivity.PROPERTY_NAME_ID); // get name of property from extra data passed by Intent
        reportIntent = new Intent(PropertyActivity.this, ReportActivity.class);
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set appropriate title for property screen
        jPropertyToolbar = findViewById(R.id.propertyToolbar);
        setSupportActionBar(jPropertyToolbar);
        getSupportActionBar().setTitle(propertyName);

        // set report button to launch report Intent when clicked
        jReportButton = findViewById(R.id.reportButton);
        jReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectedToInternet()) {
                    try {
                        reportIntent.putExtra(BaseActivity.PROPERTY_NAME_ID, propertyName);

                        AlertDialog.Builder reportTypeDialog = new AlertDialog.Builder(PropertyActivity.this);
                        reportTypeDialog.setTitle(R.string.reportDialogTitle);
                        reportTypeDialog.setMessage(R.string.reportDialogInfo);
                        reportTypeDialog.setPositiveButton(R.string.reportDialogPositiveBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reportIntent.putExtra(BaseActivity.REPORT_TYPE_ID, BaseActivity.REPORT_SIGHTING);
                                startActivity(reportIntent);
                            }
                        });
                        reportTypeDialog.setNegativeButton(R.string.reportDialogNegativeBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reportIntent.putExtra(BaseActivity.REPORT_TYPE_ID, BaseActivity.REPORT_PROBLEM);
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
                    seeMoreIntent.putExtra(BaseActivity.PROPERTY_NAME_ID, propertyName);
                    startActivity(seeMoreIntent);
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "could not open see more tab",Toast.LENGTH_SHORT).show();
                }
            }
        });

        jPropertyImageView = findViewById(R.id.propertyImageView);
        int imgResID = MainActivity.getPropertyWithName(propertyName).getImgResID();
        if (imgResID != BaseActivity.NO_IMG_ID)
            jPropertyImageView.setImageResource(imgResID);
        else
            jPropertyImageView.setImageResource(BaseActivity.DEFAULT_IMG_ID);
    }

}
