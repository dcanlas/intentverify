package good.intentions.proxy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.util.Log;
import android.os.*;
 
/**
 *	Initiates the request to authenticate.
 */
public class Solicitor extends Service {
	
	private final String OUR_PACKAGE_NAME = "good.intentions.proxy";
	private final int TIMEOUT = 10000;
	
	private volatile Intent actualIntent = null;
	private ComponentName destinationComponent = null;
	private ComponentName bouncerComponent = null;
	
    //Step 1: asks to be authenticated by Bouncer
	public void authenticate(Context context, Intent negotiationIntent){
		
		destinationComponent = actualIntent.getComponent();
		String bouncerClassName = destinationComponent.getClassName().replaceFirst("\\.[^\\.]*$", ".DevAppBouncerImpl");
		bouncerComponent = new ComponentName(destinationComponent.getPackageName(), bouncerClassName);
		negotiationIntent.setComponent(bouncerComponent);
		
		context.startService(negotiationIntent);
	}
	
	//Step 3: send msg with key and original intent.
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {	
			byte[] key = msg.getData().getByteArray(OUR_PACKAGE_NAME+".key");
			if (key == null){
				return;
			}
			Message replyMsg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putByteArray(OUR_PACKAGE_NAME + ".key", key);
            bundle.putParcelable(OUR_PACKAGE_NAME + ".intent", actualIntent);
            replyMsg.setData(bundle);
            try {
				msg.replyTo.send(replyMsg);
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
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		
		actualIntent = intent.getParcelableExtra("actualIntent");
		intent.removeExtra("actualIntent");
		authenticate(this, intent);
		return START_STICKY;
	}
}
