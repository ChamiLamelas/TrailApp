package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class that represents the Help Activity of the GWLT app.
 */
public class HelpActivity extends BaseActivity {

    private Toolbar jHelpToolbar;
    private TextView jHelpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set up toolbar
        jHelpToolbar = findViewById(R.id.helpToolbar);
        setSupportActionBar(jHelpToolbar);
        getSupportActionBar().setTitle(R.string.helpTxt);

        // set up instructions
        jHelpText = findViewById(R.id.helpText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.learnMoreButton) {
            try {
                // starts activity (opens browser) based on user's choice from dialog created by createChooser()
                Intent browserIntent = new Intent(Intent.ACTION_VIEW); // specifies Intent is for viewing a URI
                browserIntent.setData(Uri.parse("http://www.gwlt.org")); // sets data the Intent will work on
                startActivity(Intent.createChooser(browserIntent, "Choose browser.. "));
            }
            catch (android.content.ActivityNotFoundException ex) { // thrown by startActivity
                Toast.makeText(this, "no browser installed.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else if (id == R.id.helpButton) {
            Toast.makeText(this, "You are already on the help page.", Toast.LENGTH_LONG).show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
