package uk.me.jandj.bluetrack;

import android.os.*;
import android.content.*;
import android.database.sqlite.*;
import android.util.Log;
import java.io.*;

public class DeviceDatabase extends SQLiteOpenHelper {
    // If db is older than version, onUpgrade is run to upgrade
    // If db is newer than version, onDowngrade is run
    private static final int DATABASE_VERSION = 1;
    private static final String DEVICE_TABLE_CREATE = "CREATE TABLE " +
        " bluetooth_devices ( _id INTEGER PRIMARY KEY, " + 
        " mac TEXT NOT NULL UNIQUE, " +
        " name TEXT NOT NULL, " +
        " rssi INTEGER, " +
        " care_about INTEGER NOT NULL DEFAULT 0, " +
        " last_seen TEXT " +
        " );";

    /*
    public  DeviceDatabase my_device_database(Context context) {
        if(isExternalStorageWritable()) {
            File externalDir = new File(Environment.getExternalStorageDirectory(), "BlueTrack");
            externalDir.mkdir();
            
            return new DeviceDatabase(context, 
                                      (new File(externalDir, "bluetrack.db")).toString(),
                                      null, 
                                      DATABASE_VERSION);
        } else {
            // FIXME
            Log.e("Cannot write to external storage?");
        }
    }
    */

    DeviceDatabase(Context context) {
        super(context, "bluetrack.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DEVICE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Ain't got no upgrades yet!
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
