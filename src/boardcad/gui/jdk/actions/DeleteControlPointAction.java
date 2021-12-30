package boardcad.gui.jdk.actions;


import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import boardcad.commands.BrdDeleteControlPointCommand;
import boardcad.commands.BrdMacroCommand;
import boardcad.gui.jdk.BoardCAD;
import boardcad.gui.jdk.BoardEdit;
import boardcad.i18n.LanguageResource;
import cadcore.BezierKnot;
import cadcore.BezierSpline;

public final class DeleteControlPointAction extends SetCurrentCommandAction {
	static final long serialVersionUID = 1L;
	public DeleteControlPointAction()
	{
		super();
		putValue(Action.NAME, LanguageResource.getString("DELETECONTROLPOINTSBUTTON_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("DELETECONTROLPOINTSBUTTON_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/remove-controlpoint.png")));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BoardEdit edit = BoardCAD.getInstance().getSelectedEdit();
		if (edit == null)
			return;
		ArrayList<BezierKnot> selectedControlPoints = edit.getSelectedControlPoints();

		if (selectedControlPoints.size() == 0) {
			JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(),
					LanguageResource.getString("NOCONTROLPOINTSELECTEDMSG_STR"),
					LanguageResource.getString("NOCONTROLPOINTSELECTEDTITLE_STR"), JOptionPane.WARNING_MESSAGE);
			return;
		}

		int selection = JOptionPane.showConfirmDialog(BoardCAD.getInstance().getFrame(),
				LanguageResource.getString("DELETECONTROLPOINTSMSG_STR"),
				LanguageResource.getString("DELETECONTROLPOINTSTITLE_STR"), JOptionPane.WARNING_MESSAGE,
				JOptionPane.YES_NO_OPTION);

		if (selection != JOptionPane.YES_OPTION) {
			return;

		}

		BrdMacroCommand macroCmd = new BrdMacroCommand();
		macroCmd.setSource(edit);
		BezierSpline[] splines = edit.getActiveBezierSplines(edit.getCurrentBrd());

		for (int j = 0; j < splines.length; j++) {
			for (int i = 0; i < selectedControlPoints.size(); i++) {
				BezierKnot ControlPoint = selectedControlPoints.get(i);

				if (ControlPoint == splines[j].getControlPoint(0)
						|| ControlPoint == splines[j].getControlPoint(splines[j].getNrOfControlPoints() - 1)) {
					continue;
				}

				BrdDeleteControlPointCommand deleteControlPointCommand = new BrdDeleteControlPointCommand(edit,
						ControlPoint, splines[j]);

				macroCmd.add(deleteControlPointCommand);

			}
		}

		macroCmd.execute();

		BoardCAD.getInstance().getFrame().repaint();
	}

}

