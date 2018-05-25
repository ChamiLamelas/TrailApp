package org.gwlt.trailapp;

public final class Property {
    private String name;
    private int imgResID;
    private int seeMoreResID;

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

    public float getMinScaleFactor() {
        // scale to something
        // get width
        // get factor from screen width's relationship with original width
        return 1.0f;
    }

}
