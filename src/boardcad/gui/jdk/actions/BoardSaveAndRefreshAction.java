package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import board.BezierBoard;
import board.writers.BrdWriter;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class BoardSaveAndRefreshAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	{
		this.putValue(Action.NAME, LanguageResource.getString("BOARDSAVEANDREFRESH_STR"));
		this.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("BOARDSAVEANDREFRESH_STR"));
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		BezierBoard current = BoardCAD.getInstance().getCurrentBrd();

		BrdWriter.saveFile(current, current.getFilename());

		boardCAD.setOriginalBrd((BezierBoard)current.clone());

		boardCAD.setBoardChanged(false);
	}
}