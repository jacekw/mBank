package pl.jw.mbank.client.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import pl.jw.mbank.client.RequestProxyFactory;
import pl.jw.mbank.common.dto.PresentationGraphData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.request.IPresentation;

public class PanelPeriodGrowSummary extends JPanel {
	private final JLabel label = new JLabel("Growth summary:");
	private final JLabel yearStats = new JLabel();
	private final JLabel monthStats = new JLabel();
	private final JLabel halfYearStats = new JLabel();
	private final JLabel threeMonthStats = new JLabel();

	public PanelPeriodGrowSummary() {
		init();
	}

	private void init() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		//		setBorder(new EtchedBorder());

		add(label);
		add(new JSeparator());
		add(monthStats);
		add(new JSeparator());
		add(threeMonthStats);
		add(new JSeparator());
		add(halfYearStats);
		add(new JSeparator());
		add(yearStats);
	}

	void refresh(SfiData sfiData) throws Exception {
		if (sfiData != null) {
			PresentationGraphData monthData = RequestProxyFactory.getInstance().requestData(IPresentation.class)
					.getPeriodSummaryData(sfiData, 1);
			PresentationGraphData threeMonthData = RequestProxyFactory.getInstance().requestData(IPresentation.class)
					.getPeriodSummaryData(sfiData, 3);
			PresentationGraphData halfYearData = RequestProxyFactory.getInstance().requestData(IPresentation.class)
					.getPeriodSummaryData(sfiData, 6);
			PresentationGraphData yearData = RequestProxyFactory.getInstance().requestData(IPresentation.class)
					.getPeriodSummaryData(sfiData, 12);

			yearStats.setText("year: " + yearData.getDelta());
			monthStats.setText("month: " + monthData.getDelta());
			halfYearStats.setText("half year: " + halfYearData.getDelta());
			threeMonthStats.setText("three months: " + threeMonthData.getDelta());
		} else {
			yearStats.setText("");
			monthStats.setText("");
			halfYearStats.setText("");
			threeMonthStats.setText("");
		}

	}
}
