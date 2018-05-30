package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
 */

public final class MainActivity extends BaseActivity {

    private static ArrayList<RegionalMap> maps;
    private Toolbar jAppToolbar; // screen's toolbar
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
        loadMaps();
        setUpUIComponents();
    }

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
                } else if (ACTION == MotionEvent.ACTION_UP) {
                    //Log.i(LOG_TAG, "Finger up");
                    if (!moveIsValid(jMapImgVIew, dx, dy, scaleFactor)) {
                        dx = 0;
                        dy = 0;
                        saveX = 0;
                        saveY = 0;
                        //Log.i(LOG_TAG, "Invalid move; dx="+dx+"; dy="+dy);
                    } else {
                        saveX = TMP_X;
                        saveY = TMP_Y;
                    }
                }
            } else {
                dx = 0;
                dy = 0;
                saveX = 0;
                saveY = 0;
            }
            //Log.i(LOG_TAG, TMP_X + ";\t" + TMP_Y);
            Log.i(LOG_TAG, "point is on image");
            mapScalingMatrix.setScale(scaleFactor, scaleFactor);
            mapScalingMatrix.postTranslate(dx, dy);
            jMapImgVIew.setImageMatrix(mapScalingMatrix);
        } else {
            scaleDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);
        setLearnMoreToolbar(jAppToolbar);
        jMapImgVIew = findViewById(R.id.mapImgView);
        minScaleFactor = Utilities.calcMinScaleFactor(jMapImgVIew); // calculate minimum scale factor using Utility function
        mapScalingMatrix = new Matrix(); // set up image scaling matrix
        scaleFactor = minScaleFactor; // initialize scaleFactor to minimum scale factor
        mapScalingMatrix.setScale(scaleFactor, scaleFactor); // set matrix x,y scales to scale factor
        jMapImgVIew.setImageMatrix(mapScalingMatrix); // use matrix to scale image
    }

    private RegionalMap addMap(int nameID, int imgID, int menuID) {
        RegionalMap newMap = new RegionalMap(getResources().getString(nameID), imgID, menuID);
        maps.add(newMap);
        return newMap;
    }

    private void loadMaps() {
        maps = new ArrayList<>();
        // add maps using addMap()
        // add properties for each map using addProperty()
        RegionalMap fourTownGreenway = addMap(R.string.fourTownGreenWayTxt, R.drawable.four_town_greenway_1, R.menu.four_town_greenway_menu);
        fourTownGreenway.addProperty(this, R.string.asnebumskit, R.mipmap.asnebumskit, Property.PROPERTY_NO_SEE_MORE_ID);
        fourTownGreenway.addProperty(this, R.string.cascades, R.mipmap.cascades, Property.PROPERTY_NO_SEE_MORE_ID);
        fourTownGreenway.addProperty(this, R.string.cookPond, R.mipmap.cooks_pond, Property.PROPERTY_NO_SEE_MORE_ID);
        fourTownGreenway.addProperty(this, R.string.donkerCooksBrook, R.mipmap.donker_cooks_brook, Property.PROPERTY_NO_SEE_MORE_ID);
        fourTownGreenway.addProperty(this, R.string.kinneyWoods, R.mipmap.kinney_woods, Property.PROPERTY_NO_SEE_MORE_ID);
        fourTownGreenway.addProperty(this, R.string.morelandWoods, R.mipmap.moreland_woods, Property.PROPERTY_NO_SEE_MORE_ID);
        fourTownGreenway.addProperty(this, R.string.southwickMuir, R.mipmap.southwick_muir, Property.PROPERTY_NO_SEE_MORE_ID);
    }

    public static RegionalMap getRegionalMapWithName(String name) {
        for (RegionalMap map : maps) {
            if (map.getRegionName().equals(name))
                return map;
        }
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem popupBtn = menu.findItem(R.id.popupMenu);
        popupBtn.setEnabled(true);
        popupBtn.setVisible(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.popupMenu) {
            PopupMenu mapsMenu = new PopupMenu(this, jAppToolbar);
            mapsMenu.getMenuInflater().inflate(R.menu.maps_menu, mapsMenu.getMenu());
            mapsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        Intent mapIntent = new Intent(MainActivity.this, RegionalMapActivity.class);
                        mapIntent.putExtra(RegionalMap.REGIONAL_MAP_NAME_ID, item.getTitle().toString());
                        startActivity(mapIntent);
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "Regional map screen could not be opened.", Toast.LENGTH_LONG);
                    }
                    return true;
                }
            });
            mapsMenu.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
