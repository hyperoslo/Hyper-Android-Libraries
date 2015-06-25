package no.hyper.depot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by espenalmdahl on 18/06/15.
 */
public class Depot {

    private static Depot singleton;
    private Context context;
    private static final String TAG = "==> DEPOT <==";
    private String directory;

    private Depot(Context context) {
        this.context = context.getApplicationContext();
        directory = "";
    }

    public static synchronized Depot with(Context context) {
        if (singleton == null) {
            singleton = new Depot(context);
        }
        singleton.directory = "";
        return singleton;
    }


    public static Depot under(String directory) {
        if (singleton == null) {
            Log.e(TAG, "Depot is null, run like this: Depot.with(context).under(directory)");
        }
        else {
            singleton.directory = directory + "/";
        }
        return singleton;
    }


    /**
     *
     * Store primitives in SharedPreferences. Feels like overkill to create a separate file for each.
     *
     */
    public void store(String name, boolean flag) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        prefs.putBoolean(name, flag);
        prefs.commit();
    }

    public void store(String name, int i) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        prefs.putInt(name, i);
        prefs.commit();
    }

    public void store(String name, float f) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        prefs.putFloat(name, f);
        prefs.commit();
    }

    public void store(String name, long l) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        prefs.putLong(name, l);
        prefs.commit();
    }


    /**
     * Store a binary object. Remember to implement the Serializable interface in all
     * referenced objects
     *
     * @param name
     * @param serializableObject
     */
    public void store(String name, Object serializableObject) {
        if ( serializableObject instanceof Serializable) {
            BufferedOutputStream bos = null;
            ObjectOutputStream oos = null;
            try {
                String tmpPath = context.getFilesDir() + "/tmp/" + name;
                bos = new BufferedOutputStream(context.openFileOutput(tmpPath, Context.MODE_PRIVATE));
                oos = new ObjectOutputStream(bos);
                oos.writeObject(serializableObject);
                oos.close();
                Log.i(TAG, serializableObject.getClass().getSimpleName() + " object stored to file: " + context.getFilesDir() + "/" + name);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
        else {
            Log.e(TAG, "Object " + serializableObject.getClass().getSimpleName() + " does not implement Serializable.");
        }
    }


    /**
     * Store a string and keep it readable for humans
     * @param name
     * @param content String to store
     */
    public void store(String name, String content) {
        try {
            File f = new File(context.getFilesDir(), name);
            FileWriter writer = new FileWriter(f);
            writer.write(content);
            writer.close();
            Log.i(TAG, "String saved to file: " + context.getFilesDir() + "/" + name);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String retrieveString(String name) {
        try {
            File f = new File(context.getFilesDir(), name);
            FileReader reader = new FileReader(f);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ( (line = bufferedReader.readLine()) != null ) {
                builder.append(line);
                builder.append("\n");
            }
            bufferedReader.close();
            return builder.toString();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object retrieve(String name) {

        if (this.contains(name)) {
            try {
                InputStream inputStream = context.openFileInput(name);
                if ( inputStream != null ) {

                    ObjectInputStream ois = new ObjectInputStream(inputStream);
                    Object object = ois.readObject();

                    ois.close();
                    Log.i(TAG, "Object read: " + object.toString());
                    return object;
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e(TAG, "File " + name + " does not exist");
        }
        return null;
    }


    public boolean contains(String name) {
        File file = new File(context.getFilesDir() + "/" + name);
        return file.exists();
    }
}
