package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public class FitViewAction extends AbstractAction {
	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	
	public FitViewAction(){
		super();
		putValue(Action.NAME, LanguageResource.getString("FITBUTTON_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("FITBUTTON_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/zoom-fit-best.png")));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BoardCAD boardCAD = BoardCAD.getInstance();
		boardCAD.fitAll();
		boardCAD.getFrame().repaint();
	}
}