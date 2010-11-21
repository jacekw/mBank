package pl.jw.mbank.client.gui;

import javax.swing.table.TableColumn;

public class JTableMBankSfiTableColumn extends TableColumn {

	private final String name;

	public JTableMBankSfiTableColumn(String name, int modelIndex, int width, boolean isResizable) {
		super(modelIndex, width);
		this.name = name;

		setHeaderValue(name);
		setIdentifier(name);
		setMinWidth(width);
		setResizable(isResizable);

		if (!isResizable)
			setMaxWidth(width);

	}

	public String getName() {
		return name;
	}
}
