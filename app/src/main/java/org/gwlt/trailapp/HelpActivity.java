package org.gwlt.trailapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Class that represents the Help Activity of the GWLT app. This is the screen that is displayed when the user clicks the help button.
 */
public final class HelpActivity extends BaseActivity {

    private Toolbar jHelpToolbar; // screen's toolbar
    private TextView jHelpTxt; // screen's text view

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
        setLearnMoreToolbar(jHelpToolbar);
        getSupportActionBar().setTitle(R.string.helpTitle);

        // set up text view
        jHelpTxt = findViewById(R.id.helpText);
        jHelpTxt.setMovementMethod(new ScrollingMovementMethod()); // allow TextView to be scrollable

    }

    /**
     * Overrides BaseActivity's onCreateOptionsMenu() in order to disable the use of the help button
     * @param menu - menu being added to the toolbar
     * @return whether or not the action could be completed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem helpBtn = menu.findItem(R.id.helpButton);
        helpBtn.setEnabled(false);
        helpBtn.setVisible(false);
        return true;
    }
}
