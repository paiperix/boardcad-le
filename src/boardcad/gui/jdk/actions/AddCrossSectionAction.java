package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import boardcad.commands.BrdAddCrossSectionCommand;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;
import cadcore.UnitUtils;

public final class AddCrossSectionAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public AddCrossSectionAction() {
		putValue(Action.NAME, LanguageResource.getString("ADDCROSSECTION_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		boardCAD.setSelectedEdit(boardCAD.getCrossSectionEdit());

		double pos = 0.0f;
		String posStr = JOptionPane.showInputDialog(boardCAD.getFrame(), LanguageResource.getString("ADDCROSSECTIONMSG_STR"),
				LanguageResource.getString("ADDCROSSECTIONTITLE_STR"), JOptionPane.PLAIN_MESSAGE);

		if (posStr == null)
			return;

		pos = UnitUtils.convertInputStringToInternalLengthUnit(posStr);
		if (pos <= 0 || pos > boardCAD.getCurrentBrd().getLength()) {
			JOptionPane.showMessageDialog(boardCAD.getFrame(),
					LanguageResource.getString("ADDCROSSECTIONPOSITIONINVALIDMSG_STR"),
					LanguageResource.getString("ADDCROSSECTIONPOSITIONINVALIDTITLE_STR"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		BrdAddCrossSectionCommand cmd = new BrdAddCrossSectionCommand(boardCAD.getCrossSectionEdit(), pos);
		cmd.execute();

		boardCAD.getFrame().repaint();
	}
}