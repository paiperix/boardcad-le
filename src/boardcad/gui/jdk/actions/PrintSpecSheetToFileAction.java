package boardcad.gui.jdk.actions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import boardcad.FileTools;
import boardcad.gui.jdk.BoardCAD;
import boardcad.gui.jdk.TwoValuesInputDialog;
import boardcad.i18n.LanguageResource;
import boardcad.print.PrintSpecSheet;

public final class PrintSpecSheetToFileAction extends AbstractAction {
	/**
	 * 
	 */
	private PrintSpecSheet mPrintSpecSheet;
	static final long serialVersionUID = 1L;
	
	public PrintSpecSheetToFileAction()
	{
		super();
		mPrintSpecSheet = new PrintSpecSheet();		
		putValue(Action.NAME, LanguageResource.getString("PRINTSPECSHEETTOFILE_STR"));
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		BoardCAD boardCad = BoardCAD.getInstance();

		TwoValuesInputDialog resDialog = new TwoValuesInputDialog(boardCad.getFrame());
		resDialog.setMessageText(LanguageResource.getString("PRINTSPECSHEETTOFILERESOLUTIONMSG_STR"));
		resDialog.setValue1(1200);
		resDialog.setValue2(1600);
		resDialog.setValue1LabelText(LanguageResource.getString("PRINTSPECSHEETTOFILEWIDTH_STR"));
		resDialog.setValue2LabelText(LanguageResource.getString("PRINTSPECSHEETTOFILEHEIGHT_STR"));
		resDialog.setModal(true);

		resDialog.setVisible(true);
		if (resDialog.wasCancelled()) {
			resDialog.dispose();
			return;
		}
		int width = 0, height = 0;
		try {
			width = (int) resDialog.getValue1();
			height = (int) resDialog.getValue2();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(),
					LanguageResource.getString("PRINTSPECSHEETTOFILEINVALIDPARAMETERSMSG_STR"),
					LanguageResource.getString("PRINTSPECSHEETTOFILEINVALIDPARAMETERSTITLE_STR"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		BufferedImage img = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();

		Graphics2D g2d = (Graphics2D) g.create();

		// Turn on anti-aliasing, so painting is smooth.

		g2d.setRenderingHint(

				RenderingHints.KEY_ANTIALIASING,

				RenderingHints.VALUE_ANTIALIAS_ON);

		Paper paper = new Paper();
		paper.setImageableArea(0, 0, width, height);
		paper.setSize(width, height);
		PageFormat myPageFormat = new PageFormat();
		myPageFormat.setPaper(paper);
		myPageFormat.setOrientation(PageFormat.LANDSCAPE);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, height - 1, width - 1);

		mPrintSpecSheet.print(g2d, myPageFormat, 0);

		final JFileChooser fc = new JFileChooser();

		fc.setCurrentDirectory(new File(BoardCAD.defaultDirectory));

		// Create a file dialog box to prompt for a new file to display
		FileFilter filter = new FileFilter() {

			// Accept all directories and graphics files.
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				String extension = FileTools.getExtension(f);
				if (extension != null && (extension.equals("png") || extension.equals("gif")
						|| extension.equals("bmp") || extension.equals("jpg"))) {
					return true;
				}

				return false;
			}

			// The description of this filter
			@Override
			public String getDescription() {
				return LanguageResource.getString("PRINTSPECSHEETTOFILEIMAGEFILES_STR");
			}
		};

		fc.setFileFilter(filter);

		int returnVal = fc.showSaveDialog(BoardCAD.getInstance().getFrame());
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		File file = fc.getSelectedFile();

		String filename = file.getPath(); // Load and display
		// selection
		if (filename == null)
			return;

		if (FileTools.getExtension(filename) == "") {
			filename = FileTools.setExtension(filename, "jpg");
		}

		BoardCAD.defaultDirectory = file.getPath();

		try {
			File outputfile = new File(filename);
			ImageIO.write(img, FileTools.getExtension(filename), outputfile);
		} catch (Exception e) {
			String str = LanguageResource.getString("PRINTSPECSHEETTOFILEERRORMSG_STR") + e.toString();
			JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(), str,
					LanguageResource.getString("PRINTSPECSHEETTOFILEERRORTITLE_STR"),
					JOptionPane.ERROR_MESSAGE);

		}
	}
}