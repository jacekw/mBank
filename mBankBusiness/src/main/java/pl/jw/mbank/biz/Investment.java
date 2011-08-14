package pl.jw.mbank.biz;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.request.IInvestment;

@Transactional
public class Investment extends HibernateDaoSupport implements IInvestment {

	public Investment() {
		super();
	}

	private void add(final InvestmentData data) {

		getHibernateTemplate().merge(data);
	}

	@Override
	public InvestmentData get(final Integer pk) {

		return getHibernateTemplate().get(InvestmentData.class, pk);

	}

	@Override
	@Transactional
	public void update(final InvestmentData data) {
		InvestmentData id = getBySfi(data.getAccount(), data.getSfi());
		if (id == null) {
			add(data);
		} else {
			id.setInitialvalue(data.getInitialvalue());
			id.setUnits(data.getUnits());
			getHibernateTemplate().merge(data);
		}
	}

	@Override
	public InvestmentData getBySfi(final AccountData accountData, final SfiData sfiData) {

		Criteria c = getSession().createCriteria(InvestmentData.class);
		c.createCriteria("sfi", SfiData.class.getSimpleName()).add(Restrictions.eq("id", sfiData.getId()));
		c.createCriteria("account", AccountData.class.getSimpleName()).add(Restrictions.eq("id", accountData.getId()));
		return (InvestmentData) c.uniqueResult();
	}

}
