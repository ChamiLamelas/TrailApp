package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * To add a new property:
 *
 * 1) Add a new string to strings.xml with the name of the new property
 * 2) Add an image for the property to either drawable or mipmap
 * 3) Add a new item to the property_popup_menu.xml file with the following format:
 *      <item
 *          android:id="@+id/<insert id for new property></>"
 *          android:title="@string/<insert id of string with title of this property></>"/>
 * 4) In loadProperties(), use addProperty() to add a new property by passing in the id for string title of the property, the id for the image resource for the property, and the id of the see more info string
 *      If the property has no map image, use Utilities.NO_IMG_ID as a placeholder to represent that there is no image for that property.
 *
 * To update a property's see more information, go to strings.xml and navigate to the see more info string that corresponds to the property to be updated
 */

/**
 * Class that represents the Main Activity of the GWLT app.
 */
public class MainActivity extends BaseActivity {

    private Toolbar jAppToolbar; // screen's toolbar
    public static ArrayList<Property> properties; // list of properties
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
        loadProperties();
        setUpUIComponents();
        Toast.makeText(MainActivity.this,"Press and hold to access a list of properties.",Toast.LENGTH_LONG).show();
    }

    /**
     * Add a new property to the hashmap
     * @param nameResID - id of the string resource containing the property name
     * @param imgResID - id of the image resource containing the property map
     * @param seeMoreID - id of the string resource containing the property's see more info
     */
    private void addProperty(int nameResID, int imgResID, int seeMoreID) {
        properties.add(new Property(getResources().getString(nameResID), imgResID, seeMoreID));
    }

    /**
     * Loads properties into hashmap using addProperty()
     */
    private void loadProperties() {
        properties = new ArrayList<>();
        addProperty(R.string.oneTxt, R.mipmap.southwick_muir, R.string.oneSeeMore);
        addProperty(R.string.twoTxt, R.mipmap.tetasset, R.string.twoSeeMore);
        addProperty(R.string.threeTxt, R.mipmap.sibley, R.string.threeSeeMore);
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

    /**
     * Opens properties popup menu
     */
    private void openPropertiesMenu() {
        PopupMenu propertiesMenu = new PopupMenu(MainActivity.this, jMapImgVIew);
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
        jMapImgVIew.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openPropertiesMenu();
                return true;
            }
        });
    }
}
