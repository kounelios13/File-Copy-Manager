package utilities;

import gui.CustomColorChooser;
import gui.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import messages.Message;
import net.miginfocom.swing.MigLayout;
@SuppressWarnings({"static-access","serial"})
public class PreferencesManager extends JFrame implements UIPreferences{
	private GUI f;
	private Color bgColor,fgColor;
	private Message msg = new Message();
	private JPanel prefPanel = new JPanel();
	private JSlider buttonSlider =new JSlider(JSlider.HORIZONTAL,1,100,10),labelSlider = new JSlider(JSlider.HORIZONTAL,1,100,10);
	private	CustomColorChooser colorChooser = new CustomColorChooser(null);
	private File settingsFile = new File("app\\settings.dat"),dir = new File("app");
	private Settings settings = new Settings();
	private DefaultComboBoxModel<String> fontModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> fontCombo = new JComboBox<String>(fontModel);
	private JButton saveSettings=new JButton("Save settings")
	,applySettings = new JButton("Apply Settings"),loadSettings = new JButton("Load settings"),
	chooseColors = new JButton("Choose colors");
	private  JLabel lblButtonFontSize = new JLabel("Button font size");
	private  JLabel lblLabelFontSize = new JLabel("Label font size");
	private  JButton btnSample = new JButton("Button Sample");
	private  JLabel lblSample = new JLabel("Label Sample");
	private boolean foundSettings = true;
	private void init(){
		GraphicsEnvironment ee = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts=ee.getAllFonts();//get all system fonts

		//System.out.println(fonts[0].getFontName());
		for(Font f:fonts)
			fontModel.addElement(f.getFontName());
		saveSettings.addActionListener((e)->{
			foundSettings = true;
			savePreferences();
		});
		loadSettings.addActionListener((e)->{
			foundSettings = true;
			loadPreferences();
		});
		applySettings.addActionListener((e)->{
			foundSettings = true;
			applySettings();
		});
		fontCombo.addActionListener((e)->{
			foundSettings = true;
			settings.setFontName((String) fontCombo.getSelectedItem());
			updatePreview();
		});
		chooseColors.addActionListener((e)->colorChooser.setVisible(true));
		buttonSlider.addChangeListener((e)->{
			foundSettings = true;
			int value = buttonSlider.getValue();
			settings.setBtnFontSize(value);
			btnSample.setFont(getFont().deriveFont((float)value));
			updatePreview();
			this.pack();
		});
		labelSlider.addChangeListener((e)->{
			foundSettings = true;
			int value = labelSlider.getValue();
			settings.setLblFontSize(value);
			lblSample.setFont(getFont().deriveFont((float)value));
			updatePreview();
			this.pack();
			
		});
	}
	/**
	 * @wbp.parser.constructor
	 */
	public PreferencesManager(GUI frame,File sFile){
		super("Preferences");
		f=frame;
		init();
		if(sFile != null && sFile.isFile() && sFile.canWrite() && sFile.canRead())
			settingsFile = sFile;
	    prefPanel.setLayout(new MigLayout("", "[97px][97px]", "[][][][][][23px][][]"));
	    prefPanel.add(fontCombo, "cell 0 0,growx,aligny center");
	    prefPanel.add(lblButtonFontSize, "cell 0 1,alignx center,aligny center");
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
	public void loadPreferences(){
		if(!settingsFile.exists())
		{
			foundSettings = false;
			return;
		}
		ObjectInputStream in;
		try{
			in = new ObjectInputStream(new FileInputStream(settingsFile));
			settings =(Settings)in.readObject();
			bgColor = settings.getBgColor();
			fgColor = settings.getFgColor();
			updatePreview();
		}
		catch(IOException | ClassNotFoundException e){
			msg.error(prefPanel, "Can't load preferences", "Error");
		}
		
	}

	public void savePreferences(){
		foundSettings = true;
		if(!dir.exists())
			dir.mkdirs();
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(settingsFile));
			settings.setBgColor(colorChooser.getBgColor());
			settings.setFgColor(colorChooser.getFgColor());
			out.writeObject(settings);
			out.close();
		}
		catch(FileNotFoundException e){
			try {
				settingsFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				msg.error(null,"Can't save preferences","Error");
			}
			finally{
				if(settingsFile.exists()){
					msg.info(null, "Created preferences file", "Status");
					//Created needed file.Re-execute to save
					savePreferences();
				}	
				applySettings();
			}
		}
		catch(IOException io){
			msg.error(null, "IOException occured", "IO");
			//io.printStackTrace();
		}
		this.setVisible(false);
	}
	public void editPreferences() {
		// TODO Auto-generated method stub
		this.setVisible(true);
	}
	public void applySettings(){
		if(!foundSettings)
			return;
		for (JButton b:f.getButtons()){
			b.setFont(settings.getButtonFont());
			bgColor = settings.getBgColor();
			fgColor = settings.getFgColor();
			if(bgColor != null || fgColor != null)
			{
				if(bgColor != null)
					b.setBackground(bgColor);
				if(fgColor != null)
					b.setForeground(fgColor);
			}						
		}
		for(JLabel lbl:f.getLabels()){
			lbl.setFont(settings.getLabelFont());
			if(bgColor != null)
				lbl.setBackground(bgColor);
		}
		f.pack();
	}
	public void updatePreview(){
		Font bFont=settings.getButtonFont();
		Font lFont=settings.getLabelFont();
		btnSample.setFont(bFont);
		lblSample.setFont(lFont);
		settings.setBgColor(colorChooser.getBgColor());
		settings.setFgColor(colorChooser.getFgColor());
		btnSample.setBackground(colorChooser.getBgColor());
		btnSample.setForeground(colorChooser.getFgColor());
		lblSample.setBackground(colorChooser.getBackground());
		this.pack();
	}
}
@SuppressWarnings({"serial"})
class Settings implements Serializable{
	private Color bg,fg;
	private FontManager fManager = new FontManager(null,0,0);
	public void applyButtonFont(ArrayList<JButton> btns){
		Font f = fManager.getBtnFont();
		for(JButton btn:btns)
			btn.setFont(f);
	}
	public void applyLabelFont(ArrayList<JLabel> lbls){
		Font f =fManager.getLblFont();
		for(JLabel lbl:lbls)
			lbl.setFont(f);
	}
	public String getFontName(){
		return fManager.getFontName();
	}
	public void setFontName(String n){
		fManager.setName(n);
	}
	public void setBtnFontSize(double btnFontSize) {
		fManager.setButtonSize( btnFontSize );
	}
	public void setLblFontSize(double lblFontSize) {
		fManager.setLabelSize(lblFontSize);
	}
	public Font getButtonFont(){
		return fManager.getBtnFont();
	}
	public Font getLabelFont(){
		return fManager.getLblFont();
	}
	public Color getBgColor(){
		return bg;
	}
	public Color getFgColor(){
		return fg;
	}
	public void setBgColor(Color c){
		bg=c;
	}
	public void setFgColor(Color c){
		fg=c;
	}
}
@SuppressWarnings("serial")
class FontManager implements Serializable{
	/**
	 *A class that holds any font relevant information
	 */
	private String fontName;
	private double btnFontSize,lblFontSize;
	public Font getBtnFont(){
		return new Font(fontName,Font.PLAIN,(int)btnFontSize);
	}
	public Font getLblFont(){
		return new Font(fontName,Font.PLAIN,(int)lblFontSize);
	}
	public void setButtonSize(double b){
		btnFontSize = b;
	}
	public void setLabelSize(double l){
		lblFontSize = l;
	}
	public void setName(String name){
		fontName = name;
	}
	public String getFontName(){
		return fontName;
	}
	public FontManager(String name,double btn,double lbl){
		fontName = name;
		btnFontSize = btn;
		lblFontSize = lbl;
	}
}