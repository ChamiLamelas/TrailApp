package org.gwlt.trailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class that represents the Main Activity of the GWLT app. This is the screen that is displayed when the user first opens the app.
 */
public final class MainActivity extends BaseActivity {

    private static ArrayList<RegionalMap> regionalMaps; // list of regional maps
    private Toolbar jAppToolbar; // screen's toolbar
    private PhotoView jPhotoView; // photo view that holds overall map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadRegionalMaps();
        setUpUIComponents();
    }

    @Override
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);
        setLearnMoreToolbar(jAppToolbar);

        // set up photo view with correct image resource
        jPhotoView = findViewById(R.id.mapPhotoView);
        jPhotoView.setImageResource(R.drawable.trust_lands);
        jPhotoView.setMaximumScale(BaseActivity.VIEW_MAX_SCALE);
    }

    /**
     * Creates a new RegionalMap using the provided parameters, adds it to the list of regionalMaps, and then returns it.
     * @param nameID - ID of string resource of the map name
     * @param imgID - ID of image resource of the map
     * @param menuID - ID of the menu resource of the map's list of properties
     * @return the new RegionalMap that has been created
     */
    private RegionalMap addRegionalMap(int nameID, int imgID, int menuID) {
        RegionalMap newMap = new RegionalMap(getResources().getString(nameID), imgID, menuID);
        regionalMaps.add(newMap);
        return newMap;
    }

    /**
     * This function initializes the map list, creates new regionalMaps, and then adds properties to the new regionalMaps.
     */
    private void loadRegionalMaps() {
        regionalMaps = new ArrayList<>();
        RegionalMap otherProperties = addRegionalMap(R.string.otherProperties,R.drawable.trust_lands,R.menu.other_properties_map_menu);
        otherProperties.addProperty(this,R.string.bovenzi,R.mipmap.bovenzi,R.string.bovenziLink );
        otherProperties.addProperty(this,R.string.nicksWoods,R.mipmap.nicks_woods_g_1,R.string.nicksWoodsLink );
        otherProperties.addProperty(this,R.string.coesReservoir,R.mipmap.coes_reservoir,R.string.coesReservoirLink );
        otherProperties.addProperty(this,R.string.crowHill,R.mipmap.crow_hill,R.string.crowHillLink );
        otherProperties.addProperty(this,R.string.tetasset,R.mipmap.tetasset,R.string.tetassetLink );
        otherProperties.addProperty(this,R.string.eastsidetrail,R.mipmap.east_side_trail_map_1,R.string.eastsideLink );
        otherProperties.addProperty(this,R.string.broadmeadow,R.mipmap.broadmeadow,R.string.broadLink );
        otherProperties.addProperty(this,R.string.sibley,R.mipmap.sibley,R.string.sibleyLink );
        otherProperties.addProperty(this,R.string.elmersSeat,R.mipmap.elmers_seat,R.string.elmersLink );
        otherProperties.addProperty(this,R.string.pineGlen, R.mipmap.pine_glen, R.string.pineLink);
        RegionalMap fourTownGreenway = addRegionalMap(R.string.fourTownGreenWayTxt, R.drawable.four_town_greenway_1, R.menu.four_town_greenway_menu);
        fourTownGreenway.addProperty(this, R.string.asnebumskit, R.mipmap.asnebumskit, R.string.asnebumskitLink);
        fourTownGreenway.addProperty(this, R.string.cascades, R.mipmap.cascades, R.string.cascadesLink);
        fourTownGreenway.addProperty(this, R.string.cookPond, R.mipmap.cooks_pond, R.string.cookPondLink);
        fourTownGreenway.addProperty(this, R.string.donkerCooksBrook, R.mipmap.donker_cooks_brook, R.string.donkerCooksLink);
        fourTownGreenway.addProperty(this, R.string.kinneywoods, R.mipmap.kinney_woods, R.string.kinneyLink);
        fourTownGreenway.addProperty(this, R.string.morelandWoods, R.mipmap.moreland_woods, R.string.morelandLink);
        fourTownGreenway.addProperty(this, R.string.southwickMuir, R.mipmap.southwick_muir, R.string.southwickLink);
    }

    /**
     * Gets the regional map with the provided name
     * @param name - name of desired regional map
     * @return the regional map with the provided name
     */
    public static RegionalMap getRegionalMapWithName(String name) {
        for (RegionalMap map : regionalMaps) {
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
                        Toast.makeText(MainActivity.this, "Regional map screen could not be opened.", Toast.LENGTH_LONG).show();
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
