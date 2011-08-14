package pl.jw.mbank.biz;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.request.IAccount;

@Transactional
public class Account extends HibernateDaoSupport implements IAccount {

	@Override
	public List<AccountData> get() {

		Criteria c = getSession().createCriteria(AccountData.class);

		c.addOrder(Order.asc("name"));

		return c.list();
	}

	@Override
	public AccountData get(final Integer pk) {
		return getHibernateTemplate().get(AccountData.class, pk);
	}

	@Override
	@Transactional
	public void update(final AccountData data) {

		getHibernateTemplate().merge(data);
	}

}
