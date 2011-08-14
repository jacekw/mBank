package pl.jw.mbank.biz;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.pk.SfiPk;
import pl.jw.mbank.common.request.ISfi;

@Transactional
public class Sfi extends HibernateDaoSupport implements ISfi {

	public Sfi() {
		super();
	}

	@Override
	public SfiData get(final SfiData pk) {
		SfiData sfiData = null;
		if (pk != null) {
			List<SfiData> sfiDatas = getSession().createCriteria(SfiData.class).add(Restrictions.like("name", pk.getName())).list();
			if (sfiDatas != null && sfiDatas.size() > 0) {
				sfiData = sfiDatas.get(0);
			}
		}

		return sfiData;
	}

	@Override
	public List<SfiData> get() {
		Criteria c = getSession().createCriteria(SfiData.class);

		c.addOrder(Order.asc("name"));

		// Criteria sqC = c.createAlias("stockQuotes",
		// StockQuotesData.class.getSimpleName());
		// Criteria iC = c.createAlias("investment",
		// InvestmentData.class.getSimpleName());
		// sqC.add(Restrictions
		// .sqlRestriction("StockQuotes.date = (SELECT MAX(sq.date) FROM STOCKQUOTES sq WHERE sq.SFI_ID = id)"));

		return c.list();
	}

	@Override
	@Transactional
	public SfiPk add(final SfiData data) {
		int id = ((Long) getSession().createQuery("SELECT COUNT(id) FROM SFI ").list().get(0)).intValue() + 1;

		data.setId(id);

		return new SfiPk((Integer) getHibernateTemplate().save(data));
	}

	@Override
	@Transactional
	public SfiPk update(final SfiData data) {
		SfiPk pk = null;
		SfiData dataSql = get(data);
		if (dataSql == null) {
			pk = add(data);
		} else {
			dataSql.setName(data.getName());
			getHibernateTemplate().merge(dataSql);
			pk = new SfiPk(dataSql.getId());
		}
		return pk;
	}
}
