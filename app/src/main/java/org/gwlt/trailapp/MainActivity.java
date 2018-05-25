package org.gwlt.trailapp;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * To add a new property:
 *
 * 1) Add a new string to app/src/main/values/strings.xml with the name of the new property
 * 2) Add an image for the property to either drawable or mipmap
 * 3) Add a new item to the res/menu/property_popup_menu.xml file with the following format:
 *      <item
 *          android:id="@+id/<insert id for new property></>"
 *          android:title="@string/<insert id of string with title of this property></>"/>
 * 4) In loadProperties(), use addProperty() to add a new property by passing in the id for string title of the property, the id for the image resource for the property, and the id of the see more info string
 *      If the property has no map image, use BaseActivity.NO_IMG_ID as a placeholder to represent that there is no image for that property.
 *      If the property has no see more information, use BaseActivity.NO_SEE_MORE_ID as a placeholder to represent that there is no string resource for that property.
 *
 * To update a property's see more information, go to strings.xml and navigate to the see more info string that corresponds to the property to be updated
 */

/**
 * Class that represents the Main Activity of the GWLT app.
 */
public final class MainActivity extends BaseActivity {

    private Toolbar jAppToolbar; // screen's toolbar
    public static ArrayList<Property> properties; // list of properties
    private float scaleFactor; // scale factor for zooming
    private float minScaleFactor;
    private Matrix mapScalingMatrix; // matrix to scale image
    private ImageView jMapImgVIew; // image view to hold image
    private ScaleGestureDetector scaleDetector; // detector for scaling image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scaleDetector = new ScaleGestureDetector(this, new ZoomListener()); // initialize scale detector to use ZoomListener class
        loadProperties();
        setUpUIComponents();
        Toast.makeText(MainActivity.this,"Click the map icon to access a list of properties.",Toast.LENGTH_LONG).show();
    }

    /**
     * Helper class that listens for zoom actions and scales the image accordingly
     */
    private class ZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, BaseActivity.MAX_SCALE_FACTOR));
            mapScalingMatrix.setScale(scaleFactor, scaleFactor);
            jMapImgVIew.setImageMatrix(mapScalingMatrix);
            return true;
        }
    }

    /**
     * Enables the zoom listener to work on touch events on this activity
     * @param event - a motion event
     * @return whether or not the action could be performed
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        return true;
    }

    /**
     * Add a new property to the properties list with the following attributes
     * @param nameResID - id of the string resource containing the property name
     * @param imgResID - id of the image resource containing the property map
     * @param seeMoreID - id of the string resource containing the property's see more info
     */
    private void addProperty(int nameResID, int imgResID, int seeMoreID) {
        properties.add(new Property(getResources().getString(nameResID), imgResID, seeMoreID));
    }

    /**
     * Loads properties into properties list using addProperty()
     */
    private void loadProperties() {
        properties = new ArrayList<>();
        addProperty(R.string.asnebumskit, R.mipmap.asnebumskit, R.string.oneSeeMore);
        addProperty(R.string.cascades, R.mipmap.cascades, R.string.twoSeeMore);
        addProperty(R.string.cookPond, R.mipmap.cooks_pond, R.string.threeSeeMore);
        addProperty(R.string.donkerCooksBrook, R.mipmap.donker_cooks_brook, R.string.threeSeeMore);
        addProperty(R.string.kinneyWoods, R.mipmap.kinney_woods, R.string.twoSeeMore);
        addProperty(R.string.morelandWoods, R.mipmap.moreland_woods, R.string.twoSeeMore);
        addProperty(R.string.southwickMuir, R.mipmap.southwick_muir, R.string.twoSeeMore);
    }

    /**
     * Gets the property with the provided name from the list of properties
     * @param name - name of the desired property
     * @return the Property with the provided name or null if a Property with the provided name cannot be fine
     */
    public static Property getPropertyWithName(String name) {
        for (Property p : properties)
            if (p.getName().equals(name))
                return p;
        return null;
    }

    @Override
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);
        jMapImgVIew = findViewById(R.id.mapImgView);
        minScaleFactor = Utilities.calcMinScaleFactor(jMapImgVIew);
        mapScalingMatrix = new Matrix();
        scaleFactor = minScaleFactor;
        mapScalingMatrix.setScale(scaleFactor, scaleFactor);
        jMapImgVIew.setImageMatrix(mapScalingMatrix);
    }

    /**
     * MainActivity must override BaseActivity's onCreateOptionsMenu() in order to make the properties list menu item visible
     * @param menu - menu being added to the toolbar
     * @return whether or not the action could be performed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem propertiesBtn = menu.findItem(R.id.properties);
        propertiesBtn.setEnabled(true);
        propertiesBtn.setVisible(true);
        return true;
    }

    /**
     * Main Activity must override BaseActivity's onOptionsItemSelected() in order to add functionality to the properties list menu item
     * @param item - menu item that has been triggered
     * @return whether or not the action could be performed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.properties) {
            PopupMenu popupMenu = getPropertiesMenu(this, jAppToolbar);
            popupMenu.show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
