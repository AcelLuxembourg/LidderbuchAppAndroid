package lu.acel.lidderbuch.model;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lu.acel.lidderbuch.Settings;
import lu.acel.lidderbuch.network.LBNetwork;

/**
 * Created by mirkomack on 16.11.16.
 */

public class LBBackers {
    private ArrayList<LBBacker> backers;

    public LBBackers() {
        this.backers = new ArrayList<>();
    }

    public LBBackers(JSONArray jsonArray) {
        if(jsonArray == null) return;

        this.backers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                this.backers.add(new LBBacker(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMissingLogosFromServer(Context context) {
        if(this.backers == null) return;

        ArrayList<String> existingLogoFiles = loadAllExistingLogoIds(context);
        for(int i = 0; i < backers.size(); ++i) {
            LBBacker backer = backers.get(i);
            if(!existingLogoFiles.contains(backer.getLogoId())) {
                // load Logo from Server
                Bitmap logo = LBNetwork.loadFile(backer.getLogoDisplayUrl());
                saveToInternalStorage(logo, context, backer.getLogoId());
            }
        }
    }

    public void removeUnusedLogos(Context context) {
        if(this.backers == null) return;

        ArrayList<String> logoIds = loadAllExistingLogoIds(context);

        for(int i = 0; i < logoIds.size(); ++i) {
            if(!containsLogoId(logoIds.get(i))) {
                // remove the unused logo
                deleteFromInternalSorage(logoIds.get(i), context);
            }
        }
    }

    public boolean containsLogoId(String logoId) {
        if(this.backers == null) return false;

        for(int i = 0; i < backers.size(); ++i) {
            if(backers.get(i).getLogoId().equals(logoId))
                return true;
        }
        return false;
    }

    public static Bitmap getRandomLogoFile(Context context) {
        ArrayList<String> logoIds = loadAllExistingLogoIds(context);
        if(logoIds.size() > 0) {
            int random = (int)(Math.random() * logoIds.size());
            return loadFromInternalStorage(logoIds.get(random), context);
        }

        return null;
    }

    private static File getBackersDirectory(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        return cw.getDir(Settings.BACKER_DIRECTORY, Context.MODE_PRIVATE);
    }

    private static ArrayList<String> loadAllExistingLogoIds(Context context) {
        ArrayList<String> result = new ArrayList<>();

        File directory = getBackersDirectory(context);
        File[] files = directory.listFiles();

        for(int i = 0; i < files.length; ++i) {
            result.add(files[i].getName());
            System.out.println("File found (" + i + "): " + files[i].getName());
        }
        return result;
    }

    private static boolean deleteFromInternalSorage(String logoId, Context context) {
        File directory = getBackersDirectory(context);
        File f = new File(directory, logoId);

        boolean result = f.delete();
        String b = "Logo File (" + logoId + ") ";
        if(result) {
            System.out.println(b + "deleted");
        } else {
            System.out.println(b + "could not be deleted");
        }

        return result;
    }

    private static boolean saveToInternalStorage(Bitmap bitmapImage, Context context, String logoId){
        File directory = getBackersDirectory(context);

        // Create imageDir
        File path = new File(directory, logoId);

        FileOutputStream fos = null;
        boolean result = false;
        try {
            fos = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            result = bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String b = "File (" + logoId + ") ";
        if(result) {
            System.out.println(b + "saved");
        } else {
            System.out.println(b + "could not be saved");
        }

        return result;
    }

    private static Bitmap loadFromInternalStorage(String logoId, Context context) {
        try {
            File directory = getBackersDirectory(context);
            File f = new File(directory, logoId);

            Bitmap result = BitmapFactory.decodeStream(new FileInputStream(f));

            String b = "File (" + logoId + ") ";
            if(result != null) {
                System.out.println(b + "loaded");
            } else {
                System.out.println(b + "not found");
            }
            return result;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
