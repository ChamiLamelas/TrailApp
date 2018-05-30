package org.gwlt.trailapp;

/**
 * Class that represents a Property
 */
public final class Property {
    private String name; // property name
    private int imgResID; // image resource id
    private int seeMoreResID; // see more string resource id
    private String regionName;

    public static final String PROPERTY_NAME_ID = "propertyName"; // name of the property name ID for passing between intents
    public static final int PROPERTY_NO_IMG_ID = -1; // value to identify properties with no image
    public static final int PROPERTY_NO_SEE_MORE_ID = -2; // value to identify properties with no see more

    public Property(String aName, int anImgResID, int aSeeMoreResID, String rName) {
        name = aName;
        imgResID = anImgResID;
        seeMoreResID = aSeeMoreResID;
        regionName = rName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public int getSeeMoreResID() {
        return seeMoreResID;
    }

    public String getName() {
        return name;
    }

    public String getRegionName() {
        return regionName;
    }
}
