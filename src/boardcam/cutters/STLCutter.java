package boardcam.cutters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintStream;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import board.AbstractBoard;
import boardcam.MachineConfig;
import boardcad.settings.Settings;
import boardcad.settings.Settings.SettingChangedCallback;
import boardcad.i18n.LanguageResource;


/**
 * This class reads the cutter geometry from an STL-file
 * allowing for general cutters to be designed in an
 * external CAD-program and used in BoardCAD.
 */

public class STLCutter extends AbstractCutter
{
	private final String CUTTER_SCALE_X = "CutterScaleX";
	private final String CUTTER_SCALE_Y = "CutterScaleY";
	private final String CUTTER_SCALE_Z = "CutterScaleZ";

	private final String CUTTER_STL_FILENAME = "STLFilename";

	private Point3d[] cutting_point;
	private Vector3d[] cutting_normal;
	private Point3d[] transformed_cutting_point;
	private Vector3d[] transformed_cutting_normal;

	private Point3d[] collision_point;
	private Point3d[] transformed_collision_point;

	private double offset_x;
	private double offset_y;
	private double offset_z;
	private int n;
	private int cn;

//	private Point3d[][] triangle;
	private Point3d[][][][]	triangle;

	public STLCutter()
	{
	}

	public STLCutter(MachineConfig config)
	{
		final Settings cutterSettings = config.addCategory(LanguageResource.getString("CUTTERCATEGORY_STR"));
		cutterSettings.clear();
		SettingChangedCallback scaleChanged = new Settings.SettingChangedCallback()
		{
			public void onSettingChanged(Object object)
			{
				scale(cutterSettings.getDouble(CUTTER_SCALE_X), cutterSettings.getDouble(CUTTER_SCALE_Y), cutterSettings.getDouble(CUTTER_SCALE_Z));
				update3DModel();
			}
		};
		cutterSettings.addObject(CUTTER_SCALE_X,  new Double(1.0), LanguageResource.getString("CUTTERSCALEX_STR"), scaleChanged);
		cutterSettings.addObject(CUTTER_SCALE_Y,  new Double(1.0), LanguageResource.getString("CUTTERSCALEY_STR"), scaleChanged);
		cutterSettings.addObject(CUTTER_SCALE_Z,  new Double(1.0), LanguageResource.getString("CUTTERSCALEZ_STR"), scaleChanged);

		SettingChangedCallback filenameChanged = new Settings.SettingChangedCallback()
		{
			public void onSettingChanged(Object object)
			{
				try{
					loadCutter(cutterSettings.getFileName(CUTTER_STL_FILENAME));
					update3DModel();
				}
				catch(Exception e)
				{

				}
			}
		};
		cutterSettings.addFileName(CUTTER_STL_FILENAME, "", LanguageResource.getString("STL_FILENAME_STR"), filenameChanged);
	}

	public void init(String toolname)
	{

/*		try {
			createFlatCutter(new PrintStream(new File("flat_cutter.stl")), 10.0, 25.3);
		} catch (Exception e) {
		}
*/
		loadCutter(toolname);

		transformed_cutting_point=new Point3d[n];
		transformed_cutting_normal=new Vector3d[n];

		for(int i=0;i<n-1;i++)
		{
			transformed_cutting_point[i]=new Point3d(cutting_point[i].x, cutting_point[i].y, cutting_point[i].z);
			transformed_cutting_normal[i]=new Vector3d(cutting_normal[i].x, cutting_normal[i].y, cutting_normal[i].z);
		}

	}


	public void createCollisionCutter()
	{

		cn=n;

		collision_point=new Point3d[cn];
		transformed_collision_point=new Point3d[cn];

		double myscale=0.9;
		double myztrans=1;

		for(int i=0;i<cn-1;i++)
		{
			collision_point[i]=new Point3d(cutting_point[i].x*myscale, cutting_point[i].y*myscale, cutting_point[i].z+myztrans);
			transformed_collision_point[i]=new Point3d(cutting_point[i].x*myscale, cutting_point[i].y*myscale, cutting_point[i].z+myztrans);
		}

	}

	public void createCollisionCutter(String toolname, double scale_x, double scale_y, double scale_z, double myztrans)
	{

		loadCollisionCutter(toolname);

		transformed_collision_point=new Point3d[cn];

		for(int i=0;i<cn-1;i++)
		{
			collision_point[i]=new Point3d(collision_point[i].x*scale_x, collision_point[i].y*scale_y, collision_point[i].z*scale_z+myztrans);
			transformed_collision_point[i]=new Point3d(collision_point[i].x*scale_x, collision_point[i].y*scale_y, collision_point[i].z*scale_z+myztrans);
		}

	}



