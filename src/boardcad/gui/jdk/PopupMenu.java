package boardcad.gui.jdk;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boardcad.commands.BrdFitCurveCommand;
import boardcad.commands.BrdSpotCheckCommand;
import boardcad.gui.jdk.actions.AddControlPointAction;
import boardcad.gui.jdk.actions.AddCrossSectionAction;
import boardcad.gui.jdk.actions.AddGuidePointAction;
import boardcad.gui.jdk.actions.CopyCrossSectionAction;
import boardcad.gui.jdk.actions.DeleteControlPointAction;
import boardcad.gui.jdk.actions.DeleteCrossSectionAction;
import boardcad.gui.jdk.actions.GuidePointsAction;
import boardcad.gui.jdk.actions.MoveCrossSectionAction;
import boardcad.gui.jdk.actions.NextCrossSectionAction;
import boardcad.gui.jdk.actions.PasteCrossSectionAction;
import boardcad.gui.jdk.actions.PreviousCrossSectionAction;
import boardcad.gui.jdk.actions.SetCurrentCommandAction;
import boardcad.gui.jdk.actions.ToggleDeckAndBottomAction;
import boardcad.i18n.LanguageResource;

public class PopupMenu extends JPopupMenu {

	public PopupMenu() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		final JButton spotCheck = new JButton();
		spotCheck.setText(LanguageResource.getString("SPOTCHECKBUTTON_STR"));
		final ChangeListener spotCheckChangeListner = new ChangeListener() {
			BrdSpotCheckCommand cmd = new BrdSpotCheckCommand();

			boolean mIsSpotChecking = false;

			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = ((JButton) e.getSource()).getModel();
				if (model.isPressed()) {
					cmd.spotCheck();
					mIsSpotChecking = true;
				} else if (mIsSpotChecking == true) {
					cmd.restore();
					mIsSpotChecking = false;
				}

			}
		};
		spotCheck.addChangeListener(spotCheckChangeListner);	
		add(spotCheck);
		
		ToggleDeckAndBottomAction toggleDeckAndBottom = new ToggleDeckAndBottomAction();
		add(toggleDeckAndBottom);

		final AddGuidePointAction addGuidePoint = new AddGuidePointAction();
		add(addGuidePoint);
		
		final GuidePointsAction guidePoints = new GuidePointsAction();
		add(guidePoints);
		
		final AddControlPointAction addControlPoint = new AddControlPointAction();
		add(addControlPoint);
		
		final DeleteControlPointAction deleteControlPoint = new DeleteControlPointAction();
		add(deleteControlPoint);
		

		final SetCurrentCommandAction fitCurveCmd = new SetCurrentCommandAction() {
			static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {

				BoardEdit edit = BoardCAD.getInstance().getSelectedEdit();
				if (edit == null)
					return;

				BrdFitCurveCommand cmd = new BrdFitCurveCommand();
				cmd.execute();
			};

		};
		fitCurveCmd.putValue(Action.NAME, LanguageResource.getString("FITCONTROLPOINTS_STR"));
		fitCurveCmd.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("FITCONTROLPOINTS_STR"));
		add(fitCurveCmd);

		addSeparator();

		final JMenu crossSectionsForPopupMenu = new JMenu(LanguageResource.getString("CROSSECTIONSMENU_STR"));
		AbstractAction nextCrossSection = new NextCrossSectionAction();
		crossSectionsForPopupMenu.add(nextCrossSection);
		AbstractAction prevCrossSection = new PreviousCrossSectionAction();
		crossSectionsForPopupMenu.add(prevCrossSection);
		AbstractAction addCrossSection = new AddCrossSectionAction();
		crossSectionsForPopupMenu.add(addCrossSection);
		AbstractAction moveCrossSection = new MoveCrossSectionAction();
		crossSectionsForPopupMenu.add(moveCrossSection);
		AbstractAction deleteCrossSection = new DeleteCrossSectionAction();
		crossSectionsForPopupMenu.add(deleteCrossSection);
		AbstractAction copyCrossSection = new CopyCrossSectionAction();
		crossSectionsForPopupMenu.add(copyCrossSection);
		final AbstractAction pasteCrossSection = new PasteCrossSectionAction();
		crossSectionsForPopupMenu.add(pasteCrossSection);
		add(crossSectionsForPopupMenu);

	}

}
