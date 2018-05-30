package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Region;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class RegionalMapActivity extends BaseActivity {

    private RegionalMap regionalMap;
    private Toolbar jRMapToolbar;
    private ImageView jRMapView;
    private float scaleFactor;
    private float minScaleFactor;
    private Matrix mapScalingMatrix;

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
        setUpUIComponents();
    }

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

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem popupBtn = menu.findItem(R.id.popupMenu);
        popupBtn.setEnabled(true);
        popupBtn.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.popupMenu) {
            int propertiesResID = regionalMap.getPropertiesMenuResID();
            if (propertiesResID == RegionalMap.REGIONAL_MAP_NO_PROPERTIES_ID) {
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
                            Toast.makeText(RegionalMapActivity.this, "Property screen could not be opened.", Toast.LENGTH_LONG);
                        }
                        return true;
                    }
                });
            }
            else {
                Toast.makeText(this, "There are no properties currently listed for this region.", Toast.LENGTH_LONG);
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
