package boardcad.settings;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;
import boardcad.settings.Settings.Enumeration;
import cadcore.UnitUtils;

public class BoardCADSettings extends CategorizedSettings
{
	static public final String RENDERBACKGROUNDCOLOR = "renderbackgroundcolor";

	static private final String TEXTCOLOR = "textcolor";
	static private final String BACKGROUNDCOLOR = "backgroundcolor";
	static private final String STRINGERCOLOR = "stringercolor";
	static private final String FLOWLINESCOLOR = "flowlinescolor";
	static private final String APEXLINECOLOR = "apexlinecolor";
	static private final String TUCKUNDERLINECOLOR = "tuckunderlinecolor";
	static private final String CENTERLINECOLOR = "centerlinecolor";
	static private final String BRDCOLOR = "brdcolor";
	static private final String ORIGINALCOLOR = "originalcolor";
	static private final String GHOSTCOLOR = "ghostcolor";
	static private final String BLANKCOLOR = "blankcolor";
	static private final String GRIDCOLOR = "gridcolor";
	static private final String FINSCOLOR = "finscolor";
	static private final String CURVATURECOLOR = "curvaturecolor";
	static private final String VOLUMEDISTRIBUTIONCOLOR = "volumedistributioncolor";
	static private final String CENTEROFMASSCOLOR = "centerofmasscolor";
	static private final String SELECTEDTANGENTCOLOR = "selectedtangentcolor";
	static private final String SELECTEDCONTROLPOINTCENTERCOLOR = "selectedcontrolpointcentercolor";
	static private final String SELECTEDCONTROLPOINTTANGENT1COLOR = "selectedcontrolpointtangent1color";
	static private final String SELECTEDCONTROLPOINTTANGENT2COLOR = "selectedcontrolpointtangent2color";
	static private final String SELECTEDCONTROLPOINTCENTEROUTLINECOLOR = "selectedcontrolpointcenteroutlinecolor";
	static private final String SELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR = "selectedcontrolpointtangent1outlinecolor";
	static private final String SELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR = "selectedcontrolpointtangent2outlinecolor";
	static private final String UNSELECTEDTANGENTCOLOR = "unselectedtangentcolor";
	static private final String UNSELECTEDCONTROLPOINTCENTERCOLOR = "unselectedcontrolpointcentercolor";
	static private final String UNSELECTEDCONTROLPOINTTANGENT1COLOR = "unselectedcontrolpointtangent1color";
	static private final String UNSELECTEDCONTROLPOINTTANGENT2COLOR = "unselectedcontrolpointtangent2color";
	static private final String UNSELECTEDCONTROLPOINTCENTEROUTLINECOLOR = "unselectedcontrolpointcenteroutlinecolor";
	static private final String UNSELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR = "unselectedcontrolpointtangent1outlinecolor";
	static private final String UNSELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR = "unselectedcontrolpointtangent2outlinecolor";
	static private final String UNSELECTEDBACKGROUNDCOLOR = "unselectedbackgroundcolor";
	static private final String GUIDEPOINTCOLOR = "guidepointcolor";

	static private final String SELECTEDCONTROLPOINTSIZE = "selectedcontrolpointsize";
	static private final String SELECTEDCONTROLPOINTOUTLINETHICKNESS = "selectedctrlpntoutlinethickness";
	static private final String UNSELECTEDCONTROLPOINTSIZE = "unselectedcontrolpointsize";
	static private final String UNSELECTEDCONTROLPOINTOUTLINETHICKNESS = "unselectedctrlpntoutlinethickness";

	static private final String BEZIERTHICKNESS = "bezierthickness";
	static private final String CURVATURETHICKNESS = "curvaturethickness";
	static private final String VOLUMEDISTRIBUTIONTHICKNESS = "volumedistributionthickness";
	static private final String GUIDEPNTTHICKNESS = "guidepntthickness";
	static private final String BASELINETHICKNESS = "baselinethickness";
	static private final String BASELINECOLOR = "baselinecolor";
	static private final String LOOK_AND_FEEL = "lookandfeel";
	static private final String PRINTGUIDEPOINTS = "printguidepoints";
	static private final String PRINTFINS = "printfins";
	static private final String FRACTIONACCURACY = "fractionaccuracy";
	static private final String ROCKERSTICK = "rockerstick";
	static private final String OFFSETINTERPLOATION = "offsetinterpolation";
	static private final String NUM3DPROCESSES = "num3Dprocesses";


