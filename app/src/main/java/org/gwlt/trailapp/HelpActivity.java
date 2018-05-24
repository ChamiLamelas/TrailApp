package org.gwlt.trailapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Class that represents the Help Activity of the GWLT app.
 */
public class HelpActivity extends BaseActivity {

    private Toolbar jHelpToolbar; // screen's toolbar

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
    }
}