	public void scale(double xscale, double yscale, double zscale)
	{
		for(int i=0;i<n-1;i++)
		{
			cutting_point[i].x=cutting_point[i].x*xscale;
			cutting_point[i].y=cutting_point[i].y*yscale;
			cutting_point[i].z=cutting_point[i].z*zscale;

			transformed_cutting_point[i].x=transformed_cutting_point[i].x*xscale;
			transformed_cutting_point[i].y=transformed_cutting_point[i].y*yscale;
			transformed_cutting_point[i].z=transformed_cutting_point[i].z*zscale;
		}
	}

	public void setRotationCenter(double x, double y, double z)
	{
		offset_x=x;
		offset_y=y;
		offset_z=z;
	}

	public void setRotation(double angle)
	{
		for(int i=0;i<n-1;i++)
		{
			transformed_cutting_point[i]=new Point3d(cutting_point[i].x, cutting_point[i].y, cutting_point[i].z);
			transformed_cutting_normal[i]=new Vector3d(cutting_normal[i].x, cutting_normal[i].y, cutting_normal[i].z);
		}

		for(int i=0;i<cn-1;i++)
		{
			transformed_collision_point[i]=new Point3d(collision_point[i].x, collision_point[i].y, collision_point[i].z);
		}

		translate(-offset_x, -offset_y, -offset_z);
		rotate4(angle);
		translate(offset_x, offset_y, offset_z);
	}

	public void setRotation(double angle4, double angle5)
	{
		for(int i=0;i<n-1;i++)
		{
			transformed_cutting_point[i]=new Point3d(cutting_point[i].x, cutting_point[i].y, cutting_point[i].z);
			transformed_cutting_normal[i]=new Vector3d(cutting_normal[i].x, cutting_normal[i].y, cutting_normal[i].z);
		}

		for(int i=0;i<cn-1;i++)
		{
			transformed_collision_point[i]=new Point3d(collision_point[i].x, collision_point[i].y, collision_point[i].z);
		}

		translate(-offset_x, -offset_y, -offset_z);
		rotate4(angle4);
		rotate5(angle5);
		translate(offset_x, offset_y, offset_z);
	}


	/**
	* Rotates the tool around fourth (x) axis
	*
	* @param theta	The rotation angle in degrees
	*/
	private void rotate4(double theta)
	{

		//rotate board

		double[][] m = {{1.0, 0.0, 0.0},
				{0.0, Math.cos(-theta*3.1415/180.0), -Math.sin(-theta*3.1415/180.0)},
		                {0.0, Math.sin(-theta*3.1415/180.0), Math.cos(-theta*3.1415/180.0)}};



		double[] t={0.0 , 0.0, 0.0};


		transform(m,t);
	}

	/**
	* Rotates the tool around fifth (z) axis
	*
	* @param theta	The rotation angle in degrees
	*/
	private void rotate5(double theta)
	{

		//rotate board

		double[][] m = {{Math.cos(theta*3.1415/180.0), Math.sin(theta*3.1415/180.0), 0.0},
				{-Math.sin(theta*3.1415/180.0), Math.cos(theta*3.1415/180.0), 0.0},
		                {0.0, 0.0, 1.0}};

		double[] t={0.0 , 0.0, 0.0};


		transform(m,t);
	}

	private void translate(double dx, double dy, double dz)
	{

		double[][] m = {{1.0, 0.0, 0.0},
		                {0.0, 1.0, 0.0},
		                {0.0, 0.0, 1.0}};

		double[] t={dx, dy, dz};

		transform(m,t);
	}

