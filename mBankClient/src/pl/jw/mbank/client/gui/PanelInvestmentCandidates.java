package pl.jw.mbank.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import pl.jw.mbank.client.RequestProxyFactory;
import pl.jw.mbank.common.dto.InvstmentDirectionsData;
import pl.jw.mbank.common.request.IPresentation;

public class PanelInvestmentCandidates extends JPanel {

	private final ButtonGroup buttonGroup = new ButtonGroup();

	private final JRadioButton jRadioButtonOneMonth = new JRadioButton("one month", true);
	private final JRadioButton jRadioButtonThreeMonths = new JRadioButton("three months");
	private final JRadioButton jRadioButtonHalfYear = new JRadioButton("half year");
	private final JRadioButton jRadioButtonYear = new JRadioButton("year");
	private final JRadioButton jRadioButtonMax = new JRadioButton("max");

	private final JList jTableInvestCandidates = new JList();

	public PanelInvestmentCandidates() {

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		buttonGroup.add(jRadioButtonOneMonth);
		buttonGroup.add(jRadioButtonThreeMonths);
		buttonGroup.add(jRadioButtonHalfYear);
		buttonGroup.add(jRadioButtonYear);
		buttonGroup.add(jRadioButtonMax);

		JPanel jPanelPeriod = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));

		jPanelPeriod.add(jRadioButtonOneMonth);
		jPanelPeriod.add(jRadioButtonThreeMonths);
		jPanelPeriod.add(jRadioButtonHalfYear);
		jPanelPeriod.add(jRadioButtonYear);
		jPanelPeriod.add(jRadioButtonMax);

		add(jPanelPeriod, BorderLayout.NORTH);
		add(new JScrollPane(jTableInvestCandidates), BorderLayout.CENTER);

		setup();
	}

	private void setup() {
		jTableInvestCandidates.setAutoscrolls(true);
		//		jTableInvestCandidates.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableInvestCandidates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jRadioButtonOneMonth.addActionListener(new RadioChangeListener());
		jRadioButtonThreeMonths.addActionListener(new RadioChangeListener());
		jRadioButtonHalfYear.addActionListener(new RadioChangeListener());
		jRadioButtonYear.addActionListener(new RadioChangeListener());
		jRadioButtonMax.addActionListener(new RadioChangeListener());

	}

	void refresh() throws Exception {
		Integer months = null;
		if (buttonGroup.isSelected(jRadioButtonOneMonth.getModel())) {
			months = 1;
		} else if (buttonGroup.isSelected(jRadioButtonThreeMonths.getModel())) {
			months = 3;
		} else if (buttonGroup.isSelected(jRadioButtonHalfYear.getModel())) {
			months = 6;
		} else if (buttonGroup.isSelected(jRadioButtonYear.getModel())) {
			months = 12;
		}

		List<InvstmentDirectionsData> investmentDirectionsData = RequestProxyFactory.getInstance()
				.requestData(IPresentation.class).getInvestmentDirectionsData(months);

		jTableInvestCandidates.setListData(new Vector<InvstmentDirectionsData>(investmentDirectionsData));
	}

	private class RadioChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				refresh();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

	}
}
