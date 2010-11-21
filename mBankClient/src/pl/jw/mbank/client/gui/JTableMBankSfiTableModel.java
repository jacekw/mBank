/**
 * 
 */
package pl.jw.mbank.client.gui;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;

class JTableMBankSfiTableModel extends AbstractTableModel {

	private static final int SFI_PK = 8;
	public static final int ACCTUAL_UNIT_VALUE = 2;
	public static final int INIT_UNIT_VALUE = 7;
	public static final int INVESTED_AMOUNT = 4;
	public static final int IS_SYMULATION = 9;

	private final JTableMBankSfiDataSetHandler dataSetHandler;
	private int columns = 0;
	private final List<JTableMBankSfiTableColumn> visibleColumnList = new ArrayList<JTableMBankSfiTableColumn>();

	public JTableMBankSfiTableModel(JTableMBankSfiDataSetHandler dataSetHandler) {
		this.dataSetHandler = dataSetHandler;

		init();
	}

	private void init() {
		columns = 9;

		getTableColumnList();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PresentationData sdp = dataSetHandler.getSfiData().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return sdp.getSfiData().getName();
		case 1:
			return sdp.getStockQuotesData().getDate();
		case ACCTUAL_UNIT_VALUE:
			return sdp.getStockQuotesData().getValue();
		case 3:
			return sdp.getStockQuotesData().getDelta();
		case INVESTED_AMOUNT:
			return sdp.getInvestmentData().getInitialvalue();
		case 5:
			return sdp.getInvestmentData().getUnits();
		case 6:
			return sdp.getAcctualValue();
		case INIT_UNIT_VALUE:
			return sdp.getInitValue();
		case SFI_PK:
			return sdp.getSfiData().getId();
		case IS_SYMULATION:
			return sdp.getInvestmentData().isSimulation();
		default:
			return "";
		}
	}

	@Override
	public String getColumnName(int column) {
		return visibleColumnList.get(column).getName();
	}

	@Override
	public int getColumnCount() {
		return visibleColumnList.size();
	}

	@Override
	public int getRowCount() {
		return dataSetHandler.getSfiData() == null ? 0 : dataSetHandler.getSfiData().size();
	}

	public PresentationData getSelectedRowData(int rowIndex) {
		PresentationData pd = new PresentationData();

		SfiData sfiData = new SfiData();
		InvestmentData investmentData = new InvestmentData();
		StockQuotesData stockQuotesData = new StockQuotesData();
		stockQuotesData.setSfi(sfiData);

		for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
			Object v = getValueAt(rowIndex, columnIndex);

			switch (columnIndex) {
			case 0:
				sfiData.setName((String) v);
				break;
			case 1:
				stockQuotesData.setDate((Date) v);
				break;
			case ACCTUAL_UNIT_VALUE:
				stockQuotesData.setValue((BigDecimal) v);
				break;
			case 3:
				stockQuotesData.setDelta((BigDecimal) v);
				break;
			case INVESTED_AMOUNT:
				investmentData.setInitialvalue((BigDecimal) v);
				break;
			case 5:
				investmentData.setUnits((BigDecimal) v);
				break;
			case 6:
				pd.setAcctualValue((BigDecimal) v);
				break;
			case INIT_UNIT_VALUE:
				pd.setInitValue((BigDecimal) v);
				break;
			case SFI_PK: {
				sfiData.setId((Integer) v);
				break;
			}
			default:
				break;
			}

		}
		pd.setInvestmentData(investmentData);
		pd.setSfiData(sfiData);
		pd.setStockQuotesData(stockQuotesData);
		pd.setPk(sfiData.getId());

		return pd;
	}

	public List<JTableMBankSfiTableColumn> getVisibleColumnList() {
		return visibleColumnList;
	}

	public JTableMBankSfiDataSetHandler getDataSetHandler() {
		return dataSetHandler;
	}

	static Collection<JTableMBankSfiTableColumn> getTableColumnList() {
		List<JTableMBankSfiTableColumn> visibleColumnList = new ArrayList<JTableMBankSfiTableColumn>();

		visibleColumnList.add(new JTableMBankSfiTableColumn("Name", 0, 300, true));
		visibleColumnList.add(new JTableMBankSfiTableColumn("Date", 1, 75, false));
		visibleColumnList.add(new JTableMBankSfiTableColumn("Unit value", 2, 65, false));
		visibleColumnList.add(new JTableMBankSfiTableColumn("Unit's value delta", 3, 100, false));

		visibleColumnList.add(new JTableMBankSfiTableColumn("Invested [PLN]", 4, 100, false));
		visibleColumnList.add(new JTableMBankSfiTableColumn("Units", 5, 60, false));

		visibleColumnList.add(new JTableMBankSfiTableColumn("Acctual unit's value", 6, 110, false));
		visibleColumnList.add(new JTableMBankSfiTableColumn("Initial unit's value", 7, 100, false));

		return visibleColumnList;
	}

}