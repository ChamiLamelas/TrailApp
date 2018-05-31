package org.gwlt.trailapp;

import android.content.Context;

import java.util.ArrayList;

/**
 * Class that represents a RegionalMap.
 */

public final class RegionalMap {
    private String regionName; // region name
    private int regionalMapResID; // id of image resource for the regional map
    private ArrayList<Property> properties; // property list for this map
    private int propertiesMenuResID; // id of menu resource for the regional map

    public static final String REGIONAL_MAP_NAME_ID = "regionalMapName"; // regional map name id for passing between Intents
    public static final int REGIONAL_MAP_NO_IMG_ID = -3; // no image place holder for regional maps
    public static final int REGIONAL_MAP_NO_PROPERTIES_ID = -4; // no properties list for regional maps

    public RegionalMap(String name, int mapID, int menuID) {
        regionName = name;
        regionalMapResID = mapID;
        properties = new ArrayList<>();
        propertiesMenuResID = menuID;
    }

    /**
     * Adds a property to the map in the Context where the map was created with the provided resources.
     * @param context - Context where the map as created
     * @param propertyNameResID - id of the property's name resource
     * @param propertyImgResID - id of the property's image resource
     * @param propertySeeMoreResID - id of the property's see more resource
     */
    public void addProperty(Context context, int propertyNameResID, int propertyImgResID, int propertySeeMoreResID) {
        properties.add(new Property(context.getResources().getString(propertyNameResID), propertyImgResID, propertySeeMoreResID));
    }

    /**
     * Gets the property with the provided name from the list of properties. To be used by Intents that are passed with extra String data to identify the property.
     * @param name - name of the desired property
     * @return the Property with the provided name or null if a Property with the provided name cannot be fine
     */
    public Property getPropertyWithName(String name) {
        for (Property p : properties)
            if (p.getName().equals(name))
                return p;
        return null;
    }

    public String getRegionName() {
        return regionName;
    }

    public int getRegionalMapResID() {
        return regionalMapResID;
    }

    public int getPropertiesMenuResID() {
        return propertiesMenuResID;
    }
}
