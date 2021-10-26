package boardcad.gui.jdk;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

//=========================================================Status Panel
public class StatusPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JLabel mStatusText;
	private String mMode;
	private int mX,mY,mZ;

	public StatusPanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT,2,0));
		mStatusText = new JLabel("");
		mMode="";
		mX=0;
		mY=0;
		mZ=0;
		add(mStatusText);
	}

	public void setMode(String text)
	{
		mMode = text;
		updateStatus();
	}

	public void setCoordinates(double x_coord, double y_coord, double z_coord)
	{
		mX = (int)x_coord;
		mY = (int)y_coord;
		mZ = (int)z_coord;
		updateStatus();
	}

	private void updateStatus()
	{
		String text = mMode + "  x:" + mX + " y:" + mY + " z:" + mZ;
		mStatusText.setText(text);
	}
}

