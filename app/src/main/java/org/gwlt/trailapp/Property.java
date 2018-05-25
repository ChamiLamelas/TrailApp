package org.gwlt.trailapp;

import android.content.res.Resources;
import android.widget.ImageView;

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

}
