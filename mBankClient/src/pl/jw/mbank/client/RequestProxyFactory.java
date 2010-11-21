package pl.jw.mbank.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.jw.mbank.common.request.IRequest;

public class RequestProxyFactory {

	public interface IRequestProxy {
		public <I extends IRequest> I requestData(Class<I> iFace) throws Exception;
	}

	private static abstract class BaseRequestProxy implements IRequestProxy {
		private static Log log = LogFactory.getLog(DirectRequestProxy.class);

		@Override
		public <I extends IRequest> I requestData(Class<I> iFace) throws Exception {
			if (iFace == null) {
				throw new IllegalArgumentException("Request interface not initialised.");
			}

			log.debug("Requester: " + this.getClass().getName());

			return null;
		}

	}

	private static class DirectRequestProxy extends BaseRequestProxy {
		private static Log log = LogFactory.getLog(DirectRequestProxy.class);

		@Override
		public <I extends IRequest> I requestData(Class<I> iFace) throws Exception {
			super.requestData(iFace);

			String name = iFace.getSimpleName();
			name = name.substring(1);

			name = "pl.jw.mbank.biz." + name;

			log.debug("Request: \"" + name + "\"");

			Class<I> biz = (Class<I>) Class.forName(name);

			IRequest req = null;

			req = biz.newInstance();

			return (I) req;
		}
	}

	private static class SpringRequestProxy extends BaseRequestProxy {
		private static Log log = LogFactory.getLog(SpringRequestProxy.class);

		@Override
		public <I extends IRequest> I requestData(Class<I> iFace) throws Exception {
			super.requestData(iFace);

			String name = iFace.getSimpleName();
			name = name.substring(1, 2).toLowerCase() + name.substring(2) + "Dao";

			log.debug("Request: \"" + name + "\"");

			return (I) Env.getSpringContext().getBean(name);
		}
	}

	public static IRequestProxy getInstance() {
		return new SpringRequestProxy();
	}

}
