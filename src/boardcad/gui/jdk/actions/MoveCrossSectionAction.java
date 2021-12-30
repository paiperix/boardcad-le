package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import boardcad.commands.BrdMoveCrossSectionCommand;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;
import cadcore.UnitUtils;

public final class MoveCrossSectionAction extends AbstractAction {

	static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	public MoveCrossSectionAction() {
		putValue(Action.NAME, LanguageResource.getString("MOVECROSSECTION_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		boardCAD.setSelectedEdit(boardCAD.getCrossSectionEdit());

		double pos = 0.0f;
		String posStr = JOptionPane.showInputDialog(boardCAD.getFrame(),
				LanguageResource.getString("MOVECROSSECTIONMSG_STR"),
				LanguageResource.getString("MOVECROSSECTIONTITLE_STR"), JOptionPane.PLAIN_MESSAGE);

		if (posStr == null)
			return;

		pos = UnitUtils.convertInputStringToInternalLengthUnit(posStr);
		if (pos <= 0 || pos > boardCAD.getCurrentBrd().getLength()) {
			JOptionPane.showMessageDialog(boardCAD.getFrame(),
					LanguageResource.getString("MOVECROSSECTIONPOSITIONINVALIDMSG_STR"),
					LanguageResource.getString("MOVECROSSECTIONPOSITIONINVALIDTITLE_STR"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		BrdMoveCrossSectionCommand cmd = new BrdMoveCrossSectionCommand(boardCAD.getCrossSectionEdit(),
				boardCAD.getCrossSectionEdit().getCurrentBrd().getCurrentCrossSection(), pos);
		cmd.execute();

		boardCAD.getFrame().repaint();
	}
}