package org.gwlt.trailapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {

    private Toolbar jReportToolbar;
    private CheckBox jTrashBox;
    private CheckBox jOvergrownBox;
    private EditText jReportEntry;
    private Button jReportSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setUpComponents();
    }

    public void setUpComponents() {
        jReportToolbar = findViewById(R.id.reportToolbar);
        setSupportActionBar(jReportToolbar);
        final String TIME = Utilities.getFormattedTime("MM/dd/yy hh:mm");
        getSupportActionBar().setTitle("<Property Name> " + TIME);

        jTrashBox = findViewById(R.id.trashBox);
        jOvergrownBox = findViewById(R.id.overgrownBox);
        jReportEntry = findViewById(R.id.reportEntry);

        jReportSubmit = findViewById(R.id.reportSubmit);
        jReportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(Intent.createChooser(Utilities.genReport(jTrashBox.isChecked(), jOvergrownBox.isChecked(), jReportEntry.getText().toString(), TIME), "Choose mail client"));
                    finish();
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ReportActivity.this, "no mail client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
