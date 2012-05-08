package good.intentions.proxy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 *	Contains static methods available to the developer.
 */
public class GIProxy {
	
	private static final String OUR_PACKAGE_NAME = "good.intentions.proxy";

	public static void safeStartActivity(Context context, Intent intent){
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Intent startSolicitorIntent = new Intent(context, DevAppSolicitorImpl.class);
		startSolicitorIntent.putExtra("actualIntent", intent);
		context.startService(startSolicitorIntent);
		authenticate(context, intent);
	}
	
	private static void authenticate(Context context, Intent intent){
		
		Intent actualIntent = intent;
		
		Intent negotiationIntent = new Intent(intent);
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
		
		String ClassName = context.getClass().getName();
		String SolicitorClassName = ClassName.replaceFirst("\\.[^\\.]*$", ".DevAppSolicitorImpl");
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".className", SolicitorClassName);
		
		ComponentName destinationComponent = actualIntent.getComponent();
		String bouncerClassName = destinationComponent.getClassName().replaceFirst("\\.[^\\.]*$", ".DevAppBouncerImpl");
		ComponentName bouncerComponent = new ComponentName(destinationComponent.getPackageName(), bouncerClassName);
		negotiationIntent.setComponent(bouncerComponent);
		
		context.startService(negotiationIntent);
	}
}
