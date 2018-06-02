# Trail App 

The purpose of this project is to create the base of an Android app for the [Greater Worcester Land Trust](http://www.gwlt.org/) with the intention for them to continue with it in the future. This project was done as part of a larger group of community service projects run by [Bancroft School](https://www.bancroftschool.org/index.cfm) . Please note that this project is still in the BETA stage.

The app contains the following features: 

* The ability to navigate the various levels of maps starting with a map of all GWLT properties, then to regional maps, then to 
individual property maps. 
* The overall map will have a list of regional maps and then each regional map has a respective list of properties. 
* For each individual property, the ability to use the GWLT's existing trail monitoring report as well as the ability to send an image of a report through email.
* The app also contains various bits of information such as a link to more information on each individual property in addition to information about GWLT and becoming a member. 

## How to Use the Project 

In addition to these steps, the source code (Java and XML) is thoroughly documented for the purpose of making it easy to use. 

### Prerequisites

In order to edit this project, you need to install [Android Studio](https://developer.android.com/studio/) which provides the ability to edit the Java, XML, and Gradle code. 

### Installing

Download the repository as a .zip file and extract the files. Once you have installed [Android Studio](https://developer.android.com/studio/) for your platform, you will have to navigate to the directory where you extracted the .zip file of the repository and open the project that [Android Studio](https://developer.android.com/studio/) will show you. 

### Editing Project

Some features, that are primarily for GWLT, such as adding new regional maps and properties have been made easy and don't require an extensive knowledge of Java or XML. However, for larger edits to how the system works, a more extensive knowledge may be required. 

### Adding New Regional Maps and Properties 

This is primarily for the GWLT to make information updates to the app. 
All of the Java code for this should be written in loadRegionalMaps().

**How to add a new RegionalMap**

1) Add the name of the region and names of the properties to app/res/values/strings.xml

*Example Name:*
```
<string name="fourTownGreenWayTxt">Four Town Greenway</string>
```

2) Add the image for the region map to the appropriate folder in app/res/ if it has an image
3) Add the menu file with the list of properties to app/res/menu/ if it has a property list

*Example Menu:*
```
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/one"
        android:title="@string/asnebumskit"/>
    <item
        android:id="@+id/two"
        android:title="@string/cascades"/>
    <item
        android:id="@+id/three"
        android:title="@string/cookPond"/>
    <item
        android:id="@+id/four"
        android:title="@string/donkerCooksBrook"/>
    <item
        android:id="@+id/five"
        android:title="@string/kinneywoods"/>
    <item
        android:id="@+id/six"
        android:title="@string/morelandWoods"/>
    <item
        android:id="@+id/seven"
        android:title="@string/southwickMuir"/>
</menu>
```

4) Use addRegionalMap() to add (see its documentation) a new map to list of regionalMaps
     If the map has no image, use RegionalMap.REGIONAL_MAP_NO_IMG_ID as a placeholder
     If the map has no property list, use RegionalMap.REGIONAL_MAP_NO_PROPERTIES_ID as a placeholder
	
*Example with image and properties:*
```
RegionalMap fourTownGreenway = addRegionalMap(R.string.fourTownGreenWayTxt, R.drawable.four_town_greenway_1, R.menu.four_town_greenway_menu);
```

*Example using placeholders:* 
```
RegionalMap fourTownGreenway = addRegionalMap(R.string.fourTownGreenWayTxt, RegionalMap.REGIONAL_MAP_NO_IMG_ID, RegionalMap.REGIONAL_MAP_NO_PROPERTIES_ID);
```

**How to add a new Property**

1) Add the name of the property to app/res/values/strings.xml

*Example Name:*
```
<string name="asnebumskit">Asnebumskit Ridge</string>
```

2) Add the image of the property to the appropriate folder in app/res/menu/ if it has an image
3) Add the link to "see more" about the property to app/res/values/strings.xml if it has "see more" information

*Example Link:*
```
<string name="asnebumskitLink">http://www.gwlt.org/lands-and-trails/four-town-worcester-greenway/asnebumskit-ridge-reservoir/</string>
```

4) On a new map that has been created call addProperty() on the new map for each property to be added to the region
     If the property has no image, use Property.PROPERTY_NO_IMG_ID as a placeholder
     If the property has no see more information, use Property.PROPERTY_NO_SEE_MORE_ID as a placeholder
	 
*Example with image and see more:*
```
fourTownGreenway.addProperty(this, R.string.asnebumskit, R.mipmap.asnebumskit, R.string.asnebumskitLink);
```

*Example using placeholders:* 
```
fourTownGreenway.addProperty(this, R.string.asnebumskit, Property.PROPERTY_NO_SEE_MORE_ID, Property.PROPERTY_NO_IMG_ID);
```

## Testing the Project

The code can be tested using the emulator built into Android Studio or an actual Android device. To familiarize yourself with Android Studio it may be useful to first the tutorials for [building your first app](https://developer.android.com/training/basics/firstapp/) provided by Android. 

If the project is unable to find R.java, make sure that your project is on the same drive you installed Android Studio into. If this does not fix the issue, the following actions can solve the issue:  
* Clean Project (Under Build Menu)
* Rebuild Project (Under Build Menu)
* Invalidate Caches and Restart (Under File Menu)
If the project is unable to identify the desired Android SDK version, go to the project build.gradle file and use the gradle sync button. 

## Authors

* **Chami Lamelas** - [LiquidsShadow](https://github.com/LiquidsShadow)
* **Christopher Stephenson** - [cgstephenson](https://github.com/cgstephenson)

Also one can view the list of [contributors](https://github.com/LiquidsShadow/TrailApp/graphs/contributors) who participated in this project.
**Chris Banes** wrote PhotoView, see License and Acknowledgements for more. 

## License 

PhotoView, which was used to implement image panning and zooming, was written entirely by [Chris Banes](https://github.com/chrisbanes) . He used the following license when creating PhotoView. Please view the README of [this repository](https://github.com/chrisbanes/PhotoView) for more information about PhotoView

PhotoView, which "aims to help produce an easily usable implementation of a zooming Android ImageView", was licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License [here](http://www.apache.org/licenses/LICENSE-2.0) .

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Acknowledgments

* [Android Studio](https://developer.android.com/studio/intro/) and the [development guides](https://developer.android.com/guide/) provided by Android. 
* The Android development tutorials on [tutorialspoint](https://www.tutorialspoint.com/android/index.htm) .
* [Chris Banes](https://github.com/chrisbanes) who made [PhotoView](https://github.com/chrisbanes/PhotoView) .
* [PurpleBooth](https://github.com/PurpleBooth) who wrote the [template](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2) for this file. 

