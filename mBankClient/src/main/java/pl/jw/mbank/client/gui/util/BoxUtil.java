package pl.jw.mbank.client.gui.util;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSeparator;

public class BoxUtil {
	public static final Dimension PREFERRED_SIZE = new Dimension(90, 20);
	public static final Dimension PREFERRED_SIZE_LABEL = new Dimension(120, 20);
	public static final Dimension RIGID = new Dimension(5, 5);

	public static Box getBox(Component... jComponent) {
		Box b = new Box(BoxLayout.X_AXIS);

		for (Component c : jComponent) {
			b.add(Box.createRigidArea(BoxUtil.RIGID));
			b.add(c);
			b.add(Box.createRigidArea(BoxUtil.RIGID));
		}

		return b;
	}

	public static Box getLabelBox(JLabel jLabel, JComponent... jTextField) {
		Box b = new Box(BoxLayout.X_AXIS);
		b.add(Box.createRigidArea(BoxUtil.RIGID));
		b.add(jLabel);

		for (JComponent jComponent : jTextField) {
			b.add(Box.createRigidArea(BoxUtil.RIGID));
			b.add(jComponent);
			b.add(Box.createRigidArea(BoxUtil.RIGID));
		}

		b.add(Box.createHorizontalGlue());
		return b;
	}

	public static JSeparator getJSeparator() {
		JSeparator js = new JSeparator(JSeparator.HORIZONTAL);
		js.setMaximumSize(new Dimension(Short.MAX_VALUE, 10));
		js.setPreferredSize(new Dimension(50, 10));
		js.setMinimumSize(new Dimension(Short.MAX_VALUE, 10));
		return js;
	}

	public static void setSize(JComponent jComponent, Dimension size) {
		jComponent.setPreferredSize(size);
		jComponent.setMaximumSize(size);
		jComponent.setMinimumSize(size);
	}
}
