package pl.jw.mbank.biz;

import java.math.BigDecimal;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.jw.mbank.biz.common.MBankTest;
import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.request.IInvestment;
import pl.jw.mbank.common.request.ISfi;

public class InvestmentTest extends MBankTest {

	@Autowired
	protected ISfi sfiDao;
	@Autowired
	protected IInvestment investmentDao;

	public SfiData getSfiData() throws SQLException {
		SfiData sfiData = new SfiData();
		sfiData.setName("hsbc");
		sfiDao.add(sfiData);

		return sfiData;
	}

	private InvestmentData addInvestmentData() throws SQLException {
		InvestmentData id = new InvestmentData();
		id.setSfi(getSfiData());
		id.setInitialvalue(new BigDecimal(100));
		id.setUnits(new BigDecimal(4545));
		id.setSimulation(false);

		investmentDao.update(id);

		sessionFactory.getCurrentSession().flush();

		return id;
	}

	@Test
	public void add() throws SQLException {
		InvestmentData id = addInvestmentData();

		Assert.assertNotSame(0, id.getId());

	}

	@Test
	public void update() throws SQLException {
		InvestmentData id = addInvestmentData();
		id.setInitialvalue(new BigDecimal(222));
		id.setUnits(new BigDecimal(6666));

		investmentDao.update(id);

		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void getBySfi() throws SQLException {

		InvestmentData id = addInvestmentData();
		id.setInitialvalue(new BigDecimal(222));
		id.setUnits(new BigDecimal(6666));

		investmentDao.update(id);

		sessionFactory.getCurrentSession().flush();

		Assert.assertNotNull(investmentDao.getBySfi(id.getSfi()));
	}
}