	private void transform(double[][] m, double[] t)
	{

//		transformed_cutting_point=new Point3d[n];
//		transformed_cutting_normal=new Vector3d[n];
		double tx,ty,tz;

		for(int i=0;i<n-1;i++)
		{
//			transformed_cutting_point[i]=new Point3d(0.0, 0.0, 0.0);
			tx=m[0][0]*transformed_cutting_point[i].x+m[0][1]*transformed_cutting_point[i].y+m[0][2]*transformed_cutting_point[i].z+t[0];
			ty=m[1][0]*transformed_cutting_point[i].x+m[1][1]*transformed_cutting_point[i].y+m[1][2]*transformed_cutting_point[i].z+t[1];
			tz=m[2][0]*transformed_cutting_point[i].x+m[2][1]*transformed_cutting_point[i].y+m[2][2]*transformed_cutting_point[i].z+t[2];
			transformed_cutting_point[i]=new Point3d(tx, ty, tz);

//			transformed_cutting_normal[i]=new Vector3d(0.0, 0.0, 0.0);
			tx=m[0][0]*transformed_cutting_normal[i].x+m[0][1]*transformed_cutting_normal[i].y+m[0][2]*transformed_cutting_normal[i].z;
			ty=m[1][0]*transformed_cutting_normal[i].x+m[1][1]*transformed_cutting_normal[i].y+m[1][2]*transformed_cutting_normal[i].z;
			tz=m[2][0]*transformed_cutting_normal[i].x+m[2][1]*transformed_cutting_normal[i].y+m[2][2]*transformed_cutting_normal[i].z;
			transformed_cutting_normal[i]=new Vector3d(tx, ty, tz);

		}


		for(int i=0;i<cn-1;i++)
		{
			tx=m[0][0]*transformed_collision_point[i].x+m[0][1]*transformed_collision_point[i].y+m[0][2]*transformed_collision_point[i].z+t[0];
			ty=m[1][0]*transformed_collision_point[i].x+m[1][1]*transformed_collision_point[i].y+m[1][2]*transformed_collision_point[i].z+t[1];
			tz=m[2][0]*transformed_collision_point[i].x+m[2][1]*transformed_collision_point[i].y+m[2][2]*transformed_collision_point[i].z+t[2];
			transformed_collision_point[i]=new Point3d(tx, ty, tz);
		}

	}



	public double[] calcOffset(Point3d pos, Vector3d normal, AbstractBoard board)
	{
		// find the cutting point with normal pointing against the board normal
		// if ambiguous choose cutting point closest to tool tip (0,0,0)

		double min_normal=1000;
		double min_distance=1000;
		double norm, dist;
		int selected_point=-1;
		for(int i=0;i<n-1;i++)
		{
			norm=Math.sqrt( (normal.x+transformed_cutting_normal[i].x)*(normal.x+transformed_cutting_normal[i].x)+
					(normal.y+transformed_cutting_normal[i].y)*(normal.y+transformed_cutting_normal[i].y)+
					(normal.z+transformed_cutting_normal[i].z)*(normal.z+transformed_cutting_normal[i].z));

			dist=Math.sqrt( transformed_cutting_point[i].x*transformed_cutting_point[i].x +
					transformed_cutting_point[i].y*transformed_cutting_point[i].y +
					transformed_cutting_point[i].z*transformed_cutting_point[i].z);

			if(norm<min_normal || (norm==min_normal && dist<min_distance) )
			{
				min_normal=norm;
				min_distance=dist;
				selected_point=i;
			}
		}

		Point3d offsetPoint = new Point3d(pos);
		//TODO: Add feature for mould milling? IE, sub or add...
//		offsetPoint.add(transformed_cutting_point[selected_point]);
		offsetPoint.sub(transformed_cutting_point[selected_point]);

		double[] ret = new double[]{offsetPoint.x, offsetPoint.y, offsetPoint.z};
		return ret;

	}


	public void setRotationAngle(double angle)
	{
	}

	public void setRotationMatrix(double[][] m)
	{
	}

	public void setTranslationVector(double[] t)
	{
	}


	public boolean checkCollision(Point3d pos, AbstractBoard board)
	{
		return false;
	}

	public BranchGroup get3DModel()
	{
		return null;
	}

	public void update3DModel()
	{
	}

