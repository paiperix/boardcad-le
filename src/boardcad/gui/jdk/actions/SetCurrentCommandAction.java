package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import boardcad.commands.BrdCommand;
import boardcad.gui.jdk.BoardCAD;

public class SetCurrentCommandAction extends AbstractAction {
	static final long serialVersionUID = 1L;
	BrdCommand mCommand;

	public SetCurrentCommandAction() {

	}

	public SetCurrentCommandAction(final BrdCommand command) {
		mCommand = command;
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		if (BoardCAD.getInstance().getCurrentCommand() != null) {
			BoardCAD.getInstance().getCurrentCommand().onCurrentChanged();
		}

		BoardCAD.getInstance().setCurrentCommand(mCommand);

		mCommand.onSetCurrent();

		BoardCAD.getInstance().getFrame().repaint();
	}

}