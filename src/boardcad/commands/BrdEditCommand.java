package boardcad.commands;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import boardcad.gui.jdk.BezierBoardDrawUtil;
import boardcad.gui.jdk.BoardCAD;
import boardcad.gui.jdk.BoardEdit;
import boardcad.i18n.LanguageResource;
import cadcore.BezierKnot;
import cadcore.BezierSpline;
import cadcore.VecMath;

public class BrdEditCommand extends BrdAbstractEditCommand
{
	static double KEY_MOVE_AMOUNT = 1.0f;

	ArrayList<BezierKnot> mControlPointsBeforeChange;
	Point2D.Double mDragStartPos;
	Point2D.Double mDragOffset;
	Point mBoxSelectStartPos;
	private int mWhich = 0;
	boolean mIsDragging = false;
	boolean mIsKeyEditing = false;
	boolean mIsBoxSelecting = false;
	int mCurrentKeyCode = 0;
	int mRepeat = 1;

	BrdPanCommand mPanCommand = new BrdPanCommand();
	BrdZoomCommand mZoomCommand = new BrdZoomCommand();

	boolean mIsPaning = false;

	final double MAX_OFF = 4.0f;

	public BrdEditCommand()
	{
	}

	public void execute()
	{
		mSource.onBrdChanged();	//adjusts rocker to zero and cross sections to width and thickness
		super.execute();
		mIsDragging = false;
		mIsKeyEditing = false;
		mCurrentKeyCode = 0;
		mControlPointsBeforeChange = null;
		mDragStartPos = null;
		mDragOffset = null;
		mSource = null;
		mRepeat = 1;
	}

	public void onSetCurrent()
	{
		BoardCAD.getInstance().getControlPointInfo().setEnabled(true);
	}

	public void onCurrentChanged()
	{
		BoardCAD.getInstance().getControlPointInfo().setEnabled(false);
	}

	void saveControlPointsBeforeChange(BoardEdit source)
	{
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();

//		Save the original points
		mControlPointsBeforeChange = new ArrayList<BezierKnot>();
		for(int i = 0; i < selectedControlPoints.size(); i++)
		{
			mControlPointsBeforeChange.add((BezierKnot)selectedControlPoints.get(i).clone());
		}
		super.saveBeforeChange(source.getCurrentBrd());
	}

	public void moveControlPoints(double dx, double dy, int which)
	{
		ArrayList<BezierKnot> selectedControlPoints = mSource.getSelectedControlPoints();
		if(selectedControlPoints.size() > 1 || which == 0)
		{
			for(int i = 0; i < selectedControlPoints.size(); i++)
			{
				BezierKnot sel = selectedControlPoints.get(i);
				BezierKnot org = mControlPointsBeforeChange.get(i);
				Point2D.Double endpoint = org.getEndPoint();

				sel.setControlPointLocation(endpoint.x + dx, endpoint.y + dy);
			}
		}
		else{
			// We know we only have a single point, so just use it directly
			selectedControlPoints.get(0).setLocation(which, mControlPointsBeforeChange.get(0).getPoints()[which].x + dx, mControlPointsBeforeChange.get(0).getPoints()[which].y + dy);

			if(selectedControlPoints.get(0).isContinous())
			{

				int other = (which == 1) ? 2 : 1;

				Point2D.Double otherVec = getSelectedControlPointVector(mSource, other);
				double otherVecLength = VecMath.getVecLength(otherVec);

				Point2D.Double currentVec = getSelectedControlPointVector(mSource, which);
				
				Point2D.Double newOtherVec = new Point2D.Double(currentVec.x, currentVec.y);
				VecMath.normalizeVector(newOtherVec);
				VecMath.scaleVector(newOtherVec, otherVecLength);

				Point2D.Double selectedEndPoint = mControlPointsBeforeChange.get(0).getPoints()[0];
				selectedControlPoints.get(0).setLocation(other, newOtherVec.x + selectedEndPoint.x, newOtherVec.y + selectedEndPoint.y);
			}
		}
		BoardCAD.getInstance().onBrdChanged();
		BoardCAD.getInstance().onControlPointChanged();
		mSource.repaint();
	}

