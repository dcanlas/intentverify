package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *	Initiates the request to authenticate.
 */
public class Solicitor extends BroadcastReceiver{
	
	private final String OUR_PACKAGE_NAME = "good.intentions.proxy";
	private final int TIMEOUT = 10000;
	
	private int authenticationStatus = 0; //the last completed stage of authentication
	private Intent actualIntent = null;
	private final Object authMonitor = new Object();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		switch (authenticationStatus){
			case 0:
				//not needed?
				break;
				
			case 1:
				//now we send the real intent to bouncer to be sent to actual recipient
                context.startActivity(actualIntent);
                
                synchronized (authMonitor){
                	authMonitor.notify();
                }
                
				break;
				
		}
		
	}

    //asks to be authenticated by Bouncer
	public void authenticate(Context context, Intent intent){
		
		actualIntent = intent;
		authenticationStatus = 1;
		
		Intent negotiationIntent = new Intent(intent);
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".className", context.getClass().getName());
		
		context.sendBroadcast(negotiationIntent);
		
		synchronized (authMonitor){
			try {
				authMonitor.wait(TIMEOUT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.v("Solicitor", "Authentication sequence finished or timed out");
	}

}
