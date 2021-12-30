package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import boardcad.commands.BrdPasteCrossSectionCommand;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class PasteCrossSectionAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public PasteCrossSectionAction() {
		putValue(Action.NAME, LanguageResource.getString("PASTECROSSECTION_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		if (boardCAD.getCrossSectionCopy() == null)
			return;

		boardCAD.setSelectedEdit(boardCAD.getCrossSectionEdit());

		BrdPasteCrossSectionCommand cmd = new BrdPasteCrossSectionCommand(boardCAD.getCrossSectionEdit(),
				boardCAD.getCurrentBrd().getCurrentCrossSection(), boardCAD.getCrossSectionCopy());
		cmd.execute();

		boardCAD.getFrame().repaint();
	}
}