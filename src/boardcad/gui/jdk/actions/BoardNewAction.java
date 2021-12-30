package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import board.readers.BrdReader;
import boardcad.DefaultBrds;
import boardcad.commands.BrdCommandHistory;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

final public class BoardNewAction extends AbstractAction {
	static final long serialVersionUID = 1L;

	public BoardNewAction() {
		super();
		
		putValue(Action.NAME, LanguageResource.getString("NEWBOARD_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("NEWBOARD_STR"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();

		String str = (String) JOptionPane.showInputDialog(boardCAD.getFrame(), LanguageResource.getString("NEWBOARDMSG_STR"),
				LanguageResource.getString("NEWBOARDTITLE_STR"), JOptionPane.PLAIN_MESSAGE, null,
				DefaultBrds.getInstance().getDefaultBoardsList(),
				DefaultBrds.getInstance().getDefaultBoardsList()[0]);

		if (str == null)
			return;

		BrdReader.loadFile(boardCAD.getCurrentBrd(), DefaultBrds.getInstance().getBoardArray(str), str);
		boardCAD.getOriginalBrd().set(boardCAD.getCurrentBrd());
		boardCAD.fitAll();
		boardCAD.onBrdChanged();
		boardCAD.onControlPointChanged();

		BrdCommandHistory.getInstance().clear();
		boardCAD.getFrame().repaint();
		boardCAD.updateBezier3DModel();
	}
}