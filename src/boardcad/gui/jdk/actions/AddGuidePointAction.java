package boardcad.gui.jdk.actions;


import javax.swing.Action;
import javax.swing.ImageIcon;

import boardcad.commands.BrdAddGuidePointCommand;
import boardcad.i18n.LanguageResource;

public final class AddGuidePointAction extends SetCurrentOneShotCommandAction {
	static final long serialVersionUID = 1L;
	public AddGuidePointAction()
	{
		super(new BrdAddGuidePointCommand());
		putValue(Action.NAME, LanguageResource.getString("ADDGUIDEPOINTBUTTON_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("ADDGUIDEPOINTBUTTON_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/add-guidepoint.png")));
	}
}