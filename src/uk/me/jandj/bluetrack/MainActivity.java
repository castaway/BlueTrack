package uk.me.jandj.bluetrack;

import android.app.*;
import android.os.*;
import android.widget.ListView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.*;
import android.bluetooth.*;
import android.widget.*;
import uk.me.jandj.bluetrack.*;
import android.database.*;
import android.content.*;
import android.util.Log;

public class MainActivity extends Activity
{
    private static final String debug_tag = "bluetrack";
    BluetoothAdapter adapter;
    DeviceDatabase blueDBHelper;
    LoaderManager.LoaderCallbacks<Cursor> dbCallbacks;

    // activityforresult request codes:
    int REQUEST_ENABLE_BT = 100;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Button scanButton = (Button)findViewById(R.id.scan_button);
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				startScan();
			}
		});
		
		ListView scanView = (ListView)findViewById(R.id.scan_list);
		scanView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        blueDBHelper = new DeviceDatabase(this);
        SimpleCursorAdapter scanAdapter = new SimpleCursorAdapter(this,
                                                    android.R.layout.simple_list_item_multiple_choice,
                                                    null,
                                                    new String [] { "name" },
                                                    new int [] { android.R.id.text1 },
                                                    0
                                                                  );

        scanView.setAdapter(scanAdapter);

        dbCallbacks = new DeviceDatabaseCallbacks(this, scanAdapter);
        LoaderManager lm = getLoaderManager();
        lm.initLoader(R.id.device_db_loader, null, dbCallbacks);

    }
    
    public void onStart() {
        super.onStart();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
            Log.d(debug_tag, "MainActivity:onActivityResult: Got result of asking to turn on bluetooth");
            // Assuming said adapter is still here:
            actualStartScan();
        } else {
            Log.d(debug_tag, "MainActivity:onActivityResult: Some activity result we weren't expecting happened! requestCode="+requestCode);
        }
    }
	
	protected void startScan() {
        // FIXME: This stuff should probably be moved out to the service?
        // FIXME: Can return null if bluetooth not supported by hw (but manafest should keep us from being installed on such hw).
        adapter = BluetoothAdapter.getDefaultAdapter();
        
        if(adapter == null) {
            Log.d(debug_tag, "MainActivity.startScan: This device doesn't support bluetooth!");
        }

        if (!adapter.isEnabled()) {
            Log.d(debug_tag, "MainActivity: startScan: Bluetooth not enabled, asking user to fix that");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            return;
        }

        actualStartScan();
	}

    protected void actualStartScan() {
        if(adapter == null) {
            Log.d(debug_tag, "MainActivity:actualStartScan: Someone tried to start an actual scan before setting up the Bluetooth adapter, silly developer");
            return;
        }

        // Can return null if not on, but that should have been handled by isEnabled.
        adapter.startDiscovery();

        BluetrackBroadcastReceiver receiver = new BluetrackBroadcastReceiver(this, blueDBHelper.getWritableDatabase());
        this.registerReceiver(receiver, receiver.my_filter());
    }

    //public void updateScanView() {
	//	ListView scanView = (ListView)findViewById(R.id.scan_list);
    //}
	
    public void addDetected(BluetoothDevice device, String rssi, String name) {
        //blueArray.add("name: "+name+", addr: "+device.getAddress()+", rssi: ", rssi);
    }

    public void changeName(BluetoothDevice device, String name) {
        //blueArray.add("fixme, changename, name: "+name+", addr: "+device.getAddress());
    }
}
