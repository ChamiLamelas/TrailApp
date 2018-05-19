package org.gwlt.trailapp;

import android.app.PendingIntent;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar appToolbar;
    private Button reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUIComponents();
    }

    /**
     * Sets up UI components
     */
    public void setUpUIComponents() {
        appToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(appToolbar);

        reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent reportIntent = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(reportIntent);
                    finish();
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this,"could not open report tab",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Detects clicks on Toolbar menu items and runs desired functions
     * @param item - menu item that has been triggered
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.learnMoreButton) {
            learnMore();
            return true;
        }
        else if (id == R.id.helpButton) {
            Toast.makeText(this, "HELP!", Toast.LENGTH_SHORT).show(); 
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Adds menu to the toolbar
     * @param menu - menu being added to the toolbar
     * @return true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trailapp_menu, menu);
        return true;
    }

    /**
     * Lets user choose which web browser to open the link to GWLT's home page to learn more
     */
    public void learnMore() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW); // specifies Intent is for viewing a URI
        browserIntent.setData(Uri.parse("http://www.gwlt.org")); // sets data the Intent will work on

        try {
            // starts activity (opens browser) based on user's choice from dialog created by createChooser()
            startActivity(Intent.createChooser(browserIntent, "Choose browser.. "));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) { // thrown by startActivity
            Toast.makeText(this, "no browser installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
