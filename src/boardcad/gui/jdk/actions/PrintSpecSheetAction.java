package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import boardcad.i18n.LanguageResource;
import boardcad.print.PrintSpecSheet;

public final class PrintSpecSheetAction extends AbstractAction {
	private PrintSpecSheet mPrintSpecSheet;
	static final long serialVersionUID = 1L;
	public PrintSpecSheetAction(){
		super();
		mPrintSpecSheet = new PrintSpecSheet();
		this.putValue(Action.NAME, LanguageResource.getString("PRINTSPECSHEET_STR"));
		this.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("PRINTSPECSHEET_STR"));
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		mPrintSpecSheet.printSpecSheet();
	}
}