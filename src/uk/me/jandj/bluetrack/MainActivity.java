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

public class MainActivity extends Activity
{
    BluetoothAdapter adapter;
    ArrayAdapter<String> blueArray; 

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
        blueArray = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        scanView.setAdapter(blueArray);

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


        BluetrackBroadcastReceiver receiver = new BluetrackBroadcastReceiver();
        receiver.mainactivity = this;
        this.registerReceiver(receiver, receiver.my_filter());
        
	}
	
    public void addDetected(BluetoothDevice device, String rssi, String name) {
        blueArray.add(name);
    }

    public void changeName(BluetoothDevice device, String name) {
        // noop for now.
    }
}
