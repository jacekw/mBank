package pl.jw.mbank.biz;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;
import pl.jw.mbank.common.dto.pk.SfiPk;
import pl.jw.mbank.common.request.ISfi;

public class Sfi extends HibernateDaoSupport implements ISfi {

	public Sfi() throws SQLException {
		super();
	}

	public SfiData get(final SfiData pk) throws SQLException {
		HibernateCallback<SfiData> action = new HibernateCallback<SfiData>() {

			@Override
			public SfiData doInHibernate(Session session) throws HibernateException, SQLException {
				SfiData sfiData = null;
				if (pk != null) {
					List<SfiData> sfiDatas = session.createCriteria(SfiData.class)
							.add(Restrictions.like("name", pk.getName())).list();
					if (sfiDatas != null && sfiDatas.size() > 0)
						sfiData = sfiDatas.get(0);
				}

				return sfiData;
			}
		};

		return getHibernateTemplate().execute(action);
	}

	public List<SfiData> get() throws SQLException {
		HibernateCallback<List<SfiData>> action = new HibernateCallback<List<SfiData>>() {

			@Override
			public List<SfiData> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(SfiData.class);

				c.addOrder(Order.asc("name"));

				Criteria sqC = c.createAlias("stockQuotes", StockQuotesData.class.getSimpleName());
				Criteria iC = c.createAlias("investment", InvestmentData.class.getSimpleName());
				//				sqC.add(Restrictions
				//						.sqlRestriction("StockQuotes.date = (SELECT MAX(sq.date) FROM STOCKQUOTES sq WHERE sq.SFI_ID = id)"));

				return c.list();
			}
		};

		return getHibernateTemplate().execute(action);
	}

	public SfiPk add(final SfiData data) throws SQLException {
		HibernateCallback<SfiPk> action = new HibernateCallback<SfiPk>() {

			@Override
			public SfiPk doInHibernate(Session session) throws HibernateException, SQLException {
				int id = ((Long) session.createQuery("SELECT COUNT(id) FROM SFI ").list().get(0)).intValue() + 1;

				data.setId(id);

				return new SfiPk((Integer) session.save(data));
			}
		};

		return getHibernateTemplate().execute(action);
	}

	public SfiPk update(final SfiData data) throws SQLException {
		HibernateCallback<SfiPk> action = new HibernateCallback<SfiPk>() {
			@Override
			public SfiPk doInHibernate(Session session) throws HibernateException, SQLException {

				SfiPk pk = null;
				SfiData dataSql = get(data);
				if (dataSql == null) {
					pk = add(data);
				} else {
					dataSql.setName(data.getName());
					session.merge(dataSql);
					pk = new SfiPk(dataSql.getId());
				}
				return pk;
			}
		};

		return getHibernateTemplate().execute(action);

	}
}
