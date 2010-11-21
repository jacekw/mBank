package pl.jw.mbank.client.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class PanelInvestmentCandidates extends JPanel {

	private final JTable jTableInvestCandidates = new JTable();

	public PanelInvestmentCandidates() {

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		add(new JScrollPane(jTableInvestCandidates), BorderLayout.CENTER);

		setup();
	}

	private void setup() {
		jTableInvestCandidates.setAutoscrolls(true);
		jTableInvestCandidates.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableInvestCandidates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}

}
