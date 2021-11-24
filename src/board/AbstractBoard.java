package board;

import org.jogamp.vecmath.*;

public abstract class AbstractBoard
{
	abstract public double getLength();

	abstract public double getWidthAt(double x);
	abstract public double getDeckAt(double x, double y);
	abstract public double getBottomAt(double x, double y);
	abstract public Vector3f getDeckNormalAt(double x, double y);
	abstract public Vector3f getBottomNormalAt(double x, double y);

}
