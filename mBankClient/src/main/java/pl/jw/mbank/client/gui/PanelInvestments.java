package pl.jw.mbank.client.gui;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.ExceptionHandler;
import pl.jw.mbank.client.data.ITableDataRefreshCallbackHanlder;
import pl.jw.mbank.common.Util;
import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.PresentationSummaryData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.request.IInvestment;
import pl.jw.mbank.common.request.IPresentation;

public class PanelInvestments extends JPanel {

	private static final String ACTUAL_REAL_PROFIT = "Actual real profit:";
	private static final BigDecimal FUCK_BELKA = new BigDecimal(0.81);

	private static final Dimension PREFERRED_SIZE = new Dimension(90, 20);
	private static final Dimension PREFERRED_SIZE_LABEL = new Dimension(120, 20);
	private static final Dimension RIGID = new Dimension(5, 5);

	private final ITableDataRefreshCallbackHanlder refreshCallbackHanlder;

	private final JLabel jLabelInvestedAmount = new JLabel("Invested amount:");
	private final JLabel jLabelInitialUnits = new JLabel("Initial units:");
	private final JTextField jTextInvestedAmount = new JTextField();
	private final JTextField jTextUnits = new JTextField();

	private final JLabel jLabelTotalInvestedAmount = new JLabel("Total invested amount:");
	private final JTextField jTextTotalInvestedAmount = new JTextField();

	private final JLabel jLabelAcctualTotalInvestmentValue = new JLabel("Actual investment value:");
	private final JTextField jTextAcctualTotalInvestmentValue = new JTextField();

	private final JLabel jLabelAcctualProfit = new JLabel("Actual profit:");
	private final JTextField jTextAcctualProfit = new JTextField();

	private final JLabel jLabelAcctualRealProfit = new JLabel(ACTUAL_REAL_PROFIT);
	private final JLabel jLabelAcctualRealProfitPercentage = new JLabel("(..%)");
	private final JTextField jTextAcctualRealProfit = new JTextField();

	private final JCheckBox jCheckBoxSimulation = new JCheckBox("is simulation?");

	private SfiData sfiData;
	private InvestmentData investementData;

	private final Set<JTextField> setChangedFields = new HashSet<JTextField>();

