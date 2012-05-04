package good.intentions.proxy;

import java.util.Arrays;

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
	
	private int authenticationStatus = 0; //the last completed stage of authentication
	private SecureRandom rng = new SecureRandom();
	private String ourPackageName = "good.intentions.proxy";
	private byte[] key; //This may need to become an associative array or something.
	protected String[] trustedPackages; //the dev should override this. 
				//Each element should be of the form "com.example.package"
	
	abstract public void onAuthentication(Context context);
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		switch (authenticationStatus){
			case 0: //the initial request
				String packageName = intent.getStringExtra(ourPackageName + ".packageName");
				String className = intent.getStringExtra(ourPackageName + ".className"); //This should refer to the Solicitor
				if (checkOrigin(packageName)) {
					Log.v("Bouncer","Origin ok");
					sendKey(packageName, className, context);
					authenticationStatus = 1;
				}
				else {
					Log.v("Bouncer", "Origin rejected");
				}
				break;
				
			case 1:
				
				break;
			
		}
	}
	private boolean checkOrigin(String packageName) {
		return Arrays.asList(trustedPackages).contains(packageName);
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
		i.putExtra(ourPackageName+".key", key);
		context.sendBroadcast(i);
	}
}
