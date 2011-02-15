/**
 * 
 */
package pl.jw.mbank.client.data;

import java.util.List;

import pl.jw.mbank.common.IData;

public interface IDataUpdater<D extends IData> {
	public List<D> get() throws Exception;

}