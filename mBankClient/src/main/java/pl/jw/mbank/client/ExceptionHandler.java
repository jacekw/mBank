/**
 * 
 */
package pl.jw.mbank.client;

import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ExceptionHandler {

	public static void exception(Throwable ex) {
		if (ex instanceof SQLException) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
		} else if (ex instanceof ClassNotFoundException || ex instanceof InstantiationException) {
			JOptionPane.showMessageDialog(null, "Error while loading class:\n" + ex.getMessage(), "Engine error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		}

		ex.printStackTrace();
	}
}