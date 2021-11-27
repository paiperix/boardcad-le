package boardcad.commands;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import boardcad.gui.jdk.BoardEdit;
import boardcad.i18n.LanguageResource;
import cadcore.BezierCurve;
import cadcore.BezierFit;
import cadcore.BezierKnot;
import cadcore.BezierSpline;

public class BrdDeleteControlPointCommand extends BrdAbstractEditCommand
{
	BezierKnot mDeletedControlPoint;
	BezierSpline mControlPoints;
	int mIndex;

	public BrdDeleteControlPointCommand(BoardEdit source, BezierKnot ControlPoint, BezierSpline ControlPoints)
	{
		mSource = source;

		mDeletedControlPoint = ControlPoint;

		mControlPoints = ControlPoints;
	}

	public void doAction()
	{
		super.saveBeforeChange(mSource.getCurrentBrd());
		
		mIndex = mControlPoints.indexOf(mDeletedControlPoint);

		ArrayList<Point2D> points = new ArrayList<Point2D>();
				
		int steps = 1000;
		
		BezierCurve prevCurve = mControlPoints.getCurve(mIndex-1);
		for(int i = 0; i <= steps; i++)
		{
			double t = (double)i/(double)steps;
			points.add(new Point2D.Double(prevCurve.getXValue(t),prevCurve.getYValue(t)) );
		}
		BezierCurve nextCurve = mControlPoints.getCurve(mIndex);
		for(int i = 0; i <= steps; i++)
		{
			double t = (double)i/(double)steps;
			points.add(new Point2D.Double(nextCurve.getXValue(t), nextCurve.getYValue(t)) );
		}
		
		BezierKnot prev =  mControlPoints.getControlPoint(mIndex-1);
		BezierKnot next =  mControlPoints.getControlPoint(mIndex+1);

		mSource.mSelectedControlPoints.remove(mDeletedControlPoint);

		super.removePoint(mDeletedControlPoint);

		mControlPoints.remove(mDeletedControlPoint);
					
		//Pass to bezierFit
		BezierFit fitter = new BezierFit();
		Point2D[] ctrlPoints = fitter.bestFit(points);
		
		System.out.printf("prev endpoint: %f, %f\n", prev.getEndPoint().getX(),prev.getEndPoint().getY());
		System.out.printf("prev tangent to next: %f, %f\n", prev.getTangentToNext().getX(),prev.getTangentToNext().getY());
		System.out.printf("next tangent to prev: %f, %f\n", next.getTangentToPrev().getX(),next.getTangentToPrev().getY());
		System.out.printf("next endpoint: %f, %f\n", next.getEndPoint().getX(),next.getEndPoint().getY());
		System.out.printf("ctrlPoints[0]: %f, %f\n", ctrlPoints[0].getX(),ctrlPoints[0].getY());
		System.out.printf("ctrlPoints[1]: %f, %f\n", ctrlPoints[1].getX(),ctrlPoints[1].getY());
		System.out.printf("ctrlPoints[2]: %f, %f\n", ctrlPoints[2].getX(),ctrlPoints[2].getY());
		System.out.printf("ctrlPoints[3]: %f, %f\n", ctrlPoints[3].getX(),ctrlPoints[3].getY());
	
		//Update bezier curve 
		prev.setContinous(false);
		prev.setControlPointLocation(ctrlPoints[0].getX(),ctrlPoints[0].getY());
		prev.setTangentToNext(ctrlPoints[1].getX(),ctrlPoints[1].getY());
		next.setContinous(false);	
		next.setControlPointLocation(ctrlPoints[3].getX(),ctrlPoints[3].getY());
		next.setTangentToPrev(ctrlPoints[2].getX(),ctrlPoints[2].getY());

		super.saveChanges();
	}

	public void execute()
	{		
		doAction();

		mSource.onBrdChanged();

		super.execute();
	}

	public void undo()
	{
		mControlPoints.insert(mIndex, mDeletedControlPoint);

		super.undo();
	}

	public void redo()
	{
		mControlPoints.remove(mDeletedControlPoint);

		super.redo();
	}

	public String getCommandString()
	{
		return LanguageResource.getString("DELETECONTROLPOINTCMD_STR");

	}
}