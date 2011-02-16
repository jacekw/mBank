package pl.jw.mbank.biz;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.request.IAccount;

public class Account extends HibernateDaoSupport implements IAccount {

	public List<AccountData> get() throws SQLException {
		HibernateCallback<List<AccountData>> action = new HibernateCallback<List<AccountData>>() {

			@Override
			public List<AccountData> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(AccountData.class);

				c.addOrder(Order.asc("name"));

				return c.list();
			}

		};

		return getHibernateTemplate().execute(action);
	}

	public AccountData get(final Integer pk) throws SQLException {
		HibernateCallback<AccountData> action = new HibernateCallback<AccountData>() {

			@Override
			public AccountData doInHibernate(Session session) throws HibernateException, SQLException {

				return (AccountData) session.get(AccountData.class, pk);
			}

		};

		return getHibernateTemplate().execute(action);
	}

	public void update(final AccountData data) throws SQLException {
		HibernateCallback<Void> callback = new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {

				session.merge(data);

				session.flush();

				return null;
			}

		};
		getHibernateTemplate().execute(callback);

	}

}
