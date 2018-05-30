package org.gwlt.trailapp;

import java.util.ArrayList;

public final class RegionalMap {
    private String regionName;
    private int regionalMapResID;
    private ArrayList<Property> properties;
    private int propertiesMenuResID;

    public static final String REGIONAL_MAP_NAME_ID = "regionalMapName";
    public static final int REGIONAL_MAP_NO_IMG_ID = -3;
    public static final int REGIONAL_MAP_NO_PROPERTIES_ID = -4;

    public RegionalMap(String name, int mapID, int menuID) {
        regionName = name;
        regionalMapResID = mapID;
        properties = new ArrayList<>();
        propertiesMenuResID = menuID;
    }

    public void addProperty(String propertyName, int propertyImgResID, int propertySeeMoreResID) {
        properties.add(new Property(propertyName, propertyImgResID, propertySeeMoreResID, regionName));
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
