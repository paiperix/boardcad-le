package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import boardcad.gui.jdk.BoardCAD;
import boardcad.i18n.LanguageResource;

public final class BoardSaveAsAction extends AbstractAction {
	static final long serialVersionUID = 1L;

	public BoardSaveAsAction() {
		super();
		putValue(Action.NAME, LanguageResource.getString("BOARDSAVEAS_STR"));
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("boardcad/icons/save-as.png")));
	}

	@Override
	public void actionPerformed(final ActionEvent ev) {
		BoardCAD boardCAD = BoardCAD.getInstance();

		final JFileChooser fc = new JFileChooser();

		fc.setCurrentDirectory(new File(BoardCAD.defaultDirectory));
		fc.setSelectedFile(new File(boardCAD.getCurrentBrd().getFilename()));

		final int returnVal = fc.showSaveDialog(boardCAD.getFrame());
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		final File file = fc.getSelectedFile();

		final String filename = file.getPath(); // Load and display
		// selection
		if (filename == null)
			return;

		BoardCAD.defaultDirectory = file.getPath();

		boardCAD.saveAs(filename);
	}
}