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

import static org.gwlt.trailapp.Utilities.LOG_TAG;
import static org.gwlt.trailapp.Utilities.moveIsValid;
import static org.gwlt.trailapp.Utilities.pointIsOnImage;

/**
 * Class that represents the Main Activity of the GWLT app. This is the screen that is displayed when the user first opens the app.
 *
 * To add a new property:
 *
 * 1) Add a new string to app/res/values/strings.xml with the name of the new property
 * 2) Add an image for the property to either drawable or mipmap (test which works by having the Java program try to load the image)
 * 3) Add a new item to the app/res/menu/property_popup_menu.xml file with the following format:
 *      <item
 *          android:id="@+id/<insert id for new property></>"
 *          android:title="@string/<insert id of string with title of this property></>"/>
 * 4) In loadProperties(), use addProperty() to add a new property by passing in the id for string title of the property, the id for the image resource for the property, and the id of the see more info string
 *      If the property has no map image, use PropertyActivity.NO_IMG_ID as a placeholder to represent that there is no image for that property.
 *      If the property has no see more information, use SeeMoreActivity.NO_SEE_MORE_ID as a placeholder to represent that there is no string resource for that property.
 *
 * To update a property's see more information, go to strings.xml and navigate to the see more info string that corresponds to the property to be updated
 */
public final class MainActivity extends BaseActivity {

    private Toolbar jAppToolbar; // screen's toolbar
    public static ArrayList<Property> properties; // list of properties
    private float scaleFactor; // scale factor for zooming
    private float minScaleFactor; // minimum scale factor for this activity
    private Matrix mapScalingMatrix; // matrix to scale image
    private ImageView jMapImgVIew; // image view to hold image
    private ScaleGestureDetector scaleDetector; // detector for scaling image
    private float saveX;
    private float saveY;
    private float startX;
    private float startY;
    private float dx;
    private float dy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scaleDetector = new ScaleGestureDetector(this, new ZoomListener()); // initialize scale detector to use ZoomListener class
        saveX = 0.0f;
        saveY = 0.0f;
        startX = 0.0f;
        startY = 0.0f;
        dx = 0.0f;
        dy = 0.0f;
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
            scaleFactor *= detector.getScaleFactor(); // update scale factor by multiplying by old scale factor
            /*
            Using Math.max() and Math.min() a longer if statement can be avoided, however this is what it would be:

            if the maximum scale factor is smaller than the current scale factor
                scale factor = maximum scale factor
            if the minimum scale factor is larger than the current scale factor
                scale factor = minimum scale factor
             */
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, BaseActivity.MAX_SCALE_FACTOR));
            mapScalingMatrix.setScale(scaleFactor, scaleFactor); // set x,y scales for scaling matrix
            jMapImgVIew.setImageMatrix(mapScalingMatrix); // apply scale to image using matrix
            return true;
        }
    }

    // TODO fix zoom and pan
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1 && !scaleDetector.isInProgress()) {
            final int ACTION = event.getActionMasked();
            final float TMP_X = event.getX();
            final float TMP_Y = event.getY();
            if (pointIsOnImage(this, jMapImgVIew, TMP_X, TMP_Y, scaleFactor)) {
                if (ACTION == MotionEvent.ACTION_DOWN) {
                    startX = TMP_X;
                    startY = TMP_Y;
                } else if (ACTION == MotionEvent.ACTION_MOVE) {
                    dx = TMP_X - startX;
                    dy = TMP_Y - startY;
                    mapScalingMatrix.postTranslate(dx, dy);
                } else if (ACTION == MotionEvent.ACTION_UP) {
                    saveX = TMP_X;
                    saveY = TMP_Y;
                    if (!moveIsValid(jMapImgVIew, dx, dy, scaleFactor)) {
                        mapScalingMatrix.postTranslate(0, 0);
                        Log.i(LOG_TAG, "Invalid move; dx="+dx+"; dy="+dy);
                    }
                }
                //Log.i(LOG_TAG, TMP_X + ";\t" + TMP_Y);
                mapScalingMatrix.setScale(scaleFactor, scaleFactor);
                jMapImgVIew.setImageMatrix(mapScalingMatrix);
            }
        }
        else {
            scaleDetector.onTouchEvent(event);
        }
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
        addProperty(R.string.asnebumskit, R.mipmap.asnebumskit, SeeMoreActivity.NO_SEE_MORE_ID);
        addProperty(R.string.cascades, R.mipmap.cascades, SeeMoreActivity.NO_SEE_MORE_ID);
        addProperty(R.string.cookPond, R.mipmap.cooks_pond, SeeMoreActivity.NO_SEE_MORE_ID);
        addProperty(R.string.donkerCooksBrook, R.mipmap.donker_cooks_brook, SeeMoreActivity.NO_SEE_MORE_ID);
        addProperty(R.string.kinneyWoods, R.mipmap.kinney_woods, SeeMoreActivity.NO_SEE_MORE_ID);
        addProperty(R.string.morelandWoods, R.mipmap.moreland_woods, SeeMoreActivity.NO_SEE_MORE_ID);
        addProperty(R.string.southwickMuir, R.mipmap.southwick_muir, SeeMoreActivity.NO_SEE_MORE_ID);
    }

    /**
     * Gets the property with the provided name from the list of properties. To be used by Intents that are passed with extra String data to identify the property.
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
        minScaleFactor = Utilities.calcMinScaleFactor(jMapImgVIew); // calculate minimum scale factor using Utility function
        mapScalingMatrix = new Matrix(); // set up image scaling matrix
        scaleFactor = minScaleFactor; // initialize scaleFactor to minimum scale factor
        mapScalingMatrix.setScale(scaleFactor, scaleFactor); // set matrix x,y scales to scale factor
        jMapImgVIew.setImageMatrix(mapScalingMatrix); // use matrix to scale image
    }

    /**
     * MainActivity must override BaseActivity's onCreateOptionsMenu() in order to make the properties list menu item visible
     * @param menu - menu being added to the toolbar
     * @return whether or not the action could be performed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // enable property button and make it visible
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
        // if user clicks properties button, open popup menu with property list, otherwise use BaseActivity's onOptionsItemSelected()
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
