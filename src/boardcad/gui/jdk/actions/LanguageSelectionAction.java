package boardcad.gui.jdk.actions;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import boardcad.gui.jdk.BoardCAD;
import boardcad.gui.jdk.ComboBoxDialog;
import boardcad.i18n.LanguageResource;

public final class LanguageSelectionAction extends AbstractAction {
	static protected Locale[] mSupportedLanguages = { new Locale("en", ""), new Locale("fr", ""), new Locale("pt", ""),
			new Locale("es", ""), new Locale("no", ""), new Locale("nl", "") };

	static final long serialVersionUID = 1L;
	{
		this.putValue(Action.NAME, LanguageResource.getString("LANGUAGE_STR"));
		// this.putValue(Action.ACCELERATOR_KEY,
		// KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ComboBoxDialog languageDlg = new ComboBoxDialog(BoardCAD.getInstance().getFrame());
		languageDlg.setTitle(LanguageResource.getString("LANGUAGE_STR"));
		languageDlg.setMessageText(LanguageResource.getString("SELECTLANGUAGE_STR"));

		String[] languages = new String[mSupportedLanguages.length];
		for (int i = 0; i < mSupportedLanguages.length; i++) {
			languages[i] = mSupportedLanguages[i].getDisplayName();
		}

		languageDlg.setItems(languages);

		final Preferences prefs = Preferences.userNodeForPackage(BoardCAD.class);
		String languageStr = prefs.get("Language", "en");
		String selectedLanguage = "English";
		int i;
		for (i = 0; i < mSupportedLanguages.length; i++) {
			if (mSupportedLanguages[i].getLanguage().equals(languageStr)) {
				selectedLanguage = mSupportedLanguages[i].getDisplayName();
				break;
			}
		}
		languageDlg.setSelectedItem(selectedLanguage);

		languageDlg.setModal(true);

		languageDlg.setVisible(true);
		if (!languageDlg.wasCancelled()) {
			selectedLanguage = languageDlg.getSelectedItem();

			for (i = 0; i < mSupportedLanguages.length; i++) {
				if (mSupportedLanguages[i].getDisplayName().equals(selectedLanguage)) {
					languageStr = mSupportedLanguages[i].getLanguage();
					break;
				}
			}

			prefs.put("Language", languageStr);

			JOptionPane.showMessageDialog(BoardCAD.getInstance().getFrame(),
					LanguageResource.getString("LANGUAGECHANGEDMSG_STR"),
					LanguageResource.getString("LANGUAGECHANGEDTITLE_STR"), JOptionPane.INFORMATION_MESSAGE);
		}

		languageDlg.dispose();
		BoardCAD.getInstance().getFrame().repaint();
	}
}