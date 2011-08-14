package pl.jw.mbank.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import pl.jw.mbank.common.Util;
import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.InvstmentDirectionsData;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.dto.PresentationGraphData;
import pl.jw.mbank.common.dto.PresentationSummaryData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;
import pl.jw.mbank.common.dto.pk.SfiPk;
import pl.jw.mbank.common.filter.DataFilter;
import pl.jw.mbank.common.request.IPresentation;
import pl.jw.mbank.common.request.ISfi;
import pl.jw.mbank.common.request.IStockQuotes;

@Transactional
public class Presentation extends HibernateDaoSupport implements IPresentation {

	private ISfi sfi;
	private IStockQuotes stockQuotes;

	public Presentation() {
		super();
	}

	public void setSfi(ISfi sfi) {
		this.sfi = sfi;
	}

	public void setStockQuotes(IStockQuotes stockQuotes) {
		this.stockQuotes = stockQuotes;
	}

	@Override
	public void update(final List<PresentationData> dataList) {
		for (PresentationData presentationData : dataList) {
			SfiPk sfiPk = sfi.update(presentationData.getSfiData());
			presentationData.setPk(sfiPk.getId());
			stockQuotes.update(presentationData.getStockQuotesData());
		}

		getHibernateTemplate().flush();
	}

	@Override
	public List<PresentationData> get(final AccountData accountData, DataFilter filter) {
		Criteria main = getSession().createCriteria(SfiData.class);

		Criteria stockQuotes = main.createAlias("stockQuotes", StockQuotesData.class.getSimpleName()).add(Restrictions.eqProperty("id", "sfi_id"));

		Criteria invQuotes = main.createAlias("investment", InvestmentData.class.getSimpleName()).add(Restrictions.eqProperty("id", "sfi_id"));

		SQLQuery q = getSession()
				.createSQLQuery(
						"SELECT SFI.NAME, SFI.ID, SFI.INVESTMENT_ID, "
								+ " INVESTMENT.ID, INVESTMENT.SFI_ID, INVESTMENT.Simulation, Investment.account_id, "
								+ " STOCKQUOTES.ID,STOCKQUOTES.SFI_ID, STOCKQUOTES.DATE, STOCKQUOTES.VALUE , STOCKQUOTES.DELTA,"
								+ " Account.NAME, Account.ID, "
								+ " COALESCE(Investment.initialvalue,0) AS initialvalue , "
								+ " COALESCE(Investment.units,0) AS units , "
								+ " CAST (COALESCE(Investment.units,0) * StockQuotes.value AS DECIMAL(18,3)) AS acctualValue, "
								+ " CASE WHEN Investment.initialvalue IS NULL OR Investment.initialvalue = 0 OR Investment.units IS NULL OR Investment.units = 0 THEN 0 ELSE CAST (Investment.initialvalue / Investment.units  AS  DECIMAL(18,3)) END AS initValue "
								+ " FROM SFI AS sfi " + " LEFT OUTER JOIN StockQuotes ON StockQuotes.SFI_ID = SFI.ID  "
								+ " LEFT OUTER JOIN Investment ON Investment.SFI_ID = SFI.ID AND "
								+ (accountData == null ? " Investment.ACCOUNT_ID IS NULL " : " Investment.ACCOUNT_ID = " + accountData.getId())
								+ " LEFT OUTER JOIN Account ON Investment.account_id = Account.ID " + " WHERE " + " StockQuotes.date = ( "
								+ " SELECT MAX(sq.date) FROM StockQuotes sq WHERE sq.SFI_ID = SFI.ID" + " ) " + " ORDER BY SFI.NAME, STOCKQUOTES.DATE");

		q.addEntity("SFI", SfiData.class).addEntity("INVESTMENT", InvestmentData.class).addEntity("STOCKQUOTES", StockQuotesData.class).addEntity("ACCOUNT", AccountData.class)
				.addScalar("acctualValue").addScalar("initValue");//

		List<Object[]> data = q.list();

		List<PresentationData> pDataList = new ArrayList<PresentationData>();
		for (Object[] r : data) {
			PresentationData pd = new PresentationData();
			pd.setSfiData((SfiData) r[0]);
			pd.setInvestmentData((InvestmentData) r[1]);
			pd.setStockQuotesData((StockQuotesData) r[2]);
			pd.setAcctualValue((BigDecimal) r[4]);
			pd.setInitValue((BigDecimal) r[5]);
			pDataList.add(pd);
		}

		return pDataList;
	}

