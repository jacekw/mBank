package pl.jw.mbank.client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.ExceptionHandler;
import pl.jw.mbank.client.data.ITableDataRefreshCallbackHanlder;
import pl.jw.mbank.client.gui.PanelGraph.PanelGraphDataInterpreterDelta;
import pl.jw.mbank.client.gui.PanelGraph.PanelGraphDataInterpreterValue;
import pl.jw.mbank.client.gui.PanelStocks.ITableRowSelectedListener;
import pl.jw.mbank.client.gui.account.DialogAccountModification;
import pl.jw.mbank.client.gui.util.BoxUtil;
import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.request.IAccount;

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
	private final JButton jButtonAddAccount = new JButton("+");
	private final JToolBar jToolBar = new JToolBar();
	private final JTextField jTextFieldFilter = new JTextField();
	private final JTabbedPane jTabbedPaneNorth = new JTabbedPane();;
	private final JTabbedPane jTabbedPaneSouth = new JTabbedPane();

	private final PanelPeriodGrowSummary panelPeriodGrowSummary = new PanelPeriodGrowSummary();

	private final TableRefreshCallbackHanlder refreshCallbackHanlder = new TableRefreshCallbackHanlder();

	private final PanelInvestments panelInvestments = new PanelInvestments(refreshCallbackHanlder);

	private final PanelGraph panelGraphDelta = new PanelGraph(new PanelGraphDataInterpreterDelta());
	private final PanelGraph panelGraphValue = new PanelGraph(new PanelGraphDataInterpreterValue());

	private final PanelInvestmentCandidates panelInvestmentCandidates = new PanelInvestmentCandidates();

	public PanelMain(Window window) throws Exception {
		this.window = window;

		initComponents();
	}

	private void initComponents() throws Exception {

		systemTraySupport = new SystemTraySupport(window, refreshCallbackHanlder);
		systemTraySupport.init();

		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());

		jButtonLoadData.setPreferredSize(new Dimension(30, 30));
		jButtonLoadData.setMaximumSize(new Dimension(30, 30));
		jButtonLoadData.setName("jButtonLoadData");

		jButtonAddAccount.setPreferredSize(new Dimension(30, 30));
		jButtonAddAccount.setMaximumSize(new Dimension(30, 30));
		jButtonAddAccount.setName("jButtonAddAccount");

		jTextFieldFilter.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		jTextFieldFilter.setName("jTextFieldFilter");

		jToolBar.setPreferredSize(new Dimension(32, 32));

		jToolBar.add(jButtonLoadData);
		jToolBar.add(jButtonAddAccount);
		jToolBar.addSeparator();
		jToolBar.add(jTextFieldFilter);

		jTabbedPaneSouth.setPreferredSize(new Dimension(Short.MAX_VALUE, 200));
		jTabbedPaneSouth.addTab("Graph - delta", panelGraphDelta);
		jTabbedPaneSouth.addTab("Graph - unit value", panelGraphValue);

		jTabbedPaneSouth.addTab("Analysis", panelInvestmentCandidates);

		generateAccountsTabs();

		add(jToolBar, BorderLayout.PAGE_START);
		add(jTabbedPaneNorth, BorderLayout.CENTER);
		add(panelInvestments, BorderLayout.EAST);

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(panelPeriodGrowSummary, BorderLayout.SOUTH);
		southPanel.add(jTabbedPaneSouth, BorderLayout.CENTER);

		add(southPanel, BorderLayout.SOUTH);

		addActions();

		setup();
	}

	private void generateAccountsTabs() throws SQLException, Exception {
		jTabbedPaneNorth.removeAll();

		List<AccountData> listAccountData = Env.requestData(IAccount.class).get();
		for (AccountData accountData : listAccountData) {
			PanelStocks panelStocks = new PanelStocks(window, accountData, new StockTableRowSelectedListener());
			panelStocks.setup();
			jTabbedPaneNorth.add(accountData.getName(), panelStocks);

			JButton jbuttonDelete = new JButton("x");
			JLabel jLabelTab = new JLabel(accountData.getName());
			BoxUtil.setSize(jbuttonDelete, new Dimension(20, 20));
			BoxUtil.setSize(jLabelTab, new Dimension(100, 10));

			jTabbedPaneNorth.setTabComponentAt(jTabbedPaneNorth.getTabCount() - 1,
					BoxUtil.getBox(jLabelTab, jbuttonDelete));
		}

	}

	private void addActions() {
		ActionMap map = Application.getInstance().getContext().getActionMap(this);
		javax.swing.Action action = map.get("loadData");
		jButtonLoadData.setAction(action);
		jButtonLoadData.setText("");

		action = map.get("addAccount");
		jButtonAddAccount.setAction(action);
		jButtonAddAccount.setText("");

		jTabbedPaneNorth.addChangeListener(new TabChangeListener());
	}

	private void setup() throws SQLException, Exception {

		jTextFieldFilter.setText(Env.CONTEXT.getConfiguration().getDataFilter().getText());

		Env.scheduleCyclicRefresh(refreshCallbackHanlder);

	}

	@Action(name = "loadData")
	public void loadData() {

		for (Component c : jTabbedPaneNorth.getComponents()) {
			if (c instanceof PanelStocks) {
				((PanelStocks) c).loadData(jTextFieldFilter.getText());
			}
		}

	}

	@Action(name = "addAccount")
	public void addAccount() {
		try {
			DialogAccountModification dialog = new DialogAccountModification();

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);

			if (dialog.isAcceptance()) {

				Env.requestData(IAccount.class).update(dialog.getAccountData());

			}

			generateAccountsTabs();

		} catch (Exception e) {
			ExceptionHandler.exception(e);
		}
	}

	private class StockTableRowSelectedListener implements ITableRowSelectedListener {

		@Override
		public void rowSelected(AccountData accountData, SfiData sfiData) {
			try {
				panelPeriodGrowSummary.refresh(sfiData);
				panelInvestments.setData(accountData, sfiData);
				panelGraphDelta.refresh(sfiData);
				panelGraphValue.refresh(sfiData);
			} catch (Exception e) {
				ExceptionHandler.exception(e);
			}

		}

	}

	private class TabChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			try {
				PanelStocks panelStocks = (PanelStocks) jTabbedPaneNorth.getSelectedComponent();
				panelInvestments.setData(panelStocks.getAccountData(), null);
			} catch (Exception ex) {
				ExceptionHandler.exception(ex);
			}

		}
	}

}
