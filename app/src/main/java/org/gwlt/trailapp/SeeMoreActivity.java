package org.gwlt.trailapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

@Deprecated

/**
 * Class that represents a SeeMore Activity
 */
public final class SeeMoreActivity extends BaseActivity {
    private Toolbar jSeeMoreToolbar; // screen's toolbar
    private TextView jSeeMoreInfo; // screen's text info
    private Property property; // property see more screen is displaying information about

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        //property = getPropertyWithName(getIntent().getStringExtra(Property.PROPERTY_NAME_ID)); // get name of property from extra data passed by Intent
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set up see more screen with property name
        jSeeMoreToolbar = findViewById(R.id.seeMoreToolbar);
        setSupportActionBar(jSeeMoreToolbar);
        getSupportActionBar().setTitle(property.getName());
        setLearnMoreToolbar(jSeeMoreToolbar);

        // set up text view
        jSeeMoreInfo = findViewById(R.id.seeMoreInfo);
        /*
        If the property's see more resource id is not equal to the no id placeholder, set text to the string resource with see more id
        By default, the see more text is set to the default see more text (done in app/res/layout/activity_see_more.xml)
         */
        int seeMoreInfoID = property.getSeeMoreResID();
        if (seeMoreInfoID != Property.PROPERTY_NO_SEE_MORE_ID)
            jSeeMoreInfo.setText(getResources().getString(seeMoreInfoID));
        jSeeMoreInfo.setMovementMethod(new ScrollingMovementMethod()); // allow TextView to be scrollable
    }
}
