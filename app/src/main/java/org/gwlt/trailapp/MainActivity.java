package org.gwlt.trailapp;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.HashMap;

/**
 * To add a new property:
 *
 * 1) Add a new string to strings.xml with the name of the new property
 * 2) Add an image for the property to either drawable or mipmap
 * 3) Add a new item to the property_popup_menu.xml file with the following format:
 *      <item
 *          android:id="@+id/<insert id for new property></>"
 *          android:title="@string/<insert id of string with title of this property></>"/>
 * 4) In loadProperties(), use addProperty() to add a new property by passing in the id for string title of the property and the id for the image resource for the property
 *      If the property has no map image, use Utilities.NO_IMG_ID as a placeholder to represent that there is no image for that property.
 */

/**
 * Class that represents the Main Activity of the GWLT app.
 */
public class MainActivity extends BaseActivity {

    private Toolbar jAppToolbar; // screen's toolbar
    private Button jPropertyButton;
    public static HashMap<String, Integer> properties; // properties map to link property names with their respective images
    private float scaleFactor; // scale factor for zooming
    private Matrix mapScalingMatrix; // matrix to scale image
    private ImageView jMapImgVIew; // image view to hold image
    private ScaleGestureDetector scaleDetector; // detector for scaling image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scaleFactor = 1.0f; // initialize to 1 to represent starting image scale (and for multiplication later on)
        mapScalingMatrix = new Matrix();
        scaleDetector = new ScaleGestureDetector(this, new ZoomListener()); // initialize for this activity with class that extends ScaleGestureDetector.SimpleOnScaleGestureListener
        loadProperties();
        setUpUIComponents();
        Toast.makeText(MainActivity.this,"Double-tap to access a list of properties.",Toast.LENGTH_LONG).show();
    }

    /**
     * Class to be used by scaleDetector for scaling the property's map image
     */
    private class ZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor(); // update scale factor from detector's data
            /*
            check to see if updated scale factor is acceptable by checking:
            1) is it smaller than minimum scale factor, if so set scale factor equal to the minimum scale factor
            2) is it larger than maximum scale factor, if so set scale factor equal to the maximum scale factor
             */
            scaleFactor = Math.max(BaseActivity.MIN_SCALE_FACTOR, Math.min(BaseActivity.MAX_SCALE_FACTOR, scaleFactor));
            mapScalingMatrix.setScale(scaleFactor, scaleFactor); // set matrix x and y to be the scale factor
            jMapImgVIew.setImageMatrix(mapScalingMatrix); // scale image using matrix
            return true;
        }
    }

    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            openPropertiesMenu();
            return true;
        }
    }

    /**
     * Add a new property to the hashmap
     * @param nameResID - id of the string resource containing the property name
     * @param imgResID - id of the image resource containing the property map
     */
    private void addProperty(int nameResID, int imgResID) {
        String name = getResources().getString(nameResID);
        properties.put(name, imgResID);
    }

    /**
     * Loads properties into hashmap using addProperty()
     */
    private void loadProperties() {
        properties = new HashMap<>();
        addProperty(R.string.oneTxt, R.mipmap.southwick_muir);
        addProperty(R.string.twoTxt, R.mipmap.tetasset);
        addProperty(R.string.threeTxt, R.mipmap.sibley);
    }

    /**
     * Opens properties popup menu
     */
    private void openPropertiesMenu() {
        PopupMenu propertiesMenu = new PopupMenu(MainActivity.this, jPropertyButton);
        propertiesMenu.getMenuInflater().inflate(R.menu.property_popup_menu, propertiesMenu.getMenu());
        propertiesMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    Intent propertyIntent = new Intent(MainActivity.this, PropertyActivity.class);
                    propertyIntent.putExtra(BaseActivity.PROPERTY_NAME_ID, item.getTitle().toString());
                    startActivity(propertyIntent);
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this,"Could not open property",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        propertiesMenu.show();
    }

    @Override
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);

        jMapImgVIew = findViewById(R.id.mapImgView);
        jMapImgVIew.setImageResource(R.drawable.gwlt_mission_img);
        jMapImgVIew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return new GestureDetector(MainActivity.this, new DoubleTapListener()).onTouchEvent(event);
            }
        });

        // set up property button
        jPropertyButton = findViewById(R.id.gwltMissionButton);
        jPropertyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openPropertiesMenu();
            }
        });
    }
}