	public void onLeftMouseButtonPressed(BoardEdit source, MouseEvent event)
	{
		//Select point
		Point pos = event.getPoint();
		Point2D.Double brdPos = source.screenCoordinateToBrdCoordinate(pos);

		BezierSpline[] splines = source.getActiveBezierSplines(BoardCAD.getInstance().getCurrentBrd());
		if(splines == null)
			return;

		BezierKnot bestMatch = null;
		boolean hitControlPoint = false;
		for(int i = 0; i < splines.length; i++)
		{
			bestMatch = splines[i].findBestMatch(brdPos);
			if(bestMatch != null)
			{
				setWhich(splines[0].getBestMatchWhich(brdPos));
				hitControlPoint = (double)brdPos.distance(bestMatch.getPoints()[getWhich()]) < (MAX_OFF/source.getScale());
				if(hitControlPoint)
					break;
			}
		}

		if(bestMatch == null)
		{
			mSource = source;
			mIsBoxSelecting = true;
			mBoxSelectStartPos = pos;
			return;
		}

		if(!hitControlPoint)
		{
			mSource = source;
			mIsBoxSelecting = true;
			mBoxSelectStartPos = pos;

			if(!(event.isShiftDown() || event.isControlDown()))
			{
				source.clearSelectedControlPoints();	//If shift is held, don't clear
			}

		}
		else
		{
			boolean alreadySelected = source.mSelectedControlPoints.contains(bestMatch);

			if(!alreadySelected && (!(event.isShiftDown() || event.isControlDown())) )
				source.clearSelectedControlPoints();	//If shift or control is held or the ControlPoint is already selected, don't clear

			if(event.isControlDown())
				source.toggleSelectedControlPoint(bestMatch);
			else
				source.addSelectedControlPoint(bestMatch);
		}

		if(hitControlPoint && source.mSelectedControlPoints.contains(bestMatch))
		{
			mDragStartPos = source.screenCoordinateToBrdCoordinate(pos);

			mDragOffset = new Point2D.Double(bestMatch.getPoints()[getWhich()].x-mDragStartPos.x,bestMatch.getPoints()[getWhich()].y-mDragStartPos.y);

		}

		BoardCAD.getInstance().onControlPointChanged();
		BoardCAD.getInstance().redraw();
	}


	public void onMouseDragged(BoardEdit source, MouseEvent event)
	{
	    if((event.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
	    	return;	    	
	    }
		
		//Dragging points
		if(mIsKeyEditing)
			return;

		if(mIsPaning)
		{
			mPanCommand.onMouseDragged(source, event);
			return;
		}

		if(mIsBoxSelecting)
		{
			mSource.setDrawZoomRectangle(mBoxSelectStartPos, event.getPoint());

			return;
		}
		

		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() == 0)
			return;

		if(mDragStartPos == null)
			return;

		Point pos = event.getPoint();
		Point2D.Double brdPos = source.screenCoordinateToBrdCoordinate(pos);

		if(mIsDragging == false)
		{
			saveControlPointsBeforeChange(source);

			mIsDragging = true;
			mSource = source;
		}

		brdPos.x += mDragOffset.x;
		brdPos.y += mDragOffset.y;

		double x_diff = (brdPos.x - mDragStartPos.x)*(event.isAltDown()?.1f:1f);
		double y_diff = (brdPos.y - mDragStartPos.y)*(event.isAltDown()?.1f:1f);

		moveControlPoints(x_diff, y_diff, getWhich());
	}

