package pl.jw.mbank.biz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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

public class Presentation extends HibernateDaoSupport implements IPresentation {

	private ISfi sfi;
	private IStockQuotes stockQuotes;

	public Presentation() throws SQLException {
		super();
	}

	public void setSfi(ISfi sfi) {
		this.sfi = sfi;
	}

	public void setStockQuotes(IStockQuotes stockQuotes) {
		this.stockQuotes = stockQuotes;
	}

	public void update(final List<PresentationData> dataList) throws SQLException {
		HibernateCallback<Void> action = new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				for (PresentationData presentationData : dataList) {
					SfiPk sfiPk = sfi.update(presentationData.getSfiData());
					presentationData.setPk(sfiPk.getId());
					stockQuotes.update(presentationData.getStockQuotesData());
				}

				session.flush();

				return null;
			}
		};

		getHibernateTemplate().execute(action);
	}

	public List<PresentationData> get(DataFilter filter) throws SQLException {

		HibernateCallback<List<PresentationData>> action = new HibernateCallback<List<PresentationData>>() {

			@Override
			public List<PresentationData> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria main = session.createCriteria(SfiData.class);

				Criteria stockQuotes = main.createAlias("stockQuotes", StockQuotesData.class.getSimpleName()).add(
						Restrictions.eqProperty("id", "sfi_id"));

				Criteria invQuotes = main.createAlias("investment", InvestmentData.class.getSimpleName()).add(
						Restrictions.eqProperty("id", "sfi_id"));

				//				List l = main.list();

				SQLQuery q = session
						.createSQLQuery("SELECT SFI.NAME, SFI.ID, SFI.INVESTMENT_ID, "
								+ " INVESTMENT.ID, INVESTMENT.SFI_ID, INVESTMENT.Simulation,"
								+ " STOCKQUOTES.ID,STOCKQUOTES.SFI_ID, STOCKQUOTES.DATE, STOCKQUOTES.VALUE , STOCKQUOTES.DELTA,"
								+ " COALESCE(Investment.initialvalue,0) AS initialvalue , "
								+ " COALESCE(Investment.units,0) AS units , "
								+ " CAST (COALESCE(Investment.units,0) * StockQuotes.value AS DECIMAL(18,3)) AS acctualValue, "
								+ " CASE WHEN Investment.initialvalue IS NULL OR Investment.initialvalue = 0 OR Investment.units IS NULL OR Investment.units = 0 THEN 0 ELSE CAST (Investment.initialvalue / Investment.units  AS  DECIMAL(18,3)) END AS initValue "
								+ " FROM SFI AS sfi " + " LEFT OUTER JOIN StockQuotes ON StockQuotes.SFI_ID = SFI.ID  "
								+ " LEFT OUTER JOIN Investment ON Investment.SFI_ID = SFI.ID " + " WHERE "
								+ " StockQuotes.date = ( "
								+ " SELECT MAX(sq.date) FROM StockQuotes sq WHERE sq.SFI_ID = SFI.ID" + " ) "
								+ " ORDER BY SFI.NAME, STOCKQUOTES.DATE");

				q.addEntity("SFI", SfiData.class).addEntity("INVESTMENT", InvestmentData.class)
						.addEntity("STOCKQUOTES", StockQuotesData.class).addScalar("acctualValue")
						.addScalar("initValue");

				List<Object[]> data = q.list();

				List<PresentationData> pDataList = new ArrayList<PresentationData>();
				for (Object[] r : data) {
					PresentationData pd = new PresentationData();
					pd.setSfiData((SfiData) r[0]);
					pd.setInvestmentData((InvestmentData) r[1]);
					pd.setStockQuotesData((StockQuotesData) r[2]);
					pd.setAcctualValue((BigDecimal) r[3]);
					pd.setInitValue((BigDecimal) r[4]);
					pDataList.add(pd);
				}

				return pDataList;
			}
		};

		return getHibernateTemplate().execute(action);
	}

	public PresentationSummaryData getSummary() throws SQLException {
		HibernateCallback<PresentationSummaryData> action = new HibernateCallback<PresentationSummaryData>() {

			@Override
			public PresentationSummaryData doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery q = session
						.createSQLQuery("SELECT COALESCE( SUM( CAST (COALESCE(Investment.units,0) * StockQuotes.value AS DECIMAL(18,3))) ,0 ) AS acctualTotalInvestmentValue, "
								+ " COALESCE( SUM( CASE WHEN INVESTMENT.initialvalue IS NULL THEN 0 ELSE INVESTMENT.initialvalue END ),0 ) AS totalInvestedAmount "
								+ "FROM Investment "
								+ "LEFT OUTER JOIN StockQuotes ON STOCKQUOTES.sfi_id = INVESTMENT.sfi_id  "
								+ " WHERE Investment.SIMULATION = 0 "
								+ " AND "
								+ "StockQuotes.date = ("
								+ "SELECT MAX(sq.date) FROM StockQuotes sq WHERE SQ.sfi_id = INVESTMENT.sfi_id" + " ) ");

				q.addScalar("acctualTotalInvestmentValue").addScalar("totalInvestedAmount");

				Object[] dataRow = (Object[]) q.list().get(0);

				PresentationSummaryData data = new PresentationSummaryData((BigDecimal) dataRow[0],
						(BigDecimal) dataRow[1]);

				return data;
			}
		};
		return getHibernateTemplate().execute(action);
	}

	public List<PresentationGraphData> getGraphData(final SfiData sfiPk) throws SQLException {

		HibernateCallback<List<PresentationGraphData>> action = new HibernateCallback<List<PresentationGraphData>>() {

			@Override
			public List<PresentationGraphData> doInHibernate(Session session) throws HibernateException, SQLException {

				Criteria main = session.createCriteria(StockQuotesData.class);
				main.add(Restrictions.eq("sfi.id", sfiPk.getId()));
				main.addOrder(Order.asc("date"));

				return main.list();
			}
		};

		return getHibernateTemplate().execute(action);
	}

	public PresentationGraphData getPeriodSummaryData(final SfiData sfiPk, final int periodLenght) throws SQLException {

		HibernateCallback<Object[]> action = new HibernateCallback<Object[]>() {

			@Override
			public Object[] doInHibernate(Session session) throws HibernateException, SQLException {

				Calendar fromDate = Calendar.getInstance();
				fromDate.roll(Calendar.MONTH, periodLenght);

				Criteria main = session.createCriteria(StockQuotesData.class);
				main.setProjection(Projections.projectionList()
						.add(Projections.alias(Projections.sum("delta"), "delta"))
						.add(Projections.alias(Projections.max("value"), "value"))
						.add(Projections.alias(Projections.max("date"), "date"))
						.add(Projections.alias(Projections.groupProperty("sfi.id"), "sfi.id")));
				main.add(Restrictions.eq("sfi.id", sfiPk.getId()));
				main.add(Restrictions.ge("date", fromDate.getTime()));

				return (Object[]) main.uniqueResult();
			}
		};

		Object[] data = getHibernateTemplate().execute(action);

		PresentationGraphData pgd = new PresentationGraphData();
		if (data != null) {
			pgd.setDate((Date) data[2]);
			pgd.setDelta((BigDecimal) data[0]);
			pgd.setId((Integer) data[3]);
			pgd.setValue((BigDecimal) data[1]);
		}

		return pgd;
	}

	public List<InvstmentDirectionsData> getInvestmentDirectionsData() throws SQLException {

		HibernateCallback<List<InvstmentDirectionsData>> action = new HibernateCallback<List<InvstmentDirectionsData>>() {

			@Override
			public List<InvstmentDirectionsData> doInHibernate(Session session) throws HibernateException, SQLException {

				Criteria main = session.createCriteria(InvstmentDirectionsData.class, "sq");
				session.createCriteria(InvestmentData.class);
				//				main.createAlias("SFI", "SFI_ID").add(Restrictions.eq("sfi.id", "SFI_ID"));
				//				main.createCriteria("SFI");
				//				main.createCriteria("id", "SFI");
				main.addOrder(Order.asc("DATE"));

				return main.list();
			}
		};

		return getHibernateTemplate().execute(action);
	}
}
