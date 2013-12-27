package uk.me.jandj.bluetrack;
// http://developer.android.com/guide/topics/connectivity/bluetooth.html#FindingDevices
public class BluetrackBroadcastReceiver extends BroadcastReceiver {
    public MainActivity mainactivity;
    
    public IntentFilter my_filter() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        // Changed is a misnomer, actually happens when name is initially discovered (too).
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        
        return filter;
    }

    @override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(BluetothDevice.ACTION_FOUND)) {
            onReceiveFound(context, intent);
        } else if (action.equals(BluetoothDevice.ACTION_NAME_CHANGED)) {
            onReceiveNameChanged(context, intent);
        } else {
            // this should only happen if somebody is deliberately screwing with us (or we forgot to add a case to this if when we modified my_filter.
            throw new Exception("Unknown action "+action+" in onReceive");
        }
    }

    void onReceiveFound(context, intent) {
        // Always contains the extra fields EXTRA_DEVICE and EXTRA_CLASS. Can contain the extra fields EXTRA_NAME and/or EXTRA_RSSI if they are available.
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String rssi = intent.getStringExtra(BluetoothDevice.EXTRA_RSSI);
        String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

        // Would query the name if we don't have it here, but it appears that Android will do that for us anyway.

        mainactivity.addDetected(device, rssi, name);
    }

    void onReceiveNameChanged(context, intent) {
        // Always contains the extra fields EXTRA_DEVICE and EXTRA_NAME.
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

        mainactivity.changeName(device, name);
    }
}
