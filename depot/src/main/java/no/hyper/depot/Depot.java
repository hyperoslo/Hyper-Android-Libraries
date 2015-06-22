package no.hyper.depot;

import android.content.Context;
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
     * Store a binary object. Remember to implement the Serializable interface in all
     * referenced objects
     *
     * @param name
     * @param serializableObject
     */
    public void store(String name, Object serializableObject) {
        if ( serializableObject instanceof Serializable) {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(context.openFileOutput(name, Context.MODE_PRIVATE));
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(serializableObject);
                oos.close();
                Log.i(TAG, serializableObject.getClass().getSimpleName() + " object stored to file: " + context.getFilesDir() + "/" + name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
    public void storeString(String name, String content) {
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
            List<String> list = new ArrayList();
            while ( (line = bufferedReader.readLine()) != null ) {
                list.add(line);
            }
            Log.i(TAG, "Line count: " + list.size());

            for ( int i = 0 ; i < list.size()-1 ; i++ ) {
                builder.append(list.get(i));
                builder.append("\n");
            }
            builder.append(list.get(list.size()-1));
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

//                    int size = inputStream.available();
//                    byte[] buffer = new byte[size];

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
