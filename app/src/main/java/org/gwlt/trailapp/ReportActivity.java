package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * To add a new checkbox:
 *
 * 1) go to activity_report.xml and add it to where you wish on the screen
 * 2) add new Checkbox to instance fields
 * 3) initialize new Checkbox in setUpComponents() with view identified in activity_report.xml
 * 4) in onClick() under the creation of the OnClickListener for jReportSubmit, add the following if condition for the new Checkbox
 *      if checkbox is checked:
 *          add checkbox text + ":yes" to selectedCommonIssues
 */

/**
 * Class that represents a ReportActivity which is the Report screen for the GWLT app.
 */
public class ReportActivity extends BaseActivity {

    private Toolbar jReportToolbar;
    private TextView jReportInfo;
    private CheckBox jTrashBox;
    private CheckBox jOvergrownBox;
    // add more checkboxes here (step 2)
    private EditText jReportEntry;
    private Button jReportSubmit;
    private String time;
    private String propertyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        time = Utilities.getFormattedTime("MM/dd/yy hh:mm");
        propertyName = getIntent().getStringExtra(Utilities.PROPERTY_NAME_ID);
        setUpUIComponents();
    }

    /**
     * Set up UI components
     */
    @Override
    public void setUpUIComponents() {
        // set up toolbar for activity and set appropriate title
        jReportToolbar = findViewById(R.id.reportToolbar);
        setSupportActionBar(jReportToolbar);
        getSupportActionBar().setTitle("Report");

        jReportInfo = findViewById(R.id.reportInfo);
        jReportInfo.setText("Property: " + propertyName + "\nTime: " + time);
        jReportInfo.setTypeface(jReportInfo.getTypeface(), Typeface.BOLD);

        // set up entry elements (checkboxes and text box)
        jTrashBox = findViewById(R.id.trashBox);
        jOvergrownBox = findViewById(R.id.overgrownBox);
        // initialize checkbox here (step 3)
        jReportEntry = findViewById(R.id.reportEntry);

        // set up report submit button
        jReportSubmit = findViewById(R.id.reportSubmit);
        jReportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectedToInternet()) {
                    try {
                        ArrayList<String> selectedCommonIssues = new ArrayList<>();
                        if (jTrashBox.isChecked())
                            selectedCommonIssues.add(jTrashBox.getText().toString());
                        if (jOvergrownBox.isChecked())
                            selectedCommonIssues.add(jOvergrownBox.getText().toString());
                        // add to selected common issues here (step 4)
                        // starts activity based on Intent returned by Utilities.genReport()
                        startActivity(Intent.createChooser(Utilities.genReport(getSupportActionBar().getTitle().toString(), selectedCommonIssues, jReportEntry.getText().toString()), "Choose mail client"));
                        finish(); // finish the Report Activity after the email client has been opened... any further editing can be done by the user in the email client
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ReportActivity.this, "no mail client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ReportActivity.this, "An Internet connection is required to complete this action.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
