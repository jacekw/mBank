/**
 * 
 */
package pl.jw.mbank.client.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.common.IData;

public class DataGetter {

	private static final DataGetter INSTANCE = new DataGetter();

	private final Map<Class<IData>, RefreshTimer> refreshTimers = new HashMap<Class<IData>, RefreshTimer>();
	private final Map<Class<IData>, IDataUpdater<IData>> httpExecutorsMap = new HashMap<Class<IData>, IDataUpdater<IData>>();
	private final Map<Class<IData>, IDataUpdater<IData>> dbExecutorsMap = new HashMap<Class<IData>, IDataUpdater<IData>>();

	private DataGetter() {
	}

	public <D extends IData> void register(Class<D> key, IDataUpdater<D> httpDataUpdater, IDataUpdater<D> dbDataUpdater) {
		if (httpDataUpdater != null) {
			httpExecutorsMap.put((Class<IData>) key, (IDataUpdater<IData>) httpDataUpdater);
		}

		if (dbDataUpdater != null) {
			dbExecutorsMap.put((Class<IData>) key, (IDataUpdater<IData>) dbDataUpdater);
		}
	}

	public <D extends IData> List<D> get(Class<D> key) throws Exception {
		boolean isRefreshRequired = false;
		if (refreshTimers.get(key) == null) {
			refreshTimers.put((Class<IData>) key, new RefreshTimer(Env.CONTEXT.getConfiguration()
					.getDataRefreshTimeout()));
			isRefreshRequired = true;
		} else {
			isRefreshRequired = refreshTimers.get(key).isTimeout();
		}

		List<D> data = null;

		if (isRefreshRequired && httpExecutorsMap.get(key) != null) {
			data = (List<D>) httpExecutorsMap.get(key).get();
			refreshTimers.get(key).mark();
		} else if (dbExecutorsMap.get(key) != null) {
			data = (List<D>) dbExecutorsMap.get(key).get();
		} else {
			throw new IllegalStateException("Nie zarejestrowano executor-ów.");
		}

		return data;
	}

	public static DataGetter getInstance() {
		return INSTANCE;
	}
}