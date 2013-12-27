package uk.me.jandj.bluetrack;

import android.app.*;
import android.os.*;
import android.widget.ListView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.*;

public class MainActivity extends Activity
{
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
		
    }
	
	protected void startScan() {
		
	}
	
}
