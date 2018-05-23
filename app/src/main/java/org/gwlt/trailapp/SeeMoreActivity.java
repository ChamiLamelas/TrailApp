package org.gwlt.trailapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Class that represents a SeeMore Activity
 */
public class SeeMoreActivity extends BaseActivity {

    private Toolbar jSeeMoreToolbar; // screen's toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set up see more screen with property name
        jSeeMoreToolbar = findViewById(R.id.seeMoreToolbar);
        setSupportActionBar(jSeeMoreToolbar);
        getSupportActionBar().setTitle("More about " + getIntent().getStringExtra(BaseActivity.PROPERTY_NAME_ID));
    }
}
