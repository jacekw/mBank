package pl.jw.mbank.biz;

import java.sql.SQLException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.request.IInvestment;

public class Investment extends HibernateDaoSupport implements IInvestment {

	public Investment() throws SQLException {
		super();
	}

	private void add(final InvestmentData data) throws SQLException {
		HibernateCallback<Void> callback = new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				session.merge(data);
				return null;
			}

		};
		getHibernateTemplate().execute(callback);
	}

	public InvestmentData get(final Integer pk) throws SQLException {
		HibernateCallback<InvestmentData> action = new HibernateCallback<InvestmentData>() {

			@Override
			public InvestmentData doInHibernate(Session session) throws HibernateException, SQLException {

				return (InvestmentData) session.get(InvestmentData.class, pk);
			}

		};

		return getHibernateTemplate().execute(action);
	}

	public void update(final InvestmentData data) throws SQLException {
		HibernateCallback<Void> callback = new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				InvestmentData id = getBySfi(data.getSfi());
				if (id == null) {
					add(data);
				} else {
					id.setInitialvalue(data.getInitialvalue());
					id.setUnits(data.getUnits());
					session.merge(data);
				}
				return null;
			}

		};
		getHibernateTemplate().execute(callback);

	}

	@Override
	public InvestmentData getBySfi(final SfiData sfiData) throws SQLException {
		HibernateCallback<InvestmentData> action = new HibernateCallback<InvestmentData>() {

			@Override
			public InvestmentData doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(InvestmentData.class);
				c.createCriteria("sfi", SfiData.class.getSimpleName()).add(Restrictions.eq("id", sfiData.getId()));
				return (InvestmentData) c.uniqueResult();
			}

		};

		InvestmentData id = getHibernateTemplate().execute(action);

		return id;
	}
}
