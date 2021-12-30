package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import boardcad.commands.BrdCommandHistory;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public class UndoAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	public UndoAction()
	{
		super();
		putValue(Action.NAME, LanguageResource.getString("UNDO_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("UNDO_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/edit-undo.png")));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BrdCommandHistory.getInstance().undo();
		BoardCAD.getInstance().getFrame().repaint();
	}
}