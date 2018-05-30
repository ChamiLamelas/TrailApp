package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Class that represents the Regional Map Activity. This is the screen that is displayed when Regional maps are displayed.
 */
public class RegionalMapActivity extends BaseActivity {

    private RegionalMap regionalMap; // regional map being represented
    private Toolbar jRMapToolbar; // screen's toolbar
    private ImageView jRMapView; // image view holding regional map image
    private float scaleFactor; // current scale factor being applied to the image
    private float minScaleFactor; // minimum scale factor
    private Matrix mapScalingMatrix; // matrix being used to scale the image
    private ScaleGestureDetector scaleDetector; // detector for scaling image

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
            jRMapView.setImageMatrix(mapScalingMatrix); // apply scale to image using matrix
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regional_map);
        regionalMap = MainActivity.getRegionalMapWithName(getIntent().getStringExtra(RegionalMap.REGIONAL_MAP_NAME_ID));
        scaleDetector = new ScaleGestureDetector(this, new ZoomListener()); // initialize scale detector to use ZoomListener class
        setUpUIComponents();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void setUpUIComponents() {
        jRMapToolbar = findViewById(R.id.rMapToolbar);
        setSupportActionBar(jRMapToolbar);
        getSupportActionBar().setTitle(regionalMap.getRegionName());
        setLearnMoreToolbar(jRMapToolbar);
        jRMapView = findViewById(R.id.rMapImgView);
        int mapImgID = regionalMap.getRegionalMapResID();
        if (mapImgID != RegionalMap.REGIONAL_MAP_NO_IMG_ID)
            jRMapView.setImageResource(mapImgID);
        minScaleFactor = Utilities.calcMinScaleFactor(jRMapView); // calculate minimum scale factor using Utility function
        mapScalingMatrix = new Matrix(); // set up image scaling matrix
        scaleFactor = minScaleFactor; // initialize scaleFactor to minimum scale factor
        mapScalingMatrix.setScale(scaleFactor, scaleFactor); // set matrix x,y scales to scale factor
        jRMapView.setImageMatrix(mapScalingMatrix); // use matrix to scale image
    }

    /**
     * RegionalMapActivity must override this in order to enable the popup button to show the popup menu for the regional map's list of properties
     * @param menu - menu being added to the toolbar
     * @return true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem popupBtn = menu.findItem(R.id.popupMenu);
        popupBtn.setEnabled(true);
        popupBtn.setVisible(true);
        return true;
    }

    /**
     * RegionalMapActivity must override this in order to enable the popup menu of properties to be clicked
     * @param item - menu item that has been triggered
     * @return true to display menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.popupMenu) {
            int propertiesResID = regionalMap.getPropertiesMenuResID();
            if (propertiesResID != RegionalMap.REGIONAL_MAP_NO_PROPERTIES_ID) {
                PopupMenu propertiesMenu = new PopupMenu(this, jRMapToolbar);
                propertiesMenu.getMenuInflater().inflate(propertiesResID, propertiesMenu.getMenu());
                propertiesMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            Intent propertyIntent = new Intent(RegionalMapActivity.this, PropertyActivity.class);
                            propertyIntent.putExtra(Property.PROPERTY_NAME_ID, item.getTitle().toString());
                            propertyIntent.putExtra(RegionalMap.REGIONAL_MAP_NAME_ID, regionalMap.getRegionName());
                            startActivity(propertyIntent);
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(RegionalMapActivity.this, "Property screen could not be opened.", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                });
                propertiesMenu.show();
            }
            else {
                Toast.makeText(this, "There are no properties currently listed for this region.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
