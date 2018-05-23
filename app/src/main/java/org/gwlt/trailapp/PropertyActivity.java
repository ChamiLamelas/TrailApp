package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Class that represents PropertyActivity of GWLT app.
 */
public class PropertyActivity extends BaseActivity {
    private Toolbar jPropertyToolbar;
    private Button jReportButton;
    private Button jSeeMoreButton;
    private ImageView jPropertyImageView;
    private String propertyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        propertyName = getIntent().getStringExtra(Utilities.PROPERTY_NAME_ID); // get name of property from extra data passed by Intent
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
                        Intent reportIntent = new Intent(PropertyActivity.this, ReportActivity.class);
                        reportIntent.putExtra(Utilities.PROPERTY_NAME_ID, propertyName);
                        startActivity(reportIntent);
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
                    seeMoreIntent.putExtra(Utilities.PROPERTY_NAME_ID, propertyName);
                    startActivity(seeMoreIntent);
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(PropertyActivity.this, "could not open see more tab",Toast.LENGTH_SHORT).show();
                }
            }
        });

        jPropertyImageView = findViewById(R.id.propertyImageView);
        int imgResID = MainActivity.properties.get(propertyName);
        if (imgResID != Utilities.NO_IMG_ID)
            jPropertyImageView.setImageResource(imgResID);
    }

}
