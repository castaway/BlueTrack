package uk.me.jandj.bluetrack;

import android.app.*;
import android.content.*;
import android.database.*;
import android.widget.*;
import android.os.*;
import android.net.*;


// http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
public class DeviceDatabaseCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    // Hoops!
    Context myActivity;
    SimpleCursorAdapter theAdapter;
    DeviceDatabaseCallbacks(Context context, SimpleCursorAdapter adapter) {
        myActivity = context;
        theAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(myActivity, Uri.parse("content://uk.me.jandj.bluetrack.db_provider/luetooth_devices"), new String [] { "name" }, null,null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // loader.getId() would tell us which it was if we cared/had multiple
        theAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        theAdapter.swapCursor(null);
    }
}