	public void onLeftMouseButtonReleased(BoardEdit source, MouseEvent event)
	{
		if(mIsBoxSelecting)
		{
			mSource.disableDrawZoomRectangle();

			Point pos = event.getPoint();
			Point2D.Double boxStartPos = source.screenCoordinateToBrdCoordinate(mBoxSelectStartPos);
			Point2D.Double boxEndPos = source.screenCoordinateToBrdCoordinate(pos);

			if(boxStartPos.x > boxEndPos.x)
			{
				double x = boxStartPos.x;
				boxStartPos.x = boxEndPos.x;
				boxEndPos.x = x;
			}

			if(boxStartPos.y > boxEndPos.y)
			{
				double y = boxStartPos.y;
				boxStartPos.y = boxEndPos.y;
				boxEndPos.y = y;
			}

			BezierSpline[] splines = source.getActiveBezierSplines(BoardCAD.getInstance().getCurrentBrd());
			if(splines == null)
				return;

			for(int k = 0; k < splines.length; k++)
			{
				for(int i = 0; i < splines[k].getNrOfControlPoints(); i++)
				{
					BezierKnot point = splines[k].getControlPoint(i);
					for(int j = 0; j < 3; j++)
					{
						Point2D.Double p = point.getPoints()[j];

						if(p.x > boxStartPos.x && p.x < boxEndPos.x && p.y > boxStartPos.y && p.y < boxEndPos.y )	//Check within box
						{
							setWhich(j);

							if(event.isControlDown())
								source.toggleSelectedControlPoint(point);
							else
								source.addSelectedControlPoint(point);

							break;
						}
					}
				}
			}

			BoardCAD.getInstance().onControlPointChanged();
			source.repaint();
			mIsBoxSelecting = false;
		}

		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() == 0)
			return;

