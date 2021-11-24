package cadcore;

import org.jogamp.vecmath.*;

import board.BezierBoard;


abstract public class AbstractBezierBoardSurfaceModel
{
	//LinearInterpolation is obsolete, keep for reference
	public enum ModelType{LinearInterpolation, ControlPointInterpolation, SLinearInterpolation};

	static private BezierBoardControlPointInterpolationSurfaceModel mControlPointInterpolationInstance = new BezierBoardControlPointInterpolationSurfaceModel();

	static private BezierBoardSLinearInterpolationSurfaceModel mSLinearInterpolationInstance = new BezierBoardSLinearInterpolationSurfaceModel();

	static public AbstractBezierBoardSurfaceModel getBezierBoardSurfaceModel(ModelType modelType)
	{

		switch(modelType)
		{

		default:
		case ControlPointInterpolation:
			return mControlPointInterpolationInstance;

		case SLinearInterpolation:
			return mSLinearInterpolationInstance;
		}
	}

	public abstract Point3d getDeckAt(final BezierBoard brd, final double x, final double y);
	public Vector3f getDeckNormalAt(final BezierBoard brd, final double x, final double y)
	{
		final double OFFSET = 0.01;

		boolean flipNormal = false;
		double xo = x-OFFSET;
		if(xo < 0)
		{
			xo = x+OFFSET;
			flipNormal = !flipNormal;
		}
		double yo = y-OFFSET;
		if(yo < 0)
		{
			yo = y+OFFSET;
			flipNormal = !flipNormal;
		}

		Point3d p1 = getDeckAt(brd,x,y);
		Point3d p2 = getDeckAt(brd,x,yo);
		Point3d p3 = getDeckAt(brd,xo,y);

		Vector3f pv = new Vector3f(0f, (float)(y-yo), (float)(p2.z-p1.z));
		Vector3f lv = new Vector3f((float)(x-xo), 0f, (float)(p3.z-p1.z));

		Vector3f normalVec = new Vector3f();
		normalVec.cross(pv,lv);
		normalVec.normalize();

		if(flipNormal)
		{
			normalVec.scale(-1.0f);
		}

		return normalVec;
	}

	public abstract Point3d getBottomAt(final BezierBoard brd, final double x, final double y);
	public Vector3f getBottomNormalAt(final BezierBoard brd, final double x, final double y)
	{
		final double OFFSET = 0.01;

		boolean flipNormal = false;
		double xo = x-OFFSET;
		if(xo < 0)
		{
			xo = x+OFFSET;
			flipNormal = !flipNormal;
		}
		double yo = y-OFFSET;
		if(yo < 0)
		{
			yo = y+OFFSET;
			flipNormal = !flipNormal;
		}

		Point3d p1 = getBottomAt(brd,x,y);
		Point3d p2 = getBottomAt(brd,x,yo);
		Point3d p3 = getBottomAt(brd,xo,y);

		Vector3f pv = new Vector3f(0f, (float)(y-yo), (float)(p2.z-p1.z));
		Vector3f lv = new Vector3f((float)(x-xo), 0f, (float)(p3.z-p1.z));

		pv.cross(pv,lv);
		Vector3f normalVec = new Vector3f(pv);
		normalVec.normalize();

		if(flipNormal)
		{
			normalVec.scale(-1.0f);
		}

		return normalVec;
	}

	public abstract Point3d getPointAt(final BezierBoard brd, double x, double s, double minAngle, double maxAngle, boolean useMinimumAngleOnSharpCorners);
	public Vector3f getNormalAt(final BezierBoard brd, double x, double s, double minAngle,  double maxAngle, boolean useMinimumAngleOnSharpCorners)
	{
		final double X_OFFSET = 0.1;
		final double S_OFFSET = 0.01;

		boolean flipNormal = false;

		double so = s+S_OFFSET;

		if(so > 1.0)
		{
			so = s-S_OFFSET;
			flipNormal = true;
		}

		if(x < 1.0)
		{
			x = .0;
		}
		if(x > brd.getLength() - 1.0)
		{
			x = brd.getLength() - 1.0;
		}
		double xo = x+X_OFFSET;

		Point3d p = getPointAt(brd, x, s, minAngle, maxAngle, useMinimumAngleOnSharpCorners);
		Point3d pso = getPointAt(brd, x, so, minAngle, maxAngle, useMinimumAngleOnSharpCorners);
		Point3d pxo = getPointAt(brd, xo, s, minAngle, maxAngle, useMinimumAngleOnSharpCorners);

		Vector3f vc = new Vector3f(0f, (float)(p.y-pso.y), (float)(p.z-pso.z));		//Vector across
		Vector3f vl = new Vector3f((float)(pxo.x-p.x), (float)(pxo.y-p.y), (float)(pxo.z-p.z));	//Vector lengthwise

		vc.cross(vc,vl);
		Vector3f normalVec = new Vector3f(vc);
		normalVec.normalize();

		if(flipNormal == true)
		{
			normalVec.scale(-1.0f);
		}

		return normalVec;
	}

	public abstract double getCrosssectionAreaAt(final BezierBoard brd,double x, int splits);
}
