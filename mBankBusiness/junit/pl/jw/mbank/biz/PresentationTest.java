package pl.jw.mbank.biz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.jw.mbank.biz.common.MBankTest;
import pl.jw.mbank.common.dto.InvstmentDirectionsData;
import pl.jw.mbank.common.dto.PresentationGraphData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;
import pl.jw.mbank.common.request.IPresentation;
import pl.jw.mbank.common.request.ISfi;
import pl.jw.mbank.common.request.IStockQuotes;

public class PresentationTest extends MBankTest {

	@Autowired
	protected ISfi sfiDao;
	@Autowired
	protected IStockQuotes stockQuotesDao;

	@Autowired
	protected IPresentation presentationDao;

	private SfiData addStockQuotes() throws SQLException {
		SfiData sfiData = new SfiData();
		sfiData.setName("hsbc");
		sfiDao.add(sfiData);

		StockQuotesData data = new StockQuotesData();
		data.setDate(new Date(System.currentTimeMillis()));
		data.setDelta(BigDecimal.TEN);
		data.setValue(BigDecimal.TEN.multiply(BigDecimal.TEN));
		data.setSfi(sfiData);
		stockQuotesDao.update(data);

		sessionFactory.getCurrentSession().flush();

		return sfiData;
	}

	@Test
	public void getGraphData() throws SQLException {
		SfiData sfiData = addStockQuotes();

		List<PresentationGraphData> investDirections = presentationDao.getGraphData(sfiData);

		Assert.assertNotNull("Oczekiwana lista z 1 obiektem.", investDirections);
		Assert.assertEquals("Oczekiwana lista z 1 obiektem.", 1, investDirections.size());
	}

	@Test
	public void getInvestmentDirectionsData() throws SQLException {
		addStockQuotes();

		List<InvstmentDirectionsData> investDirections = presentationDao.getInvestmentDirectionsData(1);

		Assert.assertNotNull("Oczekiwana lista z 1 obiektem.", investDirections);
		Assert.assertEquals("Oczekiwana lista z 1 obiektem.", 1, investDirections.size());

	}

}
