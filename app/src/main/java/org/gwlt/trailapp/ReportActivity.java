package org.gwlt.trailapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static org.gwlt.trailapp.Utilities.genReport;

@Deprecated

/**Class that represents a ReportActivity for the GWLT app. This is the screen that is displayed when a user clicks on the Report button on the Property Screen.
 * Reports can be of two types: sighting or problem.
 *
 * To add a new checkbox:
 * <p>
 * 1) go to app/res/layout/activity_report.xml and add it to where you wish on the screen
 * 2) add new Checkbox to instance fields
 * 3) initialize new Checkbox in setUpComponents() with view identified in activity_report.xml
 * 4) add checkbox to if statement to check whether or not the checkboxes should be made invisible
 * 5) in onClick() under the creation of the OnClickListener for jReportSubmit, add the following if condition for the new Checkbox
 *      if checkbox is checked:
 *          add checkbox text + ":yes" to selectedCommonIssues
 */
public final class ReportActivity extends BaseActivity {

    public static final String REPORT_TYPE_ID = "reportType"; // name of the report type ID for passing between intents
    public static final boolean REPORT_SIGHTING = false; // report type: sighting
    public static final boolean REPORT_PROBLEM = true; // report type: problem
    public static final boolean DEFAULT_REPORT_TYPE = REPORT_SIGHTING; // default report type

    private Toolbar jReportToolbar; // screen's toolbar
    private TextView jReportInfo; // information on the report
    private TextView jReportDescription; // description of how the particular type of report works
    private CheckBox jTrashBox; // checkbox for trash on trail
    private CheckBox jOvergrownBox; // checkbox for overgrown trail
    // ********************************************************************* add more checkboxes here (step 2) *********************************************************************
    private EditText jReportEntry; // report entry
    private Button jReportSubmit; // report submit button
    private String propertyName; // property that report is being submitted for
    private boolean reportType; // report type: false (sighting) true (problem)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        propertyName = getIntent().getStringExtra(Property.PROPERTY_NAME_ID); // for reports, no other property attributes besides the property name are necessary
        reportType = getIntent().getBooleanExtra(REPORT_TYPE_ID, DEFAULT_REPORT_TYPE); // get extra boolean data with report type ID to get report type
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set up toolbar for activity and set appropriate title
        jReportToolbar = findViewById(R.id.reportToolbar);
        setSupportActionBar(jReportToolbar);
        getSupportActionBar().setTitle(R.string.reportTitleTxt);
        setLearnMoreToolbar(jReportToolbar);

        // set report information
        jReportInfo = findViewById(R.id.reportInfo);
        String reportInfoStr = "Property: " + propertyName + "\nType: ";
        reportInfoStr += (reportType == REPORT_PROBLEM) ? "Problem":"Sighting"; // set type text based on reportType instance field
        jReportInfo.setText(reportInfoStr);
        jReportInfo.setTypeface(jReportInfo.getTypeface(), Typeface.BOLD); // bold text

        // set report description
        jReportDescription = findViewById(R.id.reportDescription);
        if (reportType == REPORT_PROBLEM)
            jReportDescription.setText(R.string.problemDescriptionTxt);

        // set up entry elements (checkboxes and text box)
        jTrashBox = findViewById(R.id.trashBox);
        jOvergrownBox = findViewById(R.id.overgrownBox);
        // ********************************************************************* initialize checkbox here (step 3) *********************************************************************

        // set checkboxes as invisible if the report is a sighting
        if (reportType == REPORT_SIGHTING) {
            jTrashBox.setVisibility(View.GONE);
            jOvergrownBox.setVisibility(View.GONE);
            // ********************************************************************* add other checkboxes here (step 4) *********************************************************************
        }

        // set up report entry box
        jReportEntry = findViewById(R.id.reportEntry);

        // set up report submit button
        jReportSubmit = findViewById(R.id.reportSubmit);
        jReportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<String> selectedCommonIssues = new ArrayList<>();
                    if (reportType == REPORT_PROBLEM) {
                        if (jTrashBox.isChecked())
                            selectedCommonIssues.add(jTrashBox.getText().toString());
                        if (jOvergrownBox.isChecked())
                            selectedCommonIssues.add(jOvergrownBox.getText().toString());
                        // ********************************************************************* add to selected common issues here (step 5) *********************************************************************
                    }
                    startActivity(Intent.createChooser(genReport(propertyName, reportType, selectedCommonIssues, jReportEntry.getText().toString()), "Choose mail client")); // starts email activity
                    finish(); // finish the Report Activity after the email client has been opened... any further editing can be done by the user in the email client
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ReportActivity.this, "An email client must be installed in order to complete this action.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
