package boardcam.cutters;

import board.AbstractBoard;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


public class FlatCutter extends AbstractCutter
{
	private double toolradie;

	public FlatCutter()
	{
		toolradie=10;
	}

	public void init()
	{
	}

	public void setRadius(double radius)
	{
		toolradie=radius;
	}


	public double[] calcOffset(Point3d pos, Vector3d normal, AbstractBoard board)
	{

		Point3d offsetPoint = new Point3d(pos);

		offsetPoint.x=offsetPoint.x+(toolradie*normal.x/Math.sqrt(normal.x*normal.x+normal.y*normal.y));
		offsetPoint.y=offsetPoint.y+(toolradie*normal.y/Math.sqrt(normal.x*normal.x+normal.y*normal.y));

		double[] ret = new double[]{offsetPoint.x, offsetPoint.y, offsetPoint.z};

		return ret;

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


}

