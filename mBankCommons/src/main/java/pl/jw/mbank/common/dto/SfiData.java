package pl.jw.mbank.common.dto;

// Generated 2010-02-05 19:13:45 by Hibernate Tools 3.3.0.GA

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Sfi generated by hbm2java
 */
@Entity(name = "SFI")
@Table(name = "SFI")
public class SfiData implements java.io.Serializable {

	private int id;
	private String name;

	private List<StockQuotesData> stockQuotes;

	private InvestmentData investment;

	public SfiData() {
	}

	public SfiData(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany
	public List<StockQuotesData> getStockQuotes() {
		return stockQuotes;
	}

	public void setStockQuotes(List<StockQuotesData> stockQuotes) {
		this.stockQuotes = stockQuotes;
	}

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(nullable = true)
	public InvestmentData getInvestment() {
		return investment;
	}

	public void setInvestment(InvestmentData investmentData) {
		this.investment = investmentData;
	}

	@Override
	public String toString() {
		return "SfiData [id=" + id + ", name=" + name + "]";
	}

}
