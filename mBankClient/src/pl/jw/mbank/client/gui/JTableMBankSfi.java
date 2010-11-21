/**
 * 
 */
package pl.jw.mbank.client.gui;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

final class JTableMBankSfi extends JTable {

	public JTableMBankSfi() {
		super();
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		BigDecimal investedAmount = (BigDecimal) getModel().getValueAt(row, JTableMBankSfiTableModel.INVESTED_AMOUNT);
		BigDecimal actualUnitValue = (BigDecimal) getModel().getValueAt(row,
				JTableMBankSfiTableModel.ACCTUAL_UNIT_VALUE);
		Boolean isSelected = (Boolean) getModel().getValueAt(row, JTableMBankSfiTableModel.IS_SYMULATION);
		BigDecimal initUnitValue = (BigDecimal) getModel().getValueAt(row, JTableMBankSfiTableModel.INIT_UNIT_VALUE);
		if (isSelected != null && investedAmount != null && investedAmount.compareTo(BigDecimal.ZERO) != 0) {
			if (isSelected) {
				if (actualUnitValue.compareTo(initUnitValue) < 0) {
					c.setBackground(Color.ORANGE);
				} else if (actualUnitValue.compareTo(initUnitValue) >= 0) {
					c.setBackground(Color.MAGENTA);
				}
			} else {
				if (actualUnitValue.compareTo(initUnitValue) < 0) {
					c.setBackground(Color.RED);
				} else if (actualUnitValue.compareTo(initUnitValue) >= 0) {
					c.setBackground(Color.GREEN);
				}
			}
		} else
			c.setBackground(Color.WHITE);

		c.setForeground(Color.BLACK);

		return c;
	}
}