	static private BoardCADSettings mInstance = null;

	private Settings mColorSettings;
	private Settings mSizeSettings;
	private Settings mMiscSettings;

	public static BoardCADSettings getInstance()
	{
		if(mInstance == null) {
			mInstance = new BoardCADSettings();
		}
		return mInstance;
	}

	protected BoardCADSettings()
	{
		super();
		Settings.SettingChangedCallback defaultCallback = new Settings.SettingChangedCallback()
		{
			public void onSettingChanged(final Setting setting) {
				BoardCAD.getInstance().onSettingsChanged(setting);
			}

		};
		setDefaultCallback(defaultCallback);

		mColorSettings = this.addCategory(LanguageResource.getString("COLORS_STR"));

		mColorSettings.addColor(TEXTCOLOR, new Color(0, 0, 0), LanguageResource.getString("TEXTCOLOR_STR"));
		mColorSettings.addColor(BACKGROUNDCOLOR, new Color(200, 200, 240),
				LanguageResource.getString("BACKGROUNDCOLOR_STR"));
		mColorSettings.addColor(UNSELECTEDBACKGROUNDCOLOR, new Color(220, 220, 245),
				LanguageResource.getString("UNSELECTEDBACKGROUNDCOLOR_STR"));
		mColorSettings.addColor(RENDERBACKGROUNDCOLOR, new Color(100, 120, 245),
				LanguageResource.getString("RENDERBACKGROUNDCOLOR_STR"));
		mColorSettings.addColor(STRINGERCOLOR, new Color(100, 100, 100),
				LanguageResource.getString("STRINGERCOLOR_STR"));
		mColorSettings.addColor(FLOWLINESCOLOR, new Color(100, 150, 100),
				LanguageResource.getString("FLOWLINESCOLOR_STR"));
		mColorSettings.addColor(APEXLINECOLOR, new Color(80, 80, 200), LanguageResource.getString("APEXLINECOLOR_STR"));
		mColorSettings.addColor(TUCKUNDERLINECOLOR, new Color(150, 50, 50),
				LanguageResource.getString("TUCKUNDERCOLOR_STR"));
		mColorSettings.addColor(CENTERLINECOLOR, new Color(180, 180, 220),
				LanguageResource.getString("CENTERLINECOLOR_STR"));

		mColorSettings.addColor(BRDCOLOR, new Color(0, 0, 0), LanguageResource.getString("BRDCOLOR_STR"));
		mColorSettings.addColor(ORIGINALCOLOR, new Color(240, 240, 240),
				LanguageResource.getString("ORIGINALCOLOR_STR"));
		mColorSettings.addColor(GHOSTCOLOR, new Color(128, 128, 128), LanguageResource.getString("GHOSTCOLOR_STR"));
		mColorSettings.addColor(BLANKCOLOR, new Color(128, 128, 128), LanguageResource.getString("BLANKCOLOR_STR"));
		mColorSettings.addColor(GRIDCOLOR, new Color(128, 128, 128), LanguageResource.getString("GRIDCOLOR_STR"));
		mColorSettings.addColor(FINSCOLOR, new Color(205, 128, 128), LanguageResource.getString("FINSCOLOR_STR"));
		mColorSettings.addColor(CURVATURECOLOR, new Color(130, 130, 180),
				LanguageResource.getString("CURVATURECOLOR_STR"));
		mColorSettings.addColor(VOLUMEDISTRIBUTIONCOLOR, new Color(80, 80, 80),
				LanguageResource.getString("VOLUMEDISTRIBUTIONCOLOR_STR"));
		mColorSettings.addColor(CENTEROFMASSCOLOR, new Color(205, 10, 10),
				LanguageResource.getString("CENTEROFMASSCOLOR_STR"));
		mColorSettings.addColor(SELECTEDTANGENTCOLOR, new Color(0, 0, 0),
				LanguageResource.getString("SELECTEDTANGENTCOLOR_STR"));
		mColorSettings.addColor(SELECTEDCONTROLPOINTCENTERCOLOR, new Color(30, 30, 200),
				LanguageResource.getString("SELECTEDCONTROLPOINTCENTERCOLOR_STR"));
		mColorSettings.addColor(SELECTEDCONTROLPOINTTANGENT1COLOR, new Color(200, 200, 0),
				LanguageResource.getString("SELECTEDCONTROLPOINTTANGENT1COLOR_STR"));
		mColorSettings.addColor(SELECTEDCONTROLPOINTTANGENT2COLOR, new Color(200, 0, 0),
				LanguageResource.getString("SELECTEDCONTROLPOINTTANGENT2COLOR_STR"));

		mColorSettings.addColor(SELECTEDCONTROLPOINTCENTEROUTLINECOLOR, new Color(0, 0, 0),
				LanguageResource.getString("SELECTEDCONTROLPOINTCENTEROUTLINECOLOR_STR"));
		mColorSettings.addColor(SELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR, new Color(0, 0, 0),
				LanguageResource.getString("SELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR_STR"));
		mColorSettings.addColor(SELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR, new Color(0, 0, 0),
				LanguageResource.getString("SELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR_STR"));

		mColorSettings.addColor(UNSELECTEDTANGENTCOLOR, new Color(100, 100, 100),
				LanguageResource.getString("UNSELECTEDTANGENTCOLOR_STR"));
		mColorSettings.addColor(UNSELECTEDCONTROLPOINTCENTERCOLOR, new Color(200, 200, 240),
				LanguageResource.getString("UNSELECTEDCONTROLPOINTCENTERCOLOR_STR"));
		mColorSettings.addColor(UNSELECTEDCONTROLPOINTTANGENT1COLOR, new Color(200, 200, 240),
				LanguageResource.getString("UNSELECTEDCONTROLPOINTTANGENT1COLOR_STR"));
		mColorSettings.addColor(UNSELECTEDCONTROLPOINTTANGENT2COLOR, new Color(200, 200, 240),
				LanguageResource.getString("UNSELECTEDCONTROLPOINTTANGENT2COLOR_STR"));

		mColorSettings.addColor(UNSELECTEDCONTROLPOINTCENTEROUTLINECOLOR, new Color(80, 80, 150),
				LanguageResource.getString("UNSELECTEDCONTROLPOINTCENTEROUTLINECOLOR_STR"));
		mColorSettings.addColor(UNSELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR, new Color(150, 150, 80),
				LanguageResource.getString("UNSELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR_STR"));
		mColorSettings.addColor(UNSELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR, new Color(150, 80, 80),
				LanguageResource.getString("UNSELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR_STR"));

		mColorSettings.addColor(GUIDEPOINTCOLOR, new Color(255, 0, 0),
				LanguageResource.getString("GUIDEPOINTCOLOR_STR"));
		mColorSettings.addColor(BASELINECOLOR, new Color(0, 0, 0), LanguageResource.getString("GUIDEPOINTCOLOR_STR"));

		mSizeSettings = this.addCategory(LanguageResource.getString("SIZE_AND_THICKNESS_STR"));

		mSizeSettings.addDouble(SELECTEDCONTROLPOINTSIZE, 4.0,
				LanguageResource.getString("SELECTEDCONTROLPOINTSIZE_STR"));
		mSizeSettings.addDouble(SELECTEDCONTROLPOINTOUTLINETHICKNESS, 1.5,
				LanguageResource.getString("SELECTEDCONTROLPOINTOUTLINETHICKNESS_STR"));
		mSizeSettings.addDouble(UNSELECTEDCONTROLPOINTSIZE, 3.0,
				LanguageResource.getString("UNSELECTEDCONTROLPOINTSIZE_STR"));
		mSizeSettings.addDouble(UNSELECTEDCONTROLPOINTOUTLINETHICKNESS, 0.8,
				LanguageResource.getString("UNSELECTEDCONTROLPOINTOUTLINETHICKNESS_STR"));

		mSizeSettings.addDouble(BEZIERTHICKNESS, 0.8, LanguageResource.getString("BEZIERTHICKNESS_STR"));
		mSizeSettings.addDouble(CURVATURETHICKNESS, 1.2, LanguageResource.getString("CURVATURETHICKNESS_STR"));
		mSizeSettings.addDouble(VOLUMEDISTRIBUTIONTHICKNESS, 1.2,
				LanguageResource.getString("VOLUMEDISTRIBUTIONTHICKNESS_STR"));
		mSizeSettings.addDouble(GUIDEPNTTHICKNESS, 1.2, LanguageResource.getString("GUIDEPNTTHICKNESS_STR"));
		mSizeSettings.addDouble(BASELINETHICKNESS, 1, LanguageResource.getString("BASELINETHICKNESS_STR"));

		HashMap<Integer, String> looks = new HashMap<Integer, String>();

		mMiscSettings = this.addCategory(LanguageResource.getString("MISC_STR"));
		String systemLookAndFeelName = UIManager.getSystemLookAndFeelClassName();
		int systemLookAndFeelIndex = 0;
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			looks.put(looks.size(), info.getName());
			if (systemLookAndFeelName == info.getClassName()) {
				systemLookAndFeelIndex = looks.size() - 1;
			}
		}

