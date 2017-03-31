package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.activities.LandActivity;
import org.cramest.thecloudcart.classi.ListaCategorie;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this,LandActivity.class);
        startActivity(intent);
        //impediamo di tornare indietro con finish()
        finish();
    }
}
