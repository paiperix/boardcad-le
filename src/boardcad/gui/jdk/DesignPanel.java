package boardcad.gui.jdk;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.media.j3d.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;

import board.BezierBoard;
import cadcore.MathUtils;

// =========================================================Design Panel
class DesignPanel extends Panel implements AbstractEditor {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// private UserInterface user_interface;
	private ThreeDView rendered_view;

	BranchGroup mRoot;
	Switch mBezier3DOnSwitch;
	Shape3D mBezier3DModel;
	TransformGroup mScaleTransform;

	boolean mBezierBoardChangedFor3D = false;

//	Brd3DModelGenerator mBrd3DModelGenerator = new Brd3DModelGenerator();
	FasterBrd3DModelGenerator mBrd3DModelGenerator = new FasterBrd3DModelGenerator();

	public DesignPanel() {
		// user_interface=ui;

		rendered_view = new ThreeDView(this);

		/*
		 * setLayout(new GridLayout(2,2,3,3));
		 *
		 * add(rocker_view); add(edge_view); add(outline_view);
		 * add(threed_view);
		 *
		 * doLayout(); redraw(); // threed_view.setSize(getPreferredSize());
		 * threed_view.doLayout();
		 */
		setLayout(new GridLayout(1, 1));
		add(rendered_view);
		doLayout();

		initBezierModel();

		redraw();
		// threed_view.setSize(getPreferredSize());
		// threed_view.doLayout();

	}


	public void addModel(BranchGroup model) {
		rendered_view.addModel(model);
	}

	@Override
	public void fit_all() {
		rendered_view.fit();
	}

	public void redraw() {
		rendered_view.repaint();
		rendered_view.doLayout();
	}

	public ThreeDView get3DView() {
		return rendered_view;
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

		// BranchGroup copy = (BranchGroup)root.cloneTree();

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
}

// =========================================================Views

abstract class View extends Panel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected PopupMenu view_menu;
	protected DesignPanel design_panel;

	protected static boolean is_marked;

	protected Dimension off_dimension;
	protected Image off_image;
	protected Graphics g;
	protected double scale; // transforms between image and board coordinates
	protected int offset_x; // position of origo in image coordinates
	protected int offset_y;

	protected void clear_graphics() {
		Dimension d = getSize();

		if (g == null || d.width != off_dimension.width
				|| d.height != off_dimension.height) {
			off_dimension = d;
			off_image = createImage(d.width, d.height);
			g = off_image.getGraphics();
		}

		g.setColor(BoardCAD.getInstance().getBackgroundColor());
		g.fillRect(0, 0, d.width, d.height);
		// g.setColor(getBackground());
		// g.fillRect(0, 0, d.width, d.height);
		// g.draw3DRect(0,0,d.width-1,d.height-1,true);
		// g.draw3DRect(3,3,d.width-7,d.height-7,false);

	}
}

