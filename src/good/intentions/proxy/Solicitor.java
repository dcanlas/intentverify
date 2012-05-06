package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.util.Log;
 
/**
 *	Initiates the request to authenticate.
 */
public class Solicitor extends BroadcastReceiver{
	
	private final String OUR_PACKAGE_NAME = "good.intentions.proxy";
	private final int TIMEOUT = 10000;
	
	private volatile boolean authenticationStarted = false; //the last completed stage of authentication
	private Intent actualIntent = null;
	private ComponentName destinationComponent = null;
	private ComponentName bouncerComponent = null;
	private final Object authMonitor = new Object();

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (authenticationStarted){
			
			byte[] key = intent.getByteArrayExtra(OUR_PACKAGE_NAME+".key");
			//now we send the real intent to bouncer to be sent to actual recipient
            actualIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
            actualIntent.putExtra(OUR_PACKAGE_NAME + ".className", context.getClass().getName());
            actualIntent.putExtra(OUR_PACKAGE_NAME+".key", key);
            context.sendBroadcast(actualIntent);
            
            synchronized (authMonitor){
            	authMonitor.notify();
            }
		}
		
	}

    //asks to be authenticated by Bouncer
	public void authenticate(Context context, Intent intent){
		
		actualIntent = intent;
		authenticationStarted = true;
		Intent negotiationIntent = new Intent(intent);
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".className", context.getClass().getName());
		destinationComponent = actualIntent.getComponent();
		String bouncerClassName = destinationComponent.getClassName().replaceFirst("\\.[^\\.]*$", ".MyBouncer");
		bouncerComponent = new ComponentName(destinationComponent.getPackageName(), bouncerClassName);
		negotiationIntent.setComponent(bouncerComponent);

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
