package com.ivy.green.bolt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    public TextView header;
    public TextView body;

    public LinearLayout layout;

    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        header = (TextView) findViewById(R.id.textView5);
        body = (TextView) findViewById(R.id.textView7);

        header.setVisibility(View.VISIBLE);
        body.setVisibility(View.VISIBLE);
    }

    public void setup(View view){
        i++;

        if(i == 1){

            header.setVisibility(View.GONE);
            body.setVisibility(View.GONE);

            header = (TextView) findViewById(R.id.textView8);
            body = (TextView) findViewById(R.id.textView9);
            layout = (LinearLayout) findViewById(R.id.layout);

            header.setVisibility(View.VISIBLE);
            body.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);

        }else if(i == 2){

            header.setVisibility(View.VISIBLE);
            body.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);


        }else{

        }
    }
}
