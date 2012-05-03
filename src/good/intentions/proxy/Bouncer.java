package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *	Answers authentication requests.
 *	Must be abstract, since the developer needs to specify what to do when authentication succeeds (onAuthentication()).
 */
public abstract class Bouncer extends BroadcastReceiver{
	
	private int authenticationStatus = 0; //the last completed stage of authentication
	
	abstract public void onAuthentication(Context context);
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		switch (authenticationStatus){
			case 0:
				
				break;
				
			case 1:
				
				break;
			
		}
	}

}
