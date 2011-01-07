package pl.jw.mbank.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.ExceptionHandler;
import pl.jw.mbank.client.data.RefreshTimer;
import pl.jw.mbank.client.data.loader.HttpDataUpdater;
import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.filter.DataFilter;
import pl.jw.mbank.common.request.IPresentation;

public class PanelStocks extends JPanel {

	interface ITableRowSelectedListener {
		void rowSelected(AccountData accountData, SfiData selectedRowId);
	}

	private final Window window;

	private final AccountData accountData;

	private final RefreshTimer refreshTimer = new RefreshTimer(Env.CONTEXT.getConfiguration().getDataRefreshTimeout());

	private final ITableRowSelectedListener rowSelectedListener;

	private final JTable jTableMBankSfi = new JTableMBankSfi();

	private JTableMBankSfiTableModel jTableMBankSfiTableModel;
	private JTableMBankSfiTableColumnModel jTableMBankSfiTableColumnModel;

	public PanelStocks(Window window, AccountData ad, ITableRowSelectedListener rowSelectedListener) {
		this.window = window;
		this.accountData = ad;
		this.rowSelectedListener = rowSelectedListener;

		initComponents();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(700, 200));
		setLayout(new BorderLayout());

		add(new JScrollPane(jTableMBankSfi), BorderLayout.CENTER);

		setup();
	}

	void setup() {
		jTableMBankSfi.setAutoscrolls(true);
		jTableMBankSfi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableMBankSfi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTableMBankSfiTableModel = new JTableMBankSfiTableModel(
				new JTableMBankSfiDataSetHandler(window, jTableMBankSfi));
		jTableMBankSfi.setModel(jTableMBankSfiTableModel);

		jTableMBankSfiTableColumnModel = new JTableMBankSfiTableColumnModel();
		jTableMBankSfiTableColumnModel.addColumns(JTableMBankSfiTableModel.getTableColumnList());
		jTableMBankSfi.setColumnModel(jTableMBankSfiTableColumnModel);

		// jTableMBankSfi.setAutoCreateRowSorter(true);
		// jTableMBankSfi.setRowSorter(new
		// TableRowSorter<TableModel>(jTableMBankSfiTableModel));

		jTableMBankSfi.getSelectionModel().addListSelectionListener(new TableSelectionHandler());

	}

	void loadData(String filter) {
		Env.CONTEXT.getConfiguration().setDataFilter(new DataFilter(filter));

		//DataGetter.getInstance().register(PresentationData.class, new HttpDataUpdater(), new DbDataUpdater());

		jTableMBankSfiTableModel.getDataSetHandler().started();
		try {

			List<PresentationData> data;

			if (refreshTimer.isTimeout()) {
				new HttpDataUpdater().get();
				refreshTimer.mark();
			}

			try {
				data = Env.requestData(IPresentation.class).get(accountData, null);
			} finally {
				SystemTraySupport.showTrayPopUpMessage("Data loaded");
			}

			jTableMBankSfiTableModel.getDataSetHandler().setData(data);

			//			jTableMBankSfiTableModel.getDataSetHandler().setData(DataGetter.getInstance().get(PresentationData.class));

			jTableMBankSfiTableModel.getDataSetHandler().finished();
		} catch (Exception e) {
			jTableMBankSfiTableModel.getDataSetHandler().failed(e);
		}

	}

	private void rowSelected(PresentationData selectedRowData) throws Exception {
		SfiData sfiData = selectedRowData == null ? null : selectedRowData.getSfiData();

		rowSelectedListener.rowSelected(accountData, sfiData);
	}

	private class TableSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			try {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				if (!lsm.isSelectionEmpty()) {
					if (!lsm.getValueIsAdjusting()) {
						int i = lsm.getLeadSelectionIndex();
						if (lsm.isSelectedIndex(i)) {
							rowSelected(jTableMBankSfiTableModel.getSelectedRowData(i));
						}
					}
				} else {
					rowSelected(null);
				}

			} catch (Exception ex) {
				ExceptionHandler.exception(ex);
			}
		}

	}

}
