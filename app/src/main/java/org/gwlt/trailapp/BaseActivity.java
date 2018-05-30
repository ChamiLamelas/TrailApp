package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import static org.gwlt.trailapp.Utilities.genBrowseIntent;

/**
 * All Activities of the GWLT trail app inherit their properties from BaseActivity.
 * Note that this class is abstract, which means it cannot be implemented and thus does not have an associated XML file like the other GWLT Activities.
 * Therefore, the following rules apply to GWLT trail app activities:
 * <p>
 * All GWLT app activities will have a shared toolbar with "learn more" and help buttons.
 * Title of toolbar can be edited after toolbar is set as the supportActionBar.
 * To edit toolbar items, subclass should override onOptionsItemSelected()
 * Subclass activities should override onCreate() to setContentView() on their respective layout XML files.
 * Only reason this is not abstract is because AppCompatActivity.onCreate() has to be called in it.
 * Subclass activities must override setUpUIComponents() to implement the activity's UI components.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final float MAX_SCALE_FACTOR = 5.0f; // maximum possible scale factor to be used in image scaling in MainActivity
    private Toolbar learnMoreToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Detects clicks on Toolbar menu items and runs desired functions
     *
     * @param item - menu item that has been triggered
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // get item id to identify item using resource ids
        if (id == R.id.learnMoreButton) {
            PopupMenu learnMoreMenu = getLearnMoreMenu(this, learnMoreToolbar);
            learnMoreMenu.show();
            return true;
        } else if (id == R.id.helpButton) {
            try {
                Intent helpIntent = new Intent(this, HelpActivity.class); // opens help screen
                startActivity(helpIntent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "The help screen could not be opened.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Adds menu to the toolbar
     *
     * @param menu - menu being added to the toolbar
     * @return true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trailapp_menu, menu); // display menu
        // disable properties button and make it invisible
        MenuItem popupMenu = menu.findItem(R.id.popupMenu);
        popupMenu.setEnabled(false);
        popupMenu.setVisible(false);
        return true;
    }

    /**
     * If the Context (and thus the Activity) has an active Internet connection
     *
     * @return whether not Content is connected to the Internet
     */
    public boolean connectedToInternet() {
        /*
        ConnectivityManager is the "class that answers queries about the state of network connectivity. It also notifies applications when network connectivity changes."
        It must be retrieved using this code:
         */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connectedNetworkInfo = connMgr.getActiveNetworkInfo(); // get network info on the active network
        /*
        If there is no active network, connectedNetworkInfo == null, so that check must be performed first in order to avoid NullPointerException when isConnected() is called
        Thus, this returns false if connectedNetworkInfo == null or if connectedNetworkInfo.isConnected() returns false
         */
        return connectedNetworkInfo != null && connectedNetworkInfo.isConnected();
    }

    /**
     * Gets properties popup menu for a provided Context connected to a provided View
     *
     * @param context - context the popup menu will be displayed on
     * @param view    - view that the popup menu will be connected on
     * @return popup menu with the properties list
     */
    public PopupMenu getPropertiesMenu(Context context, View view) {
        final Context CONTEXT = context; // must store as final variable to be used in PopupMenu.OnMenuItemClickListener implementation
        PopupMenu propertiesMenu = new PopupMenu(CONTEXT, view); // instantiate PopupMenu with provided arguments
        propertiesMenu.getMenuInflater().inflate(R.menu.property_popup_menu, propertiesMenu.getMenu()); // display menu
        propertiesMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // set up listener for popup menu item clicks
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    Intent propertyIntent = new Intent(CONTEXT, PropertyActivity.class); // create Property Intent on the provided Context
                    /*
                    In order for the PropertyActivity to load the correct data, the Property Intent must hold Property name identifier data
                    This data is the title of the PopupMenu item and will be used by getPropertyWithName() to get the Property object
                     */
                    propertyIntent.putExtra(PropertyActivity.PROPERTY_NAME_ID, item.getTitle().toString());
                    startActivity(propertyIntent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(CONTEXT, "Could not open property", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        return propertiesMenu;
    }

    public PopupMenu getLearnMoreMenu(Context context, View view) {
        final Context CONTEXT = context;
        final PopupMenu learnMoreMenu = new PopupMenu(CONTEXT, view);
        learnMoreMenu.getMenuInflater().inflate(R.menu.learn_more_options_menu, learnMoreMenu.getMenu());
        learnMoreMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemID = item.getItemId();
                if (itemID == R.id.learnMoreChoice) {
                    try {
                        startActivity(Intent.createChooser(genBrowseIntent(getResources().getString(R.string.learnMoreLink)), "Choose browser.."));
                    }
                    catch (ActivityNotFoundException ex) {
                        Toast.makeText(CONTEXT, "A browser must be installed to complete this action.", Toast.LENGTH_LONG);
                    }
                }
                else if (itemID == R.id.joinChoice) {
                    try {
                        startActivity(Intent.createChooser(genBrowseIntent(getResources().getString(R.string.joinLink)), "Choose browser.."));
                    }
                    catch (ActivityNotFoundException ex) {
                        Toast.makeText(CONTEXT, "A browser must be installed to complete this action.", Toast.LENGTH_LONG);
                    }
                }
                return true;
            }
        });
        return learnMoreMenu;
    }

    public abstract void setUpUIComponents(); // all child classes must implement this

    public void setLearnMoreToolbar (Toolbar toolbar) {
        learnMoreToolbar = toolbar;
    }
}
