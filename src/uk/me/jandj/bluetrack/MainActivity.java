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

public class MainActivity extends Activity
{
    BluetoothAdapter adapter;
    DeviceDatabase blueDBHelper;
    LoaderManager.LoaderCallbacks<Cursor> dbCallbacks;

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
        //        Cursor scanViewCursor = blueDBHelper.getReadableDatabase()
        //            .rawQuery("SELECT _id, name FROM bluetooth_devices ORDER BY mac", null);
        SimpleCursorAdapter scanAdapter = new SimpleCursorAdapter(this,
                                                    android.R.layout.simple_list_item_multiple_choice,
                                                    // scanViewCursor,
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
	
	protected void startScan() {
        // FIXME: This stuff should probably be moved out to the service?
        // FIXME: Can return null if bluetooth not supported by hw (but manafest should keep us from being installed on such hw).
        adapter = BluetoothAdapter.getDefaultAdapter();
        
        if (!adapter.isEnabled()) {
            // FIXME: Ask user to enable bluetooth.
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