class ThreeDView extends View implements ItemListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JPopupMenu view_menu;
	private JCheckBoxMenuItem shade_item;

	private SimpleUniverse u = null;
	public BranchGroup scene;
	private Shape3D shape;
	private Appearance look;
	private Background mBackgroundNode;

	private DirectionalLight mUpLight;
	private DirectionalLight mDownLight;
	private DirectionalLight mLeftLight;
	private DirectionalLight mRightLight;
	private DirectionalLight mHeadLight;
	private AmbientLight mAmbientLight;

	GraphicsConfiguration config;

	Canvas3D c;

	public ThreeDView(DesignPanel dp) {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		view_menu = new JPopupMenu();
		add(view_menu);

		setLayout(new BorderLayout());
		config = SimpleUniverse.getPreferredConfiguration();

		c = new Canvas3D(config);
		add("Center", c);
		shape = new Shape3D();
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		c.addMouseListener(new ThreeDMouse(this));

		look = new Appearance();

		// Create a simple scene and attach it to the virtual universe
		scene = createSceneGraph();
		u = new SimpleUniverse(c, 4);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Headlight
		ViewingPlatform viewingPlatform = u.getViewingPlatform();

		PlatformGeometry pg = new PlatformGeometry();

		mHeadLight = new DirectionalLight(new Color3f(0.8f, 0.8f, 0.8f),
				new Vector3f(0.0f, 0.0f, -1.0f));
		mHeadLight.setInfluencingBounds(bounds);
		mHeadLight.setCapability(Light.ALLOW_STATE_WRITE);
		mHeadLight.setEnable(true);
		pg.addChild(mHeadLight);
		viewingPlatform.setPlatformGeometry(pg);

		// Svenne
		// u.setJ3DThreadPriority(Thread.MAX_PRIORITY);
		// u.getViewer().getView().setMinimumFrameCycleTime(20);
		// u.getViewer().getView().setSceneAntialiasingEnable(true);

		/*
		 * viewingPlatform.addRotateBehavior(0);
		 * viewingPlatform.addZoomBehavior(1);
		 * viewingPlatform.addTranslateBehavior(2);
		 */
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		u.getViewingPlatform().setNominalViewingTransform();
		// Svenne-test
		// u.getViewingPlatform().clearCapabilityIsFrequent(ALLBITS);

		// add orbit behavior to the ViewingPlatform
		OrbitBehavior orbit = new OrbitBehavior(c);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);

		u.addBranchGraph(scene);

		shade_item = new JCheckBoxMenuItem("Shade", false);

		shade_item.addItemListener(this);

		view_menu.add(shade_item);

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
		view_menu.add(toggleUpLight);

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
		view_menu.add(toggleDownLight);

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
		view_menu.add(toggleLeftLight);

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
		view_menu.add(toggleRightLight);

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
		view_menu.add(toggleHeadLight);

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
		view_menu.add(toggleAmbientLight);

	}

	public void addModel(BranchGroup model) {
		scene.addChild(model);
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
		if (e.getSource() == shade_item) {
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
				view_menu.show(the_view, e.getX(), e.getY());
		}
	}

	private BranchGroup createSceneGraph() {
		BranchGroup branchRoot = new BranchGroup();
		branchRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);

		// Create a bounds for the background and lights
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Set up the background
		Color3f bgColor = new Color3f(BoardCAD.getInstance()
				.getBackgroundColor());
		mBackgroundNode = new Background(bgColor);
		mBackgroundNode.setApplicationBounds(bounds);
		mBackgroundNode.setCapability(Background.ALLOW_COLOR_WRITE);
		branchRoot.addChild(mBackgroundNode);

		// Set up the ambient light
		// Color3f ambientColor = new Color3f(0.2f, 0.2f, 0.2f);
		Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
		mAmbientLight = new AmbientLight(ambientColor);
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

		// Create a Transformgroup to scale all objects so they
		// appear in the scene.
		TransformGroup objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(1.5);
		objScale.setTransform(t3d);
		branchRoot.addChild(objScale);

		/*
		 * //add texture java.net.URL texImage=null; try { texImage = new
		 * java.net.URL("file:airbrush.jpg"); // texImage = new
		 * java.net.URL("file:"+filename); } catch
		 * (java.net.MalformedURLException ex) {
		 * System.out.println(ex.getMessage()); System.exit(1); } // Set up the
		 * texture map TextureLoader tex = new TextureLoader(texImage, this);
		 * look.setTexture(tex.getTexture());
		 *
		 * // TextureAttributes texAttr = new TextureAttributes(); //
		 * texAttr.setTextureMode(TextureAttributes.MODULATE); //
		 * look.setTextureAttributes(texAttr);
		 */

		// Create an Appearance.
		Color3f objColor = new Color3f(0.5f, 0.5f, 0.6f);
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		look.setTextureAttributes(texAttr);

		// Set up the material properties
		look.setMaterial(new Material(objColor, black, objColor, white, 100.0f));

		// Create the transform group node and initialize it to the
		// identity. Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime. Add it to the
		// root of the subgraph.
		TransformGroup board_tg = new TransformGroup();
		board_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		board_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objScale.addChild(board_tg);

		shape.setGeometry(new TriangleArray(3, TriangleArray.COORDINATES
				| TriangleArray.NORMALS));
		shape.setAppearance(look);
		board_tg.addChild(shape);

		// Have Java 3D perform optimizations on this scene graph.
		branchRoot.compile();

		return branchRoot;
	}

	public void setBackgroundColor(Color color) {
		float[] components = color.getRGBComponents(null);
		mBackgroundNode.setColor(new Color3f(components[0], components[1],
				components[2]));
	}

	public void fit() {
		if (getSize().width == 0)
			return;

		if (off_dimension == null)
			return;

		scale = 0.9 * (off_dimension.width - 40)
				/ 200.0; //TODO: Use bezier board length board_handler.get_board_length();
		offset_x = 100;
		offset_y = off_dimension.height - 20;
		repaint();
	}

	// public void destroy()
	// {
	// u.removeAllLocales();
	// }
}

