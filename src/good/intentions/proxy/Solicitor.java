package good.intentions.proxy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.util.Log;
import android.os.*;
 
/**
 *	Initiates the request to authenticate.
 */
public class Solicitor extends Service {
	
	private final String OUR_PACKAGE_NAME = "good.intentions.proxy";
	private final int TIMEOUT = 10000;
	
	private volatile boolean authenticationStarted = false; //the last completed stage of authentication
	private volatile Intent actualIntent = null;
	private ComponentName destinationComponent = null;
	private ComponentName bouncerComponent = null;
	private final Object authMonitor = new Object();
	private Messenger mService = null;
	private boolean mBound = false;
	private Intent negotiationIntent = null;
	
	//defines behavior for initial binding
//	private ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            mService = new Messenger(service);
//            mBound = true;
//            Message msg = Message.obtain();
//            Bundle bundle = new Bundle();
//            bundle.putString(OUR_PACKAGE_NAME + ".packageName", Context.getApplicationContext().getPackageName());
//            msg.setData(bundle);
//            msg.replyTo = mService;
//            mService.send(msg);
//        }
//        public void onServiceDisconnected(ComponentName className) {
//            mService = null;
//            mBound = false;
//        }
//    };
	
    //Step 1: asks to be authenticated by Bouncer
	public void authenticate(Context context, Intent intent){
		
		actualIntent = intent;
		//authenticationStarted = true;
		
		negotiationIntent = new Intent(intent);
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
		
		String ClassName = context.getClass().getName();
		String SolicitorClassName = ClassName.replaceFirst("\\.[^\\.]*$", ".DevAppSolicitorImpl");
		negotiationIntent.putExtra(OUR_PACKAGE_NAME + ".className", SolicitorClassName);
		
		destinationComponent = actualIntent.getComponent();
		String bouncerClassName = destinationComponent.getClassName().replaceFirst("\\.[^\\.]*$", ".DevAppBouncerImpl");
		bouncerComponent = new ComponentName(destinationComponent.getPackageName(), bouncerClassName);
		negotiationIntent.setComponent(bouncerComponent);
		
		context.startService(negotiationIntent);
	}
	
	public void setActualIntent(Intent intent){
		actualIntent = intent;
	}
	
	//Step 3: send msg with key and original intent.
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {	
			byte[] key = msg.getData().getByteArray(OUR_PACKAGE_NAME+".key");
			if (key == null){
				return;
			}
			Message msg2 = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putByteArray(OUR_PACKAGE_NAME + ".key", key);
            bundle.putParcelable(OUR_PACKAGE_NAME + ".intent", actualIntent);
            msg2.setData(bundle);
            try {
				msg.replyTo.send(msg2);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	final Messenger mMessenger = new Messenger(new IncomingHandler());		
	
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
	

		
//		if (authenticationStarted){	
//			byte[] key = intent.getByteArrayExtra(OUR_PACKAGE_NAME+".key");
//			//now we send the real intent to bouncer to be sent to actual recipient
//            actualIntent.putExtra(OUR_PACKAGE_NAME + ".packageName", context.getPackageName());
//            actualIntent.putExtra(OUR_PACKAGE_NAME + ".className", context.getClass().getName());
//            actualIntent.putExtra(OUR_PACKAGE_NAME+".key", key);
//            context.sendBroadcast(actualIntent);
//            
//            synchronized (authMonitor){
//            	authMonitor.notify();
//            }
//		}
		
	/*	
		synchronized (authMonitor){
			try {
				authMonitor.wait(TIMEOUT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.v("Solicitor", "Authentication sequence finished or timed out");
	*/
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		actualIntent = intent.getParcelableExtra("actualIntent");
		return START_STICKY;
	}


}
