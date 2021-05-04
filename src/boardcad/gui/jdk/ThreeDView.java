package boardcad.gui.jdk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.media.j3d.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.*;

import board.BezierBoard;
import boardcad.settings.BoardCADSettings;

// =========================================================Design Panel
class ThreeDView extends Panel implements ItemListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	BranchGroup mRoot;
	Switch mBezier3DOnSwitch;
	Shape3D mBezier3DModel;
	TransformGroup mScaleTransform;

	boolean mBezierBoardChangedFor3D = false;

//	Brd3DModelGenerator mBrd3DModelGenerator = new Brd3DModelGenerator();
	FasterBrd3DModelGenerator mBrd3DModelGenerator = new FasterBrd3DModelGenerator();

	private JPopupMenu mViewMenu;
	private JCheckBoxMenuItem mShadeItem;

	private SimpleUniverse mUniverse = null;
	public BranchGroup mScene;
	private Shape3D mShape;
	private Appearance mLook;
	private Background mBackgroundNode;

	private DirectionalLight mUpLight;
	private DirectionalLight mDownLight;
	private DirectionalLight mLeftLight;
	private DirectionalLight mRightLight;
	private DirectionalLight mHeadLight;
	private AmbientLight mAmbientLight;

	GraphicsConfiguration mGfxConfig;

	Canvas3D mCanvas3D;

	public ThreeDView() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		mViewMenu = new JPopupMenu();
		add(mViewMenu);

		setLayout(new BorderLayout());
		mGfxConfig = SimpleUniverse.getPreferredConfiguration();

		mCanvas3D = new Canvas3D(mGfxConfig);
		add("Center", mCanvas3D);
		mShape = new Shape3D();
		mShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		mShape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		mShape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		mShape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		mCanvas3D.addMouseListener(new ThreeDMouse(this));

		mLook = new Appearance();

		// Create a simple scene and attach it to the virtual universe
		mScene = createSceneGraph();
		mUniverse = new SimpleUniverse(mCanvas3D, 4);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Head light
		ViewingPlatform viewingPlatform = mUniverse.getViewingPlatform();

		PlatformGeometry pg = new PlatformGeometry();

		mHeadLight = new DirectionalLight(new Color3f(0.8f, 0.8f, 0.8f),
				new Vector3f(0.0f, 0.0f, -1.0f));
		mHeadLight.setInfluencingBounds(bounds);
		mHeadLight.setCapability(Light.ALLOW_STATE_WRITE);
		mHeadLight.setEnable(true);
		pg.addChild(mHeadLight);
		viewingPlatform.setPlatformGeometry(pg);

		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		mUniverse.getViewingPlatform().setNominalViewingTransform();

		// add orbit behavior to the ViewingPlatform
		OrbitBehavior orbit = new OrbitBehavior(mCanvas3D);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);

		mUniverse.addBranchGraph(mScene);

		mShadeItem = new JCheckBoxMenuItem("Shade", false);

		mShadeItem.addItemListener(this);

		mViewMenu.add(mShadeItem);

		JCheckBoxMenuItem toggleUpLight = new JCheckBoxMenuItem(
				"Toggle up light");
		toggleUpLight.setMnemonic(KeyEvent.VK_U);
		toggleUpLight.setSelected(mUpLight.getEnable());
		toggleUpLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleUpLight();
			}
		});
		mViewMenu.add(toggleUpLight);

		JCheckBoxMenuItem toggleDownLight = new JCheckBoxMenuItem(
				"Toggle down light");
		toggleDownLight.setMnemonic(KeyEvent.VK_R);
		toggleDownLight.setSelected(mDownLight.getEnable());
		toggleDownLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleDownLight();
			}
		});
		mViewMenu.add(toggleDownLight);

		JCheckBoxMenuItem toggleLeftLight = new JCheckBoxMenuItem(
				"Toggle left light");
		toggleLeftLight.setMnemonic(KeyEvent.VK_L);
		toggleLeftLight.setSelected(mLeftLight.getEnable());
		toggleLeftLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleLeftLight();
			}
		});
		mViewMenu.add(toggleLeftLight);

		JCheckBoxMenuItem toggleRightLight = new JCheckBoxMenuItem(
				"Toggle right light");
		toggleRightLight.setMnemonic(KeyEvent.VK_R);
		toggleRightLight.setSelected(mRightLight.getEnable());
		toggleRightLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleRightLight();
			}
		});
		mViewMenu.add(toggleRightLight);

		JCheckBoxMenuItem toggleHeadLight = new JCheckBoxMenuItem(
				"Toggle head light");
		toggleHeadLight.setMnemonic(KeyEvent.VK_R);
		toggleHeadLight.setSelected(mHeadLight.getEnable());
		toggleHeadLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleHeadLight();
			}
		});
		mViewMenu.add(toggleHeadLight);

		JCheckBoxMenuItem toggleAmbientLight = new JCheckBoxMenuItem(
				"Toggle ambient light");
		toggleAmbientLight.setMnemonic(KeyEvent.VK_R);
		toggleAmbientLight.setSelected(mAmbientLight.getEnable());
		toggleAmbientLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleAmbientLight();
			}
		});
		mViewMenu.add(toggleAmbientLight);

		initBezierModel();

		redraw();
	}


	public void redraw() {
		repaint();
		doLayout();
	}

	public void setShowBezierModel(boolean show) {
		mBezier3DOnSwitch.setWhichChild(show ? Switch.CHILD_ALL
				: Switch.CHILD_NONE);
	}

	public boolean getShowBezierMode(boolean show) {
		return (mBezier3DOnSwitch.getWhichChild() == Switch.CHILD_ALL);
	}

	void initBezierModel() {

		mRoot = new BranchGroup();

		mBezier3DOnSwitch = new Switch();
		mBezier3DOnSwitch.setCapability(Switch.ALLOW_SWITCH_READ);
		mBezier3DOnSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

		mBezier3DOnSwitch.setWhichChild(Switch.CHILD_ALL);

		mRoot.addChild(mBezier3DOnSwitch);
		mScaleTransform = new TransformGroup();
		mScaleTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mBezier3DOnSwitch.addChild(mScaleTransform);
		mBezier3DModel = new Shape3D();
		mBezier3DModel.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		mBezier3DModel.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		// Create an Appearance.
		Appearance a = new Appearance();
		Color3f ambient = new Color3f(0.4f, 0.4f, 0.4f);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuse = new Color3f(0.8f, 0.8f, 0.8f);
		Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);

		// Set up the material properties
		a.setMaterial(new Material(ambient, emissive, diffuse, specular, 115.0f));
		mBezier3DModel.setAppearance(a);
		mScaleTransform.addChild(mBezier3DModel);

		addModel(mRoot);
	}

	public Shape3D getBezier3DModel() {
		return mBezier3DModel;
	}

	public void setBezierScale(double scale, double offset) {
		Transform3D transform = new Transform3D();
		transform.setScale(scale);
		transform.setTranslation(new Vector3d(offset, 0.0, 0.0));
		transform.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, -Math.PI / 2.0));
		mScaleTransform.setTransform(transform);
	}

	public void updateBezier3DModel(BezierBoard brd) {

		System.out.println("DesignPanel.updateBezier3DModel()");

		double scale = 0.008;
		double offset = -brd.getLength() * scale / 2.0;
		if (brd != null && mBezierBoardChangedFor3D) {
			setBezierScale(scale, offset);
			mBrd3DModelGenerator.update3DModel(brd, getBezier3DModel(), 8);
			mBezierBoardChangedFor3D = false;
		}
	}

	public void setBoardChangedFor3D() {
		mBezierBoardChangedFor3D = true;
	}

	public void addModel(BranchGroup model) {
		mScene.addChild(model);
	}

	void toggleUpLight() {
		mUpLight.setEnable(!mUpLight.getEnable());
	}

	void toggleDownLight() {
		mDownLight.setEnable(!mDownLight.getEnable());
	}

	void toggleLeftLight() {
		mLeftLight.setEnable(!mLeftLight.getEnable());
	}

	void toggleRightLight() {
		mRightLight.setEnable(!mRightLight.getEnable());
	}

	void toggleHeadLight() {
		mHeadLight.setEnable(!mHeadLight.getEnable());
	}

	void toggleAmbientLight() {
		mAmbientLight.setEnable(!mAmbientLight.getEnable());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == mShadeItem) {
		}
	}

	class ThreeDMouse extends MouseAdapter {
		private ThreeDView the_view;

		public ThreeDMouse(ThreeDView v) {
			the_view = v;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.isMetaDown())
				mViewMenu.show(the_view, e.getX(), e.getY());
		}
	}

	private BranchGroup createSceneGraph() {
		BranchGroup branchRoot = new BranchGroup();
		branchRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);

		// Create a bounds for the background and lights
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Set up the background
		Color3f bgColor = new Color3f(BoardCADSettings.getInstance()
				.getBackgroundColor());
		mBackgroundNode = new Background(bgColor);
		mBackgroundNode.setApplicationBounds(bounds);
		mBackgroundNode.setCapability(Background.ALLOW_COLOR_WRITE);
		branchRoot.addChild(mBackgroundNode);

		// Set up the ambient light
		// Color3f ambientColor = new Color3f(0.2f, 0.2f, 0.2f);
		Color3f ambientLightColor = new Color3f(1.0f, 1.0f, 1.0f);
		mAmbientLight = new AmbientLight(ambientLightColor);
		mAmbientLight.setInfluencingBounds(bounds);
		mAmbientLight.setCapability(AmbientLight.ALLOW_STATE_WRITE);
		mAmbientLight.setEnable(true);
		branchRoot.addChild(mAmbientLight);

		// Set up the directional lights
		Color3f lightColor = new Color3f(0.8f, 0.8f, 0.8f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f down = new Vector3f(0.0f, -1.0f, 0.0f);
		Vector3f left = new Vector3f(0.0f, 0.0f, 1.0f);
		Vector3f right = new Vector3f(0.0f, 0.0f, -1.0f);

		mUpLight = new DirectionalLight(lightColor, up);
		mUpLight.setInfluencingBounds(bounds);
		mUpLight.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
		mUpLight.setEnable(false);
		branchRoot.addChild(mUpLight);

		mDownLight = new DirectionalLight(lightColor, down);
		mDownLight.setInfluencingBounds(bounds);
		mDownLight.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
		mDownLight.setEnable(false);
		branchRoot.addChild(mDownLight);

		mLeftLight = new DirectionalLight(lightColor, left);
		mLeftLight.setInfluencingBounds(bounds);
		mLeftLight.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
		mLeftLight.setEnable(false);
		branchRoot.addChild(mLeftLight);

		mRightLight = new DirectionalLight(lightColor, right);
		mRightLight.setInfluencingBounds(bounds);
		mRightLight.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
		mRightLight.setEnable(false);
		branchRoot.addChild(mRightLight);

		// Create a Transformgroup to scale all objects so they appear in the scene.
		TransformGroup objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(1.5);
		objScale.setTransform(t3d);
		branchRoot.addChild(objScale);

		// Create an Appearance.
		Color3f boardEmissiveColor = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f boardAmbientColor = new Color3f(0.2f, 0.2f, 0.2f);
		Color3f boardDiffuseColor = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f boardSpecularColor = new Color3f(0.8f, 0.8f, 1.0f);
		float shininess = 70.0f;

		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		mLook.setTextureAttributes(texAttr);

		// Set up the material properties
		mLook.setMaterial(new Material(boardAmbientColor, boardEmissiveColor, boardDiffuseColor, boardSpecularColor, shininess));

		// Create board transform group, enable the TRANSFORM_WRITE capability so that our behavior code can modify it at runtime.
		TransformGroup boardTransformGroup = new TransformGroup();
		boardTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		boardTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objScale.addChild(boardTransformGroup);

		mShape.setGeometry(new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.NORMALS));
		mShape.setAppearance(mLook);
		boardTransformGroup.addChild(mShape);

		// Have Java 3D perform optimizations on this scene graph.
		branchRoot.compile();

		return branchRoot;
	}

	public void setBackgroundColor(Color color) {
		float[] components = color.getRGBComponents(null);
		mBackgroundNode.setColor(new Color3f(components[0], components[1],
				components[2]));
	}

}