		mMiscSettings.addObject(LOOK_AND_FEEL, mMiscSettings.new Enumeration(systemLookAndFeelIndex, looks),
				LanguageResource.getString("LOOKANDFEEL_STR"), new Settings.SettingChangedCallback() {
					@Override
					public void onSettingChanged(Setting setting) {
						Enumeration e = (Enumeration) mMiscSettings.getSettingValue(LOOK_AND_FEEL);

						String selectedLookAndFeelName = e.getAlternatives().get(e.getValue());

						if (BoardCAD.getInstance().isGUIBlocked() == false) {
							JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(),
									String.format(LanguageResource.getString("LOOKANDFEELCHANGEDMSG_STR"),
											selectedLookAndFeelName),
									LanguageResource.getString("LOOKANDFEELCHANGEDTITLE_STR"),
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
		mMiscSettings.getPreferences();

		try {
			Enumeration e = (Enumeration) mMiscSettings.getSettingValue(LOOK_AND_FEEL);

			String selectedLookAndFeelName = e.getAlternatives().get(e.getValue());

			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (selectedLookAndFeelName.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.err.println(
					"Couldn't find class for specified look and feel:" + UIManager.getSystemLookAndFeelClassName());
			System.err.println("Using the default look and feel.");
		}

		mMiscSettings.addBoolean(PRINTGUIDEPOINTS, false, LanguageResource.getString("PRINTGUIDEPOINTS_STR"));
		mMiscSettings.addBoolean(PRINTFINS, false, LanguageResource.getString("PRINTFINS_STR"));

		mMiscSettings.addInteger(FRACTIONACCURACY, 16, LanguageResource.getString("FRACTIONACCURACY_STR"),
				new Settings.SettingChangedCallback() {

					@Override
					public void onSettingChanged(Setting setting) {
						UnitUtils.setFractionAccuracy(setting.intValue());
					}
				});

		mMiscSettings.addBoolean(ROCKERSTICK, true, LanguageResource.getString("ROCKERSTICK_STR"));

		mMiscSettings.addBoolean(OFFSETINTERPLOATION, true, LanguageResource.getString("OFFSETINTERPLOATION_STR"));
		mMiscSettings.addInteger(NUM3DPROCESSES, 8, LanguageResource.getString("NUM3DPROCESSES_STR"));
	}

	public void setDefaultTheme() {
		mColorSettings.setColor(TEXTCOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(BACKGROUNDCOLOR, new Color(200, 200, 240));
		mColorSettings.setColor(UNSELECTEDBACKGROUNDCOLOR, new Color(220, 220, 245));
		mColorSettings.setColor(RENDERBACKGROUNDCOLOR, new Color(100, 120, 245));
		mColorSettings.setColor(STRINGERCOLOR, new Color(100, 100, 100));
		mColorSettings.setColor(FLOWLINESCOLOR, new Color(100, 150, 100));
		mColorSettings.setColor(APEXLINECOLOR, new Color(80, 80, 200));
		mColorSettings.setColor(TUCKUNDERLINECOLOR, new Color(150, 50, 50));
		mColorSettings.setColor(CENTERLINECOLOR, new Color(180, 180, 220));

		mColorSettings.setColor(BRDCOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(ORIGINALCOLOR, new Color(240, 240, 240));
		mColorSettings.setColor(GHOSTCOLOR, new Color(128, 128, 128));
		mColorSettings.setColor(BLANKCOLOR, new Color(128, 128, 128));
		mColorSettings.setColor(GRIDCOLOR, new Color(128, 128, 128));
		mColorSettings.setColor(FINSCOLOR, new Color(205, 128, 128));
		mColorSettings.setColor(CURVATURECOLOR, new Color(130, 130, 180));
		mColorSettings.setColor(VOLUMEDISTRIBUTIONCOLOR, new Color(80, 80, 80));
		mColorSettings.setColor(CENTEROFMASSCOLOR, new Color(205, 10, 10));
		mColorSettings.setColor(SELECTEDTANGENTCOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTCENTERCOLOR, new Color(30, 30, 200));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT1COLOR, new Color(200, 200, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT2COLOR, new Color(200, 0, 0));

		mColorSettings.setColor(SELECTEDCONTROLPOINTCENTEROUTLINECOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR, new Color(0, 0, 0));

		mColorSettings.setColor(UNSELECTEDTANGENTCOLOR, new Color(100, 100, 100));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTCENTERCOLOR, new Color(200, 200, 240));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT1COLOR, new Color(200, 200, 240));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT2COLOR, new Color(200, 200, 240));

		mColorSettings.setColor(UNSELECTEDCONTROLPOINTCENTEROUTLINECOLOR, new Color(80, 80, 150));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR, new Color(150, 150, 80));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR, new Color(150, 80, 80));

		mColorSettings.setColor(GUIDEPOINTCOLOR, new Color(255, 0, 0));
		mColorSettings.setColor(BASELINECOLOR, new Color(0, 0, 0));
	}

	public void setDarkTheme() {
		mColorSettings.setColor(TEXTCOLOR, new Color(200, 200, 200));
		mColorSettings.setColor(BACKGROUNDCOLOR, new Color(200, 200, 240));
		mColorSettings.setColor(UNSELECTEDBACKGROUNDCOLOR, new Color(220, 220, 245));
		mColorSettings.setColor(RENDERBACKGROUNDCOLOR, new Color(220, 220, 245));
		mColorSettings.setColor(STRINGERCOLOR, new Color(100, 100, 100));
		mColorSettings.setColor(FLOWLINESCOLOR, new Color(100, 150, 100));
		mColorSettings.setColor(APEXLINECOLOR, new Color(80, 80, 200));
		mColorSettings.setColor(TUCKUNDERLINECOLOR, new Color(150, 50, 50));
		mColorSettings.setColor(CENTERLINECOLOR, new Color(180, 180, 220));

		mColorSettings.setColor(BRDCOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(ORIGINALCOLOR, new Color(240, 240, 240));
		mColorSettings.setColor(GHOSTCOLOR, new Color(128, 128, 128));
		mColorSettings.setColor(BLANKCOLOR, new Color(128, 128, 128));
		mColorSettings.setColor(GRIDCOLOR, new Color(128, 128, 128));
		mColorSettings.setColor(FINSCOLOR, new Color(205, 128, 128));
		mColorSettings.setColor(CURVATURECOLOR, new Color(130, 130, 180));
		mColorSettings.setColor(VOLUMEDISTRIBUTIONCOLOR, new Color(80, 80, 80));
		mColorSettings.setColor(CENTEROFMASSCOLOR, new Color(205, 10, 10));
		mColorSettings.setColor(SELECTEDTANGENTCOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTCENTERCOLOR, new Color(30, 30, 200));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT1COLOR, new Color(200, 200, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT2COLOR, new Color(200, 0, 0));

		mColorSettings.setColor(SELECTEDCONTROLPOINTCENTEROUTLINECOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR, new Color(0, 0, 0));
		mColorSettings.setColor(SELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR, new Color(0, 0, 0));

		mColorSettings.setColor(UNSELECTEDTANGENTCOLOR, new Color(100, 100, 100));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTCENTERCOLOR, new Color(200, 200, 240));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT1COLOR, new Color(200, 200, 240));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT2COLOR, new Color(200, 200, 240));

