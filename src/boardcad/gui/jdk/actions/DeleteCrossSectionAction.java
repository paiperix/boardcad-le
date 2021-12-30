package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import boardcad.commands.BrdRemoveCrossSectionCommand;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class DeleteCrossSectionAction extends AbstractAction {
	/**
	 * 
	 */
	public DeleteCrossSectionAction() {
	}

	static final long serialVersionUID = 1L;
	{
		this.putValue(Action.NAME, LanguageResource.getString("REMOVECROSSECTION_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		boardCAD.setSelectedEdit(boardCAD.getCrossSectionEdit());

		if (boardCAD.getCrossSectionEdit().getCurrentBrd().getCrossSections().size() <= 3) {
			JOptionPane.showMessageDialog(boardCAD.getFrame(),
					LanguageResource.getString("REMOVECROSSECTIONDELETELASTERRORMSG_STR"),
					LanguageResource.getString("REMOVECROSSECTIONDELETELASTERRORTITLE_STR"),
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		BrdRemoveCrossSectionCommand cmd = new BrdRemoveCrossSectionCommand(boardCAD.getCrossSectionEdit(),
				boardCAD.getCrossSectionEdit().getCurrentBrd().getCurrentCrossSection());
		cmd.execute();

		boardCAD.getFrame().repaint();
	}
}