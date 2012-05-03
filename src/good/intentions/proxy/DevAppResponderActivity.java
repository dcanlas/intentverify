package good.intentions.proxy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 *	DevAppInitiator's target component.
 */
public class DevAppResponderActivity extends Activity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* Success notification */
        Toast.makeText(this, "HELL'S BELLS!", 0).show();
    }
}
