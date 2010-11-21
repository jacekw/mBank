package pl.jw.mbank.client.data;

import java.util.List;

import pl.jw.mbank.common.IData;

public abstract class DataUpdaterAdapter<D extends IData> implements IDataUpdater<D> {

	public void additionalAction(List<D> data) {
	};
}
