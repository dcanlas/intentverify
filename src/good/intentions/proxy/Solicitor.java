package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *	Initiates the request to authenticate.
 */
public class Solicitor extends BroadcastReceiver{
	
	private int authenticationStatus = 0; //the last completed stage of authentication
	
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		switch (authenticationStatus){
			case 0:
				
				break;
				
			case 1:
				
				break;
				
		}
		
	}
	
	public void authenticate(Intent intent){
		
	}

}
