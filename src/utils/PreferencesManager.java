package utils;
import static messages.Message.error;
import static messages.Message.info;
import static messages.Message.warning;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import org.apache.commons.io.FileUtils;
import extra.XString;
import gui.ApplicationScreen;
import gui.CustomColorChooser;
import gui.View;
import interfaces.UIPreferences;
import net.miginfocom.swing.MigLayout;
import serializable.Settings;
@SuppressWarnings({"static-access", "serial"})
public class PreferencesManager extends View implements UIPreferences{
	private ApplicationScreen appFrame;
	private Color bgColor = new Color(238,238,238),
			      fgColor = new Color(51,51,51);
	public static String sep = File.separator + File.separator;
	private FileHandler fh = new FileHandler();
	private ResourceLoader rc = new ResourceLoader(fh);
	private JPanel  prefPanel 	 = new JPanel();
	private JSlider buttonSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 18),
					labelSlider  = new JSlider(JSlider.HORIZONTAL, 1, 100, 18);
	private CustomColorChooser colorChooser = new CustomColorChooser(this);
	private File settingsFile = new File("app" + sep + "settings.dat"),
						  dir = new File("app");
	private Settings settings = new Settings();
	private DefaultComboBoxModel<String> fontModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> fontCombo = new JComboBox<String>(fontModel);
	private JButton 
	        saveAndApplySettings  =  new JButton("Save & apply settings"),
			loadSettings  =  new JButton("Load settings"),
			chooseColors  =  new JButton("Choose colors"),
				applyOnly =  new JButton("Apply settings"),
				btnSample =  new JButton("Button Sample");
	private JLabel lblButtonFontSize = new JLabel("Button font size"),
				   lblLabelFontSize  = new JLabel("Label font size"),
				   lblSample		 = new JLabel("Label Sample");
	private Font[] fonts = UIPreferences.fonts;
	public boolean exists(){
		return settingsFile.exists();
	}
	private boolean isNull(Object ...o){
		return FileHandler.isNull(o);
	}
	private void updateSliders() {
		settings.setBtnSize(buttonSlider.getValue());
		settings.setLblSize(labelSlider.getValue());
		updatePreview();
	}
	private void setColors(){
		bgColor = settings.getBgColor();
		fgColor = settings.getFgColor();
	}
	private void createFontList(){
		for (Font f : fonts)
			fontModel.addElement(f.getFontName());
	}
	protected void initUIElements() {
		createFontList();
		saveAndApplySettings.addActionListener((e) -> savePreferences());
		loadSettings.addActionListener((e) -> loadPreferences());
		fontCombo.addActionListener((e) -> {
			settings.setFontName((String) fontCombo.getSelectedItem());
			updatePreview();
		});
		chooseColors.addActionListener((e) -> colorChooser.setVisible(true));
		buttonSlider.addChangeListener((e) -> updateSliders());
		labelSlider.addChangeListener((e) -> updateSliders());
		btnSample.addActionListener((e) ->{
			info("Do not press me","Useless alert");
		});
		applyOnly.addActionListener((e)->applySettings());
	}
	/**
	 * @wbp.parser.constructor
	 * @param frame An instance of ApplicationFrame(JFrame) to work on
	 */
	public PreferencesManager(ApplicationScreen frame) {
		super("Preferences",600,800,false);
		if(isNull(frame))
			throw new IllegalArgumentException("You have to provide an instance of JFrame to work on.");
		appFrame = frame;
		initUIElements();
		prefPanel.setLayout(new MigLayout("", "[97px][97px]",
				"[][][][][][23px][][]"));
		prefPanel.add(fontCombo, "cell 0 0,growx,aligny center");
		prefPanel.add(lblButtonFontSize, "cell 0 1,alignx center,aligny center");
		prefPanel.add(buttonSlider, "cell 0 2,growx");
		prefPanel.add(btnSample, "cell 1 2");
		prefPanel.add(lblLabelFontSize, "cell 0 3,alignx center,aligny center");
		prefPanel.add(labelSlider, "cell 0 4,growx");
		prefPanel.add(lblSample, "cell 1 4,alignx center");
		prefPanel.add(loadSettings, "flowy,cell 0 5,growx,aligny top");
		prefPanel.add(applyOnly,"cell 1 5,growx,aligny top");
		prefPanel.add(saveAndApplySettings, "cell 0 7,growx,aligny top");
		prefPanel.add(chooseColors, "cell 1 7,growx,aligny top");
		this.setContentPane(prefPanel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.pack();
	}
	@Override
	public String toString(){
		return "PreferencesManager";
	}
	@Override
	public void loadPreferences(){
		/*
		 * Since we use a proxy if an exception is thrown the program will not start
		 * so by returning if something happens we can start our program normally
		 */
		if(isNull(rc.getPreferences()) )
			return;
		settings = rc.getPreferences();
		if(!settings.isFontAvailable() && !isNull(settings.getFontName()))
			error(settings.getFontName()+" font is not available on this system.");
		setColors();
		/**
		 * Important note:
		 * <i>Do</i> <em>NOT</em> use buttonSlider.setValue(settings.getBtnSize())
		 * First save the size in a variable and use the variable
		 * */
		int btnSize   = settings.getBtnSize(),
			labelSize = settings.getLblSize();
		labelSlider.setValue(labelSize);
		buttonSlider.setValue(btnSize);
		int i = 0;
		for (Font f : fonts)
			if (f.getFontName().equals(settings.getFontName()))
				break;
			else
				i++;
		/*
		 * Arial font is fail safe 
		 * So revert to that font if something goes wrong
		 * **/
		int backupFontIndex = settings.getFontIndex("Arial");
		fontCombo.setSelectedIndex(i>=fonts.length?backupFontIndex:i);
	}
	@Override
	public void savePreferences(){
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
				error("Can't save preferences");
				fh.log(e1.getMessage());
			} finally {
				if (settingsFile.exists()) {
					info("Created preferences file");
					// Created needed file.Re-execute to save
					savePreferences();
				}
			}
		} catch (IOException io) {
			error( "IOException occured", "IO");
			fh.log(io.getMessage());
		}
		if(!settingsFile.exists())
		{
			/**
			 * Apply current settings even if we can't save them
			 * but let the user know it
			 * */
			String msg = "Settings will be applied but they could not be saved.Next time you open the program these settings will not exist";
			warning(msg);
			fh.log(msg);
		}
		applySettings();
		this.setVisible(false);
	}
	public void editPreferences() {
		this.setVisible(true);
	}
	public void applySettings() {
		setColors();
		Font btnFont = settings.getButtonFont(),
			 lblFont = settings.getLabelFont();
		Stream.of(appFrame.getButtons()).forEach(b->{
			/**
			 * We use a stream since we don't care about the order of the elements
			 * */
			b.setFont(btnFont);	
			b.setBackground(bgColor);
			b.setForeground(fgColor);
		});
		Stream.of(appFrame.getLabels()).forEach(label->{
			label.setFont(lblFont);
			label.setForeground(fgColor);
		});
		appFrame.pack();
	}
	public void updatePreview() {
		Font bFont = settings.getButtonFont(),
			 lFont = settings.getLabelFont();
		setColors();
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
	public String toCol(Color c) {
		return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue()+ ")";
	}
	public void exportSettings() {
		File dir = new File("app");
		if (!dir.exists()) {
			error(prefPanel, "There are no setiings saved by user");
			return;
		}
		XString str = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("app"));
		chooser.setDialogTitle("Select a folder to export preferences report");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setApproveButtonText("Select");
		int n = chooser.showOpenDialog(null);
		if (n != JFileChooser.APPROVE_OPTION) {
		 	info("Operation aborted");
			return;
		}
		File f = new File(chooser.getSelectedFile() + File.separator
				+ File.separator + "export.rtf");
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (Exception e) {
				fh.log(e);
				return;
			}
		try {
			// \r\n is the new line character recognized by NotePad
			String ls ="\r\n";
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));		
			str = new XString(); 
			str.append("------File Copy Manager Preferences------\n\n")
			.append("\tFont name:" + settings.getFontName()+ls+ "\tButton Font Size:"+ settings.getBtnSize()+ls)
			.append("\tLabel Font Size:"+ settings.getLblSize()+ls);
			str.append("\tBackground color:"+ toCol(bgColor)+ls);
			str.append("\tForeground color:"+ toCol(fgColor) +ls);
			writer.write(str.toString());
			writer.close();
		} catch (IOException exc) {
			fh.log(exc);
		}
		finally {
			if(f.exists())
				info(prefPanel,"Preferences exported successfully");
			else
				error(prefPanel,"Preferences could not be exported");
		}
	}
	public void deleteAppSettings() {
		File dir = new File("app");
		if (!dir.exists() || dir.listFiles().length < 1) {
			error(prefPanel, "No files to delete");
			return;
		}
		boolean delete = JOptionPane
				.showConfirmDialog(null,
						"Are you sure you want to delete settings and app related files?") == JOptionPane.OK_OPTION;
		if (!delete) {
			error(prefPanel, "Operation cancelled");
			return;
		}
		try {
			FileUtils.deleteDirectory(dir);
		} catch (IOException exc) {
			fh.log(exc);
			error( "Could not delete app settings");
		}
		if (!dir.exists())
			info(prefPanel, "App settings deleted");
		else{
			String msg = "App settings could not be deleted";
			warning(msg);
			fh.log(msg);
		}
		loadPreferences();
	}
	public void prepareUI(){
		/**
		 * If there is no file saved by PreferencesManager
		 * abort any other operation
		 * */
		if(!exists())
		{
			/**
			 * No need to create a log file.Maybe the user wants to just run the
			 * app as it is without saving anything
			 * */
			return;
		}
		loadPreferences();
		updatePreview();
		applySettings();	
	}
}
