package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public class GuidePointsAction extends AbstractAction {
	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	
	public GuidePointsAction(){
		super();
		putValue(Action.NAME, LanguageResource.getString("GUIDEPOINTS_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		BoardCAD.getInstance().showGuidePointsDialog();
	}

}
