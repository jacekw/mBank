package pl.jw.mbank.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.ExceptionHandler;
import pl.jw.mbank.client.data.ITableDataRefreshCallbackHanlder;
import pl.jw.mbank.client.data.loader.DataLoadTask;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.filter.DataFilter;

public class PanelMain extends JPanel {

	private class TableRefreshCallbackHanlder implements ITableDataRefreshCallbackHanlder {
		@Override
		public void refresh() {
			jButtonLoadData.doClick();
		}
	}

	private final Window window;

	private SystemTraySupport systemTraySupport;

	private final JButton jButtonLoadData = new JButton("");
	private final JToolBar jToolBar = new JToolBar();
	private final JTextField jTextFieldFilter = new JTextField();
	private final JTabbedPane jTabbedPane = new JTabbedPane();

	private final TableRefreshCallbackHanlder refreshCallbackHanlder = new TableRefreshCallbackHanlder();

	private final PanelInvestments panelInvestments = new PanelInvestments(refreshCallbackHanlder);

	private final PanelGraph panelGraph = new PanelGraph();
	private final PanelInvestmentCandidates panelInvestmentCandidates = new PanelInvestmentCandidates();

	private final JTable jTableMBankSfi = new JTableMBankSfi();

	private JTableMBankSfiTableModel jTableMBankSfiTableModel;
	private JTableMBankSfiTableColumnModel jTableMBankSfiTableColumnModel;

	public PanelMain(Window window) {
		this.window = window;

		initComponents();
	}

	private void initComponents() {

		systemTraySupport = new SystemTraySupport(window, refreshCallbackHanlder);
		systemTraySupport.init();

		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());

		jButtonLoadData.setPreferredSize(new Dimension(30, 30));
		jButtonLoadData.setMaximumSize(new Dimension(30, 30));
		jButtonLoadData.setName("jButtonLoadData");

		jTextFieldFilter.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		jTextFieldFilter.setName("jTextFieldFilter");

		jToolBar.add(jButtonLoadData);
		jToolBar.setPreferredSize(new Dimension(32, 32));
		jToolBar.addSeparator();
		jToolBar.add(jTextFieldFilter);

		jTabbedPane.setPreferredSize(new Dimension(Short.MAX_VALUE, 200));
		jTabbedPane.addTab("Graph", panelGraph);
		jTabbedPane.addTab("Analysis", panelInvestmentCandidates);

		add(jToolBar, BorderLayout.PAGE_START);
		add(new JScrollPane(jTableMBankSfi), BorderLayout.CENTER);
		add(panelInvestments, BorderLayout.EAST);

		add(jTabbedPane, BorderLayout.SOUTH);

		addActions();

		setup();
	}

	private void setup() {
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

		jTextFieldFilter.setText(Env.CONTEXT.getConfiguration().getDataFilter().getText());

		Env.scheduleCyclicRefresh(refreshCallbackHanlder);
	}

	private void addActions() {
		ActionMap map = Application.getInstance().getContext().getActionMap(this);
		javax.swing.Action action = map.get("loadData");
		jButtonLoadData.setAction(action);
		jButtonLoadData.setText("");
	}

	@Action(name = "loadData")
	public Task<List<PresentationData>, Void> loadData() {
		Env.CONTEXT.getConfiguration().setDataFilter(new DataFilter(jTextFieldFilter.getText()));

		return new DataLoadTask(Application.getInstance(), jTableMBankSfiTableModel.getDataSetHandler());
	}

	ITableDataRefreshCallbackHanlder getRefreshCallbackHanlder() {
		return refreshCallbackHanlder;
	}

	private void rowSelected(PresentationData selectedRowData) throws Exception {
		if (selectedRowData != null) {
			panelInvestments.setData(selectedRowData.getSfiData());
			panelGraph.refresh(selectedRowData.getSfiData());
		} else {
			panelInvestments.setData(null);
			panelGraph.refresh(null);
		}
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
