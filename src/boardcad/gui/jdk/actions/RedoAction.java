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

public class RedoAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	public RedoAction()
	{
		super();
		this.putValue(Action.NAME, LanguageResource.getString("REDO_STR"));
		this.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("REDO_STR"));
		this.putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("/boardcad/icons/edit-redo.png")));

		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BrdCommandHistory.getInstance().redo();
		BoardCAD.getInstance().getFrame().repaint();
	}
}