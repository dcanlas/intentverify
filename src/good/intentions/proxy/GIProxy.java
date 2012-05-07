package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 *	Contains static methods available to the developer.
 */
public class GIProxy {
	
	public static void safeStartActivity(Context context, Intent intent){
		
		Solicitor solicitor = new DevAppSolicitorImpl();
		solicitor.authenticate(context, intent);
	}
}
