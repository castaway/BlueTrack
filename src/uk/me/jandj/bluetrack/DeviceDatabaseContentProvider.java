package uk.me.jandj.bluetrack;

import android.content.*;
import android.net.*;
import android.database.*;
import android.database.sqlite.*;


public class DeviceDatabaseContentProvider extends ContentProvider {

    private DeviceDatabase dbHelper;
    // http://developer.android.com/guide/topics/providers/content-provider-creating.html#ContentURI
    // OVERSIMPLIFIED!
    public Cursor query(Uri uri, String [] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db;
        db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT _id, name FROM bluetooth_devices ORDER BY mac", null);
    }

    public Uri insert(Uri uri, ContentValues values) {
        // inserting via here not supported (yet?), should probably throw new HissyFit().

        //SQLiteDatabase db;
        //db = dbHelper.getWritableDatabase();

        // Doesn't 
        return null;
    }

    public  int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public String getType(Uri uri) {
        // > 1 ROWS?
        return "vnd.android.cursor.dir/uk.me.jandj.bluetrack.db_provider.bluetooth_devices";
        // ROW
        // return "vnd.android.cursor.item/uk.me.jandj.bluetrack.db_provider.bluetooth_devices";
    }

    public boolean onCreate() {
        dbHelper = new DeviceDatabase( getContext() );
        return true;
    }
}
