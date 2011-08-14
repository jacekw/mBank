package pl.jw.mbank.common.request;

import pl.jw.mbank.common.dto.StockQuotesData;

public interface IStockQuotes extends IRequest {
	public abstract StockQuotesData get(Integer pk);

	public StockQuotesData get(final StockQuotesData data);

	public abstract void update(StockQuotesData data);

}
