package org.gwlt.trailapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {
    private Toolbar reportToolbar;
    private CheckBox trashBox;
    private CheckBox overgrownBox;
    private EditText reportEntry;
    private Button reportSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setUpUIComponents();
    }

    public void setUpUIComponents() {
        reportToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(reportToolbar);

        trashBox = findViewById(R.id.trashBox);
        overgrownBox = findViewById(R.id.overgrownBox);
        reportEntry = findViewById(R.id.reportEntry);

        reportSubmit = findViewById(R.id.reportSubmit);
        reportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(Intent.createChooser(Utilities.genReport(trashBox.isChecked(), overgrownBox.isChecked(), reportEntry.getText().toString()), "Choose mail client"));
                    finish();
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ReportActivity.this, "no mail client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
