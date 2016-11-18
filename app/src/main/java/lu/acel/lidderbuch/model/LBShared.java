package lu.acel.lidderbuch.model;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import lu.acel.lidderbuch.Settings;

/**
 * Created by mirkomack on 16.11.16.
 */

public class LBShared {
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(Settings.SONGS_JSON);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
