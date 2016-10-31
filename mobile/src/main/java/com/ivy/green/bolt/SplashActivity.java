package com.ivy.green.bolt;

/**
 * This code should not be edited after Beta build.
 * It was created by Bibaswan Bhawal on the 19th of july 2016 (19/7/2016)
 * It has been written for project Bolt and should not be used in any other programs Without consent of Coder
 * */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.System.exit;


public class SplashActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    public static final String PREFS_NAME = "LogData";


    private static final String TAG = "MainActivity";

    /**
     * Provides the entry point to Google Play services.
     */

    private GoogleApiClient mGoogleApiClient;

    private String falseLocation;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            } else {
                return null;
            }
        } catch (IOException ignored) {
            return "0";
        }
    }

    private void isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    /*
      Variables
     */
        Boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            runOnUiThread(new Runnable() {
                public void run() {
                    final AlertDialog.Builder noConnection = new AlertDialog.Builder(SplashActivity.this);
                    noConnection.setTitle("Couldn't connect to the internet");
                    noConnection.setMessage("Looks like you are not connected to the internet yet. Turn on mobile data or wi-fi");
                    noConnection.setNeutralButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            exit(0);
                        }
                    });

                    noConnection.setNeutralButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isConnected();
                        }
                    });

                    AlertDialog alertDialog = noConnection.create();
                    alertDialog.show();
                }
            });
        } else {
            logged_in();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Hide the actionBar */
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        /**loads the layout xml file */
        setContentView(R.layout.activity_splash);



        falseLocation = getResources().getString(R.string.false_location);

        /** checks for network connectivity and gets location*/
        buildGoogleApiClient();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    public void logged_in() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Boolean logged = settings.getBoolean("log", false);
        String email = settings.getString("Email", "Null");
        String password = settings.getString("Password", "Null");

        String log = String.valueOf((boolean) logged);
        Log.i(TAG, log);

        if(!logged){
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timerThread.start();

        }else{
            if(!email.equals("Null") || !password.equals("Null")){
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Logged-in", false);

                editor.apply();

                Thread timerThread = new Thread() {
                    public void run() {
                        try {
                            sleep(3000);
                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                timerThread.start();

            }else {
                mAuth = FirebaseAuth.getInstance();
                mAuth.addAuthStateListener(mAuthListener);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(SplashActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                                }else{
                                    Thread timerThread = new Thread() {
                                        public void run() {
                                            try {
                                                sleep(3000);

                                                if (mAuthListener != null) {
                                                    mAuth.removeAuthStateListener(mAuthListener);
                                                }

                                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    timerThread.start();
                                }
                            }
                        });
            }
        }

    }

    public void noLocation() {

        final AlertDialog.Builder noLocation = new AlertDialog.Builder(SplashActivity.this);
        noLocation.setTitle("Looks like our service isn't available in your country yet");
        noLocation.setMessage(falseLocation);

        noLocation.setNeutralButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                exit(0);
            }
        });

        AlertDialog alertDialog = noLocation.create();
        alertDialog.show();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.


        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */

    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                 PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            String country = getCountryName(this, mLastLocation.getLongitude(), mLastLocation.getLatitude());
            Log.i(TAG, "area locale = " + String.valueOf(mLastLocation.getLatitude()) + " " + String.valueOf(mLastLocation.getLongitude()) + " : " + country);

            if (!country.equals("0")) {
                if (country.equals("Egypt") || country.equals("India")) {
                    isConnected();


                } else {
                    noLocation();
                }
            } else {
                errorLocation();
            }


        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Google play services depends no these functions
     */

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        errorLocation();
        finish();
        exit(0);
    }

    private void errorLocation() {
        final AlertDialog.Builder errorLcoation = new AlertDialog.Builder(SplashActivity.this);
        errorLcoation.setTitle("Error detecting your Location");
        errorLcoation.setMessage("Sorry for the inconvenience but it looks like we couldn't get your location so please try again after enabling Wi-Fi and location services");
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        //Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Bolt")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
