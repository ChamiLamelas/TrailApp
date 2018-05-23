package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
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

    private Toolbar jAppToolbar;
    private Button jPropertyButton;
    public static HashMap<String, Integer> properties = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadProperties();
        setUpUIComponents();
        Toast.makeText(MainActivity.this,"Double-tap to access a list of properties.",Toast.LENGTH_LONG).show();
    }

    private void addProperty(int nameResID, int imgResID) {
        String name = getResources().getString(nameResID);
        properties.put(name, imgResID);
    }

    private void loadProperties() {
        addProperty(R.string.oneTxt, R.mipmap.southwick_muir);
        addProperty(R.string.twoTxt, R.mipmap.tetasset);
        addProperty(R.string.threeTxt, R.mipmap.sibley);
    }

    /**
     * Sets up UI components
     */
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);

        // set up property button
        jPropertyButton = findViewById(R.id.gwltMissionButton);
        jPropertyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu propertiesMenu = new PopupMenu(MainActivity.this, jPropertyButton);
                propertiesMenu.getMenuInflater().inflate(R.menu.property_popup_menu, propertiesMenu.getMenu());
                propertiesMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            Intent propertyIntent = new Intent(MainActivity.this, PropertyActivity.class);
                            propertyIntent.putExtra(Utilities.PROPERTY_NAME_ID, item.getTitle().toString());
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
        });
    }
}
