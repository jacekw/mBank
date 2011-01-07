/**
 * 
 */
package pl.jw.mbank.client.data;

public class RefreshTimer {

	private static long LAST_REFRESH = 0;

	private final long timeout;

	/**
	 * 
	 * @param timeout
	 *            [m]
	 */
	public RefreshTimer(int timeout) {
		this.timeout = timeout * 1000 * 60;
	}

	public void mark() {
		LAST_REFRESH = System.currentTimeMillis();
	}

	public boolean isTimeout() {
		if (System.currentTimeMillis() - LAST_REFRESH > timeout)
			return true;
		else
			return false;
	}

}