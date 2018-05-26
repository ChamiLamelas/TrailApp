package org.gwlt.trailapp;

/**
 * Class that represents a Property
 */
public final class Property {
    private String name; // property name
    private int imgResID; // image resource id
    private int seeMoreResID; // see more string resource id

    public Property(String aName, int anImgResID, int aSeeMoreResID) {
        name = aName;
        imgResID = anImgResID;
        seeMoreResID = aSeeMoreResID;
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

}
