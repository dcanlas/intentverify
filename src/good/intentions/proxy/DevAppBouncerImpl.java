package good.intentions.proxy;

/**
 *	Developer's implementation of Bouncer.
 */
public class DevAppBouncerImpl extends Bouncer {

	@Override
	public void setTrustedPackages() {
		trustedPackages.add("good.intentions.proxy");
		trustedPackages.add("good.intentions.test");
	}
}
