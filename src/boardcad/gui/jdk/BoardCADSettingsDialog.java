package boardcad.gui.jdk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JDialog;

import boardcad.i18n.LanguageResource;
import boardcad.settings.BoardCADSettings;

class BoardCADSettingsDialog extends JDialog
{
	/**
	 *
	 */
	private static final long serialVersionUID = 813932272331942542L;

	BoardCADSettingsDialog()
	{
		final BoardCADSettings settings = BoardCADSettings.getInstance();
		this.setTitle(LanguageResource.getString("BOARDCADSETTINGSTITLE_STR"));
		this.setSize(new Dimension(500, 500));
		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/boardcad/icons/BoardCAD icon 32x32.png"));
		this.setIconImage(icon);
		this.setLayout(new BorderLayout());
		this.add(new CategorizedSettingsComponent(settings), BorderLayout.CENTER);
		this.setLocationRelativeTo(null);
	}

}