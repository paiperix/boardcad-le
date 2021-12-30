package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class ToggleDeckAndBottomAction extends SetCurrentCommandAction {
	static final long serialVersionUID = 1L;
	public ToggleDeckAndBottomAction()
	{
		super();
		putValue(Action.NAME, LanguageResource.getString("TOGGLEDECKBOTTOMBUTTON_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("TOGGLEDECKBOTTOMBUTTON_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/BoardCADtoggle24x35.png")));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, 0));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BoardCAD.getInstance().toggleEditBottomOrDeck();
	}
}