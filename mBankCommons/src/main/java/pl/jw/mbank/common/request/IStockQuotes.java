package pl.jw.mbank.common.request;

import java.sql.SQLException;

import pl.jw.mbank.common.dto.StockQuotesData;

public interface IStockQuotes extends IRequest {
	public abstract StockQuotesData get(Integer pk) throws SQLException;

	public StockQuotesData get(final StockQuotesData data) throws SQLException;

	public abstract void update(StockQuotesData data) throws SQLException;

}
