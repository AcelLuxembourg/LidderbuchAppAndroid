package lu.acel.lidderbuch.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lu.acel.lidderbuch.helper.FontHelper;
import lu.acel.lidderbuch.R;

/**
 * Created by mirkomack on 16.11.16.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FontHelper.init(getApplicationContext());

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start Lidderbuch app (intro activity)
                Intent i = new Intent(SplashActivity.this, IntroActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(i, 0);
                overridePendingTransition(0,0);
                //startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
