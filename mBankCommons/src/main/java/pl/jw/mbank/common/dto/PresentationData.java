package pl.jw.mbank.common.dto;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import pl.jw.mbank.common.IData;
import pl.jw.mbank.common.dto.pk.SfiPk;

@Embeddable
public class PresentationData implements IData<SfiPk> {

	private SfiData sfiData;

	private StockQuotesData stockQuotesData;

	private InvestmentData investmentData;

	// wyliczane
	private BigDecimal initValue;
	private BigDecimal acctualValue;

	public SfiData getSfiData() {
		return sfiData;
	}

	public void setSfiData(SfiData sfiData) {
		this.sfiData = sfiData;
	}

	public BigDecimal getInitValue() {
		return initValue;
	}

	public void setInitValue(BigDecimal initValue) {
		this.initValue = initValue;
	}

	public StockQuotesData getStockQuotesData() {
		return stockQuotesData;
	}

	public void setStockQuotesData(StockQuotesData stockQuotesData) {
		this.stockQuotesData = stockQuotesData;
	}

	public InvestmentData getInvestmentData() {
		return investmentData;
	}

	public void setInvestmentData(InvestmentData investmentData) {
		this.investmentData = investmentData;
	}

	public BigDecimal getAcctualValue() {
		return acctualValue;
	}

	public void setAcctualValue(BigDecimal acctualValue) {
		this.acctualValue = acctualValue;
	}

	@Transient
	public void setPk(int pk) {
		if (sfiData != null)
			sfiData.setId(pk);
	}

	@Override
	public SfiPk getPk() {
		if (sfiData != null)
			return new SfiPk(sfiData.getId());
		else
			return null;
	}

	@Override
	public String toString() {
		return "PresentationData [acctualValue=" + acctualValue + ", initValue=" + initValue + ", investmentData="
				+ investmentData + ", sfiData=" + sfiData + ", stockQuotesData=" + stockQuotesData + "]";
	}

}
