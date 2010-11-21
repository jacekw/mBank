package pl.jw.mbank.biz.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;

public class DbConfiguration extends AnnotationConfiguration {
	private static Log log = LogFactory.getLog(DbConfiguration.class);

	@Override
	protected void reset() {
		super.reset();

		addEntities();
	}

	private void addEntities() {
		log.debug("Table definitions loading start.");

		List<Class<? extends Serializable>> tables = new ArrayList<Class<? extends Serializable>>();

		tables.add(SfiData.class);
		tables.add(StockQuotesData.class);
		tables.add(InvestmentData.class);

		for (Class<? extends Serializable> data : tables) {
			log.debug("Table definition loading: " + data.getSimpleName());
			addAnnotatedClass(data);
		}
		log.debug("Table definitions loading done.");
	}

}
