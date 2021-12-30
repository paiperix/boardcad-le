package boardcad.gui.jdk.actions;


import javax.swing.Action;
import javax.swing.ImageIcon;

import boardcad.commands.BrdAddControlPointCommand;
import boardcad.i18n.LanguageResource;

public final class AddControlPointAction extends SetCurrentOneShotCommandAction {
	static final long serialVersionUID = 1L;
	public AddControlPointAction()
	{
		super(new BrdAddControlPointCommand());
		putValue(Action.NAME, LanguageResource.getString("ADDCONTROLPOINTBUTTON_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("ADDCONTROLPOINTBUTTON_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/add-controlpoint.png")));
	}
}