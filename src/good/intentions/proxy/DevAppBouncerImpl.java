package good.intentions.proxy;

/**
 *	Developer's implementation of Bouncer.
 */
public class DevAppBouncerImpl extends Bouncer {

//	@Override
//	public void onAuthentication(Context context) {
//		
//		/* Start test responder activity */
//		Intent myIntent = new Intent();
//    	myIntent.setClassName("good.intentions.proxy", "DevAppResponderActivity");
//    	context.startActivity(myIntent);
//	}

	@Override
	public void setTrustedPackages() {
		trustedPackages.add("good.intentions.proxy");
		trustedPackages.add("good.intentions.test");
	}

//	@Override
//	public void setDestination() {
//		destination = "DevAppResponderActivity";
//	}

}
