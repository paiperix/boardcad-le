package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class PreviousCrossSectionAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public PreviousCrossSectionAction() {
		putValue(Action.NAME, LanguageResource.getString("PREVIOUSCROSSECTION_STR"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		if (boardCAD.isGhostMode()) {
			boardCAD.getGhostBrd().previousCrossSection();
		} else if (boardCAD.isOrgFocus()) {
			boardCAD.getOriginalBrd().previousCrossSection();
		} else {
			boardCAD.getCurrentBrd().previousCrossSection();
		}
		boardCAD.getFrame().repaint();
	}
}