package boardcad.commands;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import boardcad.gui.jdk.BoardEdit;
import boardcad.i18n.LanguageResource;
import boardcad.settings.BoardCADSettings;
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
		BezierCurve nextCurve = mControlPoints.getCurve(mIndex);
		if(BoardCADSettings.getInstance().isUsingBezierFitOnDelete()) {					
			for(int i = 0; i <= steps; i++)
			{
				double t = (double)i/(double)steps;
				points.add(new Point2D.Double(prevCurve.getXValue(t),prevCurve.getYValue(t)) );
			}
			for(int i = 0; i <= steps; i++)
			{
				double t = (double)i/(double)steps;
				points.add(new Point2D.Double(nextCurve.getXValue(t), nextCurve.getYValue(t)) );
			}
		}
		
		BezierKnot prev =  mControlPoints.getControlPoint(mIndex-1);
		BezierKnot next =  mControlPoints.getControlPoint(mIndex+1);

		mSource.mSelectedControlPoints.remove(mDeletedControlPoint);
		
		double l = prevCurve.getLength() + nextCurve.getLength();

		super.removePoint(mDeletedControlPoint);

		mControlPoints.remove(mDeletedControlPoint);
		
		if(BoardCADSettings.getInstance().isUsingBezierFitOnDelete()) {				
			//Pass to bezierFit
			BezierFit fitter = new BezierFit();
			Point2D[] ctrlPoints = fitter.bestFit(points);

			//Update bezier curve 
			prev.setContinous(false);
			prev.setControlPointLocation(ctrlPoints[0].getX(),ctrlPoints[0].getY());
			prev.setTangentToNext(ctrlPoints[1].getX(),ctrlPoints[1].getY());
			next.setContinous(false);	
			next.setControlPointLocation(ctrlPoints[3].getX(),ctrlPoints[3].getY());
			next.setTangentToPrev(ctrlPoints[2].getX(),ctrlPoints[2].getY());
		} else {
			for(int i = 0; i < 1000; i++) {
				double newLength = prevCurve.getLength();
				if(Math.abs(newLength - l) < 0.1)break;
				double factor = l / newLength;
				prev.scaleTangentToNext(factor);
				next.scaleTangentToPrev(factor);			
			} 
		}

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