	public void loadCutter(String filename)
	{

		n=0;
		cutting_point=new Point3d[10000];
		cutting_normal=new Vector3d[10000];

		File file = new File (filename);

		try {


			// Create a FileReader and then wrap it with BufferedReader.

			FileReader file_reader = new FileReader (file);
			BufferedReader buf_reader = new BufferedReader (file_reader);

			// Read cutter data

			String line;
			int pos=0;
			int pos2=0;

			do{

				//search for normal, i.e. start of new facet

				do{
					line=buf_reader.readLine();
					line.toLowerCase();
					pos=line.indexOf("normal");	// a new facet should start with "facet normal"
					pos2=line.indexOf("endsolid");	// the file should end with "endsolid"
				}while(pos==-1 && pos2==-1);


				if(pos>0)
				{


					//read normal
					cutting_normal[n]=new Vector3d(0.0, 0.0, 0.0);
					line=line.substring(pos+6);	// remove part before normal coordinates
					line=line.trim();		// remove leading spaces
					pos=line.indexOf(' ');		// find space after normal_x coordinate
					cutting_normal[n].x=Double.parseDouble(line.substring(0,pos));	// read normal_x
					line=line.substring(pos);	// remove normal_x
					line=line.trim();		// remove leading spaces
					pos=line.indexOf(' ');		// find space after normal_y coordinate
					cutting_normal[n].y=Double.parseDouble(line.substring(0,pos));	// read normal_y
					line=line.substring(pos);	// remove normal_y
					line=line.trim();		// remove leading spaces
					cutting_normal[n].z=Double.parseDouble(line);	// read normal_z
					for(int i=0;i<3;i++)
					{

						//read vertex
						do{
							line=buf_reader.readLine();
							line.toLowerCase();
							pos=line.indexOf("vertex");	// a new vertex should start with "vertex"
						}while(pos==-1);

						cutting_point[n]=new Point3d(0.0, 0.0, 0.0);
						line=line.substring(pos+6);	// remove part before vertex coordinates
						line=line.trim();		// remove leading spaces

						pos=line.indexOf(' ');		// find space after vertex_x coordinate
						cutting_point[n].x=Double.parseDouble(line.substring(0,pos));	// read vertex_x

						line=line.substring(pos);	// remove vertex_x
						line=line.trim();		// remove leading spaces
						pos=line.indexOf(' ');		// find space after vertex_y coordinate
						cutting_point[n].y=Double.parseDouble(line.substring(0,pos));	// read vertex_y

						line=line.substring(pos);	// remove vertex_y
						line=line.trim();		// remove leading spaces
						cutting_point[n].z=Double.parseDouble(line);	// read vertex_z

						n=n+1;
						cutting_normal[n]=new Vector3d(cutting_normal[n-1].x, cutting_normal[n-1].y,cutting_normal[n-1].z);

					}
				}

			}while(pos2==-1);

			buf_reader.close ();

		}
		catch (IOException e2) {
			System.out.println ("IO exception =" + e2 );
		}

	}


	public void loadCollisionCutter(String filename)
	{

		cn=0;
		collision_point=new Point3d[10000];

		File file = new File (filename);

		try {


			// Create a FileReader and then wrap it with BufferedReader.

			FileReader file_reader = new FileReader (file);
			BufferedReader buf_reader = new BufferedReader (file_reader);

			// Read cutter data

			String line;
			int pos=0;
			int pos2=0;

			do{

				//search for normal, i.e. start of new facet

				do{
					line=buf_reader.readLine();
					line.toLowerCase();
					pos=line.indexOf("normal");	// a new facet should start with "facet normal"
					pos2=line.indexOf("endsolid");	// the file should end with "endsolid"
				}while(pos==-1 && pos2==-1);


				if(pos>0)
				{


					//read normal
/*					cutting_normal[n]=new Vector3d(0.0, 0.0, 0.0);
					line=line.substring(pos+6);	// remove part before normal coordinates
					line=line.trim();		// remove leading spaces
					pos=line.indexOf(' ');		// find space after normal_x coordinate
					cutting_normal[n].x=Double.parseDouble(line.substring(0,pos));	// read normal_x
					line=line.substring(pos);	// remove normal_x
					line=line.trim();		// remove leading spaces
					pos=line.indexOf(' ');		// find space after normal_y coordinate
					cutting_normal[n].y=Double.parseDouble(line.substring(0,pos));	// read normal_y
					line=line.substring(pos);	// remove normal_y
					line=line.trim();		// remove leading spaces
					cutting_normal[n].z=Double.parseDouble(line);	// read normal_z
*/

					for(int i=0;i<3;i++)
					{

						//read vertex
						do{
							line=buf_reader.readLine();
							line.toLowerCase();
							pos=line.indexOf("vertex");	// a new vertex should start with "vertex"
						}while(pos==-1);

						collision_point[cn]=new Point3d(0.0, 0.0, 0.0);
						line=line.substring(pos+6);	// remove part before vertex coordinates
						line=line.trim();		// remove leading spaces

						pos=line.indexOf(' ');		// find space after vertex_x coordinate
						collision_point[cn].x=Double.parseDouble(line.substring(0,pos));	// read vertex_x

						line=line.substring(pos);	// remove vertex_x
						line=line.trim();		// remove leading spaces
						pos=line.indexOf(' ');		// find space after vertex_y coordinate
						collision_point[cn].y=Double.parseDouble(line.substring(0,pos));	// read vertex_y

						line=line.substring(pos);	// remove vertex_y
						line=line.trim();		// remove leading spaces
						collision_point[cn].z=Double.parseDouble(line);	// read vertex_z

						cn=cn+1;
//						cutting_normal[n]=new Vector3d(cutting_normal[n-1].x, cutting_normal[n-1].y,cutting_normal[n-1].z);

					}
				}

			}while(pos2==-1);

			buf_reader.close ();

		}
		catch (IOException e2) {
			System.out.println ("IO exception =" + e2 );
		}

	}


