package lu.acel.lidderbuch.model;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lu.acel.lidderbuch.Settings;

/**
 * Created by mirkomack on 16.11.16.
 */

public class LBShared {
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(getJsonPath());
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

    private static String getJsonPath() {
        String path = Settings.SONGS_JSON;

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String currentDateTime = sdf.format(c.getTime());
        try {
            Date pr = sdf.parse("2017-09-15 09:45:00");
            Date now = sdf.parse(currentDateTime);

            if(now.compareTo(pr) > 0) {
                path = Settings.SONGS_JSON_2017;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return path;
    }
}
