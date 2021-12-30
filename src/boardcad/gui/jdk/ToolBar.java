package boardcad.gui.jdk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boardcad.commands.BrdEditCommand;
import boardcad.commands.BrdPanCommand;
import boardcad.commands.BrdZoomCommand;
import boardcad.i18n.LanguageResource;
import boardcad.settings.BoardCADSettings;
import boardcad.gui.jdk.actions.BoardSaveAction;
import boardcad.gui.jdk.actions.DeleteControlPointAction;
import boardcad.gui.jdk.actions.FitViewAction;
import boardcad.gui.jdk.actions.AddControlPointAction;
import boardcad.gui.jdk.actions.AddGuidePointAction;
import boardcad.gui.jdk.actions.BoardLoadAction;
import boardcad.gui.jdk.actions.BoardNewAction;
import boardcad.gui.jdk.actions.PrintSpecSheetAction;
import boardcad.gui.jdk.actions.RedoAction;
import boardcad.gui.jdk.actions.SetCurrentCommandAction;
import boardcad.gui.jdk.actions.SetCurrentOneShotCommandAction;
import boardcad.gui.jdk.actions.ToggleDeckAndBottomAction;
import boardcad.gui.jdk.actions.UndoAction;
import cadcore.UnitUtils;

public class ToolBar extends JToolBar{
	
	private JToggleButton mLifeSizeButton;
	private JComboBox mUnitComboBox;
	
	public ToolBar()
	{
		super();
		init();
	}
	
	public void init() {
		BoardCAD boardCAD = BoardCAD.getInstance();
		
		BoardNewAction newBrd = new BoardNewAction();
		newBrd.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/new.png")));
		this.add(newBrd);

		final BoardLoadAction loadBrd = new BoardLoadAction(boardCAD.getCurrentBrd(), boardCAD.getOriginalBrd());
		loadBrd.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/open.png")));
		this.add(loadBrd);

		final AbstractAction saveBrd = new BoardSaveAction();
		saveBrd.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/save.png")));
		this.add(saveBrd);

		saveBrd.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/save-refresh.png")));
		this.add(saveBrd);

		PrintSpecSheetAction printSpecSheet = new PrintSpecSheetAction();
		printSpecSheet.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/print.png")));
		this.add(printSpecSheet);

		this.addSeparator();

		final SetCurrentOneShotCommandAction zoom = new SetCurrentOneShotCommandAction(new BrdZoomCommand());
		zoom.putValue(Action.NAME, LanguageResource.getString("ZOOMBUTTON_STR"));
		zoom.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("ZOOMBUTTON_STR"));
		zoom.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/zoom-in.png")));
		this.add(zoom);

		final AbstractAction fit = new FitViewAction();
		this.add(fit);

		mLifeSizeButton = new JToggleButton();
		mLifeSizeButton.setIcon(new ImageIcon(getClass().getResource("/boardcad/icons/zoom-1to1.png")));

		mLifeSizeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getID() == MouseEvent.MOUSE_CLICKED && e.getButton() == MouseEvent.BUTTON3) {
					BoardEdit edit = BoardCAD.getInstance().getSelectedEdit();
					if (edit != null) {
						edit.setCurrentAsLifeSizeScale();
					}
				}
			}

		});

		final ChangeListener lifeSizeChangeListner = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean isLifeSize = mLifeSizeButton.isSelected();
				BoardEdit edit = BoardCAD.getInstance().getSelectedEdit();
				if (edit != null) {
					if (edit.isLifeSize() == isLifeSize)
						return;

					edit.setLifeSize(isLifeSize);

					if (!isLifeSize)
						edit.resetToPreviousScale();

					edit.repaint();
				}

			}
		};

		mLifeSizeButton.addChangeListener(lifeSizeChangeListner);
		this.add(mLifeSizeButton);

		this.addSeparator();

		final SetCurrentCommandAction edit = new SetCurrentCommandAction(new BrdEditCommand());
		edit.putValue(Action.NAME, LanguageResource.getString("EDITBUTTON_STR"));
		edit.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("EDITBUTTON_STR"));
		edit.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/BoardCADedit24.gif")));
		this.add(edit);

		final SetCurrentCommandAction pan = new SetCurrentCommandAction(new BrdPanCommand());
		pan.putValue(Action.NAME, LanguageResource.getString("PANBUTTON_STR"));
		pan.putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("PANBUTTON_STR"));
		pan.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/boardcad/icons/BoardCADpan24.gif")));

		this.add(pan);

		this.addSeparator();

		final AbstractAction undo = new UndoAction();
		this.add(undo);
		
		final AbstractAction redo = new RedoAction();
		this.add(redo);

		this.addSeparator();

		final ToggleDeckAndBottomAction toggleDeckAndBottom = new ToggleDeckAndBottomAction();
		this.add(toggleDeckAndBottom);

		this.addSeparator();

		final AddGuidePointAction addGuidePoint = new AddGuidePointAction();
		this.add(addGuidePoint);

		final AddControlPointAction addControlPoint = new AddControlPointAction();
		this.add(addControlPoint);

		final DeleteControlPointAction deleteControlPoint = new DeleteControlPointAction();
		this.add(deleteControlPoint);

		this.addSeparator();
	
		BoardCADSettings settings = BoardCADSettings.getInstance();
		JLabel unitLabel = new JLabel(LanguageResource.getString("UNIT_STR"));
		unitLabel.setForeground(settings.getTextColor());
		this.add(unitLabel);

		String[] unitsStrList = new String[] { LanguageResource.getString("FEETINCHESRADIOBUTTON_STR"),
				LanguageResource.getString("DECIMALFEETINCHESRADIOBUTTON_STR"),
				LanguageResource.getString("MILLIMETERSRADIOBUTTON_STR"),
				LanguageResource.getString("CENTIMETERSRADIOBUTTON_STR"),
				LanguageResource.getString("METERSRADIOBUTTON_STR") };
		mUnitComboBox = new JComboBox(unitsStrList);
		
		mUnitComboBox.setForeground(settings.getTextColor());
		mUnitComboBox.setEditable(false);
		mUnitComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				mUnitComboBox.setForeground(settings.getTextColor());
				switch (cb.getSelectedIndex()) {
				default:
				case 0:
					boardCAD.setCurrentUnit(UnitUtils.INCHES);
					break;
				case 1:
					boardCAD.setCurrentUnit(UnitUtils.INCHES_DECIMAL);
					break;
				case 2:
					boardCAD.setCurrentUnit(UnitUtils.MILLIMETERS);
					break;
				case 3:
					boardCAD.setCurrentUnit(UnitUtils.CENTIMETERS);
					break;
				case 4:
					boardCAD.setCurrentUnit(UnitUtils.METERS);
					break;
				}
			}
		});
		this.addSeparator(new Dimension(5, 0));
		this.add(mUnitComboBox);
		mUnitComboBox.setMaximumSize(new Dimension(140, 30));
		
	}
	
	public void setLifeSize(boolean lifeSize) {
		mLifeSizeButton.setSelected(lifeSize);
	}

	public void setUnitSelection(int selection) {
		mUnitComboBox.setSelectedIndex(selection);
	}
}