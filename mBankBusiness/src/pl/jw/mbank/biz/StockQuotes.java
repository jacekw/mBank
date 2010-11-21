package pl.jw.mbank.biz;

import java.sql.SQLException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pl.jw.mbank.common.dto.StockQuotesData;
import pl.jw.mbank.common.request.IStockQuotes;

public class StockQuotes extends HibernateDaoSupport implements IStockQuotes {

	public StockQuotes() throws SQLException {
		super();
	}

	private void add(final StockQuotesData data) throws SQLException {
		HibernateCallback<Void> action = new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				session.merge(data);
				return null;
			}
		};

		getHibernateTemplate().execute(action);
	}

	public void update(final StockQuotesData data) throws SQLException {
		HibernateCallback<Void> action = new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				StockQuotesData dataBefore = get(data);
				if (dataBefore == null) {
					add(data);
				} else {
					data.setId(dataBefore.getId());
					session.merge(data);
				}
				return null;
			}
		};

		getHibernateTemplate().execute(action);
	}

	@Override
	public StockQuotesData get(final Integer pk) throws SQLException {
		HibernateCallback<StockQuotesData> action = new HibernateCallback<StockQuotesData>() {

			@Override
			public StockQuotesData doInHibernate(Session session) throws HibernateException, SQLException {

				return (StockQuotesData) session.get(StockQuotesData.class, pk);
			}
		};

		return getHibernateTemplate().execute(action);
	}

	@Override
	public StockQuotesData get(final StockQuotesData data) throws SQLException {
		HibernateCallback<StockQuotesData> action = new HibernateCallback<StockQuotesData>() {

			@Override
			public StockQuotesData doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(StockQuotesData.class)
						.add(Restrictions.eq("sfi.id", data.getSfi().getId()))
						.add(Restrictions.eq("date", data.getDate()));
				return (StockQuotesData) c.uniqueResult();
			}
		};

		return getHibernateTemplate().execute(action);
	}
}
