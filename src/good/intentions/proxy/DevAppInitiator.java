package good.intentions.proxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *	Test dev app for calling Solicitor code.
 */
public class DevAppInitiator extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent intent = new Intent();
        intent.setClassName("good.intentions.proxy", "good.intentions.proxy.DevAppBouncerImpl");
        
        GIProxy.safeStartActivity(this, intent);
    }
}