		if(mIsDragging == true)
		{
			execute();
		}
	}

	public void onMouseWheelMoved(BoardEdit source, MouseWheelEvent event)
	{
		int scroll = event.getWheelRotation();

		int steps = Math.abs(scroll);
		for(int i = 0; i < steps; i++)
		{
			if(scroll < 0)
			{
				mZoomCommand.zoomInStep(source, event.isAltDown());
			}
			else
			{
				mZoomCommand.zoomOutStep(source, event.isAltDown());
			}

		}
		event.consume();
	}

	public void onMouseWheelButtonPressed(BoardEdit source, MouseEvent event)
	{
		mPanCommand.onLeftMouseButtonPressed(source, event);
		mIsPaning = true;
	}

	public void onMouseWheelButtonReleased(BoardEdit source, MouseEvent event)
	{
		mPanCommand.onLeftMouseButtonReleased(source, event);
		mIsPaning = false;
	}
	
	public Point2D.Double getControlPointVector(BoardEdit source, int which){
		Point2D.Double vec = new Point2D.Double();
		BezierKnot knot = (mControlPointsBeforeChange == null || mControlPointsBeforeChange.size() == 0) ? source.getSelectedControlPoints().get(0) : mControlPointsBeforeChange.get(0);
		Point2D.Double[] points = knot.getPoints();
		VecMath.subVector(points[which], points[0], vec);
		return vec;
	}
	
	public Point2D.Double getSelectedControlPointVector(BoardEdit source, int which){
		Point2D.Double vec = new Point2D.Double();
		BezierKnot knot = source.getSelectedControlPoints().get(0);
		Point2D.Double[] points = knot.getPoints();
		VecMath.subVector(points[which], points[0], vec);
		return vec;
	}


	public boolean onKeyEvent(BoardEdit source, KeyEvent event)
	{
		if(mIsDragging)
			return false;

		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() == 0)
		{
			return mPanCommand.onKeyEvent(source, event);
		}

		double mulX = ((source.mDrawControl&(BezierBoardDrawUtil.FlipX)) != 0)?-1.0f:1.0f;
		double mulY = ((source.mDrawControl&(BezierBoardDrawUtil.FlipY)) != 0)?-1.0f:1.0f;
		int which = getWhich();


		if(event.getID() == KeyEvent.KEY_PRESSED)
		{
			int key = event.getKeyCode();

			if(mIsKeyEditing == false)
			{
				saveControlPointsBeforeChange(source);

				mIsKeyEditing = true;
				mCurrentKeyCode = key;

				mRepeat = 1;

				mSource = source;
			}
			
			double movement = (KEY_MOVE_AMOUNT/source.getScale())*(event.isAltDown()?.1f:1f);

			switch(key)
			{
			case KeyEvent.VK_LESS:
				if(selectedControlPoints.size() > 1 || mIsKeyEditing == true)
					return false;
				
				setWhich(event.isShiftDown() ? (++which % 3) : (--which < 0 ? 2 : which));

				source.repaint();
				break;
				
			case KeyEvent.VK_C:
				if(selectedControlPoints.size() > 1 || mIsKeyEditing == true)
					return false;

				BezierSpline[] splines = source.getActiveBezierSplines(BoardCAD.getInstance().getCurrentBrd());
				if(splines == null)
					return false;

				for(int i = 0; i < splines.length; i++)
				{
					int currentIndex = splines[i].indexOf(selectedControlPoints.get(0));
					if(currentIndex == -1)
						continue;

					int newIndex = ++currentIndex%splines[i].getNrOfControlPoints();

					selectedControlPoints.clear();

					selectedControlPoints.add(splines[i].getControlPoint(newIndex));

					source.repaint();
				}
				break;

			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				moveControlPoints(-movement*mulX*mRepeat, 0, which);
				break;

			case KeyEvent.VK_S:
			case KeyEvent.VK_RIGHT:
				moveControlPoints(movement*mulX*mRepeat, 0, which);
				break;

			case KeyEvent.VK_D:
			case KeyEvent.VK_UP:
				moveControlPoints(0, -movement*mulY*mRepeat, which);
				break;

			case KeyEvent.VK_F:
			case KeyEvent.VK_DOWN:
				moveControlPoints(0, movement*mulY*mRepeat, which);
				break;

			case KeyEvent.VK_E:
				if(selectedControlPoints.size() > 1)
					return false;

				extendControlPoint(source, movement*mRepeat, which);				
				break;
				
			case KeyEvent.VK_R:
				if(selectedControlPoints.size() > 1)
					return false;

				extendControlPoint(source, -movement*mRepeat, which);				
				break;

			case KeyEvent.VK_Q:
				if(selectedControlPoints.size() > 1)
					return false;

				rotateControlPoint(source, (Math.PI/180.0)*mRepeat*(event.isAltDown() ? .1 : 1), which);
				break;
				
			case KeyEvent.VK_W:
				if(selectedControlPoints.size() > 1)
					return false;

				rotateControlPoint(source, -(Math.PI/180.0)*mRepeat*(event.isAltDown() ? .1 : 1), which);
				break;


			default:
				mIsKeyEditing = false;
				return false;
			}
			
			mRepeat++;

			BoardCAD.getInstance().onControlPointChanged();
			source.repaint();

			return true;
		}
		else if(event.getID() == KeyEvent.KEY_RELEASED)
		{
			if(mIsKeyEditing == true)
			{
				if(mCurrentKeyCode == event.getKeyCode())
				{
					execute();
				}

			}

		}
		return false;
	}

	public void setContinous(BoardEdit source, boolean continous)
	{
		if(source.getSelectedControlPoints().size() == 0)
			return;

		mSource = source;

		saveControlPointsBeforeChange(mSource);

		mSource.getSelectedControlPoints().get(0).setContinous(continous);

		moveControlPoints(0, 0, (getWhich() == 0)?1:getWhich());	//If endpoint selected, select tangent

		execute();
	}

	public void setControlPoint(BoardEdit source, Point2D.Double pos)
	{
		mSource = source;

		saveControlPointsBeforeChange(mSource);

		double dx = pos.x - mControlPointsBeforeChange.get(0).getPoints()[getWhich()].x;
		double dy = pos.y - mControlPointsBeforeChange.get(0).getPoints()[getWhich()].y;

		moveControlPoints(dx, dy, getWhich());

		execute();
	}

	public void extendControlPoint(BoardEdit source, double distance, int which){
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() > 1)
			return;
		
		if(which == 0) {
			extendControlPoint(source, distance, 1);
			extendControlPoint(source, distance, 2);
			return;
		}

		Point2D.Double pointVec = getControlPointVector(source, which);
		double length = VecMath.getVecLength(pointVec);
		
		double scale = 1 + (distance / length);
		
		scaleControlPoint(source, scale,  which);
	}

	
	public void scaleControlPoint(BoardEdit source, double scale, int which){
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(which == 0 || selectedControlPoints.size() > 1)
			return;

		Point2D.Double pointVec = getControlPointVector(source, which);
		
		Point2D.Double scaledVec = new Point2D.Double(pointVec.x, pointVec.y);
		VecMath.scaleVector(scaledVec, scale);

		moveControlPoints(pointVec.x - scaledVec.x, pointVec.y - scaledVec.y, which);

		BoardCAD.getInstance().onControlPointChanged();
	}
	
	public void rotateControlPoint(BoardEdit source, double rotAngle, int which){
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() > 1)
			return;
		
		if(which == 0) {
			rotateControlPoint(source, rotAngle, 1);
			if(selectedControlPoints.get(0).isContinous() == false) {
				rotateControlPoint(source, rotAngle, 2);				
			}
		}

		Point2D.Double pointVec = getControlPointVector(source, which);
		
		Point2D.Double rotVec = new Point2D.Double(pointVec.x, pointVec.y);
		VecMath.rotateVector(rotVec, rotAngle);

		moveControlPoints(pointVec.x - rotVec.x, pointVec.y - rotVec.y, which);

		BoardCAD.getInstance().onControlPointChanged();
	}

	public void rotateControlPointToAngle(BoardEdit source, double targetAngle, int which)
	{
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(which == 0 || selectedControlPoints.size() > 1)
			return;

		Point2D.Double pointVec = getControlPointVector(source, which);
		
		Point2D.Double horAxis = new Point2D.Double(1.0, 0.0);

		double pointAngle = VecMath.getVectorAngle(horAxis, pointVec);

		double rotAngle = targetAngle - pointAngle;
		
		rotateControlPoint(source, rotAngle, which);
	}

	public void rotateControlPointToHorizontal(BoardEdit source)
	{
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() != 1)
			return;

		mSource = source;

		saveControlPointsBeforeChange(mSource);

		BezierKnot current = selectedControlPoints.get(0);

		double nextLength = current.getTangentToNextLength();
		double prevLength = current.getTangentToPrevLength();
		double x = current.getEndPoint().x;
		double y = current.getEndPoint().y;

		double tanPrevSign = (current.getTangentToPrev().x-x>0)?1:-1;
		double tanNextSign = (current.getTangentToNext().x-x>=0)?1:-1;

		if(getWhich() == 0 || getWhich() == 1)
		{
			current.setTangentToPrev((prevLength*tanPrevSign)+x, y);
			if(current.isContinous())
			{
				current.setTangentToNext((-nextLength*tanPrevSign)+x, y);
			}
		}
		else if(getWhich() == 2 || current.isContinous())
		{
			current.setTangentToNext((nextLength*tanNextSign)+x, y);
			if(current.isContinous())
			{
				current.setTangentToPrev((-prevLength*tanNextSign)+x, y);
			}
		}

		BoardCAD.getInstance().onControlPointChanged();
		execute();
	}

	public void rotateControlPointToVertical(BoardEdit source)
	{
		ArrayList<BezierKnot> selectedControlPoints = source.getSelectedControlPoints();
		if(selectedControlPoints.size() != 1)
			return;

		mSource = source;

		saveControlPointsBeforeChange(mSource);

		BezierKnot current =  selectedControlPoints.get(0);

		double nextLength = current.getTangentToNextLength();
		double prevLength = current.getTangentToPrevLength();
		double x = current.getEndPoint().x;
		double y = current.getEndPoint().y;

		double tanPrevSign = (current.getTangentToPrev().y-y>0)?1:-1;
		double tanNextSign = (current.getTangentToNext().y-y>=0)?1:-1;

		if(getWhich() == 0 || getWhich() == 1)
		{
			current.setTangentToPrev(x, (prevLength*tanPrevSign)+y);
			if(current.isContinous())
			{
				current.setTangentToNext(x, (-nextLength*tanPrevSign)+y);
			}
		}
		if(getWhich() == 0 || getWhich() == 2)
		{
			current.setTangentToNext(x, (nextLength*tanNextSign)+y);
			if(current.isContinous())
			{
				current.setTangentToPrev(x, (-prevLength*tanNextSign)+y);

			}
		}

		BoardCAD.getInstance().onControlPointChanged();
		execute();
	}

	public void redo()
	{
		super.redo();
	}

	public void undo()
	{
		super.undo();
	}

	public Object clone(){
		BrdEditCommand cmd = null;

		cmd =  (BrdEditCommand)super.clone();

//		The arrays are instantiated in this class so there is no need to copy them

		return cmd;
	}

	public String getCommandString()
	{
		return LanguageResource.getString("EDITCMD_STR");
	}

	public int getWhich() {
		return mWhich;
	}

	public void setWhich(int mWhich) {
		this.mWhich = mWhich;
	}

}