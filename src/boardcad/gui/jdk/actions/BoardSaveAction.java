package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class BoardSaveAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	
	public BoardSaveAction()
	{
		super();
		this.putValue(Action.NAME, LanguageResource.getString("BOARDSAVE_STR"));
		this.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("BOARDSAVE_STR"));
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BoardCAD boardCAD = BoardCAD.getInstance();

		boardCAD.saveAs(boardCAD.getCurrentBrd().getFilename());

		boardCAD.setBoardChanged(false);
	}
}