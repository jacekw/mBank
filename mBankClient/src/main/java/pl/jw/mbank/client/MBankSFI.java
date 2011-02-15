/**
 * 
 */
package pl.jw.mbank.client;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import pl.jw.mbank.client.gui.PanelMain;

/**
 * @author jacek
 * 
 */
public class MBankSFI extends SingleFrameApplication {

	private Exception delayedException = null;

	private PanelMain panelMBankSFI;

	@Override
	protected void initialize(String[] args) {
		try {
			addExitListener(new HideListener());

			panelMBankSFI = new PanelMain(getMainFrame());

		} catch (Exception e) {
			delayedException = e;
		}
	}

	@Override
	protected void startup() {
		try {
			if (delayedException != null)
				throw new Exception("Delayed exception.\n" + delayedException.getMessage(), delayedException);

			show(panelMBankSFI);
		} catch (Exception e) {
			ExceptionHandler.exception(e);
		} finally {
			super.shutdown();
		}
	}

	@Override
	protected void ready() {

	}

	@Override
	protected void shutdown() {
		try {

		} catch (Exception e) {
			ExceptionHandler.exception(e);
		} finally {
			Env.CONTEXT.save();
			super.shutdown();
		}
	}

	private class HideListener implements ExitListener {
		@Override
		public boolean canExit(EventObject event) {
			boolean canExit = true;

			if (event != null) {
				if (event instanceof ActionEvent) {
					// from system tray -> close
				} else if (event instanceof WindowEvent) {
					// from frame's close button -> hide
					if (getMainFrame().isVisible()) {
						getMainFrame().setVisible(false);
						canExit = false;
					}
				}
			}
			return canExit;
		}

		@Override
		public void willExit(EventObject event) {
		}
	}

	public static void main(String[] args) {
		try {
			Application.launch(MBankSFI.class, null);
		} catch (Exception e) {
			ExceptionHandler.exception(e);
			shutdownApplication();
		}
	}

	public static void shutdownApplication() {
		Application.getInstance().exit();
	}

}