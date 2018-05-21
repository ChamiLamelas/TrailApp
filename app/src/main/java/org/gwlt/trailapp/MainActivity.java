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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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
        setUpUIComponents();
    }

    private Button addButton(int btnID, int imgID) {
        Button btn = findViewById(btnID);
        final String PROPERTY_NAME = btn.getText().toString();
        properties.put(PROPERTY_NAME, imgID);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent propertyIntent = new Intent(MainActivity.this, PropertyActivity.class);
                    propertyIntent.putExtra(Utilities.PROPERTY_NAME_ID, PROPERTY_NAME);
                    startActivity(propertyIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "could not open property", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return btn;
    }

    /**
     * Sets up UI components
     */
    public void setUpUIComponents() {
        // set screen toolbar
        jAppToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(jAppToolbar);

        // set up property button
        jPropertyButton = addButton(R.id.gwltMissionButton, R.drawable.gwlt_mission_img);
    }
}