	public PanelInvestments(ITableDataRefreshCallbackHanlder refreshCallbackHanlder) {
		this.refreshCallbackHanlder = refreshCallbackHanlder;
		initComponents();
	}

	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());

		jLabelInvestedAmount.setPreferredSize(PREFERRED_SIZE_LABEL);
		jLabelInvestedAmount.setMaximumSize(PREFERRED_SIZE_LABEL);
		jLabelInvestedAmount.setMinimumSize(PREFERRED_SIZE_LABEL);

		jLabelTotalInvestedAmount.setPreferredSize(PREFERRED_SIZE_LABEL);
		jLabelTotalInvestedAmount.setMaximumSize(PREFERRED_SIZE_LABEL);
		jLabelTotalInvestedAmount.setMinimumSize(PREFERRED_SIZE_LABEL);

		jLabelAcctualTotalInvestmentValue.setPreferredSize(PREFERRED_SIZE_LABEL);
		jLabelAcctualTotalInvestmentValue.setMaximumSize(PREFERRED_SIZE_LABEL);
		jLabelAcctualTotalInvestmentValue.setMinimumSize(PREFERRED_SIZE_LABEL);

		jLabelAcctualProfit.setPreferredSize(PREFERRED_SIZE_LABEL);
		jLabelAcctualProfit.setMaximumSize(PREFERRED_SIZE_LABEL);
		jLabelAcctualProfit.setMinimumSize(PREFERRED_SIZE_LABEL);

		jLabelAcctualRealProfit.setPreferredSize(PREFERRED_SIZE_LABEL);
		jLabelAcctualRealProfit.setMaximumSize(PREFERRED_SIZE_LABEL);
		jLabelAcctualRealProfit.setMinimumSize(PREFERRED_SIZE_LABEL);

		jLabelAcctualRealProfitPercentage.setPreferredSize(new Dimension(40, 20));
		jLabelAcctualRealProfitPercentage.setPreferredSize(new Dimension(40, 20));
		jLabelAcctualRealProfitPercentage.setMinimumSize(new Dimension(40, 20));

		jLabelInitialUnits.setPreferredSize(PREFERRED_SIZE_LABEL);
		jLabelInitialUnits.setMaximumSize(PREFERRED_SIZE_LABEL);
		jLabelInitialUnits.setMinimumSize(PREFERRED_SIZE_LABEL);

		jTextUnits.setPreferredSize(PREFERRED_SIZE);
		jTextUnits.setMaximumSize(PREFERRED_SIZE);
		jTextUnits.setMinimumSize(PREFERRED_SIZE);

		jTextInvestedAmount.setMaximumSize(PREFERRED_SIZE);
		jTextInvestedAmount.setPreferredSize(PREFERRED_SIZE);
		jTextInvestedAmount.setMinimumSize(PREFERRED_SIZE);

		jTextTotalInvestedAmount.setMaximumSize(PREFERRED_SIZE);
		jTextTotalInvestedAmount.setPreferredSize(PREFERRED_SIZE);
		jTextTotalInvestedAmount.setMinimumSize(PREFERRED_SIZE);

		jTextAcctualTotalInvestmentValue.setMaximumSize(PREFERRED_SIZE);
		jTextAcctualTotalInvestmentValue.setPreferredSize(PREFERRED_SIZE);
		jTextAcctualTotalInvestmentValue.setMinimumSize(PREFERRED_SIZE);

		jCheckBoxSimulation.setMaximumSize(PREFERRED_SIZE);
		jCheckBoxSimulation.setPreferredSize(PREFERRED_SIZE);
		jCheckBoxSimulation.setMinimumSize(PREFERRED_SIZE);

		jTextAcctualProfit.setMaximumSize(PREFERRED_SIZE);
		jTextAcctualProfit.setPreferredSize(PREFERRED_SIZE);
		jTextAcctualProfit.setMinimumSize(PREFERRED_SIZE);

		jTextAcctualRealProfit.setMaximumSize(PREFERRED_SIZE);
		jTextAcctualRealProfit.setPreferredSize(PREFERRED_SIZE);
		jTextAcctualRealProfit.setMinimumSize(PREFERRED_SIZE);

		add(Box.createRigidArea(RIGID));
		add(getLabelBox(jLabelInvestedAmount, jTextInvestedAmount));
		add(Box.createRigidArea(RIGID));
		add(getLabelBox(jLabelInitialUnits, jTextUnits));
		add(Box.createRigidArea(RIGID));
		add(jCheckBoxSimulation);
		add(Box.createRigidArea(RIGID));

		add(getJSeparator());

		add(Box.createRigidArea(RIGID));
		add(getLabelBox(jLabelTotalInvestedAmount, jTextTotalInvestedAmount));
		add(Box.createRigidArea(RIGID));

		add(Box.createRigidArea(RIGID));
		add(getLabelBox(jLabelAcctualTotalInvestmentValue, jTextAcctualTotalInvestmentValue));
		add(Box.createRigidArea(RIGID));

		add(Box.createRigidArea(RIGID));
		add(getLabelBox(jLabelAcctualProfit, jTextAcctualProfit));
		add(Box.createRigidArea(RIGID));

		add(getLabelBox(jLabelAcctualRealProfit, jTextAcctualRealProfit, jLabelAcctualRealProfitPercentage));
		add(Box.createRigidArea(RIGID));

		add(Box.createVerticalGlue());

		setup();
	}

	private JSeparator getJSeparator() {
		JSeparator js = new JSeparator(JSeparator.HORIZONTAL);
		js.setMaximumSize(new Dimension(Short.MAX_VALUE, 10));
		js.setPreferredSize(new Dimension(50, 10));
		js.setMinimumSize(new Dimension(Short.MAX_VALUE, 10));
		return js;
	}

	private Box getLabelBox(JLabel jLabel, JComponent... jTextField) {
		Box b = new Box(BoxLayout.X_AXIS);
		b.add(Box.createRigidArea(RIGID));
		b.add(jLabel);

		for (JComponent jComponent : jTextField) {
			b.add(Box.createRigidArea(RIGID));
			b.add(jComponent);
			b.add(Box.createRigidArea(RIGID));
		}

		b.add(Box.createHorizontalGlue());
		return b;
	}

	private void setup() {
		jTextTotalInvestedAmount.setEditable(false);
		jTextAcctualTotalInvestmentValue.setEditable(false);
		jCheckBoxSimulation.setEnabled(false);

		jTextUnits.setEditable(false);
		jTextInvestedAmount.setEditable(false);

		jTextAcctualProfit.setEditable(false);
		jTextAcctualRealProfit.setEditable(false);

		jTextUnits.addFocusListener(new SaveFocusListener());
		jTextInvestedAmount.addFocusListener(new SaveFocusListener());

	}

	public void setData(SfiData sfiData) throws Exception {
		setChangedFields.clear();

		this.sfiData = sfiData;

		PresentationSummaryData psDataSummary = Env.requestData(IPresentation.class).getSummary();
		jTextTotalInvestedAmount.setText(String.valueOf(psDataSummary.getTotalInvestedAmount()));
		jTextAcctualTotalInvestmentValue.setText(String.valueOf(psDataSummary.getAcctualTotalInvestmentValue()));

		BigDecimal profit = psDataSummary.getAcctualTotalInvestmentValue()
				.subtract(psDataSummary.getTotalInvestedAmount()).setScale(3, RoundingMode.FLOOR);
		jTextAcctualProfit.setText(String.valueOf(profit));
		jTextAcctualRealProfit.setText(String.valueOf(profit.multiply(FUCK_BELKA).setScale(3, RoundingMode.FLOOR)));
		if (profit.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal profitPercentage = profit.divide(psDataSummary.getTotalInvestedAmount(), RoundingMode.FLOOR)
					.multiply(new BigDecimal(100)).setScale(0, RoundingMode.FLOOR);
			jLabelAcctualRealProfitPercentage.setText("(" + String.valueOf(profitPercentage) + "%)");
		}

		if (sfiData != null) {
			investementData = Env.requestData(IInvestment.class).getBySfi(sfiData);

			jTextUnits.setEditable(true);
			jTextInvestedAmount.setEditable(true);
			jCheckBoxSimulation.setEnabled(true);

			if (investementData != null) {
				jTextUnits
						.setText(String.valueOf(investementData.getUnits() == null ? "" : investementData.getUnits()));
				jTextInvestedAmount.setText(String.valueOf(investementData.getInitialvalue() == null ? ""
						: investementData.getInitialvalue()));
				jCheckBoxSimulation.setSelected(investementData.isSimulation());
			} else {
				jTextUnits.setText("");
				jTextInvestedAmount.setText("");
				jCheckBoxSimulation.setSelected(false);
			}
		} else {
			investementData = null;
			jTextUnits.setEditable(false);
			jTextInvestedAmount.setEditable(false);
			jTextUnits.setText("");
			jTextInvestedAmount.setText("");
			jCheckBoxSimulation.setSelected(false);
		}

	}

	private boolean isContextSet() {
		return sfiData != null;
	}

	private class SaveFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

		}

		@Override
		public void focusLost(FocusEvent e) {
			try {
				if (isContextSet()) {
					setChangedFields.add((JTextField) e.getSource());

					if (setChangedFields.size() == 2) {
						if (investementData == null) {
							investementData = new InvestmentData();
							investementData.setSfi(sfiData);
						}

						BigDecimal investedAmount = null;
						BigDecimal units = null;
						if (!jTextInvestedAmount.getText().equals("") && !jTextUnits.getText().equals("")) {
							investedAmount = Util.getNumber(jTextInvestedAmount.getText());
							units = Util.getNumber(jTextUnits.getText());

							if (BigDecimal.ZERO.compareTo(investedAmount) == 0) {
								units = BigDecimal.ZERO;
							} else if (BigDecimal.ZERO.compareTo(units) == 0) {
								investedAmount = BigDecimal.ZERO;
							}
						}

						investementData.setInitialvalue(investedAmount);
						investementData.setUnits(units);
						investementData.setSimulation(jCheckBoxSimulation.isSelected());

						Env.requestData(IInvestment.class).update(investementData);

						refreshCallbackHanlder.refresh();

						setChangedFields.clear();
					}
				}
			} catch (Exception ex) {
				ExceptionHandler.exception(ex);
			}
		}
	}

}
