package boardcad.commands;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import board.BezierBoard;
import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;
import cadcore.BezierCurve;
import cadcore.BezierFit;
import cadcore.BezierSpline;

public class BrdFitCurveCommand extends BrdAbstractEditCommand {

	public BrdFitCurveCommand() {

	}

	public void execute() {
		mPreviousCommand = BoardCAD.getInstance().getCurrentCommand();

		fitCurve();

		super.execute();
	}

	public void fitCurve() {
		BezierBoard brd = BoardCAD.getInstance().getCurrentBrd();
		saveBeforeChange(brd);
		
		boolean isCrossSection = BoardCAD.getInstance().getSelectedEdit().mIsCrossSectionEdit;

		ArrayList<Point2D.Double> gpArray = BoardCAD.getInstance()
				.getSelectedEdit().getGuidePoints();
		BezierSpline[] bs = BoardCAD.getInstance().getSelectedEdit()
				.getActiveBezierSplines(brd);

		for (int i = 0; i < bs.length; i++) {
			for (int k = 0; k < bs[i].getNrOfCurves(); k++) {
				BezierCurve curve = bs[i].getCurve(k);

				double xmin = 0.0;
				double xmax = 0.0;
				double ymin = -10000000.0;
				double ymax = 10000000.0;
				if(isCrossSection)
				{
					xmin = curve.getMinX();
					xmax = curve.getMaxX();
					ymin = curve.getMinY();
					ymax = curve.getMaxY();
				}
				else
				{
					xmin = curve.getStartKnot().getEndPoint().getX();
					xmax = curve.getEndKnot().getEndPoint().getX();
				}
			
				ArrayList<Point2D> currentGuidePoints = new ArrayList<Point2D>();

				for (int l = 0; l < gpArray.size(); l++) {
					Point2D.Double p = gpArray.get(l);

					if (p.x >= xmin && p.x <= xmax && p.y >= ymin
							&& p.y <= ymax) {
						currentGuidePoints.add(p);
					}
				}
				if (currentGuidePoints.isEmpty()) // No points
					continue;

				currentGuidePoints.add(curve.getStartKnot().getEndPoint());
				currentGuidePoints.add(curve.getStartKnot().getEndPoint());
				currentGuidePoints.add(curve.getEndKnot().getEndPoint());
				currentGuidePoints.add(curve.getEndKnot().getEndPoint());

				Collections.sort(currentGuidePoints, new Comparator<Point2D>() {
					public int compare(Point2D p1, Point2D p2) {
						return (p1.getX() > p2.getX()) ? 1 : -1;
					}
				});

				// Pass to bezierFit
				BezierFit fitter = new BezierFit();
				Point2D[] ctrlPoints = fitter.bestFit(currentGuidePoints);
				
				System.out.printf("before: start endpoint: %f, %f\n", curve.getStartKnot().getEndPoint().getX(), curve.getStartKnot().getEndPoint().getY());
				System.out.printf("before: start tangent to next: %f, %f\n", curve.getStartKnot().getTangentToNext().getX(),curve.getStartKnot().getTangentToNext().getY());
				System.out.printf("before: endknot tangent to prev: %f, %f\n", curve.getEndKnot().getTangentToPrev().getX(),curve.getEndKnot().getTangentToPrev().getY());
				System.out.printf("before: endknot endpoint: %f, %f\n", curve.getEndKnot().getEndPoint().getX(),curve.getEndKnot().getEndPoint().getY());
				System.out.printf("ctrlPoints[0]: %f, %f\n", ctrlPoints[0].getX(),ctrlPoints[0].getY());
				System.out.printf("ctrlPoints[1]: %f, %f\n", ctrlPoints[1].getX(),ctrlPoints[1].getY());
				System.out.printf("ctrlPoints[2]: %f, %f\n", ctrlPoints[2].getX(),ctrlPoints[2].getY());
				System.out.printf("ctrlPoints[3]: %f, %f\n", ctrlPoints[3].getX(),ctrlPoints[3].getY());

				// Update bezier curve
				curve.getStartKnot().setContinous(false);
				curve.getStartKnot().setControlPointLocation(ctrlPoints[0].getX(), ctrlPoints[0].getY());
				curve.getStartKnot().setTangentToNext(ctrlPoints[1].getX(), ctrlPoints[1].getY());
				curve.getEndKnot().setContinous(false);
				curve.getEndKnot().setControlPointLocation(ctrlPoints[3].getX(), ctrlPoints[3].getY());
				curve.getEndKnot().setTangentToPrev(ctrlPoints[2].getX(), ctrlPoints[2].getY());

				System.out.printf("After: start endpoint: %f, %f\n", curve.getStartKnot().getEndPoint().getX(), curve.getStartKnot().getEndPoint().getY());
				System.out.printf("After: start tangent to next: %f, %f\n", curve.getStartKnot().getTangentToNext().getX(),curve.getStartKnot().getTangentToNext().getY());
				System.out.printf("After: endknot tangent to prev: %f, %f\n", curve.getEndKnot().getTangentToPrev().getX(),curve.getEndKnot().getTangentToPrev().getY());
				System.out.printf("After: endknot endpoint: %f, %f\n", curve.getEndKnot().getEndPoint().getX(),curve.getEndKnot().getEndPoint().getY());
			}
		}

		saveChanges();
	}

	@Override
	public String getCommandString() {
		return LanguageResource.getString("FITCURVECMD_STR");
	}

}
