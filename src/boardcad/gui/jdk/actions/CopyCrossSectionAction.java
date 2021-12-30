package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;
import cadcore.BezierBoardCrossSection;

public final class CopyCrossSectionAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public CopyCrossSectionAction() {
		putValue(Action.NAME, LanguageResource.getString("COPYCROSSECTION_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		boardCAD.setCrossSectionCopy((BezierBoardCrossSection)boardCAD.getCurrentBrd().getCurrentCrossSection().clone());
	}
}