		mColorSettings.setColor(UNSELECTEDCONTROLPOINTCENTEROUTLINECOLOR, new Color(80, 80, 150));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR, new Color(150, 150, 80));
		mColorSettings.setColor(UNSELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR, new Color(150, 80, 80));

		mColorSettings.setColor(GUIDEPOINTCOLOR, new Color(255, 0, 0));
		mColorSettings.setColor(BASELINECOLOR, new Color(0, 0, 0));
	}

	public Color getBrdColor() {

		return mColorSettings.getColor(BRDCOLOR);
	}

	public Color getOriginalBrdColor() {

		return mColorSettings.getColor(ORIGINALCOLOR);
	}

	public Color getGhostBrdColor() {

		return mColorSettings.getColor(GHOSTCOLOR);
	}

	public Color getBlankColor() {

		return mColorSettings.getColor(BLANKCOLOR);
	}

	public Color getTextColor() {

		return mColorSettings.getColor(TEXTCOLOR);
	}

	public Color getBackgroundColor() {

		return mColorSettings.getColor(BACKGROUNDCOLOR);
	}

	public Color getUnselectedBackgroundColor() {

		return mColorSettings.getColor(UNSELECTEDBACKGROUNDCOLOR);
	}

	public Color getRenderBackgroundColor() {

		return mColorSettings.getColor(RENDERBACKGROUNDCOLOR);
	}

	public Color getStringerColor() {

		return mColorSettings.getColor(STRINGERCOLOR);
	}

	public Color getFlowLinesColor() {

		return mColorSettings.getColor(FLOWLINESCOLOR);
	}

	public Color getApexLineColor() {

		return mColorSettings.getColor(APEXLINECOLOR);
	}

	public Color getTuckUnderLineColor() {

		return mColorSettings.getColor(TUCKUNDERLINECOLOR);
	}

	public Color getCenterLineColor() {

		return mColorSettings.getColor(CENTERLINECOLOR);
	}

	public Color getGridColor() {

		return mColorSettings.getColor(GRIDCOLOR);
	}

	public Color getFinsColor() {

		return mColorSettings.getColor(FINSCOLOR);
	}

	public Color getCurvatureColor() {

		return mColorSettings.getColor(CURVATURECOLOR);
	}

	public Color getVolumeDistributionColor() {

		return mColorSettings.getColor(VOLUMEDISTRIBUTIONCOLOR);
	}

	public Color getCenterOfMassColor() {

		return mColorSettings.getColor(CENTEROFMASSCOLOR);
	}

	public Color getSelectedTangentColor() {

		return mColorSettings.getColor(SELECTEDTANGENTCOLOR);
	}

	public Color getSelectedCenterControlPointColor() {

		return mColorSettings.getColor(SELECTEDCONTROLPOINTCENTERCOLOR);
	}

	public Color getSelectedTangent1ControlPointColor() {

		return mColorSettings.getColor(SELECTEDCONTROLPOINTTANGENT1COLOR);
	}

	public Color getSelectedOutlineTangent2ControlPointColor() {

		return mColorSettings.getColor(SELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR);
	}

	public Color getSelectedOutlineCenterControlPointColor() {

		return mColorSettings.getColor(SELECTEDCONTROLPOINTCENTEROUTLINECOLOR);
	}

	public Color getSelectedOutlineTangent1ControlPointColor() {

		return mColorSettings.getColor(SELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR);
	}

	public Color getSelectedTangent2ControlPointColor() {

		return mColorSettings.getColor(SELECTEDCONTROLPOINTTANGENT2COLOR);
	}

	public Color getUnselectedTangentColor() {

		return mColorSettings.getColor(UNSELECTEDTANGENTCOLOR);
	}

	public Color getUnselectedCenterControlPointColor() {

		return mColorSettings.getColor(UNSELECTEDCONTROLPOINTCENTERCOLOR);
	}

	public Color getUnselectedTangent1ControlPointColor() {

		return mColorSettings.getColor(UNSELECTEDCONTROLPOINTTANGENT1COLOR);
	}

	public Color getUnselectedOutlineTangent2ControlPointColor() {

		return mColorSettings.getColor(UNSELECTEDCONTROLPOINTTANGENT2OUTLINECOLOR);
	}

	public Color getUnselectedOutlineCenterControlPointColor() {

		return mColorSettings.getColor(UNSELECTEDCONTROLPOINTCENTEROUTLINECOLOR);
	}

	public Color getUnselectedOutlineTangent1ControlPointColor() {

		return mColorSettings.getColor(UNSELECTEDCONTROLPOINTTANGENT1OUTLINECOLOR);
	}

	public Color getUnselectedTangent2ControlPointColor() {

		return mColorSettings.getColor(UNSELECTEDCONTROLPOINTTANGENT2COLOR);
	}

	public Color getGuidePointColor() {

		return mColorSettings.getColor(GUIDEPOINTCOLOR);
	}

	public Color getBaseLineColor() {

		return mColorSettings.getColor(BASELINECOLOR);
	}

	public double getSelectedControlPointSize() {

		return mSizeSettings.getDouble(SELECTEDCONTROLPOINTSIZE);
	}

	public double getUnselectedControlPointSize() {

		return mSizeSettings.getDouble(UNSELECTEDCONTROLPOINTSIZE);
	}

	public double getBezierThickness() {
		return mSizeSettings.getDouble(BEZIERTHICKNESS);
	}

	public double getCurvatureThickness() {
		return mSizeSettings.getDouble(CURVATURETHICKNESS);
	}

	public double getVolumeDistributionThickness() {
		return mSizeSettings.getDouble(VOLUMEDISTRIBUTIONTHICKNESS);
	}

	public double getSelectedControlPointOutlineThickness() {
		return mSizeSettings.getDouble(SELECTEDCONTROLPOINTOUTLINETHICKNESS);
	}

	public double getUnselectedControlPointOutlineThickness() {
		return mSizeSettings.getDouble(UNSELECTEDCONTROLPOINTOUTLINETHICKNESS);
	}

	public double getGuidePointThickness() {
		return mSizeSettings.getDouble(GUIDEPNTTHICKNESS);
	}

	public int getFractionAccuracy() {

		return mMiscSettings.getInt(FRACTIONACCURACY);
	}

	public boolean isPrintingControlPoints() {
		return mMiscSettings.getBoolean(PRINTGUIDEPOINTS);
	}

	public boolean isPrintingFins() {
		return mMiscSettings.getBoolean(PRINTFINS);
	}

	public boolean isUsingRockerStickAdjustment() {
		return mMiscSettings.getBoolean(ROCKERSTICK);
	}

	public boolean isUsingOffsetInterpolation() {
		return mMiscSettings.getBoolean(OFFSETINTERPLOATION);
	}

	public int getNumberOf3DProcesses() {
		return mMiscSettings.getInt(NUM3DPROCESSES);
	}



	public double getBaseLineThickness() {
		return mSizeSettings.getDouble(BASELINETHICKNESS);
	}

};
