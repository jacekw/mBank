package pl.jw.mbank.common.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dataFilter")
public class DataFilter implements Serializable {
	private static final String FILTER_ELEMENT_SEPARATOR = ";";

	private final List<String> filter = new ArrayList<String>();

	public DataFilter(String filter) {
		super();

		if (filter != null && !filter.equals(""))
			this.filter.addAll(Arrays.asList(filter.split(FILTER_ELEMENT_SEPARATOR)));
	}

	public boolean isMatching(String value) {
		boolean isMatching = true;

		if (!filter.isEmpty()) {
			for (String f : filter) {
				isMatching = value.toUpperCase().matches(".*" + f.toUpperCase() + ".*");
				if (isMatching)
					break;
			}
		}

		return isMatching;
	}

	public String getText() {
		StringBuffer sb = new StringBuffer();

		for (String filterElement : filter) {
			sb.append(filterElement);
			sb.append(FILTER_ELEMENT_SEPARATOR);
		}
		return sb.toString();
	}
}
