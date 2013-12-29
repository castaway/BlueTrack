package uk.me.jandj.bluetrack;

import android.app.*;
import android.content.*;
import android.database.*;
import android.widget.*;
import android.os.*;
import android.net.*;
import android.util.Log;


// http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
public class DeviceDatabaseCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String debug_tag = "bluetrack";
    // Hoops!
    Context myActivity;
    SimpleCursorAdapter theAdapter;
    DeviceDatabaseCallbacks(Context context, SimpleCursorAdapter adapter) {
        myActivity = context;
        theAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(debug_tag, "Callbacks.onCreateLoader, id="+id);
        return new CursorLoader(myActivity, Uri.parse("content://uk.me.jandj.bluetrack.db_provider/bluetooth_devices"), new String [] { "name" }, null,null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // loader.getId() would tell us which it was if we cared/had multiple
        Log.d(debug_tag, "Callbacks.onLoadFinished, loader="+loader+" cursor="+cursor);
        theAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(debug_tag, "Callbacks.onLoaderReset, loader="+loader);
        theAdapter.swapCursor(null);
    }
}