	@Override
	public PresentationSummaryData getSummary(final AccountData accountData) {
		SQLQuery q = getSession().createSQLQuery(
				"SELECT COALESCE( SUM( CAST (COALESCE(Investment.units,0) * StockQuotes.value AS DECIMAL(18,3))) ,0 ) AS acctualTotalInvestmentValue, "
						+ " COALESCE( SUM( CASE WHEN INVESTMENT.initialvalue IS NULL THEN 0 ELSE INVESTMENT.initialvalue END ),0 ) AS totalInvestedAmount " + "FROM Investment "
						+ "LEFT OUTER JOIN StockQuotes ON STOCKQUOTES.sfi_id = INVESTMENT.sfi_id  " + " WHERE Investment.SIMULATION = 0 " + " AND "
						+ (accountData == null ? " Investment.ACCOUNT_ID IS NULL " : " Investment.ACCOUNT_ID = " + accountData.getId()) + " AND " + "StockQuotes.date = ("
						+ "SELECT MAX(sq.date) FROM StockQuotes sq WHERE SQ.sfi_id = INVESTMENT.sfi_id" + " ) ");

		q.addScalar("acctualTotalInvestmentValue").addScalar("totalInvestedAmount");

		Object[] dataRow = (Object[]) q.list().get(0);

		PresentationSummaryData data = new PresentationSummaryData((BigDecimal) dataRow[0], (BigDecimal) dataRow[1]);

		return data;
	}

	@Override
	public List<PresentationGraphData> getGraphData(final SfiData sfiPk) {
		Criteria main = getSession().createCriteria(StockQuotesData.class);
		main.add(Restrictions.eq("sfi.id", sfiPk.getId()));
		main.addOrder(Order.asc("date"));

		return main.list();
	}

	/**
	 * 
	 * @param sfiPk
	 *            null possible -> no sfi context
	 * @param months
	 *            null possible -> no data range
	 * @return
	 */
	private List<StockQuotesData> getGrowthData(final SfiData sfiPk, final Integer months) {
		Calendar fromDate = months == null ? null : Util.rollDate(months, Calendar.getInstance());

		Criteria main = getSession().createCriteria(StockQuotesData.class);
		if (sfiPk != null) {
			main.add(Restrictions.eq("sfi.id", sfiPk.getId()));
		}
		if (fromDate != null) {
			main.add(Restrictions.ge("date", fromDate.getTime()));
		}
		main.addOrder(Order.asc("date"));

		return main.list();
	}

	@Override
	public PresentationGraphData getPeriodSummaryData(final SfiData sfiPk, final Integer months) {

		List<StockQuotesData> listData = getGrowthData(sfiPk, months);

		PresentationGraphData pgd = new PresentationGraphData();
		if (listData != null && listData.size() > 1) {
			StockQuotesData sqFirst = listData.get(0);
			StockQuotesData sqLast = listData.get(listData.size() - 1);

			BigDecimal growth = sqLast.getValue().subtract(sqFirst.getValue()).setScale(3, RoundingMode.HALF_DOWN);

			pgd.setDate(sqFirst.getDate());
			pgd.setDelta(growth.divide(sqFirst.getValue(), RoundingMode.FLOOR).multiply(new BigDecimal(100)).setScale(3, RoundingMode.HALF_DOWN));
			pgd.setSfi(sqFirst.getSfi());
			pgd.setValue(growth);
		}

		return pgd;
	}

	@Override
	public List<InvstmentDirectionsData> getInvestmentDirectionsData(final Integer months) {

		List<InvstmentDirectionsData> listIDData = new ArrayList<InvstmentDirectionsData>();

		// dla kazdego sfi pobrac wartosc z poczatkowej daty zakresu i ostatniej
		// daty zakresu i obliczyc % roznice

		Calendar fromDate = months == null ? null : Util.rollDate(months, Calendar.getInstance());

		Criteria main = getSession().createCriteria(StockQuotesData.class, "sq");
		main.createCriteria("sfi", "sfi");
		main.setProjection(Projections.projectionList().add(Projections.alias(Projections.sum("delta"), "delta")).add(Projections.alias(Projections.max("value"), "value"))
				.add(Projections.alias(Projections.max("date"), "date")).add(Projections.alias(Projections.groupProperty("sfi.id"), "sfi.id"))
				.add(Projections.alias(Projections.property("sfi.name"), "sfi.name"))

		);

		main.add(Restrictions.le("delta", BigDecimal.ZERO));
		if (fromDate != null) {
			main.add(Restrictions.ge("date", fromDate.getTime()));
		}
		main.addOrder(Order.asc("delta"));

		List<Object[]> listData = main.list();
		for (Object[] dataRow : listData) {
			InvstmentDirectionsData idd = new InvstmentDirectionsData();
			idd.setDate((Date) dataRow[2]);
			idd.setDelta((BigDecimal) dataRow[0]);
			idd.setValue((BigDecimal) dataRow[1]);
			idd.setSfi(new SfiData((Integer) dataRow[3], (String) dataRow[4]));

			listIDData.add(idd);
		}

		return listIDData;
	}
}
