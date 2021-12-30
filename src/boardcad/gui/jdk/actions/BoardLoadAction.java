package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import board.BezierBoard;
import boardcad.FileTools;
import boardcad.gui.jdk.BoardCAD;
import boardcad.gui.jdk.BoardFileView;
import boardcad.gui.jdk.BoardPreview;
import boardcad.i18n.LanguageResource;
import board.readers.*;

public class BoardLoadAction extends AbstractAction {
	static final long serialVersionUID=1L;

	BezierBoard mBrd = null;
	BezierBoard mCloneBrd = null;

	BoardLoadAction()
	{
		init();
	};

	public BoardLoadAction(BezierBoard brd)
	{
		mBrd = brd;
		init();
	}

	public BoardLoadAction(BezierBoard brd, BezierBoard cloneBrd)
	{
		mBrd = brd;
		mCloneBrd = cloneBrd;
		
		init();
	}
	
	private void init() {
		putValue(Action.NAME, LanguageResource.getString("BOARDOPEN_STR"));
		putValue(Action.SHORT_DESCRIPTION, LanguageResource.getString("BOARDOPEN_STR"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
	}


	@Override
	public void actionPerformed(ActionEvent event)
	{
//		Create a file dialog box to prompt for a new file to display
		FileFilter boardFilesFilter = new FileFilter()
		{

//			Accept all directories and brd and s3d files.
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				String extension = FileTools.getExtension(f);
				if (extension != null && (extension.equals("brd") || extension.equals("s3d") || extension.equals("s3dx") || extension.equals("srf") || extension.equals("cad") || extension.equals("stp") || extension.equals("step")))
				{
					return true;
				}

				return false;
			}

//			The description of this filter
			@Override
			public String getDescription() {
				return "Board files";
			}



		};

		FileFilter allFilesFilter = new FileFilter()
		{

//			Accept all directories and brd and s3d files.
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				String extension = FileTools.getExtension(f);
				if (extension != null)
				{
					return true;
				}

				return false;
			}

//			The description of this filter
			@Override
			public String getDescription() {
				return "All files";
			}


		};
		
		BoardCAD boardCAD = BoardCAD.getInstance();

		int r = boardCAD.saveChangedBoard();
		if (r == -1 || r == 2) // closed dialog or cancel button pressed
			return;


		final JFileChooser fc = new JFileChooser();
		fc.setFileView(new BoardFileView());
		fc.setAccessory(new BoardPreview(fc));
		fc.addChoosableFileFilter(boardFilesFilter);
		fc.addChoosableFileFilter(allFilesFilter);

		fc.setAcceptAllFileFilterUsed(false);

		fc.setCurrentDirectory(new File(BoardCAD.defaultDirectory));

		int returnVal = fc.showOpenDialog(boardCAD.getFrame());
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		File file = fc.getSelectedFile();

		String filename = file.getPath();    // Load and display selection
		if(filename == null)
			return;

		load(filename);

		boardCAD.getMenuBar().addRecentBoardFile(filename);

		boardCAD.fitAll();
		boardCAD.onBrdChanged();
		boardCAD.onControlPointChanged();
		boardCAD.setBoardChanged(false);
		boardCAD.updateBezier3DModel();
		boardCAD.redraw();

		
	}

	public void load(String filename)
	{
		BoardCAD.defaultDirectory = filename;

		String ext = FileTools.getExtension(filename);

		int ret = 0;
		String errorStr="";
		if(ext.compareToIgnoreCase("s3d")==0)
		{
			ret = S3dReader.loadFile(mBrd, filename);

			if(ret == 1)	//Show warning dialog
			{
				JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(), LanguageResource.getString("S3DTHICKNESSCURVENOTSUPPOSTEDMSG_STR"), LanguageResource.getString("S3DTHICKNESSCURVENOTSUPPOSTEDTITLE_STR"), JOptionPane.WARNING_MESSAGE);
			}

			if(ret < 0)
			{
				errorStr = S3dReader.getErrorStr();
			}
		}
		else if(ext.compareToIgnoreCase("s3dx")==0)
		{
			ret = S3dxReader.loadFile(mBrd, filename);

			if(ret == 1)	//Show warning dialog
			{
				JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(), LanguageResource.getString("S3DTHICKNESSCURVENOTSUPPOSTEDMSG_STR"), LanguageResource.getString("S3DTHICKNESSCURVENOTSUPPOSTEDTITLE_STR"), JOptionPane.WARNING_MESSAGE);
			}

			if(ret < 0)
			{
				errorStr = S3dReader.getErrorStr();
			}
		}
		else if(ext.compareToIgnoreCase("srf")==0)
		{
			ret = SrfReader.loadFile(mBrd, filename);
			if(ret < 0)
			{
				errorStr = SrfReader.getErrorStr();
			}
		}
		else if(ext.compareToIgnoreCase("brd")==0)
		{
			ret = BrdReader.loadFile(mBrd, filename);
			if(ret < 0)
			{
				errorStr = BrdReader.getErrorStr();
			}
		} else {
			errorStr = LanguageResource.getString("UNKOWNFILEFORMAT_STR");
		}

		if(ret < 0)
		{
	        JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(), errorStr, LanguageResource.getString("READBRDFAILEDTITLE_STR"), JOptionPane.ERROR_MESSAGE);
		}

		if(mCloneBrd != null)
		{
			mCloneBrd.set(mBrd);
		}

	}
}

