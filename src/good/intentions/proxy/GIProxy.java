package good.intentions.proxy;

import android.content.Context;
import android.content.Intent;

/**
 *	Contains static methods available to the developer.
 */
public class GIProxy {
	
	private static final String OUR_PACKAGE_NAME = "good.intentions.proxy";

	public static void safeStartActivity(Context context, Intent intent){
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Intent startSolicitorIntent = new Intent(context, DevAppSolicitorImpl.class);
		
		startSolicitorIntent.putExtra("actualIntent", intent);
		
		startSolicitorIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
		
		String solicitorClassName = context.getClass().getName().replaceFirst("\\.[^\\.]*$", ".DevAppSolicitorImpl");
		startSolicitorIntent.putExtra(OUR_PACKAGE_NAME + ".className", solicitorClassName);
		
		context.startService(startSolicitorIntent);
	}
}