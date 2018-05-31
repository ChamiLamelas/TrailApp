package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class that represents the Main Activity of the GWLT app. This is the screen that is displayed when the user first opens the app.
 *
 * All of this should be done in loadMaps()
 *
 * How to add a new RegionalMap
 *
 * 1) Add the name of the region and names of the properties to app/res/values/strings.xml
 * 2) Add the image for the region map to the appropriate folder in app/res/ if it has an image
 * 3) Add the menu file with the list of properties to app/res/menu/ if it has a property list
 * 4) Use addMap() to add (see its documentation) a new map to list of maps
 *      If the map has no image, use RegionalMap.REGIONAL_MAP_NO_IMG_ID as a placeholder
 *      If the map has no property list, use RegionalMap.REGIONAL_MAP_NO_PROPERTIES_ID as a placeholder
 *
 * How to add a new Property
 *
 * 1) Add the name of the property to app/res/values/strings.xml
 * 2) Add the image of the property to the appropriate folder in app/res/menu/ if it has an image
 * 3) Add the link to "see more" about the property to app/res/values/strings.xml if it has "see more" information
 * 4) On a new map that has been created call addProperty() on the new map for each property to be added to the region
 *      If the property has no image, use Property.PROPERTY_NO_IMG_ID as a placeholder
 *      If the property has no see more information, use Property.PROPERTY_NO_SEE_MORE_ID as a placeholder
 */

public final class MainActivity extends BaseActivity {

    private static ArrayList<RegionalMap> maps;
    private Toolbar jAppToolbar; // screen's toolbar
    private ImageView jMapImgVIew; // image view to hold image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMaps();
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);
        setLearnMoreToolbar(jAppToolbar);
        jMapImgVIew = findViewById(R.id.mapImgView);
        setImageView(jMapImgVIew);
        super.setUpUIComponents();
    }

    /**
     * Creates a new RegionalMap using the provided parameters, adds it to the list of maps, and then returns it.
     * @param nameID - ID of string resource of the map name
     * @param imgID - ID of image resource of the map
     * @param menuID - ID of the menu resource of the map's list of properties
     * @return the new RegionalMap that has been created
     */
    private RegionalMap addMap(int nameID, int imgID, int menuID) {
        RegionalMap newMap = new RegionalMap(getResources().getString(nameID), imgID, menuID);
        maps.add(newMap);
        return newMap;
    }

    /**
     * This function initializes the map list, creates new maps, and then adds properties to the new maps.
     */
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

    /**
     * Gets the regional map with the provided name
     * @param name - name of desired regional map
     * @return the regional map with the provided name
     */
    public static RegionalMap getRegionalMapWithName(String name) {
        for (RegionalMap map : maps) {
            if (map.getRegionName().equals(name))
                return map;
        }
        return null;
    }

    /**
     * MainActivity must override this method in order to activate the popup menu button on the toolbar
     * @param menu - menu being added to the toolbar
     * @return true to activate menu
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
     * MainActivity must override this method in order to provide functionality to the popup menu button on the toolbar
     * @param item - menu item that has been triggered
     * @return true to allow popup button functionality
     */
    @Override
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
