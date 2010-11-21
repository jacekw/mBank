package pl.jw.mbank.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PresentationSummaryData implements Serializable {

	private BigDecimal acctualTotalInvestmentValue;
	private BigDecimal totalInvestedAmount;

	public PresentationSummaryData(BigDecimal acctualTotalInvestmentValue, BigDecimal totalInvestedAmount) {
		super();
		this.acctualTotalInvestmentValue = acctualTotalInvestmentValue;
		this.totalInvestedAmount = totalInvestedAmount;
	}

	public BigDecimal getAcctualTotalInvestmentValue() {
		return acctualTotalInvestmentValue;
	}

	public void setAcctualTotalInvestmentValue(BigDecimal acctualTotalInvestmentValue) {
		this.acctualTotalInvestmentValue = acctualTotalInvestmentValue;
	}

	public BigDecimal getTotalInvestedAmount() {
		return totalInvestedAmount;
	}

	public void setTotalInvestedAmount(BigDecimal totalInvestedAmount) {
		this.totalInvestedAmount = totalInvestedAmount;
	}

}
