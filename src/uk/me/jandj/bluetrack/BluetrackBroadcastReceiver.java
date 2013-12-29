package uk.me.jandj.bluetrack;
// http://developer.android.com/guide/topics/connectivity/bluetooth.html#FindingDevices
import android.content.*;
import android.bluetooth.*;
import android.util.Log;
import android.database.sqlite.*;
import android.net.*;

public class BluetrackBroadcastReceiver extends BroadcastReceiver {
    private static final String debug_tag = "bluetrack";

    protected SQLiteDatabase blueDB;
    public MainActivity mainactivity;
    
    BluetrackBroadcastReceiver(MainActivity new_mainactivity, SQLiteDatabase new_blueDB) {
        mainactivity = new_mainactivity;
        blueDB = new_blueDB;
    }

    public IntentFilter my_filter() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        // Changed is a misnomer, actually happens when name is initially discovered (too).
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d(debug_tag, "BroadcastReceiver.onReceive: intent="+intent);
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            Log.d(debug_tag, "BroadcastReceiver.onReceive: action_found");
            onReceiveFound(context, intent);
        } else if (action.equals(BluetoothDevice.ACTION_NAME_CHANGED)) {
        Log.d(debug_tag, "BroadcastReceiver.onReceive: name_changed");
            onReceiveNameChanged(context, intent);
        } else {
            // this should only happen if somebody is deliberately screwing with us (or we forgot to add a case to this if when we modified my_filter.
            Log.w(debug_tag, "Unknown action "+action+" in onReceive");
        }
    }

    void onReceiveFound(Context context, Intent intent) {
        // Always contains the extra fields EXTRA_DEVICE and EXTRA_CLASS. Can contain the extra fields EXTRA_NAME and/or EXTRA_RSSI if they are available.
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short)42);
        String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

        // Would query the name if we don't have it here, but it appears that Android will do that for us anyway.

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("mac", device.getAddress());
        if (rssi == 42) {
            // 42 is a sentinel value; it means that we didn't get an rssi.
            // cv.put("rssi", null);
        } else {
            cv.put("rssi", rssi);
        }
        cv.put("last_seen", (byte[])null);
        cv.put("care_about", 0);
        // If we've already heard of this device (same mac string), just
        // ignore (will return the ID of the row we've already seen)
        // FIXME: conflict resolution should update rssi, last_seen.
        Log.d(debug_tag, "BroadcastReceiver.onReceiveFound: Update db: ContentValues="+cv);
        long rowid = blueDB.insertWithOnConflict("bluetooth_devices", null, cv, SQLiteDatabase.CONFLICT_IGNORE);

        context.getContentResolver().notifyChange(Uri.parse("content://uk.me.jandj.bluetrack.db_provider/luetooth_devices"), null);

        // mainactivity.addDetected(device, rssi, name);
    }

    void onReceiveNameChanged(Context context, Intent intent) {
        // Always contains the extra fields EXTRA_DEVICE and EXTRA_NAME.
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

        mainactivity.changeName(device, name);
    }
}
