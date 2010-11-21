/**
 * 
 */
package pl.jw.mbank.client.gui;

import java.awt.Cursor;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.ExceptionHandler;
import pl.jw.mbank.client.data.IDataSetCallbackHandler;
import pl.jw.mbank.common.dto.PresentationData;

public class JTableMBankSfiDataSetHandler implements IDataSetCallbackHandler<PresentationData> {

	private final JTable table;
	private final Window window;

	private List<PresentationData> sfiData = new ArrayList<PresentationData>();
	private final List<PresentationData> sfiFilteredData = new ArrayList<PresentationData>();

	public JTableMBankSfiDataSetHandler(Window window, JTable table) {
		this.window = window;
		this.table = table;
	}

	@Override
	public void started() {
		window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	@Override
	public void setData(List<PresentationData> data) {

		sfiData = data;

		sfiFilteredData.clear();
		for (PresentationData sdp : sfiData) {
			if (Env.CONTEXT.getConfiguration().getDataFilter().isMatching(sdp.getSfiData().getName()))
				sfiFilteredData.add(sdp);
		}

		if (table.getModel() instanceof AbstractTableModel) {
			((AbstractTableModel) table.getModel()).fireTableDataChanged();
		}
	}

	@Override
	public void failed(Throwable cause) {
		ExceptionHandler.exception(cause);
	}

	@Override
	public void finished() {
		window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public List<PresentationData> getSfiData() {
		return sfiFilteredData;
	}

}