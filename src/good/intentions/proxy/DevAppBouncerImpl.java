package good.intentions.proxy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *	Developer's implementation of Bouncer.
 */
public class DevAppBouncerImpl extends Bouncer {
	
	public void onReceive(Context context, Intent intent) {
		
		Log.v("Bouncer", "Bouncer got something!");
	}

	@Override
	public void onAuthentication(Context context) {
		
		/* Start test responder activity */
		Intent myIntent = new Intent();
    	myIntent.setClassName("good.intentions.proxy", "DevAppResponderActivity");
    	context.startActivity(myIntent);
	}

	@Override
	public void setTrustedPackages() {
		trustedPackages.add("good.intentions.proxy");
		trustedPackages.add("good.intentions.test");
	}

	@Override
	public void setDestination() {
		destination = "DevAppResponderActivity";
	}

}
