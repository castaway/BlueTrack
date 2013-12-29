package uk.me.jandj.bluetrack;

import android.content.*;
import android.net.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

// Should look at: http://www.vogella.com/articles/AndroidSQLite/article.html#contentprovider
public class DeviceDatabaseContentProvider extends ContentProvider {

    private static final String debug_tag = "bluetrack";
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

        Log.d(debug_tag, "ContentProvider.insert, uri " + uri);

        // Doesn't 
        return null;
    }

    public  int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        Log.d(debug_tag, "ContentProvider.update, uri " + uri);
        return 0;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(debug_tag, "ContentProvider.delete, uri " + uri);
        return 0;
    }

    public String getType(Uri uri) {
        Log.d(debug_tag, "ContentProvider.getType, uri " + uri);

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
