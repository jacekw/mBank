package pl.jw.mbank.client.gui;

import java.util.Collection;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class JTableMBankSfiTableColumnModel extends DefaultTableColumnModel {

	public void addColumns(Collection<? extends TableColumn> aColumn) {
		for (TableColumn tableColumn : aColumn) {
			addColumn(tableColumn);
		}
	}

}
