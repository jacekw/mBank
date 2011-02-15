/**
 * 
 */
package pl.jw.mbank.client.data;

import java.util.List;

import pl.jw.mbank.common.IData;

public interface IDataSetCallbackHandler<D extends IData> {
	void setData(List<D> data);

	void finished();

	void started();

	void failed(Throwable cause);
}