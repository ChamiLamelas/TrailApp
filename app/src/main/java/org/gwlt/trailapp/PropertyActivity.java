package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import static org.gwlt.trailapp.Utilities.genBrowseIntent;
import static org.gwlt.trailapp.Utilities.genEmailToGWLT;

/**
 * Class that represents PropertyActivity of GWLT app. This is the screen that applies when a user clicks on a Property name that drops down from the PopupMenu on the main screen.
 */
public final class PropertyActivity extends BaseActivity {
    private Toolbar jPropertyToolbar; // screen's toolbar
    private PhotoView jPropertyPhotoView; // image view to hold image of property map
    private Property property; // name of property

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        loadProperty();
        setUpUIComponents();
    }

    /**
     * Loading the activity's property using the regional map name and property name provided with the Intent.
     */
    private void loadProperty() {
        RegionalMap propertysParentMap = MainActivity.getRegionalMapWithName(getIntent().getStringExtra(RegionalMap.REGIONAL_MAP_NAME_ID));
        property = propertysParentMap.getPropertyWithName(getIntent().getStringExtra(Property.PROPERTY_NAME_ID));
    }

    @Override
    public void setUpUIComponents() {
        // set appropriate title for property screen
        jPropertyToolbar = findViewById(R.id.propertyToolbar);
        setSupportActionBar(jPropertyToolbar);
        getSupportActionBar().setTitle(property.getName());
        setLearnMoreToolbar(jPropertyToolbar);

        // set up photo view
        jPropertyPhotoView = findViewById(R.id.propertyPhotoView);
        int imgResID = property.getImgResID(); // get image resource id from property
        /*
        If the property's image resource id is not equal to the no id placeholder, set the image to the image resource with the provided id
        By default, the image resource is set to the default image specified by BaseActivity.DEFAULT_IMAGE_PLACEHOLDER_ID
         */
        if (imgResID == Property.PROPERTY_NO_IMG_ID)
            jPropertyPhotoView.setImageResource(BaseActivity.DEFAULT_IMAGE_PLACEHOLDER_ID);
        else
            jPropertyPhotoView.setImageResource(imgResID);
    }

    /**
     * PropertyActivity must override this in order to enable the popup button to show the popup menu for the property's list of possible actions and change its icon
     *
     * @param menu - menu being added to the toolbar
     * @return true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem popupBtn = menu.findItem(R.id.popupMenu);
        popupBtn.setEnabled(true);
        popupBtn.setVisible(true);
        popupBtn.setIcon(android.R.drawable.ic_menu_agenda);
        return true;
    }

    /**
     * PropertyActivity must override this in order to enable the popup menu of a property's possible actions to be clicked
     *
     * @param item - menu item that has been triggered
     * @return true to display menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.popupMenu) {
            PopupMenu propertiesActionsMenu = new PopupMenu(this, jPropertyToolbar);
            propertiesActionsMenu.getMenuInflater().inflate(R.menu.properties_actions_menu, propertiesActionsMenu.getMenu());
            propertiesActionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final int ITEM_ID = item.getItemId();
                    switch (ITEM_ID) {
                        case R.id.fillOutReportChoice:
                            if (connectedToInternet()) {
                                try {
                                    startActivity(Intent.createChooser(genBrowseIntent(getResources().getString(R.string.reportLink)), "Choose a browser.."));
                                } catch (ActivityNotFoundException ex) {
                                    Toast.makeText(PropertyActivity.this, "A browser must be installed to complete this action.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(PropertyActivity.this, "An Internet connection is required to  complete this action.", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case R.id.emaiLImageChoice:
                            if (connectedToInternet()) {
                                try {
                                    startActivity(Intent.createChooser(genEmailToGWLT(PropertyActivity.this,"[Report Image] " + property.getName(), getResources().getString(R.string.reportImageBody) + property.getName() + "."), "Choose email client.."));
                                } catch (ActivityNotFoundException ex) {
                                    Toast.makeText(PropertyActivity.this, "An email client must be installed to complete this action.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(PropertyActivity.this, "An Internet connection is required to  complete this action.", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case R.id.seeMoreChoice:
                            if (connectedToInternet()) {
                                try {
                                    int seeMoreLink = property.getSeeMoreResID();
                                    if (seeMoreLink != Property.PROPERTY_NO_SEE_MORE_ID)
                                        startActivity(Intent.createChooser(genBrowseIntent(getResources().getString(seeMoreLink)), "Choose browser.."));
                                    else
                                        Toast.makeText(PropertyActivity.this, R.string.defaultSeeMoreTxt + property.getName() + ".", Toast.LENGTH_LONG).show();
                                } catch (ActivityNotFoundException ex) {
                                    Toast.makeText(PropertyActivity.this, "A browser must be installed to complete this action.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PropertyActivity.this, "An Internet connection is required to  complete this action.", Toast.LENGTH_LONG).show();
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            propertiesActionsMenu.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
