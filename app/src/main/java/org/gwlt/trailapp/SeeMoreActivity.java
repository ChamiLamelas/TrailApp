package org.gwlt.trailapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

/**
 * Class that represents a SeeMore Activity
 */
public final class SeeMoreActivity extends BaseActivity {

    public static final int NO_SEE_MORE_ID = -2; // value to identify properties with no see more

    private Toolbar jSeeMoreToolbar; // screen's toolbar
    private TextView jSeeMoreInfo; // screen's text info
    private String propertyName; // property see more screen is displaying information about

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        propertyName = getIntent().getStringExtra(PropertyActivity.PROPERTY_NAME_ID);
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set up see more screen with property name
        jSeeMoreToolbar = findViewById(R.id.seeMoreToolbar);
        setSupportActionBar(jSeeMoreToolbar);
        getSupportActionBar().setTitle(propertyName);

        jSeeMoreInfo = findViewById(R.id.seeMoreInfo);
        int seeMoreInfoID = MainActivity.getPropertyWithName(propertyName).getSeeMoreResID();
        if (seeMoreInfoID != NO_SEE_MORE_ID)
            jSeeMoreInfo.setText(getResources().getString(seeMoreInfoID));
    }
}
