package pl.jw.mbank.client.config;

import java.io.Serializable;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.common.filter.DataFilter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("configuration")
public class Configuration implements Serializable {

	/** [m] */
	private final int dataRefreshTimeout = Env.DEFAULT_DATA_REFRESH_TIMEOUT;

	private DataFilter dataFilter = new DataFilter("russ.*eq;turk;hsbc.*bric eq");

	public Configuration() {

	}

	public DataFilter getDataFilter() {
		return dataFilter;
	}

	public void setDataFilter(DataFilter dataFilter) {
		this.dataFilter = dataFilter;
	}

	/**
	 * [m]
	 * 
	 * @return
	 */
	public int getDataRefreshTimeout() {
		return dataRefreshTimeout;
	}

}
