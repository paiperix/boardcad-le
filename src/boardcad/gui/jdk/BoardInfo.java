package boardcad.gui.jdk;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import board.BezierBoard;
import boardcad.i18n.LanguageResource;


public class BoardInfo extends JDialog {
	static final long serialVersionUID=1L;

	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JTextField mDesignerTextField = null;
	private JTextField mSurferTextField = null;
	private JTextField mModelTextField = null;
	private JTextArea mCommentsTextField = null;
	private JButton OkButton = null;
	private JButton CancelButton = null;
	BezierBoard mBrd;
	/**
	 * This method initializes 
	 * 
	 */
	public BoardInfo(BezierBoard brd) {
		super();
		mBrd = brd;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(464, 410));
        this.setContentPane(getJContentPane());
        this.setTitle(LanguageResource.getString("BOARDINFORTITLE_STR"));
        this.setLocationRelativeTo(null);     
        getDesignerTextField().setText(mBrd.getDesigner());
        getSurferTextField().setText(mBrd.getSurfer());
        getModelTextField().setText(mBrd.getModel());
        getCommentsTextField().setText(mBrd.getComments());

	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(24, 108, 132, 16));
			jLabel3.setHorizontalTextPosition(SwingConstants.LEFT);
			jLabel3.setText(LanguageResource.getString("BOARDCOMMENTS_STR"));
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(-3, 78, 88, 20));
			jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel2.setText(LanguageResource.getString("BOARDMODEL_STR"));
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(-3, 47, 88, 20));
			jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel1.setText(LanguageResource.getString("BOARDSURFER_STR"));
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(-3, 16, 88, 20));
			jLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel.setText(LanguageResource.getString("BOARDDESIGNER_STR"));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(jLabel3, null);
			jContentPane.add(getDesignerTextField(), null);
			jContentPane.add(getSurferTextField(), null);
			jContentPane.add(getModelTextField(), null);
			jContentPane.add(getCommentsTextField(), null);
			jContentPane.add(getOkButton(), null);
			jContentPane.add(getCancelButton(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mDesignerTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDesignerTextField() {
		if (mDesignerTextField == null) {
			mDesignerTextField = new JTextField();
			mDesignerTextField.setBounds(new Rectangle(88, 16, 351, 20));
		}
		return mDesignerTextField;
	}

	/**
	 * This method initializes mSurferTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSurferTextField() {
		if (mSurferTextField == null) {
			mSurferTextField = new JTextField();
			mSurferTextField.setBounds(new Rectangle(88, 47, 351, 20));
		}
		return mSurferTextField;
	}

	/**
	 * This method initializes mModelTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getModelTextField() {
		if (mModelTextField == null) {
			mModelTextField = new JTextField();
			mModelTextField.setBounds(new Rectangle(88, 78, 351, 20));
		}
		return mModelTextField;
	}

	/**
	 * This method initializes mCommentsTextField	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getCommentsTextField() {
		if (mCommentsTextField == null) {
			mCommentsTextField = new JTextArea();
			mCommentsTextField.setBounds(new Rectangle(23, 127, 416, 198));
			mCommentsTextField.setLineWrap(true);
		}
		return mCommentsTextField;
	}

	/**
	 * This method initializes OkButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (OkButton == null) {
			OkButton = new JButton();
			OkButton.setBounds(new Rectangle(199, 339, 110, 26));
			OkButton.setText(LanguageResource.getString("OKBUTTON_STR"));
			OkButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					mBrd.setDesigner(getDesignerTextField().getText());
					mBrd.setSurfer(getSurferTextField().getText());
					mBrd.setModel(getModelTextField().getText());
					mBrd.setComments(getCommentsTextField().getText());
					setVisible(false);
				}
			});
		}
		return OkButton;
	}

	/**
	 * This method initializes CancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (CancelButton == null) {
			CancelButton = new JButton();
			CancelButton.setBounds(new Rectangle(329, 339, 110, 26));
			CancelButton.setText(LanguageResource.getString("CANCELBUTTON_STR"));
			CancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return CancelButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
