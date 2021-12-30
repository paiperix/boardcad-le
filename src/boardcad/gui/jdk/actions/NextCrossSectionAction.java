package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class NextCrossSectionAction extends AbstractAction {
	/**
	 * 
	 */

	public NextCrossSectionAction() {
		putValue(Action.NAME, LanguageResource.getString("NEXTCROSSECTION_STR"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		if (boardCAD.isGhostMode()) {
			boardCAD.getGhostBrd().nextCrossSection();
		} else if (boardCAD.isOrgFocus()) {
			boardCAD.getOriginalBrd().nextCrossSection();
		} else {
			boardCAD.getCurrentBrd().nextCrossSection();
		}
		boardCAD.getFrame().repaint();
	}
}