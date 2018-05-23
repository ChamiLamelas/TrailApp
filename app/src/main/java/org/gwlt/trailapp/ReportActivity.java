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

/**
 * To add a new checkbox:
 * <p>
 * 1) go to activity_report.xml and add it to where you wish on the screen
 * 2) add new Checkbox to instance fields
 * 3) initialize new Checkbox in setUpComponents() with view identified in activity_report.xml
 * 4) add checkbox to if statement to check whether or not the checkboxes should be made invisible
 * 5) in onClick() under the creation of the OnClickListener for jReportSubmit, add the following if condition for the new Checkbox
 *      if checkbox is checked:
 *          add checkbox text + ":yes" to selectedCommonIssues
 */

/**
 * Class that represents a ReportActivity which is the Report screen for the GWLT app.
 */
public class ReportActivity extends BaseActivity {

    private Toolbar jReportToolbar; // screen's toolbar
    private TextView jReportInfo; // information on the report
    private TextView jReportDescription; // description of how the particular type of report works
    private CheckBox jTrashBox; // checkbox for trash on trail
    private CheckBox jOvergrownBox; // checkbox for overgrown trail
    // add more checkboxes here (step 2)
    private EditText jReportEntry; // report entry
    private Button jReportSubmit; // report submit button
    private String propertyName; // property that report is being submitted for
    private boolean reportType; // report type: false (sighting) true (problem)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        propertyName = getIntent().getStringExtra(BaseActivity.PROPERTY_NAME_ID);
        reportType = getIntent().getBooleanExtra(BaseActivity.REPORT_TYPE_ID, BaseActivity.DEFAULT_REPORT_TYPE);
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set up toolbar for activity and set appropriate title
        jReportToolbar = findViewById(R.id.reportToolbar);
        setSupportActionBar(jReportToolbar);
        getSupportActionBar().setTitle("Report");

        // set report information
        jReportInfo = findViewById(R.id.reportInfo);
        String reportInfoStr = "Property: " + propertyName + "\nType: ";
        reportInfoStr += (reportType == BaseActivity.REPORT_PROBLEM) ? "Problem":"Sighting";
        jReportInfo.setText(reportInfoStr);
        jReportInfo.setTypeface(jReportInfo.getTypeface(), Typeface.BOLD);

        // set report description
        jReportDescription = findViewById(R.id.reportDescription);
        if (reportType == BaseActivity.REPORT_PROBLEM)
            jReportDescription.setText(R.string.problemDescriptionTxt);

        // set up entry elements (checkboxes and text box)
        jTrashBox = findViewById(R.id.trashBox);
        jOvergrownBox = findViewById(R.id.overgrownBox);
        // initialize checkbox here (step 3)

        // set checkboxes as invisible if the report is a sighting
        if (reportType == BaseActivity.REPORT_SIGHTING) {
            jTrashBox.setVisibility(View.GONE);
            jOvergrownBox.setVisibility(View.GONE);
            // add other checkboxes here (step 4)
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
                    if (reportType == BaseActivity.REPORT_PROBLEM) {
                        if (jTrashBox.isChecked())
                            selectedCommonIssues.add(jTrashBox.getText().toString());
                        if (jOvergrownBox.isChecked())
                            selectedCommonIssues.add(jOvergrownBox.getText().toString());
                    }
                    // add to selected common issues here (step 5)
                    startActivity(Intent.createChooser(Utilities.genReport(propertyName, reportType, selectedCommonIssues, jReportEntry.getText().toString()), "Choose mail client")); // starts email activity
                    finish(); // finish the Report Activity after the email client has been opened... any further editing can be done by the user in the email client
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ReportActivity.this, "no mail client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
