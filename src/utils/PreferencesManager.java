/**
* <h1>PreferencesManager
* <p>allow the user to edit font,font size ,background and foreground color of UI elements
* @ author kounelios13	
*/
package utils;




import gui.CustomColorChooser;
import gui.FileCopyManager;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
	private FileCopyManager appFrame;
	private FileHandler fh = new FileHandler();
	private ResourceLoader rc = new ResourceLoader(fh);
	private boolean settingsLoaded = false;
	public static String sep = File.separator + File.separator;
	private Color bgColor, fgColor;
	private Message msg = new Message();
	private JPanel prefPanel = new JPanel();
	private JSlider buttonSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 18),
			labelSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 18);
	private CustomColorChooser colorChooser = new CustomColorChooser(appFrame,
			this);
	private File settingsFile = new File("app" + sep + "settings.dat"),
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
	private Font[] fonts =UIPreferences.fonts;
	public boolean exists(){
		return settingsFile.exists();
	}
	private void updateSliders() {
		settings.setBtnSize(buttonSlider.getValue());
		settings.setLblSize(labelSlider.getValue());
		updatePreview();
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
		buttonSlider.addChangeListener((e) -> updateSliders());
		labelSlider.addChangeListener((e) -> updateSliders());
		btnSample.addActionListener((e) -> msg.info(prefPanel,"Stop pressing me. \n I won't do anything", "Idiot alert"));
	}
	/**
	 * @wbp.parser.constructor
	 */
	public PreferencesManager(FileCopyManager frame) {
		super("Preferences");
		appFrame = frame;
		init();
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
		this.setLocationRelativeTo(null);
		this.pack();
	}
	@Override
	public void loadPreferences(){
		
		/*
		 * Since we use a proxy if an exception is thrown the program will not start
		 * so by returning if something happens we can start our program normally
		 */
		if(rc.getPreferences() == null || settingsLoaded)
			return;
		settingsLoaded = true;
		settings = rc.getPreferences();
		if(!settings.isFontAvailable() && settings.getFontName() != null)
			msg.error(null,settings.getFontName()+" font is not available on this system.");
		bgColor = settings.getBgColor();
		fgColor = settings.getFgColor();
		labelSlider.setValue(settings.getLblSize());
		buttonSlider.setValue(settings.getBtnSize());
		int i = 0;
		for (Font f : fonts)
			if (f.getFontName().equals(settings.getFontName()))
				break;
			else
				i++;
		fontCombo.setSelectedIndex(i>=fonts.length?settings.getFontIndex("Arial"):i);
		updatePreview();
		applySettings();	
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
		bgColor = settings.getBgColor();
		fgColor = settings.getFgColor();
		/*int btnSize = settings.getBtnSize(),
				lblSize = settings.getLblSize();*/
		Font btn = settings.getButtonFont(),
			 lbl = settings.getLabelFont();
		for (JButton b : appFrame.getButtons()) {
			b.setFont(btn);	
			b.setBackground(bgColor);
			b.setForeground(fgColor);
		}
		for (JLabel label : appFrame.getLabels()) {
			label.setFont(lbl);
			label.setForeground(fgColor);
		}
		appFrame.pack();
	}
	public void updatePreview() {
		Font bFont = settings.getButtonFont(),
			 lFont = settings.getLabelFont();
		bgColor = settings.getBgColor();
		fgColor = settings.getFgColor();
		btnSample.setFont(bFont);
		lblSample.setFont(lFont);
		btnSample.setBackground(bgColor);
		btnSample.setForeground(fgColor);
		lblSample.setForeground(fgColor);
		this.pack();
	}
	public void setBg(Color c) {
		settings.setBgColor(c);
	}
	public void setFg(Color c) {
		settings.setFgColor(c);
	}
	private String toCol(Color c) {
		return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue()+ ")";
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
		 	msg.info(null,"Operation aborted");
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
			str.append("Background color:" +ls+ toCol(bgColor)+ls);
			str.append("Foreground color:" +ls+ toCol(fgColor) +ls);
			writer.write(str.toString());
			writer.close();
		} catch (IOException exc) {
			fh.log(exc.getMessage());
		}
	}
	public void deleteAppSettings() {
		File dir = new File("app");
		File[] contents = dir.listFiles();
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
		for (File f :contents)
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
		loadPreferences();
		appFrame.restart();
	}
	public void prepareUI(){
		loadPreferences();
		updatePreview();
		applySettings();
	}
}
@SuppressWarnings({"serial"})
class Settings implements Serializable {
	private Color bg, fg;
	private String fontName;
	private int lblSize = 12, btnSize = 18;
	private Font[] fonts = UIPreferences.fonts;
	public boolean isFontAvailable(){
		for(Font f:fonts)
			if(f.getFontName().equals(fontName))
				return true;
		return false;	
	}
	public int getFontIndex(String name){
		for(int i=0;i<fonts.length;i++)
			if(fonts[i].getFontName().equals(name))
				return i;
		return -1;	
	}
	public void setBgColor(Color e) {
		bg = e;
	}
	public void setFgColor(Color e) {
		fg = e;
	}
	public Color getBgColor() {
		return bg != null ? bg:new Color(238,238,238);
	}
	public Color getFgColor() {
		return fg != null ? fg:new Color(51,51,51);
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
