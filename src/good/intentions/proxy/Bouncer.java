package good.intentions.proxy;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.security.SecureRandom;
/**
 *	Answers authentication requests.
 *	Must be abstract, since the developer needs to specify what to do when authentication succeeds (onAuthentication()).
 */
public abstract class Bouncer extends BroadcastReceiver{
	
	private final String OUR_PACKAGE_NAME = "good.intentions.proxy";
	
	private boolean authenticationKeySent = false; //the last completed stage of authentication
	private boolean initialized = false;
	private Object initializeMonitor = new Object();
	private SecureRandom rng = new SecureRandom();
	private byte[] key; //This may need to become an associative array or something.
	protected ArrayList<String> trustedPackages = null; //the dev should override this. 
				//Each element should be of the form "com.example.package"
	protected String destination = null;
	
	abstract public void onAuthentication(Context context);
	
	abstract public void setTrustedPackages();
	
	abstract public void setDestination();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.v("Bouncer", "Bouncer got something!");
		
		synchronized (initializeMonitor) {
			if (!initialized){
				setTrustedPackages();
				setDestination();
				initialized = true;
			}
		}
		
		if (!authenticationKeySent){
			//the initial request
				String packageName = intent.getStringExtra(OUR_PACKAGE_NAME + ".packageName");
				String className = intent.getStringExtra(OUR_PACKAGE_NAME + ".className"); //This should refer to the Solicitor
				if (checkOrigin(packageName)) {
					Log.v("Bouncer","Origin ok");
					sendKey(packageName, className, context);
					authenticationKeySent = true;
				}
				else {
					Log.v("Bouncer", "Origin rejected");
				}
		}
		else {
			//receives the original intent. now forward it.
			byte[] receivedKey = intent.getByteArrayExtra(OUR_PACKAGE_NAME+".key");
            if (receivedKey == key) {
			    context.startActivity(intent);
            }
		}
	}
	
	private boolean checkOrigin(String packageName) {
		return trustedPackages.contains(packageName);
	}
	
	private byte[] genKey() {
		byte[] bytes = new byte[20];
		rng.nextBytes(bytes);
		return bytes;
	}
	
	private void sendKey(String packageName, String className, Context context) {
		Intent i = new Intent();
		i.setClassName(packageName, className);
		key = genKey();
		Log.v("Bouncer", "Sending key " + key + " to "+packageName+", "+className);
		i.putExtra(OUR_PACKAGE_NAME+".key", key);
		context.sendBroadcast(i);
	}
}
