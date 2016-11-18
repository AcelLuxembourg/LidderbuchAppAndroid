package lu.acel.lidderbuch.model;


import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mirkomack on 16.11.16.
 */

public class LBBacker implements Serializable {
    private URL logoDisplayUrl;
    private String logoId;

    public String getLogoId() { return logoId; }
    public void setLogoId(String logoId) { this.logoId = logoId; }

    public URL getLogoDisplayUrl() { return logoDisplayUrl; }
    public void setLogoDisplayUrl(URL logoDisplayUrl) { this.logoDisplayUrl = logoDisplayUrl; }

    public LBBacker(JSONObject jsonObject) {
        // retrieve required attributes
        try {
            logoDisplayUrl = new URL(jsonObject.getString("logo_display_url"));
            logoId = jsonObject.getString("logo_id");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
