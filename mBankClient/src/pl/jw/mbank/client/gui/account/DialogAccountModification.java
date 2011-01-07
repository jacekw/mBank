package pl.jw.mbank.client.gui.account;

import static pl.jw.mbank.client.gui.util.BoxUtil.PREFERRED_SIZE;
import static pl.jw.mbank.client.gui.util.BoxUtil.PREFERRED_SIZE_LABEL;
import static pl.jw.mbank.client.gui.util.BoxUtil.RIGID;
import static pl.jw.mbank.client.gui.util.BoxUtil.getLabelBox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import pl.jw.mbank.client.gui.util.BoxUtil;
import pl.jw.mbank.common.dto.AccountData;

public class DialogAccountModification extends JDialog {

	private final JPanel jPanel = new JPanel();

	private final JLabel jLabelName = new JLabel("Name:");
	private final JTextField jTextName = new JTextField();

	private final JButton jButtonOk = new JButton("Ok");

	private boolean isAcceptance = false;

	public DialogAccountModification() {
		setModal(true);
		init();

		pack();
	}

	private void init() {
		setLayout(new BorderLayout());

		jButtonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				accept();

			}

		});
		BoxUtil.setSize(jButtonOk, new Dimension(60, 20));
		BoxUtil.setSize(jLabelName, PREFERRED_SIZE_LABEL);

		BoxUtil.setSize(jTextName, PREFERRED_SIZE);

		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.setBorder(new EtchedBorder());
		jPanel.add(Box.createRigidArea(RIGID));
		jPanel.add(getLabelBox(jLabelName, jTextName));
		jPanel.add(Box.createRigidArea(RIGID));

		jPanel.add(new JSeparator());
		jPanel.add(Box.createRigidArea(RIGID));
		jPanel.add(Box.createVerticalGlue());
		jPanel.add(BoxUtil.getBox(Box.createHorizontalGlue(), jButtonOk));

		setContentPane(jPanel);
	}

	public boolean isAcceptance() {
		return isAcceptance;
	}

	public AccountData getAccountData() {
		AccountData ad = new AccountData();
		ad.setName(jTextName.getText());

		return ad;
	}

	private void accept() {

		if (jTextName.getText() == null || jTextName.getText().equals("")) {
			throw new RuntimeException("Account name not given.");
		}

		isAcceptance = true;

		dispose();
	}
}
