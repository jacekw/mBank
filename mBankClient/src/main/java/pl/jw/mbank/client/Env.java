package pl.jw.mbank.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.jw.mbank.client.data.ITableDataRefreshCallbackHanlder;
import pl.jw.mbank.common.request.IRequest;

public class Env {

	private static Log log = LogFactory.getLog(Env.class);

	public static final String URL = "http://www.mbank.pl/indywidualny/inwestycje/sfi/notowania/tab3.pl";

	static final String CONFIG_FILE = "config.xml";

	private static ApplicationContext SPRING_CONTEXT;

	public static Context CONTEXT = new Context();

	/** [m] */
	public static final int DEFAULT_DATA_REFRESH_TIMEOUT = 60;

	public static ApplicationContext getSpringContext() {
		if (SPRING_CONTEXT == null) {
			SPRING_CONTEXT = new ClassPathXmlApplicationContext(new String[] { "spring-hibernate.xml" });
		}

		return SPRING_CONTEXT;
	}

	public static <I extends IRequest> I requestData(Class<I> iFace) throws Exception {
		return RequestProxyFactory.getInstance().requestData(iFace);
	}

	/**
	 * Zapewnia wywo³ywanie ¿¹dania refresh-a co okreœlony czas.
	 * 
	 * Wspó³istnieje z mechanizmem weryfikacji aktualnoœci danych korzystaj¹cych
	 * z tej samej wartoci timeout-a.
	 * 
	 * @param callbackHanlder
	 * @return
	 */
	public static Runnable scheduleCyclicRefresh(final ITableDataRefreshCallbackHanlder callbackHanlder) {
		Thread timerThread = new Thread() {

			@Override
			public void run() {
				int dataTimeOut = Env.CONTEXT.getConfiguration().getDataRefreshTimeout();

				setName("ScheduledCyclicRefreashThread [" + dataTimeOut + " [m],"
						+ callbackHanlder.getClass().getSimpleName());

				while (!Thread.currentThread().isInterrupted() && Thread.currentThread().isAlive()) {
					log.debug("scheduleCyclicRefresh: " + dataTimeOut + " [m],"
							+ callbackHanlder.getClass().getSimpleName());
					callbackHanlder.refresh();

					try {
						Thread.currentThread().sleep(dataTimeOut * 1000 * 60);
					} catch (InterruptedException e) {
						;
					}
				}

			}
		};
		timerThread.start();

		return timerThread;
	}

}
