package pl.jw.mbank.biz;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import pl.jw.mbank.common.dto.StockQuotesData;
import pl.jw.mbank.common.request.IStockQuotes;

@Transactional
public class StockQuotes extends HibernateDaoSupport implements IStockQuotes {

	public StockQuotes() {
		super();
	}

	private void add(final StockQuotesData data) {
		getHibernateTemplate().merge(data);
	}

	@Override
	@Transactional
	public void update(final StockQuotesData data) {
		StockQuotesData dataBefore = get(data);
		if (dataBefore == null) {
			add(data);
		} else {
			data.setId(dataBefore.getId());
			getHibernateTemplate().merge(data);
		}
	}

	@Override
	public StockQuotesData get(final Integer pk) {
		return getHibernateTemplate().get(StockQuotesData.class, pk);

	}

	@Override
	public StockQuotesData get(final StockQuotesData data) {
		Criteria c = getSession().createCriteria(StockQuotesData.class).add(Restrictions.eq("sfi.id", data.getSfi().getId())).add(Restrictions.eq("date", data.getDate()));
		return (StockQuotesData) c.uniqueResult();

	}
}
