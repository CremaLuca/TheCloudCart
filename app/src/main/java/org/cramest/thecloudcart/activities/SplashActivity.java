package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,LandActivity.class);
        startActivity(intent);
        //impediamo di tornare indietro con finish()
        finish();
    }
}
