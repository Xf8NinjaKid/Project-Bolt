package com.ivy.green.bolt;

/**
 * This code should not be edited after beta build.
 * It was created by Bibaswan Bhawal on the 21st of july 2016 (21/7/2016)
 * It has been written for project Bolt and should not be used in any other programs
 * */

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {


    private Typeface hanzipen;

    public static TextView welcome;
    public static TextView caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** Hide the actionBar */
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        /**loads the layout xml file */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // load fonts
        hanzipen = Typeface.createFromAsset(this.getAssets(), "fonts/font1.ttf");

        welcome = (TextView)findViewById(R.id.welcome);
        caption = (TextView)findViewById(R.id.caption);

        welcome.setTypeface(hanzipen);
        caption.setTypeface(hanzipen);


    }

    public void login(View view){
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void register(View view){
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
