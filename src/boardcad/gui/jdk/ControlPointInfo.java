package boardcad.gui.jdk;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cadcore.BezierKnot;
import cadcore.UnitUtils;
import boardcad.commands.BrdCommand;
import boardcad.commands.BrdEditCommand;
import boardcad.i18n.LanguageResource;
import boardcad.settings.BoardCADSettings;

public class ControlPointInfo extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JTextField mEndPointX = null;
	private JTextField mEndPointY = null;
	private JTextField mTangent1X = null;
	private JTextField mTangent1Y = null;
	private JTextField mTangent2X = null;
	private JTextField mTangent2Y = null;
	private JCheckBox mContinous = null;
	private JButton SetButton = null;
	private boolean mBlockActions = false;

	BrdEditCommand mCmd;

	private BezierKnot mControlPoint = null;
	private JButton setControlPointVerticalButton = null;
	private JButton setControlPointHorizontalButton = null;
	/**
	 * This is the default constructor
	 */
	public ControlPointInfo() {
		super();
		initialize();
	}

	public BrdEditCommand getCmd()
	{
		return mCmd;
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.insets = new Insets(3, 0, 3, 7);
		gridBagConstraints21.gridwidth = 1;
		gridBagConstraints21.ipadx = 0;
		gridBagConstraints21.gridy = 3;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridwidth = 1;
		gridBagConstraints11.ipadx = 0;
		gridBagConstraints11.insets = new Insets(3, 0, 3, 7);
		gridBagConstraints11.gridheight = 1;
		gridBagConstraints11.gridy = 2;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 2;
		gridBagConstraints9.ipadx = 6;
		gridBagConstraints9.gridwidth = 0;
		gridBagConstraints9.insets = new Insets(0, 13, 1, 13);
		gridBagConstraints9.fill = GridBagConstraints.NONE;
		gridBagConstraints9.ipady = 0;
		gridBagConstraints9.gridheight = 1;
		gridBagConstraints9.gridy = 4;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 1;
		gridBagConstraints8.insets = new Insets(0, 0, 0, 2);
		gridBagConstraints8.fill = GridBagConstraints.NONE;
		gridBagConstraints8.gridy = 4;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints7.gridy = 3;
		gridBagConstraints7.weightx = 0.0;
		gridBagConstraints7.ipadx = 0;
		gridBagConstraints7.insets = new Insets(0, 2, 0, 0);
		gridBagConstraints7.gridx = 2;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.gridy = 3;
		gridBagConstraints6.weightx = 0.0;
		gridBagConstraints6.ipadx = 0;
		gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints6.gridx = 1;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 2;
		gridBagConstraints5.weightx = 0.0;
		gridBagConstraints5.ipadx = 0;
		gridBagConstraints5.insets = new Insets(0, 2, 0, 0);
		gridBagConstraints5.gridx = 2;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.gridy = 2;
		gridBagConstraints4.weightx = 0.0;
		gridBagConstraints4.ipadx = 0;
		gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints4.gridx = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 0.0;
		gridBagConstraints3.ipadx = 0;
		gridBagConstraints3.ipady = 0;
		gridBagConstraints3.insets = new Insets(0, 2, 3, 0);
		gridBagConstraints3.gridwidth = 1;
		gridBagConstraints3.gridx = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.ipadx = 0;
		gridBagConstraints2.insets = new Insets(0, 0, 4, 0);
		gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.ipady = 0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 2;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints1.ipadx = 0;
		gridBagConstraints1.gridy = 0;
		jLabel1 = new JLabel();
		jLabel1.setText("Y");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("X");
		this.setSize(269, 148);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), LanguageResource.getString("CONTROLPOINTINFOTITLE_STR"), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 10), new Color(51, 51, 51)));
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(jLabel, gridBagConstraints);
		this.add(jLabel1, gridBagConstraints1);
		this.add(getEndPointX(), gridBagConstraints2);
		this.add(getEndPointY(), gridBagConstraints3);
		this.add(getTangent1X(), gridBagConstraints4);
		this.add(getTangent1Y(), gridBagConstraints5);
		this.add(getTangent2X(), gridBagConstraints6);
		this.add(getTangent2Y(), gridBagConstraints7);
		this.add(getContinous(), gridBagConstraints8);
		this.add(getSetButton(), gridBagConstraints9);
		this.add(getSetControlPointVerticalButton(), gridBagConstraints11);
		this.add(getSetControlPointHorizontalButton(), gridBagConstraints21);
		setColors();
	}

	void setColors()
	{

		BoardCADSettings settings = BoardCADSettings.getInstance();
		getEndPointX().setBackground(settings.getSelectedCenterControlPointColor());
		getEndPointY().setBackground(settings.getSelectedCenterControlPointColor());
		getTangent1X().setBackground(settings.getSelectedTangent1ControlPointColor());
		getTangent1Y().setBackground(settings.getSelectedTangent1ControlPointColor());
		getTangent2X().setBackground(settings.getSelectedTangent2ControlPointColor());
		getTangent2Y().setBackground(settings.getSelectedTangent2ControlPointColor());

	}

	/**
	 * This method initializes mEndPointX
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getEndPointX() {
		if (mEndPointX == null) {
			mEndPointX = new JTextField();
		}
		return mEndPointX;
	}

	/**
	 * This method initializes mEndPointY
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getEndPointY() {
		if (mEndPointY == null) {
			mEndPointY = new JTextField();
		}
		return mEndPointY;
	}

	/**
	 * This method initializes mTangent1X
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTangent1X() {
		if (mTangent1X == null) {
			mTangent1X = new JTextField();
		}
		return mTangent1X;
	}

	/**
	 * This method initializes mTangent1Y
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTangent1Y() {
		if (mTangent1Y == null) {
			mTangent1Y = new JTextField();
		}
		return mTangent1Y;
	}

	/**
	 * This method initializes mTangent2X
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTangent2X() {
		if (mTangent2X == null) {
			mTangent2X = new JTextField();
		}
		return mTangent2X;
	}

	/**
	 * This method initializes mTangent2Y
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTangent2Y() {
		if (mTangent2Y == null) {
			mTangent2Y = new JTextField();
		}
		return mTangent2Y;
	}

	/**
	 * This method initializes mContinous
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getContinous() {
		if (mContinous == null) {
			mContinous = new JCheckBox();
			mContinous.setText(LanguageResource.getString("CONTROLPOINTCONTINOUS_STR"));
			mContinous.setToolTipText(LanguageResource.getString("CONTROLPOINTCONTINOUSTOOLTIP_STR"));
			mContinous.addItemListener(new java.awt.event.ItemListener() {
				@Override
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if(mBlockActions)
						return;

					((BrdEditCommand)BoardCAD.getInstance().getCurrentCommand()).setContinous(BoardCAD.getInstance().getSelectedEdit(),   e.getStateChange() == java.awt.event.ItemEvent.SELECTED);
				}
			});
		}
		return mContinous;
	}

	/**
	 * This method initializes SetButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSetButton()
	{
		if (SetButton == null) {
			SetButton = new JButton();
			SetButton.setText(LanguageResource.getString("CONTROLPOINTSET_STR"));
			SetButton.setToolTipText(LanguageResource.getString("CONTROLPOINTSETTOOLTIP_STR"));
			SetButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(mBlockActions)
						return;

					((BrdEditCommand)BoardCAD.getInstance().getCurrentCommand()).setControlPoint(BoardCAD.getInstance().getSelectedEdit(), getValues());
				}
			});
		}
		return SetButton;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		jLabel.setEnabled(enabled);
		jLabel1.setEnabled(enabled);
		getEndPointX().setEnabled(enabled);
		getEndPointY().setEnabled(enabled);
		getTangent1X().setEnabled(enabled);
		getTangent1Y().setEnabled(enabled);
		getTangent2X().setEnabled(enabled);
		getTangent2Y().setEnabled(enabled);
		getContinous().setEnabled(enabled);
		getSetButton().setEnabled(enabled);
	}

	public void setWhich(int which)
	{
		getEndPointX().setEnabled((which==0)?true:false);
		getEndPointY().setEnabled((which==0)?true:false);
		getTangent1X().setEnabled((which==1)?true:false);
		getTangent1Y().setEnabled((which==1)?true:false);
		getTangent2X().setEnabled((which==2)?true:false);
		getTangent2Y().setEnabled((which==2)?true:false);
	}

	public void setControlPoint(BezierKnot ControlPoint)
	{
		mBlockActions = true;
		mControlPoint = ControlPoint;

		getEndPointX().setText(UnitUtils.convertLengthToCurrentUnit(mControlPoint.getEndPoint().x, false));
		getEndPointY().setText(UnitUtils.convertLengthToCurrentUnit(mControlPoint.getEndPoint().y, false));
		getTangent1X().setText(UnitUtils.convertLengthToCurrentUnit(mControlPoint.getTangentToPrev().x, false));
		getTangent1Y().setText(UnitUtils.convertLengthToCurrentUnit(mControlPoint.getTangentToPrev().y, false));
		getTangent2X().setText(UnitUtils.convertLengthToCurrentUnit(mControlPoint.getTangentToNext().x, false));
		getTangent2Y().setText(UnitUtils.convertLengthToCurrentUnit(mControlPoint.getTangentToNext().y, false));
		getContinous().setSelected(mControlPoint.isContinous());
		mBlockActions = false;
	}

	public Point2D.Double getValues()
	{
		Point2D.Double point = new Point2D.Double();

		String svx, svy;

		if(getEndPointX().isEnabled())
		{
			svx = getEndPointX().getText();
			svy = getEndPointY().getText();
		}
		else if(getTangent1X().isEnabled())
		{
			svx = getTangent1X().getText();
			svy = getTangent1Y().getText();
		}
		else //if(getTangent2X().isEnabled())
		{
			svx = getTangent2X().getText();
			svy = getTangent2Y().getText();
		}

		point.setLocation(UnitUtils.convertInputStringToInternalLengthUnit(svx), UnitUtils.convertInputStringToInternalLengthUnit(svy));

		return point;
	}

	boolean isEditing()
	{
		return (getEndPointX().hasFocus() ||
				getEndPointY().hasFocus() ||
				getTangent1X().hasFocus() ||
				getTangent1Y().hasFocus() ||
				getTangent2X().hasFocus() ||
				getTangent2Y().hasFocus() );
	}

	/**
	 * This method initializes setControlPointVerticalButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSetControlPointVerticalButton() {
		if (setControlPointVerticalButton == null) {
			setControlPointVerticalButton = new JButton();
			setControlPointVerticalButton.setText("|");
			setControlPointVerticalButton.setToolTipText(LanguageResource.getString("CONTROLPOINTVERTICALTOOLTIP_STR"));
			setControlPointVerticalButton.setMnemonic(KeyEvent.VK_UNDEFINED);
			setControlPointVerticalButton.setHorizontalTextPosition(SwingConstants.CENTER);
			setControlPointVerticalButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							BrdCommand cmd = BoardCAD.getInstance().getCurrentCommand();

							if(cmd.getClass().getSimpleName().compareTo("BrdEditCommand") == 0)
							{
								((BrdEditCommand)cmd).rotateControlPointToVertical(BoardCAD.getInstance().getSelectedEdit());

							}
						}
					});
		}
		return setControlPointVerticalButton;
	}

	/**
	 * This method initializes setControlPointHorizontalButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSetControlPointHorizontalButton() {
		if (setControlPointHorizontalButton == null) {
			setControlPointHorizontalButton = new JButton();
			setControlPointHorizontalButton.setText("__");
			setControlPointHorizontalButton.setToolTipText(LanguageResource.getString("CONTROLPOINTHORIZONTALTOOLTIP_STR"));
			setControlPointHorizontalButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							BrdCommand cmd = BoardCAD.getInstance().getCurrentCommand();

							if(cmd.getClass().getSimpleName().compareTo("BrdEditCommand") == 0)
							{
								((BrdEditCommand)cmd).rotateControlPointToHorizontal(BoardCAD.getInstance().getSelectedEdit());

							}
						}
					});
		}
		return setControlPointHorizontalButton;
	}

}  //  @jve:decl-index=0:visual-constraint="73,6"