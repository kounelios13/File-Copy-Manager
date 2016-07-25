//TODO
/*fIX THIS HELL For God's shake
 rewrite everything
 *
 */
package utilities;

import gui.CustomColorChooser;
import gui.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import messages.Message;
import net.miginfocom.swing.MigLayout;
@SuppressWarnings({"static-access", "serial"})
public class PreferencesManager extends JFrame implements UIPreferences {
	private GUI appFrame;
	private FileHandler fh = new FileHandler();
	public static String sep = File.separator + File.separator;
	private Color bgColor, fgColor;
	private Message msg = new Message();
	private JPanel prefPanel = new JPanel();
	private JSlider buttonSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 18),
			labelSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 18);
	private CustomColorChooser colorChooser = new CustomColorChooser(appFrame,
			this);
	public static File settingsFile = new File("app" + sep + "settings.dat"),
			dir = new File("app");
	private Settings settings = new Settings();
	private DefaultComboBoxModel<String> fontModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> fontCombo = new JComboBox<String>(fontModel);
	private JButton saveSettings = new JButton("Save settings"),
			applySettings = new JButton("Apply Settings"),
			loadSettings = new JButton("Load settings"),
			chooseColors = new JButton("Choose colors");
	private JLabel lblButtonFontSize = new JLabel("Button font size");
	private JLabel lblLabelFontSize = new JLabel("Label font size");
	private JButton btnSample = new JButton("Button Sample");
	private JLabel lblSample = new JLabel("Label Sample");
	private Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	private void updateSliders() {
		settings.setBtnSize(buttonSlider.getValue());
		settings.setLblSize(labelSlider.getValue());
	}
	private void init() {
		for (Font f : fonts)
			fontModel.addElement(f.getFontName());
		saveSettings.addActionListener((e) -> savePreferences());
		loadSettings.addActionListener((e) -> loadPreferences());
		applySettings.addActionListener((e) ->	applySettings());
		fontCombo.addActionListener((e) -> {
			settings.setFontName((String) fontCombo.getSelectedItem());
			updatePreview();
		});
		chooseColors.addActionListener((e) -> colorChooser.setVisible(true));
		buttonSlider.addChangeListener((e) -> {
			updateSliders();
			updatePreview();
			this.pack();
		});
		labelSlider.addChangeListener((e) -> {
			updateSliders();
			updatePreview();
			this.pack();
		});
		btnSample.addActionListener((e) -> msg.info(prefPanel,"Stop pressing me. \n I won't do anything", "Idiot alert"));
	}
	/**
	 * @wbp.parser.constructor
	 */
	public PreferencesManager(GUI frame, File sFile) {
		super("Preferences");
		appFrame = frame;
		init();
		if (sFile != null && sFile.isFile() && sFile.canWrite()
				&& sFile.canRead())
			settingsFile = sFile;
		prefPanel.setLayout(new MigLayout("", "[97px][97px]",
				"[][][][][][23px][][]"));
		prefPanel.add(fontCombo, "cell 0 0,growx,aligny center");
		prefPanel
				.add(lblButtonFontSize, "cell 0 1,alignx center,aligny center");
		prefPanel.add(buttonSlider, "cell 0 2,growx");
		prefPanel.add(btnSample, "cell 1 2");
		prefPanel.add(lblLabelFontSize, "cell 0 3,alignx center,aligny center");
		prefPanel.add(labelSlider, "cell 0 4,growx");
		prefPanel.add(lblSample, "cell 1 4,alignx center");
		prefPanel.add(loadSettings, "flowy,cell 0 5,growx,aligny top");
		this.setContentPane(prefPanel);
		prefPanel.add(chooseColors, "cell 1 5,growx,aligny top");
		prefPanel.add(saveSettings, "cell 0 7,growx,aligny top");
		prefPanel.add(applySettings, "cell 1 7,growx,aligny top");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}
	@Override
	public void loadPreferences() {
		if (!settingsFile.exists()) {
			return;
		}
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(settingsFile));
			settings = (Settings) in.readObject();
			bgColor = settings.getBgColor();
			fgColor = settings.getFgColor();
			in.close();
			labelSlider.setValue(settings.getLblSize());
			buttonSlider.setValue(settings.getBtnSize());
			int i = 0;
			for (Font f : fonts)
				if (f.getFontName().equals(settings.getFontName()))
					break;
				else
					i++;
			fontCombo.setSelectedIndex(i);
			updatePreview();
			applySettings();
		} catch (InvalidClassException | ClassNotFoundException e) {
			boolean d = settingsFile.delete();
			msg.error(
					prefPanel,
					"Settings come from an older version of program that is not supported.Please choose new settings and press 'Save'",
					"Unsupported settings");
			if (d)
				msg.info(prefPanel, "Old file deleted", "Success");
			fh.log(e.getMessage());
		} catch (IOException e) {
			msg.error(prefPanel, "Can't load preferences", "Error");
			fh.log(e.getMessage());
		}
	}
	@Override
	public void savePreferences() {
		if (!dir.exists())
			dir.mkdirs();
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(settingsFile));
			out.writeObject(settings);
			out.close();
		} catch (FileNotFoundException e) {
			fh.log(e.getMessage());
			try {
				settingsFile.createNewFile();
			} catch (IOException e1) {
				msg.error(null, "Can't save preferences");
				fh.log(e1.getMessage());
			} finally {
				if (settingsFile.exists()) {
					msg.info(null, "Created preferences file", "Status");
					// Created needed file.Re-execute to save
					savePreferences();
				}
				applySettings();
			}
		} catch (IOException io) {
			msg.error(null, "IOException occured", "IO");
			fh.log(io.getMessage());
		}
		this.setVisible(false);
	}
	public void editPreferences() {
		loadPreferences();
		this.setVisible(true);
	}
	public void applySettings() {
		for (JButton b : appFrame.getButtons()) {
			b.setFont(settings.getButtonFont());
			bgColor = settings.getBgColor();
			fgColor = settings.getFgColor();
			if (bgColor != null)
				b.setBackground(bgColor);
			if (fgColor != null)
				b.setForeground(fgColor);
		}
		for (JLabel lbl : appFrame.getLabels()) {
			lbl.setFont(settings.getLabelFont());
			if (fgColor != null)
				lbl.setForeground(settings.getFgColor());
		}
		appFrame.pack();
	}
	public void updatePreview() {
		Font bFont = settings.getButtonFont();
		Font lFont = settings.getLabelFont();
		btnSample.setFont(bFont);
		lblSample.setFont(lFont);
		btnSample.setBackground(settings.getBgColor());
		btnSample.setForeground(settings.getFgColor());
		lblSample.setForeground(settings.getFgColor());
		this.pack();
	}
	public void setBg(Color c) {
		settings.setBgColor(c);
	}
	public void setFg(Color c) {
		settings.setFgColor(c);
	}
	public String toCol(Color c) {
		return "rgb(" + c.getRed() + "," + c.getBlue() + "," + c.getBlue()+ ")";
	}
	public void exportSettings() {
		File dir = new File("app");
		if (!dir.exists()) {
			msg.error(prefPanel, "There are no setiings saved by user");
			return;
		}
		StringBuilder str = null;
		JFileChooser ch = new JFileChooser();
		ch.setCurrentDirectory(new File("app"));
		ch.setDialogTitle("Choose wher to export file");
		ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		ch.setApproveButtonText("Select");
		int n = ch.showOpenDialog(null);
		if (n != JFileChooser.APPROVE_OPTION) {
			System.out.println("exited");
			return;
		}
		File f = new File(ch.getSelectedFile() + File.separator
				+ File.separator + "export.txt");
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (Exception e) {
				fh.log(e.getMessage());
				return;
			}
		try {
			String ls =System.lineSeparator();
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write("------File Copy Manager Preferences------\n\n\n\n");
			writer.write(ls);
			str = new StringBuilder();
			str.append("Font name:" + settings.getFontName()+ls+ "Button Font Size:"+ls+ settings.getBtnSize()+ls);
			str.append("Label Font Size:"+ls+ settings.getLblSize() +ls+ls);
			if (bgColor != null)
				str.append("Background color:" +ls+ toCol(bgColor)+ls);
			if (fgColor != null)
				str.append("Foreground color:" +ls+ toCol(fgColor) +ls);
			writer.write(str.toString());
			writer.close();
		} catch (IOException exc) {
			fh.log(exc.getMessage());
		}
	}
	public void deleteAppSettings() {
		File dir = new File("app");
		if (!dir.exists() || dir.listFiles().length < 1) {
			msg.error(prefPanel, "No files to delete");
			return;
		}
		boolean delete = JOptionPane
				.showConfirmDialog(null,
						"Are you sure you want to delete settings and app related files?") == JOptionPane.OK_OPTION;
		if (!delete) {
			msg.error(prefPanel, "Operation cancelled");
			return;
		}

		if (!dir.exists()) {
			msg.error(prefPanel, "No app settings found", "Error");
			return;
		}
		// instead of just return if dir does not exist display inform the user
		// that there are no app related files available to delete
		for (File f : dir.listFiles())
			f.delete();
		if (dir.listFiles().length == 0)
			msg.info(prefPanel, "All app related files have been deleted",
					"Success");
		else
			msg.error(prefPanel, "Could not delete all files", "Failure");
		if (dir.delete())
			msg.info(prefPanel, "App folder deleted", "Success");
		else
			msg.error(prefPanel, "Could not delete app directory", "Failed!!!");
	}
}
@SuppressWarnings({"serial"})
class Settings implements Serializable {
	private Color bg, fg;
	private String fontName;
	private int lblSize = 12, btnSize = 18;
	public void setBgColor(Color e) {
		bg = e;
	}
	public void setFgColor(Color e) {
		fg = e;
	}
	public Color getBgColor() {
		return bg;
	}
	public Color getFgColor() {
		return fg;
	}
	public int getLblSize() {
		return lblSize;
	}
	public void setLblSize(int lblSize) {
		this.lblSize = lblSize;
	}
	public int getBtnSize() {
		return btnSize;
	}
	public void setBtnSize(int btnSize) {
		this.btnSize = btnSize;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public Font getButtonFont() {
		return new Font(fontName, Font.PLAIN, btnSize);
	}
	public Font getLabelFont() {
		return new Font(fontName, Font.PLAIN, lblSize);
	}
}
