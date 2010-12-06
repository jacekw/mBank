package pl.jw.mbank.common.dto;

public class InvstmentDirectionsData extends StockQuotesData {

	@Override
	public String toString() {
		return getSfi().getName() + " : " + getDelta();
	}

}
