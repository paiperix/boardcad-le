package boardcad.gui.jdk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;

import board.BezierBoard;
import boardcad.settings.BoardCADSettings;

class ThreeDView extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	BranchGroup mRoot;
	Shape3D mBezier3DModel;
	TransformGroup mScaleTransform;

	boolean mBezierBoardChangedFor3D = false;

	FasterBrd3DModelGenerator mBrd3DModelGenerator = new FasterBrd3DModelGenerator();

	private JPopupMenu mViewMenu;

	private SimpleUniverse mUniverse = null;
	public BranchGroup mScene;
	private Shape3D mShape;
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
		
		setLayout(new BorderLayout());

		init3DView();
		
		initMenu();

		initBezierModel();

		redraw();		
	}
	
	public void hardReset() {
		if(mCanvas3D != null) {
			remove(mCanvas3D);	
			mCanvas3D = null;
		}
		
		init3DView();
		initBezierModel();
		updateBezier3DModel(true);
	}
	
	private void init3DView() {
		mGfxConfig = SimpleUniverse.getPreferredConfiguration();

		mCanvas3D = new Canvas3D(mGfxConfig);
		add("Center", mCanvas3D);

		mShape = new Shape3D();
		mShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		mShape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		mShape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		mShape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		mCanvas3D.addMouseListener(new ThreeDMouse(this));

		// Create a simple scene and attach it to the virtual universe
		mScene = createSceneGraph();
		mUniverse = new SimpleUniverse(mCanvas3D, 4);
		mUniverse.getViewer().getView().setFrontClipPolicy(View.PHYSICAL_EYE);
		mUniverse.getViewer().getView().setFrontClipDistance(0.01);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

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
	}
	
	private void initMenu(){
		mViewMenu = new JPopupMenu();
		
		JMenuItem refreshItem = new JMenuItem("Refresh");
		refreshItem.setMnemonic(KeyEvent.VK_R);
		refreshItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		mViewMenu.add(refreshItem);

		JCheckBoxMenuItem toggleUpLight = new JCheckBoxMenuItem("Toggle up light");
		toggleUpLight.setMnemonic(KeyEvent.VK_U);
		//toggleUpLight.setSelected(mUpLight.getEnable());
		toggleUpLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleUpLight();
			}
		});
		mViewMenu.add(toggleUpLight);

		JCheckBoxMenuItem toggleDownLight = new JCheckBoxMenuItem("Toggle down light");
		toggleDownLight.setMnemonic(KeyEvent.VK_R);
		//toggleDownLight.setSelected(mDownLight.getEnable());
		toggleDownLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleDownLight();
			}
		});
		mViewMenu.add(toggleDownLight);

		JCheckBoxMenuItem toggleLeftLight = new JCheckBoxMenuItem("Toggle left light");
		toggleLeftLight.setMnemonic(KeyEvent.VK_L);
		//toggleLeftLight.setSelected(mLeftLight.getEnable());
		toggleLeftLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleLeftLight();
			}
		});
		mViewMenu.add(toggleLeftLight);

		JCheckBoxMenuItem toggleRightLight = new JCheckBoxMenuItem("Toggle right light");
		toggleRightLight.setMnemonic(KeyEvent.VK_R);
		//toggleRightLight.setSelected(mRightLight.getEnable());
		toggleRightLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleRightLight();
			}
		});
		mViewMenu.add(toggleRightLight);

		JCheckBoxMenuItem toggleHeadLight = new JCheckBoxMenuItem("Toggle head light");
		toggleHeadLight.setMnemonic(KeyEvent.VK_R);
		//toggleHeadLight.setSelected(mHeadLight.getEnable());
		toggleHeadLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleHeadLight();
			}
		});
		mViewMenu.add(toggleHeadLight);

		JCheckBoxMenuItem toggleAmbientLight = new JCheckBoxMenuItem("Toggle ambient light");
		toggleAmbientLight.setMnemonic(KeyEvent.VK_R);
		//toggleAmbientLight.setSelected(mAmbientLight.getEnable());
		toggleAmbientLight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				toggleAmbientLight();
			}
		});
		mViewMenu.add(toggleAmbientLight);

		setComponentPopupMenu(mViewMenu);
	}


	public void redraw() {
		repaint();
		doLayout();
	}

	public void setShowBezierModel(boolean show) {
		if(show) {
			if(mRoot == null) {
				updateBezier3DModel(true);
			}
		} else {
			if(mRoot != null) {
				removeModel(mRoot);
				mRoot = null;
				mScaleTransform = null;
				mBezier3DModel = null;
			}			
		}
	}

	public boolean getShowBezierMode(boolean show) {
		return (mRoot != null);
	}

	void initBezierModel() {

		mRoot = new BranchGroup();
		mRoot.setCapability(BranchGroup.ALLOW_DETACH);

		mScaleTransform = new TransformGroup();
		mScaleTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mRoot.addChild(mScaleTransform);

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

	public void updateBezier3DModel(boolean forceRefresh) {
		BezierBoard brd = BoardCAD.getInstance().getCurrentBrd();
		double scale = 0.008;
		double offset = -brd.getLength() * scale / 2.0;
		if (brd != null && (mBezierBoardChangedFor3D || forceRefresh)) {
			if(forceRefresh){
				if(mRoot != null)removeModel(mRoot);
				initBezierModel();
			}
			setBezierScale(scale, offset);
			mBrd3DModelGenerator.update3DModel(brd, getBezier3DModel(), BoardCADSettings.getInstance().getNumberOf3DProcesses(), forceRefresh);
			mBezierBoardChangedFor3D = false;
		}
	}

	public void setBoardChangedFor3D() {
		mBezierBoardChangedFor3D = true;
	}

	public void addModel(BranchGroup model) {
		mScene.addChild(model);
	}

	public void removeModel(BranchGroup model) {
		model.detach();
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

	class ThreeDMouse extends MouseAdapter {
		private ThreeDView m3DView;

		public ThreeDMouse(ThreeDView view) {
			m3DView = view;
		}


		public void mouseReleased(MouseEvent e) {
	
			if (e.isPopupTrigger()) {
				mViewMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
		}
		public void mousePressed(MouseEvent e) {
			
			if (e.isPopupTrigger()) {
				mViewMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
		}
	}

	private BranchGroup createSceneGraph() {
		BranchGroup branchRoot = new BranchGroup();
		branchRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		branchRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		branchRoot.setCapability(BranchGroup.ALLOW_DETACH);

		// Create a bounds for the background and lights
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Set up the background
		Color3f bgColor = new Color3f(BoardCADSettings.getInstance().getBackgroundColor().getRGBColorComponents(new float[3]));
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

		// Have Java 3D perform optimizations on this scene graph.
		branchRoot.compile();

		return branchRoot;
	}

	public void setBackgroundColor(Color color) {
		if(mBackgroundNode != null) {
			float[] components = color.getRGBComponents(null);
			mBackgroundNode.setColor(new Color3f(components[0], components[1],
					components[2]));
		}
	}

	public void refresh() {
		updateBezier3DModel(true);
	}
	
}

