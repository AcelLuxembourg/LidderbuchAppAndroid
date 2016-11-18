package lu.acel.lidderbuch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import org.json.JSONArray;

import lu.acel.lidderbuch.R;
import lu.acel.lidderbuch.Settings;
import lu.acel.lidderbuch.helper.FontHelper;
import lu.acel.lidderbuch.model.LBBackers;
import lu.acel.lidderbuch.network.LBNetwork;

/**
 * Created by mirkomack on 16.11.16.
 */

public class IntroActivity extends AppCompatActivity {
    private static int INTRO_TIME_OUT = 900;

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FetchBackersTask().execute();
        FontHelper.init(getApplicationContext());

        Bitmap logo = LBBackers.getRandomLogoFile(getApplicationContext());

        setContentView(R.layout.activity_intro);

        if(logo != null) {
            ImageView mImg;
            mImg = (ImageView) findViewById(R.id.img_backer_logo);
            mImg.setImageBitmap(logo);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start Lidderbuch app (main activity)
                    //startActivity(i);

                    startMain();

                    // close this activity
                    finish();
                }
            }, INTRO_TIME_OUT);
        } else {
            startMain();
        }
    }

    private void startMain() {
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 0);
        overridePendingTransition(0, 0);
    }

    class FetchBackersTask extends AsyncTask<Void, Void, Void> {

        LBBackers backers;

        public FetchBackersTask() {

        }

        protected Void doInBackground(Void... voids) {

            // Fetch the newest Backerlist from the Server and Store it in the shared prefs
            JSONArray backersArray = LBNetwork.requestWebService(Settings.BACKER_API);
            backers = new LBBackers(backersArray);

            backers.loadMissingLogosFromServer(getApplicationContext());
            backers.removeUnusedLogos(getApplicationContext());

            return null;
        }

        protected void onPostExecute(Void vo) { }
    }


}
