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
@SuppressWarnings({"static-access","serial","unused"})
public class PreferencesManager extends JFrame implements UIPreferences{
	private GUI f;
	private Color bgColor,fgColor;
	private Message msg = new Message();
	private JPanel prefPanel = new JPanel();
	private JSlider buttonSlider =new JSlider(JSlider.HORIZONTAL,1,100,10),labelSlider = new JSlider(JSlider.HORIZONTAL,1,100,10);
	private	CustomColorChooser colorChooser = new CustomColorChooser(null);
	private File settingsFile = new File("app\\settings.dat"),dir = new File("app");
	private Settings settings = new Settings();
	private DefaultComboBoxModel<String> fontModel = new DefaultComboBoxModel<>();
	private JComboBox<String> fontCombo = new JComboBox<>(fontModel);
	private JButton saveSettings=new JButton("Save settings"),applySettings = new JButton("Apply Settings"),loadSettings = new JButton("Load settings");
	private void init(){
		GraphicsEnvironment ee = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts=ee.getAllFonts();//get all system fonts
		for(Font f:fonts)
			fontModel.addElement(f.getFontName());
		saveSettings.addActionListener((e)->{
			savePreferences();
		});
		loadSettings.addActionListener((e)->{
			loadPreferences();
		});
		applySettings.addActionListener((e)->{
			for(JButton b:f.getButtons())
				System.out.println(b.getText());
		});
	}
	public PreferencesManager(GUI frame){
		super("Preferences");
		f=frame;
		init();
	    prefPanel.add(loadSettings);
		prefPanel.add(saveSettings);
		prefPanel.add(fontCombo);
		prefPanel.add(applySettings);
	   
		this.setContentPane(prefPanel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(1200,1080);
		this.pack();
		//this.setVisible(true);
	}
	public PreferencesManager(File sFile){
		if(sFile != null && sFile.isFile() && sFile.canWrite() && sFile.canRead())
			settingsFile = sFile;			
	}
	public void loadPreferences(){
		ObjectInputStream in;
		try{
			in = new ObjectInputStream(new FileInputStream(settingsFile));
			settings =(Settings)in.readObject();
		}
		catch(IOException | ClassNotFoundException e){
			
		}
	}

	public void savePreferences(){
		if(!dir.exists())
			dir.mkdirs();
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(settingsFile));
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
	
}
@SuppressWarnings({"serial"})
class Settings implements Serializable{

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
	public void setBtnFontSize(double btnFontSize) {
		fManager.setButtonSize( btnFontSize );
	}
	public void setLblFontSize(double lblFontSize) {
		fManager.setLabelSize(lblFontSize);
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
	public FontManager(String name,double btn,double lbl){
		fontName = name;
		btnFontSize = btn;
		lblFontSize = lbl;
	}
}