	//create a flat cutter and save as STL-file

	public void createFlatCutter(String filename, double tool_radius, int nr_triangles)
	{
		Point3d p1,p2,p3;
		Vector3d normal = new Vector3d();
		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();

		PrintStream dataOut;

		try
		{
			dataOut=new PrintStream(new File(filename));

			dataOut.println("solid cutter");

			double step_angle=360.0/(nr_triangles/2.0);

			for(double i=0;i<360.0;i=i+step_angle)
			{

				p1=new Point3d((tool_radius-0.1)*Math.cos((i+step_angle)*Math.PI/180.0), (tool_radius-0.1)*Math.sin((i+step_angle)*Math.PI/180.0), 0.0);
				p2=new Point3d(tool_radius*Math.cos((i+step_angle)*Math.PI/180.0), tool_radius*Math.sin((i+step_angle)*Math.PI/180.0), 0.1);
				p3=new Point3d(tool_radius*Math.cos(i*Math.PI/180.0), tool_radius*Math.sin(i*Math.PI/180.0), 0.1);

				v1.sub(p1, p2);
				v2.sub(p1, p3);
				normal.cross(v1, v2);
				normal.normalize();

				if(!(Double.toString(normal.x).equals("NaN")))
				{
					dataOut.println("facet normal " + Double.toString(normal.x) + " " + Double.toString(normal.y)+ " " + Double.toString(normal.z) + "");
					dataOut.println(" outer loop");
					dataOut.println("  vertex " + Double.toString(p1.x) + " " + Double.toString(p1.y) + " " + Double.toString(p1.z) + "");
					dataOut.println("  vertex " + Double.toString(p2.x) + " " + Double.toString(p2.y) + " " + Double.toString(p2.z) + "");
					dataOut.println("  vertex " + Double.toString(p3.x) + " " + Double.toString(p3.y) + " " + Double.toString(p3.z) + "");
					dataOut.println(" endloop");
					dataOut.println("endfacet");
				}


				p1=new Point3d((tool_radius-0.1)*Math.cos((i+step_angle)*Math.PI/180.0), (tool_radius-0.1)*Math.sin((i+step_angle)*Math.PI/180.0), 0.0);
				p2=new Point3d(tool_radius*Math.cos(i*Math.PI/180.0), tool_radius*Math.sin(i*Math.PI/180.0), 0.1);
				p3=new Point3d((tool_radius-0.1)*Math.cos(i*Math.PI/180.0), (tool_radius-0.1)*Math.sin(i*Math.PI/180.0), 0.0);

				v1.sub(p1, p2);
				v2.sub(p1, p3);
				normal.cross(v1, v2);
				normal.normalize();

				if(!(Double.toString(normal.x).equals("NaN")))
				{
					dataOut.println("facet normal " + Double.toString(normal.x) + " " + Double.toString(normal.y)+ " " + Double.toString(normal.z) + "");
					dataOut.println(" outer loop");
					dataOut.println("  vertex " + Double.toString(p1.x) + " " + Double.toString(p1.y) + " " + Double.toString(p1.z) + "");
					dataOut.println("  vertex " + Double.toString(p2.x) + " " + Double.toString(p2.y) + " " + Double.toString(p2.z) + "");
					dataOut.println("  vertex " + Double.toString(p3.x) + " " + Double.toString(p3.y) + " " + Double.toString(p3.z) + "");
					dataOut.println(" endloop");
					dataOut.println("endfacet");
				}



			}

			dataOut.println("endsolid cutter");


			}
			catch(IOException excep2)
			{
				System.out.println("Problem creating file");
			}



	}


}

