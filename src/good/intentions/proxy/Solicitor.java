package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *	Initiates the request to authenticate.
 */
public class Solicitor extends BroadcastReceiver{
	
	private int authenticationStatus = 0; //the last completed stage of authentication
	private Intent actualIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		switch (authenticationStatus){
			case 0:
				//actualIntent received here and saved.
				break;
				
			case 1:
				//now we send the real intent to bouncer to be sent to actual recipient
                context.sendBroadcast(actualIntent);
				break;
				
		}
		
	}

    //asks to be authenticated by Bouncer
	public void authenticate(Intent intent){
		
	}

}
