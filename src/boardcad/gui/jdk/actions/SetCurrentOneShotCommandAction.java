package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;

import boardcad.commands.BrdCommand;
import boardcad.gui.jdk.BoardCAD;

public class SetCurrentOneShotCommandAction extends SetCurrentCommandAction {
	static final long serialVersionUID = 1L;

	public SetCurrentOneShotCommandAction(final BrdCommand command) {
		super(command);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		mCommand.setPreviousCommand(BoardCAD.getInstance().getCurrentCommand());

		super.actionPerformed(event);
	}

}